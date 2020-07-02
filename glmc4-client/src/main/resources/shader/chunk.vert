#version 410 core

layout (location=0) in vec3 inPos;
layout (location=1) in vec2 inUV1;
layout (location=2) in vec3 inTint1;
layout (location=3) in vec2 inUV2;
layout (location=4) in vec3 inTint2;

uniform mat4 perspective, view, model;

out vec2 uv1;
out vec3 tint1;
out vec2 uv2;
out vec3 tint2;

void main(){
    gl_Position = perspective * view * model * vec4(inPos, 1);
    uv1 = inUV1;
    tint1 = inTint1;
    uv2 = inUV2;
    tint2 = inTint2;
}