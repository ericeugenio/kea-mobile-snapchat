package com.example.snapchat.utilities.hardware

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.*
import androidx.camera.core.CameraSelector.LensFacing
import androidx.camera.core.ImageCapture.FlashMode
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.concurrent.futures.await
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.launch
import java.io.File

interface PhotoListener {
    fun onInit(lensInfo: MutableMap<Int, CameraInfo>)
    fun onSuccess(imageUri: Uri?)
    fun onError()
}

data class CameraUiState(
    @FlashMode val flashMode: Int = ImageCapture.FLASH_MODE_OFF,
    @LensFacing val lens: Int = CameraSelector.LENS_FACING_BACK,
    val lensInfo: MutableMap<Int, CameraInfo> = mutableMapOf()
) {
    fun hasFlash(): Boolean = lensInfo[lens]?.hasFlashUnit() == true
    fun isFlashOn(): Boolean = flashMode == ImageCapture.FLASH_MODE_ON
    fun hasDualCamera(): Boolean = lensInfo.size > 1
    fun isBackCamera(): Boolean = lens == CameraSelector.LENS_FACING_BACK
}

class PhotoManager private constructor(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private var listener: PhotoListener? = null
) : LifecycleEventObserver {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var previewUseCase: Preview
    private lateinit var imageCaptureUseCase: ImageCapture

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    listener?.onInit(cameraProvider.getLensInfo(source))
                }, ContextCompat.getMainExecutor(context))
            }
            else -> Unit
        }
    }

    fun addListener(listener: PhotoListener) {
        this.listener = listener
    }

    fun removeListener() {
        this.listener = null
    }

    fun getPreview(cameraUiState: CameraUiState, cameraPreview: PreviewView = previewView()) : PreviewView {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                val cameraProvider = cameraProviderFuture.await()
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(cameraUiState.lens)
                    .build()

                previewUseCase = Preview.Builder()
                    .build()
                    .apply {
                        setSurfaceProvider(cameraPreview.surfaceProvider)
                    }
                imageCaptureUseCase = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .setFlashMode(cameraUiState.flashMode)
                    .build()

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        previewUseCase,
                        imageCaptureUseCase
                    )
                } catch(e: Exception) {
                    e.message?.let { Log.e("CAMERA", it) }
                }
            }
        }

        return cameraPreview
    }

    fun captureImage() {
        val file = File.createTempFile("temp", "jpg")
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file)
            .build()

        imageCaptureUseCase.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException)
                {
                    listener?.onError()
                }
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    listener?.onSuccess(outputFileResults.savedUri)
                }
            }
        )
    }

    private fun previewView() = PreviewView(context).apply {
        scaleType = PreviewView.ScaleType.FILL_CENTER
        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
    }

    data class Builder(
        private val context: Context,
        private val lifecycle: LifecycleOwner,
        private var listener: PhotoListener? = null
    ) {
        fun addListener(listener: PhotoListener) = apply { this.listener = listener }
        fun build() = PhotoManager(context, lifecycle, listener)
    }
}

fun ProcessCameraProvider.getLensInfo(source: LifecycleOwner): MutableMap<Int, CameraInfo> {
    val cameraInfo = HashMap<Int, CameraInfo>()
    val availableLenses = arrayOf(CameraSelector.LENS_FACING_BACK, CameraSelector.LENS_FACING_FRONT)

    availableLenses.forEach { lens ->
        val camera: Camera
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lens)
            .build()

        if (hasCamera(cameraSelector)) {
            try {
                unbindAll()
                camera = bindToLifecycle(source, cameraSelector)
                cameraInfo[lens] = camera.cameraInfo
            } catch(e: Exception) {
                e.message?.let { Log.e("CAMERA", it) }
            }
        }
    }

    return cameraInfo
}

