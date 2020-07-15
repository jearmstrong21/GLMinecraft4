#version 330 core

layout (location=0) in vec3 inPos;

uniform mat4 perspective;
uniform mat4 view;
uniform mat4 model;

void main(){
    gl_Position=perspective*view*vec4(inPos, 1);
}