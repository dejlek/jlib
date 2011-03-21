/**
 * $Id: FilterPanel.java 1011 2010-07-08 11:28:38Z dyki $
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

package jlib.gui.table.areen.filter;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import jlib.gui.table.areen.AreenTableRowSorter;

/**
 *
 * @todo FilterPanel does not yet register for filteredTable's resize events.
 *       It is important we do so ASAP otherwise FilterPanel will not resize accordingly.
 * @author dejan
 */
public class FilterPanel extends javax.swing.JPanel implements TableColumnModelListener {
    JTable filteredTable = null;                /// A JTable object that is supposed to be filtered
	TableColumnModel columnModel = null;        /// filteredTable's column model - used for convenience
    Vector<JComponent> filters;                 /// contains a list of all components in the FilterPanel
	int lastPosX = 0;
	int lastPosY = 0;
	JPanel me;
	Vector<BaseRowFilter> rowFilters;           /// Filters that will be used
	Map<TableColumn,Integer> link;              /// We need this map to find out what is the (view) index
	                                            /// of the TableColumn object we are filtering
	/// TODO: Remove this map above if we do not use it anymore.
	
	AreenTableRowSorter rowSorter;

	RowFilter<DefaultTableModel, Object> tableRowFilter;
	boolean doesFiltering;                      /// Indicator telling us if the currently added filter actually
	                                            /// _DOES_ some filtering, or not
	DefaultTableModel tableModel;                 /// Model of the given table. We have it here just for convenience.

    /** Creates new form FilterPanel */
    public FilterPanel() {
        filters = new Vector<JComponent>();
        initComponents();
		me = this;
		columnModel = new DefaultTableColumnModel();
		rowFilters = new Vector<BaseRowFilter>();
		link = new HashMap<TableColumn,Integer>();
    } // FilterPanel() constructor

	/**
	 * Yet another constructor that takes a JTable AND an AreenTableRowSorter object.
	 * @param argTable A JTable object that is filtered by a FilterPanel.
	 */
	public FilterPanel(JTable argTable, AreenTableRowSorter argSorter) {
		this();
		columnModel = null; // setTable() will change it
		rowSorter = argSorter;
		// setTable() must be called AFTER rowSorter is set!
		setTable(argTable);
		link = new HashMap<TableColumn,Integer>();
    } // FilterPanel() constructor

	/**
	 * Yet another constructor that takes a JTable object.
	 * @param argTable A JTable object that is filtered by a FilterPanel.
	 */
	public FilterPanel(JTable argTable) {
		this();
		columnModel = null; // setTable() will change it
		setTable(argTable);
    } // FilterPanel() constructor

	public String dump() {
		String ret = "[" + filters.size() + " objects:";
		for (JComponent jc: filters) {
			ret += "(" + jc.getClass().getSimpleName() + " ";
			ret += jc.getLocation().x + ",";
			ret += jc.getLocation().y + " ";
			ret += jc.getWidth() + "," + jc.getHeight() + " ";
			ret += ")";
		}
		ret += "]";
		return ret;
	} // dump()

	public BaseRowFilter getRowFilter(int argIndex) {
		return rowFilters.get(argIndex);
	}

	public void addRowFilter(BaseRowFilter argRowFilter) {
		rowFilters.add(argRowFilter);
	}
	
	public void setRowFilter(int argIndex, BaseRowFilter argRowFilter) {
		rowFilters.set(argIndex, argRowFilter);
	}

	/**
	 * Makes a new RowFilter objects and sets it to be a new RowFilter in the
	 * rowSorter object.
	 * TODO: Rename this method into updateFilter() !
	 */
	public void setFilter() {
        tableRowFilter = new RowFilter<DefaultTableModel, Object>() {
            public boolean include(Entry entry) {
				boolean ret = true;
				for (int i = 0; i<rowFilters.size(); i++) {
					//just in case the value is null - it used to throw null pointer and prevented loading the table
					Object val = entry.getValue(rowFilters.get(i).getColumnIndex());
					if(val==null){
						break;
					}
					
					String ref = (String)(val.toString());
					//System.out.print("|" + ref + "|." + i +": " + rowFilters.get(i).check(ref) + " ");
					ret = ret && rowFilters.get(i).check(ref);
				}
				//System.out.println(" --> " + ret);
				return ret;
            } // include()
        };
		rowSorter.setRowFilter(tableRowFilter);
	} // setFilter()

