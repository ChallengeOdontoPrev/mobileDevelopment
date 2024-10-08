package com.example.appodontoprev


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ConfiguracoesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_configuracoes)

        val btnVoltarConfig = findViewById<ImageView>(R.id.btnVoltarConfig)
        val tipoUsuario = intent.getStringExtra("tipoUsuario")

        btnVoltarConfig.setOnClickListener {
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            intent.putExtra("tipoUsuario", tipoUsuario)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}