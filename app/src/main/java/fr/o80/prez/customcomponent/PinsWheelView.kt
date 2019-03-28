package fr.o80.prez.customcomponent

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.*

private const val START_ANGLE = 30f
private const val WHEEL_MARGIN_PERCENT = 0.2f
private const val STROKE_PERCENT = 0.05f

/**
 * @author Olivier Perez
 */
class PinsWheelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Drawing values
    private lateinit var backgroundPaint: Paint
    private lateinit var arcPaint: Paint
    private lateinit var disabledPaint: Paint
    private lateinit var clearPaint: Paint
    private lateinit var pinPaint: Paint

    // Attributs values
    private var pinSize: Float = 0f

    // Measured values
    private var size: Float = 0f
    private var center: Float = 0f
    private var wheelMargin: Float = 0f

    // Domain values
    private var editing = false
    private var pinPosition: Float = -1f

    var progress = 1f
        set(value) {
            if (value < 0f || value > 1f) throw IllegalArgumentException("Progress must be between 0f and 1f")
            field = value
            postInvalidate()
        }

    init {
        if (!isInEditMode) {
            // TODO Create custom parameters
            attrs?.let { read(it) }
        }
    }

    private fun read(attrs: AttributeSet) {
        pinSize = 5f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when {
            editing && event.action == MotionEvent.ACTION_DOWN -> {
                true
            }
            editing && event.action == MotionEvent.ACTION_MOVE -> {
                pinPosition = event.toPinPosition(center)
                postInvalidate()
                true
            }
            else -> super.onTouchEvent(event)
        }
    }

    private fun setupPaints(wheelStrokeWidth: Float) {
        backgroundPaint = Paint().apply {
            color = Color.GREEN
            isAntiAlias = true
            strokeWidth = 5f
            style = Paint.Style.FILL
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }

        arcPaint = Paint().apply {
            shader = SweepGradient(
                center, center,
                intArrayOf(
                    Color.rgb(255, 0, 0),
                    Color.rgb(255, 200, 0),
                    Color.rgb(0, 255, 0),
                    Color.rgb(0, 200, 255),
                    Color.rgb(0, 0, 255)
                ),
                floatArrayOf(
                    0f,
                    0.25f,
                    0.50f,
                    0.75f,
                    1f
                )
            )
            isDither = true
            isAntiAlias = true
            strokeWidth = wheelStrokeWidth
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }

        disabledPaint = Paint().apply {
            color = Color.rgb(170, 170, 170)
            isDither = true
            isAntiAlias = true
            strokeWidth = wheelStrokeWidth + 10
            style = Paint.Style.STROKE
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        }

        clearPaint = Paint().apply {
            color = Color.WHITE
            strokeWidth = 1f
            style = Paint.Style.FILL
        }

        pinPaint = Paint().apply {
            color = Color.BLACK
            isDither = true
            isAntiAlias = true
            strokeWidth = wheelStrokeWidth + 10
            style = Paint.Style.STROKE
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        val sizeInt = min(widthSize, heightSize)

        setMeasuredDimension(sizeInt, sizeInt)

        size = sizeInt.toFloat()
        center = size / 2f
        wheelMargin = WHEEL_MARGIN_PERCENT * size

        setupPaints(STROKE_PERCENT * size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), clearPaint)
        canvas.rotate(90f, center, center)

        // Draw colored wheel
        canvas.drawArc(
            left.toFloat() + wheelMargin,
            top.toFloat() + wheelMargin,
            right.toFloat() - wheelMargin,
            bottom.toFloat() - wheelMargin,
            START_ANGLE,
            360f - START_ANGLE - START_ANGLE,
            false,
            arcPaint
        )

        // Draw grayed wheel regarding the progress
        val roundSize = 5f
        val wheelLength = (360f - 2 * START_ANGLE) + 2 * roundSize
        canvas.drawArc(
            left.toFloat() + wheelMargin,
            top.toFloat() + wheelMargin,
            right.toFloat() - wheelMargin,
            bottom.toFloat() - wheelMargin,
            progress * wheelLength + START_ANGLE - roundSize,
            wheelLength - progress * wheelLength,
            false,
            disabledPaint
        )

        if (pinPosition >= 0) {
            // Because of the weight of pin, pinWheelLenght is a bit smaller than wheelLength
            val pinWheelLength = (360f - 2 * START_ANGLE) + 2 * roundSize - pinSize
            canvas.drawArc(
                left.toFloat() + wheelMargin,
                top.toFloat() + wheelMargin,
                right.toFloat() - wheelMargin,
                bottom.toFloat() - wheelMargin,
                pinPosition * pinWheelLength + START_ANGLE - roundSize,
                pinSize,
                false,
                pinPaint
            )
        }
    }

    fun edit() {
        editing = true
    }

    fun save(): Float {
        editing = false
        return pinPosition
    }
}