package com.example.appodontoprev


import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class CadastroDentistaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro_dentista)

        val spinner: Spinner = findViewById(R.id.spinner)

        // Cria um adapter usando o array de strings
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.spinner_options, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val botVolLoginDent = findViewById<TextView>(R.id.botVolLoginDent)

        botVolLoginDent.setOnClickListener {
            val intent = Intent(this, LoginDentistaActivity::class.java)
            startActivity(intent)
            finish()
        }

        val viewVolLogDent = findViewById<TextView>(R.id.viewVolLogDent)

        viewVolLogDent.setOnClickListener {
            val intent = Intent(this, LoginDentistaActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}