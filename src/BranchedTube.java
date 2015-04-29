import javax.media.opengl.GL;
import javax.vecmath.Vector3f;

/**
 * Created by eli on 4/28/2015.
 */
public class BranchedTube extends Tube {

    private Tube left = null;
    private Tube right = null;
    private float rotate;
    private static Vector3f childOffset = new Vector3f(0.58f, 0.68f, 0f);
    private static float childRotation = 45;

    public BranchedTube(GL gl) {
        super(gl);
        rotate = (float) Math.random() * 90f;
    }

    @Override
    public void draw(int depth) {
        gl.glPushMatrix();
        gl.glRotatef(rotate, 0, 1, 0);
        getBranchedTube().draw(gl);

        if (depth-- > 0) {
            drawRight(depth);
            drawLeft(depth);
        }

        gl.glPopMatrix();
    }

    @Override
    public Vector3f getCamera(float t) {
        return null;
    }

    /** Draw the right branch **/
    private void drawRight(int depth) {
        if (right == null) right = makeChild();
        gl.glPushMatrix();
        gl.glTranslatef(childOffset.x, childOffset.y, childOffset.z);
        gl.glRotatef(-childRotation, 0, 0, 1);
        right.draw(depth);
        gl.glPopMatrix();
    }

    /** Draw the left branch **/
    private void drawLeft(int depth) {
        if (left == null) left = makeChild();
        gl.glPushMatrix();
        gl.glTranslatef(-childOffset.x, childOffset.y, childOffset.z);
        gl.glRotatef(childRotation, 0, 0, 1);
        left.draw(depth);
        gl.glPopMatrix();
    }
}
