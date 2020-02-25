package io.koreada.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Dialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	JLabel lblNewLabel = new JLabel("Dialog");
	private JFrame parentFrame = new JFrame();
	Dialog dialog = null;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			Dialog dialog = new Dialog();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public Dialog(final java.awt.Frame parent) {
		setTitle("Information");
		initialize();
	}
	
	public void initialize() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setType(Type.POPUP);
		setResizable(false);
		setModal(true);
		setBounds(0, 0, 450, 111);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel, BorderLayout.CENTER);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						closeDialog();
					}					
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
	
	private void closeDialog() {
		// TODO Auto-generated method stub
		this.setVisible(false);
	}

	public void showMessage(String string) {
		// TODO Auto-generated method stub
		try {
			this.lblNewLabel.setText(string);
			this.setTitle(string);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setLocationRelativeTo(parentFrame);
			this.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
