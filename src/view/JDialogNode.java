package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.media.j3d.Transform3D;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import dataStructure.entity.VFIFE_BoundaryConditionLogical;
import dataStructure.entity.VFIFE_Node;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JDialogNode extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private VFIFE_Node m_vnode;
    private VFIFE_Modeling_view view = null;
    private JCheckBox xdisplacementRestraint;
    private JCheckBox ydisplacementRestraint;
    private JCheckBox zdisplacementRestraint;
    private JCheckBox xrotationRestraint;
    private JCheckBox yrotationRestraint;
    private JCheckBox zrotationRestraint;

    /**
     * Launch the application.
     */
    /**
     * Create the dialog.
     */
    public JDialogNode(VFIFE_Modeling_view view, VFIFE_Node vnode) {
        this.m_vnode = vnode;
        this.view = view;

        setBounds(500, 300, 300, 346);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblNewLabel = new JLabel("Node ID");
        lblNewLabel.setBounds(10, 10, 100, 15);
        contentPanel.add(lblNewLabel);

        JLabel nodeidLabel = new JLabel();
        nodeidLabel.setText(m_vnode.getNode_id() + "");
        nodeidLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nodeidLabel.setBounds(98, 10, 64, 15);
        contentPanel.add(nodeidLabel);

        JLabel lblX = new JLabel("X");
        lblX.setBounds(10, 35, 54, 15);
        contentPanel.add(lblX);

        JLabel lblY = new JLabel("Y");
        lblY.setBounds(10, 60, 54, 15);
        contentPanel.add(lblY);

        JLabel lblZ = new JLabel("Z");
        lblZ.setBounds(10, 85, 54, 15);
        contentPanel.add(lblZ);

        JLabel xlabel = new JLabel("");
        xlabel.setText(m_vnode.getCoord().getCoordinate_x() + "");
        xlabel.setHorizontalAlignment(SwingConstants.CENTER);
        xlabel.setBounds(108, 35, 54, 15);
        contentPanel.add(xlabel);

        JLabel ylabel = new JLabel("");
        ylabel.setText(m_vnode.getCoord().getCoordinate_y() + "");
        ylabel.setHorizontalAlignment(SwingConstants.CENTER);
        ylabel.setBounds(108, 62, 54, 15);
        contentPanel.add(ylabel);

        JLabel zlabel = new JLabel("");
        zlabel.setText(m_vnode.getCoord().getCoordinate_z() + "");
        zlabel.setHorizontalAlignment(SwingConstants.CENTER);
        zlabel.setBounds(108, 87, 54, 15);
        contentPanel.add(zlabel);

        JLabel label = new JLabel("Constraint Name:");
        label.setBounds(10, 121, 120, 15);
        contentPanel.add(label);

        JLabel boundConNamelbl = new JLabel("");
        if (m_vnode.getRestraint() != null) {
            boundConNamelbl.setText("Non-free node");
        } else {
            boundConNamelbl.setText("Free node");
        }
        boundConNamelbl.setHorizontalAlignment(SwingConstants.CENTER);
        boundConNamelbl.setBounds(108, 121, 100, 15);
        contentPanel.add(boundConNamelbl);

        JLabel lblNewLabel_1 = new JLabel("Restraints of freedom:");
        lblNewLabel_1.setBounds(10, 146, 200, 15);
        contentPanel.add(lblNewLabel_1);

        JLabel boundConDesclbl = new JLabel("");
        boundConDesclbl.setHorizontalAlignment(SwingConstants.CENTER);
        boundConDesclbl.setBounds(108, 146, 100, 15);
        if (m_vnode.getRestraint() != null) {
            boundConDesclbl.setText(" "); // description not stored
        }
        contentPanel.add(boundConDesclbl);

        xdisplacementRestraint = new JCheckBox("X Displacement");
        xdisplacementRestraint.setBounds(10, 174, 139, 23);
        if (vnode.getRestraint() != null && !vnode.getRestraint().getBc_x_displacement_free()) {
            xdisplacementRestraint.setSelected(true);
        }
        contentPanel.add(xdisplacementRestraint);

        xrotationRestraint = new JCheckBox("X Rotation");
        xrotationRestraint.setBounds(163, 174, 132, 23);
        if (vnode.getRestraint() != null && !vnode.getRestraint().getBc_x_rotation_free()) {
            xrotationRestraint.setSelected(true);
        }
        contentPanel.add(xrotationRestraint);

        ydisplacementRestraint = new JCheckBox("Y Displacement");
        ydisplacementRestraint.setBounds(10, 199, 139, 23);
        if (vnode.getRestraint() != null && !vnode.getRestraint().getBc_y_displacement_free()) {
            ydisplacementRestraint.setSelected(true);
        }
        contentPanel.add(ydisplacementRestraint);

        yrotationRestraint = new JCheckBox("Y Rotation");
        yrotationRestraint.setBounds(163, 199, 132, 23);
        if (vnode.getRestraint() != null && !vnode.getRestraint().getBc_y_rotation_free()) {
            yrotationRestraint.setSelected(true);
        }
        contentPanel.add(yrotationRestraint);

        zdisplacementRestraint = new JCheckBox("Z Displacement");
        zdisplacementRestraint.setBounds(10, 224, 139, 23);
        if (vnode.getRestraint() != null && !vnode.getRestraint().getBc_z_displacement_free()) {
            zdisplacementRestraint.setSelected(true);
        }
        contentPanel.add(zdisplacementRestraint);

        zrotationRestraint = new JCheckBox("Z Rotation");
        zrotationRestraint.setBounds(163, 224, 132, 23);
        if (vnode.getRestraint() != null && !vnode.getRestraint().getBc_z_rotation_free()) {
            zrotationRestraint.setSelected(true);
        }
        contentPanel.add(zrotationRestraint);

        xdisplacementRestraint.setEnabled(true);
        ydisplacementRestraint.setEnabled(true);
        zdisplacementRestraint.setEnabled(true);
        xrotationRestraint.setEnabled(true);
        yrotationRestraint.setEnabled(true);
        zrotationRestraint.setEnabled(true);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                
                // if there is no boundary condition, create one
                if (m_vnode.getRestraint() == null) {
                    m_vnode.setRestraint(new VFIFE_BoundaryConditionLogical(true,true,true,true,true,true));
                }

                // set restraint values and save
                JDialogNode.this.m_vnode.getRestraint().setBc_x_displacement_free(!JDialogNode.this.xdisplacementRestraint.isSelected());
                JDialogNode.this.m_vnode.getRestraint().setBc_y_displacement_free(!JDialogNode.this.ydisplacementRestraint.isSelected());
                JDialogNode.this.m_vnode.getRestraint().setBc_z_displacement_free(!JDialogNode.this.zdisplacementRestraint.isSelected());
                JDialogNode.this.m_vnode.getRestraint().setBc_x_rotation_free(!JDialogNode.this.xrotationRestraint.isSelected());
                JDialogNode.this.m_vnode.getRestraint().setBc_y_rotation_free(!JDialogNode.this.yrotationRestraint.isSelected());
                JDialogNode.this.m_vnode.getRestraint().setBc_z_rotation_free(!JDialogNode.this.zrotationRestraint.isSelected());
                JDialogNode.this.view.updateScene(new Transform3D());
                JDialogNode.this.dispose();
            }
        });
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

    }
}
