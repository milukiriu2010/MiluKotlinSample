package milu.kiriu2010.exdb1.opengl06.w062v

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// -----------------------------------------------------
// シェーダ(ステンシル鏡面反射):VBOあり
// OpenGL ES 2.0
// -----------------------------------------------------
//   正射影で画面全体にかぶさるようにレンダリングする
//   鏡面世界を床面に合成する
// -----------------------------------------------------
// https://wgld.org/d/webgl/w062.html
// -----------------------------------------------------
class WV062ShaderMirror: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec2  a_TextureCoord;
            // 正射影座標変換行列
            uniform   mat4  u_matO;
            varying   vec2  v_TextureCoord;

            void main() {
                v_TextureCoord = a_TextureCoord;
                gl_Position    = u_matO * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   sampler2D u_Texture;
            // 映り込み係数
            // どの程度の透明度で鏡面世界を合成するのかを決める係数
            uniform   float     u_alpha;
            varying   vec2      v_TextureCoord;

            void main() {
                vec2 tc = vec2(v_TextureCoord.s, 1.0-v_TextureCoord.t);
                gl_FragColor = vec4(texture2D(u_Texture,tc).rgb, u_alpha);
            }
            """.trimIndent()


    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle)

        // ----------------------------------------------
        // attributeハンドルに値をセット
        // ----------------------------------------------
        hATTR = IntArray(3)
        // 属性(頂点)
        hATTR[0] = GLES20.glGetAttribLocation(programHandle, "a_Position").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_Position:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_Position:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_Position:glGetAttribLocation")

        // 属性(テクスチャ座標)
        hATTR[1] = GLES20.glGetAttribLocation(programHandle, "a_TextureCoord").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_TextureCoord:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_TextureCoord:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_TextureCoord:glGetAttribLocation")

        // ----------------------------------------------
        // uniformハンドルに値をセット
        // ----------------------------------------------
        hUNI = IntArray(3)

        // uniform(正射影座標変換行列)
        hUNI[0] = GLES20.glGetUniformLocation(programHandle,"u_matO")
        MyGLES20Func.checkGlError("u_matO:glGetUniformLocation")

        // uniform(テクスチャユニット)
        hUNI[1] = GLES20.glGetUniformLocation(programHandle, "u_Texture")
        MyGLES20Func.checkGlError("u_Texture:glGetUniformLocation")

        // uniform(どの程度の透明度で鏡面世界を合成するのかを決める係数)
        hUNI[2] = GLES20.glGetUniformLocation(programHandle, "u_alpha")
        MyGLES20Func.checkGlError("u_alpha:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_matO: FloatArray,
             u_Texture: Int,
             u_alpha: Float) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[0])
        GLES20.glVertexAttribPointer(hATTR[0],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(テクスチャ座標)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[1])
        GLES20.glVertexAttribPointer(hATTR[1],2,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_TextureCoord",this,model)

        // uniform(正射影座標変換行列)
        GLES20.glUniformMatrix4fv(hUNI[0],1,false,u_matO,0)
        MyGLES20Func.checkGlError2("u_matO",this,model)

        // uniform(テクスチャユニット)
        GLES20.glUniform1i(hUNI[1], u_Texture)
        MyGLES20Func.checkGlError2("u_Texture",this,model)

        // uniform(どの程度の透明度で鏡面世界を合成するのかを決める係数)
        GLES20.glUniform1f(hUNI[2], u_alpha)
        MyGLES20Func.checkGlError2("u_alpha",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}