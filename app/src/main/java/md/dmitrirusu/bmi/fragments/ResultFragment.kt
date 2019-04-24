package md.dmitrirusu.bmi.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_result.*
import md.dmitrirusu.bmi.R
import java.math.BigDecimal
import java.math.RoundingMode


private const val PARAM_NAME = "userName"
private const val PARAM_HEIGHT = "height"
private const val PARAM_WEIGHT = "weight"
private const val PARAM_GENDER = "gender"

private const val MIN_NORMAL_THRESHOLD = 18.5
private const val MAX_NORMAL_THRESHOLD = 25

class ResultFragment : Fragment() {

    private var weigth: Double = 50.0
    private var heigth: Double = 150.0
    private var gender: String? = "Male"
    private var userName: String? = ""

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userName = it.getString(PARAM_NAME)
            gender = it.getString(PARAM_GENDER)
            heigth = it.getDouble(PARAM_HEIGHT)
            weigth = it.getDouble(PARAM_WEIGHT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(md.dmitrirusu.bmi.R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val massIndex = calculateMassIndex(heigth, weigth)

        mass_index_whole_part.text = getWholePart(massIndex)
        mass_index_decimal_part.text = getDecimalPart(massIndex)

        when {
            massIndexInNormalRange(massIndex) -> result_message.text = getString(R.string.bmi_normal, userName)
            massIndexBelowNormal(massIndex) -> result_message.text = getString(R.string.bmi_below_normal, userName)
            massIndexAboveNormal(massIndex) -> result_message.text = getString(R.string.bmi_above_normal, userName)
        }

        btn_share.setOnClickListener { listener?.onShareButtonPressed() }
        btn_rate.setOnClickListener { listener?.onRateButtonPressed() }
    }

    private fun getWholePart(massIndex: Double): String {
        val (doubleAsString, indexOfDecimal) = getMassIndexAsString(massIndex)

        return doubleAsString.substring(0, indexOfDecimal)
    }

    private fun getDecimalPart(massIndex: Double): String {
        val (doubleAsString, indexOfDecimal) = getMassIndexAsString(massIndex)

        return doubleAsString.substring(indexOfDecimal)
    }

    private fun getMassIndexAsString(massIndex: Double): Pair<String, Int> {
        val doubleAsString = BigDecimal(massIndex).setScale(2, RoundingMode.UP).toString()
        val indexOfDecimal = doubleAsString.indexOf(".")
        return Pair(doubleAsString, indexOfDecimal)
    }

    private fun massIndexAboveNormal(massIndex: Double) = massIndex > MAX_NORMAL_THRESHOLD

    private fun massIndexBelowNormal(massIndex: Double) = massIndex < MIN_NORMAL_THRESHOLD

    private fun massIndexInNormalRange(massIndex: Double) =
        massIndex > MIN_NORMAL_THRESHOLD && massIndex < MAX_NORMAL_THRESHOLD

    private fun calculateMassIndex(height: Double, weight: Double): Double {
        // 'height / 100' because we have height in centimeters, but we need in meters
        return weight / ((height / 100) * (height / 100))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {

        fun onShareButtonPressed()

        fun onRateButtonPressed()
    }

    companion object {
        @JvmStatic
        fun newInstance(userName: String, heigth: Double, weigth: Double, gender: String) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_NAME, userName)
                    putDouble(PARAM_HEIGHT, heigth)
                    putDouble(PARAM_WEIGHT, weigth)
                    putString(PARAM_GENDER, gender)
                }
            }
    }
}
