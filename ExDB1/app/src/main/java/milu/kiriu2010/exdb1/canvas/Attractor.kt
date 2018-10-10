package milu.kiriu2010.exdb1.canvas

data class Attractor(
    // 位置
    val il: PVector = PVector(),
    // 質量
    var mass: Float = 3f,
    // G
    var g: Float = 200f
) {
    fun attract(m: Mover, magMin: Float = 5f, magMax: Float = 25f): PVector {
        val force = PVector(il).sub(m.il)
        var distance = force.mag()
        if ( distance < magMin ) {
            distance = magMin
        }
        if ( distance > magMax ) {
            distance = magMax
        }

        force.normalize()
        val strength = (g * mass * m.mass)/(distance*distance)
        force.mult(strength)
        return force
    }
}