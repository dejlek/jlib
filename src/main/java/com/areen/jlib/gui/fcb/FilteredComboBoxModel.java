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
 *   Vipul Kumar
 */

package com.areen.jlib.gui.fcb;

import com.areen.jlib.beans.AtmRegistry;
import com.areen.jlib.gui.form.ifp.CodePair;
import com.areen.jlib.tuple.Pair;
import com.areen.jlib.util.Sise;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;
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
        implements MutableComboBoxModel, TableModelListener, PropertyChangeListener, Serializable {
    /*
     * There are two major use-cases for the FilteredComboBoxModel (FCBM) class:
     * 1) When it deals with arrays of Pair objects.
     *    In this case FCBM object maintains an internal liste of all Pair objects inside the `fcbObjects`
     *    ArrayList. The `objects` ArrayList in this case stores references to the table-model rows (whatever
     *    the getElementAt() method returns.
     * 2) When it deals with the table model objects.
     *    In this case, we use the table model, fcbObjects and objects are ArrayLists of Object[] arrays that 
     *    store references to individual cell objects.
     */
    
    // ====================================================================================================
    // ==== Variables =====================================================================================
    // ====================================================================================================
    
    // :::::: PRIVATE/PROTECTED ::::::
    
    /**
     * In the case we use table model as the source for combo-box items, the objects ArrayList object stores
     * model indexes of filtered items. If the table-model is not used, then objects ArrayList stores either
     * Pair(s), or Object[]s.
     */
    ArrayList objects; // TODO: Once we switch to JDK7 we will use the element type E: ArrayList<E> objects;
    private final ArrayList fcbObjects; // TODO: same here
    private final HashMap<String, Object> fcbObjectsMap; // TODO: same here
    Object selectedObject;
    private String lastPattern = ""; 
    private int keyFieldIndex = 0; /// The index of the (unique) key part of each item.
    private boolean tableModelInUse = false;
    private AbstractTableModel tableModel;
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
    
    private static final Logger LOGGER = Logger.getLogger(FilteredComboBoxModel.class.getCanonicalName());
    private String dataSetID = "unknown";
    private AtmRegistry tableModelRegistry;

    private ArrayList<Integer> objmap;
    private String[] patternParts;
    private int tableModelIndex;
    
    /**
     * A simple configuration flag to inform ComboBoxFilter what text should initially be shown to the
     * user in the text editor component.
     */
    private int config = 0;
    
    private boolean defaultFilter = true;
    
    // ====================================================================================================
    // ==== Constructors ==================================================================================
    // ====================================================================================================
    
    /**
     * Constructs an empty ArrayListModel object.
     */
    public FilteredComboBoxModel() {
        objects = new ArrayList();
        fcbObjects = new ArrayList();
        fcbObjectsMap = new HashMap<String, Object>();
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
        fcbObjectsMap = new HashMap<String, Object>();
        
        int i, c;
        for (i = 0, c = items.length; i < c; i++) {
            //objects.addElement(items[i]);
            objects.add(items[i]);
            fcbObjectsMap.put(items[i].toString(), items[i]);
        } // for

        if (getSize() > 0) {
            setSelectedItem(getElementAt(0));
        } // if
        
        // fcbObjects = new ArrayList<E>(); JDK 7
        fcbObjects = new ArrayList();
        fcbObjects.addAll(Arrays.asList(items));
    } // FilteredComboBoxModel constructor
    
    /**
     * If FilteredComboBox should show values from some JTable object, use this constructor.
     * 
     * NOTE: argAbstractTableModel MAY be null! Later developer may call setTableModel() method to set the
     * reference to the actual table model object.
     * 
     * The reason why we have argModelID here is that it is very much possible that argAbstractTableModel
     * could be a DefaultTableModel, not AreenTableModel, so we have to give it a unique ID ourselves.
     * 
     * @param argAbstractTableModel
     * @param argColumns int[] object holding (table model) column indexes that should be included in the
     *   filtering process. By convention, the first index in this array is the index of the primary key
     *   column.
     */
    public FilteredComboBoxModel(
            AbstractTableModel argAbstractTableModel, int[] argColumns, String argModelID) {
        setTableModelInUse(true);
        dataSetID = argModelID;
        tableModel = argAbstractTableModel;
        objects = new ArrayList();
        columns = argColumns;
        fcbObjects = new ArrayList();
        fcbObjectsMap = new HashMap<String, Object>();
        handleNewTableModel(tableModel);
    } // FilteredComboBoxModel constructor

    /**
     * If we have an AtmRegistry object, then we use this constructor to find appropriate table-model to use
     * for value changes...
     * 
     * @param argAtmRegistry
     * @param argColumns int[] object holding (table model) column indexes that should be included in the
     *   filtering process. By convention, the first index in this array is the index of the primary key
     *   column.
     * @param argModelID 
     */
    public FilteredComboBoxModel(AtmRegistry argAtmRegistry, int[] argColumns, String argModelID) {
        setTableModelInUse(true);
        dataSetID = argModelID;
        setTableModelRegistry(argAtmRegistry);
        tableModel = argAtmRegistry.get(argModelID);
        //DEBUG: System.out.println("tableModel: " + tableModel);
        objects = new ArrayList();
        columns = argColumns;
        fcbObjects = new ArrayList();
        fcbObjectsMap = new HashMap<String, Object>();
        handleNewTableModel(tableModel);
        //DEBUG: System.out.println("objects: " + objects.size());
        //DEBUG: System.out.println("fcbObjects: " + fcbObjects.size());
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
        } // if
        
        fcbObjects = new ArrayList();
        fcbObjects.ensureCapacity(v.size());
        fcbObjectsMap = new HashMap<String, Object>();
        for (Object obj : v) {
            fcbObjects.add(obj);
            fcbObjectsMap.put(obj.toString(), obj);
        } // foreach
    } //  // FilteredComboBoxModel constructor

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
        //DEBUG: System.out.println("setSelectedItem() called ");
        //DEBUG: System.out.println("1. selectedObject: " + selectedObject);
        //DEBUG: System.out.println("anObject: " + anObject);
        boolean notNullAndDiffSelection = (selectedObject != null && !selectedObject.equals(anObject));
        //DEBUG: System.out.println("notNullAndDiffSelection: " + notNullAndDiffSelection);
        boolean nullCurrentSelection = (selectedObject == null);
        //DEBUG: System.out.println("nullCurrentSelection: " + nullCurrentSelection);
        boolean nullNewSelection = (anObject == null);
        //DEBUG: System.out.println("nullNewSelection: " + nullNewSelection);
        if (notNullAndDiffSelection || nullCurrentSelection && !nullNewSelection) {
            selectedObject = anObject;
            fireContentsChanged(this, -1, -1);
        } // if
        if (nullNewSelection) {
            selectedObject = null;
        } // if
        //DEBUG: System.out.println("2. selectedObject: " + selectedObject);
    } // setSelectedItem() method

    // implements javax.swing.ComboBoxModel
    @Override
    public Object getSelectedItem() {
        return selectedObject;
    } // getSelectedItem() method

    // implements javax.swing.ListModel
    @Override
    public int getSize() {
        return objects.size();
    } // getSize() method

    // implements javax.swing.ListModel
    @Override
    public Object getElementAt(int index) {
        if (index >= 0 && index < objects.size()) {
            //return objects.elementAt(index);
            return objects.get(index);
        } else {
            return null;
        } // else
    } // getElementAt() method
    
    /**
     * Use this method to insert an item into the combo-box model.
     * Precondition:
     *   This method expect tableModelIndex value to be set, so it can insert a proper Integer object into
     *   the objects array.
     * NOTE: implements javax.swing.MutableComboBoxModel
     */
    @Override
    public void addElement(Object anObject) {
        //DEBUG: System.out.println("addElement(" + anObject.getClass().getCanonicalName() + ")");
        fcbObjects.add(anObject);
        fcbObjectsMap.put(anObject.toString(), anObject);
        /* 
         * TODO: when an element is added to the combo box, we should check if it matches the filter or not!
         *       If it does, then we add it to the `objects` array, and only then we fireIntervalAdded().
         */
        if (isTableModelInUse()) {
            // Check if anObject matches the pattern
            if (matchesPattern(anObject)) {
                objects.add(anObject);
                fireIntervalAdded(this, objects.size() - 1, objects.size() - 1);
            }
        } else {
            objects.add(anObject);
            fireIntervalAdded(this, objects.size() - 1, objects.size() - 1);
        } // else
    } // addElement() method

    // implements javax.swing.MutableComboBoxModel
    @Override
    public void insertElementAt(Object anObject, int index) {
        //objects.insertElementAt(anObject, index);
        objects.add(index, anObject);
        fireIntervalAdded(this, index, index);
    } // insertElementAt() method

    // implements javax.swing.MutableComboBoxModel
    @Override
    public void removeElementAt(int index) {
        if (getElementAt(index) == selectedObject) {
            if (index == 0) {
                setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
            } else {
                setSelectedItem(getElementAt(index - 1));
            } // else
        } // if

        //objects.removeElementAt(index);
        objects.remove(index);
        
        fireIntervalRemoved(this, index, index);
    } // removeElementAt() method

    // implements javax.swing.MutableComboBoxModel
    @Override
    public void removeElement(Object anObject) {
        int index = objects.indexOf(anObject);
        if (index != -1) {
            removeElementAt(index);
        } // if
    } // removeElement() method
    
    // ::::: TableModelListener ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    @Override
    public void tableChanged(TableModelEvent e) {
        int first;
        int last;
        switch (e.getType()) {
            case TableModelEvent.INSERT:
                //DEBUG: System.out.println("FCBM: TableModel insert");
                first = e.getFirstRow();
                last = e.getLastRow();
                //DEBUG: System.out.println("      F: " + first + "   L: " + last);
                addElement(getRow(first));
                fireIntervalAdded(this, first, last);
                break;
            case TableModelEvent.DELETE:
                //DEBUG: System.out.println("FCBM: TableModel delete");
                first = e.getFirstRow();
                last = e.getLastRow();
                //DEBUG: System.out.println("      F: " + first + "   L: " + last);
                removeElements(first, last);
                fireIntervalRemoved(this, first, last);
                break;
            case TableModelEvent.UPDATE:
                //DEBUG: System.out.println("FCBM: TableModel update");
                first = e.getFirstRow();
                last = e.getLastRow();
                //DEBUG: System.out.println("      F: " + first + "   L: " + last);
                Object obj = getRow(first);
                updateElement(first, obj);
                fireContentsChanged(this, first, last);
                break;
            default:
                // nothing
        } // switch
    } // tableChanged() method

    // ::::: PropertyChangeListener ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (dataSetID == null) {
            return;
        }
        if (evt.getPropertyName().equals(dataSetID)) {
            tableModel = (AbstractTableModel) evt.getNewValue();
        }
    } // propertyChange() method
    
    // ====================================================================================================
    // ==== Public Methods ================================================================================
    // ====================================================================================================

    /**
     * Returns the index-position of the specified object in the list.
     *
     * @param anObject
     * @return an int representing the index position, where 0 is
     *         the first position
     */
    public int getIndexOf(Object anObject) {
        return objects.indexOf(anObject);
    } // getIndexOf() method

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
        } // else
    } // removeAllElements() method
    
    /**
     * This method is the most important method for the filtered combo-box. Here we set the pattern, and
     * filter the items in the fcbObjects ArrayList to objects ArrayList. If all fcbObject items match the
     * pattern, than objects ArrayList should have the same content as the fcbObjects.
     * 
     * @param argPattern String object containing a pattern to match against.
     */
    public void setPattern(String argPattern) {
        //DEBUG: System.out.println("setPattern(" + argPattern + ")");
        exactObject = null;
        exactIndex = -1;
        
        String copy = argPattern.trim();
        //DEBUG: System.out.println("lastPattern(" + lastPattern + ")");
        //DEBUG: System.out.println("copy(" + copy + ")");
        //DEBUG: System.out.println("selected item(" + getSelectedItem() + ")");
        //DEBUG: System.out.println("selected pair(" + getKeyValuePairOfTheSelectedItem() + ")");
        if (lastPattern.equals(copy)
                && (argPattern.length() != copy.length())) {
            // we have the same pattern, probably with an additional space, no need for filtering.
            // However, we return only if length changed because in the case user picks an item (in table)
            // and presses F2 to edit again, we lose the selected index, so we have to re-filter again.
            return;
        } // if
        //DEBUG: System.out.println("DEBUG: setPattern(" + argPattern + ");");
        
        // record the size before we start modifying the filtered list of objects
        int size1 = getSize();
        //DEBUG: System.out.println("size1: " + size1);
        if (!objects.isEmpty()) {
            objects.clear();
        } // if
        String[] strings = null;
        boolean exactMatchFound = false;
        //DEBUG: System.out.println("argPattern.isEmpty() (" + argPattern.isEmpty() + ")");
        if (argPattern.isEmpty()) {
            // pattern contains no characters - might be erased, so we have to populate objects list.
            objects.addAll(fcbObjects);
        } else {
            if (defaultFilter) {
                // Before we split, lets remove some special characters.
                copy.replaceAll(" - ", " ");
                // pattern contains at least one character
                strings = copy.split(" ");

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
                        //Logging just slows the function - the function needs to finish as soon as possible!
                        //LOGGER.info("Exact match `" + obj.toString() + "` found at idx=" + exactIndex);
                        objects.add(obj);
                    } // if
                } // for
            } // if
        } // else
        if (defaultFilter) {
            // get the size after filtering
            int size2 = getSize();
            //DEBUG: System.out.println("size2: " + size2);
            //DEBUG: System.out.println(size1 + ", " + size2);
            if (size1 < size2) {
                fireIntervalAdded(this, size1, size2 - 1);
                fireContentsChanged(this, 0, size1 - 1);
            } else if (size1 > size2) {
                fireIntervalRemoved(this, size2, size1 - 1);
                fireContentsChanged(this, 0, size2 - 1);
            } else {
                fireContentsChanged(this, 0, size2 - 1);
            } // else
            int newSize = this.getSize();
            //DEBUG: System.out.println("newSize: " + newSize);
            //DEBUG: System.out.println("exactMatchFound: " + exactMatchFound);
            // Let's select appropriate item.
            if (newSize > 0) {
                if (exactMatchFound) {
                    // if we had an exact match, select that item.
                    //DEBUG: System.out.println("### Exact match found!");
                    //DEBUG: System.out.println("(" + exactIndex + ")" + exactObject.toString());
                    setSelectedItem(exactObject);
                } else {
                    // if we did not have an exact match, select the first item in the newly created list.
                    // WARNING: it is a BUG to select 
                    // 
                    setSelectedIndex(0);
                } // else
            } else {
                setSelectedItem(null);
            } // else
        } // if
        lastPattern = copy;
        patternParts = strings; // store the patternParts so we do not have to split again
    } // setPattern() method implementation
    
    /**
     * 
     * @param argPattern
     * @return
     */
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
    } // lookupItem() method

    // checks if str1 starts with str2 - ignores case
    private boolean startsWithIgnoreCase(String str1, String str2) {
        return str1.toUpperCase().startsWith(str2.toUpperCase());
    } // startsWithIgnoreCase() method

    /**
     * 
     * @return
     */
    public boolean isTableModelInUse() {
        return tableModelInUse;
    } // isTableModelInUse() method

    /**
     * 
     * @param argTableModelInUse
     */
    public void setTableModelInUse(boolean argTableModelInUse) {
        tableModelInUse = argTableModelInUse;
    } // setTableModelInUse() method

    /**
     * 
     * @param argIndex
     */
    public void setSelectedIndex(int argIndex) {
        //DEBUG: System.out.println("setSelectedIndex() called=========");
        //DEBUG: System.out.println("argIndex: " + argIndex);
        int newSize = getSize();
        //DEBUG: System.out.println("getSize: " + newSize);
        if (argIndex > -1 && argIndex < newSize) {
            setSelectedItem(objects.get(argIndex));
        } else {
            setSelectedItem(null);
        } // else
    } // setSelectedIndex() method

    /**
     * Use this method to objtain a reference to an object containing the key part of the Item. In the case
     * our combo-box contains a list of Pairs, we will get the first element. In the case it is a list of
     * Object[] arrays, then we use the columns array to get the index of the key.
     * @param argItem 
     * @return 
     */
    public Object getKeyOfAnItem(Object argItem) {
        //DEBUG: System.out.println("getKeyOfAnItem############# argItem: " + argItem);
        if (argItem == null) {
            //DEBUG: System.out.println("return null!!!!!!");
            return null;
        } // if
        if (argItem instanceof Pair) {
            return ((Pair) argItem).getFirst();
        } // if
        if (argItem instanceof Object[]) {
            // we assume whenever we deal with Object[] array, it came from a table model.
            int idx = columns[0];
            Object itemKey = ((Object[]) argItem)[idx];
            if (itemKey instanceof String) {
                //DEBUG: System.out.println("itemKey: " + itemKey);
                String itemKeyString = (String) itemKey;
                //DEBUG: System.out.println("itemKeyString: " + itemKeyString);
                return itemKeyString;
            } // if
            return itemKey;
        } // if
        
        // in any other case we will convert object to String and check if it is a Sise record or not.
        String str = argItem.toString();
        //DEBUG: System.out.println("str.contains(Sise.UNIT_SEPARATOR_STRING): " 
        //        + str.contains(Sise.UNIT_SEPARATOR_STRING));
        if (str.contains(Sise.UNIT_SEPARATOR_STRING)) {
            // we deal with a Sise record
            String[] units = Sise.units(str);
            return units[0];
        } else {
            //DEBUG: System.out.println("getKeyOfAnItem str: " + str);
            // If it is any other String, just simply return it.
            return str;
        } // else        
    } // getKeyOfAnItem() method
    
    /**
     * Use this method to objtain a reference to an object containing the key part of the Item. In the case
     * our combo-box contains a list of Pairs, we will get the first element. In the case it is a list of
     * Object[] arrays, then we use the columns array to get the index of the key.
     * @param argItem 
     * @return 
     */
    public Pair getKeyValuePairOfAnItem(Object argItem) {
        Pair returnKeyValuePair = null;
        if (argItem == null) {
            returnKeyValuePair = null;
        } // if
        if (argItem instanceof Pair) {
            returnKeyValuePair = (Pair) argItem;
        } // if
        if (argItem instanceof Object[]) {
            if (columns.length >= 2) {
                // we assume whenever we deal with Object[] array, it came from a table model.
                int keyIdx = columns[0];
                int valIdx = columns[1];
                Object itemKey = ((Object[]) argItem)[keyIdx];
                Object itemVal = ((Object[]) argItem)[valIdx];
                if (itemKey != null && itemVal != null) {
                    returnKeyValuePair = new Pair(itemKey, itemVal);
                } // if
            } // if
        } // if
        return returnKeyValuePair;
    } // getKeyValueOfAnItem() method
    
    /**
     * Use this method to obtain a key of the selected item.
     * @return 
     */
    public Object getKeyOfTheSelectedItem() {
        //DEBUG: System.out.println("getKeyOfTheSelectedItem()");
        Object selected = getSelectedItem();
        return getKeyOfAnItem(selected);
    } // getKeyOfTheSelectedItem() method
    
    /**
     * Use this method to obtain a key of the selected item.
     * @return 
     */
    public Pair getKeyValuePairOfTheSelectedItem() {
        //DEBUG: System.out.println("getKeyOfTheSelectedItem()");
        Object selected = getSelectedItem();
        return getKeyValuePairOfAnItem(selected);
    } // getKeyValuePairOfTheSelectedItem() method
    
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
    
    /**
     * Use this method when you want to explicitly select the item by its key.
     * 
     * TODO: I hate nested elseifs, make it into some sort of private methods that deal with specific type.
     * 
     * @param argKey 
     */
    public void pickItemByKey(Object argKey) {
        //DEBUG: System.out.println("pickItemByKey: " + argKey);
        Object foundItem = null;
        
        if (argKey == null) {
            setSelectedItem(null);
            setPickedItem(null);
            return;
        } // if
        
        for (Object item : fcbObjects) {
            Object itemKey = null;
            if (item instanceof Pair) {
                Pair pair = (Pair) item;
                itemKey = pair.getKey();
            } else if (item instanceof Object[]) {
                Object[] arr = (Object[]) item;
                itemKey = arr[0];
                //DEBUG: System.out.println("itemKey: " + itemKey);
            } else {
                itemKey = item;
            }
            
            if (argKey.equals(itemKey)) {
                foundItem = item;
            }
        } // foreach
        // Bug fix for the picked Item and Picked key were different.
        if (foundItem != null) {
            setSelectedItem(foundItem);         
            setPickedKey(argKey);
            setPickedItem(foundItem);
        } // if
    } // setSelectedItemByKey() method
    
    public void printDebugInfo() {
        //DEBUG: System.out.println("++++ types ++++++++++++++++++++++++++++++++++++++++++++++++");
        Object[] objs = (Object[]) fcbObjects.get(0);
        for (Object obj : objs) {
            if (obj == null) {
                System.out.println("NULL");
            } else {
                System.out.println(obj.getClass().getCanonicalName());
            }
        }
        //DEBUG: System.out.println("++++ fcbObjects ++++++++++++++++++++++++++++++++++++++++++++++++");
        for (Object row : fcbObjects) {
            Object[] tmp = (Object[]) row;
            System.out.println(Arrays.toString(tmp));
        }
        //DEBUG: System.out.println("++++ objects ++++++++++++++++++++++++++++++++++++++++++++++++");
        for (Object row : objects) {
            Object[] tmp = (Object[]) row;
            System.out.println(Arrays.toString(tmp));
        }
    }
    
    public Pair getKeyValuePairForItem(String argItem) {
        Pair retKeyValuePair = null;
        if (fcbObjectsMap != null) {
            Object[] row = (Object[]) fcbObjectsMap.get(argItem);
            if (row != null) {
                if (columns[0] < row.length && columns[1] < row.length) {
                    Object tmpKey = row[columns[0]];
                    Object tmpValue = row[columns[1]];
                    retKeyValuePair = new Pair((String) tmpKey.toString(),
                            (String) tmpValue.toString());
                    return retKeyValuePair;
                } // if
            } // if
        } // if
        
        return retKeyValuePair;
    } // getKeyValuePairForItem() method
    
    /**
     * This method is used to return the ComboBox value at the requested 
     * index as a KeyValue Pair. 
     * In case of atmRegistry this extracts 
     * the key and value from the given table model in atmRegistry.
     * CodePair is cast to Pair.
     * In case where there may not be a key and value, or is not of
     * type Pair or CodePair or atmRegistry, this
     * returns both key and value of the Pair as the same element value. 
     * @param index
     * @return 
     */
    public Pair getKeyValuePairForElementAt(int index) {

        Pair retKeyValuePair = null;

        if (index >= 0 && index < objects.size()) {
            
            Object element = objects.get(index);
            
            if (element != null) {
                
                if (element instanceof CodePair) {

                    retKeyValuePair = (Pair) element;

                } else if (element instanceof Pair) {

                    retKeyValuePair = (Pair) element;

                } else if (tableModelRegistry != null) {
                    
                    if (tableModel != null && columns.length >= 2) {
                        
                        if (index < tableModel.getRowCount()
                                && columns[0] < tableModel.getColumnCount()
                                && columns[1] < tableModel.getColumnCount()) {

                            Object tmpKey = tableModel.getValueAt(index, columns[0]);
                            Object tmpValue = tableModel.getValueAt(index, columns[1]);

                            retKeyValuePair = new Pair((String) tmpKey.toString(),
                                    (String) tmpValue.toString());
                        } // if
                    } // if

                } else {

                    //could be text field
                    retKeyValuePair = new Pair(element, element);

                } // else
            }

        } // if
        return retKeyValuePair;
    } // getKeyValuePairForElementAt() method

    // ====================================================================================================
    // ==== Accessors =====================================================================================
    // ====================================================================================================

    /**
     * 
     * @see config
     */
    public int getConfig() {
        return config;
    }

    /**
     * 
     * @see config
     * @param argConfig 
     */
    public void setConfig(int argConfig) {
        config = argConfig;
    }
    
    /**
     * Use this method to obtain the array of all items (the original list of items).
     * 
     * @return A shallow copy (clone) of the fcbObjects array.
     */
    public ArrayList getFcbObjects() {
        return (ArrayList) fcbObjects.clone();
    } // getFcbObjects() method
    
    /**
     * Use this method to get an array of (model) column indexes that define what columns are included in
     * the combo-box lookup.
     * 
     * NOTE: the first element of the return array is the index of the column which contains the KEY.
     * 
     * @return int[] array.
     */
    public int[] getColumns() {
        return columns;
    } // getColumns() method
    
    /**
     * 
     * @return
     */
    public Object getPickedItem() {
        return pickedItem;
    } // getPickedItem() method
    
    /**
     * 
     * @param argPickedItem
     */
    public void setPickedItem(Object argPickedItem) {
        exactObject = argPickedItem;
        pickedItem = argPickedItem;
    } // setPickedItem() method

    /**
     * 
     * @return
     */
    public Object getPickedKey() {
        return pickedKey;
    } // getPickedKey() method

    /**
     * 
     * @param argPickedKey
     */
    public void setPickedKey(Object argPickedKey) {
        pickedKey = argPickedKey;
    } // setPickedKey() method
    
    /**
     * 
     * @param argReadyToPick
     */
    public void setReadyToPick(boolean argReadyToPick) {
        readyToPick = argReadyToPick;
    } // setReadyToPick() method
    
    /**
     * 
     * @return
     */
    public boolean isReadyToPick() {
        return readyToPick;
    } // isReadyToPick() method
    
    /**
     * 
     * @return
     */
    public boolean isReadyToFinish() {
        return readyToFinish;
    } // isReadyToFinish() method

    /**
     * 
     * @param argReadyToFinish
     */
    public void setReadyToFinish(boolean argReadyToFinish) {
        this.readyToFinish = argReadyToFinish;
    } // setReadyToFinish() method
    
    /**
     * Use this method to obtain the index of the key in a SISE record.
     * 
     * @return 
     */
    public int getKeyIndex() {
        return keyIndex;
    } // getKeyIndex() method

    /**
     * 
     * @return
     */
    public boolean isMultiSelectionAllowed() {
        return multiSelectionAllowed;
    } // isMultiSelectionAllowed() method

    /**
     * 
     * @param argMultiSelectionAllowed
     */
    public void setMultiSelectionAllowed(boolean argMultiSelectionAllowed) {
        multiSelectionAllowed = argMultiSelectionAllowed;
    } // setMultiSelectionAllowed() method
    
    /**
     * 
     * @return
     */
    public boolean isCancelled() {
        return cancelled;
    } // isCancelled() method

    /**
        * 
     * @param argCancelled
     */
    public void setCancelled(boolean argCancelled) {
        cancelled = argCancelled;
    } // setCancelled() method

    /**
     * 
     * @return
     */
    public boolean isAnyPatternAllowed() {
        return anyPatternAllowed;
    } // isAnyPatternAllowed() method

    /**
     * 
     * @param argAnyPatternAllowed
     */
    public void setAnyPatternAllowed(boolean argAnyPatternAllowed) {
        anyPatternAllowed = argAnyPatternAllowed;
    } // setAnyPatternAllowed() method

    /**
     * 
     * @return
     */
    public String getLastPattern() {
        return lastPattern;
    } // getLastPattern() method
    
    /**
     * Use this method to set a reference to an AtmRegistry object
     * 
     * PRE: dataSetID String must be set.
     * 
     * @param argTMRegistry 
     */
    public void setTableModelRegistry(AtmRegistry argTMRegistry) {
        boolean changed = false;
        
        if (tableModelRegistry == null) {
            tableModelRegistry = argTMRegistry;
            changed = true;
        } else {
            if (tableModelRegistry != argTMRegistry) {
                tableModelRegistry = argTMRegistry;
                changed = true;
                
            } // if
        } // else
        
        if (changed) {
            if ((tableModel != null)  && (dataSetID != null) && (!dataSetID.isEmpty())) {
                tableModelRegistry.set(dataSetID, tableModel);
            }
            tableModelRegistry.addPropertyChangeListener(dataSetID, this);
        } // if
    } // setTableModelRegistry() method
    
    public AtmRegistry getTableModelRegistry() {
        return tableModelRegistry;
    }

    /**
     * This method is used to retrieve an indication that the model depends or not on the default 
     * filtering provided.
     * 
     * @return
     */
    public boolean isDefaultFiltering() {
        return defaultFilter;
    } // isDefaultFiltering() method

    /**
     * This method is used to set whether or not the model depends on the default filtering provided.
     * 
     * @param filter
     */
    public void setDefaultFiltering(boolean filter) {
        this.defaultFilter = filter;
    } // setDefaultFiltering() method

    // ====================================================================================================
    // ==== Private Methods ===============================================================================
    // ====================================================================================================
    
    /**
     * This method is used internally to find an item argItem (which is expected to be an array of Objects)
     * in the given ArrayList object `data`.
     * 
     * We use this method to find an object in the objects ArrayList that has an equal key, so we can remove
     * the correct element in the objects ArrayList as well.
     * 
     * @param argItem
     * @param data
     * @return 
     */
    private Object findItem(Object[] argItem, ArrayList data) {
        int ret = -1;
        int cnt = 0;
        for (Object obj : data) {
            Object[] row = (Object[]) obj;
            if (argItem[keyIndex].equals(row[keyIndex])) {
                return obj;
            }
            ++cnt;
        } // foreach
        return null;
    } // findItem() method
    
    /**
     * This method is used internally to delete elements in the fcbObjects (and objects) ArrayLists with
     * indexes between argStart and argEnd.
     * 
     * @param argStart
     * @param argEnd 
     */
    private void removeElements(int argStart, int argEnd) {
        for (int i = argEnd; i >= argStart; i--) {
            Object removedObject = fcbObjects.remove(i);
            Object obj = findItem((Object[]) removedObject, objects);
            if (obj != null) {
                objects.remove(obj);
                String objString = obj.toString();
                if (fcbObjectsMap.get(objString) != null) {
                    fcbObjectsMap.remove(objString);
                } // if
            } // if
        } // for
    } // removeElements() method
    
    private void updateElement(int argIndex, Object argObject) {
        if (isTableModelInUse()) {
            Object updatedObject = fcbObjects.get(argIndex);
            int idx = objects.indexOf(updatedObject);
            if (idx > -1) {
                objects.set(idx, argObject);
            } // if
            String objString = updatedObject.toString();
            if (fcbObjectsMap.get(objString) != null) {
                fcbObjectsMap.remove(objString);
                fcbObjectsMap.put(argObject.toString(), argObject);
            } // if
            fcbObjects.set(argIndex, argObject);
        } else {
            Object updatedObject = fcbObjects.get(argIndex);
            String objString = updatedObject.toString();
            if (fcbObjectsMap.get(objString) != null) {
                fcbObjectsMap.remove(objString);
                fcbObjectsMap.put(argObject.toString(), argObject);
            } // if
            fcbObjects.set(argIndex, argObject);
        }
    } // updateElement
    
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
        } // for
        return row;
    } // getRow() method
    
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
            //DEBUG: System.out.println("check instanceof Object[]");
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
        //DEBUG: System.out.println("argWhat: [" + argWhat + "] left: [" + left + "] right: [" + right + "]");
        if (left == null && right == null) {
            //DEBUG: System.out.println("return 0");
            return 0;
        } // if
        
        if ((left != null && left.toLowerCase().equals(argWhat.toLowerCase()))
                || (right != null && right.toLowerCase().equals(argWhat.toLowerCase()))) {
            // we have an exact match
            //DEBUG: System.out.println("return 2 EXACT");
            return 2;
        } // if
        
        if ((left != null && left.toLowerCase().contains(argWhat.toLowerCase()))
                || (right != null && right.toLowerCase().contains(argWhat.toLowerCase()))) {
            // we found a partial match
            //DEBUG: System.out.println("return 1 PARTIAL");
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
        if (!argWhat.contains("[Ljava.lang.Object")) {
            if (columns.length >= 2) {
                // we assume whenever we deal with Object[] array, it came from a table model.
                int keyIdx = columns[0];
                int valIdx = columns[1];
                Object itemKey = argObjects [keyIdx];
                Object itemVal = argObjects [valIdx];
                String left = itemKey != null ? itemKey.toString() : null;
                String right = itemVal != null ? itemVal.toString() : "";
                //DEBUG: System.out.println("argWhat: [" + argWhat + "] left: [" + left + "] 
                //            right: [" + right + "]");
                if (left == null && right == null) {
                    //DEBUG: System.out.println("return 0");
                    return 0;
                } // if

                if ((left != null && left.toLowerCase().equals(argWhat.toLowerCase()))
                        || (right != null && right.toLowerCase().equals(argWhat.toLowerCase()))) {
                    // we have an exact match
                    //DEBUG: System.out.println("return 2 EXACT");
                    return 2;
                } // if

                if ((left != null && left.toLowerCase().contains(argWhat.toLowerCase()))
                        || (right != null && right.toLowerCase().contains(argWhat.toLowerCase()))) {
                    // we found a partial match
                    //DEBUG: System.out.println("return 1 PARTIAL");
                    return 1;
                } // if
                
            } // if
            /*for (Object obj : argObjects) {
                str = (obj == null) ? "" : obj.toString();
                //
                System.out.println("argWhat: " + argWhat + " str [" + str + "]");
                if (str.equalsIgnoreCase(argWhat)) {
                    // we found an exact match
                    return 2;
                } // if
                //
                if (str.toLowerCase().contains(argWhat.toLowerCase())) {
                    // we found a partial match
                    return 1;
                } // if
            } // foreach*/
        } else {
            String str = (argObjects == null) ? "" : argObjects.toString();
            //DEBUG: System.out.println("str: [" + str + "]");
            //DEBUG: System.out.println("argWhat: [" + argWhat + "]");
            if (str.equalsIgnoreCase(argWhat)) {
                // we found an exact match
                return 2;
            } // if
        } // else
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
        //DEBUG: System.out.println("String: " + argWhat + " ? " + Sise.record(argObjects));
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

    /**
     * This method is used internally, by the constructors mostly, to initialise fcbObjects and objects
     * ArrayList objects.
     * 
     * PRE-CONDITION:
     *   1) the columns array is expected to be initialised.
     * 
     * @param argAbstractTableModel AbstractTableModel that holds the source of data.
     */
    private void handleNewTableModel(AbstractTableModel argAbstractTableModel) {
        objects = new ArrayList();
        int rowCount = (argAbstractTableModel == null) ? 0 : argAbstractTableModel.getRowCount();
        int initialRowCount = rowCount;
        initialRowCount = (initialRowCount <= 0) ? 4 : initialRowCount;
        objects.ensureCapacity(initialRowCount);
        fcbObjects.ensureCapacity(initialRowCount);

        for (int i = 0; i < rowCount; i++) {
            Object[] row = new Object[tableModel.getColumnCount()];
            
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                row[j] = tableModel.getValueAt(i, j);
            } // for
            fcbObjects.add(row);
            fcbObjectsMap.put(row.toString(), row);
            objects.add(row);
        } // for

        keyIndex = columns[0]; // by convention the first element in the columns array is the index of the key
        
        if (getSize() > 0) {
            setSelectedItem(getElementAt(0));
        } // if

        if (tableModel != null) {
            tableModel.addTableModelListener(this);
        }
    } // handleNewTableModel() method

    /**
     * Use this method to find out whether the object anObject matches the pattern.
     * 
     * NOTE: We assume that if the pattern is an empty string, everything matches.
     * 
     * Preconditions:
     *   This method expects lastPattern to be set. By default it is an empty string.
     * 
     * @param anObject
     * @return Boolean true if it matches the pattern (or if pattern is empty), false if not.
     */
    private boolean matchesPattern(Object anObject) {
        boolean ret = false;
        
        String copy = lastPattern;
        
        if (copy.isEmpty() || (patternParts == null)) {
            ret = true;
        } else {            
            boolean found = true;
            int val = 0;
            for (String str : patternParts) {
                val = check(str, anObject);
                found &= ((val == 1) || (val == 2));
            } // foreach
            ret = found;
        } // else
        
        return ret;
    } // matchesPattern() method

} // FilteredComboBoxModel class

// $Id$
