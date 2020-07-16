#version 330 core

layout (location=0) in vec3 inPos;
layout (location=1) in vec2 inUV;

uniform mat4 perspective;
uniform mat4 view;
uniform mat4 model;

out vec2 uv;

void main(){
    gl_Position=perspective*view*model*vec4(inPos, 1);
    uv=inUV;
}