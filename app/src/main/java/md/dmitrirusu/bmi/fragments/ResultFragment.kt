package md.dmitrirusu.bmi.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_result.*
import java.math.BigDecimal
import java.math.RoundingMode


private const val PARAM_NAME = "userName"
private const val PARAM_MASS_INDEX = "massIndex"

class ResultFragment : Fragment() {
    private var userName: String? = null
    private var massIndex: Double? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userName = it.getString(PARAM_NAME)
            massIndex = it.getDouble(PARAM_MASS_INDEX)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(md.dmitrirusu.bmi.R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        massIndex?.let {
            val doubleAsString = BigDecimal(it).setScale(2, RoundingMode.UP).toString()
            val indexOfDecimal = doubleAsString?.indexOf(".")
            mass_index_whole_part.text = doubleAsString.substring(0, indexOfDecimal)
            mass_index_decimal_part.text = doubleAsString.substring(indexOfDecimal)
        }


        result_message.text = "Hello %s, you are normal".format(userName)
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
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
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(userName: String, massIndex: Double) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_NAME, userName)
                    putDouble(PARAM_MASS_INDEX, massIndex)
                }
            }
    }
}
