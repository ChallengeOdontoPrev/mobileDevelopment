package com.example.appodontoprev

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class AlterarSenhaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alterar_senha)

        val origem = intent.getStringExtra("origem")

        val botVolLoginAll = findViewById<Button>(R.id.botVolLoginAll)
        botVolLoginAll.setOnClickListener {
            val intent = when (origem) {
                "dentista" -> Intent(this, LoginDentistaActivity::class.java)
                "atendente" -> Intent(this, LoginAtendenteActivity::class.java)
                else -> Intent(this, MenuLoginActivity::class.java) // Fallback
            }
            startActivity(intent)
            finish()
        }

        val btnEnviar = findViewById<Button>(R.id.btnEntrar)
        btnEnviar.setOnClickListener {
            showAlert()
        }
    }

    private fun showAlert() {
        AlertDialog.Builder(this)
            .setTitle("Aviso")
            .setMessage("Ainda não é possível alterar a senha, função disponível em breve!")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}