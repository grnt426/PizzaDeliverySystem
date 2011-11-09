package com.teama.pds;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * This table model will hold our customer information
 */
public class CustomerTableModel extends AbstractTableModel {
	private Controller controller;
	ArrayList<Customer> customerArrayList;
	String[] colNames = {"Name", "Phone", "Number"};

	public CustomerTableModel(Controller controller) {
		this.controller = controller;
		customerArrayList = controller.getAllCustomers();
	}

	/**
	 * Returns the number of rows in the model. A
	 * <code>JTable</code> uses this method to determine how many rows it
	 * should display.  This method should be quick, as it
	 * is called frequently during rendering.
	 *
	 * @return the number of rows in the model
	 * @see #getColumnCount
	 */
	public int getRowCount() {
		return customerArrayList.size();
	}

	/**
	 * Returns the number of columns in the model. A
	 * <code>JTable</code> uses this method to determine how many columns it
	 * should create and display by default.
	 *
	 * @return the number of columns in the model
	 * @see #getRowCount
	 */
	public int getColumnCount() {
		return 3;
	}

	public String getColumnName(int col) {
		return colNames[col];
	}

	/**
	 * Returns the value for the cell at <code>columnIndex</code> and
	 * <code>rowIndex</code>.
	 *
	 * @param rowIndex	the row whose value is to be queried
	 * @param columnIndex the column whose value is to be queried
	 * @return the value Object at the specified cell
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		return grabTableData()[rowIndex][columnIndex];
	}


	//--------Non-Extended-Methods-Below-This-Line-----------------------

	/**
	 * @return a 2D array of our table data
	 */
	private String[][] grabTableData() {
		updateCustomers();
		String[][] tableData = new String[customerArrayList.size()][3];
		int row = 0;
		for (Customer cust : customerArrayList) {
			//name, then phone, then location
			tableData[row][0] = cust.getName();
			tableData[row][1] = cust.getPhoneNumber();
			tableData[row][2] = cust.getLocation();
			row++;
		}
		return tableData;
	}

	private void updateCustomers() {
		customerArrayList = controller.getAllCustomers();
	}
}
