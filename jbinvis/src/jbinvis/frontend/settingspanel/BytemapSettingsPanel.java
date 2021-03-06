/*
 *  
 */
package jbinvis.frontend.settingspanel;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import jbinvis.frontend.QuickEnable;

/**
 *
 * @author Billy
 */
public class BytemapSettingsPanel extends javax.swing.JPanel implements QuickEnable {

    /**
     * Creates new form BytemapSettings
     */
    public BytemapSettingsPanel() {
        initComponents();
        disableAll();
        
        spinnerPixelSize.setModel(new SpinnerListModel(new Object[] {1,2,4,8,16,32}));
        spinnerScanwidth.setModel(new SpinnerNumberModel(1, 1,512, 1));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        comboFunction = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        spinnerPixelSize = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        comboPixelFormat = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        spinnerScanwidth = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        comboColourScheme = new javax.swing.JComboBox<>();

        setPreferredSize(new java.awt.Dimension(300, 600));

        jLabel1.setText("Space Filling Function");

        comboFunction.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Scanline", "Hilbert" }));

        jLabel2.setText("Pixel Size");

        jLabel3.setText("Pixel Format");

        comboPixelFormat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "8bpp", "24bpp RGB", "32bpp ARGB", "32bpp BGRA" }));

        jLabel4.setText("Scanwidth");

        spinnerScanwidth.setToolTipText("Only available for scanline, with pixel size of 1");

        jLabel5.setText("Colour Scheme");

        comboColourScheme.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Green", "Character Class", "Entropy" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboFunction, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(spinnerPixelSize)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(55, 55, 55)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spinnerScanwidth, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(comboPixelFormat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboColourScheme, 0, 135, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboFunction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(spinnerPixelSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(comboPixelFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinnerScanwidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboColourScheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(345, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboColourScheme;
    private javax.swing.JComboBox<String> comboFunction;
    private javax.swing.JComboBox<String> comboPixelFormat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSpinner spinnerPixelSize;
    private javax.swing.JSpinner spinnerScanwidth;
    // End of variables declaration//GEN-END:variables

    @Override
    public void enableAll() {
        comboColourScheme.setEnabled(true);
        comboFunction.setEnabled(true);
        comboPixelFormat.setEnabled(true);
        spinnerPixelSize.setEnabled(true);
        spinnerScanwidth.setEnabled(true);
    }

    @Override
    public void disableAll() {
        comboColourScheme.setEnabled(false);
        comboFunction.setEnabled(false);
        comboPixelFormat.setEnabled(false);
        spinnerPixelSize.setEnabled(false);
        spinnerScanwidth.setEnabled(false);
    }

    public JComboBox<String> getComboColourScheme() {
        return comboColourScheme;
    }

    public JComboBox<String> getComboFunction() {
        return comboFunction;
    }

    public JComboBox<String> getComboPixelFormat() {
        return comboPixelFormat;
    }

    public JSpinner getSpinnerPixelSize() {
        return spinnerPixelSize;
    }

    public JSpinner getSpinnerScanwidth() {
        return spinnerScanwidth;
    }
    
    
}
