import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

class MyContacts extends JFrame {
	DBConnection dbconn;

	private JButton btnInsert = null;
	private JButton btnUpdate = null;
	private JButton btnDelete = null;
	private JTextField txtFilter = null;
	private JButton btnQuery = null;

	private JTable jTable = null;

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(() -> {
			new MyContacts();
		});
	}

	public MyContacts() {
		super("Contacts");
		initialize();

		dbconn = DBConnection.getInstance();
		doQuery();
	}

	private void initialize() {
		JPanel pnlTop = new JPanel();
		txtFilter = new JTextField(20);
		btnQuery = new JButton("Search");
		pnlTop.add(txtFilter);
		pnlTop.add(btnQuery);

		jTable = new JTable();

		btnInsert = new JButton("Insert");
		btnUpdate = new JButton("Update");
		btnDelete = new JButton("Delete");
		JPanel pnlBottom = new JPanel();
		pnlBottom.add(btnInsert);
		pnlBottom.add(btnUpdate);
		pnlBottom.add(btnDelete);

		getContentPane().add(pnlTop, BorderLayout.NORTH);
		getContentPane().add(new JScrollPane(jTable), BorderLayout.CENTER);
		getContentPane().add(pnlBottom, BorderLayout.SOUTH);

		btnQuery.addActionListener(e -> doQuery());
		btnInsert.addActionListener(e -> doInsert());
		btnUpdate.addActionListener(e -> doUpdate());
		btnDelete.addActionListener(e -> doDelete());

		this.setSize(500, 500);
		this.setLocation(300, 150);
		this.setVisible(true);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				if (dbconn != null) {
					dbconn.close();
				}
				dbconn = null;
				System.exit(0);
			}
		});
	}

	private void doQuery() {
		List<Contacts> list = new ArrayList<Contacts>();

		String sWhere = "";
		String word = txtFilter.getText().trim();
		if (word.length() > 0) {
			// To find names containing word:
			sWhere = " where name like '%" + word + "%' " + " or email like '%" + word + "%' ";
		}
		list = (new ContactsDAO()).query(sWhere);
		if (list.size() > 0) {
			Vector<Vector<Object>> data = new Vector<>();
			for (Contacts tempc : list) {
				Vector<Object> record = new Vector<>();
				record.add(tempc.getId());
				record.add(tempc.getName());
				record.add(tempc.getBirthday());
				record.add(tempc.getTelephone());
				record.add(tempc.getEmail());
				record.add(tempc.getRemark());
				data.add(record);
			}
			Vector<String> columns = new Vector<>();
			columns.addAll(Arrays.asList("id", "name", "birthday", "telephone", "email", "remark"));
			jTable.setModel(new DefaultTableModel(data, columns));
		} else {
			JOptionPane.showMessageDialog(this, "No data");
		}
	}

	private void doInsert() {
		System.out.println("0.0..");
		ContactsDetailDialog inputDialog = new ContactsDetailDialog();
		System.out.println("0.1..");
		// inputDialog.uiClear();
		inputDialog.setVisible(true);
		System.out.println("0.2..");
		
		System.out.println("isOkPressed()" + inputDialog.isOkPressed());
		if (!inputDialog.isOkPressed()) {
			System.out.println("0.3..");
			return;
		} else {
			System.out.println("0.4..");
		}

		Contacts inputContact = inputDialog.ui2entity();
		System.out.println("1, " + inputContact);

		int newId = (new ContactsDAO()).insert(inputContact);
		System.out.println("2, " + newId);

		doQuery();
	}

	private void doUpdate() {
		Contacts c = getSelectedContacts();
		if (c == null) {
			JOptionPane.showMessageDialog(this, "Please select one row to update");
			return;
		}

		ContactsDetailDialog inputDialog = new ContactsDetailDialog();
		inputDialog.uiClear();
		inputDialog.entity2ui(c);
		inputDialog.setVisible(true);

		if (!inputDialog.isOkPressed())
			return;

		Contacts inputContact = inputDialog.ui2entity();
		inputContact.setId(c.getId());
		boolean success = (new ContactsDAO()).update(inputContact);

		if (success)
			doQuery();
	}

	private void doDelete() {
		Contacts c = getSelectedContacts();
		if (c == null) {
			JOptionPane.showMessageDialog(this, "Please select one row to delete");
			return;
		}
		if (JOptionPane.showConfirmDialog(this,
				"Do you want to delete the record of " + c.getName()) != JOptionPane.OK_OPTION) {
			return;
		}
		boolean success = (new ContactsDAO()).delete(c);

		if (success)
			doQuery(); // refresh the table
	}

	// get contacts in selected row
	private Contacts getSelectedContacts() {
		if (jTable.getSelectedRowCount() <= 0) {
			return null;
		}

		int row = jTable.getSelectedRow();
		if (row < 0)
			return null;

		TableModel tm = jTable.getModel();
		if (tm == null)
			return null;

		Contacts c = new Contacts();
		c.setId((int) tm.getValueAt(row, 0));
		c.setName(String.valueOf(tm.getValueAt(row, 1)));
		c.setBirthday(String.valueOf(tm.getValueAt(row, 2)));
		c.setTelephone(String.valueOf(tm.getValueAt(row, 3)));
		c.setEmail(String.valueOf(tm.getValueAt(row, 4)));
		c.setRemark(String.valueOf(tm.getValueAt(row, 5)));

		return c;
	}
}