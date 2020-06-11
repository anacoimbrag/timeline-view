package com.anacoimbra.android.timeline

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.core.content.res.ResourcesCompat
import kotlin.math.absoluteValue
import kotlin.math.min


class Timeline @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attr, defStyle) {

    @VisibleForTesting
    var bulletIcon: Int

    @VisibleForTesting
    var bulletIconTint: Int

    @VisibleForTesting
    var bulletSize: Float

    @VisibleForTesting
    var bulletIconPadding: Float

    @VisibleForTesting
    var bulletCornerRadius: Float

    @VisibleForTesting
    var bulletBackground: Int

    @VisibleForTesting
    var bulletType: BulletType

    @VisibleForTesting
    var bulletGravity: BulletGravity

    @VisibleForTesting
    var lineWidth: Float

    @VisibleForTesting
    var lineColor: Int

    @VisibleForTesting
    var linePadding: Float

    @VisibleForTesting
    var lineDashSize: Float

    @VisibleForTesting
    var lineDashGap: Float

    @VisibleForTesting
    var lineType: LineType

    @VisibleForTesting
    var lineVisibility: LineVisibility

    private val backgroundType = TypedValue()

    private val backgroundDrawable: Drawable?
    private val iconDrawable: Drawable?

    private val dashEffect: DashPathEffect
    private val dotEffect: DashPathEffect

    private val bulletPaint = Paint()
    private val linePaint = Paint()

