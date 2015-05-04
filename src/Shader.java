import javax.media.opengl.GL;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Created by eli on 5/2/2015.
 */
public class Shader {
    GL gl;
    int shaderprogram;

    public enum Type {
        VERTEX(GL.GL_VERTEX_SHADER),
        FRAGMENT(GL.GL_FRAGMENT_SHADER);

        public int shader;
        Type(int shader) {
            this.shader = shader;
        }
    }

    public Shader(GL gl) {
        this.gl = gl;
        this.shaderprogram = gl.glCreateProgram();
    
    }

    public void load(String filename, Type t) {

        String[] src = readFile(filename);
        int glShader = gl.glCreateShader(t.shader);

        gl.glShaderSource(glShader, 1, src, null, 0);
        gl.glCompileShader(glShader);
        gl.glAttachShader(shaderprogram, glShader);

    }

    public void useShader() {
        gl.glUseProgram(shaderprogram);
    }

    public void link() {
        gl.glLinkProgram(shaderprogram);
        gl.glValidateProgram(shaderprogram);
        IntBuffer intBuffer = IntBuffer.allocate(1);
        gl.glGetProgramiv(shaderprogram, GL.GL_LINK_STATUS, intBuffer);

        if (intBuffer.get(0) != 1)
        {
            gl.glGetProgramiv(shaderprogram, GL.GL_INFO_LOG_LENGTH, intBuffer);
            int size = intBuffer.get(0);
            System.err.println("Program link error: ");
            if (size > 0)
            {
                ByteBuffer byteBuffer = ByteBuffer.allocate(size);
                gl.glGetProgramInfoLog(shaderprogram, size, intBuffer, byteBuffer);
                for (byte b : byteBuffer.array())
                {
                    System.err.print((char) b);
                }
            }
            else
            {
                System.out.println("Unknown");
            }
            System.exit(1);
        }
        //gl.glUseProgram(shaderprogram);
    }

    public String[] readFile(String name) {
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

    public void init() {
        float  MaterialThickness = 0.01f;
        Vector3f ExtinctionCoefficient = new Vector3f(0.1f,0.1f,0.1f);
        Vector4f LightColor = new Vector4f(0.5f,0.1f,0.1f,0.0f);
        Vector4f BaseColor = new Vector4f(1.0f,1.0f,1.0f,0.0f);
        Vector4f SpecColor = new Vector4f(0.2f,0.2f,0.2f,0.0f);
        float SpecPower = 0.2f;
        float RimScalar = 22.0f;
        Vector3f LightPosition = new Vector3f(0.0f,4.0f,-2.0f);
        setUniformVariables(MaterialThickness, ExtinctionCoefficient, LightColor, BaseColor, SpecColor, SpecPower, RimScalar, LightPosition);
    }

    public void setUniformVariables(float MaterialThickness, Vector3f ExtinctionCoefficient, Vector4f LightColor, Vector4f BaseColor, Vector4f SpecColor, float SpecPower, float RimScalar, Vector3f LightPosition) {
    	int loc1 = gl.glGetUniformLocation(shaderprogram, "MaterialThickness");
    	gl.glUniform1f(loc1, MaterialThickness);
    	int loc2 = gl.glGetUniformLocation(shaderprogram, "ExtinctionCoefficient");
    	gl.glUniform3f(loc2, ExtinctionCoefficient.x,ExtinctionCoefficient.y,ExtinctionCoefficient.z);
    	int loc3 = gl.glGetUniformLocation(shaderprogram, "LightColor");
    	gl.glUniform4f(loc3, LightColor.x,LightColor.y,LightColor.z,LightColor.w);
    	int loc4 = gl.glGetUniformLocation(shaderprogram, "BaseColor");
    	gl.glUniform4f(loc4, BaseColor.x,BaseColor.y,BaseColor.z,BaseColor.w);
    	int loc5 = gl.glGetUniformLocation(shaderprogram, "SpecColor");
    	gl.glUniform4f(loc5, SpecColor.x,SpecColor.y,SpecColor.z,SpecColor.w);
    	int loc6 = gl.glGetUniformLocation(shaderprogram, "SpecPower");
    	gl.glUniform1f(loc6, SpecPower);
    	int loc7 = gl.glGetUniformLocation(shaderprogram, "RimScalar");
    	gl.glUniform1f(loc7, RimScalar);
    	int loc8 = gl.glGetUniformLocation(shaderprogram, "LightPosition");
    	gl.glUniform3f(loc8, LightPosition.x,LightPosition.y,LightPosition.z);
    	
    }
    
    public void setMaterialThickness(float MaterialThickness){
    	int loc = gl.glGetUniformLocation(shaderprogram, "MaterialThickness");
    	gl.glUniform1f(loc, MaterialThickness);
    }
    
    public void setExtinctionCoefficient(Vector3f ExtinctionCoefficient){
    	int loc = gl.glGetUniformLocation(shaderprogram, "ExtinctionCoefficient");
    	gl.glUniform3f(loc, ExtinctionCoefficient.x,ExtinctionCoefficient.y,ExtinctionCoefficient.z);
    }
    
    public void setLightColor(Vector4f LightColor){
    	int loc = gl.glGetUniformLocation(shaderprogram, "LightColor");
    	gl.glUniform4f(loc, LightColor.x,LightColor.y,LightColor.z,LightColor.w);
    }
    
    public void setBaseColor(Vector4f BaseColor){
    	int loc = gl.glGetUniformLocation(shaderprogram, "BaseColor");
    	gl.glUniform4f(loc, BaseColor.x,BaseColor.y,BaseColor.z,BaseColor.w);
    }
    
    public void setSpecColor(Vector4f SpecColor){
    	int loc = gl.glGetUniformLocation(shaderprogram, "SpecColor");
    	gl.glUniform4f(loc, SpecColor.x,SpecColor.y,SpecColor.z,SpecColor.w);
    }

    public void setSpecPower(float SpecPower){
    	int loc = gl.glGetUniformLocation(shaderprogram, "SpecPower");
    	gl.glUniform1f(loc, SpecPower);
    }
    
    public void setRimPower(float RimPower){
    	int loc = gl.glGetUniformLocation(shaderprogram, "RimPower");
    	gl.glUniform1f(loc, RimPower);
    }
    
    public void setLightPosition(Vector3f LightPosition){
    	int loc = gl.glGetUniformLocation(shaderprogram, "LightPosition");
    	gl.glUniform3f(loc, LightPosition.x,LightPosition.y,LightPosition.z);
    }
    
}
