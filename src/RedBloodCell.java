import javax.media.opengl.GL;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

/**
 * Created by eli on 5/4/2015.
 */
public class RedBloodCell extends FloatingObject{

    private static ObjModel geometry;

    public RedBloodCell(GL gl) {
        super("models/red_blood_cell.obj", gl);
    }

    @Override
    protected void setShader() {
    	shader.setBaseColor(new Vector4f(1.0f,1.0f,1.0f,0.0f)); //red blood cell
		shader.setLightColor(new Vector4f(0.5f,0.1f,0.1f,0.0f));
		shader.setSpecColor(new Vector4f(0.2f,0.2f,0.2f,0.0f));
		shader.setLightPosition(new Vector3f(0.0f,0.0f,-2.0f));
		shader.setExtinctionCoefficient(new Vector3f(10,2,2f));
		shader.setMaterialThickness(0.01f);
//        shader.link();
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
