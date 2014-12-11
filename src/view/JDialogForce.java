/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import dataStructure.entity.VFIFE_AppliedLoad;
import dataStructure.entity.VFIFE_Bar;
import dataStructure.entity.VFIFE_CartesianPoint;
import dataStructure.entity.VFIFE_Load;
import dataStructure.entity.VFIFE_LoadBar;
import dataStructure.entity.VFIFE_LoadNode;
import dataStructure.entity.VFIFE_Node;

/**
 *
 * @author Xiaowei
 */
public class JDialogForce extends javax.swing.JDialog {

    /**
     * Creates new form JDialogForce
     */
    private VFIFE_Load m_force;

    public JDialogForce(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public JDialogForce(VFIFE_Load force) {
        initComponents();
        m_force = force;

        // set position and size
        setBounds(500, 300, 320, 320);

        jLabel_id.setText(String.valueOf(force.getId()));
        jTextField_start.setText(String.valueOf(force.getStart_time()));
        jTextField_end.setText(String.valueOf(force.getEnd_time()));

        jLabel_type.setText(force.getParent_load_case().toString());
        
        if (force.getClass().toString().contains("LoadMemberConcentrated")) {
            VFIFE_LoadBar v5force = (VFIFE_LoadBar) force;
            // load postion - distance from start end
            VFIFE_CartesianPoint point = v5force.getLoad_position();
            double distance = point.getCoordinate_x();

            VFIFE_Bar supportBar = v5force.getSupporting_bar();
            
            jLabel_element.setText("Bar "+supportBar.getBar_id());
            
            VFIFE_CartesianPoint startPos = supportBar.getStart_node().getCoord();
            VFIFE_CartesianPoint endPos = supportBar.getEnd_node().getCoord();

            double barLength = this.getLength(startPos.getCoordinate_x(),
                    startPos.getCoordinate_y(), startPos.getCoordinate_z(),
                    endPos.getCoordinate_x(), endPos.getCoordinate_y(),
                    endPos.getCoordinate_z());

            double radio = distance / barLength;

            // position on bar
            double px = (startPos.getCoordinate_x() + endPos.getCoordinate_x()) * radio;
            double py = (startPos.getCoordinate_y() + endPos.getCoordinate_y()) * radio;
            double pz = (startPos.getCoordinate_z() + endPos.getCoordinate_z()) * radio;

            jLabel_pos.setText("("+String.valueOf(px)+", "+String.valueOf(py)+", "+String.valueOf(pz)+")");
            
            // force vector
            VFIFE_AppliedLoad staticforce = (VFIFE_AppliedLoad) v5force
                    .getLoad_value();
            double fx = staticforce.getApplied_force_fx();
            double fy = staticforce.getApplied_force_fy();
            double fz = staticforce.getApplied_force_fz();
            
            jLabel_force.setText("("+String.valueOf(fx)+", "+String.valueOf(fy)+", "+String.valueOf(fz)+")");
            
        } else if (force.getClass().toString().contains("LoadNode")) {

            VFIFE_LoadNode v5nodeforce = (VFIFE_LoadNode) force;

            // load postion
            VFIFE_Node supportNode = v5nodeforce.getSupporting_node();
            
            jLabel_element.setText("Node "+supportNode.getNode_id());
            
            VFIFE_CartesianPoint point = supportNode.getCoord();
            double px = point.getCoordinate_x();
            double py = point.getCoordinate_y();
            double pz = point.getCoordinate_z();

            jLabel_pos.setText("("+String.valueOf(px)+", "+String.valueOf(py)+", "+String.valueOf(pz)+")");
            
            // force vector
            VFIFE_AppliedLoad staticforce = (VFIFE_AppliedLoad) v5nodeforce
                    .getLoad_value();
            double fx = staticforce.getApplied_force_fx();
            double fy = staticforce.getApplied_force_fy();
            double fz = staticforce.getApplied_force_fz();

            jLabel_force.setText("("+String.valueOf(fx)+", "+String.valueOf(fy)+", "+String.valueOf(fz)+")");
            
        }
        
       
    }

    
    public double getLength(double p1x, double p1y, double p1z, double p2x,
            double p2y, double p2z) {
        double len = 0;
        len += Math.pow(p1x - p2x, 2);
        len += Math.pow(p1y - p2y, 2);
        len += Math.pow(p1z - p2z, 2);
        len = Math.sqrt(len);
        return len;
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField_start = new javax.swing.JTextField();
        jTextField_end = new javax.swing.JTextField();
        jLabel_id = new javax.swing.JLabel();
        jLabel_element = new javax.swing.JLabel();
        jLabel_pos = new javax.swing.JLabel();
        jLabel_force = new javax.swing.JLabel();
        jLabel_type = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Load Info");
        setPreferredSize(new java.awt.Dimension(330, 320));
        setResizable(false);

        jLabel1.setText("Load ID");

        jLabel2.setText("Supporting Element");

        jLabel3.setText("Load Position");

        jLabel4.setText("Force ");

        jLabel5.setText("Type");

        jLabel6.setText("Start Time");

        jLabel7.setText("End Time");

        jTextField_start.setText("jTextField_starttime");
        jTextField_start.setMinimumSize(new java.awt.Dimension(126, 21));

        jTextField_end.setText("jTextField_endtime");
        jTextField_end.setMinimumSize(new java.awt.Dimension(126, 21));

        jLabel_id.setText("jLabel_id");

        jLabel_element.setText("jLabel_element");

        jLabel_pos.setText("jLabel_pos");

        jLabel_force.setText("jLabel_force");

        jLabel_type.setText("jLabel_type");

        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_type)
                    .addComponent(jLabel_force)
                    .addComponent(jLabel_pos)
                    .addComponent(jLabel_element)
                    .addComponent(jLabel_id)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextField_start, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField_end, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(49, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(50, 50, 50))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel_id))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel_element))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel_pos))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel_force))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel_type))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField_start, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField_end, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // save start and end time
        m_force.setStart_time(Double.parseDouble(this.jTextField_start.getText()));
        m_force.setEnd_time(Double.parseDouble(this.jTextField_end.getText()));

        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel_element;
    private javax.swing.JLabel jLabel_force;
    private javax.swing.JLabel jLabel_id;
    private javax.swing.JLabel jLabel_pos;
    private javax.swing.JLabel jLabel_type;
    private javax.swing.JTextField jTextField_end;
    private javax.swing.JTextField jTextField_start;
    // End of variables declaration//GEN-END:variables
}
