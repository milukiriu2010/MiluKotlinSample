package milu.kiriu2010.exdb1.opengl01.labo01

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLUtils
import android.graphics.Bitmap
import android.graphics.Color
import android.util.AttributeSet
import java.nio.ByteBuffer
import java.nio.ByteOrder

// ----------------------------------------------
// テクスチャ用画像をプログラムで生成しOpenGLを使って描画
// OpenGL ES 2.0
// ----------------------------------------------
class Labo01View : GLSurfaceView, GLSurfaceView.Renderer {

    private var _gl_prog: Int = 0
    private var _gl_vars: MutableMap<String, Int> = mutableMapOf()

    private lateinit var _vertex_buffer: FloatBuffer
    private lateinit var _texture_buffer: FloatBuffer

    private var _draw_counter: Int = 0

    constructor(ctx: Context): super(ctx) {
    }

    /* @JvmOverloads */
    constructor(
            ctx: Context,
            attrs: AttributeSet? = null)
            : super(ctx, attrs) {

    }

    init {
        // OpenGL ES 2
        setEGLContextClientVersion(2);

        _draw_counter = 0;

        setRenderer(this);

        // 自動で連続描画を行う
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY)
    }

    override fun onDrawFrame(gl: GL10?) {
// クリア色設定
        val l = (Math.sin(_draw_counter / 100.0) / 2.0 + 0.5).toFloat()
        GLES20.glClearColor(l, l, l, 1.0f)

        // カラーバッファクリア
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // 長方形のテクスチャ変数設定
        GLES20.glEnableVertexAttribArray(_gl_vars.get("A_texture_uv") ?: 0)
        GLES20.glVertexAttribPointer(_gl_vars.get("A_texture_uv") ?: 0, 2, GLES20.GL_FLOAT, false, 0, _texture_buffer)

        // 長方形の頂点変数設定
        GLES20.glEnableVertexAttribArray(_gl_vars.get("A_position") ?: 0)
        GLES20.glVertexAttribPointer(_gl_vars.get("A_position") ?: 0, 2, GLES20.GL_FLOAT, false, 0, _vertex_buffer)

        // 長方形を描画
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        // 変数解除
        GLES20.glDisableVertexAttribArray(_gl_vars.get("A_texture_uv") ?: 0)
        GLES20.glDisableVertexAttribArray(_gl_vars.get("A_position") ?: 0)

        _draw_counter++
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // 画面全体を(-1.0, -1.0)-(1.0, 1.0)の座標系に対応させる
        GLES20.glViewport(0, 0, width, height);
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        val vs_code =
                """
                attribute vec2 A_position;
                attribute vec2 A_texture_uv;
                varying vec2 V_texture_uv;
                
                void main(void){
                    gl_Position = vec4(A_position, 0.0, 1.0);
                    V_texture_uv = A_texture_uv;
                }
                """
        val fs_code =
                """
                precision mediump float;
                uniform sampler2D texture0;
                varying vec2 V_texture_uv;
                
                void main() {	
                    gl_FragColor = texture2D(texture0, V_texture_uv);
                }
                """

        val v_shader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)
        val f_shader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)

        // Vertexシェーダーのコードをコンパイル
        GLES20.glShaderSource(v_shader, vs_code)
        GLES20.glCompileShader(v_shader)

        // Fragmentシェーダーのコードをコンパイル
        GLES20.glShaderSource(f_shader, fs_code)
        GLES20.glCompileShader(f_shader)

        // Programを作成
        _gl_prog = GLES20.glCreateProgram()

        // Programのシェーダーを設定
        GLES20.glAttachShader(_gl_prog, v_shader)
        GLES20.glAttachShader(_gl_prog, f_shader)

        GLES20.glLinkProgram(_gl_prog)
        GLES20.glUseProgram(_gl_prog)

        _gl_vars = HashMap()

        // gl変数を保存
        _gl_vars["A_position"] = GLES20.glGetAttribLocation(_gl_prog, "A_position")
        _gl_vars["A_texture_uv"] = GLES20.glGetAttribLocation(_gl_prog, "A_texture_uv")
        _gl_vars["texture0"] = GLES20.glGetUniformLocation(_gl_prog, "texture0")

        // テクスチャ用Bitmap作成
        var bmp: Bitmap? = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888)

        // テクスチャ用の画像を描画
        for (y in 0..31) {
            for (x in 0..31) {
                if (x == 0 || x == 31 || y == 0 || y == 31) {
                    bmp!!.setPixel(x, y, Color.argb(255, 0, y * 8, 255))
                } else {
                    bmp!!.setPixel(x, y, Color.argb(255, 255 - x * 8, y * 4, 0))
                }
            }
        }

        // テクスチャ0を有効化
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)

        val textures = IntArray(1)

        // テクスチャ作成しidをtextures[0]に保存
        GLES20.glGenTextures(1, textures, 0)

        // テクスチャ0にtextures[0]をバインド
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])

        // 縮小時の補間設定
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        // 拡大時の補間設定
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

        // bmpをテクスチャ0に設定
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0)

        bmp!!.recycle()
        //bmp = null

        // gl変数texture0に0を設定
        GLES20.glUniform1i(_gl_vars["texture0"] ?: 0, 0)

        // 「全体」を覆うテクスチャ座標配列を作成
        val texture_uvs = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f)

        val tex_bb = ByteBuffer.allocateDirect(texture_uvs.size * 4)
        tex_bb.order(ByteOrder.nativeOrder())

        _texture_buffer = tex_bb.asFloatBuffer()
        _texture_buffer.put(texture_uvs)
        _texture_buffer.position(0)

        // （-0,9, -0.9)-(0.9, 0.9)の長方形頂点データ作成
        val vertex_array = floatArrayOf(-0.9f, -0.9f, -0.9f, 0.9f, 0.9f, -0.9f, 0.9f, 0.9f)

        // 頂点データをバッファに格納
        val vt_bb = ByteBuffer.allocateDirect(vertex_array.size * 4)
        vt_bb.order(ByteOrder.nativeOrder())

        _vertex_buffer = vt_bb.asFloatBuffer()
        _vertex_buffer.put(vertex_array)
        _vertex_buffer.position(0)
    }
}