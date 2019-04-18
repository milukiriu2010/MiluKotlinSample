package milu.kiriu2010.exdb1.opengl04.w042

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc

class W042Shader {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            attribute vec2  a_TextureCoord;
            uniform   mat4  u_matM;
            uniform   mat4  u_matMVP;
            uniform   mat4  u_matINV;
            uniform   vec3  u_vecLight;
            uniform   vec3  u_vecEye;
            varying   vec4  v_Color;
            varying   vec2  v_TextureCoord;
            varying   vec3  v_vecLight;
            varying   vec3  v_vecEye;

            void main() {
                vec3 pos      = (u_matM   * vec4(a_Position,0.0)).xyz;
                vec3 invEye   = (u_matINV * vec4(u_vecEye,0.0)).xyz;
                vec3 invLight = (u_matINV * vec4(u_vecLight,0.0)).xyz;
                vec3 eye      = invEye - pos;
                vec3 light    = invLight - pos;
                vec3 n = normalize(a_Normal);
                vec3 t = normalize(cross(a_Normal,vec3(0.0,1.0,0.0)));
                vec3 b = cross(n,t);
                v_vecEye.x = dot(t,eye);
                v_vecEye.y = dot(b,eye);
                v_vecEye.z = dot(n,eye);
                normalize(v_vecEye);
                v_vecLight.x = dot(t,light);
                v_vecLight.y = dot(b,light);
                v_vecLight.z = dot(n,light);
                normalize(v_vecLight);
                v_Color        = a_Color;
                v_TextureCoord = a_TextureCoord;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   sampler2D u_Texture0;
            varying   vec4      v_Color;
            varying   vec2      v_TextureCoord;
            varying   vec3      v_vecLight;
            varying   vec3      v_vecEye;

            void main() {
                vec3  mNormal   = (texture2D(u_Texture0, v_TextureCoord) * 2.0 - 1.0).rgb;
                vec3  light     = normalize(v_vecLight);
                vec3  eye       = normalize(v_vecEye);
                vec3  halfLE    = normalize(light+eye);
                float diffuse   = clamp(dot(mNormal, light), 0.1, 1.0);
                float specular  = pow(clamp(dot(mNormal,halfLE) ,0.0,1.0), 100.0);
                vec4  destColor = v_Color * vec4(vec3(diffuse),1.0) + vec4(vec3(specular),1.0);
                gl_FragColor  = destColor;
            }
            """.trimIndent()

    fun loadShader(): Int {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        return MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position","a_Normal","a_Color","a_TextureCoord") )
    }
}
