package com.example.appodontoprev

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MenuLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_login)

        // Find the ImageViews
        val botaoAtendente = findViewById<ImageView>(R.id.botaoAtendente)
        val botaoDentista = findViewById<ImageView>(R.id.botaoDentista)

        // Set click listener for Atendente
        botaoAtendente.setOnClickListener {
            val intent = Intent(this, LoginAtendenteActivity::class.java)
            startActivity(intent)
        }

        // Set click listener for Dentista
        botaoDentista.setOnClickListener {
            val intent = Intent(this, LoginDentistaActivity::class.java)
            startActivity(intent)
        }
    }
}