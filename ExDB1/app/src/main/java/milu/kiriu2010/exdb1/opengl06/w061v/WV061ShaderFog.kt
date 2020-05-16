package milu.kiriu2010.exdb1.opengl06.w061v

import android.opengl.GLES20
import milu.kiriu2010.gui.model.MgModelAbs
import milu.kiriu2010.gui.basic.MyGLES20Func
import milu.kiriu2010.gui.shader.es20.ES20MgShader
import milu.kiriu2010.gui.vbo.es20.ES20VBOAbs

// ------------------------------------------------------------
// シェーダ(フォグ):VBOあり
// OpenGL ES 2.0
// ------------------------------------------------------------
// https://wgld.org/d/webgl/w061.html
// ------------------------------------------------------------
class WV061ShaderFog: ES20MgShader() {
    // 頂点シェーダ
    private val scv =
            """
            attribute vec3  a_Position;
            attribute vec4  a_Color;
            attribute vec2  a_TextureCoord;
            uniform   mat4  u_matM;
            uniform   mat4  u_matMVP;
            uniform   mat4  u_matTex;
            varying   vec4  v_Position;
            varying   vec4  v_Color;
            varying   vec2  v_TextureCoord;
            varying   vec4  v_TexProjCoord;

            void main() {
                vec3   pos      = (u_matM * vec4(a_Position, 1.0)).xyz;
                v_Position      = u_matMVP * vec4(a_Position, 1.0);
                v_Color         = a_Color;
                // テクスチャ座標は、
                // フラグメントシェーダ側で半月形に見えるように
                // アルファ値を加工するために使う
                v_TextureCoord  = a_TextureCoord;
                v_TexProjCoord  = u_matTex * vec4(pos, 1.0);
                gl_Position     = v_Position;
            }
            """.trimIndent()

    // フラグメントシェーダ
    private val scf =
            """
            precision mediump   float;

            // ----------------------------------------------------------------
            // テクスチャ座標をずらすために使う
            // 全てのパーティクルが同じノイズを表示しているのは、かっこよくないので、
            // フラグメントシェーダ側でテクスチャ座標をずらすために使う
            // ----------------------------------------------------------------
            uniform   vec2      u_offset;
            // ----------------------------------------------------------------
            // 他のモデルの深度とパーティクルの深度の差が、
            // どのくらい近い場合にアルファ値を操作するのかを決めるための係数
            //   0.0 - 1.0
            // パーティクル以外のモデルの深度、
            // すなわちオフスクリーンレンダリングされたフレームバッファから
            // 読みだした深度が仮に0.5で、u_distLengthが仮に0.1だったとすると、
            // パーティクルの深度が0.4-0.6の範囲に収まるとき、
            // アルファ値に影響を及ぼす
            // ----------------------------------------------------------------
            uniform   float     u_distLength;
            uniform   sampler2D u_TextureDepth;
            uniform   sampler2D u_TextureNoise;
            uniform   int       u_softParticle;
            varying   vec4      v_Position;
            varying   vec4      v_Color;
            varying   vec2      v_TextureCoord;
            varying   vec4      v_TexProjCoord;

            // フレームバッファに描きこまれた深度値を本来の値に変換する
            float restDepth(vec4 RGBA) {
                const float rMask = 1.0;
                const float gMask = 1.0/255.0;
                const float bMask = 1.0/(255.0*255.0);
                const float aMask = 1.0/(255.0*255.0*255.0);
                float depth = dot(RGBA, vec4(rMask, gMask, bMask, aMask));
                return depth;
            }

            const float near = 0.1;
            const float far  = 10.0;
            const float linearDepth = 1.0/(far-near);

            void main() {
                // 射影テクスチャマッピングを使ってオフスクリーンレンダリングした深度値
                // すなわち、パーティクル以外のモデルの深度値を読み出す
                float depth      = restDepth(texture2DProj(u_TextureDepth, v_TexProjCoord));
                // パーティクル自身の深度値
                float linearPos  = linearDepth * length(v_Position);
                // オフセットを適用したノイズテクスチャの色情報を取得
                vec4  noiseColor = texture2D(u_TextureNoise, v_TextureCoord + u_offset);
                // パーティクルを半月形になるように透明度を調整している
                // (0.5,1.0)というテクスチャ座標位置とパーティクルのテクスチャ座標との距離を
                // クランプ下上で1.0から減算する。
                // こうすると、(0.5,1.0)を中心として、
                // 円形に透明度が徐々に高くなっていくようなアルファ値を適用できる
                // (0.5,1.0)をずらすと、半月形だけでなく円形に透明度を適用することもできる。
                float alpha      = 1.0 - clamp(length(vec2(0.5,1.0) - v_TextureCoord)*2.0, 0.0, 1.0);
                if (bool(u_softParticle)) {
                    float distance = abs(depth-linearPos);
                    if (u_distLength >= distance) {
                        float d = distance/u_distLength;
                        alpha *= d;
                    }
                }
                gl_FragColor = vec4(v_Color.rgb, noiseColor.r * alpha);
            }
            """.trimIndent()


