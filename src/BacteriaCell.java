import javax.media.opengl.GL;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

public class BacteriaCell extends FloatingObject{

    private static ObjModel geometry;
    

    public BacteriaCell(GL gl) {
        super("models/bacteria.obj", gl);
    }

    @Override
    protected void setShader() {
    	shader.setBaseColor(new Vector4f(0.3f,0.4f,0.0f,0.0f)); //bacteria
		shader.setLightColor(new Vector4f(0.1f,0.1f,0.0f,0.2f));
		shader.setSpecColor(new Vector4f(0.f,0.1f,0.0f,0.2f));
		shader.setLightPosition(new Vector3f(0f,0f,-0.27f));
		shader.setExtinctionCoefficient(new Vector3f(0.1f,0.1f,0.1f));
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
        	gl.glScalef(.02f,.02f,.02f);
    }
}