	/**
	 * Adds a new (JComponent) object to the panel.
	 * @note doesFiltering should be set to true before this method finishes!
	 *       Also note this method DOES NOT actually add any filter!
	 * @param argComponent
	 */
	public void addFilter(JComponent argComponent) {
		Dimension d = argComponent.getPreferredSize();
		int width = argComponent.getWidth();
		int height = (int)d.getHeight();
		// fix width and height if zeros
		if (width == 0)
			width = 50;
		if (height == 0)
			height = 20;
		// add given argComponent into the internal list of components
		filters.add(argComponent);
		// only if table has enough columns we can take the width of the column
		if (columnModel.getColumnCount() > filters.size()) {
			width = columnModel.getColumn(filters.size()).getWidth();
		}
		// update the size of the newly added component
		d.setSize(new Dimension(width, height));
		filters.lastElement().setPreferredSize(d);
		filters.lastElement().setSize(d);
		// set the position
		filters.lastElement().setLocation(lastPosX, lastPosY);
		// store the position for next addFilter() call
		lastPosX += filters.lastElement().getWidth();
		// also add it to the FilterPanel object (becomes visible)
		add(filters.lastElement());
		// re-align
		realign();
		// and finally, validate
		validate();
		//System.out.println("addFilter:" + dump());
		doesFiltering = true; // return back to true
	} // addFilter() method

	public void printLinks() {
		for (Map.Entry<TableColumn,Integer> entry : link.entrySet())  {
			TableColumn tc = entry.getKey();
			Integer cidx = entry.getValue();
		}
	} // printLinks() method

	/**
	 * By default, when you add a new column, "All customizations and adjustments
	 * are lost after the new column is added. For example, if you've installed
	 * a special renderer on one of the columns or if the user has moved a column,
	 * all these changes will be lost when the new column is added. In fact,
	 * if you remove a column (but did not remove the column data), the column
	 * will reappear after the new column is added." --- exampledepot.com
	 * This method adds a new column to table without reconstructing
     * all the other columns.
	 * @note table should have "auto-create columns from model" set to false!
	 */
    public void addColumn(JTable table, Object headerLabel, Object[] values) {
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        TableColumn col = new TableColumn(model.getColumnCount());
        // Ensure that auto-create is off
        if (table.getAutoCreateColumnsFromModel()) {
            throw new IllegalStateException();
        } // if
        col.setHeaderValue(headerLabel);
        table.addColumn(col);
        model.addColumn(headerLabel.toString(), values);
    } // addColumn() method


	/**
	 * Adds an "invisible" new filter (JLabel) object to the list. We need this
	 * so everything is properly aligned.
	 * @note It may be better to put a simple JComponent here - IMHO it would be
	 *       worth of trying...
	 * @param argComponent
	 */
	public void addFilter() {
		doesFiltering = false;
		addFilter(new JLabel());
	} // addFilter() method

	/**
	 * Set given argComponent to be a
	 * @param argIndex
	 * @param argComponent
	 */
	public void setFilter(int argIndex, JComponent argComponent) {
		// start from the last element in the filters list, and continue adding
		// dummy filters until we reach the argIndex.
		for (int i = filters.size(); i<argIndex; i++) {
			addFilter();
		}
		addFilter(argComponent);
		//System.out.println("setFilter:" + dump());
	} // setFilter()

	/**
	* Realigns all JComponents inside the FilterPanel according to the
	* changes in the column model listener.
	* @note This method expects columnModel class-member to be set before
	*       you call it!
	*       Also note that number of columns must never be greater than number
	*       of filters in the FilterPanel!
	*/
	void realign() {
		if (filters.isEmpty() || (columnModel.getColumnCount() == 0))
			return;
		int width = 0;
		int height = 0;
		int viewIndex = 0;
		int numOfFilters = filters.size();
		int numOfColumns = columnModel.getColumnCount();
		// note: it is impossible to get column that changed width becase
		// sometimes ALL columns are resized! It is better to go through all
		// TableColumns in this case and make appropriate adjustments.
		int posX = filters.firstElement().getX();
		int posY = filters.firstElement().getY();

		// iterate through all filters and resize them according to the size of
		// filteredTable columns
		for (int i = 0; i < numOfFilters; i++) {
			Dimension d = filters.elementAt(i).getSize();
			height = (int)d.getHeight(); // retain original heights of filters
			width = (int)d.getWidth();
			viewIndex = filteredTable.convertColumnIndexToView(i);
			if (viewIndex >= 0) {
				if (i < numOfColumns) {
					// if table has column with index i, resize the i-th filter
					width = columnModel.getColumn(viewIndex).getWidth();
					if (i == 0) {
						width -= 2; // move 2px to the left
					} // if
					d.setSize(new Dimension(width, height));
					filters.elementAt(i).setSize(d);
					filters.elementAt(i).setPreferredSize(d);
					if (!filters.elementAt(i).isVisible()) {
						filters.elementAt(i).setVisible(true);
					} // if
				} // if i < numOfColumns
			} else {
				width = 0;
				// hide this filter
				d.width = 0;
				filters.elementAt(i).setSize(d);
				filters.elementAt(i).setPreferredSize(d);
				filters.elementAt(i).setVisible(false);
			} // else
			filters.elementAt(i).setLocation(posX, posY);
			posX += width; // TODO: get gap between cells from the table, and/or from the FlowLayout of the panel
		} // for
		// store the last X coordinate - we will need it in case a new colum is added.
		lastPosX = posX;
		lastPosY = posY;
	} // realign() method

