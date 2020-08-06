#version 330 core

layout (location=0) in vec3 inPos;
layout (location=1) in vec2 inUV;
layout (location=2) in vec3 inColor;
layout (location=3) in float inAO;
layout (location=4) in float inFakeLight;
layout (location=5) in float inSunlight;

uniform mat4 perspective;
uniform mat4 view;

uniform float x;
uniform float z;

out vec2 uv;
out vec3 color;
out float ao;
out float fakeLight;
out float sunlight;

void main() {
    gl_Position=perspective * view * vec4(inPos+vec3(x, 0, z), 1.0);
    uv = inUV;
    color = inColor;
    ao = inAO;
    fakeLight = inFakeLight;
    sunlight = inSunlight;
}