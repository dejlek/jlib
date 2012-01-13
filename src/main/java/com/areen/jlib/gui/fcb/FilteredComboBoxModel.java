/**
 * $Id$
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
 * Author(s) in chronological order:
 *   Dejan Lekic , http://dejan.lekic.org
 * Contributor(s):
 *   -
 */

package com.areen.jlib.gui.fcb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 * The only reason for this class is that we have to maintain two lists. One list is the list of ALL
 * data records (fcbObjects), and the list of records that match the filter (super.objects).
 * 
 * Note: this is almost a full-copy of the DefaultComboBoxModel. I could not subclass it because its
 * "objects" ArrayList is not accessible...
 * 
 * Also note that once we start using JDK 7 we will have to add type parameter E here.
 * 
 * @author dejan
 */
public class FilteredComboBoxModel
        extends AbstractListModel
        implements MutableComboBoxModel, Serializable {
    
    //ArrayList<E> objects; JDK 7
    ArrayList objects;
    Object selectedObject;
    //private ArrayList<E> fcbObjects; JDK 7
    private final ArrayList fcbObjects;
    private String lastPattern = "";
    private int keyFieldIndex = 0; /// The index of the (unique) key part of each item.
    private boolean tableModelInUse = false;
    AbstractTableModel tableModel;
    
    /**
     * Constructs an empty ArrayListModel object.
     */
    public FilteredComboBoxModel() {
        objects = new ArrayList();
        fcbObjects = new ArrayList();

    }

    /**
     * Constructs a ArrayListModel object initialized with
     * an array of objects.
     *
     * @param items  an array of Object objects
     */
    public FilteredComboBoxModel(final Object[] items) {
        //objects = new ArrayList<E>(); JDK 7
        objects = new ArrayList<Object>();
        objects.ensureCapacity(items.length);

        int i, c;
        for (i = 0, c = items.length; i < c; i++) {
            //objects.addElement(items[i]);
            objects.add(items[i]);
        } // for

        if (getSize() > 0) {
            //selectedObject = getElementAt(0);
            setSelectedItem(getElementAt(0));
        }
        
        // fcbObjects = new ArrayList<E>(); JDK 7
        fcbObjects = new ArrayList();
        fcbObjects.addAll(Arrays.asList(items));
    }
    
    /**
     * Be sure the type E is equal to Object[] when using this constructor!
     * 
     * @param argAbstractTableModel
     * @param columns An int[] array holding information what columns from the model we want to show in the
     *                combo-box.
     * @param argKeyFieldNumber 
     */
    public FilteredComboBoxModel(
            AbstractTableModel argAbstractTableModel
            , int[] columns
            , int argKeyFieldNumber) {
        tableModelInUse = true;
        tableModel = argAbstractTableModel;
        objects = new ArrayList();
        objects.ensureCapacity(argAbstractTableModel.getRowCount());
        fcbObjects = new ArrayList();
        fcbObjects.ensureCapacity(argAbstractTableModel.getRowCount());
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            //objects.addElement(items[i]);
            Object[] row = new Object[tableModel.getColumnCount()];
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                row[j] = tableModel.getValueAt(i, j);
            } // for
            objects.add(row); // have to cast because Java will shout otherwise...
            fcbObjects.add(row);
        } // for

        if (getSize() > 0) {
            setSelectedItem(getElementAt(0));
        }
        
        tableModel.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                int first;
                int last;
                switch (e.getType()) {
                    case TableModelEvent.INSERT:
                        System.out.println("FCBM: insert");
                        first = e.getFirstRow();
                        last = e.getLastRow();
                        //fcbObjects.add(first, getRow(first));
                        FilteredComboBoxModel.this.addElement(getRow(first));
                        break;
                    case TableModelEvent.DELETE:
                        System.out.println("FCBM: delete");
                        first = e.getFirstRow();
                        last = e.getLastRow();
                        break;
                    case TableModelEvent.UPDATE:
                        System.out.println("FCBM: update");
                        first = e.getFirstRow();
                        last = e.getLastRow();
                        objects.set(first, getRow(first));
                        fcbObjects.set(first, getRow(first));
                        break;
                    default:
                        // nothing
                } // switch
            } // tableChanged() method
        }); // TableModelListener implementation (anonymous)
    } // FilteredComboBoxModel constructor

    /**
     * Constructs a ArrayListModel object initialized with
     * a ArrayList.
     *
     * @param v  a ArrayList object ...
     */
    public FilteredComboBoxModel(ArrayList v) {
        objects = v;

        if (getSize() > 0) {
            selectedObject = getElementAt(0);
        }
        
        fcbObjects = new ArrayList();
        fcbObjects.ensureCapacity(v.size());
        for (Object obj : v) {
            fcbObjects.add(obj);
        } // foreach
    }

    // implements javax.swing.ComboBoxModel
    /**
     * Set the value of the selected item. The selected item may be null.
     * <p>
     * @param anObject The combo box value or null for no selection.
     */
    @Override
    public void setSelectedItem(Object anObject) {
        if ((selectedObject != null && !selectedObject.equals(anObject)) 
                || selectedObject == null 
                && anObject != null) {
            selectedObject = anObject;
            fireContentsChanged(this, -1, -1);
        }
    }

    // implements javax.swing.ComboBoxModel
    @Override
    public Object getSelectedItem() {
        return selectedObject;
    }

    // implements javax.swing.ListModel
    @Override
    public int getSize() {
        return objects.size();
    }

    // implements javax.swing.ListModel
    @Override
    public Object getElementAt(int index) {
        if (index >= 0 && index < objects.size()) {
            //return objects.elementAt(index);
            return objects.get(index);
        } else {
            return null;
        }
    }

    /**
     * Returns the index-position of the specified object in the list.
     *
     * @param anObject
     * @return an int representing the index position, where 0 is
     *         the first position
     */
    public int getIndexOf(Object anObject) {
        return objects.indexOf(anObject);
    }

    // implements javax.swing.MutableComboBoxModel
    @Override
    public void addElement(Object anObject) {
        //TODO: when an element is added to the combo box, we should check if it matches the filter or not!
        //objects.addElement(anObject);
        objects.add(anObject);
        fcbObjects.add(anObject);
        fireIntervalAdded(this, objects.size() - 1, objects.size() - 1);
        if (objects.size() == 1 && selectedObject == null && anObject != null) {
            setSelectedItem(anObject);
        } // if
    } // addElement() method

    // implements javax.swing.MutableComboBoxModel
    @Override
    public void insertElementAt(Object anObject, int index) {
        //objects.insertElementAt(anObject, index);
        objects.add(index, anObject);
        fireIntervalAdded(this, index, index);
    }

    // implements javax.swing.MutableComboBoxModel
    @Override
    public void removeElementAt(int index) {
        if (getElementAt(index) == selectedObject) {
            if (index == 0) {
                setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
            } else {
                setSelectedItem(getElementAt(index - 1));
            }
        }

        //objects.removeElementAt(index);
        objects.remove(index);
        
        fireIntervalRemoved(this, index, index);
    }

    // implements javax.swing.MutableComboBoxModel
    @Override
    public void removeElement(Object anObject) {
        int index = objects.indexOf(anObject);
        if (index != -1) {
            removeElementAt(index);
        }
    }

    /**
     * Empties the list.
     */
    public void removeAllElements() {
        if (objects.size() > 0) {
            int firstIndex = 0;
            int lastIndex = objects.size() - 1;
            //objects.removeAllElements();
            objects.clear();
            selectedObject = null;
            fireIntervalRemoved(this, firstIndex, lastIndex);
        } else {
            selectedObject = null;
        }
    }
    
    public void setPattern(String argPattern) {
        String copy = argPattern.trim();
        if (lastPattern.equals(copy)) {
            // we have the same pattern, probably with an additional space, no need for filtering.
            System.out.println("DEBUG: (equal) setPattern(" + argPattern + ");");
            return;
        }
        System.out.println("DEBUG: setPattern(" + argPattern + ");");
        
        // record the size before we start modifying the filtered list of objects
        int size1 = getSize();
        
        if (!objects.isEmpty()) {
            objects.clear();
        }
        
        if (argPattern.isEmpty()) {
            // pattern contains no characters - might be erased, so we have to populate objects list.
            objects.addAll(fcbObjects);
        } else {
            // Before we split, lets remove some special characters.
            copy.replaceAll(" - ", " ");
            // pattern contains at least one character
            String[] strings = copy.split(" ");
            
            boolean found;

            Object obj = null;
            for (int i = 0; i < fcbObjects.size(); i++) {
                obj = fcbObjects.get(i);
                found = true;
                for (String str : strings) {
                    String itemAsString = null;
                    if (tableModelInUse) {
                        // if we use the table model, we have to convert an array of Objects to a String
                        // before we try to find the match.
                        //itemAsString = Sise.record((Object[]) obj);
                        itemAsString = ((Object[]) obj)[0].toString();
                    } else {
                        itemAsString = obj.toString();
                    }
                    // we are going to to AND found with true or false because we want objects that 
                    // match all elements in the "strings" array of String objects.
                    if (itemAsString.toLowerCase().contains(str.toLowerCase())) {
                        found &= true;
                    } else {
                        found &= false;
                    }
                } // foreach
                if (found) {
                    // obj contains one of the strings in the pattern. Let's see if we have that object in
                    // filtered list. If not, we must add it.
                    objects.add(obj);
                } // if
            } // for
        } // else
        
        // get the size after filtering
        int size2 = getSize();
        
        System.out.println(size1 + ", " + size2);
        if (size1 < size2) {
            fireIntervalAdded(this, size1, size2 - 1);
            fireContentsChanged(this, 0, size1 - 1);
        } else if (size1 > size2) {
            fireIntervalRemoved(this, size2, size1 - 1);
            fireContentsChanged(this, 0, size2 - 1);
        } else {
            fireContentsChanged(this, 0, size2 - 1);
        }
        if (this.getSize() > 0) {
            setSelectedItem(objects.get(0));
        }
        lastPattern = copy;
    } // setPattern() method implementation
    
    public Object lookupItem(String argPattern) {
        Object selectedItem = getSelectedItem();
        // only search for a different item if the currently selected does not match
        if (selectedItem != null && startsWithIgnoreCase(selectedItem.toString(), argPattern)) {
            return selectedItem;
        } else {
            // iterate over all items
            for (int i = 0, n = getSize(); i < n; i++) {
                Object currentItem = getElementAt(i);
                // current item starts with the pattern?
                if (startsWithIgnoreCase(currentItem.toString(), argPattern)) {
                    return currentItem;
                } // if
            } // for
        } // else
        // no item starts with the pattern => return null
        return null;
    }

    // checks if str1 starts with str2 - ignores case
    private boolean startsWithIgnoreCase(String str1, String str2) {
        return str1.toUpperCase().startsWith(str2.toUpperCase());
    }

    public boolean isTableModelInUse() {
        return tableModelInUse;
    }

    public void setTableModelInUse(boolean argTableModelInUse) {
        tableModelInUse = argTableModelInUse;
    }

    public void setSelectedIndex(int argIndex) {
        if (argIndex > -1 && argIndex < getSize()) {
            setSelectedItem(objects.get(argIndex));
        }
    }

    /**
     * Use this method when you need to get all fields of a row in an AbstractTableModel as an array of
     * Objects.
     * 
     * @param argRowIndex
     * @return 
     */
    private Object[] getRow(int argRowIndex) {
        Object[] row = new Object[tableModel.getColumnCount()];
        for (int j = 0; j < row.length; j++) {
            row[j] = tableModel.getValueAt(argRowIndex, j);
        }
        return row;
    }
} // FilteredComboBoxModel class

// $Id$
