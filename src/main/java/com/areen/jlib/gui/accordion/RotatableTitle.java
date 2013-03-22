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

	private int fontSize;
	private boolean horizontal; // if the row is horizontal 
	
	// :::::: PRIVATE/PROTECTED :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	
	private boolean expanded;   // state of the title
	private String text;

	// ====================================================================================================
	// ==== Constructors ==================================================================================
	// ====================================================================================================
	
	public RotatableTitle(String argText, boolean argHorizontal, int argFontSize) {
		this.horizontal = argHorizontal;
		this.text = argText;
		this.fontSize = argFontSize;
		expanded = false;

		setSize(Accordion.getTitleSize(), Accordion.getTitleSize());
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
				currentText += " \u25B2"; 
			} else {
				currentText += " \u25BC";
			} //if

		} else {
			if (expanded) {
				currentText += " \u25B2";
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
	

	/**
	 * @return the fontSize
	 */
	public int getFontSize() {
		return fontSize;
	}

	/**
	 * @param fontSize the fontSize to set
	 */
	public void setFontSize(int argFontSize) {
		this.fontSize = argFontSize;
	}

	/**
	 * Get the title
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * Set the title
	 * @param text
	 */
	public void setText(String argText) {
		this.text = argText;
	}
	

	// ====================================================================================================
	// ==== Private/Protected/Package Methods =============================================================
	// ====================================================================================================
}