	/**
	 * Initialises all filter JComponent objects that should be visible in
	 * the FilterPanel.This method is supposed to be called only when the FilterPanel
	 * is initialised, to initialise components
	 * @note filteredTable object must be properly set before you call this method!
	 */
	public void initFilterComponents() {
        for (int i = 0; i < filteredTable.getColumnCount(); i++) {
            if (filters.size() < (i + 1)) {
				addFilter();
			} // if
        } // for
	} // initFilterComponents() method

	public AreenTableRowSorter getRowSorter() {
		return rowSorter;
	}

	public void setRowSorter(AreenTableRowSorter argRowSorter) {
		this.rowSorter = argRowSorter;
	}

    public void setTable(JTable argTable) {
        filteredTable = argTable;
		tableModel = (DefaultTableModel)argTable.getModel();
        columnModel = (DefaultTableColumnModel)argTable.getColumnModel();
		initFilterComponents();
		columnModel.addColumnModelListener(this);
		// make a new table row sorter object for the model of the currently given table.
		if (rowSorter == null) {
			rowSorter = new AreenTableRowSorter<DefaultTableModel>(tableModel);
			argTable.setRowSorter(rowSorter);
		} // if
		// make a new filter and set it for this sorter object
		setFilter();
		// set this row sorter for the given table

		// listen for changes in the filtered table and resize appropriately
        filteredTable.addComponentListener(new java.awt.event.ComponentAdapter() {
			@Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                JTable jt = (JTable)evt.getSource();
				setSize(jt.getWidth(), getHeight());
            } // componentResized() method
        });
	} // setTable() method

	public void columnAdded(TableColumnModelEvent e) {
		DefaultTableColumnModel dtcm = (DefaultTableColumnModel) e.getSource();
		int numOfColumns = dtcm.getColumnCount();
		if (filters.size() < numOfColumns) {
			addFilter(new JLabel());
		}
	} // columnAdded() method

	public void columnMarginChanged(ChangeEvent e) {
		columnModel = (DefaultTableColumnModel) e.getSource();
		realign();
	}

	public void columnMoved(TableColumnModelEvent e) {
		columnModel = (DefaultTableColumnModel) e.getSource();
		realign();
	}

	public void columnRemoved(TableColumnModelEvent e) {
		columnModel = (DefaultTableColumnModel) e.getSource();
		//int vidx = filteredTable.convertColumnIndexToView(e.getToIndex());
		int vidx = e.getFromIndex(); // view index
			
		int midx;
		
		if(vidx==filteredTable.getColumnCount())
			midx = filteredTable.getColumnCount();
		else
			midx = filteredTable.convertColumnIndexToModel(vidx); // model index
		
        // NOTE: looks like getFromIdex() returns MODEL index! ??? Check if that is true...
		// now let's find what rowFilter corresponds to the removed colum
		// (if any)
		for (BaseRowFilter filter: rowFilters) {
		
			if (filter.getColumnIndex() == vidx) { // we found it
				// now that we know what filter object correcponds to the
				// removed column, we can reset it so it does not confuse users.
				// (it still filters!)
				filter.reset(); // filter set to <all>
				JComponent filterWidget = filters.get(midx);
				setFilter(); // tell row sorter to update
			} // if
		} // foreach
		realign();
	}

	public void columnSelectionChanged(ListSelectionEvent e) {
		//System.out.println("Selection Changed");
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(153, 153, 153));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
    }// </editor-fold>//GEN-END:initComponents

	private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
		realign();
	}//GEN-LAST:event_formComponentResized

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

// $Id: FilterPanel.java 1011 2010-07-08 11:28:38Z dyki $