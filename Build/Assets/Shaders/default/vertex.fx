#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;

uniform mat4 transform;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void){

    vec4 worldPosition = vec4(position.xyz,1.0);

    gl_Position = projectionMatrix * viewMatrix * transform * worldPosition;
    pass_textureCoords = textureCoords;

    surfaceNormal = (transformationMatrix * vec4(normal,0.0)).xyz;
}