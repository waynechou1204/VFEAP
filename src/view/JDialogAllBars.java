package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import dataStructure.VFIFE_Model;
import dataStructure.entity.VFIFE_Bar;
import dataStructure.entity.VFIFE_Material;

public class JDialogAllBars extends javax.swing.JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField textField;
    private JComboBox<String> comboBox;
    
    private VFIFE_Model m_v5model;

    /**
     * Create the dialog.
     */
    public JDialogAllBars(VFIFE_Model v5model) {    
    	this.m_v5model = v5model;
        
        setBounds(400, 400, 280, 200);
        this.setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // bar ID info
        JLabel lblNewLabel = new JLabel("Define all bars'material & section area");
        lblNewLabel.setBounds(20, 20, 250, 15);
        contentPanel.add(lblNewLabel);

        // Material name info
        JLabel lblMaterial = new JLabel("Material Name");
        lblMaterial.setBounds(20, 60, 100, 15);
        contentPanel.add(lblMaterial);

        Vector<String> materials = new Vector<String>();
        materials.add("No Change");
        for (VFIFE_Material vm : m_v5model.getMateriaux()) {
            materials.add(vm.getName());
        }
        comboBox = new JComboBox<String>(materials);
        
        comboBox.setBounds(120, 60, 140, 21);
        contentPanel.add(comboBox);

        // Section area info
        JLabel lblSectionarea = new JLabel("Section Area");
        lblSectionarea.setBounds(20, 100, 80, 15);
        contentPanel.add(lblSectionarea);

        textField = new JTextField();
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
            	double section_area_value = 0;
            	if(!JDialogAllBars.this.textField.getText().isEmpty()){	// not empty value, then set to bar
            		section_area_value = Double.parseDouble(JDialogAllBars.this.textField.getText());
            		for(VFIFE_Bar bar:m_v5model.getBars()){
                		bar.setSection_area(section_area_value);
                	}
            	}
            	
            	//Define All elements' material               
	            if ((String) comboBox.getSelectedItem()!="No Change") { 	// set non-null material
            	    for (VFIFE_Material vm : m_v5model.getMateriaux()) {
	                    if ((String) comboBox.getSelectedItem() == vm.getName()) {
	                    	for(VFIFE_Bar bar:m_v5model.getBars()){
	                    		bar.setMaterial(vm);
	                    	}
	                    	break;
	                    }
	                }
                }
                JDialogAllBars.this.dispose();
            }
        });
        okButton.setActionCommand("Save");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	JDialogAllBars.this.dispose();
            }
        });
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);

    }
}
