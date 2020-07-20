#version 330 core

uniform vec3 col;

out vec4 fc;

void main(){
    fc = vec4(col, 1);
}