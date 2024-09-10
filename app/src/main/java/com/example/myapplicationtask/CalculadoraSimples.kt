package com.example.peojeto_calculadora

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var valorInicial: EditText
    private lateinit var valorDesconto: EditText
    private lateinit var botaoCalcular: Button
    private lateinit var botaoLimpar: Button
    private lateinit var mensagem: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        valorInicial = findViewById(R.id.valorInicial)
        valorDesconto = findViewById(R.id.valorDesconto)
        botaoCalcular = findViewById(R.id.botaoCalcular)
        botaoLimpar = findViewById(R.id.botaoLimpar)
        mensagem = findViewById<TextView>(R.id.mensagem)

    }

    fun botaoCalcular(view: View) {

        // Coleta os valores digitados pelo usuário e os transforma em Double
        val valor1 = valorInicial.text.toString().toDoubleOrNull() ?: return
        val valor2 = valorDesconto.text.toString().toDoubleOrNull() ?: return

        // Verifica se os valores são válidos
        if (valor1 <= 0 || valor2 < 0) {
            mensagem.text = "Valores inválidos, precisa fazer de novo!"
            return
        }

        val resultado = valor1 - valor2

        // Atualiza os textos da interface com os resultados
        mensagem.text = "Resultado: $resultado"

        // Desabilita os campos e botões após o cálculo
        valorInicial.isEnabled = false
        valorDesconto.isEnabled = false
        botaoCalcular.isEnabled = false

    }

    fun botaoLimpar(view: View){

        valorInicial.text.clear()
        valorDesconto.text.clear()
        mensagem.text = ""

        valorInicial.isEnabled = true
        valorDesconto.isEnabled = true
        botaoCalcular.isEnabled = true



    }
}
