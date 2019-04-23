package md.dmitrirusu.bmi

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import md.dmitrirusu.bmi.fragments.MainFragment
import md.dmitrirusu.bmi.fragments.ResultFragment


class MainActivity : AppCompatActivity(),
    ResultFragment.OnFragmentInteractionListener,
    MainFragment.OnFragmentInteractionListener {

    private lateinit var mInterstitialAd: InterstitialAd

    lateinit var userName: String
    lateinit var gender: String
    var wiegth: Double = 0.0
    var height: Double = 0.0

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onContinueButtonClicked(userName: String, gender: String, wiegth: Double, height: Double) {
        this.userName = userName
        this.gender = gender
        this.wiegth = wiegth
        this.height = height

        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.")
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarTransparent()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupAdMob()
        back_arrow.setOnClickListener { onBackPressed() }


        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, MainFragment.newInstance())
            .commit()
    }

    private fun setupAdMob() {
        MobileAds.initialize(this, getString(R.string.admob_app_id))
        adView.loadAd(
            AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build()
        )

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                toolbar_text.text = getString(R.string.bmi_details)
                val massIndex: Double = (wiegth / ((height / 100) * (height / 100)))
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, ResultFragment.newInstance(userName, massIndex))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            toolbar_text.text = getString(R.string.add_bmi_details)

            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }
}
