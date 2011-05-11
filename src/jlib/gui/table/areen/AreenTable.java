
package jlib.gui.table.areen;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dejan
 */
public class AreenTable extends JTable {

    public AreenTable() {
        super();

        // Install AreenTableCellHeaderRenderer object as the default header renderer.
        getTableHeader().setDefaultRenderer(new AreenTableCellHeaderRenderer());
    } // AreenTable constructor

    public void setTableModel(DefaultTableModel dtm){
        setModel(dtm);
    }
} // AreenTable class

// $Id$
