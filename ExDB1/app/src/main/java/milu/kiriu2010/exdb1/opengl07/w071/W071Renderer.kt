package milu.kiriu2010.exdb1.opengl07.w071

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.model.Sphere01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// -------------------------------------
// いまいちよくわからない
// -------------------------------------
// 頂点テクスチャフェッチ
// -------------------------------------
// https://wgld.org/d/webgl/w071.html
// -------------------------------------
class W071Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(球体)
    private lateinit var modelSphere: Sphere01Model

    // シェーダ(点のレンダリングを行う)
    private lateinit var pointShader: W071ShaderPoint
    // シェーダ(テクスチャへの描きこみを行う)
    private lateinit var mappingShader: W071ShaderMapping

    // 画面縦横比
    var ratio: Float = 1f

    // フレームバッファ
    val bufFrame = IntBuffer.allocate(2)

    // 深度バッファ用レンダ―バッファ
    val bufDepthRender = IntBuffer.allocate(2)

    // フレームバッファ用のテクスチャ
    val frameTexture = IntBuffer.allocate(2)

    // 描画対象のテクスチャ
    var textureType = 0

    // 頂点インデックス
    lateinit var indices: IntArray

    override fun onDrawFrame(gl: GL10?) {
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()

        // ビュー×プロジェクション座標変換行列
        Matrix.setLookAtM(matV,0,
                0f,0f,5f,
                0f,0f,0f,
                0f,1f,0f)
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,10f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // フレームバッファをバインド
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[0])

        // ビューポートを設定
        GLES20.glViewport(0,0,16,16)

        // フレームバッファを初期化
        GLES20.glClearColor(0f,0f,0f,1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // テクスチャへ頂点情報をレンダリング
        mappingShader.draw(modelSphere,indices.size.toFloat())

        // フレームバッファのバインドを解除
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)

        // ビューポートを設定
        GLES20.glViewport(0, 0, renderW, renderH)

        // canvasを初期化
        GLES20.glClearColor(1f,1f,1f,1f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // フレームバッファをテクスチャとしてバインド
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTexture[0])

        // 点を描画
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t0,0f,1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        pointShader.draw(modelSphere,matMVP,indices.size.toFloat(),0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height


        // フレームバッファ生成
        GLES20.glGenFramebuffers(1,bufFrame)
        // レンダ―バッファ生成
        GLES20.glGenRenderbuffers(1,bufDepthRender)
        // フレームバッファを格納するテクスチャ生成
        GLES20.glGenTextures(1,frameTexture)
        MyGLFunc.createFrameBuffer(16,16,0,bufFrame,bufDepthRender,frameTexture)

    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // uniform変数の上限数
        // Huawei P20Lite:1024
        val uniformMaxCnt = IntBuffer.allocate(1)
        GLES20.glGetIntegerv(GLES20.GL_MAX_FRAGMENT_UNIFORM_VECTORS,uniformMaxCnt)
        //callback.receive(pointSizeRange)
        Log.d(javaClass.simpleName,"uniformMaxCnt:${uniformMaxCnt.get(0)}")
        // 頂点テクスチャフェッチが可能かどうか調べる
        // Huawei P20Lite:16
        val vertexTextureMaxCnt = IntBuffer.allocate(1)
        GLES20.glGetIntegerv(GLES20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS,vertexTextureMaxCnt)
        Log.d(javaClass.simpleName,"vertexTextureMaxCnt:${vertexTextureMaxCnt.get(0)}")

        // 深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // シェーダ(点のレンダリングを行う)
        pointShader = W071ShaderPoint()
        pointShader.loadShader()

        // シェーダ(テクスチャへの描きこみを行う)
        mappingShader = W071ShaderMapping()
        mappingShader.loadShader()

        // モデル生成(球体)
        modelSphere = Sphere01Model()
        modelSphere.createPath(mapOf(
                "pattern" to 2f,
                "row"     to 15f,
                "column"  to 15f,
                "radius" to 1f
        ))

        // 頂点の数分だけインデックスを格納
        indices = IntArray(modelSphere.datPos.size/3)
        (0 until modelSphere.datPos.size/3).forEach {
            indices[it] = it
        }

        // ライトの向き
        vecLight[0] = -0.577f
        vecLight[1] =  0.577f
        vecLight[2] =  0.577f
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}