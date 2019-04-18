package milu.kiriu2010.exdb1.opengl04.w045

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc

class W045Shader {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            attribute vec2  a_TextureCoord;
            uniform   mat4  u_matM;
            uniform   mat4  u_matMVP;
            varying   vec3  v_Position;
            varying   vec4  v_Color;
            varying   vec2  v_TextureCoord;
            varying   vec3  v_tNormal;
            varying   vec3  v_tTangent;

            void main() {
                v_Position    = (u_matM * vec4(a_Position,1.0)).xyz;
                v_Color       = a_Color;
                v_tNormal     = (u_matM * vec4(a_Normal  ,0.0)).xyz;
                v_tTangent    = cross(v_tNormal,vec3(0.0,1.0,0.0));
                gl_Position   = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump     float;

            uniform   vec3        u_vecEye;
            uniform   sampler2D   u_normalMap;
            uniform   samplerCube u_CubeTexture;
            uniform   int         u_Reflection;
            varying   vec3        v_Position;
            varying   vec4        v_Color;
            varying   vec2        v_TextureCoord;
            varying   vec3        v_tNormal;
            varying   vec3        v_tTangent;

            void main() {
                vec3  tBinormal = cross(v_tNormal, v_tTangent);
                mat3  mView     = mat3(v_tTangent, tBinormal, v_tNormal);
                vec3  mNormal   = mView * (texture2D(u_normalMap,v_TextureCoord)*2.0-1.0).rgb;
                vec3  ref;
                if (bool(u_Reflection)) {
                    ref = reflect(v_Position - u_vecEye, mNormal);
                }
                else {
                    ref = v_tNormal;
                }
                vec4  envColor  = textureCube(u_CubeTexture, ref);
                vec4  destColor = v_Color * envColor;
                gl_FragColor    = destColor;
            }
            """.trimIndent()

    fun loadShader(): Int {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        return MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color") )
    }
}
