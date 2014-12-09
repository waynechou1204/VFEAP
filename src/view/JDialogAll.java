package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dataStructure.VFIFE_Model;
import dataStructure.entity.VFIFE_Bar;
import dataStructure.entity.VFIFE_Material;

public class JDialogAll extends javax.swing.JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField textField;
    private JComboBox comboBox;
    
    private VFIFE_Bar m_vbar;
    private VFIFE_Model m_v5model;

    /**
     * Create the dialog.
     */
  //  public JDialogAll(VFIFE_Bar vbar, VFIFE_Model v5model) {
    public JDialogAll(VFIFE_Model v5model) {    
       // this.m_vbar = vbar;
        this.m_v5model = v5model;
        
        setBounds(400, 400, 280, 200);
        this.setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // bar ID info
        JLabel lblNewLabel = new JLabel("All Bars");
        lblNewLabel.setBounds(94, 22, 50, 15);
        contentPanel.add(lblNewLabel);

        // Material name info
        JLabel lblMaterial = new JLabel("Material Name");
        lblMaterial.setBounds(20, 60, 100, 15);
        contentPanel.add(lblMaterial);

        Vector<String> materials = new Vector<String>();
        for (VFIFE_Material vm : m_v5model.getMateriaux()) {
            materials.add(vm.getName());
        }
        comboBox = new JComboBox(materials);
        
//        if (vbar.getMaterial()!=null) {
//            comboBox.setSelectedItem(vbar.getMaterial().getName());
//        }
//        else{
//            comboBox.setSelectedIndex(0);
//        }
        comboBox.setBounds(120, 60, 140, 21);
        contentPanel.add(comboBox);

        // Section area info
        JLabel lblSectionarea = new JLabel("Section Area");
        lblSectionarea.setBounds(20, 100, 80, 15);
        contentPanel.add(lblSectionarea);

        textField = new JTextField();
//        this.textField.setText(m_vbar.getSection_area() + "");
        textField.setBounds(120, 100, 140, 21);
        contentPanel.add(textField);
        textField.setColumns(10);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton("Save");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // set All elements' section area
            	for(VFIFE_Bar bar:m_v5model.getBars()){
            		bar.setSection_area(Double.parseDouble(JDialogAll.this.textField.getText()));
            	}
            
                
                for (VFIFE_Material vm : m_v5model.getMateriaux()) {
                    if ((String) comboBox.getSelectedItem() == vm.getName()) {
        //Define All elements' material               
                    	for(VFIFE_Bar bar:m_v5model.getBars()){
//                    		bar.setSection_area(Double.parseDouble(JDialogAll.this.textField.getText()));
                    		bar.setMaterial(vm);
                    	}
                    	break;
                    }
                }

                JDialogAll.this.dispose();
            }
        });
        okButton.setActionCommand("Save");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JDialogAll.this.dispose();
            }
        });
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);

    }
}
