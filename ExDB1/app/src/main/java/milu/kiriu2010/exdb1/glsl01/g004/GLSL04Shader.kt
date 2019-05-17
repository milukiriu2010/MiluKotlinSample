package milu.kiriu2010.exdb1.glsl01.g004

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.MgShader

// ---------------------------------------
// 様々な図形を描く
// ---------------------------------------
class GLSL04Shader: MgShader() {
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

    override fun loadShader(): MgShader {
        // 頂点シェーダを生成
        val svhandle = MyGLFunc.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        val sfhandle = MyGLFunc.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLFunc.createProgram(svhandle,sfhandle, arrayOf("a_Position") )
        return this
    }

    fun draw(model: MgModelAbs,
             u_time: Float,
             u_mouse: FloatArray,
             u_resolution: FloatArray,
             u_type: Int) {
        GLES20.glUseProgram(programHandle)
        MyGLFunc.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        model.bufPos.position(0)
        GLES20.glGetAttribLocation(programHandle,"a_Position").also {
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false, 3*4, model.bufPos)
            GLES20.glEnableVertexAttribArray(it)
        }
        MyGLFunc.checkGlError2("a_Position",this,model)

        // uniform(時間)
        GLES20.glGetUniformLocation(programHandle,"u_time").also {
            GLES20.glUniform1f(it,u_time)
        }
        MyGLFunc.checkGlError2("u_time",this,model)

        // uniform(タッチ位置)
        GLES20.glGetUniformLocation(programHandle,"u_mouse").also {
            GLES20.glUniform2fv(it,1,u_mouse,0)
        }
        MyGLFunc.checkGlError2("u_mouse",this,model)

        // uniform(解像度)
        GLES20.glGetUniformLocation(programHandle,"u_resolution").also {
            GLES20.glUniform2fv(it,1,u_resolution,0)
        }
        MyGLFunc.checkGlError2("u_resolution",this,model)

        // uniform(描画する図形の種類)
        GLES20.glGetUniformLocation(programHandle,"u_type").also {
            GLES20.glUniform1i(it,u_type)
        }
        MyGLFunc.checkGlError2("u_type",this,model)

        // モデル描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // 頂点配列を無効化
        GLES20.glDisableVertexAttribArray(programHandle)
    }
}
