#version 410 core

in vec2 uv;

uniform sampler2D tex;

out vec4 fc;

void main(){
    float c=texture(tex, uv).x;
    if (c==1.0)fc=vec4(vec3(1), 1);
    else discard;
}