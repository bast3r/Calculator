package by.dazerty.calculator

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel

class CalculationViewModel : ViewModel() {
    private lateinit var operation : (Double, Double) -> Double
    var operationView : String = ""
    public var operand1 : Double = Double.NaN
        get() { return field }
    public var operand2 : Double = Double.NaN
        get() { return field }

    val setOperator = { oper : (Double, Double) -> Double ->
        this.operation = oper
    }

    fun calculate() : Double {
        if (operation == null) {
            return Double.NaN
        }

        return  operation(operand1, operand2)
    }

//    fun setOperand1(value : Double) {
//        operand1 = value
//    }
//
//    fun setOperand2(value : Double) {
//        operand2 = value
//    }
}