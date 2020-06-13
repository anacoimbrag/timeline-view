package com.anacoimbra.android.timeline.enums

enum class LineType(private val type: Int) {
    SOLID(1), DASHED(2), DOTTED(3);

    companion object {
        private val values = values()
        fun getByType(value: Int) = values.firstOrNull { it.type == value }
    }
}