package milu.kiriu2010.exdb1.opengl.begin02

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.Matrix
import java.nio.FloatBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import android.os.SystemClock




// http://developer.wonderpla.net/entry/blog/engineer/GLSurfaceView/
class MyTriangle03Renderer: GLSurfaceView.Renderer {
    private val mModelMatrix = FloatArray(16)       // ワールド行列
    private val mViewMatrix = FloatArray(16)        // ビュー行列
    private val mProjectionMatrix = FloatArray(16)  // 射影行列
    private val mMVPMatrix = FloatArray(16)         // これらの積行列

    private lateinit var mTriangleVertices: FloatBuffer  // 頂点バッファ

    private var mMVPMatrixHandle: Int = 0  // u_MVPMatrixのハンドル
    private var mPositionHandle: Int = 0   // a_Positionのハンドル
    private var mColorHandle: Int = 0      // a_Colorのハンドル

    private val mBytesPerFloat = 4                                  // floatのバイト数
    private val mStrideBytes = 7 * mBytesPerFloat                   // ストライドバイト数
    private val mPositionOffset = 0                                 // 位置情報の先頭位置
    private val mPositionDataSize = 3                               // 位置情報のデータサイズ
    private val mColorOffset = mPositionOffset + mPositionDataSize  // 色情報の先頭位置
    private val mColorDataSize = 4                                  // 色情報のデータサイズ

    init {
        // 頂点バッファ生成
        val triangleVerticesData = floatArrayOf(
                // 各頂点情報
                // (座標属性) X, Y, Z,
                // (色属性) R, G, B, A
                -0.5f, -0.25f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.25f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                0.0f, 0.559016994f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f)

        // バッファを確保し、バイトオーダーをネイティブに合わせる(Javaとネイティブではバイトオーダーが異なる)
        mTriangleVertices = ByteBuffer.allocateDirect(triangleVerticesData.size * mBytesPerFloat)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
        mTriangleVertices.put(triangleVerticesData).position(0)  // データをバッファへ
    }

    override fun onDrawFrame(gl: GL10) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)  // バッファのクリア

        // プリミティブをアニメーション
        // 経過秒から回転角度を求める(10秒/周)
        val time = SystemClock.uptimeMillis() % 10000L
        val angleInDegrees = 360.0f / 10000.0f * time.toInt()

        // ワールド行列に対して回転をかける
        Matrix.setIdentityM(mModelMatrix, 0)  // 単位行列でリセット
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f)  // 回転行列
        drawTriangle(mTriangleVertices)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        // スクリーンが変わり画角を変更する場合、射影行列を作り直す
        GLES20.glViewport(0, 0, width, height)

        val ratio = width.toFloat()/height.toFloat()
        val left = -ratio
        val bottom = -1.0f
        val top = 1.0f
        val near = 1.0f
        val far = 10.0f

        Matrix.frustumM(mProjectionMatrix, 0, left, ratio, bottom, top, near, far)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)  // 描画領域を黒色でクリア

        // カメラ(ビュー行列)を設定
        val eye = floatArrayOf(0.0f, 0.0f, 1.5f)
        val look = floatArrayOf(0.0f, 0.0f, -5.0f)
        val up = floatArrayOf(0.0f, 1.0f, 0.0f)
        Matrix.setLookAtM(mViewMatrix, 0, eye[0], eye[1], eye[2], look[0], look[1], look[2], up[0], up[1], up[2])

        // バーテックスシェーダ
        val vertexShader =
                ( "uniform mat4 u_MVPMatrix;      \n"
                + "attribute vec4 a_Position;     \n"
                + "attribute vec4 a_Color;        \n"
                + "varying vec4 v_Color;          \n"
                + "void main()                    \n"
                + "{                              \n"
                + "   v_Color = a_Color;          \n"
                + "   gl_Position = u_MVPMatrix   \n"
                + "               * a_Position;   \n"
                + "}                              \n")

        // フラグメントシェーダ
        val fragmentShader =
                ("precision mediump float;       \n"
                + "varying vec4 v_Color;          \n"
                + "void main()                    \n"
                + "{                              \n"
                + "   gl_FragColor = v_Color;     \n"
                + "}                              \n")

        // バーテックスシェーダをコンパイル
        var vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)
        if (vertexShaderHandle != 0) {
            GLES20.glShaderSource(vertexShaderHandle, vertexShader)  // シェーダソースを送信し
            GLES20.glCompileShader(vertexShaderHandle)  // コンパイル

            // コンパイル結果のチェック
            val compileStatus = IntArray(1)
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

            if (compileStatus[0] == 0) {
                // コンパイル失敗
                GLES20.glDeleteShader(vertexShaderHandle)
                vertexShaderHandle = 0
            }
        }
        if (vertexShaderHandle == 0) {
            throw RuntimeException("Error creating vertex shader.")
        }

        // フラグメントシェーダをコンパイル
        var fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)
        if (fragmentShaderHandle != 0) {
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader)
            GLES20.glCompileShader(fragmentShaderHandle)

            val compileStatus = IntArray(1)
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(fragmentShaderHandle)
                fragmentShaderHandle = 0
            }
        }
        if (fragmentShaderHandle == 0) {
            throw RuntimeException("Error creating fragment shader.")
        }

        // シェーダプログラムをリンク
        var programHandle = GLES20.glCreateProgram()
        if (programHandle != 0) {
            GLES20.glAttachShader(programHandle, vertexShaderHandle)  // バーテックスシェーダをアタッチ
            GLES20.glAttachShader(programHandle, fragmentShaderHandle)  // フラグメントシェーダをアタッチ
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position")  // attributeのindexを設定
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color")  // attributeのindexを設定
            GLES20.glLinkProgram(programHandle)  // バーテックスシェーダとフラグメントシェーダをプログラムへリンク

            // リンク結果のチェック
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0)

            if (linkStatus[0] == 0) {
                // リンク失敗
                GLES20.glDeleteProgram(programHandle)
                programHandle = 0
            }
        }
        if (programHandle == 0) {
            throw RuntimeException("Error creating program.")
        }

        // ハンドル(ポインタ)の取得
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix")
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position")
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color")

        // シェーダプログラム適用
        GLES20.glUseProgram(programHandle)
    }

    // 三角形を描画する
    private fun drawTriangle(aTriangleBuffer: FloatBuffer) {
        // OpenGLに頂点バッファを渡す
        aTriangleBuffer.position(mPositionOffset)  // 頂点バッファを座標属性にセット
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false, mStrideBytes, aTriangleBuffer)  // ポインタと座標属性を結び付ける
        GLES20.glEnableVertexAttribArray(mPositionHandle)  // 座標属性有効

        aTriangleBuffer.position(mColorOffset)  // 頂点バッファを色属性にセット
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false, mStrideBytes, aTriangleBuffer)  // ポインタと色属性を結び付ける
        GLES20.glEnableVertexAttribArray(mColorHandle)  // 色属性有効

        // ワールド行列×ビュー行列×射影行列をセット
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0)
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0)
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3)  // 三角形を描画
    }
}
