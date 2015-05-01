import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.*;

import java.awt.event.*;
import java.io.*;
import java.nio.*;

class BloodCellGame extends JFrame implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, ActionListener {

    /* GL, display, model transformation, and mouse control variables */
    private final GLCanvas canvas;
    private final GLU glu = new GLU();
    BranchedTube first;
    private ObjModel white = new ObjModel("models/white_blood_cell.obj");
    private ObjModel red = new ObjModel("models/red_blood_cell.obj");
    private GL gl;
    private FPSAnimator animator;
    private int winW = 800, winH = 800;
    private boolean wireframe = false;
    private boolean cullface = true;
    private boolean flatshade = false;
    private float xpos = 0, ypos = 0, zpos = 0;
    private float centerx, centery, centerz;
    private float roth = 0, rotv = 0.1f;
    private float znear, zfar;
    private int mouseX, mouseY, mouseButton;
    private float motionSpeed, rotateSpeed;
    private float animation_speed = 1.0f;
    private float rotate_param = 0;

    public BloodCellGame() {
        super("Blood Cell Game");
        canvas = new GLCanvas();
        canvas.addGLEventListener(this);
        canvas.addKeyListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        animator = new FPSAnimator(canvas, 30);    // create a 30 fps animator
        getContentPane().add(canvas);
        setSize(winW, winH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        animator.start();
        canvas.requestFocus();

    }

    public static void main(String[] args) {

        new BloodCellGame();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
            case KeyEvent.VK_Q:
                System.exit(0);
                break;
            case 'r':
            case 'R':
                initViewParameters();
                break;
            case 'w':
            case 'W':
                wireframe = !wireframe;
                break;
            case 'b':
            case 'B':
                cullface = !cullface;
                break;
            case 'f':
            case 'F':
                flatshade = !flatshade;
                break;
            case 'a':
            case 'A':
                if (animator.isAnimating())
                    animator.stop();
                else
                    animator.start();
                break;
            case '+':
            case '=':
                animation_speed *= 1.2f;
                break;
            case '-':
            case '_':
                animation_speed /= 1.2;
                break;
            default:
                break;
        }
        canvas.display();
    }

    public void display(GLAutoDrawable drawable) {
    	
    //begin shader
    	int v = gl.glCreateShader(GL.GL_VERTEX_SHADER);
    	int v2 = gl.glCreateShader(GL.GL_VERTEX_SHADER);
		int f = gl.glCreateShader(GL.GL_FRAGMENT_SHADER);
		int f2 = gl.glCreateShader(GL.GL_FRAGMENT_SHADER);
		
		String[] fsrc = loadShader("checker.frag");
		String[] vsrc = loadShader("checker.vert");
		String[] vsrc2 = loadShader("sss.vert");
		String[] fsrc2 = loadShader("sss.frag");
		
		gl.glShaderSource(v, 1, vsrc, null, 0);
		gl.glCompileShader(v);
		gl.glShaderSource(f, 1, fsrc, null, 0);
		gl.glCompileShader(f);
		gl.glShaderSource(f2, 1, fsrc2, null, 0);
		gl.glCompileShader(f2);
		gl.glShaderSource(v2, 1, vsrc2, null, 0);
		gl.glCompileShader(v2);
		
		int shaderprogram = gl.glCreateProgram();
		gl.glAttachShader(shaderprogram, v);
		gl.glAttachShader(shaderprogram, f);
		gl.glLinkProgram(shaderprogram);
		gl.glValidateProgram(shaderprogram);
		
		int shaderprogram2 = gl.glCreateProgram();
		gl.glAttachShader(shaderprogram2, v2);
		gl.glAttachShader(shaderprogram2, f);
		gl.glLinkProgram(shaderprogram2);
		gl.glValidateProgram(shaderprogram2);
		
		int shaderprogram3 = gl.glCreateProgram();
		gl.glAttachShader(shaderprogram3, v);
		gl.glAttachShader(shaderprogram3, f2);
		gl.glLinkProgram(shaderprogram3);
		gl.glValidateProgram(shaderprogram3);

	//end shader
		
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, wireframe ? GL.GL_LINE : GL.GL_FILL);
        gl.glShadeModel(flatshade ? GL.GL_FLAT : GL.GL_SMOOTH);
        if (cullface)
            gl.glEnable(GL.GL_CULL_FACE);
        else
            gl.glDisable(GL.GL_CULL_FACE);

