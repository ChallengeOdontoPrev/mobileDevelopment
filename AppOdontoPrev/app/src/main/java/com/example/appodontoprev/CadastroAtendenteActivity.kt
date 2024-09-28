package com.example.appodontoprev

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class CadastroAtendenteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.cadastro_atendente_activity)

        // Configurar o Spinner
        val spinner: Spinner = findViewById(R.id.spinner)

        // Cria um adapter usando o array de strings
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.spinner_options, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Encontrar o botão Voltar
        val botaoVoltar = findViewById<Button>(R.id.botaoVoltarCadastro)
        val botaoVoltarLogin = findViewById<TextView>(R.id.botaoVoltarLogin)


        // Configurar o OnClickListener para o botão Voltar
        botaoVoltar.setOnClickListener {
            val intent = Intent(this, LoginAtendenteActivity::class.java)
            startActivity(intent)
            finish()
        }
        botaoVoltarLogin.setOnClickListener {
            val intent = Intent(this, LoginAtendenteActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}