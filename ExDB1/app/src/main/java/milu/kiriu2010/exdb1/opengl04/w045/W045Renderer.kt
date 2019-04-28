package milu.kiriu2010.exdb1.opengl04.w045

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.Matrix
import android.view.MotionEvent
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.basic.MyQuaternion
import milu.kiriu2010.gui.model.Cube01Model
import milu.kiriu2010.gui.model.Sphere01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.sqrt

// キューブ環境バンプマッピング
// http://opengles2learning.blogspot.com/2011/06/texturing-cube-different-textures-on.html
class W045Renderer(ctx: Context): MgRenderer(ctx) {
    // 描画オブジェクト(立方体)
    private lateinit var drawObjCube: Cube01Model
    // 描画オブジェクト(球体)
    private lateinit var drawObjSphere: Sphere01Model
    // 描画オブジェクト(トーラス)
    private lateinit var drawObjTorus: Torus01Model

    // シェーダ
    private lateinit var shader: W045Shader

    // 画面縦横比
    var ratio: Float = 0f

    // ビットマップ配列
    val bmpArray = arrayListOf<Bitmap>()

    // キューブマップ用のターゲットを格納する配列
    val targetArray = arrayListOf<Int>(
            GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X,
            GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
            GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
            GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
            GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
            GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z
    )

    // テクスチャ配列
    val normalTextures = IntArray(2)
    val cubeTextures = IntArray(2)

    override fun onDrawFrame(gl: GL10?) {
        // 回転角度
        angle[0] =(angle[0]+1)%360
        angle[1] =(angle[0]+180)%360
        val t1 = angle[0].toFloat()
        val t2 = angle[1].toFloat()

        // canvasを初期化
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glClearDepthf(1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // ビュー×プロジェクション座標変換行列
        vecEye = qtnNow.toVecIII(floatArrayOf(0f,0f,20f))
        vecEyeUp = qtnNow.toVecIII(floatArrayOf(0f,1f,0f))
        Matrix.setLookAtM(matV, 0,
                vecEye[0], vecEye[1], vecEye[2],
                vecCenter[0], vecCenter[1], vecCenter[2],
                vecEyeUp[0], vecEyeUp[1], vecEyeUp[2])
        Matrix.perspectiveM(matP,0,45f,ratio,0.1f,200f)
        Matrix.multiplyMM(matVP,0,matP,0,matV,0)

        // 法線マップテクスチャ
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,normalTextures[0])

        // キューブマップテクスチャ
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,cubeTextures[0])


        // 背景用キューブをレンダリング
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,100f,100f,100f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(drawObjCube,matM,matMVP,vecEye,0,1,0)

        // 球体をレンダリング
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t1,0f,0f,1f)
        Matrix.translateM(matM,0,5f,0f,0f)
        Matrix.rotateM(matM,0,t1,0f,-1f,0f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(drawObjSphere,matM,matMVP,vecEye,0,1,1)

        // トーラスをレンダリング
        Matrix.setIdentityM(matM,0)
        Matrix.rotateM(matM,0,t2,0f,0f,1f)
        Matrix.translateM(matM,0,5f,0f,0f)
        Matrix.rotateM(matM,0,t2,1f,-1f,1f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shader.draw(drawObjTorus,matM,matMVP,vecEye,0,1,1)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // canvasを初期化する色を設定する
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // canvasを初期化する際の深度を設定する
        GLES20.glClearDepthf(1f)

        // カリングと深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // シェーダプログラム登録
        shader = W045Shader()
        shader.loadShader()

        // 法線マップテクスチャを生成
        GLES20.glGenTextures(1,normalTextures,0)
        MyGLFunc.createTexture(0,normalTextures,bmpArray[6])

        // キューブマップを生成
        generateCubeMap()

        // モデル生成(立方体)
        drawObjCube = Cube01Model()
        drawObjCube.createPath(mapOf(
                "pattern" to 2f,
                "scale"   to 2f,
                "colorR"  to 1f,
                "colorG"  to 1f,
                "colorB"  to 1f,
                "colorA"  to 1f
        ))

        // モデル生成(球体)
        drawObjSphere = Sphere01Model()
        drawObjSphere.createPath(mapOf(
                "row"    to 32f,
                "column" to 32f,
                "radius" to 2.5f,
                "colorR" to 1f,
                "colorG" to 1f,
                "colorB" to 1f,
                "colorA" to 1f
        ))

        // モデル生成(トーラス)
        drawObjTorus = Torus01Model()
        drawObjTorus.createPath(mapOf(
                "row"     to 32f,
                "column"  to 32f,
                "iradius" to 1f,
                "oradius" to 2f,
                "colorR"  to 1f,
                "colorG"  to 1f,
                "colorB"  to 1f,
                "colorA"  to 1f
        ))

        // ----------------------------------
        // 単位行列化
        // ----------------------------------
        // モデル変換行列
        Matrix.setIdentityM(matM,0)
        // モデル変換行列の逆行列
        Matrix.setIdentityM(matI,0)
        // ビュー変換行列
        Matrix.setIdentityM(matV,0)
        // プロジェクション変換行列
        Matrix.setIdentityM(matP,0)
        // モデル・ビュー・プロジェクション行列
        Matrix.setIdentityM(matMVP,0)
        // テンポラリ行列
        Matrix.setIdentityM(matVP,0)
    }

    // キューブマッピング用テクスチャ
    private fun generateCubeMap() {
        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(1,cubeTextures,0)
        MyGLFunc.checkGlError("glGenTextures")

        // テクスチャをキューブマップにバインド
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,cubeTextures[0])

        // テクスチャへimageを適用
        (0..5).forEach { id ->
            val bitmap = bmpArray[id]
            val bw = bitmap.width
            val bh = bitmap.height
            val buffer = ByteBuffer.allocateDirect(bw*bh*4)
            bitmap.copyPixelsToBuffer(buffer)
            buffer.position(0)

            GLES20.glTexImage2D(targetArray[id],0,GLES20.GL_RGBA,
                    bw,bh,0,GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE,buffer)
            if ( bitmap.isRecycled == false ) {
                bitmap.recycle()
            }
        }

        // ミニマップを生成
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_CUBE_MAP)

        // テクスチャのパラメータを設定
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        // テクスチャのバインド無効化
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,0)
    }

    override fun setMotionParam(motionParam: MutableMap<String, Float>) {
    }

    override fun closeShader() {
    }
}