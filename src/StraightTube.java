import javax.media.opengl.GL;
import javax.vecmath.Vector3f;

/**
 * Created by eli on 4/28/2015.
 */
public class StraightTube extends Tube {

    private Tube child = null;
    private static Vector3f childOffset = new Vector3f(0f, 1.1f, 0f);
    private static Vector3f offset = new Vector3f(0f, -0.1f, 0f);

    public StraightTube(GL gl) {
        super(gl);
    }

    @Override
    public void draw(int depth) {
        gl.glPushMatrix();
        gl.glTranslatef(offset.x, offset.y, offset.z);
        getStraightTube().draw(gl);

        if (depth > 0) {
            drawChild(depth--);
        }

        gl.glPopMatrix();
    }

    @Override
    public Vector3f getCamera(float t) {
        return null;
    }

    /** Draw the child and its children recursively
     * @param depth, number of times to recursively draw children
     */
    private void drawChild(int depth) {
        if (child == null) child = makeChild();
        gl.glPushMatrix();
        gl.glTranslatef(childOffset.x, childOffset.y, childOffset.z);
        child.draw(depth);
        gl.glPopMatrix();
    }
}
