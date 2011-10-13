/**
 * $Id: AreenTablePanel.java 157 2011-10-12 12:47:16Z vipul $
 *
 * Copyright (c) 2009-2010 Areen Design Services Ltd
 * 23 Eyot Gardens; London; W6 9TR
 * http://www.areen.com
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of
 * Areen Design Services Ltd ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Areen Design Services Ltd.
 * 
 * This file is best viewed with 110 columns.
12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * 
 * Author(s) in chronological order:
 *   Dejan Lekic , http://dejan.lekic.org
 *   Mehjabeen Nujurally
 *   Vipul Kumar
 * Contributor(s):
 *   -
 */

package com.areen.jlib.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 *
 * @author dejan
 */
public class MenuButton extends JButton
            implements ChangeListener, ActionListener, PopupMenuListener, PropertyChangeListener 
{

    private JButton mainButton, dropDownButton;
    private JPopupMenu dropDownMenu;
   
    /**
     * Default Constructor that creates a blank button with a down facing arrow.
     */
    public MenuButton() {
        this("");
    }
    
    /**
     * Default Constructor that creates a blank button with the specified action and a down facing arrow.
     */
    public MenuButton(javax.swing.Action argAction) {
        this(new JButton(argAction), SwingConstants.SOUTH);
    }
    
    
    /**
     * Creates a button with the specified label and a down facing arrow.
     * @param argLabel String
     */
    public MenuButton(String argLabel) {
        this(new JButton(argLabel), SwingConstants.SOUTH);
    }
  
    /**
     * Creates a button with the specified text 
     * and a arrow in the specified direction.
     * @param text String
     * @param orientation int
     */
    public MenuButton(String text, int orientation) {
        this(new JButton(text), orientation);
    }

    /**
     * Passes in the button to use in the left hand side, with the specified 
     * orientation for the arrow on the right hand side.
     * @param mainButton JButton
     * @param orientation int
     */
    public MenuButton(JButton mainButton, int orientation) {
        super();
        this.mainButton = mainButton;
        this.mainButton.setIcon(mainButton.getIcon());

        this.dropDownButton = new BasicArrowButton(orientation);
        dropDownButton.addActionListener(this);

        this.setBorderPainted(false);
        this.dropDownButton.setBorderPainted(false);
        this.mainButton.setBorderPainted(false);

        this.setPreferredSize(new Dimension(34, 24));
        this.setMaximumSize(new Dimension(40, 24));
        this.setMinimumSize(new Dimension(34, 24));

        this.setLayout(new BorderLayout());
        this.setMargin(new Insets(-3, -3, -3, -3));

        this.add(mainButton, BorderLayout.CENTER);
        this.add(dropDownButton, BorderLayout.EAST);
        this.mainButton.addPropertyChangeListener("enabled", this);
    }

    @Override
    public void setIcon(Icon defaultIcon) {
        super.setIcon(defaultIcon);
        this.mainButton.setIcon(defaultIcon);
    }


    /**
     * Sets the popup menu to show when the arrow is clicked.
     * @param menu JPopupMenu
     */
    public void setMenu(JPopupMenu menu) {
        this.dropDownMenu = menu;
    }

    /**
     * returns the main (left hand side) button.
     * @return JButton
     */
    public JButton getMainButton() {
        return mainButton;
    }

    /**
     * gets the drop down button (with the arrow)
     * @return JButton
     */
    public JButton getDropDownButton() {
        return dropDownButton;
    }

    /**
     * gets the drop down menu
     * @return JPopupMenu
     */
    public JPopupMenu getMenu() {
        return dropDownMenu;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        dropDownButton.setEnabled(mainButton.isEnabled());
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == mainButton.getModel()) {
            if (dropDownMenu.isVisible() && !mainButton.getModel().isRollover()) {
                mainButton.getModel().setRollover(true);
                return;
            }
            dropDownButton.getModel().setRollover(mainButton.getModel().isRollover());
            dropDownButton.setSelected(mainButton.getModel().isArmed() && mainButton.getModel().isPressed());
        } else {
            if (dropDownMenu.isVisible() && !dropDownButton.getModel().isSelected()) {
                dropDownButton.getModel().setSelected(true);
                return;
            }
            mainButton.getModel().setRollover(dropDownButton.getModel().isRollover());
        }
    }

    /**
     * action listener for the arrow button- shows / hides the popup menu.
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        if (this.dropDownMenu == null) {
            return;
        }

        Point p = this.getLocationOnScreen();
        dropDownMenu.setLocation((int) p.getX(),
                (int) p.getY() + this.getHeight());
        dropDownMenu.show(mainButton, 0, mainButton.getHeight());

    }

    public void popupMenuCanceled(PopupMenuEvent e) {
        dropDownMenu.setVisible(false);
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

        mainButton.getModel().setRollover(true);
        dropDownButton.getModel().setSelected(true);
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        //popupVisible = false;

        mainButton.getModel().setRollover(false);
        dropDownButton.getModel().setSelected(false);
        ((JPopupMenu) e.getSource()).removePopupMenuListener(this); // act as good programmer :)
    }

    /**
     * adds a action listener to this button (actually to the left hand side 
     * button, and any left over surrounding space.  the arrow button will not
     * be affected.
     * @param al ActionListener
     */
    public void addActionListener(ActionListener al) {
        this.mainButton.addActionListener(al);
        this.addActionListener(al);
    }

    public static void test() {
        JFrame frame = new JFrame("Simple Split Button Test");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());

        JToolBar toolBar = new JToolBar("tb");
        JButton sbButton = new JButton("sb");
        sbButton.setBackground(Color.BLACK);
        sbButton.setContentAreaFilled(false);
        MenuButton sb = new MenuButton(sbButton, SwingConstants.SOUTH);

        toolBar.add(new JButton("test button"));
        toolBar.add(sb);


        p.add(new JLabel("DropDownButton test"), BorderLayout.CENTER);

        JPopupMenu testMenu = new JPopupMenu("test menu");
        testMenu.add(addMI("menuItem1"));
        testMenu.add(addMI("menuItem2"));
        sb.setMenu(testMenu);

        frame.getContentPane().add(toolBar, BorderLayout.NORTH);
        frame.getContentPane().add(p, BorderLayout.CENTER);

        frame.setSize(200, 100);
        frame.show();

    }

    private static JMenuItem addMI(String text) {
        JMenuItem mi = new JMenuItem(text);
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println(e.getActionCommand());
            }
        });

        return mi;
    }

    public static void main(String[] args) {
        test();
    }    
    
}

// $Id$
