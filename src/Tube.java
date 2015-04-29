import javax.media.opengl.GL;
import javax.vecmath.Vector3f;
import java.util.Random;

/**
 * Created by eli on 4/28/2015.
 */
public abstract class Tube {
    private static ObjModel straightTubeGeometry = null;
    private static ObjModel branchedTubeGeometry = null;
    private static Random random;
    protected GL gl;

    public Tube(GL gl) {
        this.gl = gl;
        random = new Random();
    }

    protected static ObjModel getStraightTube() {
        if (straightTubeGeometry == null) {
            straightTubeGeometry = new ObjModel("models/straight_tube.obj");
        }
        return straightTubeGeometry;
    }

    protected static ObjModel getBranchedTube() {
        if (branchedTubeGeometry == null) {
            branchedTubeGeometry = new ObjModel("models/branch_tube.obj");
        }
        return branchedTubeGeometry;
    }

    protected Tube makeChild() {
        if(random.nextBoolean()) {
            return new BranchedTube(gl);
        } else {
            return new StraightTube(gl);
        }
    }

    abstract public void draw(int depth);

    abstract public Vector3f getCamera(float t);
}
