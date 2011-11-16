/**
 * $Id: InputFormPanel.java 1104 2011-11-07 11:03:42Z mehjabeen $
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
 * This is the main class for the "apc-client" application. 
 * Execute apc-client like this: java -jar apc-client.jar -Dareen.developer=dejan
 * You can ommit the parameter if you do not want to debug the application.
 * 
 * Author(s) in chronological order:
 *   Mehjabeen Nujurally
 * Contributor(s):
 *   -
 */

package com.areen.jlib.gui.form.ifp;

import com.areen.jlib.model.SimpleObject;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;

/**
 *
 * @author mehjabeen
 */
public class InputFormPanel extends javax.swing.JPanel {

    int currentRequisition;
    float titleFontSize = 20;
    Font defaultFont = new Font("Helvetica", Font.PLAIN, 12);
    LinkedHashMap<String, Object> labels = new LinkedHashMap();
    int formColumns = 2;
    InputFormModel formModel;
    InputFormContent formContent;
    
    /** Creates new form RequisitionCreationDialog */
    public InputFormPanel() {
        initComponents();
        Font titleFont = defaultFont.deriveFont(titleFontSize);
        titleLabel.setFont(titleFont);
    }// InputFormPanel() method
    
    /** Creates new form RequisitionCreationDialog */
    public InputFormPanel(int argRequisitionId) {
        this();
        currentRequisition = argRequisitionId;
    }// InputFormPanel() method
    
    
    public InputFormPanel(InputFormModel argModel) {
        initComponents();
        formModel = argModel;
        Font titleFont = defaultFont.deriveFont(titleFontSize);
        titleLabel.setFont(titleFont);
        titleLabel.setText(formModel.title);
        for(int i=0; i<formModel.model.getNumberOfFields() ; i++){
            addFormField(formModel.model.getTitles()[i], null);
        }// for
        for(int j=0; j<formModel.actions.size(); j++){
            addButton(new JButton((Action)formModel.actions.get(j)));
        }// for
        configureResizing();
    }// InputFormPanel() method
    
     public InputFormPanel(InputFormModel argModel, InputFormContent argFormContentPanel) {
        initComponents();
        Font titleFont = defaultFont.deriveFont(titleFontSize);
        titleLabel.setFont(titleFont);
        titleLabel.setText(argModel.title);
        formContentPanel.removeAll();
        formContentPanel.setLayout(new BorderLayout());
        formContentPanel.add(argFormContentPanel, BorderLayout.CENTER);
        for(int j=0; j<argModel.actions.size(); j++){
            addButton(new JButton((Action)argModel.actions.get(j)));
        }// for
    }// InputFormPanel() method
    
    public void setTitle(String argTitle){
        titleLabel.setText(argTitle);
    }// setTitle() method

    public void addButton(JButton argButton){
        buttonContainerPanel.add(argButton);
    }// addButton() method
    
    public void addFormField(String argFieldName, JComponent argFieldComponent){
        labels.put(argFieldName, argFieldComponent);
    }// addFormField() method
    
