import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by eli on 4/28/2015.
 */
public abstract class Tube extends DrawableObject {
    protected static Random random;
    protected GLU glu;
    static WhiteBloodCell whiteBloodCell;
	ArrayList<FloatingObject> list = new ArrayList<FloatingObject>();
	ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
 
//    RedBloodCell redBloodCellBloodCellGame.list[];
//    Vector3f cellBloodCellGame.positions[];

    public Tube(String file, GL gl, GLU glu, WhiteBloodCell whiteBloodCell) {
        super(file, gl);
        this.glu = glu;
        Tube.whiteBloodCell = whiteBloodCell;
//        redBloodCellBloodCellGame.list = new RedBloodCell[3];
//        cellBloodCellGame.positions = new Vector3f[3];

        for(int i = 0; i < 3; i++) {
        	int x = (int)(Math.random()*100);
        	if (x>22){
            list.add(i,new RedBloodCell(gl));
        	}
        	else if (x<19){
            list.add(i,new BacteriaCell(gl));
            }
        	else
        	list.add(i,new WhiteBloodCell(gl, glu));	
            positions.add(i,new Vector3f((float)Math.random() * 0.18f - 0.08f, (float)Math.random() * 0.18f - 0.08f, (float)Math.random()*0.5f - 0.25f));
        }

        random = new Random();
    }

    protected Tube makeChild() {
        if(random.nextBoolean() && random.nextBoolean()) {
            return new BranchedTube(gl, glu, whiteBloodCell);
        } else {
            return new StraightTube(gl, glu, whiteBloodCell);
        }
    }

    protected void drawWhiteBloodCell(Vector3f position) {
        gl.glPushMatrix();
        position.x += BloodCellGame.position.x;
        position.y += BloodCellGame.position.y;
        gl.glTranslatef(position.x, position.y - 0.1f, position.z - 0.3f);
        whiteBloodCell.draw();
        gl.glPopMatrix();
    }

    protected void setShader() {
        float  MaterialThickness = 0.01f;
        Vector3f ExtinctionCoefficient = new Vector3f(0.1f,0.1f,0.1f);
        Vector4f LightColor = new Vector4f(0.1f,0.0f,0.0f,0.2f);
        Vector4f BaseColor = new Vector4f(0.1f,0.0f,0.0f,0.0f);
        Vector4f SpecColor = new Vector4f(0.1f,0.0f,0.0f,0.2f);
        float SpecPower = 0.2f;
        float RimScalar = 22.0f;
        Vector3f LightPosition = new Vector3f(0.0f,4.0f,-2.0f);
        shader.setUniformVariables(MaterialThickness, ExtinctionCoefficient, LightColor, BaseColor, SpecColor, SpecPower, RimScalar, LightPosition);
        shader.useShader();
    }

    abstract public Tube getChild();

    abstract public void draw(int depth, float t, boolean first);

//    protected void drawRedBloodCells(){
//        for(int i = 0; i < 3; i++) {
//            gl.glPushMatrix();
//            gl.glTranslatef(cellBloodCellGame.positions[i].x, cellBloodCellGame.positions[i].y, cellBloodCellGame.positions[i].z);
//            redBloodCellBloodCellGame.list[i].draw();
//            gl.glPopMatrix();
//        }
//
//    }
    
    protected void drawCells(){
        for(int i = 0; i < 3; i++) {
            gl.glPushMatrix();
            gl.glTranslatef(positions.get(i).x, positions.get(i).y, positions.get(i).z);
            list.get(i).draw();
            gl.glPopMatrix();
        }

    }

    abstract public void positionCamera(float t);


}
