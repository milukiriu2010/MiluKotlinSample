package milu.kiriu2010.exdb1.opengl03.w041

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.MyGLFunc

class W041BShader {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec4  a_Color;
            uniform   mat4  u_matMVP;
            varying   vec4  v_Color;

            void main() {
                v_Color        = a_Color;
                gl_Position    = u_matMVP * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   sampler2D u_Texture0;
            uniform   int       u_useBlur;
            varying   vec4      v_Color;

            void main() {
                vec2 tFrag     = vec2(1.0/256.0);
                vec4 destColor = texture2D(u_Texture0, gl_FragCoord.st * tFrag);
                if(bool(u_useBlur)){
                    destColor *= 0.36;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-1.0,  1.0)) * tFrag) * 0.04;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 0.0,  1.0)) * tFrag) * 0.04;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 1.0,  1.0)) * tFrag) * 0.04;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-1.0,  0.0)) * tFrag) * 0.04;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 1.0,  0.0)) * tFrag) * 0.04;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-1.0, -1.0)) * tFrag) * 0.04;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 0.0, -1.0)) * tFrag) * 0.04;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 1.0, -1.0)) * tFrag) * 0.04;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-2.0,  2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-1.0,  2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 0.0,  2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 1.0,  2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 2.0,  2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-2.0,  1.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 2.0,  1.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-2.0,  0.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 2.0,  0.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-2.0, -1.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 2.0, -1.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-2.0, -2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2(-1.0, -2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 0.0, -2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 1.0, -2.0)) * tFrag) * 0.02;
                    destColor += texture2D(u_Texture0, (gl_FragCoord.st + vec2( 2.0, -2.0)) * tFrag) * 0.02;
                }
                gl_FragColor = v_Color * destColor;
            }
            """.trimIndent()

    fun loadShader(): Int {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        return MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Color") )
    }
}
