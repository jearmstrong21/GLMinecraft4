#version 330 core

in vec3 col;

out vec4 fc;

uniform sampler2D tex;

void main() {
//    fc=vec4(col.xy, 0.0, 1.0);
    fc = texture(tex, col.xy);
}