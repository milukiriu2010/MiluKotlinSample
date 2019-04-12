package milu.kiriu2010.exdb1.opengl05.w051

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.MyGLFunc

// スクリーンレンダリング用シェーダ
class W051ShaderScreen {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec3  a_Normal;
            attribute vec4  a_Color;
            // カメラ視点のモデル座標変換行列
            uniform   mat4  u_matM;
            // カメラ支店の座標変換行列
            uniform   mat4  u_matMVP;
            // 射影テクスチャマッピング用行列
            uniform   mat4  u_matTex;
            // ライト視点の座標変換行列
            uniform   mat4  u_matLight;
            varying   vec3  v_Position;
            varying   vec3  v_Normal;
            varying   vec4  v_Color;
            varying   vec4  v_TexCoord;
            varying   vec4  v_Depth;

            void main() {
                v_Position  = (u_matM * vec4(a_Position, 1.0)).xyz;
                v_Normal    = a_Normal;
                v_Color     = a_Color;
                v_TexCoord  = u_matTex   * vec4(v_Position, 1.0);
                v_Depth     = u_matLight * vec4(a_Position, 1.0);
                gl_Position = u_matMVP   * vec4(a_Position, 1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            uniform   mat4        u_matINV;
            uniform   vec3        u_vecEye;
            uniform   sampler2D   u_Texture0;
            uniform   int         u_depthBuffer;
            varying   vec3        v_Position;
            varying   vec3        v_Normal;
            varying   vec4        v_Color;
            varying   vec4        v_TexCoord;
            varying   vec4        v_Depth;

            float restDepth(vec4 RGBA) {
                const float rMask = 1.0;
                const float gMask = 1.0/255.0;
                const float bMask = 1.0/(255.0*255.0);
                const float aMask = 1.0/(255.0*255.0*255.0);
                float depth = dot(RGBA, vec4(rMask, gMask, bMask, aMask));
                return depth;
            }

            void main() {
                vec3  light      = u_vecEye - v_Position;
                vec3  invLight   = normalize(u_matINV * vec4(light, 0.0)).xyz;
                float diffuse    = clamp(dot(v_Normal, invLight), 0.2, 1.0);
                float shadow     = restDepth(texture2DProj(u_Texture0, v_TexCoord));
                vec4  depthColor = vec4(1.0);
                if (v_Depth.w > 0.0) {
                    if (bool(u_depthBuffer)) {
                        vec4 lightCoord = v_Depth/v_Depth.w;
                        if ( (lightCoord.z - 0.0001) > shadow ) {
                            depthColor = vec4(0.5,0.5,0.5,1.0);
                        }
                    }
                    else {
                        float  near = 0.1;
                        float  far  = 150.0;
                        float  linearDepth = 1.0/(far-near);
                        linearDepth *= length(v_Position.xyz - u_vecEye);
                        if ( (linearDepth - 0.0001) > shadow ) {
                            depthColor = vec4(0.5,0.5,0.5,1.0);
                        }
                    }
                }

                gl_FragColor = v_Color * vec4(vec3(diffuse), 1.0) * depthColor;
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
