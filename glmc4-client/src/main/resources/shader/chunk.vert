#version 330 core

layout (location=0) in vec2 inPos;
layout (location=1) in vec3 inCol;

out vec3 col;

void main() {
    gl_Position=vec4(inPos, 0.0, 1.0);
    col = inCol;
}