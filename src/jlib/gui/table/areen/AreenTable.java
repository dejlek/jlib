
package jlib.gui.table.areen;

import javax.swing.JTable;

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

} // AreenTable class

// $Id$
