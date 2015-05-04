import javax.media.opengl.GL;
import javax.vecmath.Vector3f;

/**
 * Created by eli on 5/4/2015.
 */
abstract public class FloatingObject extends DrawableObject {

    private Vector3f spin;
    private float spinAmount;
    private float spinSpeed;

    FloatingObject(String file, GL gl) {
        super(file, gl);

        spin = new Vector3f((float)Math.random(), (float)Math.random(), (float)Math.random());
        spinAmount = 60;
        spinSpeed = 1 + (float)Math.random() * 4f;
    }

    @Override
    public void transform() {
        gl.glRotatef(spinAmount += spinSpeed, spin.x, spin.y, spin.z);
    }

}
