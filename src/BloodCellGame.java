import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.j2d.TextRenderer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import javax.vecmath.Vector2f;
import java.awt.*;
import java.awt.event.*;

class BloodCellGame extends JFrame implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, ActionListener {

    /* GL, display, model transformation, and mouse control variables */
    private final GLCanvas canvas;
    private final GLU glu = new GLU();
    Tube first;
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
    private float t = 0;
    private TextRenderer titleRenderer;
    private TextRenderer subtitleRenderer;
    private int showText = 100;

    public static Vector2f position = new Vector2f(0, 0);

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
            case KeyEvent.VK_UP:
                if(position.y < 0.12) position.y += 0.006;
                break;
            case KeyEvent.VK_DOWN:
                if(position.y > -0.01) position.y -= 0.006;
                break;
            case KeyEvent.VK_LEFT:
                if(position.x > -0.05) position.x -= 0.006;
                break;
            case KeyEvent.VK_RIGHT :
                if(position.x < 0.05) position.x += 0.006;
                break;
            case KeyEvent.VK_Q:
                System.exit(0);
                break;
        }
        canvas.display();
    }

    public void display(GLAutoDrawable drawable) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, wireframe ? GL.GL_LINE : GL.GL_FILL);
        gl.glShadeModel(flatshade ? GL.GL_FLAT : GL.GL_SMOOTH);
        if (cullface)
            gl.glEnable(GL.GL_CULL_FACE);
        else
            gl.glDisable(GL.GL_CULL_FACE);

        gl.glLoadIdentity();

		/* this is the transformation of the entire scene */


        if(showText-- > 0) {
            // optionally set the color
            float alpha = (showText < 20) ? ((float)showText / 20f) : 1;

            titleRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
            titleRenderer.setColor(1, 0.2f, 0.2f, alpha);
            titleRenderer.draw("Blood Cell Visualization",50, 80);
            titleRenderer.endRendering();

            subtitleRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
            subtitleRenderer.setColor(0.7f, 0.2f, 0.2f, alpha);
            subtitleRenderer.draw("Eli Siskind, Alex Lee",50, 50);
            subtitleRenderer.endRendering();
        } else {
            updateScene();
        }

    }

    private void updateScene() {
        if(first == null) first = new BranchedTube(gl, glu, new WhiteBloodCell(gl, glu));

        first.draw(2, t, true);

        t += 0.02;

        if(t >= 1){
            t = 0;
            first = first.getChild();
        }

    }

    private void moveCamera() {
        gl.glTranslatef(-xpos, -ypos, -zpos);
        gl.glTranslatef(centerx, centery, centerz);
        gl.glRotatef(360.f - roth, 0, 1.0f, 0);
        gl.glRotatef(rotv, 1.0f, 0, 0);
        //gl.glRotatef(-90, 1.0f, 0, 0);
        gl.glRotatef(rotate_param, 0, 1f, 0);
        gl.glTranslatef(-centerx, -centery, -centerz);
    }

    private void set_material() {
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

    public void init_lights() {
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
        gl.glLightfv(GL.GL_LIGHT2, GL.GL_POSITION, position, 0);
        gl.glLightfv(GL.GL_LIGHT2, GL.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL.GL_LIGHT2, GL.GL_SPECULAR, specular, 0);
    }

    @Override
    /**
     * Initializes shading parameters and view parameters
     * @param drawable
     */
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL();

        titleRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 36));
        subtitleRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 24));
        initViewParameters();
        initShadingParameters();
        init_lights();


    }

    /**
     * Sets parameters so that objects are in view, and animation speeds are correct.
     */
    void initViewParameters() {
        roth = rotv = 0;

        znear = 0.01f;
        zfar = 1000.f;

        motionSpeed = 0.002f;
        rotateSpeed = 0.1f;
    }

    /**
     * Initializes values for correct jogl shading, like backface culling and depth testing.
     */
    private void initShadingParameters() {
        gl.glClearColor(.1f, .1f, .1f, 1f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LESS);
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        gl.glCullFace(GL.GL_BACK);
        gl.glEnable(GL.GL_CULL_FACE);
        gl.glShadeModel(GL.GL_SMOOTH);
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