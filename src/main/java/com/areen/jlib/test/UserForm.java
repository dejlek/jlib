/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.test;

import com.areen.jlib.gui.form.ifp.InputFormFieldEditor;
import com.areen.jlib.gui.form.ifp.InputFormModel;
import com.areen.jlib.gui.form.ifp.InputFormPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author mehjabeen
 */
public class UserForm extends JFrame {

    public UserForm() {
        InputFormModel<User> userFormModel = new InputFormModel();
        userFormModel.model = new User();
        userFormModel.title = "USER DETAILS";
        userFormModel.actions.add(new AbstractAction("OK") {

            @Override
            public void actionPerformed(final ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        });
        userFormModel.actions.add(new AbstractAction("Cancel") {

            @Override
            public void actionPerformed(final ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        });

        //Option 1://-----------------------------------------------------------

        InputFormPanel userFormPanel = new InputFormPanel(userFormModel);
       
        final JComboBox nameCombo = new JComboBox(new String[]{"Dejan", "Vipul", "Mehjabeen"});
        final JLabel sportLabel = new JLabel("Hello");

        nameCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                String chosenName = (String) cb.getSelectedItem();
                sportLabel.setText(chosenName);
            }
        });
        class myNameCombo<nameCombo> extends InputFormFieldEditor {

            @Override
            public Object getValue() {
                return nameCombo.getSelectedItem();
            }

            @Override
            public JComponent getComponent() {
                return nameCombo;
            }
        };
        
        userFormPanel.setFieldWithInputComponentAndLabel(3, new myNameCombo(), sportLabel);
        //userFormPanel.setHiddenFields(2); // making a field hidden after any custom components are set for particular fields
        userFormPanel.setFieldAtFormIndex(0, 2);


        //Option 2://-----------------------------------------------------------
        /*
        InputFormContent<User> ucp= new InputFormContent(new User());
        ucp.add(new TestContentPanel());
        InputFormPanel userFormPanel = new InputFormPanel(userFormModel,ucp);
        */

        getContentPane().add(userFormPanel);
        setLocationRelativeTo(null);
        pack();
    }

    public static void main(final String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new UserForm().setVisible(true);
            }
        });
    }
}
