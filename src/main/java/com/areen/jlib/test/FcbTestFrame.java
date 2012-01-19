/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.areen.jlib.test;

import com.areen.jlib.gui.ComboBoxLink;
import com.areen.jlib.gui.WideComboBox;
import com.areen.jlib.gui.fcb.ComboBoxFilter;
import com.areen.jlib.gui.fcb.FilteredComboBoxCellEditor;
import com.areen.jlib.gui.fcb.FilteredComboBoxCellRenderer;
import com.areen.jlib.gui.fcb.FilteredComboBoxModel;
import com.areen.jlib.tuple.Pair;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dejan
 */
public class FcbTestFrame extends javax.swing.JFrame {
    private Pair<String, String>[] codeAndValues;
    private FilteredComboBoxModel cbModel;
    private FilteredComboBoxModel vecModel;
    private DefaultTableModel tableModel;
    
    /**
     * Creates new form FcbTestFrame
     */
    public FcbTestFrame(Pair<String, String>[] argCodeAndValues) {
        codeAndValues = argCodeAndValues;
        
        // we have to initialise the model before some of the combo-boxes are created
        cbModel = new FilteredComboBoxModel(codeAndValues);
        vecModel = new FilteredComboBoxModel(codeAndValues);
        
        initComponents();
        tableModel = (DefaultTableModel) testTable.getModel();
        
        testTable.setSurrendersFocusOnKeystroke(true);
        
        // TABLE
        JComboBox fcb = new JComboBox(vecModel);
        fcb.setEditable(true);
        new ComboBoxFilter(filteredComboBox, vecModel);
        testTable.getColumnModel().getColumn(3).setCellEditor(new FilteredComboBoxCellEditor(fcb));
        
        // FILTERED COMBO BOX (TEST)
        //filteredComboBox.setEditable(true);
        //filteredComboBox.setModel(cbModel); no need, the constructor will set the model for us
        new ComboBoxFilter(filteredComboBox, cbModel);
        filteredComboBox.setRenderer(new FilteredComboBoxCellRenderer(cbModel));
        System.out.println(filteredComboBox.getModel().getClass().getCanonicalName());
        new ComboBoxLink(filteredComboBox, jLabel1);
        
        
        // add it to the table too
        WideComboBox tableCb = new WideComboBox();
        FilteredComboBoxModel fcbModel = new FilteredComboBoxModel(codeAndValues);
        tableCb.setRenderer(new FilteredComboBoxCellRenderer(fcbModel));
        new ComboBoxFilter(tableCb, fcbModel);
        testTable.getColumnModel().getColumn(2).setCellEditor(new FilteredComboBoxCellEditor(tableCb));
        
        // FILTERED COMBO BOX (TEST)
        //normalComboBox.setModel(new DefaultComboBoxModel(argCodeAndValues));
        normalComboBox.setModel(new FilteredComboBoxModel(codeAndValues));
        normalComboBox.setEditable(true);
        new ComboBoxLink(normalComboBox, jLabel1);
        
        tableComboBox.setEditable(true);
        FilteredComboBoxModel tableCbModel = new FilteredComboBoxModel(tableModel, new int[]{0, 1});
        tableComboBox.setRenderer(new FilteredComboBoxCellRenderer(tableCbModel));
        new ComboBoxFilter(tableComboBox, tableCbModel);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this
     * code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        testTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        filteredComboBox = new WideComboBox(cbModel);
        jLabel1 = new javax.swing.JLabel();
        normalComboBox = new javax.swing.JComboBox();
        tableComboBox = new WideComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FilteredComboBox Test");

        testTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                { new Integer(0), "One", null, null},
                { new Integer(11), "Two", null, null},
                { new Integer(31), "Three", null, null},
                { new Integer(2), "Four", null, null}
            },
            new String [] {
                "ID", "NA1", "NA2", "Top Level Domain"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        testTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                testTableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(testTable);

        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        filteredComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        filteredComboBox.setNextFocusableComponent(jButton1);
        filteredComboBox.setOpaque(false);
        filteredComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filteredComboBoxActionPerformed(evt);
            }
        });

        jLabel1.setText("<-- WideComboBox with ComboBoxFilter applied.");
        jLabel1.setFocusable(false);

        normalComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        tableComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 854, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(tableComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(filteredComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(normalComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filteredComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(normalComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(tableComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        System.out.println("===================================================");
        System.out.println("Final selection: " + filteredComboBox.getSelectedItem());
        System.out.println("------ Bye! ---------------------------------------");
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void filteredComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filteredComboBoxActionPerformed
        // TODO add your handling code here:
        // System.out.println("DEBUG: action performed: " + evt.getActionCommand());
        // System.out.println("DEBUG: action performed sel: " + filteredComboBox.getSelectedItem());
    }//GEN-LAST:event_filteredComboBoxActionPerformed

    private void testTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_testTableKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_INSERT) {
            tableModel.addRow(new Object[] { tableModel.getRowCount(), "New", "", ""});
        }
    }//GEN-LAST:event_testTableKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel. For
         * details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FcbTestFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FcbTestFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FcbTestFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FcbTestFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new FcbTestFrame(null).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox filteredComboBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JComboBox normalComboBox;
    private javax.swing.JComboBox tableComboBox;
    private javax.swing.JTable testTable;
    // End of variables declaration//GEN-END:variables
}
