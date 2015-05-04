import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Vector4f;

/**
 * Created by eli on 5/4/2015.
 */
public class WhiteBloodCell extends FloatingObject{

    private static ObjModel geometry;

    public WhiteBloodCell(GL gl, GLU glu) {
        super("models/white_blood_cell.obj", gl);
    }

    @Override
    protected void setShader() {
        shader.setBaseColor(new Vector4f(1.0f,1.0f,1.0f,0.0f));
        shader.setLightColor(new Vector4f(0.2f,0.2f,0.2f,0.0f));
        shader.useShader();
    }

    @Override
    protected ObjModel getGeometry() {
        if (geometry == null) {
            geometry = new ObjModel(this.file);
        }
        return geometry;
    }

    @Override
    public void transform() {
        super.transform();
        gl.glScalef(0.1f, 0.1f, 0.1f);
    }
}
