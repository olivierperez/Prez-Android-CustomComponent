package fr.o80.prez.customcomponent

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hello.setOnClickListener {
            ValueAnimator.ofFloat(0f, 1f)
                .apply {
                    duration = 2000
                    interpolator = AccelerateInterpolator(1.5f)
                    addUpdateListener { animation -> pinsWheelView.progress = animation.animatedValue as Float }
                }
                .start()
        }

    }
}
