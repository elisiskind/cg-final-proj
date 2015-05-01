uniform vec3 LightPosition;
 
varying vec3 worldNormal, eyeVec, lightVec, vertPos, lightPos;
 
void subScatterVS(in vec4 ecVert)
{
    lightVec = LightPosition - ecVert.xyz;
    eyeVec = -ecVert.xyz;
    vertPos = ecVert.xyz;
    lightPos = LightPosition;
}
 
void main()
{
    worldNormal = gl_NormalMatrix * gl_Normal;
     
    vec4 ecPos = gl_ModelViewProjectionMatrix * gl_Vertex;
     
    subScatterVS(ecPos);
     
    gl_Position = ecPos;
 
    gl_TexCoord[0] = gl_TextureMatrix[0] * gl_MultiTexCoord0;
}
