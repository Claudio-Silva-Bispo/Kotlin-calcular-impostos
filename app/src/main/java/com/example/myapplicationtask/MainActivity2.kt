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

    // Variaveis para os campos de input
    private lateinit var editTextSalario: EditText
    private lateinit var editTextQtdDep: EditText

    // Inserir variaveis para os botões
    private lateinit var buttonCalcular: Button
    private lateinit var buttonLimpar: Button

    // Mensagens no app
    private lateinit var textViewResultImp: TextView
    private lateinit var textViewResultInss: TextView
    private lateinit var textViewMsgError: TextView
    private lateinit var textViewSalarioLiq: TextView
    private lateinit var textViewAliqImposto: TextView
    private lateinit var textViewAliqInss: TextView
    private lateinit var textViewTetoCont: TextView



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // campos para inserir dados
        editTextSalario = findViewById(R.id.editTextSalario)
        editTextQtdDep = findViewById(R.id.editTextQtdDep)

        // botões da tela
        buttonCalcular = findViewById(R.id.buttonCalcular)
        buttonLimpar = findViewById(R.id.buttonLimpar)

        // Mensagens, seja de erro, com info ou demais
        textViewResultImp = findViewById(R.id.textViewResultImp)
        textViewResultInss = findViewById(R.id.textViewResultInss)
        textViewSalarioLiq = findViewById(R.id.textViewSalarioLiq)
        textViewMsgError = findViewById(R.id.textViewMsgError)
        textViewAliqImposto = findViewById(R.id.textViewAliqImposto)
        textViewAliqInss = findViewById(R.id.textViewAliqInss)
        textViewTetoCont = findViewById(R.id.textViewTetoCont)


    }

    fun botaoCalcular(view: View) {

        // Coleta os valores digitados pelo usuário e os transforma em Double. Váriaveis que não podem mudar
        val salarioBruto = editTextSalario.text.toString().toDoubleOrNull() ?: return
        val qtdDependentes = editTextQtdDep.text.toString().toDoubleOrNull() ?: return

        // Criar as próximas váriaveis para o cáculo do inss, irpr e salário liquido.
        var contruibuicaoInss = calcularInss(salarioBruto)
        var valorInicialBase = salarioBruto - contruibuicaoInss - (qtdDependentes * 189.59)
        var contribuicaoIrpf = calcularImposto(valorInicialBase)
        var salarioLiquido = salarioBruto - contruibuicaoInss - contribuicaoIrpf

        // Verifica se os valores são válidos
        if (salarioBruto <= 0 || qtdDependentes < 0 || salarioBruto == null || qtdDependentes == null || qtdDependentes > 10) {
            textViewMsgError.text = "Valores inválidos. Lembrabdo que não pode ter dependentes maior que 10 pessoas.!"
            return
        }

        // Desabilita os campos e botões após o cálculo. Só liberar eles depois de clicar em limpar.
        editTextSalario.isEnabled = false
        editTextQtdDep.isEnabled = false
        buttonCalcular.isEnabled = false

        if (contribuicaoIrpf.toInt() == 0)
        {
            textViewResultImp.text = "Isento do imposto!"
        } else
        {
            textViewResultImp.text = "Imposto de renda $contribuicaoIrpf"
            textViewTetoCont.text = "Inss para contribuição $contruibuicaoInss"
            textViewSalarioLiq.text = "Salário Líquido $salarioLiquido"
        }
    }

    fun botaoLimpar(view: View){

        // Limpar os campos para inserir novos dados
        editTextSalario.text.clear()
        editTextQtdDep.text.clear()

        // Limpar as mensagens
        textViewResultImp.text = ""
        textViewMsgError.text = ""
        textViewTetoCont.text = ""
        textViewSalarioLiq.text = ""
        textViewAliqInss.text = ""
        textViewAliqImposto.text = ""
        textViewResultInss.text = ""

        // Quando o usuáario clicar em limpar, ai as opções estarão disponíveis
        editTextSalario.isEnabled = true
        editTextQtdDep.isEnabled = true
        textViewResultImp.isEnabled = true
        buttonCalcular.isEnabled = true
        buttonLimpar.isEnabled = true



    }

    fun calcularInss(salarioBruto: Double): Int
    {
        var Contribuicaoinss = 0
        var tetoContribuicao = 7786.02
        var salarioInicialBase = if (salarioBruto > tetoContribuicao)
        {
            tetoContribuicao
        }else salarioBruto

        // faixas de pagamento inicial, vamos iniciar zeradas
        var faixaUm: Double = 0.0
        var faixaDois: Double = 0.0
        var faixaTres: Double = 0.0
        var faixaQuatro: Double = 0.0

        // Definir Teto
        var salarioTetoUm: Double = 1412.00
        var salarioTetoDois: Double = 2666.68
        var salarioTetoTres: Double = 4000.00

        // Definir %
        var porcentagemUm: Double = 0.075
        var porcentagemDois: Double = 0.09
        var porcentagemTres: Double = 0.12
        var porcentagemQuatro: Double = 0.14

        if (salarioBruto > tetoContribuicao)
        {
            textViewTetoCont.text = "Salário mais alto que o teto"
        }

        if (salarioInicialBase <= salarioTetoUm)
        {
            faixaUm = salarioInicialBase * porcentagemUm
        }else if (salarioInicialBase <= salarioTetoDois)
        {
            faixaUm = salarioTetoUm * porcentagemUm
            faixaDois = (salarioInicialBase - salarioTetoUm) * porcentagemDois
        }else if (salarioInicialBase <= salarioTetoTres)
        {
            faixaUm = salarioTetoUm * porcentagemUm
            faixaDois = (salarioInicialBase - salarioTetoUm) * porcentagemDois
            faixaTres = (salarioInicialBase - salarioTetoDois) * porcentagemTres
        }else
        {
            faixaUm = salarioTetoUm * porcentagemUm
            faixaDois = (salarioInicialBase - salarioTetoUm) * porcentagemDois
            faixaTres = (salarioInicialBase - salarioTetoDois) * porcentagemTres
            faixaQuatro = (salarioInicialBase - salarioTetoTres) * porcentagemQuatro
        }

        Contribuicaoinss = (faixaUm + faixaDois + faixaTres + faixaQuatro).toInt()

        return Contribuicaoinss
    }

    fun calcularImposto(salarioBruto: Double): Double
    {

        // Iniciar varivaies com faixas e porcentagens
        var contribuicaoIrpf:Double
        var faixaUmIrpf: Double = 2259.20
        var faixaDoisIrpf: Double = 2826.65
        var faixaTresIrpf: Double = 3751.05
        var faixaQuatroIrpf: Double = 4664.68

        // Definir %
        var porcentagemUm: Double = 0.075
        var porcentagemDois: Double = 0.15
        var porcentagemTres: Double = 0.225
        var porcentagemQuatro: Double = 0.275

        // Definir valores de contruibuição
        var valorUm: Double = 0.0
        var valorDois: Double = 169.44
        var valorTres: Double = 381.44
        var valorQuatro: Double = 662.77
        var valorCinco: Double = 896.0

        if (salarioBruto <= faixaUmIrpf)
        {
            contribuicaoIrpf = valorUm
        }else if(salarioBruto <= faixaDoisIrpf)
        {
            contribuicaoIrpf = (salarioBruto * porcentagemUm) - valorDois
        }else if (salarioBruto <= faixaTresIrpf)
        {
            contribuicaoIrpf = (salarioBruto * porcentagemDois) - valorTres
        }else if(salarioBruto <= faixaQuatroIrpf)
        {
            contribuicaoIrpf = (salarioBruto * porcentagemTres) - valorQuatro
        }else
        {
            contribuicaoIrpf = (salarioBruto * porcentagemQuatro) - valorCinco
        }

        return contribuicaoIrpf
    }
}

