#version 130

uniform sampler2D fbo_texture;
uniform sampler2D depth_texture;
varying vec2 f_texcoord;
uniform float nearValue;
uniform float farValue;

void main() {
    vec4 diffusePixel = texture2D(fbo_texture, f_texcoord);
    float depthPixel = texture2D(depth_texture, f_texcoord).r;
    if (depthPixel < nearValue || depthPixel > farValue) {
        float adjustedSpread = 3.0f;
        if (depthPixel < nearValue) {
            adjustedSpread = min(3.0, (nearValue - depthPixel) * 100);
        } else {
            adjustedSpread = min(3.0, (depthPixel - farValue) * 200);
        }
        gl_FragColor = textureLod(fbo_texture, f_texcoord, adjustedSpread);
    } else {
        gl_FragColor = diffusePixel;
    }
}
