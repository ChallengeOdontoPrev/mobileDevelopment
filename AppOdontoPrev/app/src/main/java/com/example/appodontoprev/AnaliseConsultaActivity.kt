package com.example.appodontoprev

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class AnaliseConsultaActivity : AppCompatActivity() {
    private lateinit var imageViewFotoInicial: ImageView
    private lateinit var buttonFotoInicial: Button
    private lateinit var imageViewFotoFinal: ImageView
    private lateinit var buttonFotoFinal: Button
    private lateinit var checkBoxInicial: CheckBox
    private lateinit var checkBoxFinal: CheckBox
    private lateinit var btnFinalConsul: Button
    private var photoUriInicial: Uri? = null
    private var photoUriFinal: Uri? = null
    private var tipoUsuario: String = ""
    private var fotoInicialEnviada = false
    private var fotoFinalEnviada = false
    private var isInitialPhoto = true

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
            photoUriInicial?.let {
                displayImage(it, true)
                updateCheckBox(checkBoxInicial)
                fotoInicialEnviada = true
                enableFotoFinalButton()
            }
        }
    }

    private val cameraLauncherFinal = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            photoUriFinal?.let {
                displayImage(it, false)
                updateCheckBox(checkBoxFinal)
                fotoFinalEnviada = true
            }
        }
    }

    private val galleryLauncherInicial = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            displayImage(it, true)
            updateCheckBox(checkBoxInicial)
            fotoInicialEnviada = true
            enableFotoFinalButton()
        }
    }

    private val galleryLauncherFinal = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            displayImage(it, false)
            updateCheckBox(checkBoxFinal)
            fotoFinalEnviada = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analise_consulta)

        // Recuperar dados da consulta do Intent
        val nomePaciente = intent.getStringExtra("patientName") ?: ""
        val dataConsulta = intent.getStringExtra("appointmentDate") ?: ""
        val horaConsulta = intent.getStringExtra("appointmentTime") ?: ""
        val procedimento = intent.getStringExtra("procedureType") ?: ""
        tipoUsuario = intent.getStringExtra("tipoUsuario") ?: ""

        // Atualizar TextViews com os dados
        findViewById<TextView>(R.id.nomePaciente).text = nomePaciente
        findViewById<TextView>(R.id.dataConsulta).text = formatDate(dataConsulta)
        findViewById<TextView>(R.id.horarioConsulta).text = horaConsulta
        findViewById<TextView>(R.id.procedimentoConsulta).text = procedimento

        initializeViews()
        setupListeners()
        setupInitialState()
    }

    private fun initializeViews() {
        imageViewFotoInicial = findViewById(R.id.imageViewFotoInicial)
        buttonFotoInicial = findViewById(R.id.FotoInicial)
        imageViewFotoFinal = findViewById(R.id.imageViewFotoFinal)
        buttonFotoFinal = findViewById(R.id.fotoFinal)
        checkBoxInicial = findViewById(R.id.checkInicial)
        checkBoxFinal = findViewById(R.id.checkFinal)
        btnFinalConsul = findViewById(R.id.btnFinalConsul)
    }

    private fun setupListeners() {
        buttonFotoInicial.setOnClickListener {
            isInitialPhoto = true
            showPhotoSourceDialog()
        }

        buttonFotoFinal.setOnClickListener {
            if (fotoInicialEnviada) {
                isInitialPhoto = false
                showPhotoSourceDialog()
            } else {
                showFotoInicialAlertDialog()
            }
        }

        btnFinalConsul.setOnClickListener {
            if (fotoInicialEnviada && fotoFinalEnviada) {
                showFinalizationDialog()
            } else {
                showAlertDialog()
            }
        }

        findViewById<ImageView>(R.id.btnVolConsuPac)?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupInitialState() {
        checkBoxInicial.isClickable = false
        checkBoxFinal.isClickable = false
        disableFotoFinalButton()
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

    private fun updateCheckBox(checkBox: CheckBox) {
        checkBox.isChecked = true
        checkBox.buttonTintList = ColorStateList.valueOf(Color.GREEN)
    }

    private fun disableFotoFinalButton() {
        buttonFotoFinal.isEnabled = false
        buttonFotoFinal.setBackgroundColor(Color.GRAY)
    }

    private fun enableFotoFinalButton() {
        buttonFotoFinal.isEnabled = true
        buttonFotoFinal.setBackgroundColor(Color.parseColor("#FF6052"))
    }

    private fun showFotoInicialAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("ATENÇÃO")
            .setMessage("A foto inicial precisa ser enviada primeiro!")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("ATENÇÃO")
            .setMessage("Não é possível prosseguir sem enviar as fotos do início e do fim do procedimento!")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showFinalizationDialog() {
        AlertDialog.Builder(this)
            .setMessage("Obrigado por colaborar conosco, suas fotos foram analisadas e aprovadas")
            .setPositiveButton("OK") { _, _ ->
                navigateToMainMenu()
            }
            .setCancelable(false)
            .show()
    }

    private fun navigateToMainMenu() {
        val intent = Intent(this, MenuPrincipalActivity::class.java)
        intent.putExtra("tipoUsuario", tipoUsuario)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun formatDate(dateStr: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            val date = inputFormat.parse(dateStr)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            dateStr
        }
    }
}