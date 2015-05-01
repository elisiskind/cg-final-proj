varying vec3 normal;
varying vec3 position_eyespace;
varying vec3 position_worldspace;

void main(void){

vec3 color = vec3(1.0,0.2,0.2);

vec3 normal2 = normalize(normal+0.4*noise3(70.0*position_worldspace));

vec3 light = normalize(gl_LightSource[1].position.xyz - position_eyespace);

vec3 eye_vector = normalize(-position_eyespace);

vec3 reflected_vector = normalize(-reflect(light,normal2));

float ambient = 0.6;
float diffuse = 0.4*max(dot(normal2,light),0.0);
float specular = 0.2 * pow(max(dot(reflected_vector,eye_vector),0.0),10.0);
vec3 white = vec3(1.0,1.0,1.0);
color = ambient*color+diffuse*color+specular*white;
gl_FragColor = vec4(color, 1.0);

}
