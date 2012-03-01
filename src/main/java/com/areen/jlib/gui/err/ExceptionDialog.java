/**
 * Project: jlib 
 * Version: $Id$
 * License: SPL
 *
 * This file is best viewed with 110 columns.
 * 34567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
 * 
 * Authors (in chronological order): 
 *   Dejan Lekic - http://dejan.lekic.org
 * 
 * Contributors (in chronological order): 
 *   -
 */

package com.areen.jlib.gui.err;

import java.awt.event.KeyEvent;

/**
 *
 * @author dejan
 */
public class ExceptionDialog extends java.awt.Dialog {

    /** Creates new form ExceptionDialog */
    public ExceptionDialog(final java.awt.Frame parent, final boolean modal) {
        super(parent, modal);
        initComponents();
        this.setTitle("Java exception information");
        setResizable(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileAndLineInformationLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        exceptionTextArea = new javax.swing.JTextArea();
        bottomPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(800, 300));
        setPreferredSize(new java.awt.Dimension(800, 300));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        fileAndLineInformationLabel.setFont(fileAndLineInformationLabel.getFont().deriveFont(fileAndLineInformationLabel.getFont().getStyle() | java.awt.Font.BOLD));
        fileAndLineInformationLabel.setText("Test10.java: Line 15");
        add(fileAndLineInformationLabel, java.awt.BorderLayout.PAGE_START);

        exceptionTextArea.setColumns(20);
        exceptionTextArea.setRows(5);
        exceptionTextArea.setPreferredSize(null);
        jScrollPane1.setViewportView(exceptionTextArea);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        okButton.setMnemonic(KeyEvent.VK_ESCAPE);
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        bottomPanel.add(okButton);

        add(bottomPanel, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /** Closes the dialog */
    private void closeDialog(final java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void okButtonActionPerformed(final java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        setVisible(false);
}//GEN-LAST:event_okButtonActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(final String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ExceptionDialog dialog = new ExceptionDialog(new java.awt.Frame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(final java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setModal(true);
                dialog.setVisible(true);
            }
        });
    }

    public void setInfo(final String argInfo) {
        fileAndLineInformationLabel.setText(argInfo);
    }

    public void setExceptionText(final String argExc) {
        exceptionTextArea.setText(argExc);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JTextArea exceptionTextArea;
    private javax.swing.JLabel fileAndLineInformationLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables

}

// $Id$
