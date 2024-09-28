package com.example.appodontoprev

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MenuPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)

        val tipoUsuario = intent.getStringExtra("tipoUsuario")
        val iconPerson = findViewById<ImageView>(R.id.IconPerson)

        when (tipoUsuario) {
            "dentista" -> iconPerson.setImageResource(R.drawable.iconedentista)
            "atendente" -> iconPerson.setImageResource(R.drawable.iconeatendente)
        }
    }
}