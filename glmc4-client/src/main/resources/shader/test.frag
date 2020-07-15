#version 330 core

uniform vec3 color;
uniform sampler2D tex;

in vec2 uv;

out vec4 fc;

void main(){
    //        fc=vec4(color, 1);
    //        fc=vec4(uv, 0, 1);
    fc = vec4(color, 1)*texture(tex, uv);
}