    override fun loadShader(): ES20MgShader {
        // 頂点シェーダを生成
        svhandle = MyGLES20Func.loadShader(GLES20.GL_VERTEX_SHADER, scv)
        // フラグメントシェーダを生成
        sfhandle = MyGLES20Func.loadShader(GLES20.GL_FRAGMENT_SHADER, scf)

        // プログラムオブジェクトの生成とリンク
        programHandle = MyGLES20Func.createProgram(svhandle,sfhandle)

        // ----------------------------------------------
        // attributeハンドルに値をセット
        // ----------------------------------------------
        hATTR = IntArray(3)
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

        // 属性(法線)
        hATTR[1] = GLES20.glGetAttribLocation(programHandle, "a_Normal").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_Normal:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,3,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_Normal:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_Normal:glGetAttribLocation")

        // 属性(テクスチャ座標)
        hATTR[2] = GLES20.glGetAttribLocation(programHandle, "a_TextureCoord").also {
            // attribute属性を有効にする
            // ここで呼ばないと描画されない
            GLES20.glEnableVertexAttribArray(it)
            MyGLES20Func.checkGlError("a_TextureCoord:glEnableVertexAttribArray")
            // attribute属性を登録
            GLES20.glVertexAttribPointer(it,2,GLES20.GL_FLOAT,false,0,0)
            MyGLES20Func.checkGlError("a_TextureCoord:glVertexAttribPointer")
        }
        MyGLES20Func.checkGlError("a_TextureCoord:glGetAttribLocation")

        // ----------------------------------------------
        // uniformハンドルに値をセット
        // ----------------------------------------------
        hUNI = IntArray(8)

        // uniform(モデル)
        hUNI[0] = GLES20.glGetUniformLocation(programHandle,"u_matM")
        MyGLES20Func.checkGlError("u_matM:glGetUniformLocation")

        // uniform(モデル×ビュー×プロジェクション)
        hUNI[1] = GLES20.glGetUniformLocation(programHandle,"u_matMVP")
        MyGLES20Func.checkGlError("u_matMVP:glGetUniformLocation")

        // uniform()
        hUNI[2] = GLES20.glGetUniformLocation(programHandle,"u_matTex")
        MyGLES20Func.checkGlError("u_matTex:glGetUniformLocation")

        // uniform()
        hUNI[3] = GLES20.glGetUniformLocation(programHandle,"u_offset")
        MyGLES20Func.checkGlError("u_offset:glGetUniformLocation")

        // uniform()
        hUNI[4] = GLES20.glGetUniformLocation(programHandle,"u_distLength")
        MyGLES20Func.checkGlError("u_distLength:glGetUniformLocation")

        // uniform(テクスチャユニット)
        hUNI[5] = GLES20.glGetUniformLocation(programHandle, "u_TextureDepth")
        MyGLES20Func.checkGlError("u_TextureDepth:glGetUniformLocation")

        // uniform(テクスチャユニット)
        hUNI[6] = GLES20.glGetUniformLocation(programHandle, "u_TextureNoise")
        MyGLES20Func.checkGlError("u_TextureNoise:glGetUniformLocation")

        // uniform()
        hUNI[7] = GLES20.glGetUniformLocation(programHandle, "u_softParticle")
        MyGLES20Func.checkGlError("u_softParticle:glGetUniformLocation")

        return this
    }

    fun draw(model: MgModelAbs,
             bo: ES20VBOAbs,
             u_matMVP: FloatArray,
             u_matTex: FloatArray,
             u_offset: FloatArray,
             u_distLength: Float,
             u_TextureDepth: Int,
             u_TextureNoise: Int,
             u_softParticle: Int) {

        GLES20.glUseProgram(programHandle)
        MyGLES20Func.checkGlError2("UseProgram",this,model)

        // attribute(頂点)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[0])
        GLES20.glVertexAttribPointer(hATTR[0],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Position",this,model)

        // attribute(法線)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[1])
        GLES20.glVertexAttribPointer(hATTR[1],3,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_Normal",this,model)

        // attribute(テクスチャ座標)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bo.hVBO[2])
        GLES20.glVertexAttribPointer(hATTR[2],2,GLES20.GL_FLOAT,false,0,0)
        MyGLES20Func.checkGlError2("a_TextureCoord",this,model)

        // uniform(モデル)
        GLES20.glUniformMatrix4fv(hUNI[0],1,false,u_matMVP,0)
        MyGLES20Func.checkGlError2("u_matM",this,model)

        // uniform(モデル×ビュー×プロジェクション)
        GLES20.glUniformMatrix4fv(hUNI[1],1,false,u_matMVP,0)
        MyGLES20Func.checkGlError2("u_matMVP",this,model)

        // uniform()
        GLES20.glUniformMatrix4fv(hUNI[2],1,false,u_matTex,0)
        MyGLES20Func.checkGlError2("u_matTex",this,model)

        // uniform()
        GLES20.glUniform2fv(hUNI[3],1,u_offset,0)
        MyGLES20Func.checkGlError2("u_offset",this,model)

        // uniform()
        GLES20.glUniform1f(hUNI[4],u_distLength)
        MyGLES20Func.checkGlError2("u_distLength",this,model)

        // uniform(テクスチャユニット)
        GLES20.glUniform1i(hUNI[5], u_TextureDepth)
        MyGLES20Func.checkGlError2("u_TextureDepth",this,model)

        // uniform(テクスチャユニット)
        GLES20.glUniform1i(hUNI[6], u_TextureNoise)
        MyGLES20Func.checkGlError2("u_TextureNoise",this,model)

        // uniform()
        GLES20.glUniform1i(hUNI[7], u_softParticle)
        MyGLES20Func.checkGlError2("u_softParticle",this,model)

        // モデルを描画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.datIdx.size, GLES20.GL_UNSIGNED_SHORT, model.bufIdx)

        // リソース解放
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0)
    }
}