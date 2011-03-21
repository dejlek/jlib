/**
 * $Id: AreenTableCellHeaderRenderer.java 809 2010-03-16 16:14:13Z dejan $
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

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultRowSorter;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class AreenTableCellHeaderRenderer extends DefaultTableCellRenderer {
	
	/**
	 * Default constructor.
	 */
	public AreenTableCellHeaderRenderer() {
        super();
		setHorizontalAlignment(0);
		setHorizontalTextPosition(10);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		JTableHeader tableHeader = table.getTableHeader();
		Color fg = null;
		Color bg = null;
		Border border = null;
		Icon icon = null;

		if (hasFocus) {
			fg = UIManager.getColor("TableHeader.focusCellForeground");
			bg = UIManager.getColor("TableHeader.focusCellBackground");
			border = UIManager.getBorder("TableHeader.focusCellBorder");
		}

		if (fg == null) {
			fg = tableHeader.getForeground();
		}
		if (bg == null) {
			bg = tableHeader.getBackground();
		}
		if (border == null) {
			border = UIManager.getBorder("TableHeader.cellBorder");
		}

		/*
		if (!tableHeader.isPaintingForPrint() && (table.getRowSorter() != null)) {
			icon = getSortIcon(table, table.convertColumnIndexToModel(column));
		}
		 *
		 */

		DefaultRowSorter drs = (DefaultRowSorter)table.getRowSorter();
		int numKeys = drs.getMaxSortKeys();

		boolean sorted = false;
		SortOrder sortOrder = SortOrder.UNSORTED;
		int cnt = 0;
		// iterate through the list of all sort keys, and check if some of them
		// refers to the column this component is responsible for...
		// if we find it, we add a unicode char that indicates the sort order.
		//System.out.print("(atchr cid " + column + " num_keys " + numKeys + " ");
		here:
		for (RowSorter.SortKey sortKey: table.getRowSorter().getSortKeys()) {
			++cnt;
			if (sortKey.getColumn() == table.convertColumnIndexToModel(column)) {
				if (cnt <= numKeys) {
					//System.out.print(" key_id" + " " + (cnt-1) + " #key_cid " + sortKey.getColumn() + " #column " + column + " ");
					sorted = true;
					sortOrder = sortKey.getSortOrder();
					break here;
				}
			} // if
			//System.out.print(" key_id" + " " + (cnt-1) + " key_cid " + sortKey.getColumn());
		} // foreach
		//System.out.println(")");
		String title = value != null && value != "" ? value.toString() : " ";
		if (sorted) {
			if (sortOrder == SortOrder.ASCENDING) {
				title += " △"; // ▲ △ -- unfortunately some fonts do not contain these glyphs...
				//setIcon(new javax.swing.ImageIcon(getClass().getResource("/areen/tim/icons/arrow_up.png")));
			}
			if (sortOrder == SortOrder.DESCENDING) {
				title += " ▽"; // ▼ ▽ -- unfortunately some fonts do not contain these glyphs...
				//setIcon(new javax.swing.ImageIcon(getClass().getResource("/areen/tim/icons/arrow_down.png")));
			} // else
		} else {
			setIcon(null);
		}

		setFont(tableHeader.getFont());
		setText(title);
		setBorder(border);
		return this;
	} // getTableCellRendererComponent()

} // AreenTableCellHeaderRenderer class

// $Id
