package milu.kiriu2010.exdb1.opengl04.w047v

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.Matrix
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.Cube01Model
import milu.kiriu2010.gui.model.Sphere01Model
import milu.kiriu2010.gui.model.Torus01Model
import milu.kiriu2010.gui.renderer.MgRenderer
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs
import milu.kiriu2010.gui.vbo.es20.ES20VBOIpnc
import java.nio.ByteBuffer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// --------------------------------------
// 動的キューブマッピング
// --------------------------------------
// https://wgld.org/d/webgl/w047.html
// --------------------------------------
// キューブマップのシェーダで
// UseProgramで0x502が発生する
// トーラスが写らない
// --------------------------------------
class WV047Renderer(ctx: Context): MgRenderer(ctx) {

    // 描画オブジェクト(立方体)(背景)
    private lateinit var modelCube: Cube01Model
    // 描画オブジェクト(球体)
    private lateinit var modelSphere: Sphere01Model
    // 描画オブジェクト(トーラス)
    private lateinit var modelTorus: Torus01Model

    // VBO(立方体)
    private lateinit var boCube: ES20VBOAbs
    // VBO(球体)
    private lateinit var boSphere: ES20VBOAbs
    // VBO(トーラス)
    private lateinit var boTorus: ES20VBOAbs

    // シェーダ(反射光ライティング)
    private lateinit var shaderSpecular: WV047ShaderSpecular
    // シェーダ(キューブマッピング)
    private lateinit var shaderCubeMap: WV047ShaderCubeMap

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

    // 視点座標(トーラス用)
    val torusEye = FloatArray(3*6)
    // カメラの上方向を表すベクトル(トーラス用)
    val torusCamUp = FloatArray(3*6)
    // 位置(トーラス用)
    val torusPos = FloatArray(3*6)
    // 環境色(トーラス用)
    // 実際には、環境光ではなく、モデルの色としてつかっている
    val torusAmb = FloatArray(4*6)

    // テクスチャ配列
    val textures = IntArray(2)

    // フレームバッファ
    val bufFrame = IntBuffer.allocate(1)

    // 深度バッファ用レンダ―バッファ
    val bufDepthRender = IntBuffer.allocate(1)

    // フレームバッファ用のテクスチャ
    val frameTexture = IntBuffer.allocate(1)

    init {
        // ビットマップをロード
        bmpArray.clear()
        val bmp0 = BitmapFactory.decodeResource(ctx.resources, R.drawable.cube_w44_px)
        val bmp1 = BitmapFactory.decodeResource(ctx.resources, R.drawable.cube_w44_py)
        val bmp2 = BitmapFactory.decodeResource(ctx.resources, R.drawable.cube_w44_pz)
        val bmp3 = BitmapFactory.decodeResource(ctx.resources, R.drawable.cube_w44_nx)
        val bmp4 = BitmapFactory.decodeResource(ctx.resources, R.drawable.cube_w44_ny)
        val bmp5 = BitmapFactory.decodeResource(ctx.resources, R.drawable.cube_w44_nz)
        bmpArray.add(bmp0)
        bmpArray.add(bmp1)
        bmpArray.add(bmp2)
        bmpArray.add(bmp3)
        bmpArray.add(bmp4)
        bmpArray.add(bmp5)
    }

    override fun onDrawFrame(gl: GL10?) {
        // 回転角度
        angle[0] =(angle[0]+1)%360
        val t0 = angle[0].toFloat()

        // フレームバッファをバインド
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,bufFrame[0])

        // フレームバッファへの６方向レンダリング
        render2FrameBuffer(t0)

