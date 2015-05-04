import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.util.Random;

/**
 * Created by eli on 4/28/2015.
 */
public abstract class Tube extends DrawableObject {
    private static Random random;
    protected GLU glu;
    protected WhiteBloodCell whiteBloodCell;

    public Tube(String file, GL gl, GLU glu, WhiteBloodCell whiteBloodCell) {
        super(file, gl);
        this.glu = glu;
        this.whiteBloodCell = whiteBloodCell;
        random = new Random();
    }

    protected Tube makeChild() {
        if(random.nextBoolean() && random.nextBoolean()) {
            return new BranchedTube(gl, glu, whiteBloodCell);
        } else {
            return new StraightTube(gl, glu, whiteBloodCell);
        }
    }

    protected void drawWhiteBloodCell(Vector3f position) {
        gl.glPushMatrix();
        gl.glTranslatef(position.x, position.y-0.1f, position.z - 0.3f);
        whiteBloodCell.draw();
        gl.glPopMatrix();
    }

    protected void setShader() {
        float  MaterialThickness = 0.01f;
        Vector3f ExtinctionCoefficient = new Vector3f(0.1f,0.1f,0.1f);
        Vector4f LightColor = new Vector4f(0.5f,0.1f,0.1f,0.0f);
        Vector4f BaseColor = new Vector4f(1.0f,1.0f,1.0f,0.0f);
        Vector4f SpecColor = new Vector4f(0.2f,0.2f,0.2f,0.0f);
        float SpecPower = 0.2f;
        float RimScalar = 22.0f;
        Vector3f LightPosition = new Vector3f(0.0f,4.0f,-2.0f);
        shader.setUniformVariables(MaterialThickness, ExtinctionCoefficient, LightColor, BaseColor, SpecColor, SpecPower, RimScalar, LightPosition);
        shader.useShader();
    }

    abstract public Tube getChild();

    abstract public void draw(int depth, float t, boolean first);

    abstract public void positionCamera(float t);


}
