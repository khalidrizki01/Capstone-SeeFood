package com.example.capstone_seefood

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.os.PersistableBundle
import android.view.Surface
import android.view.TextureView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import java.io.File
import java.io.FileOutputStream

class ScanActivity : AppCompatActivity() {
    lateinit var capReq: CaptureRequest.Builder
    lateinit var handler: Handler
    lateinit var handlerThread: Thread
    lateinit var cameraManager: CameraManager
    lateinit var textureView: TextureView
    lateinit var cameraCaptureSession: CameraCaptureSession
    lateinit var cameraDevice: CameraDevice
    lateinit var captureRequest: CaptureRequest
    lateinit var imageReader: ImageReader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        textureView = findViewById(R.id.previewscan)
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler((handlerThread as HandlerThread).looper)

        imageReader = ImageReader.newInstance(1080,1920, ImageFormat.JPEG,1)
        imageReader.setOnImageAvailableListener(object: ImageReader.OnImageAvailableListener{
            override fun onImageAvailable(pO: ImageReader?) {
                var image= pO?.acquireLatestImage()
                var buffer = image!!.planes[0].buffer
                var bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)

                var file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "scan.jpeg")
                var opStream = FileOutputStream(file)

                opStream.write(bytes)
                opStream.close()

                image.close()
                Toast.makeText(this@ScanActivity,"Makanan Scan", Toast.LENGTH_SHORT).show()
            }
        },handler)

        findViewById<Button>(R.id.btnscanmakanan).apply{
            setOnClickListener {
               capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                capReq.addTarget(imageReader.surface)
                cameraCaptureSession.capture(capReq.build(),null,null)
            }
        }
        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                open_camera()
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                TODO("Not yet implemented")
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun open_camera() {
        cameraManager.openCamera(
            cameraManager.cameraIdList[0],
            object : CameraDevice.StateCallback() {
                override fun onOpened(pO: CameraDevice) {
                    cameraDevice = pO
                    capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                    var surface = Surface(textureView.surfaceTexture)
                    capReq.addTarget(surface)

                    cameraDevice.createCaptureSession(listOf(surface,imageReader.surface), object:
                        CameraCaptureSession.StateCallback() {
                        override fun onConfigured(pO: CameraCaptureSession) {
                            cameraCaptureSession = pO
                            cameraCaptureSession.setRepeatingRequest(capReq.build(),null,null)
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {
                            TODO("Not yet implemented")
                        }
                    },handler)
                }

                override fun onClosed(camera: CameraDevice) {
                    super.onClosed(camera)
                }

                override fun onDisconnected(camera: CameraDevice) {
                    TODO("Not yet implemented")
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    TODO("Not yet implemented")
                }
            },
            handler
        )
    }
}
