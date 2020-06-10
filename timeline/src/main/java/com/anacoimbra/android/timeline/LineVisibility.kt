package com.anacoimbra.android.timeline

enum class LineVisibility(private val type: Int) {
    VISIBLE(1), INVISIBLE(2), GONE(3);

    companion object {
        private val values = values()
        fun getByType(value: Int) = values.firstOrNull { it.type == value }
    }
}