#version 330 core

layout (location=0) in vec2 inPos;

out vec2 uv;

uniform float x;
uniform float y;
uniform float w;
uniform float h;

uniform float uvx;
uniform float uvy;
uniform float uvw;
uniform float uvh;

void main() {
    gl_Position = vec4(vec2(x, y)+vec2(w, h)*inPos, 0, 1);
    uv = vec2(uvx, uvy)+vec2(uvw, uvh)*vec2(0, 1)+vec2(1, -1)*vec2(uvw, uvh)*inPos;
}