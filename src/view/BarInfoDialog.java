package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import model.VFIFE_Bar;
import model.VFIFE_Material;
import model.VFIFE_Model;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;

public class BarInfoDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JLabel baridLabel;
	private JComboBox comboBox;
	private VFIFE_Bar vbar;
	private VFIFE_Model v5model;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			BarInfoDialog dialog = new BarInfoDialog();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	

	/**
	 * Create the dialog.
	 */
	public BarInfoDialog(VFIFE_Bar vbar,VFIFE_Model v5model) {
		this.vbar = vbar;
		this.v5model = v5model;
		setBounds(100, 100, 188, 242);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("id");
			lblNewLabel.setBounds(10, 10, 12, 15);
			contentPanel.add(lblNewLabel);
		}
		{
			baridLabel = new JLabel();
			this.baridLabel.setText(this.vbar.getBar_id()+"");
			baridLabel.setHorizontalAlignment(SwingConstants.CENTER);
			baridLabel.setBounds(98, 10, 64, 15);
			contentPanel.add(baridLabel);
		}
		
		JLabel lblMaterial = new JLabel("material");
		lblMaterial.setBounds(10, 47, 54, 21);
		contentPanel.add(lblMaterial);
		
		Vector<String> materials = new Vector<String>();
		for(VFIFE_Material vm : this.v5model.getMateriaux()){
			materials.add(vm.getName());
		}
		comboBox = new JComboBox(materials);
		//comboBox.setSelectedItem(vbar.getMaterial().getName());
		comboBox.setSelectedIndex(0);
		comboBox.setBounds(98, 47, 64, 21);
		contentPanel.add(comboBox);
		
		JLabel lblSectionarea = new JLabel("section_area");
		lblSectionarea.setBounds(10, 94, 80, 15);
		contentPanel.add(lblSectionarea);
		
		textField = new JTextField();
		this.textField.setText(this.vbar.getSection_area()+"");
		textField.setBounds(98, 91, 66, 21);
		contentPanel.add(textField);
		textField.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("SAVE");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						BarInfoDialog.this.vbar.setSection_area(Double.parseDouble(BarInfoDialog.this.textField.getText()));
						for(VFIFE_Material vm : BarInfoDialog.this.v5model.getMateriaux())
						{
							if((String)comboBox.getSelectedItem() == vm.getName())
							{
								BarInfoDialog.this.vbar.setMaterial(vm);
								break;
							}
						}
						
						BarInfoDialog.this.dispose();
					}
				});
				okButton.setActionCommand("SAVE");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						BarInfoDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
