import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Vector3f;
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
    	shader.setBaseColor(new Vector4f(0.7f,0.7f,0.7f,0.4f)); //white blood cell
		shader.setLightColor(new Vector4f(0.3f,0.3f,0.3f,0.0f));
		shader.setSpecColor(new Vector4f(0.5f,0.0f,0.5f,0.0f));
		shader.setLightPosition(new Vector3f(0.0f,0.2f,-1.0f));
		shader.setExtinctionCoefficient(new Vector3f(7f,4f,7f));
		shader.setMaterialThickness(0.015f);
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
