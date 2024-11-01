package com.example.appodontoprev

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CadastroConcluidoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_concluido)

        val btnVolLogin = findViewById<Button>(R.id.btnVolLogin)
        val tipoUsuario = intent.getStringExtra("tipo_usuario")

        btnVolLogin.setOnClickListener {
            val intent = when (tipoUsuario) {
                "dentista" -> Intent(this, LoginDentistaActivity::class.java)
                "atendente" -> Intent(this, LoginAtendenteActivity::class.java)
                else -> Intent(this, MenuLoginActivity::class.java)
            }

            // Limpa a pilha de activities anteriores
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}