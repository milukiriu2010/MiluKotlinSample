package milu.kiriu2010.exdb1.mgl00.octahedron01

import android.opengl.GLES20
import milu.kiriu2010.exdb1.opengl.MyGLFunc

class Octahedron01Shader {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3 a_Position;
            attribute vec3 a_Normal;
            attribute vec4 a_Color;
            uniform   mat4 u_matMVP;
            // モデル座標変換行列
            uniform   mat4 u_matM;
            varying   vec3 v_Position;
            varying   vec3 v_Normal;
            varying   vec4 v_Color;

            void main() {
                v_Position     = (u_matM * vec4(a_Position,1.0)).xyz;
                v_Normal       = a_Normal;
                v_Color        = a_Color;
                gl_Position    = u_matMVP * vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump float;
            // 逆行列
            uniform   mat4 u_matINV;
            // 平行光源
            uniform   vec3 u_vecLight;
            // 視線ベクトル
            uniform   vec3 u_vecEye;
            // 環境光の色
            uniform   vec4 u_vecAmbientColor;
            varying   vec3 v_Position;
            varying   vec3 v_Normal;
            varying   vec4 v_Color;

            void main() {
                // 点光源から頂点へ向かうベクトル
                vec3  vecLight = u_vecLight - v_Position;
                // 光の逆ベクトル
                vec3  invLight = normalize(u_matINV * vec4(vecLight,0.0)).xyz;
                // 視線の逆ベクトル
                vec3  invEye   = normalize(u_matINV * vec4(u_vecEye,0.0)).xyz;
                // 光ベクトルと視線ベクトルからハーフベクトルを算出
                vec3  halfLE   = normalize(invLight + invEye);
                // 拡散度("頂点の法線ベクトル"と"光の逆ベクトル"の内積をとり、0.0～1.0の値を返す
                float diffuse  = clamp(dot(v_Normal,invLight), 0.0, 1.0) + 0.2;
                // 面の法線ベクトルとハーフベクトルの内積をとることで反射光の強さを決定する
                float specular = pow(clamp(dot(v_Normal, halfLE),0.0,1.0), 50.0);
                // 頂点の色に拡散光と反射光を足す
                vec4  vecColor = v_Color * vec4(vec3(diffuse),1.0) + vec4(vec3(specular),1.0) + u_vecAmbientColor;
                gl_FragColor   = vecColor;
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
