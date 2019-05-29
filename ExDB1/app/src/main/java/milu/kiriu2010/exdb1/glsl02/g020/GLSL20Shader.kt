package milu.kiriu2010.exdb1.glsl02.g020

import android.opengl.GLES20
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.shader.es20.ES20MgShader

// -------------------------------------------
// レイマーチングソフトシャドウ
// -------------------------------------------
// https://wgld.org/d/glsl/g020.html
// -------------------------------------------
class GLSL20Shader: ES20MgShader() {
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

            const vec3 cPos = vec3(0.0, 5.0, 5.0);
            const vec3 cDir = vec3(0.0, -0.707, -0.707);
            const vec3 cUp  = vec3(0.0,  0.707, -0.707);

            // 光源位置
            const vec3  lightDir = vec3(0.0, 1.0, 0.0);

            // torus distance function
            float distFuncTorus(vec3 p){
                p.xz -= u_mouse * 2.0 - 1.0;
                vec2 t = vec2(3.0, 1.0);
                vec2 r = vec2(length(p.xz) - t.x, p.y);
                return length(r) - t.y;
            }

            // floor distance function
            float distFuncFloor(vec3 p){
                return dot(p, vec3(0.0, 1.0, 0.0)) + 1.0;
            }

            // distance function
            float distFunc(vec3 p){
                float d1 = distFuncTorus(p);
                float d2 = distFuncFloor(p);
                return min(d1, d2);
            }

            // ---------------------------------------
            // 法線を算出する
            // ---------------------------------------
            //   p: レイとオブジェクトの交点の座標位置
            // ---------------------------------------
            vec3 genNormal(vec3 p){
                float d = 0.0001;
                // -----------------------------------------
                // 正負それぞれにほんの少しだけずらした座標を
                // distanceFuncに渡すことによって、
                // その戻り値から勾配を計算している
                // これにより、各軸に対して
                // どの程度の傾きになっているかわかる
                // -----------------------------------------
                return normalize(vec3(
                    distFunc(p + vec3(  d, 0.0, 0.0)) - distFunc(p + vec3( -d, 0.0, 0.0)),
                    distFunc(p + vec3(0.0,   d, 0.0)) - distFunc(p + vec3(0.0,  -d, 0.0)),
                    distFunc(p + vec3(0.0, 0.0,   d)) - distFunc(p + vec3(0.0, 0.0,  -d))
                ));
            }

            // generate shadow
            float genShadow(vec3 ro, vec3 rd){
                float h = 0.0;
                float c = 0.001;
                float r = 1.0;
                float shadowCoef = 0.5;
                for(float t = 0.0; t < 50.0; t++){
                    h = distFunc(ro + rd * c);
                    if(h < 0.001){
                        return shadowCoef;
                    }
                    r = min(r, h * 16.0 / c);
                    c += h;
                }
                return 1.0 - shadowCoef + r * shadowCoef;
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

                // camera and ray
                vec3 cSide = cross(cDir, cUp);
                float targetDepth = 1.0;
                vec3 ray = normalize(cSide * p.x + cUp * p.y + cDir * targetDepth);

                // marching loop
                float tmp, dist;
                tmp = 0.0;
                vec3 dPos = cPos;
                for(int i = 0; i < 256; i++){
                    dist = distFunc(dPos);
                    if(dist < 0.001){break;}
                    tmp += dist;
                    dPos = cPos + tmp * ray;
                }

                // light offset
	            vec3 light = normalize(lightDir + vec3(sin(u_time), 0.0, 0.0));

                // hit check
                vec3  color;
                float shadow = 1.0;
                if (abs(dist) < 0.001) {
                    vec3  normal = genNormal(dPos);
                    // 法線の出力
                    if ( bool(u_showNormal) ) {
                        color = normal;
                    }
                    // ライティング
                    else {
                        vec3  halfLE = normalize(light-ray);
                        float diff   = clamp(dot(light,normal), 0.1, 1.0);
                        float spec   = pow(clamp(dot(halfLE, normal), 0.0, 1.0), 50.0);

                        // generate shadow
                        shadow = genShadow(dPos + normal * 0.001, light);

                   		// generate tile pattern
                        float u = 1.0 - floor(mod(dPos.x, 2.0));
                        float v = 1.0 - floor(mod(dPos.z, 2.0));
                        if((u == 1.0 && v < 1.0) || (u < 1.0 && v == 1.0)){
                            diff *= 0.7;
                        }

                        color = vec3(diff)+vec3(spec);
                    }
                }
                else {
                    color = vec3(0.0);
                }
                gl_FragColor = vec4(color*max(0.5,shadow), 1.0);
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
