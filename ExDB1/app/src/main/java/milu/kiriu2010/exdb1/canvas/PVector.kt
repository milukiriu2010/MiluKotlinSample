package milu.kiriu2010.exdb1.canvas

import kotlin.math.sqrt

// 物体データ
//   位置(x,y)
//   移動可能領域：左(x1)
//   移動可能領域：右(x2)
//   移動可能領域：上(y1)
//   移動可能領域：下(y2)
data class PVector(
        var x: Float = 0f,
        var y: Float = 0f,
        var x1: Float = 0f,
        var x2: Float = 0f,
        var y1: Float = 0f,
        var y2: Float = 0f) {

    // 移動
    fun set( pv: PVector ): PVector {
        x = pv.x
        y = pv.y

        return this
    }

    // 移動(加算)
    fun add(dx: Float, dy: Float): PVector {
        x += dx
        y += dy

        return this
    }

    // 移動(加算)
    fun add( dv: PVector ): PVector {
        x += dv.x
        y += dv.y

        return this
    }

    // 移動(加算)
    // 移動量のリミッターつき
    fun add(dv: PVector, limit: Float): PVector {
        val ax = x+dv.x
        val ay = y+dv.y
        if ( sqrt(ax*ax+ay*ay) > limit ) return this
        x = ax
        y = ay

        return this
    }

    // 移動(減算)
    fun sub(dv: PVector): PVector {
        x -= dv.x
        y -= dv.y

        return this
    }

    // 移動(乗算)
    fun mult(d: Float): PVector {
        x *= d
        y *= d

        return this
    }

    // 移動(徐算)
    fun div(d: Float): PVector {
        if ( d == 0.0f ) {
        }
        else {
            x /= d
            y /= d
        }
        return this
    }

    // ベクトルの大きさ
    fun mag() : Float{
        return sqrt(x*x + y*y)
    }

    // 単位ベクトルに変換
    fun normalize(): PVector {
        val mag = mag()
        if ( mag != 0f ) {
            x /= mag
            y /= mag
        }

        return this
    }

    // ランダムに位置を決定する
    // 単位ベクトルに変換する
    fun random2D(): PVector {
        x = (-100..100).shuffled().first().toFloat()
        y = (-100..100).shuffled().first().toFloat()
        normalize()

        return this
    }

    // -------------------------------------------
    // 端に到達したときの次の位置を決定する
    // -------------------------------------------
    // 右端を超えていたら、左端に戻す
    // 左端を超えていたら、右端に戻す
    // 下端を超えていたら、上端に戻す
    // 上端を超えていたら、下端に戻す
    // -------------------------------------------
    fun checkEdge(): PVector {
        // 右端を超えていたら、左端に戻す
        if ( x > x2 ) {
            x = x1
        }
        // 左端を超えていたら、右端に戻す
        else if ( x < x1 ) {
            x = x2
        }

        // 下端を超えていたら、上端に戻す
        if ( y > y2 ) {
            y = y1
        }
        // 上端を超えていたら、下端に戻す
        else if ( y < y1 ) {
            y = y2
        }

        return this
    }
}