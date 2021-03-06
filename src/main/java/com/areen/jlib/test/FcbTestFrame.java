/**
 * Project: jlib
 * Version: $Id$
 * License: SPL
 * 
 * Authors (in chronological order):
 *   Dejan Lekic - http://dejan.lekic.org
 * Contributors (in chronological order):
 *   -
 */
package com.areen.jlib.test;

import com.areen.jlib.beans.AtmRegistry;
import com.areen.jlib.gui.ComboBoxLink;
import com.areen.jlib.gui.ComboBoxPairRenderer;
import com.areen.jlib.gui.DefaultCellEditorX;
import com.areen.jlib.gui.DummyIcon;
import com.areen.jlib.gui.WideComboBox;
import com.areen.jlib.gui.fcb.*;
import com.areen.jlib.tuple.Pair;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author dejan
 */
public class FcbTestFrame extends javax.swing.JFrame {
    private Pair<String, String>[] codeAndValues;
    private FilteredComboBoxModel cbModel;
    private FilteredComboBoxModel cbModel2;
    private FilteredComboBoxModel vecModel;
    private DefaultTableModel tableModel;
    private FilteredComboBoxModel tableCbModel; /// Table model used for FCB that uses table-model
    
    static final Logger LOGGER = Logger.getLogger(ComboBoxFilter.class.getCanonicalName());
    /**
     * Creates new form FcbTestFrame
     */
    public FcbTestFrame(Pair<String, String>[] argCodeAndValues) {
        LOGGER.info("blah!");
        codeAndValues = argCodeAndValues;
        
        // we have to initialise the model before some of the combo-boxes are created
        cbModel = new FilteredComboBoxModel(codeAndValues);
        cbModel2 = new FilteredComboBoxModel(codeAndValues);
        vecModel = new FilteredComboBoxModel(codeAndValues);

        initComponents();

        tableModel = (DefaultTableModel) testTable.getModel();
        
        testTable.setSurrendersFocusOnKeystroke(true);
        
        // TABLE
        //JComboBox fcb = new JComboBox(vecModel);
        //fcb.setEditable(true);
        //new ComboBoxFilter(fcb, vecModel);
        //testTable.getColumnModel().getColumn(3).setCellEditor(new FilteredComboBoxCellEditor(fcb));
        FilteredComboBoxModel nfcbm = new FilteredComboBoxModel(codeAndValues);
        WideComboBox wcb = new WideComboBox(nfcbm);
        // wcb.setEditable(true);
        testTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditorX(wcb));
        
        // FILTERED COMBO BOX (TEST)
        //filteredComboBox.setUI(ColorArrowUI.createUI(filteredComboBox));
        //filteredComboBox.setEditable(true);
        //filteredComboBox.setModel(cbModel); no need, the constructor will set the model for us
        //new ComboBoxFilter(filteredComboBox, cbModel);
        FilteredComboBox fcb = (FilteredComboBox) filteredComboBox;
        /*        
        * fcb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filteredComboBoxActionPerformed(evt);
            }
        });
        * 
        */
        // cbModel.setMultiSelectionAllowed(true);
        // cbModel.setAnyPatternAllowed(true);
        fcb.setRenderer(new FilteredComboBoxCellRenderer(cbModel));

        // atm CbomBoxLink behaves weird when we set selected item AFTER
        cbModel.setReadyToPick(true);
        //fcb.setSelectedIndex(5);
        fcb.setSelectedItem(null);

        //fcb.setMultiSelectionAllowed(true);
        //new ComboBoxLink(filteredComboBox, jTextArea1);
        //fcb.setSelectedItem(null);
        new ComboBoxLink(fcb, jLabel1);
        // filteredComboBox.setSelectedIndex(5); <-- does not make ComboBoxLink change the label! (BUG)
        fcb.pickItemByKey("ba");
        System.out.println(fcb.getSelectedItem());
        
        // Second FCB
        secondFcb.setSelectedItem(null);
        new ComboBoxLink(secondFcb, jLabel2);
        
        // add it to the table too
        FilteredComboBoxModel fcbModel = new FilteredComboBoxModel(codeAndValues);
        FilteredComboBoxCellEditor fcbce = new FilteredComboBoxCellEditor(fcbModel);
        fcbce.setConfig(2);
        testTable.getColumnModel().getColumn(2).setCellEditor(fcbce);
        
