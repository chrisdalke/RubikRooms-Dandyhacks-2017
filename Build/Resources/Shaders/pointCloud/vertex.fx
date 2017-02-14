#version 400 core

in vec3 position;

out float depth;

uniform mat4 projectionMatrix;
uniform vec2 shift;
uniform float scale;

void main(void){

    vec4 worldPosition = vec4(position.xyz,1.0);
    worldPosition.x = worldPosition.x - (shift.x/(worldPosition.z*(10)))*0.05;
    worldPosition.y = worldPosition.y - (shift.y/(worldPosition.z*(10)))*0.05;
    worldPosition.x = mod(worldPosition.x+1,2)-1;
    worldPosition.y = mod(worldPosition.y+1,2)-1;
    depth = worldPosition.z;
    gl_Position =  worldPosition;
}