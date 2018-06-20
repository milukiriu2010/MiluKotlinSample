package com.example.milu.intent2.abc

open class Team() {
    var type: String? = null
    var name: String? = null
    var playerLst: MutableList<String> = mutableListOf()
    var yearPosMap:MutableMap<Int,Int> = mutableMapOf()

    fun addPlayer( player: String ) {
        this.playerLst.add( player )
    }

    fun putYearPosMap( year : Int, pos: Int ){
        this.yearPosMap.put( year, pos )
    }

    override fun toString(): String {
        return this.name ?: "<NULL>"
    }

    /*
    fun Any?.toString() : String {
        return name ?: "<NULL>"
    }
    */
}