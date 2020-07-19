#version 330 core

out vec4 fc;

in vec2 uv;

uniform sampler2D tex;

void main(){
    //    fc=vec4(1, 0, 1, 1);
    bool b=texture(tex, uv).x>0.5;
    if (b)fc=vec4(0, 0, 0, 1);
    else discard;
    //    fc=vec4(uv, 0, 1);
}