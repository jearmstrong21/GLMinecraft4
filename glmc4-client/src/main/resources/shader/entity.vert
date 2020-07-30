#version 330 core

layout (location=0) in vec3 inPos;
layout (location=1) in vec2 inUV;
layout (location=2) in vec3 inColor;

uniform mat4 perspective;
uniform mat4 view;
uniform mat4 model;

out vec2 uv;
out vec3 color;

void main(){
    gl_Position=perspective*view*model*vec4(inPos, 1);
    uv=inUV;
    color=inColor;
}