#version 120

uniform sampler2D fbo_texture;
uniform sampler2D depth_texture;
varying vec2 f_texcoord;
uniform int sampleCount;
uniform float spread;
uniform float nearValue;
uniform float farValue;
uniform int direction;

void main() {
    vec4 diffusePixel = texture2D(fbo_texture, f_texcoord);
    float depthPixel = texture2D(depth_texture, f_texcoord).r;
    if (depthPixel < nearValue || depthPixel > farValue) {
        vec4 color = vec4(0.0);
        vec2 dir = vec2(0.0);
        if (direction == 0) {
            dir = vec2(1.0, 0.0);
        } else {
            dir = vec2(0.0, 1.0);
        }
        float adjustedSpread = 0.0f;
        if (depthPixel < nearValue) {
            adjustedSpread = min(spread, (nearValue - depthPixel) * 20 * spread);
        } else {
            adjustedSpread = min(spread, (depthPixel - farValue) * 50 * spread);
        }
        vec2 off1 = dir * 1.411764705882353  * adjustedSpread;
        vec2 off2 = dir * 3.2941176470588234 * adjustedSpread;
        vec2 off3 = dir * 5.176470588235294 * adjustedSpread;
        color += texture2D(fbo_texture, f_texcoord) * 0.1964825501511404;
        color += texture2D(fbo_texture, f_texcoord + off1) * 0.2969069646728344;
        color += texture2D(fbo_texture, f_texcoord - off1) * 0.2969069646728344;
        color += texture2D(fbo_texture, f_texcoord + off2) * 0.09447039785044732;
        color += texture2D(fbo_texture, f_texcoord - off2) * 0.09447039785044732;
        color += texture2D(fbo_texture, f_texcoord + off3) * 0.010381362401148057;
        color += texture2D(fbo_texture, f_texcoord - off3) * 0.010381362401148057;
        gl_FragColor = color;
    } else {
        gl_FragColor = diffusePixel;
    }
}
