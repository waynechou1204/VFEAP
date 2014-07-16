/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import static control.Modeling.exportFile;
import static control.Modeling.loadCIS;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;
import jsdai.lang.SdaiException;
import model.VFIFE_Model;

/**
 *
 * @author Xiaowei
 */
public class JFrameMain extends javax.swing.JFrame {

    private VFIFE_Model m_v5model = null;

    // panel
    private VFIFE_Modeling_view m_view = null;

    /**
     * Creates new form JFrameMain
     *
     * @param v5model as the model passed in
     */
    public JFrameMain(VFIFE_Model v5model) {

        // make menu always before
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        initComponents();

        // set frame location
        Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.width > displaySize.width) {
            frameSize.width = displaySize.width;
        }
        if (frameSize.height > displaySize.height) {
            frameSize.height = displaySize.height;
        }
        this.setLocation((displaySize.width - frameSize.width) / 2, (displaySize.height - frameSize.height) / 2);

        // set visibility
        this.setVisible(true);

        // set model data
        m_v5model = v5model;
        m_view = new VFIFE_Modeling_view(m_v5model);

        //m_view=new VFIFE_Modeling_view();
        this.getContentPane().add(m_view, BorderLayout.CENTER);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenuItemOpenv5m = new javax.swing.JMenuItem();
        jMenuItemExport = new javax.swing.JMenuItem();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuDefine = new javax.swing.JMenu();
        jMenuItemMaterial = new javax.swing.JMenuItem();
        jMenuSpecify = new javax.swing.JMenu();
        jMenuItemLoad = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("VFIFE Modeling Tool");
        setName("mainframe"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1024, 768));
        setResizable(false);

        jMenuBar.setPreferredSize(new java.awt.Dimension(81, 28));

        jMenuFile.setText("File");
        jMenuFile.setPreferredSize(new java.awt.Dimension(35, 19));

        jMenuItemOpen.setText("Open");
        jMenuItemOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemOpen);
        
        jMenuItemOpenv5m.setText("Openv5m");
        jMenuItemOpenv5m.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jMenuItemOpenv5mActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemOpenv5m);
        
        jMenuItemExport.setText("Export");
        jMenuItemExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExportActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemExport);

        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemExit);

        jMenuBar.add(jMenuFile);

        jMenuDefine.setText("Define");

        jMenuItemMaterial.setText("Material");
        jMenuItemMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMaterialActionPerformed(evt);
            }
        });
        jMenuDefine.add(jMenuItemMaterial);

        jMenuBar.add(jMenuDefine);

        jMenuSpecify.setText("Specify");

        jMenuItemLoad.setText("Load");
        jMenuSpecify.add(jMenuItemLoad);

        jMenuBar.add(jMenuSpecify);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 272, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenActionPerformed
        // get file open path
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CIS2/STP FILE", "stp"));
        int i = fileChooser.showOpenDialog(getContentPane());

        if (i == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String stpFilePath = selectedFile.getAbsolutePath();

            try {
                // load file and parse
                this.getContentPane().remove(m_view);
                m_v5model = loadCIS(stpFilePath);
                m_view = new VFIFE_Modeling_view(m_v5model);
                this.getContentPane().add(m_view, BorderLayout.CENTER);

            } catch (SdaiException e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jMenuItemOpenActionPerformed

    
    private void jMenuItemOpenv5mActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenActionPerformed
        // get file open path
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("V5M FILE", "v5m"));
        int i = fileChooser.showOpenDialog(getContentPane());

        if (i == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String stpFilePath = selectedFile.getAbsolutePath();

            try {
                // load file and parse
                this.getContentPane().remove(m_view);
                m_v5model = control.Modeling.loadV5(stpFilePath);
                m_view = new VFIFE_Modeling_view(m_v5model);
                this.getContentPane().add(m_view, BorderLayout.CENTER);

            } catch (SdaiException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void jMenuItemExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExportActionPerformed
        if (!m_v5model.isEmpty()) {
            // get file save path
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("model.v5m")); // default file name
            fileChooser.setFileFilter(new FileNameExtensionFilter("V5M & XML FILE", "v5m", "xml"));
            int i = fileChooser.showSaveDialog(getContentPane());
            if (i == JFileChooser.APPROVE_OPTION) {
                File getfile = fileChooser.getSelectedFile();
                try {
                    if (getfile != null) {
                        exportFile(m_v5model, getfile.getAbsolutePath());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (SdaiException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No model to export", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItemExportActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jMenuItemMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMaterialActionPerformed
        if (!m_v5model.isEmpty()) {
            JDialogMateriaux materiaux = new JDialogMateriaux(m_v5model);
        }
    }//GEN-LAST:event_jMenuItemMaterialActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuDefine;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemExport;
    private javax.swing.JMenuItem jMenuItemLoad;
    private javax.swing.JMenuItem jMenuItemMaterial;
    private javax.swing.JMenuItem jMenuItemOpen;
    private javax.swing.JMenuItem jMenuItemOpenv5m;
    private javax.swing.JMenu jMenuSpecify;
    // End of variables declaration//GEN-END:variables
}
