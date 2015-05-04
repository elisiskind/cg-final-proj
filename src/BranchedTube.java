import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Vector3f;

/**
 * Created by eli on 4/28/2015.
 */
public class BranchedTube extends Tube {
    private static ObjModel geometry;
    private Tube left = null;
    private Tube right = null;
    private final float translations[] = {-0.92f, -0.05f};
    private static float childRotation = 45;
    private boolean nextIsLeft;

    public BranchedTube(GL gl, GLU glu, WhiteBloodCell whiteBloodCell) {
        super("models/branch.obj", gl, glu, whiteBloodCell);
        nextIsLeft = random.nextBoolean();
    }

    @Override
    public void draw(int depth, float t, boolean first) {

        gl.glPushMatrix();
//        gl.glRotatef(rotate, 0, 0, 1);

        if(first) positionCamera(t);
        this.draw();
//        drawRedBloodCells();
        drawCells();

        if (depth-- > 0) {
            gl.glPushMatrix();
            if(nextIsLeft)
                drawLeft(depth);
            else
                drawRight(depth);
            gl.glPopMatrix();
            gl.glPushMatrix();
            if(nextIsLeft)
                drawRight(depth);
            else
                drawLeft(depth);
            gl.glPopMatrix();
        }

        gl.glPopMatrix();
    }

    protected ObjModel getGeometry() {
        if (geometry == null) {
            geometry = new ObjModel(this.file);
        }
        return geometry;
    }

    @Override
    public void positionCamera(float t) {
        float scalar = nextIsLeft ? -1 : 1;

        Vector3f camera = new Vector3f(0, 0, t * -1.07f);
        if(t >= 0.8) {
            camera.x += 1.07 * scalar * (t - 0.8);
            camera.z *= 1.1f;
        }

        float x = 0;
        if(t > 0.6 && t <= 0.8) {
            x = scalar * ((t - 0.6f)/0.2f * 100);
        } else if (t > 0.8){
            x = scalar * 100;
        }

        glu.gluLookAt(camera.x, camera.y, camera.z, -camera.x + x, -camera.y, -camera.z - 100, 0, 1, 0);

        if(t > 0.75){
            camera.x += scalar * (t - 0.75f);
        }

        drawWhiteBloodCell(camera);
    }

    public Tube getChild() {
        return nextIsLeft ? left : right;
    }

    /** Draw the right branch **/
    private void drawRight(int depth) {
        if (right == null) right = makeChild();
        gl.glTranslatef(0, 0, translations[0]);
        gl.glRotatef(-childRotation, 0, 1, 0);
        gl.glTranslatef(0, 0, translations[1]);
        right.draw(depth, 0, false);
    }

    /** Draw the left branch **/
    private void drawLeft(int depth) {
        if (left == null) left = makeChild();
        gl.glTranslatef(0, 0, translations[0]);
        gl.glRotatef(childRotation, 0, 1, 0);
        gl.glTranslatef(0, 0, translations[1]);
        left.draw(depth, 0, false);
    }



    public void transform() {
        gl.glRotatef(-90, 0, 1, 0);
        gl.glTranslatef(-1f, 0, 0);
    }
}
