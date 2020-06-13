package com.anacoimbra.android.timeline

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import com.anacoimbra.android.timeline.enums.BulletGravity
import com.anacoimbra.android.timeline.enums.BulletType
import com.anacoimbra.android.timeline.enums.LineType
import com.anacoimbra.android.timeline.enums.LineVisibility
import kotlin.math.absoluteValue
import kotlin.math.min


@Suppress("unused")
class Timeline @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attr, defStyle) {

    @VisibleForTesting
    var iconDrawable: Drawable?

    @VisibleForTesting
    var bulletIconTint: ColorStateList?

    @VisibleForTesting
    var bulletSize: Float

    @VisibleForTesting
    var bulletIconPadding: Float

    @VisibleForTesting
    var bulletCornerRadius: Float

    @ColorInt
    @VisibleForTesting
    var bulletBackgroundColor: Int

    @VisibleForTesting
    var bulletBackgroundDrawable: Drawable?

    var bulletType: BulletType = BulletType.ROUND
        set(value) {
            field = value
            invalidate()
        }

    var bulletGravity: BulletGravity = BulletGravity.CENTER
        set(value) {
            field = value
            invalidate()
        }

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

    var lineType: LineType = LineType.SOLID
        set(value) {
            field = value
            invalidate()
        }

    var lineVisibility: LineVisibility = LineVisibility.BOTH
        set(value) {
            field = value
            invalidate()
        }

    private val dashEffect: DashPathEffect
    private val dotEffect: DashPathEffect

    private val bulletPaint = Paint()
    private val linePaint = Paint()

