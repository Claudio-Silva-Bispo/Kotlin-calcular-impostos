package com.example.myapplicationtask

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Declaração dos componentes da interface do usuário
    private lateinit var primeiroValor: EditText
    private lateinit var segundoValor: EditText
    private lateinit var botaoCalcular: Button
    private lateinit var botaoLimpar: Button
    private lateinit var textoMensagem: TextView
    private lateinit var mensagemErro: TextView
    private lateinit var mensagemTabela: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inicializa os componentes com base nos IDs definidos no layout XML
        primeiroValor = findViewById(R.id.primeiroValor)
        segundoValor = findViewById(R.id.segundoValor)
        botaoCalcular = findViewById(R.id.botaoCalcular)
        botaoLimpar = findViewById(R.id.botaoLimpar)
        textoMensagem = findViewById(R.id.textoMensagem)
        mensagemErro = findViewById(R.id.mensagemErro)
        mensagemTabela = findViewById(R.id.mensagemTabela)

        // Define o que acontece quando o botão "Calcular" é clicado
        botaoCalcular.setOnClickListener {
            calcularValores()
        }

        // Define o que acontece quando o botão "Limpar" é clicado
        botaoLimpar.setOnClickListener {
            limparCampos()
        }
    }

    private fun calcularValores() {
        // Coleta os valores digitados pelo usuário e os transforma em Double
        val valor1 = primeiroValor.text.toString().toDoubleOrNull() ?: return
        val valor2 = segundoValor.text.toString().toDoubleOrNull() ?: return

        // Verifica se os valores são válidos
        if (valor1 <= 0 || valor2 < 0) {
            textoMensagem.text = "Valores inválidos, precisa fazer de novo!"
            return
        }

        // Lógica de cálculo
        val contribuicao = calcularFuncao(valor1) // Calcula o valor com base no primeiro input
        val valorFinal = calcularFuncaoDois(valor1, valor2) // Calcula o valor final com base nos dois inputs

        // Atualiza os textos da interface com os resultados
        textoMensagem.text = "Resultado: $valorFinal"
        mensagemErro.text = "Nenhum erro."
        mensagemTabela.text = "Contribuição: $contribuicao\nValor Final: $valorFinal"

        // Desabilita os campos e botões após o cálculo
        primeiroValor.isEnabled = false
        segundoValor.isEnabled = false
        botaoCalcular.isEnabled = false
        botaoLimpar.isEnabled = false
    }

    private fun limparCampos() {
        // Limpa os valores dos campos de entrada e das mensagens
        primeiroValor.text.clear()
        segundoValor.text.clear()
        textoMensagem.text = ""
        mensagemErro.text = ""
        mensagemTabela.text = ""

        // Reativa os campos e botões
        primeiroValor.isEnabled = true
        segundoValor.isEnabled = true
        botaoCalcular.isEnabled = true
        botaoLimpar.isEnabled = false
    }

    // Função para calcular a contribuição baseada em faixas de valores
    private fun calcularFuncao(valor: Double): Double {
        var desconto = 0.0
        var valorInicial = valor

        // Calcula a contribuição de acordo com as faixas definidas
        when {
            valorInicial > 3960 -> {
                desconto += (valorInicial - 3960) * 0.14
                valorInicial = 3960.0
            }
            valorInicial > 2640 -> {
                desconto += (valorInicial - 2640) * 0.12
                valorInicial = 2640.0
            }
            valorInicial > 1320 -> {
                desconto += (valorInicial - 1320) * 0.09
                valorInicial = 1320.0
            }
            else -> {
                desconto += valorInicial * 0.075
                valorInicial = 0.0
            }
        }

        return desconto
    }

    // Função para calcular um valor final com base em duas entradas
    private fun calcularFuncaoDois(valor1: Double, valor2: Double): Double {
        // Verifica se os valores são válidos
        if (valor1 <= 0 || valor2 <= 0) {
            return 0.0
        }

        // Aplica uma fórmula para calcular o valor final
        val resultado = valor1 + valor2 - (valor1 * 0.1) - (valor2 * 0.05)

        return resultado
    }
}
