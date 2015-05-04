import javax.media.opengl.GL;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

/**
 * Created by eli on 5/4/2015.
 */
abstract public class FloatingObject extends DrawableObject {

    private Vector3f spin;
    private float spinAmount;

    FloatingObject(String file, GL gl) {
        super(file, gl);

        spin = new Vector3f((float)Math.random(), (float)Math.random(), (float)Math.random());
    }

    @Override
    protected void init() {
        shader = new Shader(gl);
        shader.load("shaders/sss.vert", Shader.Type.VERTEX);
        shader.load("shaders/sss.frag", Shader.Type.FRAGMENT);
        shader.link();

        float  MaterialThickness = 0.01f;
        Vector3f ExtinctionCoefficient = new Vector3f(0.1f,0.1f,0.1f);
        Vector4f LightColor = new Vector4f(0.5f,0.1f,0.1f,0.0f);
        Vector4f BaseColor = new Vector4f(1.0f,1.0f,1.0f,0.0f);
        Vector4f SpecColor = new Vector4f(0.2f,0.2f,0.2f,0.0f);
        float SpecPower = 0.2f;
        float RimScalar = 22.0f;
        Vector3f LightPosition = new Vector3f(0.0f,4.0f,-2.0f);
        shader.setUniformVariables(MaterialThickness, ExtinctionCoefficient, LightColor, BaseColor, SpecColor, SpecPower, RimScalar, LightPosition);
    }

    @Override
    public void transform() {
        gl.glRotatef(spinAmount += 2, spin.x, spin.y, spin.z);
    }

}
