package md.dmitrirusu.bmi.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.super_rabbit.wheel_picker.OnValueChangeListener
import com.super_rabbit.wheel_picker.WheelPicker
import kotlinx.android.synthetic.main.fragment_main.*
import md.dmitrirusu.bmi.R
import md.dmitrirusu.bmi.adapters.WPDayPickerAdapter


class MainFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private var weigth: Double = 50.0
    private var heght: Double = 150.0
    private var gender: String = "Male"
    private var userName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_continue.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if(!userName.isEmpty()) {
                    listener?.onContinueButtonClicked(userName, gender, weigth, heght)
                }
            }

        })

        textInputLayout.imeOptions = EditorInfo.IME_ACTION_DONE
        textInputLayout.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                userName = text.toString()
            }

        })

        picker_gender.setAdapter(WPDayPickerAdapter(resources.getStringArray(R.array.gender).toList()))
        picker_gender.setOnValueChangeListener(object : OnValueChangeListener {
            override fun onValueChange(picker: WheelPicker, oldVal: String, newVal: String) {
                gender = newVal
            }
        })


        picker_weight.setMin(0)
        picker_weight.setMax(250)
        picker_weight.scrollTo(50)
        picker_weight.setOnValueChangeListener(object : OnValueChangeListener {
            override fun onValueChange(picker: WheelPicker, oldVal: String, newVal: String) {
                weigth = newVal.toDouble()
            }
        })

        picker_height.setMin(0)
        picker_height.setMax(250)
        picker_height.scrollTo(150)
        picker_height.setOnValueChangeListener(object : OnValueChangeListener {
            override fun onValueChange(picker: WheelPicker, oldVal: String, newVal: String) {
                heght = newVal.toDouble()
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onContinueButtonClicked(userName: String, gender: String, wiegth: Double, height: Double)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}
