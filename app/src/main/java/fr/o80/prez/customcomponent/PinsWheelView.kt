package fr.o80.prez.customcomponent

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min


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

    private lateinit var backgroundPaint: Paint
    private lateinit var arcPaint: Paint
    private lateinit var disabledPaint: Paint
    private lateinit var clearPaint: Paint

    private var size: Float = 0f
    private var center: Float = 0f
    private var wheelMargin: Float = 0f

    private var progress = 65f / 100f

    init {
        if (!isInEditMode) {
            // TODO Create custom parameters
            attrs?.let { read(it) }
        }
    }

    private fun read(attrs: AttributeSet) {
        // TODO Read attrs
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

        val gradient = SweepGradient(
            center, center,
            intArrayOf(
                Color.rgb(255, 0, 0),
                Color.rgb(0, 255, 0),
                Color.rgb(0, 0, 255)
            ),
            floatArrayOf(
                0f,
                0.5f,
                1f
            )
        )

        arcPaint = Paint().apply {
            shader = gradient
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
            strokeWidth = wheelStrokeWidth + 5
            style = Paint.Style.STROKE
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        }

        clearPaint = Paint().apply {
            color = Color.WHITE
            strokeWidth = 1f
            style = Paint.Style.FILL
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
        canvas.saveLayer(0f, 0f, right.toFloat(), bottom.toFloat(), clearPaint)
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
        val x = 5f
        val wheelLength = (360f - 2 * START_ANGLE) + 2 * x
        canvas.drawArc(
            left.toFloat() + wheelMargin,
            top.toFloat() + wheelMargin,
            right.toFloat() - wheelMargin,
            bottom.toFloat() - wheelMargin,
            progress * wheelLength + START_ANGLE - x,
            wheelLength - progress * wheelLength,
            false,
            disabledPaint
        )
    }
}