package mainpackage;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class GUI extends JFrame {

	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = 7492809873433402699L;

	DatabaseModel db;

	public GUI(DatabaseModel db) {
		this.db = db;
	}

	public JLabel lblHead = new JLabel("Demo: SQLite connect with java");

	public String source = null;

	public JScrollPane scrBorder = new JScrollPane();
	public JTable tblData = new JTable();
	public DefaultTableModel tmodel = new DefaultTableModel();

	public JPanel pnlBottom = new JPanel(); // FlowLayout
	public JButton btnUpd = new JButton("Update");
	public JButton btnAdd = new JButton("Add");
	public JButton btnDel = new JButton("Delete");

	public JPanel pnlLeft = new JPanel();
	public JLabel lblName = new JLabel("Full name");
	public JTextField tfName = new JTextField(20);
	public JLabel lblDate = new JLabel("Date");
	public JTextField tfDate = new JTextField(20);
	public JButton btnOk = new JButton("Ok");

	public void initComponents() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// Main
		tmodel.setColumnIdentifiers(db.getColumnName());
		db.loadData(tmodel);
		tblData.setModel(tmodel);
		tblData.setRowHeight(30);
		scrBorder.setViewportView(tblData);
		add(scrBorder, BorderLayout.CENTER);
		// Bottom
		pnlBottom.setLayout(new GridLayout(1, 3));
		pnlBottom.add(btnAdd);
		pnlBottom.add(btnUpd);
		pnlBottom.add(btnDel);
		add(pnlBottom, BorderLayout.SOUTH);
		// Left
		pnlLeft.setLayout(new GridLayout(15, 1));
		pnlLeft.add(lblName);
		pnlLeft.add(tfName);
		pnlLeft.add(lblDate);
		pnlLeft.add(tfDate);
		pnlLeft.add(btnOk);
		btnOk.setEnabled(false);
		tfName.setEditable(false);
		tfDate.setEditable(false);
		add(pnlLeft, BorderLayout.WEST);
		pack();
		setVisible(true);

		btnDel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (tblData.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "No row chosen", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				int row = tblData.getSelectedRow();
				db.deleteByID(Integer.parseInt(tmodel.getValueAt(row, 0).toString()));
				tmodel.removeRow(row);
			}
		});

		btnAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tfName.setEditable(true);
				tfDate.setEditable(true);
				btnOk.setEnabled(true);
				source = "add";
			}
		});

		btnUpd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (tblData.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "No row chosen", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				tfName.setEditable(true);
				tfDate.setEditable(true);
				btnOk.setEnabled(true);

				loadDataFromTable(tfName, tfDate);
				source = "upd";
			}
		});

		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (source == null)
					return;
				if (source.equals("upd")) {
					int id = Integer.parseInt(tmodel.getValueAt(tblData.getSelectedRow(), 0).toString());
					int row = 0;
					db.updateByID(id, tfName.getText(), tfDate.getText());
					for (int i = 0; i < tmodel.getRowCount(); i++)
						if (Integer.parseInt(tmodel.getValueAt(i, 0).toString()) == id) {
							row = i;
							break;
						}
					tmodel.removeRow(row);
					tmodel.insertRow(row, new Object[] { id, tfName.getText(), tfDate.getText() });
				} else { // add
					db.addRow(tfName.getText(), tfDate.getText());
					int id;
					if (tmodel.getRowCount() == 0)
						id = 1;
					else
						id = Integer.parseInt(tmodel.getValueAt(tmodel.getRowCount() - 1, 0).toString()) + 1;
					tmodel.addRow(new Object[] { id, tfName.getText(), tfDate.getText() });

				}
				tfName.setText("");
				tfName.setEditable(false);
				tfDate.setText("");
				tfDate.setEditable(false);
				btnOk.setEnabled(false);
				source = null;
			}
		});
	}

	public void loadDataFromTable(JTextField name, JTextField date) {
		name.setText((String) tblData.getValueAt(tblData.getSelectedRow(), 1));
		date.setText((String) tblData.getValueAt(tblData.getSelectedRow(), 2));
	}
}
