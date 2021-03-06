/*
 *  
 */
package jbinvis.visualisations;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import jbinvis.backend.FileCache;
import jbinvis.renderer.camera.PerspectiveCamera;
import jbinvis.main.FileUpdateListener;
import jbinvis.main.JBinVis;
import jbinvis.renderer.BinVisCanvas;
import jbinvis.renderer.CanvasShader;
import jbinvis.renderer.CanvasTexture;
import jbinvis.renderer.RenderLogic;
import jbinvis.renderer.camera.OrthographicCamera;

/**
 *
 * @author Billy
 */
public class Trigraph extends RenderLogic implements FileUpdateListener{
    private JBinVis jbinvis;
    private PerspectiveCamera camera;
    private OrthographicCamera orthoCam;
    
    private CanvasShader shaderPass1;
    private CanvasShader shaderPass2;
    
    private CanvasTexture tex2;
    
    private int u_sampler;
        
    private final int sideCount = 128;
    private final int pointCount = sideCount*sideCount*sideCount;
    
    double angleTheta = 0, angleAlpha = 0;
    
    private GLU glu;
    
    private int halfQuadSize=256, centerX=256, centerY=256;
    
    private int[] fbos = {0};
    private int[] textures = {0,0,0};
    private int[] vbos = {0,0};
    
    private byte[] tex3DBuffer, zeroBuffer;
    private ByteBuffer tex3DBufferWrap;
    
    private int u_front = 0;
    private int u_back = 0;
    private int u_volume = 0;
    
    private boolean signalUpdateTexture = false;
    private boolean initialized =false;
    
    public Trigraph() {
       jbinvis = JBinVis.getInstance();
       glu = new GLU();
    }

    @Override
    public void init(GL2 gl) {
        camera = new PerspectiveCamera(gl);
        camera.eyeZ=5;
        orthoCam = new OrthographicCamera(gl);
        
        shaderPass1 = new CanvasShader(gl, "trigraph_pass1");
        shaderPass2 = new CanvasShader(gl, "trigraph_pass2");
        
        generateTextures(gl);
        generateFramebuffers(gl);
        generateVBO(gl);
        
        
        u_front = shaderPass2.getUniformLocation(gl, "u_front");
        u_back = shaderPass2.getUniformLocation(gl, "u_back");
        u_volume = shaderPass2.getUniformLocation(gl, "u_volume");
        
        
        initialized = true;
    }
    
