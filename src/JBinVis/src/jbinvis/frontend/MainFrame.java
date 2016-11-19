/*
 *  
 */
package jbinvis.frontend;

import com.jogamp.opengl.GL2;
import jbinvis.renderer.BinVisCanvas;
import jbinvis.renderer.CanvasShader;
import jbinvis.renderer.CanvasTexture;
import jbinvis.renderer.RenderLogic;
import jbinvis.renderer.camera.Camera;
import jbinvis.renderer.camera.OrthographicCamera;

/**
 *
 * @author Billy
 */
public class MainFrame extends javax.swing.JFrame {

    private BinVisCanvas canvas = null;
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        canvas = BinVisCanvas.create(panelCanvas);
        canvas.setRenderLogic(new RenderLogic() { 
            private CanvasShader shader;
            private CanvasTexture texture;
            private Camera camera = null;
            
            @Override
            public void init(GL2 gl) 
            {
                shader = new CanvasShader(gl, "simple");
                texture = new CanvasTexture(gl, 4, 4);
                
                for(int y=0;y<4;y++) {
                    for(int x=0;x<4;x++) {
                        texture.setPixel(x, y, (byte)(0xFF * (x % 2)), (byte)0, (byte)0);
                    }
                }
                texture.update(gl);
                
                camera = new OrthographicCamera(gl);
            }
            
            public void render(GL2 gl, double delta) {
                gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
                gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
                
                camera.update(gl);
                texture.bind(gl);
                shader.begin(gl);
                
                gl.glBegin(GL2.GL_TRIANGLES);
                gl.glColor3d(1,0,1);
                
                gl.glTexCoord2d(0,0);
                gl.glVertex3d(10, 10, 0);
                gl.glTexCoord2d(1,0);
                gl.glVertex3d(120, 10, 0);
                gl.glTexCoord2d(1,1);
                gl.glVertex3d(120, 120, 0);

                gl.glTexCoord2d(0,0);
                gl.glVertex3d(10, 10, 0);
                gl.glTexCoord2d(1,1);
                gl.glVertex3d(120, 120, 0);
                gl.glTexCoord2d(0,1);
                gl.glVertex3d(10, 120, 0);
                gl.glEnd();
                
                shader.end(gl);
                
            }

            @Override
            public void resize(int width, int height) {
                if(camera != null)
                    camera.setViewportDimensions(width, height);
            }
            
            public void dispose(GL2 gl) {
                shader.dispose(gl);
                texture.dispose(gl);
            }
        });
        

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelHeader = new javax.swing.JPanel();
        panelCanvas = new javax.swing.JPanel();
        panelFooter = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuClose = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(640, 640));
        setPreferredSize(new java.awt.Dimension(640, 640));
        setSize(new java.awt.Dimension(640, 640));

        panelHeader.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelHeader.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 695, Short.MAX_VALUE)
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 69, Short.MAX_VALUE)
        );

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelCanvas.setBackground(new java.awt.Color(153, 153, 153));
        panelCanvas.setLayout(new java.awt.BorderLayout());

        panelFooter.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelFooter.setPreferredSize(new java.awt.Dimension(4, 80));

        javax.swing.GroupLayout panelFooterLayout = new javax.swing.GroupLayout(panelFooter);
        panelFooter.setLayout(panelFooterLayout);
        panelFooterLayout.setHorizontalGroup(
            panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 695, Short.MAX_VALUE)
        );
        panelFooterLayout.setVerticalGroup(
            panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 76, Short.MAX_VALUE)
        );

        panelCanvas.add(panelFooter, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(panelCanvas, java.awt.BorderLayout.CENTER);

        jMenu1.setText("File");

        menuClose.setText("Close");
        menuClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCloseActionPerformed(evt);
            }
        });
        jMenu1.add(menuClose);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_menuCloseActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem menuClose;
    private javax.swing.JPanel panelCanvas;
    private javax.swing.JPanel panelFooter;
    private javax.swing.JPanel panelHeader;
    // End of variables declaration//GEN-END:variables
}