        // FILTERED COMBO BOX (TEST)
        //normalComboBox.setModel(new DefaultComboBoxModel(argCodeAndValues));
        normalComboBox.setModel(new FilteredComboBoxModel(codeAndValues));
        normalComboBox.setRenderer(new ComboBoxPairRenderer());
        //normalComboBox.setEditable(true);
        //new ComboBoxLink(normalComboBox, jLabel1);
        AtmRegistry atmRegistry = new AtmRegistry();
        atmRegistry.set("/ds/demo", tableModel);
        //tableComboBox.setEditable(true);
        tableCbModel = new FilteredComboBoxModel(atmRegistry, 
                new int[]{0, 1}, 
                "/ds/demo");
        tableComboBox.setRenderer(new FilteredComboBoxCellRenderer(tableCbModel));
        new ComboBoxFilter(tableComboBox, tableCbModel);
        jLabel1.setIcon(new DummyIcon("A", Color.YELLOW, Color.DARK_GRAY, 18));
        jLabel2.setIcon(new DummyIcon("B", Color.LIGHT_GRAY, Color.decode("000024"), 18));
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
        jSeparator1 = new javax.swing.JSeparator();
        filteredComboBox = new FilteredComboBox(cbModel);
        jLabel1 = new javax.swing.JLabel();
        normalComboBox = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        secondFcb = new FilteredComboBox(cbModel2);
        jLabel2 = new javax.swing.JLabel();
        customPanel = new javax.swing.JPanel();
        tableComboBox = new WideComboBox();
        debugButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();

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
        testTable.setNextFocusableComponent(filteredComboBox);
        testTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                testTableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(testTable);

        filteredComboBox.setNextFocusableComponent(secondFcb);
        filteredComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filteredComboBoxActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("<-- WideComboBox with ComboBoxFilter applied.");
        jLabel1.setFocusable(false);

        normalComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setBorder(null);
        jScrollPane2.setViewportView(jTextArea1);

        secondFcb.setModel(cbModel2);
        secondFcb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                secondFcbActionPerformed(evt);
            }
        });

        jLabel2.setText("jLabel2");

        customPanel.setPreferredSize(new java.awt.Dimension(0, 32));
        customPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        tableComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        tableComboBox.setPreferredSize(new java.awt.Dimension(200, 25));
        customPanel.add(tableComboBox);

        debugButton.setText("Debug");
        debugButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                debugButtonActionPerformed(evt);
            }
        });
        customPanel.add(debugButton);

        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        customPanel.add(jButton1);

        addButton.setText("+");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        customPanel.add(addButton);

        removeButton.setText("-");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        customPanel.add(removeButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(filteredComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(secondFcb, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(normalComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(customPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filteredComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(normalComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(secondFcb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        System.out.println("DEBUG: action performed: " + evt.getActionCommand());
        /*
        Object obj = filteredComboBox.getSelectedItem();
        if (obj != null)
            System.out.println(obj.getClass().getCanonicalName());
        else 
            System.out.println("null");
        obj = filteredComboBox.getModel().getSelectedItem();
        if (obj != null)
            System.out.println(obj.getClass().getCanonicalName());
        else 
            System.out.println("null");
        
        System.out.println("DEBUG: action performed sel: " + filteredComboBox.getSelectedItem());
        FilteredComboBox fcb = (FilteredComboBox) filteredComboBox;
        
        if (fcb.isMultiSelectionAllowed()) {
            System.out.println("DEBUG: last pattern: " + cbModel.getLastPattern());
            for (Object o : fcb.getMatchingItems()) {
                System.out.println(o.getClass());
                System.out.println(o.toString());
            }
        }

        System.out.println("DEBUG: # of matching items: " + fcb.getMatchingItems().length);
        System.out.println("DEBUG: picked: " + fcb.getPickedItem());
        */
    }//GEN-LAST:event_filteredComboBoxActionPerformed

    private void testTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_testTableKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_INSERT) {
            tableModel.addRow(new Object[] { tableModel.getRowCount(), "New", "", ""});
        }
    }//GEN-LAST:event_testTableKeyPressed

    private void secondFcbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_secondFcbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_secondFcbActionPerformed

    private void debugButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_debugButtonActionPerformed
        // TODO add your handling code here:
        FilteredComboBox fcb = (FilteredComboBox) filteredComboBox;
        System.out.println("I: " + fcb.getPickedItem());
        System.out.println("K: " + fcb.getPickedKey());
        System.out.println("SI: " + fcb.getSelectedItem());
        //File file = GuiTools.makeScreenshot("apc-client", this);
        //System.out.println("Screenshot file: " + file);
        //tableCbModel.printDebugInfo();
    }//GEN-LAST:event_debugButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        tableModel.addRow(new Object[] { tableModel.getRowCount() + 100, "New", "", ""});
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        int selRow = testTable.getSelectedRow();
        if (selRow > -1) {
            tableModel.removeRow(selRow);
        }
    }//GEN-LAST:event_removeButtonActionPerformed

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
    private javax.swing.JButton addButton;
    private javax.swing.JPanel customPanel;
    private javax.swing.JButton debugButton;
    private javax.swing.JComboBox filteredComboBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JComboBox normalComboBox;
    private javax.swing.JButton removeButton;
    private javax.swing.JComboBox secondFcb;
    private javax.swing.JComboBox tableComboBox;
    private javax.swing.JTable testTable;
    // End of variables declaration//GEN-END:variables

} // FcbTestFrame class

// $Id$
