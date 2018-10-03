package milu.kiriu2010.exdb1.canvas

data class Mover(
        // 画像を描画する位置
        val il: PVector = PVector(),
        // 画像の移動速度
        val iv: PVector = PVector(),
        // 画像の移動加速度
        val ia: PVector = PVector(),
        //   重さ(mass)
        var mass: Float = 1f) {

        // 力を加える
        fun applyForce( force: PVector ): Mover {
                val f = PVector().set(force)
                f.div(mass)
                ia.add(f)
                return this
        }


        // -------------------------------------------------
        // タッチ位置に向かって加速する
        // -------------------------------------------------
        // touched: 画面タッチしているかどうか
        // rl: 画面タッチ位置
        // -------------------------------------------------
        fun update( touched: Boolean, tl: PVector ) {
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
}