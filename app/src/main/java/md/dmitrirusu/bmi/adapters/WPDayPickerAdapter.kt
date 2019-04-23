package md.dmitrirusu.bmi.adapters

import com.super_rabbit.wheel_picker.WheelAdapter

class WPDayPickerAdapter(var values: List<String>) : WheelAdapter {

    //get item value based on item position in wheel
    override fun getValue(position: Int): String {
        if(position <0) {
            return ""
        }
        if(position>= values.size){
            return ""
        }
        return values[position]
    }

    //get item position based on item string value
    override fun getPosition(vale: String): Int {
        return 0
    }

    //return a string with the approximate longest text width, for supporting WRAP_CONTENT
    override fun getTextWithMaximumLength(): String {
        return "Mmm 00, 0000"
    }

    //return the maximum index
    override fun getMaxIndex(): Int {
        return values.size-1
    }

    //return the minimum index
    override fun getMinIndex(): Int {
        return 0
    }
}