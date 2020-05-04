package milu.kiriu2010.exdb1.a2x

// 液体
data class Liquid(
    var x: Float = 0f,
    var y: Float = 0f,
    var w: Float = 0f,
    var h: Float = 0f,
    // drag係数(液体の中を通過するときの係数)
    var c: Float = 0f
) {

}
