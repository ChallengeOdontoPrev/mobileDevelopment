package com.example.appodontoprev


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RedefinirSenhaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_redifinir_senha)

        val alterarSenha = findViewById<Button>(R.id.alterarSenha)

        alterarSenha.setOnClickListener {
            val intent = Intent(this, MenuLoginActivity::class.java)
            startActivity(intent)
        }


    }
}