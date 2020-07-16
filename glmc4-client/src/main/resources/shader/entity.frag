#version 330 core

uniform sampler2D tex;

in vec2 uv;

out vec4 fc;

void main(){
    fc = texture(tex, uv);
}