package milu.kiriu2010.exdb1.glsl01.g007

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLFunc
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.MgShader

// ---------------------------------------
// フラグメントシェーダ ノイズ
// ---------------------------------------
// https://wgld.org/d/glsl/g007.html
// ---------------------------------------
class GLSL07Shader: MgShader() {
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
            // ノイズタイプ
            uniform   int       u_noiseType;

            const int   oct  = 8;
            const float per  = 0.5;
            const float PI   = 3.1415926;
            const float cCorners = 1.0 / 16.0;
            const float cSides   = 1.0 / 8.0;
            const float cCenter  = 1.0 / 4.0;

            // 補間関数
            float interpolate(float a, float b, float x){
	            float f = (1.0 - cos(x * PI)) * 0.5;
	            return a * (1.0 - f) + b * f;
            }

            // 乱数生成
            float rnd(vec2 p){
                return fract(sin(dot(p ,vec2(12.9898,78.233))) * 43758.5453);
            }

            // 補間乱数
            float irnd(vec2 p){
                vec2 i = floor(p);
                vec2 f = fract(p);
                vec4 v = vec4(rnd(vec2(i.x,       i.y      )),
                              rnd(vec2(i.x + 1.0, i.y      )),
                              rnd(vec2(i.x,       i.y + 1.0)),
                              rnd(vec2(i.x + 1.0, i.y + 1.0)));
                // interpolateで乱数を補間している
                return interpolate(interpolate(v.x, v.y, f.x), interpolate(v.z, v.w, f.x), f.y);
            }

            // ノイズ生成
            float noise(vec2 p){
                float t = 0.0;
                for(int i = 0; i < oct; i++){
                    float freq = pow(2.0, float(i));
                    float amp  = pow(per, float(oct - i));
                    // 乱数を合成している
                    t += irnd(vec2(p.x / freq, p.y / freq)) * amp;
                }
                return t;
            }

            // シームレスノイズ生成
            // 一辺の長さをどのくらいに設定してシームレスなタイル状にするか引数で指定できる
            float snoise(vec2 p, vec2 q, vec2 r){
                return noise(vec2(p.x,       p.y      )) *        q.x  *        q.y  +
                       noise(vec2(p.x,       p.y + r.y)) *        q.x  * (1.0 - q.y) +
                       noise(vec2(p.x + r.x, p.y      )) * (1.0 - q.x) *        q.y  +
                       noise(vec2(p.x + r.x, p.y + r.y)) * (1.0 - q.x) * (1.0 - q.y);
            }

            void main() {
                float n = 0.0;
                // ノイズ
                if ( u_noiseType == 0 ) {
                    vec2 t = gl_FragCoord.xy + vec2(u_time * 10.0);
                    n = noise(t);
                }
                // シームレスなノイズ
                else {
                    const float map = 256.0;
                    vec2 t = mod(gl_FragCoord.xy + vec2(u_time * 10.0), map);
                    n = snoise(t, t / map, vec2(map));
                }

                gl_FragColor = vec4(vec3(n), 1.0);
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
             u_noiseType: Int) {
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

        // uniform(ノイズの種類)
        GLES20.glGetUniformLocation(programHandle,"u_noiseType").also {
            GLES20.glUniform1i(it,u_noiseType)
        }
        MyGLFunc.checkGlError2("u_noiseType",this,model)

        // モデル描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // 頂点配列を無効化
        GLES20.glDisableVertexAttribArray(programHandle)
    }
}