    init {
        context.obtainStyledAttributes(attr, R.styleable.Timeline).apply {
            val bulletIcon = getResourceId(R.styleable.Timeline_bulletIcon, 0)
            iconDrawable =
                if (bulletIcon != 0) resources.getDrawable(bulletIcon, context.theme) else null
            bulletIconTint = getColorStateList(R.styleable.Timeline_bulletIconTint)
            val defaultSize = resources.getDimension(R.dimen.timeline_default_bullet_size)
            bulletSize = getDimension(R.styleable.Timeline_bulletSize, defaultSize)
            val defaultPadding = resources.getDimension(R.dimen.timeline_default_bullet_padding)
            bulletIconPadding = getDimension(R.styleable.Timeline_bulletIconPadding, defaultPadding)
            val defaultCornerRadius = resources.getDimension(R.dimen.timeline_default_corner_radius)
            bulletCornerRadius =
                getDimension(R.styleable.Timeline_bulletCornerRadius, defaultCornerRadius)
            val defaultBulletColor = getColorForAttribute(R.attr.colorPrimary, R.color.timeline_default_badge_color)
            bulletBackgroundDrawable = getDrawable(R.styleable.Timeline_bulletBackground)
            bulletBackgroundColor =
                getColor(R.styleable.Timeline_bulletBackground, defaultBulletColor)
            val type = getInt(R.styleable.Timeline_bulletType, 4)
            bulletType = BulletType.getByType(type) ?: BulletType.ROUND
            val gravity = getInt(R.styleable.Timeline_bulletGravity, 2)
            bulletGravity = BulletGravity.getByValue(gravity) ?: BulletGravity.CENTER
            val defaultLineWidth = resources.getDimension(R.dimen.timeline_default_line_width)
            lineWidth = getDimension(R.styleable.Timeline_lineWidth, defaultLineWidth)
            val defaultLineColor =
                getColorForAttribute(R.attr.colorAccent, R.color.timeline_default_line_color)
            lineColor =
                getColor(R.styleable.Timeline_lineColor, defaultLineColor)
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

        dashEffect = DashPathEffect(floatArrayOf(lineDashSize, lineDashGap), 0f)
        dotEffect = DashPathEffect(floatArrayOf(lineWidth, lineDashGap), 0f)

        linePaint.color = lineColor
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

        val bulletLeft = paddingStart.toFloat()
        val bulletRight = bulletLeft + bulletSize - paddingEnd
        val bulletTop: Float = when (bulletGravity) {
            BulletGravity.TOP -> paddingTop.toFloat()
            BulletGravity.CENTER ->
                (height).div(2f) - bulletSize.div(2f) - paddingTop
            BulletGravity.BOTTOM -> height - paddingBottom - bulletSize
        }
        val bulletBottom: Float = when (bulletGravity) {
            BulletGravity.TOP -> bulletTop + bulletSize
            BulletGravity.CENTER ->
                (height).div(2f) + bulletSize.div(2)
            BulletGravity.BOTTOM -> height - paddingBottom.toFloat()
        }

        drawBackground(canvas, bulletLeft, bulletTop, bulletRight, bulletBottom)
        drawIcon(canvas, bulletLeft, bulletTop, bulletRight, bulletBottom)
        drawLines(canvas, bulletLeft, bulletTop, bulletRight, bulletBottom)
    }

    fun setIcon(@DrawableRes icon: Int) {
        iconDrawable = ResourcesCompat.getDrawable(resources, icon, context.theme)
        invalidate()
    }

    fun setIconTint(colorStateList: ColorStateList?) {
        bulletIconTint = colorStateList
        invalidate()
    }

    fun setIconTint(@ColorRes color: Int) {
        bulletIconTint = ResourcesCompat.getColorStateList(resources, color, context.theme)
        invalidate()
    }

    fun setBulletSize(@DimenRes size: Int) {
        bulletSize = resources.getDimension(size)
        invalidate()
    }

    fun setIconPadding(@DimenRes padding: Int) {
        bulletIconPadding = resources.getDimension(padding)
        invalidate()
    }

    fun setBulletCornerRadius(@DimenRes radius: Int) {
        bulletCornerRadius = resources.getDimension(radius)
        invalidate()
    }

    fun setBulletBackground(@ColorRes color: Int) {
        bulletBackgroundColor = ResourcesCompat.getColor(resources, color, context.theme)
        invalidate()
    }

    fun setBulletBackgroundDrawable(@DrawableRes background: Int) {
        bulletBackgroundDrawable = ResourcesCompat.getDrawable(resources, background, context.theme)
        invalidate()
    }

    fun setLineWidth(@DimenRes lineWidth: Int) {
        this.lineWidth = resources.getDimension(lineWidth)
        invalidate()
    }

    fun setLineColorResource(@ColorRes color: Int) {
        lineColor = ResourcesCompat.getColor(resources, color, context.theme)
        invalidate()
    }

    fun setLinePadding(@DimenRes padding: Int) {
        linePadding = resources.getDimension(padding)
        invalidate()
    }

    fun setLineDashSize(@DimenRes size: Int) {
        lineDashSize = resources.getDimension(size)
        invalidate()
    }

    fun setLineDashGap(@DimenRes gap: Int) {
        lineDashGap = resources.getDimension(gap)
        invalidate()
    }

    private fun drawBackground(
        canvas: Canvas,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float
    ) {
        if (bulletBackgroundDrawable == null || bulletBackgroundDrawable is ColorDrawable) {
            bulletPaint.color = bulletBackgroundColor
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
        } else {
            bulletBackgroundDrawable?.setBounds(
                left.toInt(),
                top.toInt(),
                right.toInt(),
                bottom.toInt()
            )
            bulletBackgroundDrawable?.draw(canvas)
        }
    }

    private fun drawIcon(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        if (iconDrawable != null) {
            if (bulletIconTint != null)
                iconDrawable?.setTintList(bulletIconTint)
            val iconLeft = left.toInt() + bulletIconPadding.toInt()
            val iconRight = right.toInt() - bulletIconPadding.toInt()
            val iconTop = top + bulletIconPadding.toInt()
            val iconBottom = bottom - bulletIconPadding.toInt()
            iconDrawable?.setBounds(iconLeft, iconTop.toInt(), iconRight, iconBottom.toInt())
            iconDrawable?.draw(canvas)
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
                height - paddingBottom.toFloat(),
                linePaint
            )
    }

    @VisibleForTesting
    fun getColorForAttribute(attr: Int, @ColorRes defaultColor: Int): Int {
        return try {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(attr, typedValue, true)
            typedValue.data
        } catch (e: Exception) {
            ResourcesCompat.getColor(resources, defaultColor, context.theme)
        }
    }
}