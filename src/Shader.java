import javax.media.opengl.GL;
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

        gl.glUseProgram(shaderprogram);
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
}
