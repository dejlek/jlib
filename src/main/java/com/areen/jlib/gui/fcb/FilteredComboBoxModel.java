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
 * Contributors (in chronological order):
 *   -
 */

package com.areen.jlib.gui.fcb;

import com.areen.jlib.tuple.Pair;
import com.areen.jlib.util.Sise;
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
    
    // ====================================================================================================
    // ==== Variables =====================================================================================
    // ====================================================================================================
    
    // :::::: PRIVATE/PROTECTED ::::::
    
    //ArrayList<E> objects; JDK 7
    ArrayList objects;
    Object selectedObject;
    //private ArrayList<E> fcbObjects; JDK 7
    private final ArrayList fcbObjects;
    private String lastPattern = "";
    private int keyFieldIndex = 0; /// The index of the (unique) key part of each item.
    private boolean tableModelInUse = false;
    AbstractTableModel tableModel;
    private Object exactObject = null; /// If there is an exact match, here we hold a reference to the Object.
    private int exactIndex = -1; /// If there is an exact match, here we hold the index of the Object.
    private int[] columns;
    private boolean cancelled = false;
    /** 
     * Indicator whether cell editors should stop editing. This value is most useful in case when we want to
     * pick the item with the mouse.
     */
    private boolean readyToFinish = false; 
    private int keyIndex = 0; /// Index of a key in the SISE record.

    private boolean multiSelectionAllowed = false;
    /**
     * Indicates whether we want ComboBoxFilter to retain the entered string even if we found no matches.
     */
    private boolean anyPatternAllowed = false;
    /**
     * Should we pick an item after the next call to setSelectedItem() or setSelectedIndex() ?
     */
    private boolean readyToPick;

    private Object pickedItem;
    private Object pickedKey;
    
    // ====================================================================================================
    // ==== Constructors ==================================================================================
    // ====================================================================================================
    
    /**
     * Constructs an empty ArrayListModel object.
     */
    public FilteredComboBoxModel() {
        objects = new ArrayList();
        fcbObjects = new ArrayList();
    } // FilteredComboBoxModel constructor (default)

    /**
     * Constructs a ArrayListModel object initialized with
     * an array of objects.
     *
     * @param items  an array of Object objects
     */
    public FilteredComboBoxModel(final Object[] items) {
        //objects = new ArrayList<E>(); JDK 7
        objects = new ArrayList();
        objects.ensureCapacity(items.length);

        int i, c;
        for (i = 0, c = items.length; i < c; i++) {
            //objects.addElement(items[i]);
            objects.add(items[i]);
        } // for

        if (getSize() > 0) {
            setSelectedItem(getElementAt(0));
        }
        
        // fcbObjects = new ArrayList<E>(); JDK 7
        fcbObjects = new ArrayList();
        fcbObjects.addAll(Arrays.asList(items));
    } // FilteredComboBoxModel constructor
    
    /**
     * Be sure the type E is equal to Object[] when using this constructor!
     * 
     * @param argAbstractTableModel
     * @param columns An int[] array holding information what columns from the model we want to show in the
     *                combo-box. The first element in the columns array contains the index of the key field.
     * @param argKeyFieldNumber 
     */
    public FilteredComboBoxModel(
            AbstractTableModel argAbstractTableModel
            , int[] argColumns) {
        tableModelInUse = true;
        tableModel = argAbstractTableModel;
        columns = argColumns;
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

        keyIndex = columns[0]; // by convention the first element in the columns array is the index of the key
        
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
                        //System.out.println("FCBM: TableModel insert");
                        first = e.getFirstRow();
                        last = e.getLastRow();
                        //fcbObjects.add(first, getRow(first));
                        FilteredComboBoxModel.this.addElement(getRow(first));
                        break;
                    case TableModelEvent.DELETE:
                        //System.out.println("FCBM: TableModel delete");
                        first = e.getFirstRow();
                        last = e.getLastRow();
                        break;
                    case TableModelEvent.UPDATE:
                        //System.out.println("FCBM: TableModel update");
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

    // ====================================================================================================
    // ==== Interface/Superclass Methods ==================================================================
    // ====================================================================================================
    
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
        } // if
        if (anObject == null) {
            selectedObject = null;
        } // if
    } // setSelectedItem() method

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
    
    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================
    
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
    } //  // FilteredComboBoxModel constructor

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
        //System.out.println("setPattern(" + argPattern + ")");
        exactObject = null;
        exactIndex = -1;
        
        String copy = argPattern.trim();
        
        if (lastPattern.equals(copy)
                && (argPattern.length() != copy.length())) {
            // we have the same pattern, probably with an additional space, no need for filtering.
            // However, we return only if length changed because in the case user picks an item (in table)
            // and presses F2 to edit again, we lose the selected index, so we have to re-filter again.
            return;
        } // if
        //System.out.println("DEBUG: setPattern(" + argPattern + ");");
        
        // record the size before we start modifying the filtered list of objects
        int size1 = getSize();
        
        if (!objects.isEmpty()) {
            objects.clear();
        }
        
        boolean exactMatchFound = false;
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
            int val = -1;
            for (int i = 0; i < fcbObjects.size(); i++) {
                obj = fcbObjects.get(i);
                
                if (check(copy, obj) == 2) {
                    // we have an exact match!
                    exactObject = obj;
                    exactIndex = i;
                    exactMatchFound = true;
                } // if
                
                found = true;
                for (String str : strings) {
                    val = check(str, obj);
                    found &= ((val == 1) || (val == 2));
                } // foreach
                
                if (found) {
                    System.out.println("M(" + exactIndex + ")" + obj.toString());
                    objects.add(obj);
                } // if
            } // for
        } // else
        
        // get the size after filtering
        int size2 = getSize();
        
        //System.out.println(size1 + ", " + size2);
        if (size1 < size2) {
            fireIntervalAdded(this, size1, size2 - 1);
            fireContentsChanged(this, 0, size1 - 1);
        } else if (size1 > size2) {
            fireIntervalRemoved(this, size2, size1 - 1);
            fireContentsChanged(this, 0, size2 - 1);
        } else {
            fireContentsChanged(this, 0, size2 - 1);
        }

        // Let's select appropriate item.
        if (this.getSize() > 0) {
            if (exactMatchFound) {
                // if we had an exact match, select that item.
                //System.out.println("### Exact match found!");
                //System.out.println("(" + exactIndex + ")" + exactObject.toString());
                setSelectedItem(exactObject);
            } else {
                // if we did not have an exact match, select the first item in the newly created list.
                // WARNING: it is a BUG to select 
                // 
                setSelectedIndex(0);
            } // else
        } else {
            setSelectedItem(null);
        }

        lastPattern = copy;
    } // setPattern() method implementation
    
    public Object lookupItem(String argPattern) {
        if (argPattern.isEmpty()) {
            return null;
        } // if
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
        } else {
            setSelectedItem(null);
        } // else
    }

    /**
     * Use this method to objtain a reference to an object containing the key part of the Item. In the case
     * our combo-box contains a list of Pairs, we will get the first element. In the case it is a list of
     * Object[] arrays, then we use the columns array to get the index of the key.
     * @return 
     */
    public Object getKeyOfAnItem(Object argItem) {
        if (argItem == null) {
            return null;
        }
        if (argItem instanceof Pair) {
            return ((Pair) argItem).getFirst();
        }
        if (argItem instanceof Object[]) {
            // we assume whenever we deal with Object[] array, it came from a table model.
            int idx = columns[0];
            return ((Object[]) argItem)[idx];
        }
        
        // in any other case we will convert object to String and check if it is a Sise record or not.
        String str = argItem.toString();
        if (str.contains(Sise.UNIT_SEPARATOR_STRING)) {
            // we deal with a Sise record
            String[] units = Sise.units(str);
            return units[0];
        } else {
            // If it is any other String, just simply return it.
            return str;
        } // else        
    } // getKeyOfAnItem() method
    
    /**
     * Use this method to obtain a key of the selected item.
     * @return 
     */
    public Object getKeyOfTheSelectedItem() {
        //System.out.println("getKeyOfTheSelectedItem()");
        Object selected = getSelectedItem();
        return getKeyOfAnItem(selected);
    } // getKeyOfTheSelectedItem() method
    
    /**
     * Use this method to get an array of combo-box items selected with wildcard character '*'.
     * @return An array of Objects that match the pattern.
     */
    public Object[] getMatchingItems() {
        if (isMultiSelectionAllowed() && (exactObject == null)) {
            return objects.toArray();
        } else {
            // we fall back to the normal way of doing things
            Object[] retOne = new Object[1];
            retOne[0] = getSelectedItem();
            return retOne;
        } // else
    } // getMatchingItems() method    
    
    // ====================================================================================================
    // ==== Accessors =====================================================================================
    // ====================================================================================================

    public Object getPickedItem() {
        return pickedItem;
    }
    
    public void setPickedItem(Object argPickedItem) {
        exactObject = argPickedItem;
        pickedItem = argPickedItem;
    }

    public Object getPickedKey() {
        return pickedKey;
    }

    public void setPickedKey(Object argPickedKey) {
        pickedKey = argPickedKey;
    }
    
    public void setReadyToPick(boolean argReadyToPick) {
        readyToPick = argReadyToPick;
    }
    
    public boolean isReadyToPick() {
        return readyToPick;
    }
    
    public boolean isReadyToFinish() {
        return readyToFinish;
    }

    public void setReadyToFinish(boolean argReadyToFinish) {
        readyToFinish = argReadyToFinish;
    }
    
    /**
     * Use this method to obtain the index of the key in a SISE record.
     * 
     * @return 
     */
    public int getKeyIndex() {
        return keyIndex;
    } // getKeyIndex() method

    public boolean isMultiSelectionAllowed() {
        return multiSelectionAllowed;
    }

    public void setMultiSelectionAllowed(boolean argMultiSelectionAllowed) {
        multiSelectionAllowed = argMultiSelectionAllowed;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean argCancelled) {
        cancelled = argCancelled;
    }

    public boolean isAnyPatternAllowed() {
        return anyPatternAllowed;
    }

    public void setAnyPatternAllowed(boolean argAnyPatternAllowed) {
        anyPatternAllowed = argAnyPatternAllowed;
    }

    public String getLastPattern() {
        return lastPattern;
    }
    
    // ====================================================================================================
    // ==== Private Methods ===============================================================================
    // ====================================================================================================
    
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
    
    /**
     * This method acts as a "dispatcher" to appropriate check() function.
     * @param argWhat
     * @param argObject
     * @return 0 - if not found, 1 - if found , 2 - if the exact match was found
     */
    private int check(String argWhat, Object argObject) {
        if (argObject instanceof Pair) {
            return check(argWhat, (Pair) argObject);
        } // if
        if (argObject instanceof Object[]) {
            return check(argWhat, (Object[]) argObject);
        } // if
        
        // fall back to using String
        return check(argWhat, argObject.toString());
    } // check() method
    
    /**
     * Checks for partial or exact match of argWhat within the argPair object.
     * @param argWhat
     * @param argString
     * @return 0 - if there is no match, 1 - if argString contains argWhat or 2 if argString is equal 
     *             to argWhat (case insensitive equality).
     */
    private int check(String argWhat, Pair argPair) {
        //System.out.println("PAIR: " + argWhat + " ? " + argPair.toString());
        String left = argPair.getFirst() != null ? argPair.getFirst().toString() : null;
        String right = argPair.getSecond() != null ? argPair.getSecond().toString() : "";
        
        if (left == null && right == null) {
            return 0;
        } // if
        
        if ((left != null && left.toLowerCase().equals(argWhat.toLowerCase()))
                || (right != null && right.toLowerCase().equals(argWhat.toLowerCase()))) {
            // we have an exact match
            return 2;
        } // if
        
        if ((left != null && left.toLowerCase().contains(argWhat.toLowerCase()))
                || (right != null && right.toLowerCase().contains(argWhat.toLowerCase()))) {
            // we found a partial match
            return 1;
        } // if
        return 0;
    } // check() method
    
    /**
     * Checks for partial or exact match of argWhat within an array of Objects.
     * 
     * @param argWhat
     * @param argObjects
     * @return 0 - if there is no match, 1 - if argString contains argWhat or 2 if argString is equal 
     *             to argWhat (case insensitive equality).
     */
    private int check(String argWhat, Object[] argObjects) {
        //System.out.println("OBJECT[]: " + argWhat + " ? " + Sise.record(argObjects));
        String str = null;
        for (Object obj : argObjects) {
            str = (obj == null) ? "" : obj.toString();
            if (str.equalsIgnoreCase(argWhat)) {
                // we found an exact match
                return 2;
            } // if
            if (str.toLowerCase().contains(argWhat.toLowerCase())) {
                // we found a partial match
                return 1;
            } // if
        } // foreach
        return 0;
    } // check() method
    
    /**
     * Checks for partial or exact match of argWhat within argString
     * @param argWhat
     * @param argString
     * @return 0 - if there is no match, 1 - if argString contains argWhat or 2 if argString is equal 
     *             to argWhat (case insensitive equality).
     */
    private int check(String argWhat, String argString) {
        //System.out.println("String: " + argWhat + " ? " + Sise.record(argObjects));
        if (argString.equalsIgnoreCase(argWhat)) {
            // we have an exact match
            return 2;
        } // if
        if (argString.toLowerCase().contains(argWhat.toLowerCase())) {
            // we found a partial match
            return 1;
        } // if
        return 0;
    } // check() method

} // FilteredComboBoxModel class

// $Id$
