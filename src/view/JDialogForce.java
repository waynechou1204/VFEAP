/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import dataStructure.entity.VFIFE_AppliedLoad;
import dataStructure.entity.VFIFE_Bar;
import dataStructure.entity.VFIFE_CartesianPoint;
import dataStructure.entity.VFIFE_Load;
import dataStructure.entity.VFIFE_LoadBar;
import dataStructure.entity.VFIFE_LoadCaseAccidental;
import dataStructure.entity.VFIFE_LoadCasePermanent;
import dataStructure.entity.VFIFE_LoadCaseRamp;
import dataStructure.entity.VFIFE_LoadCaseVariable;
import dataStructure.entity.VFIFE_LoadNode;
import dataStructure.entity.VFIFE_Node;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import java.awt.Dimension;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Xiaowei
 */
public class JDialogForce extends javax.swing.JDialog {

    /**
     * Creates new form JDialogForce
     */
    private VFIFE_Load m_force;

    /**
     * @wbp.parser.constructor
     */
    public JDialogForce(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setResizable(false);
        initComponents();
    }

    public JDialogForce(VFIFE_Load force) {
        m_force = force;
        initComponents();
        // set position and size
        setBounds(500, 300, 400, 400);

        jLabel_id.setText(String.valueOf(force.getId()));
        jTextField_start.setText(String.valueOf(force.getStart_time()));
        jTextField_end.setText(String.valueOf(force.getEnd_time()));

        String[] names = force.getParent_load_case().getClass().toString().split("[.]");
        //System.out.println(names[names.length-1]);
        comboBox.setSelectedItem(names[names.length-1]);
        //comboBox.setse
        if (force.getClass().toString().contains("VFIFE_LoadBar")) {
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Load Info");
        setPreferredSize(new Dimension(400, 400));

        jLabel1.setText("Force id");

        jLabel2.setText("Supporting Element");

        jLabel3.setText("Load Position");

        jLabel4.setText("Force ");

        jLabel5.setText("Type ");

        jLabel6.setText("Start Time");

        jLabel7.setText("End Time");

        jTextField_start.setText("jTextField_starttime");
        jTextField_start.setMinimumSize(new java.awt.Dimension(126, 21));

        jTextField_end.setText("jTextField_endtime");
        jTextField_end.setMinimumSize(new java.awt.Dimension(126, 21));

        jLabel_id.setText("force id");

//        if(m_force.getClass().toString().contains("VFIFE_LoadNode"))
//        {
//        	VFIFE_LoadNode v5nodeforce = (VFIFE_LoadNode) m_force;
//        	jLabel_element.setText("Node " + v5nodeforce.getSupporting_node().getNode_id());
//        	jLabel_pos.setText("jLabel_pos");
//        }
//        else if(m_force.getClass().toString().contains("VFIFE_LoadBar"))
//        {
//        	VFIFE_LoadBar v5nodeforce = (VFIFE_LoadBar) m_force;
//        	jLabel_element.setText("Bar " + v5nodeforce.getSupporting_bar().getBar_id());
//        	jLabel_pos.setText("jLabel_pos");
//        }
        jLabel_element.setText("support element");

        jLabel_pos.setText("force pos");

        jLabel_force.setText("force value");

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
        
        
        comboBox.addItem("VFIFE_LoadCaseAccidental");
        comboBox.addItem("VFIFE_LoadCasePermanent");
        comboBox.addItem("VFIFE_LoadCaseRamp");
        comboBox.addItem("VFIFE_LoadCaseVariable");
        comboBox.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent paramItemEvent) {
				// TODO Auto-generated method stub
				String type = (String)paramItemEvent.getItem();
				if(type.equals("VFIFE_LoadCaseRamp"))
				{
					//getContentPane().add(paramComponent);
					JDialogForce.this.txtJtextfieldramploadtime.setVisible(true);
					JDialogForce.this.lblRamploadStartTime.setVisible(true);
				}
				else
				{
					JDialogForce.this.txtJtextfieldramploadtime.setVisible(false);
					JDialogForce.this.lblRamploadStartTime.setVisible(false);
				}
			}
        	
        });
        
        
        
        txtJtextfieldramploadtime = new JTextField();
        txtJtextfieldramploadtime.setText("");
        txtJtextfieldramploadtime.setColumns(10);
        if(!this.m_force.getParent_load_case().getClass().equals(VFIFE_LoadCaseRamp.class))
        {
        	this.txtJtextfieldramploadtime.setVisible(false);
        	this.lblRamploadStartTime.setVisible(false);
        }
        else
        {
        	this.txtJtextfieldramploadtime.setText(((VFIFE_LoadCaseRamp)this.m_force.getParent_load_case()).getRamp_duration()+"");
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap(119, Short.MAX_VALUE)
        			.addComponent(jButton1)
        			.addGap(41)
        			.addComponent(jButton2)
        			.addGap(108))
        		.addGroup(Alignment.LEADING, layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jLabel6)
        				.addComponent(jLabel1)
        				.addComponent(jLabel2)
        				.addComponent(jLabel3)
        				.addComponent(jLabel4)
        				.addComponent(jLabel5)
        				.addComponent(jLabel7)
        				.addComponent(lblRamploadStartTime))
        			.addGap(33)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        				.addComponent(txtJtextfieldramploadtime)
        				.addComponent(jLabel_force)
        				.addComponent(jLabel_pos)
        				.addComponent(jLabel_element)
        				.addComponent(jLabel_id)
        				.addComponent(jTextField_start, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(jTextField_end, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(comboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        			.addContainerGap(73, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel1)
        				.addComponent(jLabel_id))
        			.addGap(18)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel2)
        				.addComponent(jLabel_element))
        			.addGap(18)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel3)
        				.addComponent(jLabel_pos))
        			.addGap(18)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel4)
        				.addComponent(jLabel_force))
        			.addGap(18)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel5)
        				.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel6)
        				.addComponent(jTextField_start, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jTextField_end, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel7))
        			.addGap(30)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblRamploadStartTime)
        				.addComponent(txtJtextfieldramploadtime, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jButton1)
        				.addComponent(jButton2))
        			.addContainerGap())
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // save start and end time
        m_force.setStart_time(Double.parseDouble(this.jTextField_start.getText()));
        m_force.setEnd_time(Double.parseDouble(this.jTextField_end.getText()));
        if(((String)this.comboBox.getSelectedItem()).equals("VFIFE_LoadCaseRamp"))
        {
        	this.m_force.setParent_load_case(new VFIFE_LoadCaseRamp(Double.parseDouble(this.txtJtextfieldramploadtime.getText())));
        }
        else if(((String)this.comboBox.getSelectedItem()).equals("VFIFE_LoadCaseAccidental"))
        {
        	this.m_force.setParent_load_case(new VFIFE_LoadCaseAccidental());
        }
        else if(((String)this.comboBox.getSelectedItem()).equals("VFIFE_LoadCasePermanent"))
        {
        	this.m_force.setParent_load_case(new VFIFE_LoadCasePermanent());
        }
        else if(((String)this.comboBox.getSelectedItem()).equals("VFIFE_LoadCaseVariable"))
        {
        	this.m_force.setParent_load_case(new VFIFE_LoadCaseVariable());
        }
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
    private javax.swing.JTextField jTextField_end;
    private javax.swing.JTextField jTextField_start;
    private JComboBox comboBox = new JComboBox();
    private JTextField txtJtextfieldramploadtime;
    private JLabel lblRamploadStartTime = new JLabel("RampLoad Duration");
}
