package by.dazerty.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import org.w3c.dom.Text

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val calculationViewModel : CalculationViewModel by lazy {
        ViewModelProvider(this).get(CalculationViewModel::class.java)
    }

    private val plusOperation = { o1 : Double, o2 : Double -> o1 + o2 }
    private val minusOperation = { o1 : Double, o2 : Double -> o1 - o2 }
    private val divideOperation = { o1 : Double, o2 : Double ->
        if (o2 == 0.0) {
            Toast.makeText(this, "Can not divide by zero!", Toast.LENGTH_SHORT).show()
            Double.NaN
        }
        o1 / o2
    }
    private val multiplyOperation = { o1 : Double, o2 : Double -> o1 * o2 }

    // сохранение состояния
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
//        outState.putDouble("operand1", calculationViewModel.operand1)
//        outState.putDouble("operand2", calculationViewModel.operand2);
//        outState.putString("operator", calculationViewModel.operationView);

        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

//        calculationViewModel.operand1 = savedInstanceState.getDouble("operand1")
//        calculationViewModel.operand2 = savedInstanceState.getDouble("operand2")
//        calculationViewModel.operationView = savedInstanceState.getString("operator").toString()
//
//        currentOperand.text = calculationViewModel.operand1.toString() + calculationViewModel.operationView + calculationViewModel.operand2.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resultTextView : TextView = findViewById(R.id.textView_result_string)
        val currentOperand : TextView = findViewById(R.id.textResult)

        for (i in 0 .. 9) {
            val id = resources.getIdentifier("button_" + i, "id", packageName)
            val btnNumber : Button? = findViewById(id)
            btnNumber?.setOnClickListener { view : View? ->
                currentOperand.text = currentOperand.text.toString() + i
            }
        }

        val clickListener = { view: View ->
            val value : String = currentOperand.text.toString()

            var operation : String
            when (view.id) {
                R.id.button_plus -> {
                    operation = "+"
                    calculationViewModel.setOperator(plusOperation)
                }
                R.id.button_minus -> {
                    operation = "-"
                    calculationViewModel.setOperator(minusOperation)
                }
                R.id.button_divide -> {
                    operation = "/"
                    calculationViewModel.setOperator(divideOperation)
                }
                R.id.button_multiply -> {
                    operation = "*"
                    calculationViewModel.setOperator(multiplyOperation)
                }
                else -> {
                    operation = "+"
                    calculationViewModel.setOperator(plusOperation)
                }
            }
            currentOperand.text = value + operation
        }

        val btnPlus : Button = findViewById(R.id.button_plus)
        btnPlus.setOnClickListener(clickListener)

        val btnMinus : Button = findViewById(R.id.button_minus)
        btnMinus.setOnClickListener(clickListener)

        val btnDivide : Button = findViewById(R.id.button_divide)
        btnDivide.setOnClickListener(clickListener)

        val btnMultiply : Button = findViewById(R.id.button_multiply)
        btnMultiply.setOnClickListener(clickListener)

        val resultBtn : Button = findViewById(R.id.button_result)
        resultBtn.setOnClickListener { view : View ->
            var value : String = currentOperand.text.toString()

            if (value.length > 3 && value.contains(Regex("[\\+\\-\\*\\/]"))) {

                value = value.replace(Regex("[\\+\\-\\*\\/]")) {
                    calculationViewModel.operationView = it.value
                    calculationViewModel.setOperator(when (it.value) {
                        "+" -> plusOperation
                        "*" -> multiplyOperation
                        "-" -> minusOperation
                        "/" -> divideOperation
                        else -> plusOperation
                    })
                    "|"
                }

                val (op1, op2) = value.split("|")
                calculationViewModel.operand1 = op1.toDouble()
                calculationViewModel.operand2 = op2.toDouble()

                resultTextView.text = calculationViewModel.calculate().toString()
            }
        }

        val btnDel : Button = findViewById(R.id.button_delete)
        btnDel.setOnClickListener {
            resultTextView.text = "0"
            if (currentOperand.text.length > 0)
                currentOperand.text = currentOperand.text.subSequence(0, currentOperand.text.length - 1)
        }
    }
}