        // フレームバッファのバインドを解除
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)

        // canvasを初期化
        GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f)
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

        // 背景用キューブをレンダリング
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,textures[0])
        Matrix.setIdentityM(matM,0)
        Matrix.scaleM(matM,0,100f,100f,100f)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderCubeMap.draw(modelCube,boCube,matM,matMVP,vecEye,0,0,"Default")

        // 動的キューブマップテクスチャを適用して球体をレンダリング
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,frameTexture[0])
        Matrix.setIdentityM(matM,0)
        Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
        shaderCubeMap.draw(modelSphere,boSphere,matM,matMVP,vecEye,0,1,"Default")

        // スペキュラライティングシェーダを使って
        // トーラスをレンダリング
        targetArray.forEachIndexed { id, target ->
            Matrix.setIdentityM(matM,0)
            Matrix.translateM(matM,0,torusPos[0+id*3],torusPos[1+id*3],torusPos[2+id*3])
            Matrix.rotateM(matM,0,t0,torusEye[0+id*3],torusEye[1+id*3],torusEye[2+id*3])
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            Matrix.invertM(matI,0,matM,0)

            val amb = FloatArray(4)
            amb[0] = torusAmb[0+id*4]
            amb[1] = torusAmb[1+id*4]
            amb[2] = torusAmb[2+id*4]
            amb[3] = torusAmb[3+id*4]
            shaderSpecular.draw(modelTorus,boTorus,matMVP,matI,vecLight,vecEye,amb)
        }
    }

    // フレームバッファへの６方向レンダリング
    private fun render2FrameBuffer(t1:Float) {
        targetArray.forEachIndexed { id, target ->
            // フレームバッファにテクスチャを関連付ける
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,GLES20.GL_COLOR_ATTACHMENT0,target,frameTexture[0],0)

            // フレームバッファを初期化
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
            GLES20.glClearDepthf(1f)
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

            // ライトベクトル
            val lightDirection = floatArrayOf(-1f,1f,1f)

            // 方角を判別して処理する
            when (target) {
                GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X -> {
                    torusEye[0]   = 1f;  torusEye[1]   =  0f;  torusEye[2]    = 0f;
                    torusCamUp[0] = 0f;  torusCamUp[1] = -1f;  torusCamUp[2] = 0f;
                    torusPos[0]   = 6f;  torusPos[1]   = 0f;   torusPos[2]    = 0f;
                    torusAmb[0]   = 1f;  torusAmb[1]   = 0.5f;  torusAmb[2] = 0.5f;   torusAmb[3] = 1f;
                }
                GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y -> {
                    torusEye[3]   = 0f;  torusEye[4]   = 1f;   torusEye[5]   = 0f;
                    torusCamUp[3] = 0f;  torusCamUp[4] = 0f;   torusCamUp[5] = 1f;
                    torusPos[3]   = 0f;  torusPos[4]   = 6f;   torusPos[5]   = 0f;
                    torusAmb[4] = 0.5f;  torusAmb[5] = 1f;     torusAmb[6] = 0.5f;    torusAmb[7] = 1f;
                }
                GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z -> {
                    torusEye[6]   = 0f;  torusEye[7]   = 0f;    torusEye[8]  = 1f;
                    torusCamUp[6] = 0f;  torusCamUp[7] = -1f;  torusCamUp[8] = 0f;
                    torusPos[6]   = 0f;  torusPos[7]   = 0f;    torusPos[8]  = 6f;
                    torusAmb[8] = 0.5f;  torusAmb[9] = 0.5f;    torusAmb[10] = 1f;    torusAmb[11] = 1f;
                }
                GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X -> {
                    torusEye[9]  = -1f;  torusEye[10] = 0f;     torusEye[11] = 0f;
                    torusCamUp[9] = 0f;  torusCamUp[10] = -1f;  torusCamUp[11] = 0f;
                    torusPos[9]   = -6f; torusPos[10] = 0f;     torusPos[11] = 0f;
                    torusAmb[12] = 0.5f; torusAmb[13] = 0f;     torusAmb[14] = 0f;   torusAmb[15] = 1f;
                }
                GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y -> {
                    torusEye[12] = 0f;   torusEye[13] = -1f;    torusEye[14] = 0f;
                    torusCamUp[12] = 0f; torusCamUp[13] = 0f;  torusCamUp[14] = -1f;
                    torusPos[12] = 0f;   torusPos[13] = -6f;    torusPos[14] = 0f;
                    torusAmb[16] = 0f;   torusAmb[17] = 0.5f;   torusAmb[18] = 0f;   torusAmb[19] = 1f;
                }
                GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z -> {
                    torusEye[15] = 0f;   torusEye[16] = 0f;     torusEye[17] = -1f;
                    torusCamUp[15] = 0f; torusCamUp[16] = -1f;  torusCamUp[17] = 0f;
                    torusPos[15] = 0f;   torusPos[16] = 0f;     torusPos[17] = -6f;
                    torusAmb[20] = 0f;   torusAmb[21] = 0f;     torusAmb[22] = 0.5f; torusAmb[23] = 1f;
                }
            }

            // ビュー×プロジェクション座標変換行列
            Matrix.setLookAtM(matV,0,
                    0f,0f,0f,
                    torusEye[0+id*3], torusEye[1+id*3], torusEye[2+id*3],
                    torusCamUp[0+id*3], torusCamUp[1+id*3], torusCamUp[2+id*3]
            )
            Matrix.perspectiveM(matP,0,90f,1f,0.1f,200f)
            Matrix.multiplyMM(matVP,0,matP,0,matV,0)

            // キューブマップテクスチャで背景用キューブをレンダリング
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textures[0])
            Matrix.setIdentityM(matM,0)
            Matrix.scaleM(matM,0,100f,100f,100f)
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            shaderCubeMap.draw(modelCube,boCube,matM,matMVP, floatArrayOf(0f,0f,0f),0,0,"FrameBuffer")

            // 視線ベクトルの変換
            var torusInvEye = FloatArray(3)
            torusInvEye[0] = -torusEye[0+id*3]
            torusInvEye[1] = -torusEye[1+id*3]
            torusInvEye[2] = -torusEye[2+id*3]

            // 環境色
            var amb = FloatArray(4)
            amb[0] = torusAmb[0+id*4]
            amb[1] = torusAmb[1+id*4]
            amb[2] = torusAmb[2+id*4]
            amb[3] = torusAmb[3+id*4]

            // スペキュラライティングシェーダでトーラスをレンダリング
            Matrix.setIdentityM(matM,0)
            Matrix.translateM(matM,0,torusPos[0+id*3],torusPos[1+id*3],torusPos[2+id*3])
            Matrix.rotateM(matM,0,t1,torusEye[0+id*3],torusEye[1+id*3],torusEye[2+id*3])
            Matrix.multiplyMM(matMVP,0,matVP,0,matM,0)
            Matrix.invertM(matI,0,matM,0)
            shaderSpecular.draw(modelTorus,boTorus,matMVP,matI,lightDirection,torusInvEye,amb)
        }

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        ratio = width.toFloat()/height.toFloat()

        renderW = width
        renderH = height

        // フレームバッファ生成
        GLES20.glGenFramebuffers(1,bufFrame)
        // 深度バッファ用レンダ―バッファ生成
        GLES20.glGenRenderbuffers(1,bufDepthRender)
        // フレームバッファ用テクスチャ生成
        GLES20.glGenTextures(1,frameTexture)
        MyGLES20Func.createFrameBuffer(renderW,renderH,0,bufFrame,bufDepthRender,frameTexture)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 深度テストを有効にする
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)

        // シェーダ(反射光ライティング)
        shaderSpecular = WV047ShaderSpecular()
        shaderSpecular.loadShader()

        // シェーダ(キューブ環境マッピング)
        shaderCubeMap = WV047ShaderCubeMap()
        shaderCubeMap.loadShader()

        // モデル生成(立方体)
        modelCube = Cube01Model()
        modelCube.createPath(mapOf(
                "pattern" to 2f,
                "scale"   to 2f,
                "colorR"  to 1f,
                "colorG"  to 1f,
                "colorB"  to 1f,
                "colorA"  to 1f
        ))

        // モデル生成(球体)
        modelSphere = Sphere01Model()
        modelSphere.createPath(mapOf(
                "row"    to 32f,
                "column" to 32f,
                "radius" to 3f,
                "colorR" to 1f,
                "colorG" to 1f,
                "colorB" to 1f,
                "colorA" to 1f
        ))

        // モデル生成(トーラス)
        modelTorus = Torus01Model()
        modelTorus.createPath(mapOf(
                "row"     to 32f,
                "column"  to 32f,
                "iradius" to 0.5f,
                "oradius" to 1f,
                "colorR"  to 1f,
                "colorG"  to 1f,
                "colorB"  to 1f,
                "colorA"  to 1f
        ))

        // VBO(立方体)
        boCube = ES20VBOIpnc()
        boCube.makeVIBO(modelCube)

        // VBO(球体)
        boSphere = ES20VBOIpnc()
        boSphere.makeVIBO(modelSphere)

        // VBO(トーラス)
        boTorus = ES20VBOIpnc()
        boTorus.makeVIBO(modelTorus)

        // キューブマップを生成
        generateCubeMap()

        // 光源位置
        vecLight[0] = -1f
        vecLight[1] =  1f
        vecLight[2] =  1f

        // 視点位置
        vecEye[0] =  0f
        vecEye[1] =  0f
        vecEye[2] = 20f
    }

    private fun generateCubeMap() {
        // テクスチャ作成し、idをtexturesに保存
        GLES20.glGenTextures(1,textures,0)
        MyGLES20Func.checkGlError("glGenTextures")

        // テクスチャをキューブマップにバインド
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,textures[0])

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
        boCube.deleteVIBO()
        boSphere.deleteVIBO()
        boTorus.deleteVIBO()
        shaderSpecular.deleteShader()
        shaderCubeMap.deleteShader()

        GLES20.glDeleteTextures(textures.size,textures,0)
    }
}