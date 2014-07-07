package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import model.VFIFE_Node;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NodeInfoDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private VFIFE_Node m_vnode;

    /**
     * Launch the application.
     */
    /**
     * Create the dialog.
     */
    public NodeInfoDialog(VFIFE_Node vnode) {
        this.m_vnode = vnode;
        
        setBounds(500, 300, 300, 250);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblNewLabel = new JLabel("Node ID");
        lblNewLabel.setBounds(10, 10, 100, 15);
        contentPanel.add(lblNewLabel);

        JLabel nodeidLabel = new JLabel();
        nodeidLabel.setText(m_vnode.getNode_name()+ "");
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
        if (m_vnode.getRestraint()!=null) {
            boundConNamelbl.setText(m_vnode.getRestraint().getBoundary_condition_name()); 
        }
        else{
            boundConNamelbl.setText("Free node");
        }
        boundConNamelbl.setHorizontalAlignment(SwingConstants.CENTER);
        boundConNamelbl.setBounds(108, 121, 100, 15);
        contentPanel.add(boundConNamelbl);

        JLabel lblNewLabel_1 = new JLabel("Constraint Detail:");
        lblNewLabel_1.setBounds(10, 146, 120, 15);
        contentPanel.add(lblNewLabel_1);

        JLabel boundConDesclbl = new JLabel("");
        boundConDesclbl.setText(m_vnode.getRestraint().getBoundary_condition_description());
        boundConDesclbl.setHorizontalAlignment(SwingConstants.CENTER);
        boundConDesclbl.setBounds(108, 146, 100, 15);
        contentPanel.add(boundConDesclbl);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                NodeInfoDialog.this.dispose();
            }
        });
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

    }
}
