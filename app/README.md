# Documentação do Projeto: Aplicativo Android de Cálculo

## Visão Geral
Este aplicativo Android tem como objetivo calcular dois tipos de valores baseados em entradas fornecidas pelo usuário e exibir os resultados na interface do aplicativo. A aplicação é desenvolvida em Kotlin e usa o Android Studio.

## Configuração do Projeto
Criação do Projeto:

Abra o Android Studio.
Crie um novo projeto com uma Activity em branco.
Nomeie o projeto conforme desejado (por exemplo, MyApplicationTask).
Configuração do Layout:

O layout XML do projeto deve conter os seguintes componentes:
Dois EditText para entrada de valores (primeiroValor e segundoValor).
Dois Button para as ações de calcular e limpar (botaoCalcular e botaoLimpar).
Três TextView para mostrar mensagens e resultados (textoMensagem, mensagemErro, mensagemTabela).

```bash
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <EditText
        android:id="@+id/primeiroValor"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Primeiro Valor"
        android:inputType="numberDecimal" />

    <EditText
        android:id="@+id/segundoValor"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Segundo Valor"
        android:inputType="numberDecimal" />

    <Button
        android:id="@+id/botaoCalcular"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Calcular" />

    <Button
        android:id="@+id/botaoLimpar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Limpar" />

    <TextView
        android:id="@+id/textoMensagem"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="" />

    <TextView
        android:id="@+id/mensagemErro"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="" />

    <TextView
        android:id="@+id/mensagemTabela"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="" />
</LinearLayout>

```

# Estrutura do Código
## MainActivity.kt

```bash
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
        val contribuicao = calcularFuncao(valor1) // Calcula a contribuição baseada no primeiro valor
        val valorFinal = calcularFuncaoDois(valor1, valor2) // Calcula o valor final com base nos dois valores

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

```

# Funcionalidade e Explicação do Código

## Componentes da Interface
EditText:
primeiroValor: Campo de entrada para o primeiro valor.
segundoValor: Campo de entrada para o segundo valor.
Button:
botaoCalcular: Botão para iniciar o cálculo.
botaoLimpar: Botão para limpar os campos e resultados.
TextView:
textoMensagem: Mostra o resultado final do cálculo.
mensagemErro: Mostra mensagens de erro quando os valores são inválidos.
mensagemTabela: Exibe detalhes adicionais dos cálculos.

## Funções de Cálculo
calcularFuncao(valor: Double):
Calcula a contribuição com base em faixas de valor especificadas.
Utiliza a estrutura when para aplicar diferentes taxas de desconto para diferentes faixas de valor.
calcularFuncaoDois(valor1: Double, valor2: Double):
Calcula o valor final com base em duas entradas.
Aplica uma fórmula de cálculo que considera uma dedução percentual sobre cada valor.

## Funções Auxiliares
calcularValores():

Coleta valores dos campos de entrada, valida-os, realiza os cálculos e atualiza a interface com 
os resultados.
Desativa os campos e botões após o cálculo para evitar entradas duplicadas.
limparCampos():

Limpa todos os campos de entrada e mensagens.
Reativa os campos e botões, exceto o botão Limpar, que permanece desativado após a limpeza.

## Requisitos e Ferramentas
Android Studio: Ambiente de desenvolvimento integrado para desenvolvimento de aplicativos Android.
Kotlin: Linguagem de programação utilizada para escrever o código.
XML: Linguagem de marcação utilizada para definir o layout da interface do usuário.

## Passos para Configuração e Execução
Instale o Android Studio se ainda não estiver instalado.
Crie um novo projeto no Android Studio com uma Activity em branco.
Configure o layout XML com os componentes descritos.
Copie o código Kotlin fornecido para a MainActivity.kt.
Execute o aplicativo no emulador ou dispositivo Android para verificar a funcionalidade.
