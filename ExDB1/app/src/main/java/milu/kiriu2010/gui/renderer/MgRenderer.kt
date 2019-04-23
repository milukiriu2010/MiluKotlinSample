package milu.kiriu2010.gui.renderer

import android.opengl.GLSurfaceView

abstract class MgRenderer: GLSurfaceView.Renderer {
    // モデル変換行列
    protected val matM = FloatArray(16)
    // モデル変換行列の逆行列
    protected val matI = FloatArray(16)
    // ビュー変換行列
    protected val matV = FloatArray(16)
    // プロジェクション変換行列
    protected val matP = FloatArray(16)
    // モデル・ビュー・プロジェクション行列
    protected val matMVP = FloatArray(16)
    // ビュー・プロジェクション行列
    protected val matPV = FloatArray(16)
    // 点光源の位置
    protected val vecLight = floatArrayOf(5f,5f,5f)
    // 環境光の色
    protected val vecAmbientColor = floatArrayOf(0.1f,0.1f,0.1f,1f)
    // カメラの座標
    protected val vecEye = floatArrayOf(0f,0f,10f)
    // カメラの上方向を表すベクトル
    protected val vecEyeUp = floatArrayOf(0f,1f,0f)
    // 中心座標
    protected val vecCenter = floatArrayOf(0f,0f,0f)

    // 回転角度
    protected var angle1 = 0

    // 深度テスト
    var isDepth = true
    // カリング
    var isCull = true
    // 回転スイッチ
    var rotateSwitch = true
    // シェーダスイッチ
    var shaderSwitch = 0
}