    init {
        context.obtainStyledAttributes(attr, R.styleable.Timeline).apply {
            bulletIcon = getResourceId(R.styleable.Timeline_bulletIcon, 0)
            bulletIconTint = getColor(R.styleable.Timeline_bulletIconTint, 0)
            val defaultSize = resources.getDimension(R.dimen.timeline_default_bullet_size)
            bulletSize = getDimension(R.styleable.Timeline_bulletSize, defaultSize)
            val defaultPadding = resources.getDimension(R.dimen.timeline_default_bullet_padding)
            bulletIconPadding = getDimension(R.styleable.Timeline_bulletIconPadding, defaultPadding)
            val defaultCornerRadius = resources.getDimension(R.dimen.timeline_default_corner_radius)
            bulletCornerRadius =
                getDimension(R.styleable.Timeline_bulletCornerRadius, defaultCornerRadius)
            bulletBackground =
                getResourceId(
                    R.styleable.Timeline_bulletBackground,
                    R.color.timeline_default_badge_color
                )
            val type = getInt(R.styleable.Timeline_bulletType, 4)
            bulletType = BulletType.getByType(type) ?: BulletType.ROUND
            val gravity = getInt(R.styleable.Timeline_bulletGravity, 2)
            bulletGravity = BulletGravity.getByValue(gravity) ?: BulletGravity.CENTER
            val defaultLineWidth = resources.getDimension(R.dimen.timeline_default_line_width)
            lineWidth = getDimension(R.styleable.Timeline_lineWidth, defaultLineWidth)
            lineColor =
                getColor(R.styleable.Timeline_lineColor, R.color.timeline_default_line_color)
            linePadding = getDimension(R.styleable.Timeline_linePadding, 0f)
            val defaultDashSize = resources.getDimension(R.dimen.timeline_default_line_dash_size)
            lineDashSize = getDimension(R.styleable.Timeline_lineDashSize, defaultDashSize)
            val defaultDashGap = resources.getDimension(R.dimen.timeline_default_line_dash_gap)
            lineDashGap = getDimension(R.styleable.Timeline_lineDashGap, defaultDashGap)
            val lineTypeInt = getInt(R.styleable.Timeline_lineType, 1)
            lineType = LineType.getByType(lineTypeInt) ?: LineType.SOLID
            val lineVisibilityInt = getInt(R.styleable.Timeline_lineVisibility, 1)
            lineVisibility = LineVisibility.getByType(lineVisibilityInt) ?: LineVisibility.BOTH
        }.recycle()

        resources.getValue(bulletBackground, backgroundType, true)
        backgroundDrawable = if (backgroundType.type == TypedValue.TYPE_REFERENCE)
            resources.getDrawable(bulletBackground, null) else null
        iconDrawable = if (bulletIcon != 0) resources.getDrawable(bulletIcon, null) else null

        dashEffect = DashPathEffect(floatArrayOf(lineDashSize, lineDashGap), 0f)
        dotEffect = DashPathEffect(floatArrayOf(lineWidth, lineDashGap), 0f)

        linePaint.color = ResourcesCompat.getColor(resources, lineColor, null)
        linePaint.strokeWidth = lineWidth
        when (lineType) {
            LineType.SOLID -> {
            }
            LineType.DASHED -> linePaint.pathEffect = dashEffect
            LineType.DOTTED -> linePaint.pathEffect = dotEffect
        }

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val desiredWidth = (bulletSize + paddingStart + paddingEnd).toInt()
        val desiredHeight = height
        val width: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(desiredWidth, widthSize)
            else -> desiredWidth
        }
        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(desiredHeight, widthSize)
            else -> desiredHeight
        }

        setMeasuredDimension(width.absoluteValue, height.absoluteValue)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        val bulletLeft = 0f + paddingStart
        val bulletRight = bulletLeft + bulletSize.toInt()
        val bulletTop: Float = when (bulletGravity) {
            BulletGravity.TOP -> 0f + paddingTop.toFloat()
            BulletGravity.CENTER ->
                (height).div(2f) - bulletSize.div(2f)
            BulletGravity.BOTTOM -> height - paddingBottom - bulletSize
        }
        val bulletBottom: Float = when (bulletGravity) {
            BulletGravity.TOP -> bulletTop + bulletSize
            BulletGravity.CENTER ->
                (height).div(2f) + bulletSize.div(2)
            BulletGravity.BOTTOM -> height - paddingBottom.toFloat()
        }

        drawBackground(canvas, bulletLeft, bulletRight, bulletTop, bulletBottom)
        drawIcon(canvas, bulletLeft, bulletTop, bulletRight, bulletBottom)
        drawLines(canvas, bulletLeft, bulletTop, bulletRight, bulletBottom)
    }

    private fun drawBackground(
        canvas: Canvas,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float
    ) {
        if (backgroundType.type >= TypedValue.TYPE_FIRST_COLOR_INT && backgroundType.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            bulletPaint.color = ResourcesCompat.getColor(resources, bulletBackground, null)
            when (bulletType) {
                BulletType.ICON -> bulletIconPadding = 0f
                BulletType.SQUARE -> canvas.drawRect(
                    left,
                    top,
                    right,
                    bottom,
                    bulletPaint
                )
                BulletType.CIRCLE -> canvas.drawCircle(
                    (left + right).div(2f),
                    (top + bottom).div(2f),
                    bulletSize.div(2f),
                    bulletPaint
                )
                BulletType.ROUND -> canvas.drawRoundRect(
                    left,
                    top,
                    right,
                    bottom,
                    bulletCornerRadius,
                    bulletCornerRadius,
                    bulletPaint
                )
            }
        } else if (backgroundType.type == TypedValue.TYPE_REFERENCE) {
            backgroundDrawable?.setBounds(
                left.toInt(),
                top.toInt(),
                right.toInt(),
                bottom.toInt()
            )
            backgroundDrawable?.draw(canvas)
        }
    }

    private fun drawIcon(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        if (iconDrawable != null) {
            if (bulletIconTint != 0)
                iconDrawable.setTint(bulletIconTint)
            val iconLeft = left.toInt() + bulletIconPadding.toInt()
            val iconRight = right.toInt() - bulletIconPadding.toInt()
            val iconTop = top + bulletIconPadding.toInt()
            val iconBottom = bottom - bulletIconPadding.toInt()
            iconDrawable.setBounds(iconLeft, iconTop.toInt(), iconRight, iconBottom.toInt())
            iconDrawable.draw(canvas)
        }
    }

    private fun drawLines(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        if (lineVisibility == LineVisibility.BOTH || lineVisibility == LineVisibility.TOP)
            canvas.drawLine(
                (left + right).div(2f),
                0f + paddingTop.toFloat(),
                (left + right).div(2f),
                top - linePadding,
                linePaint
            )
        if (lineVisibility == LineVisibility.BOTH || lineVisibility == LineVisibility.BOTTOM)
            canvas.drawLine(
                (left + right).div(2f),
                bottom + linePadding,
                (left + right).div(2f),
                this.bottom - paddingBottom.toFloat(),
                linePaint
            )
    }
}