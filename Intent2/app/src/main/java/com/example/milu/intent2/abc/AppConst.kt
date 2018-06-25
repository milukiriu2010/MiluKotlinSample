package com.example.milu.intent2.abc

enum class AppConst private constructor(value: String) {
    KEY_USER_FIRST_NAME("KeyUserFistName");

    private val value: String? = null

    init {
        //this.value = value
    }

    fun value(): String? {
        return this.value
    }
}
