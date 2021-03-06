/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.gui.attrib;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 *
 * @author dejan
 */
public class AttributesEditableView 
        extends JComponent
        implements PropertyChangeListener, MouseListener {
    
    /* Class implementation comment (if any)
     * 
     */
    
    // ====================================================================================================
    // ==== Variables =====================================================================================
    // ====================================================================================================
    
    // :::::: CONFIGURATION VARIABLES :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    // :::::: PUBLIC ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    // :::::: PRIVATE/PROTECTED :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    protected AttributesModel model;
    protected JLabel[] labels;
    protected boolean editorComponent;
    protected boolean inTable;
    protected JLabel titleLabel;
    
    // ====================================================================================================
    // ==== Constructors ==================================================================================
    // ====================================================================================================
    
    public AttributesEditableView() {
        super();
        FlowLayout fl = new FlowLayout(FlowLayout.LEFT);
        fl.setVgap(0);
        setLayout(fl);
        editorComponent = false;
        inTable = false;
        titleLabel = new JLabel("Hello");
        titleLabel.setForeground(Color.LIGHT_GRAY);
    }

    public AttributesEditableView(AttributesModel argModel) {
        this();
        setModel(argModel);
    }
    
    // ====================================================================================================
    // ==== Interface/Superclass Methods ==================================================================
    // ====================================================================================================
    
    // :::: <Superclass> method overrides :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    // :::: JComponent method overrides :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    /**
     * I had to do this because it is a common problem with JComponent subclasses.
     * More about it: http://docs.oracle.com/javase/tutorial/uiswing/painting/problems.html
     * 
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(getForeground());
    }
    
    // :::: PropertyChangeListener mthod implementations ::::::::::::::::::::::::::::::::::::::::::::::::::
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("attributes")) {
            String str = (String) evt.getNewValue();
            
            updateView();
        } else if (evt.getPropertyName().equals("descriptionsChanged")) {
            String[] descriptions = model.getDescriptions();
            for (int i = 0; i < model.getNumberOfAttributes(); i++) {
                labels[i].setToolTipText(descriptions[i]);
            }
        } // else if
    }
    
    // :::: <Interface> method implementations ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Override
    public void mouseClicked(MouseEvent e) {
        // labels have names in form of "attributelbl-3", we need to extract the ID from the name
        String num = e.getComponent().getName().split("-")[1];
        int idx = Integer.parseInt(num);

        if (editorComponent) {
            model.setSelectedAttributeIndex(idx);
        }
        
        model.next(idx);
    } // mouseClicked() method

    @Override
    public void mousePressed(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // labels have names in form of "attributelbl-3", we need to extract the ID from the name
        String num = e.getComponent().getName().split("-")[1];
        int idx = Integer.parseInt(num);
        model.setSelectedAttributeIndex(idx);
        updateView();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // labels have names in form of "attributelbl-3", we need to extract the ID from the name
        String num = e.getComponent().getName().split("-")[1];
        int idx = Integer.parseInt(num);
        
        if (!editorComponent) {
            model.setSelectedAttributeIndex(-1);
        }
        
        updateView();
    }
    
    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================

    /**
     * Use this method to forcefully highlight specific attribute.
     * @param argIndex 
     */
    public final void highlight(int argIndex) {
        for (int i = 0; i < model.getNumberOfAttributes(); i++) {
            if (i == argIndex) {
                labels[i].setOpaque(true);
                labels[i].setBackground(model.getSelectedBackgroundColor());
            } else {
                labels[i].setOpaque(false);
                labels[i].setBackground(model.getBackgroundColor());
            }
        } // for
    } // highlight() method
    
    /**
     * Extremely important method. Here we set the model, and generate JLabel objects for the attributes.
     * We also set the values, and initialise the listeners.
     * @param argModel 
     */
    public final void setModel(AttributesModel argModel) {
        if (model != null) {
            model.removePropertyChangeListener(this);
        }
        
        labels = null;
        removeAll();
        labels = new JLabel[argModel.getNumberOfAttributes()];
        
        int w = 0;
        int h = 0;
        for (int i = 0; i < argModel.getNumberOfAttributes(); i++) {
            String txt = "" + argModel.getValue(i);
            
            JLabel lbl = new JLabel(txt);
            labels[i] = lbl;
            lbl.setName("attributelbl-" + i); // very important, because we will use the name to get the idx
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            if (isInTable()) {
                lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
            }
            lbl.setToolTipText(argModel.getDescription(i));
            lbl.setBorder(new LineBorder(Color.GRAY, 1));
            Font font = lbl.getFont();
            Font newFont = font.deriveFont(11f);
            lbl.setFont(newFont);
            
            int nh = lbl.getPreferredSize().height;
            int nw = nh; // make the width to be equal to the height
            lbl.setPreferredSize(new Dimension(nw, nh));
            lbl.addMouseListener(this);

            add(lbl);
        } // for
        
        int midx = argModel.getSelectedAttributeIndex();
        if (editorComponent) {
            String[] titles = argModel.getTitles();
            if ((titles != null) & (midx >= 0)) {
                titleLabel.setText(titles[midx]);
            } else {
                titleLabel.setText("");
            }
            add(titleLabel);
        }
        
        model = argModel;
        updateView();
        model.addPropertyChangeListener(this);
    } // setModel() method
    
    // ====================================================================================================
    // ==== Accessors =====================================================================================
    // ====================================================================================================
    
    public final AttributesModel getModel() {
        return model;
    }

    public boolean isInTable() {
        return inTable;
    }

    public void setInTable(boolean argInTable) {
        inTable = argInTable;
    }
    
    // ====================================================================================================
    // ==== Private/Protected/Package Methods =============================================================
    // ====================================================================================================

    /**
     * Updates the view
     */
    protected void updateView() {
        String attrs = model.getAttributes();
        int midx = model.getSelectedAttributeIndex();
        
        for (int i = 0; i < model.getNumberOfAttributes(); i++) {
            labels[i].setText("" + model.getValue(i));
            if (midx == i) {
                labels[i].setOpaque(true);
                labels[i].setBackground(model.getSelectedBackgroundColor());
            } else {
                labels[i].setOpaque(false);
                labels[i].setBackground(model.getBackgroundColor());
            }
        } // for
        
        if (editorComponent) {
            String[] titles = model.getTitles();
            if ((titles != null) & (midx >= 0)) {
                titleLabel.setText(titles[midx]);
            } else {
                titleLabel.setText("");
            }
        } // if
    } // updateView() method
    
} // AttributesEditableView class
