#version 330 core

layout (location=0) in vec3 inPos;
layout (location=1) in vec2 inUV;

uniform mat4 perspective;
uniform mat4 view;

uniform float x;
uniform float z;

out vec2 uv;

void main() {
    gl_Position=perspective * view * vec4(inPos+vec3(x, 0, z), 1.0);
    uv = inUV;
}