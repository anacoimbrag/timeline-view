package com.anacoimbra.android.timeline

import android.os.Build
import androidx.core.content.res.ResourcesCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.anacoimbra.android.timeline.enums.BulletGravity
import com.anacoimbra.android.timeline.enums.BulletType
import com.anacoimbra.android.timeline.enums.LineType
import com.anacoimbra.android.timeline.enums.LineVisibility
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.N_MR1], manifest = Config.NONE)
class TimelineTest {

    private val timeline = Timeline(ApplicationProvider.getApplicationContext())

    @Test
    fun `given a timeline with no custom attributes, then it should have default values`() {
        val resources = timeline.context.resources
        assertEquals(timeline.bulletIcon, 0)
        assertEquals(timeline.bulletIconTint, 0)
        assertEquals(
            timeline.bulletSize,
            resources.getDimension(R.dimen.timeline_default_bullet_size)
        )
        assertEquals(
            timeline.bulletIconPadding,
            resources.getDimension(R.dimen.timeline_default_bullet_padding)
        )
        assertEquals(
            timeline.bulletCornerRadius,
            resources.getDimension(R.dimen.timeline_default_corner_radius)
        )
        assertEquals(
            timeline.bulletBackground,
            R.color.timeline_default_badge_color
        )
        assertEquals(timeline.bulletType, BulletType.ROUND)
        assertEquals(timeline.bulletGravity, BulletGravity.CENTER)

        assertEquals(
            timeline.lineWidth,
            resources.getDimension(R.dimen.timeline_default_line_width)
        )
        assertEquals(
            timeline.lineColor,
            ResourcesCompat.getColor(resources, R.color.timeline_default_line_color, null)
        )
        assertEquals(timeline.linePadding, 0f)
        assertEquals(
            timeline.lineDashSize,
            resources.getDimension(R.dimen.timeline_default_line_dash_size)
        )
        assertEquals(
            timeline.lineDashGap,
            resources.getDimension(R.dimen.timeline_default_line_dash_gap)
        )
        assertEquals(timeline.lineType, LineType.SOLID)
        assertEquals(timeline.lineVisibility, LineVisibility.BOTH)
    }

}