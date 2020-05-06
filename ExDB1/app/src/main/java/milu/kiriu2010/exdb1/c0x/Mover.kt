package milu.kiriu2010.exdb1.c0x

import android.util.Log
import kotlin.math.PI
import kotlin.math.cos

// -----------------------------------------
// 物体(画像)
// -----------------------------------------
data class Mover(
        // 画像を描画する位置
        val il: PVector = PVector(),
        // 画像の移動速度
        val iv: PVector = PVector(),
        // 画像の移動加速度
        val ia: PVector = PVector(),
        // 重さ(mass)
        var mass: Float = 1f
        ) {

    // 物体の幅(オリジナル)
    // 回転時に用いる
    var w: Float = 0f
        set(value) {
            field = value
            cl.x = il.x + value/2f
        }
    // 物体の高さ(オリジナル)
    // 回転時に用いる
    var h: Float = 0f
        set(value) {
            field = value
            cl.y = il.y + value/2f
        }

    // 物体の中心位置
    // 回転時に用いる
    var cl: PVector = PVector()

    // 物体の回転角度(位置)
    var al: PAngle = PAngle()

    // 物体の回転角度(刻み)
    var av: PAngle = PAngle()

    // -------------------------------------------
    // 回転(物体の中心が軸)
    // -------------------------------------------
    fun rotateByCenter() {
        // 回転(刻み)だけ回転させる
        al.x += av.x
        al.x = al.x%360
        al.y += av.y
        al.y = al.y%360

        // 回転
        il.x = cl.x + w/2f * cos(al.y/180.0* PI).toFloat()
        il.y = cl.y + h/2f * cos(al.x/180.0* PI).toFloat()
    }

    // -------------------------------------------
    // 回転に伴う幅(射影)
    // -------------------------------------------
    fun widthByRotate(): Float {
        val tmpW = w*cos(al.y/180* PI).toFloat()
        return -tmpW
        /*
        return if ( tmpW >= 0 ) {
            tmpW
        }
        else {
            -tmpW
        }
        */
    }

    // -------------------------------------------
    // 回転に伴う高さ(射影)
    // -------------------------------------------
    fun heightByRotate(): Float {
        val tmpH = h*cos(al.x/180*PI).toFloat()
        return -tmpH
        /*
        return if ( tmpH >= 0 ) {
            tmpH
        }
        else {
            -tmpH
        }
        */
    }

    // -------------------------------------------
    // 力を加える。すなわち、加速度を与える。
    // 「力=重さ×加速度」
    // -------------------------------------------
    fun applyForce( force: PVector ): Mover {
            val f = PVector().set(force)
            f.div(mass)
            ia.add(f)
            return this
    }

    // -------------------------------------------
    // 端に到達したら、反射する
    // -------------------------------------------
    private fun checkReflect() {
        // -------------------------------
        // 右端を超えていたら、
        // 位置を右端
        // 左右の速度を逆向きにする
        // -------------------------------
        if ( il.x > il.x2 ) {
                il.x = il.x2
                iv.x = -1 * iv.x
        }
        // -------------------------------
        // 左端を超えていたら、
        // 位置を左端
        // 左右の速度を逆向きにする
        // -------------------------------
        else if ( il.x < il.x1 ) {
                il.x = il.x1
                iv.x = -1 * iv.x
        }

        // -------------------------------
        // 下端を超えていたら、
        // 位置を下端
        // 上下の速度を逆向きにする
        // -------------------------------
        if ( il.y > il.y2 ) {
                il.y = il.y2
                iv.y = -1 * iv.y
        }
        // -------------------------------
        // 上端を超えていたら、
        // 位置を上端
        // 上下の速度を逆向きにする
        // -------------------------------
        else if ( il.y < il.y1 ) {
                il.y = il.y1
                iv.y = -1 * iv.y
        }
    }

    // -------------------------------------------------
    // 通常の移動(反射付き)
    // -------------------------------------------------
    fun moveReflect(limit: Float = 200f, isReflect: Boolean = true ) {
        // 速度に加速度を加算する
        // 速度にリミットを設けている
        // 質量が軽いほどリミット値を大きくしている
        iv.add(ia,limit/mass)
        // 移動
        il.add(iv)
        // 反射チェック
        if ( isReflect ) checkReflect()
    }

    // -------------------------------------------------
    // タッチしていないときは、バルーンのような動きをする
    // タッチしている場合は、タッチ位置に向かって加速する
    // -------------------------------------------------
    // touched: 画面タッチしているかどうか
    // rl: 画面タッチ位置
    // -------------------------------------------------
    fun moveBallon( touched: Boolean, tl: PVector ) {
        // 画面タッチされていないときは
        // 加速度をランダムに決定
        if ( touched == false ) {
            val ta = PVector().random2D()
            // random2Dは単位ベクトル化されているので倍数計算する
            ta.mult((0..10).shuffled().first().toFloat() )
            ia.set(ta)
        }
        // 画面タッチしているときは
        // 現在位置とタッチ位置を元に加速度を決定
        else {
            // 次の加速度
            val dir = PVector().set(tl)
            // "画面タッチ位置"－"現在位置"
            dir.sub(il)
            // 単位ベクトル化
            dir.normalize()
            // スケール
            //dir.mult((0..10).shuffled().first().toFloat())
            dir.mult(5f)
            ia.set(dir)
        }

        // 速度に加速度を加算する
        // 速度にリミットを設けている
        iv.add(ia,20f)
        // 移動
        il.add(iv)
        // 右端調整
        il.checkEdge()
    }

    // -------------------------------------------------
    // 物体が液体の中にいるかどうか
    // 全体が包まれると"液体の中"と判定している
    // 一部が包まれているだけだと"液体の外"と判定している
    //   true  => 液体の中
    //   false => 液体の外
    // -------------------------------------------------
    fun isInside(l: Liquid): Boolean {
        if ( (il.x > l.x) and
                (il.x < l.x + l.w) and
                (il.y > l.y) and
                (il.y< l.y + l.h) ){
                return true
        }
        else {
                return false
        }
    }

    // -------------------------------------------------
    // 液体の中を通過するときの抵抗
    // -------------------------------------------------
    fun drag(l: Liquid) {
        // "物体の速度"の大きさ
        val speed = iv.mag()
        // 抵抗の大きさ
        val dragMag = l.c * speed * speed

        // 抵抗の力を計算
        val drag = PVector().set(iv)
        drag.mult(-1f)
        drag.normalize()
        drag.mult(dragMag)

        Log.d(javaClass.simpleName, "mass[{${mass}}]il.x[${il.x}]drag.x[${drag.x}]drag.y[${drag.y}]")

        // 抵抗の力を加える
        applyForce(drag)
    }

}