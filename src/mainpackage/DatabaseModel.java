package mainpackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DatabaseModel {
	Connection con = null;
	String databaseName;

	public DatabaseModel(String databaseName) {
		setDatabaseName(databaseName);
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public void openConnection(String database) {
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + database + ".db");
			System.out.println("Connected to database: " + database);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Object[] getColumnName() {
		Object result[] = null;
		try {
			Statement st = con.createStatement();
			ResultSetMetaData rsmeta = st.executeQuery("SELECT * FROM customer_info").getMetaData();
			result = new Object[rsmeta.getColumnCount()];
			for (int i = 1; i <= rsmeta.getColumnCount(); i++) {
				result[i - 1] = rsmeta.getColumnName(i);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void loadData(DefaultTableModel tmodel) {
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM customer_info");
			Object row[] = new Object[tmodel.getColumnCount()];
			while (rs.next()) {
				for (int i = 0; i < tmodel.getColumnCount(); i++) {
					row[i] = rs.getObject(i + 1);
				}
				tmodel.addRow(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteByID(int id) {
		try {
			Statement st = con.createStatement();
			st.executeUpdate("DELETE FROM customer_info WHERE id = " + id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateByID(int id, String name, String date) {
		try {
			Statement st = con.createStatement();
			System.out.println(id);
			System.out.println("UPDATE customer_info "
					+ "SET name = \'" + name + "\', "
					+ "date = \'" + date + "\') "
					+ "WHERE id = " + id);
			st.executeUpdate("UPDATE customer_info "
					+ "SET name = \'" + name + "\', "
					+ "date = \'" + date + "\' "
					+ "WHERE id = " + id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addRow(String name, String date) {
		try {
			Statement st = con.createStatement();
			st.executeUpdate("INSERT INTO customer_info (name, date) VALUES (\'" + name + "\',\'" + date
					+ "\')");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Add error!");
		}
	}

}