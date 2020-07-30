#version 330 core

uniform sampler2D tex;

in vec2 uv;
in vec3 color;

out vec4 fc;

void main(){
    fc = vec4(color, 1)*texture(tex, uv);
}