package com.example.milu.intent1

class User(var name: String, var gender: Boolean) {
    var skillPoints: Int = 0

    init {
        this.skillPoints = 0
    }

    companion object {

        var USER_NAME = "username"
        var USER_GENDER = "gender" // true -> male // false -> female
        var USER_SKILL_POINTS = "skillPoints"
    }
}