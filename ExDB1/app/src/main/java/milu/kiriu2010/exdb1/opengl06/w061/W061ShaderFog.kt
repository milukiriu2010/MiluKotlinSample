package milu.kiriu2010.exdb1.opengl06.w061

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.shader.MgShader

// シェーダ(フォグ)
class W061ShaderFog: MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec4  a_Color;
            attribute vec2  a_TextureCoord;
            uniform   mat4  u_matM;
            uniform   mat4  u_matMVP;
            uniform   mat4  u_matTex;
            varying   vec4  v_Position;
            varying   vec4  v_Color;
            varying   vec2  v_TextureCoord;
            varying   vec4  v_TexProjCoord;

            void main() {
                vec3   pos      = (u_matM * vec4(a_Position, 1.0)).xyz;
                v_Position      = u_matMVP * vec4(a_Position, 1.0);
                v_Color         = a_Color;
                v_TextureCoord  = a_TextureCoord;
                v_TexProjCoord  = u_matTex * vec4(pos, 1.0);
                gl_Position     = v_Position;
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   vec2      u_offset;
            uniform   float     u_distLength;
            uniform   sampler2D u_TextureDepth;
            uniform   sampler2D u_TextureNoise;
            uniform   int       u_softParticle;
            varying   vec4      v_Position;
            varying   vec4      v_Color;
            varying   vec2      v_TextureCoord;
            varying   vec4      v_TexProjCoord;

            // フレームバッファに描きこまれた深度値を本来の値に変換する
            float restDepth(vec4 RGBA) {
                const float rMask = 1.0;
                const float gMask = 1.0/255.0;
                const float bMask = 1.0/(255.0*255.0);
                const float aMask = 1.0/(255.0*255.0*255.0);
                float depth = dot(RGBA, vec4(rMask, gMask, bMask, aMask));
                return depth;
            }

            const float near = 0.1;
            const float far  = 10.0;
            const float linearDepth = 1.0/(far-near);

            void main() {
                float depth      = restDepth(texture2DProj(u_TextureDepth, v_TexProjCoord));
                float linearPos  = linearDepth * length(v_Position);
                vec4  noiseColor = texture2D(u_TextureNoise, v_TextureCoord + u_offset);
                float alpha      = 1.0 - clamp(length(vec2(0.5,1.0) - v_TextureCoord)*2.0, 0.0, 1.0);
                if (bool(u_softParticle)) {
                    float distance = abs(depth-linearPos);
                    if (u_distLength >= distance) {
                        float d = distance/u_distLength;
                        alpha *= d;
                    }
                }
                gl_FragColor = vec4(v_Color.rgb, noiseColor.r * alpha);
            }
            """.trimIndent()


    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Color","a_TextureCoord") )
        return this
    }

    fun draw(model: MgModelAbs,
             matM: FloatArray,
             matMVP: FloatArray,
             matTex: FloatArray,
             u_offset: FloatArray,
             u_distLength: Float,
             u_TextureDepth: Int,
             u_TextureNoise: Int,
             u_softParticle: Int) {

        GLES20.glUseProgram(programHandle)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Position:${model.javaClass.simpleName}")

        // attribute(色)
        model.bufCol.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Color").also {
            GLES20.glVertexAttribPointer(it,4,GLES20.GL_FLOAT,false, 4*4, model.bufCol)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_Color:${model.javaClass.simpleName}")

        // attribute(テクスチャ座標)
        model.bufTxc.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_TextureCoord").also {
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false, 2*4, model.bufTxc)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError("a_TextureCoord:${model.javaClass.simpleName}")

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glGetUniformLocation(programHandle,"u_matMVP").also {
            GLES20.glUniformMatrix4fv(it,1,false,matMVP,0)
        }
        MyGLFunc.checkGlError("u_matMVP:${model.javaClass.simpleName}")

        // uniform()
        GLES20.glGetUniformLocation(programHandle,"u_matTex").also {
            GLES20.glUniformMatrix4fv(it,1,false,matTex,0)
        }
        MyGLFunc.checkGlError("u_matTex:${model.javaClass.simpleName}")

        // uniform()
        GLES20.glGetUniformLocation(programHandle,"u_offset").also {
            GLES20.glUniform2fv(it,1,u_offset,0)
        }
        MyGLFunc.checkGlError("u_offset:${model.javaClass.simpleName}")

        // uniform()
        GLES20.glGetUniformLocation(programHandle,"u_distLength").also {
            GLES20.glUniform1f(it,u_distLength)
        }
        MyGLFunc.checkGlError("u_distLength:${model.javaClass.simpleName}")

        // uniform(テクスチャ)
        GLES20.glGetUniformLocation(programHandle, "u_TextureDepth").also {
            GLES20.glUniform1i(it, u_TextureDepth)
        }
        MyGLFunc.checkGlError("u_TextureDepth:${model.javaClass.simpleName}")

        // uniform(テクスチャ)
        GLES20.glGetUniformLocation(programHandle, "u_TextureNoise").also {
            GLES20.glUniform1i(it, u_TextureNoise)
        }
        MyGLFunc.checkGlError("u_TextureNoise:${model.javaClass.simpleName}")

        // uniform()
        GLES20.glGetUniformLocation(programHandle, "u_softParticle").also {
            GLES20.glUniform1i(it, u_softParticle)
        }
        MyGLFunc.checkGlError("u_softParticle:${model.javaClass.simpleName}")

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)
    }
}