    private void generateVBO(GL2 gl) {
        // vertex array for cube
        float[] vertArray = {
            -1, 1,-1,
             1, 1,-1,
             1, 1, 1,
            -1, 1, 1,
            -1,-1,-1,
             1,-1,-1,
             1,-1, 1,
            -1,-1, 1
        };
        
        // indices array for cube
        short[] vertIndices = {
            3,2,6, // front
            3,6,7,
            2,1,5, // right
            2,5,6,
            1,0,4, // back
            1,4,5,
            0,3,7, // left
            0,7,4,
            0,1,2, // top
            0,2,3,
            5,4,7, // bottom
            5,7,6       
        };
        
        FloatBuffer vertBuffer = FloatBuffer.wrap(vertArray);
        ShortBuffer vertIndexBuffer= ShortBuffer.wrap(vertIndices);
        
        gl.glGenBuffers(2,vbos,0);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,vbos[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER,vertArray.length*4, 
                vertBuffer,GL2.GL_STATIC_DRAW);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER,vbos[1]);
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER,vertIndices.length*2, 
                vertIndexBuffer,GL2.GL_STATIC_DRAW);
        
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,0);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER,0);
    }
   
    private void generateFramebuffers(GL2 gl) {              
        // create the buffers
        gl.glGenFramebuffers(1, fbos, 0);
  
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, fbos[0]);
        gl.glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT0, GL2.GL_TEXTURE_2D, textures[0], 0);
        gl.glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT1, GL2.GL_TEXTURE_2D, textures[1], 0);
        

        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
    }
    
    private void generateTextures(GL2 gl) {
        // empty buffers to fill texture
        byte[] buffer = new byte[512*512*3];
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        
        // create the textures
        gl.glGenTextures(3,textures,0);
        
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[0]);
        gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0,GL2.GL_RGB, 512,512, 0,GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, byteBuffer);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
        
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[1]);
        gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0,GL2.GL_RGB, 512,512, 0,GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, byteBuffer);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
        
        tex3DBuffer = new byte[pointCount*4];
        zeroBuffer = new byte[pointCount*4];
        
        for(int z=0;z<sideCount;z++) {
            for(int y=0;y<sideCount;y++) {
                for(int x=0;x<sideCount;x++) {
                    int offset = 4*(x+y*sideCount+z*sideCount*sideCount);
                    tex3DBuffer[offset] = (byte)(x);
                    tex3DBuffer[offset+1] = (byte)(y);
                    tex3DBuffer[offset+2] = (byte)(z);
                    tex3DBuffer[offset+3] = (byte)x;
                }
            }
        }
        
        tex3DBufferWrap = ByteBuffer.wrap(tex3DBuffer);
        
        gl.glBindTexture(GL2.GL_TEXTURE_3D, textures[2]);
        gl.glTexImage3D(GL2.GL_TEXTURE_3D, 0,GL2.GL_RGBA, sideCount,sideCount,sideCount, 0,GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, tex3DBufferWrap);
        gl.glTexParameteri(GL2.GL_TEXTURE_3D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_3D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        
        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
        gl.glBindTexture(GL2.GL_TEXTURE_3D, 0);
    }
    
    @Override
    public void update(GL2 gl, double delta) {
        if(camera != null) {
            camera.eyeX = Math.cos(angleAlpha) * Math.cos(angleTheta) * 2.8;
            camera.eyeZ = Math.cos(angleAlpha) * Math.sin(angleTheta) * 2.8;
            camera.eyeY = Math.sin(angleAlpha) * 2.8;
        }
        
        if(signalUpdateTexture) {
            gl.glBindTexture(GL2.GL_TEXTURE_3D, textures[2]);
            gl.glTexImage3D(GL2.GL_TEXTURE_3D, 0,GL2.GL_RGBA, sideCount,sideCount,sideCount, 0,GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, tex3DBufferWrap);
            gl.glBindTexture(GL2.GL_TEXTURE_3D, 0);
            signalUpdateTexture = false;
        }
    }

    @Override
    public void resize(int width, int height) {
        if (camera != null) {
            camera.setViewportDimensions(width, height);
            orthoCam.setViewportDimensions(width, height);
        }
        
        halfQuadSize = Math.min(width, height) / 2;
        centerX = width / 2;
        centerY = height / 2;
    }

    // TODO: Use two FBOs instead, one cull back, one cull front
    // shader1 can be reused for both fbos.
    
    @Override
    public void render(GL2 gl, double delta) {
        if(!jbinvis.isLoaded()) {
            gl.glClearColor(0.5f,0.5f,0.5f,1);
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
            return;
        }
        // (1) render to fbo
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, fbos[0]);
        
        // use perspective camera
        camera.setViewportDimensions(512, 512);
        camera.update(gl);
        shaderPass1.begin(gl);
        
        // preparation
        gl.glPushAttrib(GL2.GL_VIEWPORT_BIT|GL2.GL_ENABLE_BIT | GL2.GL_POLYGON_BIT);
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glFrontFace(GL2.GL_CW);
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glEnableVertexAttribArray(0);
                
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbos[0]);
        gl.glVertexAttribPointer(0, 3, GL2.GL_FLOAT,false, 0, 0);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, vbos[1]);
        
        // render
        gl.glViewport(0,0,512,512);
        gl.glClearColor(0,0,0,1);
        
        // draw onto frontface buffer
        gl.glDrawBuffer(GL2.GL_COLOR_ATTACHMENT0);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);   
        gl.glDrawElements(GL2.GL_TRIANGLES, 36, GL2.GL_UNSIGNED_SHORT, 0);
        
        // draw onto backface buffer
        gl.glDrawBuffer(GL2.GL_COLOR_ATTACHMENT1);
        gl.glCullFace(GL2.GL_FRONT);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);   
        gl.glDrawElements(GL2.GL_TRIANGLES, 36, GL2.GL_UNSIGNED_SHORT, 0);
        
        // clean up
        gl.glDisableVertexAttribArray(0);
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
        gl.glPopAttrib();
        
        shaderPass1.end(gl);
        
        // set render target back to normal
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
        
        
        // (2) render to screen
        gl.glClearColor(0.5f,0.5f,0.5f,1);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        
        gl.glPushAttrib(GL2.GL_ENABLE_BIT);
        gl.glDisable(GL2.GL_CULL_FACE);
        
        shaderPass2.begin(gl);
        
        // set the sampler locations
        gl.glUniform1i(u_front, 0);
        gl.glUniform1i(u_back, 1);
        gl.glUniform1i(u_volume, 2);
        
        // bind textures
        gl.glActiveTexture(GL2.GL_TEXTURE0);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[0]);
        gl.glActiveTexture(GL2.GL_TEXTURE1);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[1]);
        gl.glActiveTexture(GL2.GL_TEXTURE2);
        gl.glBindTexture(GL2.GL_TEXTURE_3D, textures[2]);
        
        // use ortho camera
        orthoCam.update(gl);
        
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3d(1, 0.5, 0.5);
        gl.glVertex2d(centerX - halfQuadSize, centerY - halfQuadSize);
        gl.glTexCoord2d(1, 0);
        gl.glVertex2d(centerX + halfQuadSize, centerY - halfQuadSize);
        gl.glTexCoord2d(1, 1);
        gl.glVertex2d(centerX + halfQuadSize, centerY + halfQuadSize);
        gl.glTexCoord2d(0, 1);
        gl.glVertex2d(centerX - halfQuadSize, centerY + halfQuadSize);
        gl.glTexCoord2d(0, 0);
        gl.glEnd();
        
        shaderPass2.end(gl);
        
        // clean up
        gl.glActiveTexture(GL2.GL_TEXTURE0);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
        gl.glActiveTexture(GL2.GL_TEXTURE1);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
        gl.glActiveTexture(GL2.GL_TEXTURE2);
        gl.glBindTexture(GL2.GL_TEXTURE_3D, 0);
        gl.glPopAttrib();
        
        angleTheta += delta * Math.PI / 10;
        angleAlpha = Math.sin(angleTheta) * Math.PI/4;
    }

    @Override
    public void dispose(GL2 gl) {
        gl.glDeleteBuffers(2, vbos, 0);
        gl.glDeleteTextures(3, textures,0);
        gl.glDeleteFramebuffers(2, fbos,0);
        shaderPass1.dispose(gl);
        shaderPass2.dispose(gl);
    }
    
    private void update3DTexture() {
        if(!jbinvis.isLoaded() || !initialized)
            return;
        long offset = jbinvis.getFileOffset();
        long i = 0;
        long fileLength = jbinvis.getFileSize();
        FileCache file = jbinvis.getFile();
        
        // zero out the buffer
        System.arraycopy(zeroBuffer, 0, tex3DBuffer, 0, pointCount*4);
        
        int a,b,c,buffOff;
        int sideCountSq = sideCount * sideCount;
        while(i<8192 && offset + i + 2<fileLength) {
            a = file.read(offset+i) >> 1;
            b = file.read(offset+i+1) >> 1;
            c = file.read(offset+i+2) >> 1;
            buffOff=4*(a*sideCountSq + b*sideCount + c);
            tex3DBuffer[buffOff+1]=(byte)255;
            tex3DBuffer[buffOff+2]=(byte)255;
            /*
            if(isAbove(tex3DBuffer[buffOff],33)) {
                if(!isAbove(tex3DBuffer[buffOff+2],32))
                      tex3DBuffer[buffOff+2]+=32;
                tex3DBuffer[buffOff]=0;
            }
            else 
                tex3DBuffer[buffOff]+=32;
*/
            
            tex3DBuffer[buffOff+3] = (byte)255;
           i++;
        }
        
        signalUpdateTexture = true;
    }
    
    private boolean isAbove(byte v, int threshold) {
        return v > (threshold-256) && v<0;
    }

    @Override
    public void fileOffsetUpdated() {
        update3DTexture();
    }

    @Override
    public void fileClosed() {
    }

    @Override
    public void fileOpened() {
        update3DTexture();
    }
    
       @Override
    public void onAttachToCanvas(BinVisCanvas canvas) {
        jbinvis.addFileUpdateListener(this);
        update3DTexture();
    }

    @Override
    public void onUnattachFromCanvas(BinVisCanvas canvas) {
        jbinvis.removeFileUpdateListener(this);
    }
    
}
