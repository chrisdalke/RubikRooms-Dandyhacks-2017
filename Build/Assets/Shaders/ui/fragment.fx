#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;

out vec4 out_color;

uniform sampler2D textureSampler;

void main(void){

    vec4 textureColor = texture(textureSampler,pass_textureCoords);
    if (textureColor.a<0.5){
        discard;
    }

    out_color = textureColor;
}