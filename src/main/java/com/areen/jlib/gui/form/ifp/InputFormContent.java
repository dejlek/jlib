/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.gui.form.ifp;

import com.areen.jlib.model.SimpleObject;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;
import org.jdesktop.swingx.JXDatePicker;

/**
 * Every class which has an underlying VO (transfer object) should implement this
 * interface. SimpleObject makes sure some VO-related methods are provided.
 * You can say that SimpleObject is a wrapper for its own transfer object (VO).
 * SimpleObject types are most often going to be used as simple models or as a
 * way to obtain/use underlying transfer object as a model...
 *
 * @author dejan
 */
public class InputFormContent<T extends SimpleObject> extends JPanel {
    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    LinkedHashMap<String, Object> globalLabels = new LinkedHashMap();
    HashMap<Integer, Object> specialComponentField = new HashMap();
    HashMap<Integer, Boolean> notLabeledField = new HashMap();
    HashMap<Integer, String> labelForField = new HashMap();
    PropertyChangeSupport changes;
    T model;
    public InputFormContent(final T argSo) { 
        model = argSo; 
        if (changes == null) {
            changes = new PropertyChangeSupport(new PropertyChangeListener() {

                @Override
                public void propertyChange(final PropertyChangeEvent evt) {
                    reportChange(evt);
                } // propertyChange() method
            });
        } // if

        changes.addPropertyChangeListener("VALUE_CHANGE", new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                reportChange(evt);
            } // propertyChange() method
        });
    }
    public T getModel() {
        return model;
    }
    public void setModel(final T argSo) { model = argSo; }
    
    public void setComponentForModelIndex(int argModelIndex, Object argComponent) { 
        specialComponentField.put(argModelIndex, argComponent);
    }
    
    public void setUnLabeledForModelIndex(int argModelIndex, boolean argUnLabeled) { 
        notLabeledField.put(argModelIndex, argUnLabeled);
    }
    
    public void setLabelForModelIndex(int argModelIndex, String argLabel) { 
        labelForField.put(argModelIndex, argLabel);
    }
    
    public JPanel createSubGroupDetailsPanel(int[] argModelFields, int argFormColumns, int argFormAlignment) {
        int formColumns = argFormColumns;
        LinkedHashMap<String, Object> labels = new LinkedHashMap();
        String curLabel = "";
        for (int i = 0; i < argModelFields.length; i++) {
            curLabel = getModel().getTitles()[argModelFields[i]];
            System.out.println("TYPE: " + getModel().getFieldClass(argModelFields[i]));
            if (getModel().getFieldClass(argModelFields[i]).toString().indexOf("String") >= 0
                    || getModel().getFieldClass(argModelFields[i]).toString().indexOf("Integer") >= 0
                    || getModel().getFieldClass(argModelFields[i]).toString().indexOf("Float") >= 0
                    || getModel().getFieldClass(argModelFields[i]).toString().indexOf("Boolean") >= 0) {
                if (!specialComponentField.containsKey(argModelFields[i])) {
                    labels.put(curLabel, null);
                } else {
                    labels.put(curLabel, specialComponentField.get(argModelFields[i]));
                }
            } else if (getModel().getFieldClass(argModelFields[i]).toString().indexOf("Date") >= 0) {
                if (!specialComponentField.containsKey(argModelFields[i])) {
                    final JXDatePicker datePicker = new JXDatePicker();
                    final int n = argModelFields[i];
                    datePicker.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            changes.firePropertyChange("VALUE_CHANGE", getModel().getTitles()[n] 
                                    + " : ", getModel().getTitles()[n] + " : " + datePicker.getDate());
                        }
                    });

                    class DatePicker<datePicker> extends InputFormFieldEditor {

                        @Override
                        public Object getValue() {
                            return datePicker.getDate();
                        }

                        @Override
                        public JComponent getComponent() {
                            return datePicker;
                        }
                    };

                    labels.put(curLabel, new DatePicker());
                } else {
                    labels.put(curLabel, specialComponentField.get(argModelFields[i]));
                }
            } // else if
        } // for 
        JPanel groupFormContentPanel = new JPanel();
        groupFormContentPanel.setLayout(new SpringLayout());
        Iterator it = labels.entrySet().iterator();
        int counter = 0;
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            final JLabel fieldLabel = new JLabel(pairs.getKey().toString(), JLabel.TRAILING);
            String[] modelFieldTitles = getModel().getTitles();
            int fieldIndex = 0;
            for (int i = 0; i < modelFieldTitles.length; i++) {
                if (modelFieldTitles[i].equalsIgnoreCase(pairs.getKey().toString())) {
                    fieldIndex = i;
                    break;
                } // if
            } // for
            if (notLabeledField.containsKey(fieldIndex)) {
                System.out.println("Adding empty label");
                groupFormContentPanel.add(new JLabel(""));
            } else if (labelForField.containsKey(fieldIndex)) {
                fieldLabel.setText(labelForField.get(fieldIndex));
                groupFormContentPanel.add(fieldLabel);
            } else {
                groupFormContentPanel.add(fieldLabel);
            } // else

            System.out.println("Field: " + pairs.getKey().toString() + " Form component: " 
                    + pairs.getValue());
            
            if (pairs.getValue() == null) {
                final JTextField fieldComponent = new JTextField();
                System.out.println(getModel().get(argModelFields[counter]).toString());
                fieldComponent.setText(getModel().get(argModelFields[counter]).toString());
                fieldComponent.addKeyListener(new KeyListener() {

                    @Override
                    public void keyTyped(final KeyEvent e) {
                    } // keyTyped() method

                    @Override
                    public void keyPressed(final KeyEvent e) {
                    } // keyPressed() method

                    @Override
                    public void keyReleased(final KeyEvent e) {
                        changes.firePropertyChange("VALUE_CHANGE", fieldLabel.getText() + " : ",
                                fieldLabel.getText() + " : " + fieldComponent.getText());
                    } // keyReleased() method
                });
                fieldLabel.setLabelFor(fieldComponent);

                class FormTextField<fieldComponent> extends InputFormFieldEditor {

                    @Override
                    public Object getValue() {
                        return fieldComponent.getText();
                    }

                    @Override
                    public JComponent getComponent() {
                        return fieldComponent;
                    }
                };

                pairs.setValue(new FormTextField());
                groupFormContentPanel.add(fieldComponent);
            } else if (pairs.getValue() instanceof Object[]) {
                JPanel componentsPanel = new JPanel();
                componentsPanel.setLayout(new BoxLayout(componentsPanel, BoxLayout.X_AXIS));
                final Object[] components = (Object[]) pairs.getValue();
                for (int i = 0; i < components.length; i++) {
                    componentsPanel.add((JComponent) components[i]);
                } // for
                groupFormContentPanel.add(componentsPanel);
            } else if (pairs.getValue() instanceof InputFormFieldEditor) {
                final Map.Entry fieldPair = pairs;
                String tmp = getModel().get(argModelFields[counter]).toString();
                ((InputFormFieldEditor) pairs.getValue()).setValue(tmp);
                groupFormContentPanel.add(((InputFormFieldEditor) pairs.getValue()).getComponent());
            } else {
                groupFormContentPanel.add((JComponent) pairs.getValue());
            } // else
            counter++;
        } // while
        if (argFormAlignment == VERTICAL) {
            makeCompactGrid(groupFormContentPanel, labels.size(), formColumns, 6, 6, 6, 6);
        } else if (argFormAlignment == HORIZONTAL && formColumns <= 1) {
            makeCompactGrid(groupFormContentPanel, 1, labels.size() * 2, 6, 6, 6, 6);
        }
        groupFormContentPanel.validate();
        groupFormContentPanel.repaint();
        System.out.println("SIZE: " + labels.size());
        globalLabels.putAll(labels);
        return groupFormContentPanel;
    } // createSubGroupDetailsPanel() method

    public void setPropertyChangeSupport(PropertyChangeSupport argChanges) {
        changes = argChanges;
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return changes;
    }
    
    public static void makeCompactGrid(final Container parent, final int rows, final int cols,
            final int initialX, final int initialY,
            final int xPad, final int yPad) {
        SpringLayout layout;

        try {
            layout = (SpringLayout) parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
            return;
        } // catch

        //Align all cells in each column and make them the same width.
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width,
                        getConstraintsForCell(r, c, parent, cols).
                        getWidth());
            } // for
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints = getConstraintsForCell(r, c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            } // for
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        } // for

        //Align all cells in each row and make them the same height.
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height,
                        getConstraintsForCell(r, c, parent, cols).
                        getHeight());
            } // for

            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints = getConstraintsForCell(r, c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            } // for
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        } // for

        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    } // makeCompactGrid() method

    /* Used by makeCompactGrid. */
    private static SpringLayout.Constraints getConstraintsForCell(
            final int row, final int col,
            final Container parent,
            final int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    } // getConstraintsForCell() method

    public void reportChange(final PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        //DEBUG:
        System.out.println("CHANGE: " + propertyName + " " + evt.getNewValue());
        if ("VALUE_CHANGE".equals(propertyName)) {
            String[] value = evt.getNewValue().toString().split(":");
            String[] modelTitles = model.getTitles();
            for (int i = 0; i < modelTitles.length; i++) {
                //DEBUG: System.out.println("FIELD: "+value[0] + " " + modelTitles[i]);
                if (value[0].trim().equalsIgnoreCase(modelTitles[i])) {
                    model.set(i, value[1].trim());
                    //DEBUG: System.out.println(formModel.model.toString());
                    break;
                } // if
            } // for
        } // if
    } // reportChange() method
    
} // SimpleObject interface

// $Id: SimpleObject.java 65 2011-07-12 09:47:52Z dejan $
