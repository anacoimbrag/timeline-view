package com.anacoimbra.android.timeline

enum class BulletGravity(private val value: Int) {
    TOP(1), CENTER(2), BOTTOM(3);

    companion object {
        private val values = values()
        fun getByValue(value: Int) = values.firstOrNull { it.value == value }
    }
}