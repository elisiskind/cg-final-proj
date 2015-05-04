import javax.media.opengl.GL;

/**
 * Created by eli on 5/4/2015.
 */
abstract public class DrawableObject {
    protected String file;
    protected Shader shader;
    protected GL gl;

    DrawableObject(String file, GL gl) {
        this.gl = gl;
        this.file = file;
        init();
    }
    abstract protected void init();
    abstract protected ObjModel getGeometry();

    public void draw() {
        gl.glPushMatrix();
        transform();
        shader.useShader();
        getGeometry().draw(gl);
//        shader.useShader();
        gl.glPopMatrix();
    }

    abstract public void transform();
}
