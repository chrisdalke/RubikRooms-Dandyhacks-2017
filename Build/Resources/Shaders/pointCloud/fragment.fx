#version 400 core

in float depth;

out vec4 out_color;

void main(void){

    float color = 1.0 - depth;
    out_color = vec4(color, color,color,1.0);
}