package com.example.appodontoprev

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class AgendamentoConsutaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamento_consulta)

        val btnVolAgenConsul = findViewById<ImageButton>(R.id.btnVolAgenConsul)
        val btnAgenConsul = findViewById<Button>(R.id.btnAgenConsul)

        // Listener para voltar
        btnVolAgenConsul.setOnClickListener {
            val intent = Intent(this, ConsultasActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Listener para o botão de agendamento
        btnAgenConsul.setOnClickListener {
            // Exibe o alerta
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Agendamento")
            builder.setMessage("Agendamento de consulta em breve.")
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss() // Fecha o alerta ao clicar em OK
            }
            builder.show()
        }
    }
}
