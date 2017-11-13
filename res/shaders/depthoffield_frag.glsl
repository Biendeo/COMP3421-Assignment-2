#version 120

uniform sampler2D fbo_texture;
uniform sampler2D depth_texture;
varying vec2 f_texcoord;
uniform int sampleCount;
uniform float spread;
uniform float nearValue;
uniform float farValue;

void main() {
    vec4 diffusePixel = texture2D(fbo_texture, f_texcoord);
    float depthPixel = texture2D(depth_texture, f_texcoord).r;
    if (depthPixel < nearValue || depthPixel > farValue) {
        float adjustedSpread = 0.0f;
        if (depthPixel < nearValue) {
            adjustedSpread = min(spread, (nearValue - depthPixel) * 20 * spread);
        } else {
            adjustedSpread = min(spread, (depthPixel - farValue) * 20 * spread);
        }
        vec4 outputColor = vec4(0.0, 0.0, 0.0, 1.0);
        for (int i = 0; i < sampleCount; ++i) {
            for (int j = 0; j < sampleCount; ++j) {
                vec2 offset = f_texcoord;
                offset.x += -adjustedSpread + (adjustedSpread * 2 * i / (sampleCount - 1));
                offset.y += -adjustedSpread + (adjustedSpread * 2 * j / (sampleCount - 1));
                offset.x = clamp(offset.x, 0.0, 1.0);
                offset.y = clamp(offset.y, 0.0, 1.0);
                outputColor += texture2D(fbo_texture, offset);
            }
        }
        gl_FragColor = outputColor / (sampleCount * sampleCount);
    } else {
        gl_FragColor = diffusePixel;
    }
}
