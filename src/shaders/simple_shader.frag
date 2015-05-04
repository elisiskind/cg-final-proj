varying vec4 diffuse,ambientGlobal, ambient, ecPos;
varying vec3 normal,halfVector;
varying float dist;
varying vec4 color;
 
void main()
{

    vec3 n,halfV;
    float NdotL,NdotHV;
    color += ambientGlobal;
    float att,spotEffect;

    /* a fragment shader can't write a verying variable, hence we need
    a new variable to store the normalized interpolated normal */
    n = normalize(normal);

//    // Compute the light direction
//    lightDir = vec3(gl_LightSource[0].position-ecPos);
//
//    /* compute the distance to the light source to a varying variable*/
//    dist = length(lightDir);
//
//    /* compute the dot product between normal and ldir */
//    NdotL = max(dot(n,normalize(lightDir)),0.0);
//
//    if (NdotL > 0.0) {
//            color += diffuse * NdotL + ambient;
//            halfV = normalize(halfVector);
//            NdotHV = max(dot(n,halfV),0.0);
//            color += gl_FrontMaterial.specular * gl_LightSource[0].specular * pow(NdotHV,gl_FrontMaterial.shininess);
//    }

    gl_FragColor = color;
}