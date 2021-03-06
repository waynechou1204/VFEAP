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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dataStructure.VFIFE_Model;
import dataStructure.entity.VFIFE_Bar;
import dataStructure.entity.VFIFE_Material;

public class JDialogBar extends javax.swing.JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField textField;
    private JLabel baridLabel;
    private JComboBox<String> comboBox;
    
    private VFIFE_Bar m_vbar;
    private VFIFE_Model m_v5model;

    /**
     * Create the dialog.
     */
    public JDialogBar(VFIFE_Bar vbar, VFIFE_Model v5model) {
        
        this.m_vbar = vbar;
        this.m_v5model = v5model;
        
        setBounds(400, 400, 280, 200);
        this.setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // bar ID info
        JLabel lblNewLabel = new JLabel("Bar ID");
        lblNewLabel.setBounds(20, 20, 50, 15);
        contentPanel.add(lblNewLabel);

        baridLabel = new JLabel();
        this.baridLabel.setText(m_vbar.getBar_id() + "");
        baridLabel.setHorizontalAlignment(SwingConstants.CENTER);
        baridLabel.setBounds(100, 20, 50, 15);
        contentPanel.add(baridLabel);

        // Material name info
        JLabel lblMaterial = new JLabel("Material Name");
        lblMaterial.setBounds(20, 60, 100, 15);
        contentPanel.add(lblMaterial);

        Vector<String> materials = new Vector<String>();
        materials.add("NULL");
        for (VFIFE_Material vm : m_v5model.getMateriaux()) {
            materials.add(vm.getName());
        }
        comboBox = new JComboBox<String>(materials);
        if (vbar.getMaterial()!=null) {
            comboBox.setSelectedItem(vbar.getMaterial().getName());
        }
        else{
            comboBox.setSelectedIndex(0);
        }
        comboBox.setBounds(120, 60, 140, 21);
        contentPanel.add(comboBox);

        // Section area info
        JLabel lblSectionarea = new JLabel("Section Area");
        lblSectionarea.setBounds(20, 100, 80, 15);
        contentPanel.add(lblSectionarea);

        textField = new JTextField();
        this.textField.setText(m_vbar.getSection_area() + "");
        textField.setBounds(120, 100, 140, 21);
        contentPanel.add(textField);
        textField.setColumns(10);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton("Save");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // set section area and material
                m_vbar.setSection_area(Double.parseDouble(JDialogBar.this.textField.getText()));
                
                // bind material to bar
                if ((String) comboBox.getSelectedItem()=="NULL") { 	// set null material
                	m_vbar.setMaterial(null);
                }
                else{												// set non-null material
                	for (VFIFE_Material vm : m_v5model.getMateriaux()) {
                        if ((String) comboBox.getSelectedItem() == vm.getName()) {
                            m_vbar.setMaterial(vm);
                            break;
                        }
                    }
                }

                JDialogBar.this.dispose();
            }
        });
        okButton.setActionCommand("Save");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JDialogBar.this.dispose();
            }
        });
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);

    }
}
