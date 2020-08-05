#version 410 core

in vec2 uv;

out vec4 fc;

uniform sampler2D tex;

uniform bool underwater;

void main(){
    vec3 c=texture(tex, uv).xyz;
    if (underwater)c*=vec3(0.8, 0.8, 1);
    fc=vec4(c, 1);
}