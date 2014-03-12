/**
 * Project: JLIB
 * Version: $Id$ 
 * License: (see below block, or the accompanied COPYING.txt file)
 **************************************************************************************** LICENSE BEGIN *****
 * Copyright (c) 2009-2013, JLIB AUTHORS (see the AUTHORS.txt file for the list of the individuals)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 *  1) Redistributions of source code must retain the above copyright notice, this list of conditions and the 
 *     following disclaimer.
 *  2) Redistributions in binary form must reproduce the above copyright notice, this list of conditions and 
 *     the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  3) Neither the names of the organisations involved in the JLIB project, nor the names of their 
 *     contributors to the JLIB project may be used to endorse or promote products derived from this software 
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED 
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR 
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 ****************************************************************************************** LICENSE END *****
 * 
 * This file is best viewed with 110 columns.
12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * 
 * In chronological order
 * Author(s):
 *   Dejan Lekic , http://dejan.lekic.org
 * Contributor(s):
 *   -
 */
package com.areen.jlib.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Dialog.ModalityType;
import javax.swing.border.BevelBorder;
import java.awt.Font;
import javax.swing.SwingConstants;

/**
 * This class is made to be used whenever there is a long-running process that has to block user's
 * interaction with the GUI while some worker is doing its job.
 * 
 * NOTE: The client code will be responsible for specifying where the dialog will appear, by setting location
 * relative to some Component.
 * 
 * @author Dejan
 */
public class ProgressUpdateDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel bodyTextLabel;
	private JLabel titleLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ProgressUpdateDialog dialog = new ProgressUpdateDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ProgressUpdateDialog() {
		setUndecorated(true);
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 340, 100);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.SOUTH);
			panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				JButton button = new JButton("Close");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
					}
				});
				button.setActionCommand("OK");
				panel.add(button);
			}
		}
		{
			JPanel topPanel = new JPanel();
			contentPanel.add(topPanel, BorderLayout.NORTH);
			{
				titleLabel = new JLabel("Title");
				titleLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
				topPanel.add(titleLabel);
			}
		}
		{
			bodyTextLabel = new JLabel("New label");
			bodyTextLabel.setHorizontalAlignment(SwingConstants.LEFT);
			bodyTextLabel.setVerticalAlignment(SwingConstants.TOP);
			contentPanel.add(bodyTextLabel, BorderLayout.CENTER);
		}
	}
	
	public void setBodyText(String argText) {
		bodyTextLabel.setText(argText);
	}
    
    public String getBodyText() {
        return bodyTextLabel.getText();
    }
	
    @Override
	public void setTitle(String argText) {
		super.setTitle(argText);
		titleLabel.setText(argText);
	}

} // ProgressUpdateDialog class

// $Id$
