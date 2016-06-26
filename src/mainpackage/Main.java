package mainpackage;

import javax.swing.SwingUtilities;

public class Main {
	public static DatabaseModel db = new DatabaseModel("test");

	public static void main(String args[]) {
		db.openConnection("test");
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				GUI gui = new GUI(db);
				gui.initComponents();
			}
		});
	}
}
