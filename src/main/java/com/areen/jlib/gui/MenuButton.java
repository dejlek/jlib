/**
 * Project: jlib
 * Version: $Id$
 * License: SPL
 *
 * This file is best viewed with 110 columns.
 * 34567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
 *
 * Authors (in chronological order):
 *   Dejan Lekic , http://dejan.lekic.org
 *   Mehjabeen Nujurally
 *   Vipul Kumar
 * Contributors (in chronological order):
 *   -
 */

package com.areen.jlib.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
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
            implements ChangeListener, ActionListener, PopupMenuListener, PropertyChangeListener {

    private JButton mainButton, dropDownButton;
    private JPopupMenu dropDownMenu;

    /**
     * Default Constructor that creates a blank button with a down facing arrow.
     */
    public MenuButton() {
        this("");
    } // MenuButton() method

    /**
     * Default Constructor that creates a blank button with the specified action and a down facing arrow.
     * @param argAction 
     */
    public MenuButton(final javax.swing.Action argAction) {
        this(new JButton(argAction), SwingConstants.SOUTH);
    } // MenuButton() method


    /**
     * Creates a button with the specified label and a down facing arrow.
     * @param argLabel String
     */
    public MenuButton(final String argLabel) {
        this(new JButton(argLabel), SwingConstants.SOUTH);
    } // MenuButton() method

    /**
     * Creates a button with the specified text
     * and a arrow in the specified direction.
     * @param text String
     * @param orientation int
     */
    public MenuButton(final String text, final int orientation) {
        this(new JButton(text), orientation);
    } // MenuButton() method

    /**
     * Passes in the button to use in the left hand side, with the specified
     * orientation for the arrow on the right hand side.
     * @param argMainButton 
     * @param orientation int
     */
    public MenuButton(final JButton argMainButton, final int orientation) {
        super();
        this.mainButton = argMainButton;
        this.mainButton.setIcon(mainButton.getIcon());

        this.dropDownButton = new BasicArrowButton(orientation);
        dropDownButton.addActionListener(this);
        AbstractAction action = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MenuButton.this.actionPerformed(e);
            } // actionPerformed() method
        };
        dropDownButton.setAction(action);

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
    } // MenuButton() method

    @Override
    public void setIcon(final Icon defaultIcon) {
        super.setIcon(defaultIcon);
        this.mainButton.setIcon(defaultIcon);
    } // setIcon() method

    @Override
    public void setToolTipText(final String text) {
        this.mainButton.setToolTipText(text);
        this.dropDownButton.setToolTipText(text);
    } // setToolTipText() method

    /**
     * Sets the popup menu to show when the arrow is clicked.
     * @param menu JPopupMenu
     */
    public void setMenu(final JPopupMenu menu) {
        this.dropDownMenu = menu;
    } // setMenu() method

    /**
     * gets the drop down menu
     * @return JPopupMenu
     */
    public JPopupMenu getMenu() {
        return dropDownMenu;
    } // getMenu() method

    /**
     * returns the main (left hand side) button.
     * @return JButton
     */
    public JButton getMainButton() {
        return mainButton;
    } // getMainButton() method

    /**
     * gets the drop down button (with the arrow)
     * @return JButton
     */
    public JButton getDropDownButton() {
        return dropDownButton;
    } // getDropDownButton() method


    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        dropDownButton.setEnabled(mainButton.isEnabled());
    } // propertyChange() method

    @Override
    public void stateChanged(final ChangeEvent e) {
        if (e.getSource() == mainButton.getModel()) {
            if (dropDownMenu.isVisible() && !mainButton.getModel().isRollover()) {
                mainButton.getModel().setRollover(true);
                return;
            } // if
            dropDownButton.getModel().setRollover(mainButton.getModel().isRollover());
            dropDownButton.setSelected(mainButton.getModel().isArmed() && mainButton.getModel().isPressed());
        } else {
            if (dropDownMenu.isVisible() && !dropDownButton.getModel().isSelected()) {
                dropDownButton.getModel().setSelected(true);
                return;
            } // if
            mainButton.getModel().setRollover(dropDownButton.getModel().isRollover());
        } // else
    } // stateChanged() method

    /**
     * action listener for the arrow button- shows / hides the popup menu.
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (this.dropDownMenu == null) {
            return;
        } // if

        Point p = this.getLocationOnScreen();
        dropDownMenu.setLocation((int) p.getX(),
                (int) p.getY() + this.getHeight());
        dropDownMenu.show(mainButton, 0, mainButton.getHeight());

    } // actionPerformed() method

    @Override
    public void popupMenuCanceled(final PopupMenuEvent e) {
        dropDownMenu.setVisible(false);
    } // popupMenuCanceled() method

    @Override
    public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {

        mainButton.getModel().setRollover(true);
        dropDownButton.getModel().setSelected(true);
    } // popupMenuWillBecomeVisible() method

    @Override
    public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
        //popupVisible = false;

        mainButton.getModel().setRollover(false);
        dropDownButton.getModel().setSelected(false);
        ((JPopupMenu) e.getSource()).removePopupMenuListener(this); // act as good programmer :)
    } // popupMenuWillBecomeInvisible() method

    /**
     * adds a action listener to this button (actually to the left hand side
     * button, and any left over surrounding space.  the arrow button will not
     * be affected.
     * @param al ActionListener
     */
    @Override
    public void addActionListener(final ActionListener al) {
        this.mainButton.addActionListener(al);
        //this.addActionListener(al);
    } // addActionListener() method

    /**
     * 
     */
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

    } // test() method

    private static JMenuItem addMI(final String text) {
        JMenuItem mi = new JMenuItem(text);
        mi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                System.out.println(e.getActionCommand());
            } // actionPerformed() method
        });

        return mi;
    } // addMI() method

    /**
     * 
     * @param args
     */
    public static void main(final String[] args) {
        test();
    } // main() method

} // MenuButton class

// $Id$
