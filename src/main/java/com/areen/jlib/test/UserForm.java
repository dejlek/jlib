/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.test;

import com.areen.apc.lib.model.User;
import com.areen.jlib.gui.form.ifp.InputFormModel;
import com.areen.jlib.gui.form.ifp.InputFormPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author mehjabeen
 */
public class UserForm extends JFrame{
    
    public UserForm(){
        InputFormModel<User> userFormModel = new InputFormModel();
        userFormModel.model = new User();
        userFormModel.title = "USER DETAILS";
        userFormModel.actions.add(new AbstractAction("OK"){

            @Override
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        
        });
        userFormModel.actions.add(new AbstractAction("Cancel"){

            @Override
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        
        });
        
        //Option 1://-----------------------------------------------------------
        
        InputFormPanel userFormPanel = new InputFormPanel(userFormModel);
        JComboBox nameCombo = new JComboBox(new String[]{"Dejan", "Vipul", "Mehjabeen"});
        final JLabel sportLabel = new JLabel("Hello");
        nameCombo.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                String chosenName = (String)cb.getSelectedItem();
                sportLabel.setText(chosenName);
            }
        });
        
        
        userFormPanel.setFieldWithInputComponentAndLabel(3, nameCombo, sportLabel);
        //userFormPanel.setHiddenFields(2); // making a field hidden after any custom components are set for particular fields
        userFormPanel.setFieldAtFormIndex(0,2);
        //Option 2://-----------------------------------------------------------
        /*
        UserContentPanel ucp= new UserContentPanel();        
        InputFormPanel userFormPanel = new InputFormPanel(userFormModel,ucp);
         */
        getContentPane().add(userFormPanel);
    }
    
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new UserForm().setVisible(true);                
            }
        });
    }
}
