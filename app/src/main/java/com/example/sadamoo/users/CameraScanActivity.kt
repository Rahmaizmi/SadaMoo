package com.example.sadamoo.users

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sadamoo.databinding.ActivityCameraScanBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraScanBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Setup UI
        setupUI()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun setupUI() {
        // Back button
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Capture button
        binding.fabCapture.setOnClickListener {
            capturePhoto()
        }

        // Gallery button (optional)
        binding.btnGallery.setOnClickListener {
            Toast.makeText(this, "Fitur galeri - Coming Soon!", Toast.LENGTH_SHORT).show()
        }

        // Flash toggle
        binding.btnFlash.setOnClickListener {
            toggleFlash()
        }

        // Switch camera button
        binding.btnSwitchCamera.setOnClickListener {
            Toast.makeText(this, "Switch camera - Coming Soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            try {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                // Preview
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

                // Image capture
                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                // Select back camera as default
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Toast.makeText(this, "Gagal membuka kamera: ${exc.message}", Toast.LENGTH_SHORT).show()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun capturePhoto() {
        showScanningAnimation()

        // Simulate ML processing with delay
        Handler(Looper.getMainLooper()).postDelayed({
            // Navigate to result activity with mock data
            val intent = Intent(this, ScanResultActivity::class.java).apply {
                putExtra("cattle_type", "Sapi Limosin")
                putExtra("disease_detected", "Lumpy Skin Disease (LSD)")
                putExtra("confidence_score", 87.5f)
                putExtra("severity", "Berat")
                putExtra("is_healthy", false)
            }
            startActivity(intent)
            finish()
        }, 3000) // 3 second delay for scanning effect
    }

    private fun showScanningAnimation() {
        binding.apply {
            // Hide capture button and show loading
            fabCapture.visibility = View.GONE
            btnBack.visibility = View.GONE
            btnGallery.visibility = View.GONE
            btnFlash.visibility = View.GONE
            btnSwitchCamera.visibility = View.GONE

            // Show scanning overlay
            layoutScanning.visibility = View.VISIBLE

            // Start scanning animation
            startScanningEffect()
        }
    }

    private fun startScanningEffect() {
        val scanningTexts = arrayOf(
            "Menganalisis gambar...",
            "Mendeteksi jenis sapi...",
            "Memeriksa kondisi kesehatan...",
            "Mengidentifikasi penyakit...",
            "Menghitung tingkat kepercayaan...",
            "Hampir selesai..."
        )

        var currentIndex = 0
        val handler = Handler(Looper.getMainLooper())

        val updateText = object : Runnable {
            override fun run() {
                if (currentIndex < scanningTexts.size) {
                    binding.tvScanningText.text = scanningTexts[currentIndex]
                    currentIndex++
                    handler.postDelayed(this, 500) // Update every 0.5 seconds
                }
            }
        }
        handler.post(updateText)

        // Animate scanning line
        animateScanningLine()
    }

    private fun animateScanningLine() {
        // Simple scanning line animation
        binding.viewScanLine.animate()
            .translationY(400f)
            .setDuration(2000)
            .withEndAction {
                binding.viewScanLine.animate()
                    .translationY(-400f)
                    .setDuration(1000)
                    .start()
            }
            .start()
    }

    private fun toggleFlash() {
        Toast.makeText(this, "Flash toggle - Coming Soon!", Toast.LENGTH_SHORT).show()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Izin kamera diperlukan untuk menggunakan fitur scan.", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
