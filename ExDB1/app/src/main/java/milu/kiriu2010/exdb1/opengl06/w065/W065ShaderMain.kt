package milu.kiriu2010.exdb1.opengl06.w065

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.es20.ES20MgShader

// ----------------------------------------
// シェーダ(メイン)
// ----------------------------------------
// https://wgld.org/d/webgl/w065.html
// ----------------------------------------
class W065ShaderMain: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            uniform   mat4  u_matM;
            uniform   mat4  u_matMVP;
            uniform   mat4  u_matINV;
            uniform   mat4  u_matO;
            uniform   vec3  u_vecLight;
            uniform   vec3  u_vecCenter;
            uniform   vec3  u_vecEye;
            uniform   vec4  u_ambientColor;
            varying   vec4  v_Color;
            varying   vec4  v_TextureCoord;
            varying   float v_DotLE;

            void main() {
                vec3   pos      = (u_matM * vec4(a_Position,1.0)).xyz;
                vec3   invLight = normalize(u_matINV * vec4(u_vecLight-pos, 0.0)).xyz;
                vec3   invEye   = normalize(u_matINV * vec4(u_vecEye      , 0.0)).xyz;
                vec3   halfLE   = normalize(invLight + invEye);
                float  diffuse  = clamp(dot(a_Normal,invLight), 0.0, 1.0);
                float  specular = pow(clamp(dot(a_Normal, halfLE), 0.0, 1.0), 50.0);
                v_Color = a_Color*vec4(vec3(diffuse),1.0) + vec4(vec3(specular),0.0) + u_ambientColor;
                v_TextureCoord  = u_matO * vec4(pos,1.0);
                v_DotLE         = pow(max(dot(normalize(u_vecCenter-u_vecEye),normalize(u_vecLight)),0.0), 10.0);
                gl_Position     = u_matMVP   * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   sampler2D u_TextureBlur;
            varying   vec4      v_Color;
            varying   vec4      v_TextureCoord;
            varying   float     v_DotLE;

            const vec3 throughColor = vec3(1.0,0.5,0.2);

            void main() {
                float bDepth  = pow(texture2DProj(u_TextureBlur, v_TextureCoord).r, 20.0);
                vec3  through = throughColor * v_DotLE * bDepth;
                gl_FragColor  = vec4(v_Color.rgb + through, v_Color.a);
            }
            """.trimIndent()


    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )
        return this
    }

    fun draw(model: MgModelAbs,
             matM: FloatArray,
             matMVP: FloatArray,
             matINV: FloatArray,
             matO: FloatArray,
             u_vecLight: FloatArray,
             u_vecCenter: FloatArray,
             u_vecEye: FloatArray,
             u_ambientColor: FloatArray,
             u_TextureBlur: Int) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram", this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Position", this,model)

        // attribute(法線)
        model.bufNor.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Normal").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufNor)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Normal", this,model)

        // attribute(色)
        model.bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false, 4*4, model.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Color", this,model)

        // uniform(モデル座標変換行列)
        GLES20.glGetUniformLocation(programHandle,"u_matM").also {
            GLES20.glUniformMatrix4fv(it,1,false,matM,0)
        }
        MyGLES20Func.checkGlError2("u_matM", this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLES20Func.checkGlError2("u_matMVP", this,model)

        // uniform(逆行列)
        GLES20.glGetUniformLocation(programHandle,"u_matINV").also {
            GLES20.glUniformMatrix4fv(it,1,false,matINV,0)
        }
        MyGLES20Func.checkGlError2("u_matINV", this,model)

        // uniform(正射影座標行列)
        GLES20.glGetUniformLocation(programHandle,"u_matO").also {
            GLES20.glUniformMatrix4fv(it,1,false,matO,0)
        }
        MyGLES20Func.checkGlError2("u_matO", this,model)

        // uniform(ライティング)
        GLES20.glGetUniformLocation(programHandle,"u_vecLight").also {
            GLES20.glUniform3fv(it,1,u_vecLight,0)
        }
        MyGLES20Func.checkGlError2("u_vecLight", this,model)

        // uniform(注視点)
        GLES20.glGetUniformLocation(programHandle,"u_vecCenter").also {
            GLES20.glUniform3fv(it,1,u_vecCenter,0)
        }
        MyGLES20Func.checkGlError2("u_vecCenter", this,model)

        // uniform(視点座標)
        GLES20.glGetUniformLocation(programHandle,"u_vecEye").also {
            GLES20.glUniform3fv(it,1,u_vecEye,0)
        }
        MyGLES20Func.checkGlError2("u_vecEye", this,model)

        // uniform(環境色)
        GLES20.glGetUniformLocation(programHandle, "u_ambientColor").also {
            GLES20.glUniform4fv(it, 1,u_ambientColor,0)
        }
        MyGLES20Func.checkGlError2("u_ambientColor", this,model)

        // uniform(テクスチャ)
        GLES20.glGetUniformLocation(programHandle, "u_TextureBlur").also {
            GLES20.glUniform1i(it, u_TextureBlur)
        }
        MyGLES20Func.checkGlError2("u_TextureBlur", this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}