/**
 * $Id: AreenTableShowHideColumns.java 810 2010-03-16 16:53:43Z dejan $
 *
 * Copyright (c) 2009-2010 Areen Design Services Ltd
 * 282 King Street; London, W6 0SJ; United Kingdom
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Areen Design Services Ltd ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Areen Design Services Ltd.
 */

package jlib.gui.table.areen;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * This JFrame class takes a JTable object as an argument, and interacively
 * shows and hides that table's columns. Use disable() method to disable
 * showing and hiding of a specific column.
 * Example:
 * <code>
 * ...
 * AreenTableShowHideColumns shc = new AreenTableShowHideColumns(myTable);
 * shc.disable(3); // disables column with index 3 (view's index!)
 * shc.setVisible(true);
 * ...
 * </code>
 *
 * @author dejan
 */
public class AreenTableShowHideColumns extends javax.swing.JFrame
		implements ItemListener {
	JTable table;
	TableColumnModel columnModel;
	DefaultTableModel tableModel;
    Vector<ColumnInfo> data; /// Contains all information we need

    /** Creates new form AreenTableShowHideColumns */
    public AreenTableShowHideColumns() {
		data = new Vector<ColumnInfo>();
        initComponents();
		table = new JTable();
		ImageIcon icon = (ImageIcon) cancelButton.getIcon();
		setIconImage(icon.getImage());
		pack();
    } // AreenTableShowHideColumns() constructor

	/** Creates new form AreenTableShowHideColumns */
    public AreenTableShowHideColumns(JTable argTable) {
		table = argTable;
		tableModel = (DefaultTableModel)table.getModel(); // MODEL
		columnModel = table.getColumnModel(); // VIEW
		data = new Vector<ColumnInfo>();
        initComponents();
		initCheckboxes();
		cancelButton.setVisible(false); // for now we are not going to show this button...
		ImageIcon icon = (ImageIcon) cancelButton.getIcon();
		//setIconImage(icon.getImage());
		pack();
    } // AreenTableShowHideColumns() constructor

	void initCheckboxes() {
		int viewIndex = 0;
		JCheckBox tmp = null;
		Dimension d = mainPanel.getPreferredSize();
		d.width = 300;
		for (int i=0; i<tableModel.getColumnCount(); i++) {
			String name = tableModel.getColumnName(i);
			viewIndex = table.convertColumnIndexToView(i);
			
			tmp = new JCheckBox(name + " " + i + "/"+ viewIndex, ((viewIndex > -1) ? true : false));
			tmp.addItemListener(this);
			
			// store all these information in the new ColumnInfo object, and put it into
			// the "data" vector
			ColumnInfo ci = new ColumnInfo(tmp, columnModel.getColumn(viewIndex), viewIndex);
			data.add(ci);

			// finally, add JCheckBox object we created in this iteration to the panel.
			mainPanel.add(tmp);
			d.height =  d.height + tmp.getPreferredSize().height;
		}
		d.height = d.height - 40;
		mainPanel.setPreferredSize(d);
	} // initCheckboxes() method

	/**
	 * Moves the last column in the table (view) argTable to a new position with index
	 * argIndex.
	 *
	 * @param argTable A reference to the JTable object that needs to be manipulated.
	 * @param argIndex An index (position) to where we want the last column to be moved to.
	 */
	public void positionColumn(JTable argTable, int argIndex) {
		int lastColumnIdx = argTable.getColumnCount()-1;
		if (argIndex <= lastColumnIdx) {  // it makes sense to move only if argIndex is lesser thn lastColumnIdx
			// DEBUG: System.out.println(lastColumnIdx + " --> " + argIndex);
			table.moveColumn(lastColumnIdx, argIndex);
		} // if
	} // positionColumn() method

	/**
	 * Handler for the Item(StateChanged) event. Here is the main logic behind
	 * AreenTableShowHideColumns dialog.
	 *
	 * @param e ItemEvent - what happened?
	 */
	public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();
		JCheckBox cb = null;
		int idx = -1;
		ColumnInfo ci = null;
		for (int i = 0; i<data.size(); i++) {
			ci = data.get(i);
			cb = ci.checkBox;
			if (cb == source) {
				idx = i;
				break;
			} // if
		} // for
		// DEBUG: System.out.println(ci);
		// becase we broke the for loop cb contains the reference to the
		// changed checkbox
		int viewIndex = 0;
		if (cb.isSelected()) {
			// add a column into the table (view)
			table.addColumn(ci.column);
			viewIndex = table.convertColumnIndexToView(idx);
			//System.out.println(idx);
			// restore its original position
			positionColumn(table, ci.vindex);
		} else {
			viewIndex = table.convertColumnIndexToView(idx);
			table.removeColumn(columnModel.getColumn(viewIndex));
			/** DEBUG:
			for (int i = 0; i < columnModel.getColumnCount(); i++) {
				System.out.println(columnModel.getColumn(i).getHeaderValue());
			}
			 */
		} // else
	} // itemStateChanged() method

	/**
	 * Disable show/hide of a column with index (model index!) equal to
	 * argIndex.
	 * 
	 * @param argIndex
	 */
	public void disable(int argIndex) {
		data.get(argIndex).checkBox.setEnabled(false);
	} // disable() method

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      headingLabel = new javax.swing.JLabel();
      navigationPanel = new javax.swing.JPanel();
      separator = new javax.swing.JSeparator();
      cancelButton = new javax.swing.JButton();
      okButton = new javax.swing.JButton();
      mainPanel = new javax.swing.JPanel();

      setTitle("Show/Hide columns");

      headingLabel.setFont(new java.awt.Font("Tahoma", 1, 13));
      headingLabel.setText(" Columns to show/hide:");
      getContentPane().add(headingLabel, java.awt.BorderLayout.PAGE_START);

      navigationPanel.setLayout(new javax.swing.BoxLayout(navigationPanel, javax.swing.BoxLayout.LINE_AXIS));

      separator.setPreferredSize(new java.awt.Dimension(50, 5));
      navigationPanel.add(separator);

      //cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/areen/tim/icons/chart_organisation.png")));
      cancelButton.setText("Cancel");
      cancelButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cancelButtonActionPerformed(evt);
         }
      });
      navigationPanel.add(cancelButton);

      okButton.setText("OK");
      okButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            okButtonActionPerformed(evt);
         }
      });
      navigationPanel.add(okButton);

      getContentPane().add(navigationPanel, java.awt.BorderLayout.PAGE_END);

      mainPanel.setPreferredSize(new java.awt.Dimension(200, 50));
      mainPanel.setLayout(new javax.swing.BoxLayout(mainPanel, javax.swing.BoxLayout.PAGE_AXIS));
      getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

      pack();
   }// </editor-fold>//GEN-END:initComponents

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
		setVisible(false);
		dispose();
	}//GEN-LAST:event_okButtonActionPerformed

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
		setVisible(false); // Just hide it - the window must stay alive
		                   // so we do not lose ColumnInfo objects!
	}//GEN-LAST:event_cancelButtonActionPerformed

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton cancelButton;
   private javax.swing.JLabel headingLabel;
   private javax.swing.JPanel mainPanel;
   private javax.swing.JPanel navigationPanel;
   private javax.swing.JButton okButton;
   private javax.swing.JSeparator separator;
   // End of variables declaration//GEN-END:variables

	/**
	 * A simple structure to hold per-column information we need.
	 */
	private class ColumnInfo {
		public ColumnInfo(JCheckBox argCheckBox, TableColumn argTableColumn, int argVIndex) {
			checkBox = argCheckBox;
			column = argTableColumn;
			vindex = argVIndex;
		}
		public JCheckBox checkBox;
		public TableColumn column;
		public int vindex;

		@Override
		public String toString() {
			return "(column_info " + checkBox.getText() + ", " + column.getHeaderValue() + ", " + vindex + ")";
		}

	} // ColumnInfo class

} // AreenTableShowHideColumns

// $Id: AreenTableShowHideColumns.java 810 2010-03-16 16:53:43Z dejan $
