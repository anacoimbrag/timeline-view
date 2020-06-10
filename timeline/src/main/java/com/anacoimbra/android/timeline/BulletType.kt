package com.anacoimbra.android.timeline

enum class BulletType(private val type: Int) {
    ICON(1), SQUARE(2), CIRCLE(3), ROUND(4);

    companion object {
        private val values = values()
        fun getByType(value: Int) = values.firstOrNull { it.type == value }
    }
}