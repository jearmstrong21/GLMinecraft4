#version 330 core

in vec2 uv;
in vec3 color;
in float ao;
in float fakeLight;

out vec4 fc;

uniform sampler2D tex;

void main() {
    //    fc=vec4(uv, 0.0, 1.0);
    fc = ao*fakeLight*vec4(color, 1)*texture(tex, uv);
}