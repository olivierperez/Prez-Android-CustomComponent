package fr.o80.prez.customcomponent

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var editing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        demoBtn.setOnClickListener {
            ValueAnimator.ofFloat(0f, 1f)
                .apply {
                    duration = 2000
                    interpolator = DecelerateInterpolator(0.9f)
                    addUpdateListener { animation -> pinsWheelView.progress = animation.animatedValue as Float }
                }
                .start()
        }

        editingBtn.setOnClickListener {
            if (editing) {
                editing = false
                val position = pinsWheelView.save()
                editingBtn.text = "Modify"
                Toast.makeText(this, "Position $position", Toast.LENGTH_SHORT).show()
            } else {
                editing = true
                pinsWheelView.edit()
                editingBtn.text = "Save"
            }
        }

    }
}
