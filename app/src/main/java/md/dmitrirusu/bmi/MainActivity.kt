package md.dmitrirusu.bmi

import android.content.ActivityNotFoundException
import android.content.Intent
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


    override fun onContinueButtonClicked(userName: String, gender: String, wiegth: Double, height: Double) {
        this.userName = userName
        this.gender = gender
        this.wiegth = wiegth
        this.height = height

        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            Log.d(this.javaClass.simpleName, "The interstitial wasn't loaded yet.")
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
        mInterstitialAd.adUnitId = getString(R.string.admob_interstitial_unit_id)
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                mInterstitialAd.loadAd(AdRequest.Builder().build())

                toolbar_text.text = getString(R.string.bmi_details)

                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, ResultFragment.newInstance(userName, height, wiegth, gender))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onShareButtonPressed() {
        val massIndex = calculateMassindex()

        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.check_my_bmi_result))
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.my_bmi_is, massIndex))
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.where_to_send)))
    }

    private fun calculateMassindex(): Double {
        return wiegth / ((height / 100) * (height / 100))
    }

    override fun onRateButtonPressed() {
        val uri = Uri.parse("market://details?id=" + this.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.packageName)
                )
            )
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            toolbar_text.text = getString(R.string.add_bmi_details)
        }

        super.onBackPressed()
    }

    private fun setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }
}
