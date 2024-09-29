package com.example.appodontoprev

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

class AnaliseConsultaActivity : AppCompatActivity() {

    private lateinit var imageViewFotoInicial: ImageView
    private lateinit var buttonFotoInicial: Button
    private lateinit var imageViewFotoFinal: ImageView
    private lateinit var buttonFotoFinal: Button
    private var photoUriInicial: Uri? = null
    private var photoUriFinal: Uri? = null

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            openCamera(isInitialPhoto)
        }
    }

    private val galleryPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            openGallery(isInitialPhoto)
        }
    }

    private val cameraLauncherInicial = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            photoUriInicial?.let { displayImage(it, true) }
        }
    }

    private val cameraLauncherFinal = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            photoUriFinal?.let { displayImage(it, false) }
        }
    }

    private val galleryLauncherInicial = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { displayImage(it, true) }
    }

    private val galleryLauncherFinal = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { displayImage(it, false) }
    }

    private var isInitialPhoto = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analise_consulta)

        imageViewFotoInicial = findViewById(R.id.imageViewFotoInicial)
        buttonFotoInicial = findViewById(R.id.FotoInicial)
        imageViewFotoFinal = findViewById(R.id.imageViewFotoFinal)
        buttonFotoFinal = findViewById(R.id.fotoFinal)

        buttonFotoInicial.setOnClickListener {
            isInitialPhoto = true
            showPhotoSourceDialog()
        }

        buttonFotoFinal.setOnClickListener {
            isInitialPhoto = false
            showPhotoSourceDialog()
        }
    }

    private fun showPhotoSourceDialog() {
        val options = arrayOf("Galeria", "Câmera")
        AlertDialog.Builder(this)
            .setTitle("Escolha uma opção")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> checkGalleryPermission()
                    1 -> checkCameraPermission()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun checkGalleryPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                openGallery(isInitialPhoto)
            }
            else -> {
                galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                openCamera(isInitialPhoto)
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openGallery(isInitial: Boolean) {
        if (isInitial) {
            galleryLauncherInicial.launch("image/*")
        } else {
            galleryLauncherFinal.launch("image/*")
        }
    }

    private fun openCamera(isInitial: Boolean) {
        val photoFile = File.createTempFile("IMG_", ".jpg", externalCacheDir)
        val uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
        if (isInitial) {
            photoUriInicial = uri
            cameraLauncherInicial.launch(uri)
        } else {
            photoUriFinal = uri
            cameraLauncherFinal.launch(uri)
        }
    }

    private fun displayImage(uri: Uri, isInitial: Boolean) {
        if (isInitial) {
            imageViewFotoInicial.setImageURI(uri)
            imageViewFotoInicial.visibility = View.VISIBLE
        } else {
            imageViewFotoFinal.setImageURI(uri)
            imageViewFotoFinal.visibility = View.VISIBLE
        }
    }
}