        gl.glLoadIdentity();

		/* this is the transformation of the entire scene */

        moveCamera();

		gl.glUseProgram(shaderprogram);
		gl.glScalef(.1f, .1f, .1f);
        white.draw(gl);
        gl.glTranslatef(0, 2.5f, 0);
		gl.glUseProgram(shaderprogram2);
        red.draw(gl);
        gl.glScalef(10f, 10f, 10f);

		gl.glUseProgram(shaderprogram3);
        gl.glPushMatrix();    // push the current matrix to stack
        if (first == null) {
            first = new BranchedTube(gl);
        }
        first.draw(3);
        gl.glPopMatrix();
    }

    private void moveCamera() {
        gl.glTranslatef(-xpos, -ypos, -zpos);
        gl.glTranslatef(centerx, centery, centerz);
        gl.glRotatef(360.f - roth, 0, 1.0f, 0);
        gl.glRotatef(rotv, 1.0f, 0, 0);
        gl.glRotatef(-90, 1.0f, 0, 0);
        gl.glRotatef(rotate_param, 0, 1f, 0);
        gl.glTranslatef(-centerx, -centery, -centerz);
    }

    private void set_material_mushroom() {
        //material
        float mat_ambient[] = {0.6f, 0.6f, 0.6f, 1};
        float mat_specular[] = {0f, 0f, 0f, 1};
        float mat_diffuse[] = {.6f, .6f, .6f, 1};
        float mat_shininess[] = {1};
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, mat_shininess, 0);

        float bmat_ambient[] = {0, 0, 0, 1};
        float bmat_specular[] = {0, .8f, .8f, 1};
        float bmat_diffuse[] = {0, .4f, .4f, 1};
        float bmat_shininess[] = {128};
        gl.glMaterialfv(GL.GL_BACK, GL.GL_AMBIENT, bmat_ambient, 0);
        gl.glMaterialfv(GL.GL_BACK, GL.GL_SPECULAR, bmat_specular, 0);
        gl.glMaterialfv(GL.GL_BACK, GL.GL_DIFFUSE, bmat_diffuse, 0);
        gl.glMaterialfv(GL.GL_BACK, GL.GL_SHININESS, bmat_shininess, 0);
    }

    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL();

        initViewParameters();
        gl.glClearColor(.1f, .1f, .1f, 1f);
        gl.glClearDepth(1.0f);

        // white light at the eye
        float light0_position[] = {0, 0, 1, 0};
        float light0_diffuse[] = {1, 1, 1, 1};
        float light0_specular[] = {1, 1, 1, 1};
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, light0_position, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, light0_diffuse, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, light0_specular, 0);

        //red light
        float light1_position[] = {-.1f, .1f, 0, 0};
        float light1_diffuse[] = {.6f, .05f, .05f, 1};
        float light1_specular[] = {.6f, .05f, .05f, 1};
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, light1_position, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, light1_diffuse, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, light1_specular, 0);

        float position[] = {.1f, .1f, 0, 0};
        float diffuse[] = {.05f, .05f, .6f, 1};
        float specular[] = {.05f, .05f, .6f, 1};
        float spotDirection[] = {-1.0f, -1.0f, 0.f};
        gl.glLightfv(GL.GL_LIGHT2, GL.GL_POSITION, position, 0);
        gl.glLightfv(GL.GL_LIGHT2, GL.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL.GL_LIGHT2, GL.GL_SPECULAR, specular, 0);

        //material
        float mat_ambient[] = {0, 0, 0, 1};
        float mat_specular[] = {.8f, .8f, .8f, 1};
        float mat_diffuse[] = {.4f, .4f, .4f, 1};
        float mat_shininess[] = {128};
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, mat_ambient, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, mat_diffuse, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, mat_shininess, 0);

        float bmat_ambient[] = {0, 0, 0, 1};
        float bmat_specular[] = {0, .8f, .8f, 1};
        float bmat_diffuse[] = {0, .4f, .4f, 1};
        float bmat_shininess[] = {128};
        gl.glMaterialfv(GL.GL_BACK, GL.GL_AMBIENT, bmat_ambient, 0);
        gl.glMaterialfv(GL.GL_BACK, GL.GL_SPECULAR, bmat_specular, 0);
        gl.glMaterialfv(GL.GL_BACK, GL.GL_DIFFUSE, bmat_diffuse, 0);
        gl.glMaterialfv(GL.GL_BACK, GL.GL_SHININESS, bmat_shininess, 0);

        float lmodel_ambient[] = {0, 0, 0, 1};
        gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, lmodel_ambient, 0);
        gl.glLightModeli(GL.GL_LIGHT_MODEL_TWO_SIDE, 1);

        gl.glEnable(GL.GL_NORMALIZE);
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);
        gl.glEnable(GL.GL_LIGHT1);
        gl.glEnable(GL.GL_LIGHT2);

        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LESS);
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        gl.glCullFace(GL.GL_BACK);
        gl.glEnable(GL.GL_CULL_FACE);
        gl.glShadeModel(GL.GL_SMOOTH);
        
		
	}
    
	
	public String[] loadShader( String name )
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            InputStream is = getClass().getResourceAsStream(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
                sb.append('\n');
            }
            is.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return new String[]
        { sb.toString() };
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        winW = width;
        winH = height;

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.f, (float) width / (float) height, znear, zfar);
        gl.glMatrixMode(GL.GL_MODELVIEW);
    }

    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseButton = e.getButton();
        canvas.display();
    }

    public void mouseReleased(MouseEvent e) {
        mouseButton = MouseEvent.NOBUTTON;
        canvas.display();
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (mouseButton == MouseEvent.BUTTON3) {
            zpos -= (y - mouseY) * motionSpeed;
            mouseX = x;
            mouseY = y;
            canvas.display();
        } else if (mouseButton == MouseEvent.BUTTON2) {
            xpos -= (x - mouseX) * motionSpeed;
            ypos += (y - mouseY) * motionSpeed;
            mouseX = x;
            mouseY = y;
            canvas.display();
        } else if (mouseButton == MouseEvent.BUTTON1) {
            roth -= (x - mouseX) * rotateSpeed;
            rotv += (y - mouseY) * rotateSpeed;
            mouseX = x;
            mouseY = y;
            canvas.display();
        }
    }


    /* computes optimal transformation parameters for OpenGL rendering.
     * this is based on an estimate of the scene's bounding box
     */
    void initViewParameters() {
        roth = rotv = 0;

        /*
        float ball_r = (float) Math.sqrt((xmax - xmin) * (xmax - xmin)
                + (ymax - ymin) * (ymax - ymin)
                + (zmax - zmin) * (zmax - zmin)) * 0.707f;

        centerx = (xmax + xmin) / 2.f;
        centery = (ymax + ymin) / 2.f;
        centerz = (zmax + zmin) / 2.f;
        xpos = centerx;
        ypos = centery;
        zpos = ball_r / (float) Math.sin(45.f * Math.PI / 180.f) + centerz;
        */


        znear = 0.01f;
        zfar = 1000.f;


        motionSpeed = 0.002f;
        rotateSpeed = 0.1f;

    }

    // these event functions are not used for this assignment
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
