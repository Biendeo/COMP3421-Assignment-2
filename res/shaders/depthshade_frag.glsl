#version 120

uniform sampler2D fbo_texture;
uniform sampler2D depth_texture;
varying vec2 f_texcoord;

void main() {
    float closeDistance = 0.0f;
    float farDistance = 5.0f;
    float scale = 1 - min(max(closeDistance, gl_FragCoord.z / gl_FragCoord.w), farDistance);
    vec4 diffusePixel = texture2D(fbo_texture, f_texcoord);
    float depthPixel = texture2D(depth_texture, f_texcoord).r;
    gl_FragColor = vec4(1 - depthPixel, 1 - depthPixel,  1 - depthPixel, 1.0f) * 2.0 + diffusePixel * 0.5;
}
