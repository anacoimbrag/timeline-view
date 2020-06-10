package com.anacoimbra.android.timeline

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat


class Timeline @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attr, defStyle) {

    private var bulletIcon: Int
    private var bulletSize: Float
    private var bulletIconPadding: Float
    private var bulletCornerRadius: Float
    private var bulletBackground: Int
    private var bulletType: BulletType
    private var bulletGravity: BulletGravity
    private var lineWidth: Float
    private var lineColor: Int
    private var linePadding: Float
    private var lineDashSize: Float
    private var lineDashGap: Float
    private var lineDotSize: Float
    private var lineType: LineType
    private var lineVisibility: LineVisibility
    private var layout: Int

    private val backgroundType = TypedValue()

    private val backgroundDrawable: Drawable
    private val iconDrawable: Drawable

    private val dashEffect: DashPathEffect
    private val dotEffect: DashPathEffect

    private val bulletPaint = Paint()
    private val linePaint = Paint()

    init {
        context.obtainStyledAttributes(attr, R.styleable.Timeline).apply {
            bulletIcon = getResourceId(R.styleable.Timeline_bulletIcon, 0)
            val defaultSize = resources.getDimension(R.dimen.default_bullet_size)
            bulletSize = getDimension(R.styleable.Timeline_bulletSize, defaultSize)
            val defaultPadding = resources.getDimension(R.dimen.default_bullet_padding)
            bulletIconPadding = getDimension(R.styleable.Timeline_bulletIconPadding, defaultPadding)
            val defaultCornerRadius = resources.getDimension(R.dimen.default_corner_radius)
            bulletCornerRadius =
                getDimension(R.styleable.Timeline_bulletCornerRadius, defaultCornerRadius)
            bulletBackground = getResourceId(R.styleable.Timeline_bulletBackground, 0)
            val type = getInt(R.styleable.Timeline_bulletType, 4)
            bulletType = BulletType.getByType(type) ?: BulletType.ROUND
            val gravity = getInt(R.styleable.Timeline_bulletGravity, 2)
            bulletGravity = BulletGravity.getByValue(gravity) ?: BulletGravity.CENTER
            val defaultLineWidth = resources.getDimension(R.dimen.default_line_width)
            lineWidth = getDimension(R.styleable.Timeline_lineWidth, defaultLineWidth)
            lineColor = getColor(R.styleable.Timeline_lineColor, R.color.default_line_color)
            linePadding = getDimension(R.styleable.Timeline_linePadding, 0f)
            lineDashSize = getDimension(R.styleable.Timeline_lineDashSize, 0f)
            lineDashGap = getDimension(R.styleable.Timeline_lineDashGap, 0f)
            lineDotSize = getDimension(R.styleable.Timeline_lineDotSize, 0f)
            val lineTypeInt = getInt(R.styleable.Timeline_lineType, 1)
            lineType = LineType.getByType(lineTypeInt) ?: LineType.SOLID
            val lineVisibilityInt = getInt(R.styleable.Timeline_lineVisibility, 1)
            lineVisibility = LineVisibility.getByType(lineVisibilityInt) ?: LineVisibility.VISIBLE
            layout = getResourceId(R.styleable.Timeline_layout, 0)

            backgroundDrawable = resources.getDrawable(bulletBackground, null)
            iconDrawable = resources.getDrawable(bulletIcon, null)

            dashEffect = DashPathEffect(floatArrayOf(lineDashSize, lineDashGap), 0f)
            dotEffect = DashPathEffect(floatArrayOf(lineWidth, lineDashGap), 0f)
        }.recycle()

        resources.getValue(bulletBackground, backgroundType, true)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        val bulletLeft = left + paddingStart
        val bulletRight = bulletLeft + bulletSize.toInt()
        val bulletTop: Float = when (bulletGravity) {
            BulletGravity.TOP -> top + paddingTop.toFloat()
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

        if (backgroundType.type >= TypedValue.TYPE_FIRST_COLOR_INT && backgroundType.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            bulletPaint.color = ResourcesCompat.getColor(resources, bulletBackground, null)
            when (bulletType) {
                BulletType.ICON -> bulletIconPadding = 0f
                BulletType.SQUARE -> canvas.drawRect(
                    bulletLeft.toFloat(),
                    bulletTop,
                    bulletRight.toFloat(),
                    bulletBottom,
                    bulletPaint
                )
                BulletType.CIRCLE -> canvas.drawCircle(
                    (bulletLeft + bulletRight).div(2f),
                    (bulletTop + bulletBottom).div(2f),
                    bulletSize.div(2f),
                    bulletPaint
                )
                BulletType.ROUND -> canvas.drawRoundRect(
                    bulletLeft.toFloat(),
                    bulletTop,
                    bulletRight.toFloat(),
                    bulletBottom,
                    bulletCornerRadius,
                    bulletCornerRadius,
                    bulletPaint
                )
            }
        } else if (backgroundType.type == TypedValue.TYPE_REFERENCE) {
            backgroundDrawable.setBounds(
                bulletLeft,
                bulletTop.toInt(),
                bulletRight,
                bulletBottom.toInt()
            )
            backgroundDrawable.draw(canvas)
        }

        val iconLeft = bulletLeft + bulletIconPadding.toInt()
        val iconRight = bulletRight - bulletIconPadding.toInt()
        val iconTop = bulletTop + bulletIconPadding.toInt()
        val iconBottom = bulletBottom - bulletIconPadding.toInt()
        iconDrawable.setTint(Color.RED)
        iconDrawable.setBounds(iconLeft, iconTop.toInt(), iconRight, iconBottom.toInt())
        iconDrawable.draw(canvas)


        linePaint.color = ResourcesCompat.getColor(resources, lineColor, null)
        linePaint.strokeWidth = lineWidth
        when (lineType) {
            LineType.SOLID -> {
            }
            LineType.DASHED -> linePaint.pathEffect = dashEffect
            LineType.DOTTED -> linePaint.pathEffect = dotEffect
        }
        canvas.drawLine(
            (bulletLeft + bulletRight).div(2f),
            top + paddingTop.toFloat(),
            (bulletLeft + bulletRight).div(2f),
            bottom - paddingBottom.toFloat(),
            linePaint
        )
    }
}