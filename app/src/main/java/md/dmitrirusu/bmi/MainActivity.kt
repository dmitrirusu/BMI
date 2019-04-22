package md.dmitrirusu.bmi

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.NumberPicker


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val genders = arrayOf("unassessed", "Skipped", "Incorrect", "Correct", "1 mark")

        picker.setMinValue(0)
        picker.setMaxValue(genders.size - 1)
        picker.setDisplayedValues(genders)
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS)
    }
}
