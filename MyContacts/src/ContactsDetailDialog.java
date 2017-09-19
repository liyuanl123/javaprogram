import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ContactsDetailDialog extends JDialog {
	boolean okPressed = false;

	private JLabel lblName = null;
	private JTextField txtName = null;
	private JLabel lblBirthday = null;
	private JTextField txtBirthday = null;
	private JLabel lblTelephone = null;
	private JTextField txtTelephone = null;
	private JLabel lblEmail = null;
	private JTextField txtEmail = null;
	private JLabel lblRemark = null;
	private JTextField txtRemark = null;

	private JPanel pnlInput = null;
	private JPanel pnlButtons = null;

	private JButton btnOk = null;
	private JButton btnCancel = null;

	public ContactsDetailDialog() {
		lblName = new JLabel("Name");
		txtName = new JTextField();
		lblBirthday = new JLabel("Birthday");
		txtBirthday = new JTextField();
		lblTelephone = new JLabel("Telephone");
		txtTelephone = new JTextField();
		lblEmail = new JLabel("Email");
		txtEmail = new JTextField();
		lblRemark = new JLabel("Remark");
		txtRemark = new JTextField();

		pnlInput = new JPanel();
		pnlInput.setLayout(new GridLayout(5, 2));

		pnlInput.add(lblName);
		pnlInput.add(txtName);
		pnlInput.add(lblBirthday);
		pnlInput.add(txtBirthday);
		pnlInput.add(lblEmail);
		pnlInput.add(txtEmail);
		pnlInput.add(lblTelephone);
		pnlInput.add(txtTelephone);
		pnlInput.add(lblRemark);
		pnlInput.add(txtRemark);

		pnlButtons = new JPanel();
		btnOk = new JButton("Ok");
		btnCancel = new JButton("Cancel");
		pnlButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
		pnlButtons.add(btnOk);
		pnlButtons.add(btnCancel);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(pnlInput, BorderLayout.CENTER);
		getContentPane().add(pnlButtons,  BorderLayout.SOUTH);
	
	    setSize(300,200);
	    this.setModal(true);//cannot click on other windows when this dialog is shown
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    
	    btnOk.addActionListener(e->{
	    	okPressed = true;
	    	dispose();//causes the JFrame window to be destroyed and cleaned up by the operating system.
	    });
	    
	    btnCancel.addActionListener(e->{
	    	okPressed = false;
	    	dispose();
	    });
	}
	
	public boolean isOkPressed(){
		return okPressed;
	}
	
	public Contacts ui2entity(){
		Contacts c = new Contacts();
		c.setName(txtName.getText());
		c.setBirthday(txtBirthday.getText());
		c.setTelephone(txtTelephone.getText());
		c.setEmail(txtEmail.getText());
		c.setRemark(txtRemark.getText());
		return c;
	}
	
	public void entity2ui(Contacts c){
		if(c == null){
			uiClear();
			return;
		}
		
		txtName.setText(c.getName());
		txtBirthday.setText(c.getBirthday());
		txtTelephone.setText(c.getBirthday());
		txtEmail.setText(c.getEmail());
		txtRemark.setText(c.getRemark());
	}
	
	public void uiClear(){
		txtName.setText("");
		txtBirthday.setText("");
		txtTelephone.setText("");
		txtEmail.setText("");
		txtRemark.setText("");
	}
}
