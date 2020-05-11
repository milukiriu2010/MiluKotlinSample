package milu.kiriu2010.exdb1.glsl01.g007v

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// ----------------------------------------------
// フラグメントシェーダ ノイズ:VBOあり
// OpenGL ES 2.0
// ----------------------------------------------
// https://wgld.org/d/glsl/g007.html
// ----------------------------------------------
class GLSLV07Shader: ES20MgShader() {
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

        // uniform(ノイズの種類)
        hUNI[3] = GLES20.glGetUniformLocation(programHandle,"u_noiseType")
        MyGLES20Func.checkGlError("u_noiseType:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_time: Float,
             u_mouse: FloatArray,
             u_resolution: FloatArray,
             u_noiseType: Int) {
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

        // uniform(ノイズの種類)
        GLES20.glUniform1i(hUNI[3],u_noiseType)
        MyGLES20Func.checkGlError2("u_noiseType",this,model)

        // モデル描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}
