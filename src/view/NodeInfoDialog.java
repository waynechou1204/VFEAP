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
	private VFIFE_Node vnode;

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the dialog.
	 */
	public NodeInfoDialog(VFIFE_Node vnode) {
		this.vnode = vnode;
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
			JLabel nodeidLabel = new JLabel();
			nodeidLabel.setText(vnode.getNode_id()+"");
			nodeidLabel.setHorizontalAlignment(SwingConstants.CENTER);
			nodeidLabel.setBounds(98, 10, 64, 1);
			contentPanel.add(nodeidLabel);
		}
		{
			JLabel lblX = new JLabel("x");
			lblX.setBounds(10, 49, 54, 15);
			contentPanel.add(lblX);
		}
		{
			JLabel lblY = new JLabel("y");
			lblY.setBounds(10, 87, 54, 15);
			contentPanel.add(lblY);
		}
		{
			JLabel lblZ = new JLabel("z");
			lblZ.setBounds(10, 130, 54, 15);
			contentPanel.add(lblZ);
		}
		{
			JLabel xlabel = new JLabel("");
			xlabel.setText(vnode.getCoord().getCoordinate_x()+"");
			xlabel.setHorizontalAlignment(SwingConstants.CENTER);
			xlabel.setBounds(108, 49, 54, 15);
			contentPanel.add(xlabel);
		}
		{
			JLabel ylabel = new JLabel("");
			ylabel.setText(vnode.getCoord().getCoordinate_y()+"");
			ylabel.setHorizontalAlignment(SwingConstants.CENTER);
			ylabel.setBounds(108, 87, 54, 15);
			contentPanel.add(ylabel);
		}
		{
			JLabel zlabel = new JLabel("");
			zlabel.setText(vnode.getCoord().getCoordinate_z()+"");
			zlabel.setHorizontalAlignment(SwingConstants.CENTER);
			zlabel.setBounds(108, 130, 54, 15);
			contentPanel.add(zlabel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
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
	}

}
