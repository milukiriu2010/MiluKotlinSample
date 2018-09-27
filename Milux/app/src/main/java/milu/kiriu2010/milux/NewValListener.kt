package milu.kiriu2010.milux

interface NewValListener {
    // 新しい照度の値を渡す
    fun onUpdate( lux: Float )
}