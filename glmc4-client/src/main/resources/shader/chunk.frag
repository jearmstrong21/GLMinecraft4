#version 330 core

in vec2 uv;

out vec4 fc;

uniform sampler2D tex;

void main() {
//    fc=vec4(uv, 0.0, 1.0);
    fc = texture(tex, uv);
}