    public void configureResizing(){
        formContentPanel.removeAll();
        formContentPanel.setLayout(new SpringLayout());
        Iterator it = labels.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pairs = (Map.Entry)it.next();
            JLabel fieldLabel = new JLabel(pairs.getKey().toString(), JLabel.TRAILING);
            formContentPanel.add(fieldLabel);
            if(pairs.getValue()==null){
                JTextField fieldComponent = new JTextField();
                fieldLabel.setLabelFor(fieldComponent);
                formContentPanel.add(fieldComponent);
            }// if
            else if(pairs.getValue() instanceof JComponent[]){
                JPanel componentsPanel = new JPanel();
                componentsPanel.setLayout(new BoxLayout(componentsPanel, BoxLayout.X_AXIS));
                JComponent[] components = (JComponent[])pairs.getValue();
                for (int i=0; i<components.length; i++){
                    componentsPanel.add(components[i]);
                }// for
                formContentPanel.add(componentsPanel);
            }// if
            else{
                formContentPanel.add((JComponent)pairs.getValue());
            }// else
        }// while 
        makeCompactGrid(formContentPanel, labels.size(), formColumns, 6, 6, 6, 6);
        formContentPanel.validate();
        formContentPanel.repaint();
        this.validate();
        this.repaint();
    }// configureResizing() method

    public SimpleObject getModel() {
        if(formModel!=null){
            return formModel.model;
        }// if
        return null;
    }// getModel() method

    public void setModel(SimpleObject argModel) {
        if(formModel!=null){
            formModel.model = argModel;
        }// if
        if(formContent!=null){
            formContent.setModel(argModel);
        }// if
    }// setModel() method
    
    @Override
    public void setFont(Font argFont){
        defaultFont = argFont;
        for(int i=0; i<getComponentCount(); i++){
            if(getComponent(i) instanceof JLabel){
                ((JLabel)getComponent(i)).setFont(defaultFont);
                ((JLabel)getComponent(i)).validate();
                ((JLabel)getComponent(i)).repaint();
            }// if
        }// for
        this.validate();
        this.repaint();
    }// setFont() method
    
    public void setTitleSize(float argSize){
        titleFontSize = argSize;
        Font titleFont = defaultFont.deriveFont(titleFontSize);
        titleLabel.setFont(titleFont);
        titleLabel.validate();
        titleLabel.repaint();
    }// setTitleSize() method 
    
    public void setHiddenFields(int ... argModelIndices){
        for(int i=argModelIndices.length-1; i>=0; i--){
            //DEBUG: System.out.println(argModelIndices[i] + " " + labels.keySet().toArray()[(argModelIndices[i])]);
            //DEBUG: System.out.println(labels.keySet().toArray().length + " " + labels.values().toArray().length);
            labels.keySet().remove(labels.keySet().toArray()[argModelIndices[i]].toString());
        }// for
        configureResizing();
    }// setHiddenFields() method
    
    public void setFieldWithInputComponent(int argModelIndex, JComponent argComponent){
        int counter=0;
        Iterator it = labels.entrySet().iterator();
        done: while(it.hasNext()){
            Map.Entry pairs = (Map.Entry)it.next();
            if (counter == argModelIndex){
                pairs.setValue(argComponent);
                break done;  
            }// if
            counter++;
        }// while
        configureResizing();
    }// setFieldWithInputComponent() method
    
    public void setFieldWithInputComponentAndLabel(int argModelIndex, JComponent argComponent, JLabel argLabel){
        int counter=0;
        Iterator it = labels.entrySet().iterator();
        done: while(it.hasNext()){
            Map.Entry pairs = (Map.Entry)it.next();
            if (counter == argModelIndex){
                pairs.setValue(new JComponent[]{argComponent, argLabel});
                break done;  
            }// if
            counter++;
        }// while
        configureResizing();
    }// setFieldWithInputComponent() method
    
    public void setFieldAtFormIndex(int argModelIndex, int argFormIndex){
        int counter=0;
        Iterator it = labels.entrySet().iterator();
        LinkedHashMap rearrangedMap = new LinkedHashMap();
        String fieldLabel = "";
        Object fieldComponent = null;
        done: while(it.hasNext()){
            Map.Entry pairs = (Map.Entry)it.next();
            if (counter == argModelIndex){
                Map.Entry modelIndexPair = pairs;
                fieldLabel = modelIndexPair.getKey().toString();
                //DEBUG: System.out.println(" " + counter + " " + fieldLabel);
                if(modelIndexPair.getValue() instanceof JComponent){
                    fieldComponent = (JComponent)modelIndexPair.getValue();
                }// if
                else if(modelIndexPair.getValue() instanceof JComponent[]){
                    fieldComponent = (JComponent[])modelIndexPair.getValue();
                }// else if
            }// if
            else{
                rearrangedMap.put(pairs.getKey(), pairs.getValue());
            }// else
            if(counter == argFormIndex){
                if(fieldLabel.equals("")){
                    fieldLabel = labels.keySet().toArray()[argModelIndex].toString();
                    if(labels.values().toArray()[argModelIndex] instanceof JComponent){
                        fieldComponent = (JComponent)labels.values().toArray()[argModelIndex];
                    }// if
                    else if(labels.values().toArray()[argModelIndex] instanceof JComponent[]){
                        fieldComponent = (JComponent[])labels.values().toArray()[argModelIndex];
                    }// else if
                }// if
                //DEBUG: System.out.println("Form " + counter + " " + fieldLabel);
                rearrangedMap.put(fieldLabel, fieldComponent);
                //rearrangedMap.put(pairs.getKey(), pairs.getValue());
            }// if
            counter++;
        }// while
        
        labels = rearrangedMap;
        configureResizing();
    }// setFieldWithInputComponent() method
    
    public static void makeCompactGrid(Container parent, int rows, int cols, 
                                       int initialX, int initialY, 
                                       int xPad, int yPad) {
        SpringLayout layout;

        try {
            layout = (SpringLayout)parent.getLayout();
        }// try 
        catch (ClassCastException exc) {
            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
            return;
        }// catch

        //Align all cells in each column and make them the same width.
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width,
                                   getConstraintsForCell(r, c, parent, cols).
                                   getWidth());
            }// for
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints =getConstraintsForCell(r, c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }// for
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }// for

        //Align all cells in each row and make them the same height.
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height,
                                    getConstraintsForCell(r, c, parent, cols).
                                    getHeight());
            }// for

            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints =getConstraintsForCell(r, c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }// for
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }// for

        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    }// makeCompactGrid() method
    
    /* Used by makeCompactGrid. */
    private static SpringLayout.Constraints getConstraintsForCell(
                                                int row, int col,
                                                Container parent,
                                                int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }// getConstraintsForCell() method
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        titlePanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        titleSeparator = new javax.swing.JSeparator();
        buttonPanel = new javax.swing.JPanel();
        buttonSeparator = new javax.swing.JSeparator();
        buttonContainerPanel = new javax.swing.JPanel();
        formContentPanel = new javax.swing.JPanel();
        formFieldsPanel = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setLayout(new java.awt.BorderLayout());

        titlePanel.setLayout(new java.awt.BorderLayout());

        titleLabel.setText("Title");
        titlePanel.add(titleLabel, java.awt.BorderLayout.CENTER);
        titlePanel.add(titleSeparator, java.awt.BorderLayout.PAGE_END);

        add(titlePanel, java.awt.BorderLayout.NORTH);

        buttonPanel.setMinimumSize(new java.awt.Dimension(26, 26));
        buttonPanel.setPreferredSize(new java.awt.Dimension(26, 35));
        buttonPanel.setLayout(new java.awt.BorderLayout());
        buttonPanel.add(buttonSeparator, java.awt.BorderLayout.PAGE_START);

        buttonContainerPanel.setMaximumSize(new java.awt.Dimension(10, 10));
        buttonContainerPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));
        buttonPanel.add(buttonContainerPanel, java.awt.BorderLayout.CENTER);

        add(buttonPanel, java.awt.BorderLayout.SOUTH);

        formContentPanel.setLayout(new java.awt.GridBagLayout());

        formFieldsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        formFieldsPanel.setAutoscrolls(true);
        formFieldsPanel.setLayout(new javax.swing.BoxLayout(formFieldsPanel, javax.swing.BoxLayout.Y_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        formContentPanel.add(formFieldsPanel, gridBagConstraints);

        add(formContentPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonContainerPanel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JSeparator buttonSeparator;
    private javax.swing.JPanel formContentPanel;
    private javax.swing.JPanel formFieldsPanel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JSeparator titleSeparator;
    // End of variables declaration//GEN-END:variables
}// InputFormPanel class
// $Id: InputFormPanel.java 1104 2011-11-07 11:03:42Z mehjabeen $
