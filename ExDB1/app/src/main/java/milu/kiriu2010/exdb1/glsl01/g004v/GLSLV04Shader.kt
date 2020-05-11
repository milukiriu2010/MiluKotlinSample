package milu.kiriu2010.exdb1.glsl01.g004v

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// ----------------------------------------------
// 様々な図形を描く:VBOあり
// OpenGL ES 2.0
// ----------------------------------------------
// https://wgld.org/d/glsl/g004.html
// ----------------------------------------------
class GLSLV04Shader: ES20MgShader() {
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
            // 描画する図形の種類
            uniform   int       u_type;

            void main() {
                // 0～1の範囲で入ってくるマウスの位置を
                // -1～1の範囲に正規化している
                // Y座標は上下逆のため、正負を逆転している
                vec2 m = vec2(u_mouse.x*2.0-1.0,-u_mouse.y*2.0+1.0);
                // 今から処理しようとしているスクリーン上のピクセル位置を
                // -1～1の範囲に正規化している
                vec2 p = (gl_FragCoord.xy * 2.0 - u_resolution)/min(u_resolution.x, u_resolution.y);
                float t = 0.0;

                // 輪
                if ( u_type == 1 ) {
                    t = 0.02/abs(0.5-length(p));
                }
                // 輪(時間経過によって大きさが変化する)
                else if ( u_type == 2 ) {
                    t = 0.02/abs(abs(sin(u_time))-length(p));
                }
                // グラデーション
                else if ( u_type == 3 ) {
                    vec2 v = vec2(0.0,1.0);
                    // ピクセル位置とY方向にプラスとなるベクトルとの内積
                    // X軸より上が白っぽく、上に上がるにつれ真っ白になる。
                    // X軸より下は負になるので黒になる
                    t = dot(p,v);
                }
                // 上から円錐を見たような描画
                else if ( u_type == 4 ) {
                    vec2 v = vec2(0.0,1.0);
                    t = dot(p,v)/(length(p)*length(v));
                }
                // 集中線のような放射状のライン
                else if ( u_type == 5 ) {
                    t = atan(p.y,p.x) + u_time;
                    t = sin(t*10.0);
                }
                // 花
                else if ( u_type == 6 ) {
                    float u = sin((atan(p.y,p.x)+u_time*0.5)*6.0);
                    t = 0.01/abs(u-length(p));
                }
                // 波打つリング
                else if ( u_type == 7 ) {
                    float u = sin((atan(p.y,p.x)+u_time*0.5)*20.0)*0.01;
                    t = 0.01/abs(0.5+u-length(p));
                }
                // 花その２
                else if ( u_type == 8 ) {
                    float u = sin((atan(p.y,p.x)+u_time*0.5)*20.0)*0.5;
                    t = 0.01/abs(0.25+u-length(p));
                }
                // 花模様をファンのように変形
                else if ( u_type == 9 ) {
                    float u = abs( sin((atan(p.y,p.x)-length(p)+u_time)*10.0)*0.5 )+0.2;
                    t = 0.01/abs(u-length(p));
                }
                else {
                    t = 0.0;
                }

                gl_FragColor = vec4(vec3(t), 1.0);
            }
            """.trimIndent()

    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle)

        // ----------------------------------------------
        // attributeハンドルに値をセット
        // ----------------------------------------------
        hATTR = IntArray(1)
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

        // ----------------------------------------------
        // uniformハンドルに値をセット
        // ----------------------------------------------
        hUNI = IntArray(4)

        // uniform(時間)
        hUNI[0] = GLES20.glGetUniformLocation(programHandle,"u_time")
        MyGLES20Func.checkGlError("u_time:glGetUniformLocation")

        // uniform(タッチ位置)
        hUNI[1] = GLES20.glGetUniformLocation(programHandle,"u_mouse")
        MyGLES20Func.checkGlError("u_mouse:glGetUniformLocation")

        // uniform(解像度)
        hUNI[2] = GLES20.glGetUniformLocation(programHandle,"u_resolution")
        MyGLES20Func.checkGlError("u_resolution:glGetUniformLocation")

        // uniform(描画する図形の種類)
        hUNI[3] = GLES20.glGetUniformLocation(programHandle,"u_type")
        MyGLES20Func.checkGlError("u_type:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_time: Float,
             u_mouse: FloatArray,
             u_resolution: FloatArray,
             u_type: Int) {
        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[0])
        GLES20.glVertexAttribPointer(hATTR[0],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // uniform(時間)
        GLES20.glUniform1f(hUNI[0],u_time)
        MyGLES20Func.checkGlError2("u_time",this,model)

        // uniform(タッチ位置)
        GLES20.glUniform2fv(hUNI[1],1,u_mouse,0)
        MyGLES20Func.checkGlError2("u_mouse",this,model)

        // uniform(解像度)
        GLES20.glUniform2fv(hUNI[2],1,u_resolution,0)
        MyGLES20Func.checkGlError2("u_resolution",this,model)

        // uniform(描画する図形の種類)
        GLES20.glUniform1i(hUNI[3],u_type)
        MyGLES20Func.checkGlError2("u_type",this,model)

        // モデル描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
