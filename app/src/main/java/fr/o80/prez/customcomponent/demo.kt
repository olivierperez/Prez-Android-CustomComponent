package fr.o80.prez.customcomponent

import android.view.MotionEvent
import kotlin.math.*

private const val PIF = PI.toFloat()

fun MotionEvent.toPinPosition(center: Float): Float {
    val angle = atan2(x - center, y - center)
    val adjustAngle = if (angle > 0) {
        2 * PIF - angle
    } else {
        abs(angle)
    }

    val start = -0.1f
    val end = 1.1f

    val positionInCircle = adjustAngle / (2 * PIF)
    val positionInWheel = start + positionInCircle * (end - start)

    return min(max(positionInWheel, 0f), 1f)
}