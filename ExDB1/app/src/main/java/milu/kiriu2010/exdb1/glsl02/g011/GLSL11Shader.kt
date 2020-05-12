package milu.kiriu2010.exdb1.glsl02.g011

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader

// -------------------------------------------
// 視野角からレイを定義したレイマーチング:VBOなし
// OpenGL ES 2.0
// -------------------------------------------
// https://wgld.org/d/glsl/g011.html
// -------------------------------------------
class GLSL11Shader: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;

            void main() {
                gl_Position = vec4(a_Position,1.0);
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            // 経過時間(ミリ秒を1/1000)
            uniform   float     u_time;
            // 0.0-1.0に正規化
            uniform   vec2      u_mouse;
            // 描画領域の幅・高さ
            uniform   vec2      u_resolution;
            // 法線の出力をするかどうか
            //   0: ライティング
            //   1: 法線
            uniform   int       u_showNormal;

            const float PI = 3.14159265;
            const float angle = 60.0;
            const float fov = angle * 0.5 * PI / 180.0;
            vec3  cPos = vec3(0.0, 0.0, 2.0);
            // 球の半径
            const float sphereSize = 1.0;
            // 光源位置
            const vec3  lightDir = vec3(-0.577, 0.577, 0.577);

            // ---------------------------------------
            // オブジェクトまでの最短距離を取得
            // ---------------------------------------
            //       p: レイの先端座標
            //  戻り値: "レイの先端⇔球体"までの距離
            // ---------------------------------------
            float distanceFunc(vec3 p){
                return length(p) - sphereSize;
            }

            // ---------------------------------------
            // 法線を算出する
            // ---------------------------------------
            //   p: レイとオブジェクトの交点の座標位置
            // ---------------------------------------
            vec3 getNormal(vec3 p){
                float d = 0.0001;
                // -----------------------------------------
                // 正負それぞれにほんの少しだけずらした座標を
                // distanceFuncに渡すことによって、
                // その戻り値から勾配を計算している
                // これにより、各軸に対して
                // どの程度の傾きになっているかわかる
                // -----------------------------------------
                return normalize(vec3(
                    distanceFunc(p + vec3(  d, 0.0, 0.0)) - distanceFunc(p + vec3( -d, 0.0, 0.0)),
                    distanceFunc(p + vec3(0.0,   d, 0.0)) - distanceFunc(p + vec3(0.0,  -d, 0.0)),
                    distanceFunc(p + vec3(0.0, 0.0,   d)) - distanceFunc(p + vec3(0.0, 0.0,  -d))
                ));
            }

            void main() {
                // ----------------------------------------------------
                // マウス座標の正規化
                // ----------------------------------------------------
                // 0～1の範囲で入ってくるマウスの位置を
                // -1～1の範囲に正規化している
                // Y座標は上下逆のため、正負を逆転している
                // ----------------------------------------------------
                vec2 m = vec2(u_mouse.x*2.0-1.0,-u_mouse.y*2.0+1.0);
                // ----------------------------------------------------
                // フラグメント座標の正規化
                // ----------------------------------------------------
                // 今から処理しようとしているスクリーン上のピクセル位置を
                // -1～1の範囲に正規化している
                // ----------------------------------------------------
                vec2 p = (gl_FragCoord.xy * 2.0 - u_resolution)/min(u_resolution.x, u_resolution.y);

                // ray
                vec3 ray = normalize(vec3(sin(fov)*p.x,sin(fov)*p.y,-cos(fov)));

                // marching loop
                float distance = 0.0; // レイとオブジェクト間の最短距離
                float rLen = 0.0;     // レイに継ぎ足す長さ
                vec3  rPos = cPos;    // レイの先端位置
                for(int i = 0; i < 16; i++){
                    distance = distanceFunc(rPos);
                    rLen += distance;
                    rPos = cPos + ray * rLen;
                }

                // hit check
                if (abs(distance) < 0.001) {
                    vec3  normal = getNormal(rPos);
                    // 法線の出力
                    if ( bool(u_showNormal) ) {
                        gl_FragColor = vec4(normal,1.0);
                    }
                    // ライティング
                    else {
                        float diff   = clamp(dot(lightDir,normal), 0.1, 1.0);
                        gl_FragColor = vec4(vec3(diff), 1.0);
                    }
                }
                else {
                    gl_FragColor = vec4(vec3(0.0), 1.0);
                }
            }
            """.trimIndent()

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle, arrayOf("a_Position") )
        return this
    }

    fun draw(model: MgModelAbs,
             u_time: Float,
             u_mouse: FloatArray,
             u_resolution: FloatArray,
             u_showNormal: Int) {
        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // uniform(時間)
        GLES20.glGetUniformLocation(programHandle,"u_time").also {
            GLES20.glUniform1f(it,u_time)
        }
        MyGLES20Func.checkGlError2("u_time",this,model)

        // uniform(タッチ位置)
        GLES20.glGetUniformLocation(programHandle,"u_mouse").also {
            GLES20.glUniform2fv(it,1,u_mouse,0)
        }
        MyGLES20Func.checkGlError2("u_mouse",this,model)

        // uniform(解像度)
        GLES20.glGetUniformLocation(programHandle,"u_resolution").also {
            GLES20.glUniform2fv(it,1,u_resolution,0)
        }
        MyGLES20Func.checkGlError2("u_resolution",this,model)

        // uniform(法線の出力をするかどうか)
        GLES20.glGetUniformLocation(programHandle,"u_showNormal").also {
            GLES20.glUniform1i(it,u_showNormal)
        }
        MyGLES20Func.checkGlError2("u_showNormal",this,model)

        // モデル描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // 頂点配列を無効化
        GLES20.glDisableVertexAttribArray(programHandle)
    }
}
