/**
 * $Id$
 *
 * Copyright (c) 2009-2013 Areen Design Services Ltd
 * 23 Eyot Gardens; London; W6 9TR
 * http://www.areen.com
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of Areen Design Services Ltd ("Confidential 
 * Information").  You shall not disclose such Confidential Information and shall use it only in accordance 
 * with the terms of the license agreement you entered into with Areen Design Services Ltd.
 * 
 * This file is best viewed with 110 columns.
12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * 
 * Author(s) in chronological order:
 *   Mateusz Dykiert , http://dykiert.org
 * Contributor(s):
 *   -
 */


package com.areen.jlib.gui.accordion;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * Class for rotatable title. The title behaves differently depending on the orientation. If the title
 * is horizontal accordion it will collapse to small vertical strip. If we are dealing with vertical
 * accordion then collapsed and expanded states the title remains horizontal.
 * @author Matthew
 *
 */
public class RotatableTitle extends JPanel {

	// ====================================================================================================
	// ==== Variables =====================================================================================
	// ====================================================================================================

	// :::::: CONFIGURATION VARIABLES :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	int fontSize = 15;

	// :::::: PRIVATE/PROTECTED :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	private boolean horizontal; // if the row is horizontal 
	private boolean expanded;   // state of the title
	private String text;

	// ====================================================================================================
	// ==== Constructors ==================================================================================
	// ====================================================================================================

	public RotatableTitle(String argText, boolean argHorizontal) {
		this.horizontal = argHorizontal;
		this.text = argText;

		expanded = false;

		setSize(35, 35);
		setPreferredSize(getSize());
	} //RotatableTitle

	// ====================================================================================================
	// ==== Interface/Superclass Methods ==================================================================
	// ====================================================================================================

	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    
		g2d.setFont(new Font("Microsoft JhengHei", Font.BOLD, fontSize));
		FontMetrics metrics = g2d.getFontMetrics();

		//if horizontal and collapsed we need to show the title in 
		if (horizontal && !expanded) {
			g2d.rotate(- Math.PI / 2);

			// translate so that it is printed in the correct place
			g2d.translate(- metrics.stringWidth(text) - metrics.getFont().getSize() - 10, 0);
		} 

		String currentText = text;

		//add arrows
		if (horizontal) {
			if (expanded) {
				currentText += " \u25E3"; 
			} else {
				currentText += " \u25BC";
			} //if

		} else {
			if (expanded) {
				currentText += " \u25E4";
			} else {
				currentText += " \u25BC";
			}
		} //else

		g2d.drawString(currentText, 2, metrics.getFont().getSize() + 2);
	} // paint

	// ====================================================================================================
	// ==== Public Methods ================================================================================
	// ====================================================================================================

	public void rotate(boolean argExpanded) {
		expanded = argExpanded;

		repaint();
	} //rotate


	// ====================================================================================================
	// ==== Accessors =====================================================================================
	// ====================================================================================================

	/**
	 * @return the expanded
	 */
	public boolean isExpanded() {
		return expanded;
	}

	/**
	 * @param expanded the expanded to set
	 */
	public void setExpanded(boolean argExpanded) {
		this.expanded = argExpanded;
	}


	// ====================================================================================================
	// ==== Private/Protected/Package Methods =============================================================
	// ====================================================================================================
}