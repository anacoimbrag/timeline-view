package com.anacoimbra.android.timeline

enum class LineVisibility(private val type: Int) {
    BOTH(1), TOP(2), BOTTOM(3), NONE(4);

    companion object {
        private val values = values()
        fun getByType(value: Int) = values.firstOrNull { it.type == value }
    }
}