import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Vector3f;

/**
 * Created by eli on 4/28/2015.
 */
public class StraightTube extends Tube {
    private static ObjModel geometry;
    private Tube child = null;
    private static Vector3f childOffset = new Vector3f(0f, 0f, -1.3f);

    public StraightTube(GL gl, GLU glu, WhiteBloodCell whiteBloodCell) {
        super("models/tube.obj", gl, glu, whiteBloodCell);

    }

    @Override
    public Tube getChild() {
        return child;
    }

    @Override
    public void draw(int depth, float t, boolean first) {
        gl.glPushMatrix();
        if(first) positionCamera(t);
        this.draw();
        drawRedBloodCells();
        drawChild(depth);
        gl.glPopMatrix();
    }

    @Override
    public void positionCamera(float t) {
        Vector3f camera = new Vector3f(0, 0, -1.3f);
        camera.scale(t);
        glu.gluLookAt(camera.x, camera.y, camera.z, -camera.x, -camera.y, -camera.z - 100, 0, 1, 0);

        drawWhiteBloodCell(camera);
    }

    /** Draw the child and its children recursively
     * @param depth, number of times to recursively draw children
     */
    private void drawChild(int depth) {
        if (child == null) child = makeChild();
        gl.glPushMatrix();
        gl.glTranslatef(childOffset.x, childOffset.y, childOffset.z);
        child.draw(depth, 0, false);
        gl.glPopMatrix();
    }

    protected ObjModel getGeometry() {
        if (geometry == null) {
            geometry = new ObjModel(this.file);
        }
        return geometry;
    }

    public void transform() {
        gl.glRotatef(-90, 1, 0, 0);
        gl.glScalef(1.8f, 1.4f, 1.8f);
        gl.glTranslatef(0f, 0.7f, 0);
    }
}
