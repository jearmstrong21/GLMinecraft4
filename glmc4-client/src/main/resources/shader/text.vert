#version 410 core

layout (location=0) in vec2 inPos;

uniform vec2 posOffset;
uniform vec2 posScale;

uniform vec2 uvOffset;
uniform vec2 uvScale;

out vec2 uv;

void main(){
    gl_Position=vec4(inPos*posScale+posOffset, 0, 1);
    uv=inPos*uvScale+uvOffset;
}