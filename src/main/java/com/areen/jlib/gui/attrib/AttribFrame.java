/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.areen.jlib.gui.attrib;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author dejan
 */
public class AttribFrame extends javax.swing.JPanel {

    private final AttributesEditableView attributesEditableView1;
    private final AttributesEditor attributesEditor1;
    private final AttributesModel attributesModel;
    private int testNumber;
    
    /**
     * Creates new form AttribFrame
     */
    public AttribFrame() {
        testNumber = 0;
        
        initComponents();
        
        // Allowed values
        String[] alvals = new String[] { "YN?", "ABCD", "TF" };
        
        // Initialisation
        attributesModel = new AttributesModel(alvals, "?AT");
        attributesModel.setTitles(new String[]{ "One", "Two", "Three" });
        
        
        attributesEditableView1 = new AttributesEditableView(attributesModel);
        attributesEditableView1.setBackground(Color.WHITE);
        attributesEditor1 = new AttributesEditor(attributesModel);
        
        jPanel2.add(attributesEditableView1);
        jPanel2.add(attributesEditor1);
        jPanel2.add(new JLabel("|"));
        jPanel2.add(new JTextField("?"));
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this
     * code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        testButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText("jLabel1");
        jPanel2.add(jLabel1);

        add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        testButton.setText("Test");
        testButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testButtonActionPerformed(evt);
            }
        });
        jPanel1.add(testButton);

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        jPanel1.add(exitButton);

        add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Attributes"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void testButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testButtonActionPerformed
        ++testNumber;
        testButton.setText("Test " + testNumber);
        
        switch (testNumber) {
            case 1: 
                attributesModel.setAttributes("NBF");
                break;
                
            default:
                // nothing
        } // switch
        
        if (testNumber == 9) {
            // we reset when test number is 9 so next time user clicks we start again with test 1
            testNumber = 0;
        }
        
    }//GEN-LAST:event_testButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exitButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton testButton;
    // End of variables declaration//GEN-END:variables
}
