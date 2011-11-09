package com.teama.pds;

import javax.swing.*;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.Enumeration;

/**
 * @author Matthew Frey
 *         Column Model for our Customer Table Model
 */
public class CustomerTableColumnModel implements TableColumnModel {
	Controller controller;
	String[] columns;

	public CustomerTableColumnModel(Controller controller) {
		this.controller = controller;
		columns = new String[]{"Name", "Phone", "Location"};
	}

	/**
	 * Appends <code>aColumn</code> to the end of the
	 * <code>tableColumns</code> array.
	 * This method posts a <code>columnAdded</code>
	 * event to its listeners.
	 *
	 * @param aColumn the <code>TableColumn</code> to be added
	 * @see #removeColumn
	 */
	public void addColumn(TableColumn aColumn) {
		//not allowing adding
	}

	/**
	 * Deletes the <code>TableColumn</code> <code>column</code> from the
	 * <code>tableColumns</code> array.  This method will do nothing if
	 * <code>column</code> is not in the table's column list.
	 * This method posts a <code>columnRemoved</code>
	 * event to its listeners.
	 *
	 * @param column the <code>TableColumn</code> to be removed
	 * @see #addColumn
	 */
	public void removeColumn(TableColumn column) {
		//not implementing
	}

	/**
	 * Moves the column and its header at <code>columnIndex</code> to
	 * <code>newIndex</code>.  The old column at <code>columnIndex</code>
	 * will now be found at <code>newIndex</code>.  The column that used
	 * to be at <code>newIndex</code> is shifted left or right
	 * to make room.  This will not move any columns if
	 * <code>columnIndex</code> equals <code>newIndex</code>.  This method
	 * posts a <code>columnMoved</code> event to its listeners.
	 *
	 * @param columnIndex the index of column to be moved
	 * @param newIndex	index of the column's new location
	 * @throws IllegalArgumentException if <code>columnIndex</code> or
	 *                                  <code>newIndex</code>
	 *                                  are not in the valid range
	 */
	public void moveColumn(int columnIndex, int newIndex) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Sets the <code>TableColumn</code>'s column margin to
	 * <code>newMargin</code>.  This method posts
	 * a <code>columnMarginChanged</code> event to its listeners.
	 *
	 * @param newMargin the width, in pixels, of the new column margins
	 * @see #getColumnMargin
	 */
	public void setColumnMargin(int newMargin) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Returns the number of columns in the model.
	 *
	 * @return the number of columns in the model
	 */
	public int getColumnCount() {
		return 3;
	}

	/**
	 * Returns an <code>Enumeration</code> of all the columns in the model.
	 *
	 * @return an <code>Enumeration</code> of all the columns in the model
	 */
	public Enumeration<TableColumn> getColumns() {
		return null;
	}

	/**
	 * Returns the index of the first column in the table
	 * whose identifier is equal to <code>identifier</code>,
	 * when compared using <code>equals</code>.
	 *
	 * @param columnIdentifier the identifier object
	 * @return the index of the first table column
	 *         whose identifier is equal to <code>identifier</code>
	 * @throws IllegalArgumentException if <code>identifier</code>
	 *                                  is <code>null</code>, or no
	 *                                  <code>TableColumn</code> has this
	 *                                  <code>identifier</code>
	 * @see #getColumn
	 */
	public int getColumnIndex(Object columnIdentifier) {
		return 0;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Returns the <code>TableColumn</code> object for the column at
	 * <code>columnIndex</code>.
	 *
	 * @param columnIndex the index of the desired column
	 * @return the <code>TableColumn</code> object for
	 *         the column at <code>columnIndex</code>
	 */
	public TableColumn getColumn(int columnIndex) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Returns the width between the cells in each column.
	 *
	 * @return the margin, in pixels, between the cells
	 */
	public int getColumnMargin() {
		return 0;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Returns the index of the column that lies on the
	 * horizontal point, <code>xPosition</code>;
	 * or -1 if it lies outside the any of the column's bounds.
	 * <p/>
	 * In keeping with Swing's separable model architecture, a
	 * TableColumnModel does not know how the table columns actually appear on
	 * screen.  The visual presentation of the columns is the responsibility
	 * of the view/controller object using this model (typically JTable).  The
	 * view/controller need not display the columns sequentially from left to
	 * right.  For example, columns could be displayed from right to left to
	 * accomodate a locale preference or some columns might be hidden at the
	 * request of the user.  Because the model does not know how the columns
	 * are laid out on screen, the given <code>xPosition</code> should not be
	 * considered to be a coordinate in 2D graphics space.  Instead, it should
	 * be considered to be a width from the start of the first column in the
	 * model.  If the column index for a given X coordinate in 2D space is
	 * required, <code>JTable.columnAtPoint</code> can be used instead.
	 *
	 * @return the index of the column; or -1 if no column is found
	 * @see javax.swing.JTable#columnAtPoint
	 */
	public int getColumnIndexAtX(int xPosition) {
		return 0;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Returns the total width of all the columns.
	 *
	 * @return the total computed width of all columns
	 */
	public int getTotalColumnWidth() {
		return 0;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Sets whether the columns in this model may be selected.
	 *
	 * @param flag true if columns may be selected; otherwise false
	 * @see #getColumnSelectionAllowed
	 */
	public void setColumnSelectionAllowed(boolean flag) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Returns true if columns may be selected.
	 *
	 * @return true if columns may be selected
	 * @see #setColumnSelectionAllowed
	 */
	public boolean getColumnSelectionAllowed() {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Returns an array of indicies of all selected columns.
	 *
	 * @return an array of integers containing the indicies of all
	 *         selected columns; or an empty array if nothing is selected
	 */
	public int[] getSelectedColumns() {
		return new int[0];  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Returns the number of selected columns.
	 *
	 * @return the number of selected columns; or 0 if no columns are selected
	 */
	public int getSelectedColumnCount() {
		return 0;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Sets the selection model.
	 *
	 * @param newModel a <code>ListSelectionModel</code> object
	 * @see #getSelectionModel
	 */
	public void setSelectionModel(ListSelectionModel newModel) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Returns the current selection model.
	 *
	 * @return a <code>ListSelectionModel</code> object
	 * @see #setSelectionModel
	 */
	public ListSelectionModel getSelectionModel() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Adds a listener for table column model events.
	 *
	 * @param x a <code>TableColumnModelListener</code> object
	 */
	public void addColumnModelListener(TableColumnModelListener x) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Removes a listener for table column model events.
	 *
	 * @param x a <code>TableColumnModelListener</code> object
	 */
	public void removeColumnModelListener(TableColumnModelListener x) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
