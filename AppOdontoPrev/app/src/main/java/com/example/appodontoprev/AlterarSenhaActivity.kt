package com.example.appodontoprev

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AlterarSenhaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
    }
}