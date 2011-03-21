/**
 * $Id: AreenTableRowSorter.java 753 2010-03-03 14:27:52Z dejan $
 *
 * Copyright (c) 2009-2010 Areen Design Services Ltd
 * 282 King Street; London, W6 0SJ; United Kingdom
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Areen Design Services Ltd ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Areen Design Services Ltd.
 */

package jlib.gui.table.areen;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.DefaultRowSorter;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableStringConverter;


/**
 * This is a straight copy of the TableRowSorter class from Java SE, the only difference
 * is that we have few differences...
 * 1) it overrides setSortKeys()
 * 2) it overrides toggleSortOrder()
 * 3) always keeps a primary sorting column
 * @author dejan
 * @param <M> TableModel subclass.
 */
public class AreenTableRowSorter<M extends TableModel> extends DefaultRowSorter<M, Integer> {
	private int primarySortingColumnIndex = -1;
	private int secondarySortingColumnIndex = 0;

    /**
     * Comparator that uses compareTo on the contents.
     */
    private static final Comparator COMPARABLE_COMPARATOR =
            new ComparableComparator();

    /**
     * Underlying model.
     */
    private M tableModel;

    /**
     * For toString conversions.
     */
    private TableStringConverter stringConverter;


    /**
     * Creates a <code>AreenTableRowSorter</code> with an empty model.
     */
    public AreenTableRowSorter() {
        this(null);
    }

    /**
     * Creates a <code>AreenTableRowSorter</code> using <code>model</code>
     * as the underlying <code>TableModel</code>.
     *
     * @param model the underlying <code>TableModel</code> to use,
     *        <code>null</code> is treated as an empty model
     */
    public AreenTableRowSorter(M model) {
		super();
        setModel(model);
    }

    /**
     * Sets the <code>TableModel</code> to use as the underlying model
     * for this <code>AreenTableRowSorter</code>.  A value of <code>null</code>
     * can be used to set an empty model.
     *
     * @param model the underlying model to use, or <code>null</code>
     */
    public void setModel(M model) {
        tableModel = model;
        setModelWrapper(new AreenTableRowSorterModelWrapper());
    }

    /**
     * Sets the object responsible for converting values from the
     * model to strings.  If non-<code>null</code> this
     * is used to convert any object values, that do not have a
     * registered <code>Comparator</code>, to strings.
     *
     * @param stringConverter the object responsible for converting values
     *        from the model to strings
     */
    public void setStringConverter(TableStringConverter stringConverter) {
        this.stringConverter = stringConverter;
    }

    /**
     * Returns the object responsible for converting values from the
     * model to strings.
     *
     * @return object responsible for converting values to strings.
     */
    public TableStringConverter getStringConverter() {
        return stringConverter;
    }

    /**
     * Returns the <code>Comparator</code> for the specified
     * column.  If a <code>Comparator</code> has not been specified using
     * the <code>setComparator</code> method a <code>Comparator</code>
     * will be returned based on the column class
     * (<code>TableModel.getColumnClass</code>) of the specified column.
     * If the column class is <code>String</code>,
     * <code>Collator.getInstance</code> is returned.  If the
     * column class implements <code>Comparable</code> a private
     * <code>Comparator</code> is returned that invokes the
     * <code>compareTo</code> method.  Otherwise
     * <code>Collator.getInstance</code> is returned.
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public Comparator<?> getComparator(int column) {
        Comparator comparator = super.getComparator(column);
        if (comparator != null) {
            return comparator;
        }
        Class columnClass = getModel().getColumnClass(column);
        if (columnClass == String.class) {
            return Collator.getInstance();
        }
        if (Comparable.class.isAssignableFrom(columnClass)) {
            return COMPARABLE_COMPARATOR;
        }
        return Collator.getInstance();
    }

    /**
     * {@inheritDoc}
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    protected boolean useToString(int column) {
        Comparator comparator = super.getComparator(column);
        if (comparator != null) {
            return false;
        }
        Class columnClass = getModel().getColumnClass(column);
        if (columnClass == String.class) {
            return false;
        }
        if (Comparable.class.isAssignableFrom(columnClass)) {
            return false;
        }
        return true;
    }

    /**
     * Implementation of DefaultRowSorter.ModelWrapper that delegates to a
     * TableModel.
     */
    private class AreenTableRowSorterModelWrapper extends ModelWrapper<M,Integer> {
        public M getModel() {
            return tableModel;
        }

        public int getColumnCount() {
            return (tableModel == null) ? 0 : tableModel.getColumnCount();
        }

        public int getRowCount() {
            return (tableModel == null) ? 0 : tableModel.getRowCount();
        }

        public Object getValueAt(int row, int column) {
            return tableModel.getValueAt(row, column);
        }

        public String getStringValueAt(int row, int column) {
            TableStringConverter converter = getStringConverter();
            if (converter != null) {
                // Use the converter
                String value = converter.toString(
                        tableModel, row, column);
                if (value != null) {
                    return value;
                }
                return "";
            }

            // No converter, use getValueAt followed by toString
            Object o = getValueAt(row, column);
            if (o == null) {
                return "";
            }
            String string = o.toString();
            if (string == null) {
                return "";
            }
            return string;
        }

        public Integer getIdentifier(int index) {
            return index;
        }
    }


    private static class ComparableComparator implements Comparator {
        @SuppressWarnings("unchecked")
        public int compare(Object o1, Object o2) {
            return ((Comparable)o1).compareTo(o2);
        }
    }

	/**
	 * We need to fix sorting keys all the time... boring
	 * @param sortKeys
	 * @author Dejan
	 */
	@Override
	public void setSortKeys(List<? extends SortKey> sortKeys) {
		// if we do not have primary sorting column set (default behaviour)
		if (primarySortingColumnIndex == -1) {
			super.setSortKeys(sortKeys);
			return;
		}
		// check if the first key is the key linked to the primary sorting column
		RowSorter.SortKey firstKey = sortKeys.get(0);
		List<RowSorter.SortKey> newKeys = new ArrayList<SortKey>(sortKeys.size());
		if (firstKey.getColumn() == primarySortingColumnIndex) {
			// everything OK, let the superclass take over
			super.setSortKeys(sortKeys);
		} else {
			// trouble, RowSorter borked our primary key, GRRRRRR!
			// we must fix it
			// First, find primary sorting key
			int idx = 0;
			for (RowSorter.SortKey sortKey: sortKeys) {
				if (sortKey.getColumn() == primarySortingColumnIndex) {
					newKeys.add(0, sortKey);
				} else {
					newKeys.add(sortKey);
				} // else
			} // for
			// give superclass a fixed set of keys, so it can continue with sorting
			printSortKeys(newKeys);
			super.setSortKeys(newKeys);
		} // else
	} // setSortKeys() method

    private void checkColumn(int column) {
        if (column < 0 || column >= getModelWrapper().getColumnCount()) {
            throw new IndexOutOfBoundsException(
                    "column beyond range of TableModel");
        }
    }

    private SortKey toggle(SortKey key) {
        if (key.getSortOrder() == SortOrder.ASCENDING) {
            return new SortKey(key.getColumn(), SortOrder.DESCENDING);
        }
        return new SortKey(key.getColumn(), SortOrder.ASCENDING);
    }

	/**
	 * we must override toggleSortOrder() and fix sort ordering in this derived class,
	 * otherwise DefaultRowSorter won't do a thing...
	 * @param column
	 */
	@Override
	public void toggleSortOrder(int column) {
		// if we do not have primary sorting column set (default behaviour)
		if (primarySortingColumnIndex == -1) {
			super.toggleSortOrder(column);
			return;
		}
        checkColumn(column);
        if (isSortable(column)) {
            List<SortKey> keys = new ArrayList<SortKey>(getSortKeys());
            SortKey sortKey;
            int sortIndex;
            for (sortIndex = keys.size() - 1; sortIndex >= 0; sortIndex--) {
                if (keys.get(sortIndex).getColumn() == column) {
                    break;
                }
            } // for
            if (sortIndex == -1) {
                // Key doesn't exist
                sortKey = new SortKey(column, SortOrder.ASCENDING);
                keys.add(getMaxSortKeys()-1, sortKey);
            } else {
				keys.set(sortIndex, toggle(keys.get(sortIndex)));
			}

			// we need to cut the list in case we have more elements
            if (keys.size() > getMaxSortKeys()) {
                keys = keys.subList(0, getMaxSortKeys());
            }

			// DEBUG
			//System.out.print(sortIndex + "  ");
			//System.out.print(column + "  ");
			//this.printSortKeys(keys);

            super.setSortKeys(keys);
        } // if
	} // toggleSortOrder()

	public void printSortKeys(List<? extends SortKey> sortKeys) {
		System.out.print(sortKeys.size());
		for (RowSorter.SortKey sortKey: sortKeys) {
			System.out.print(" [" + sortKey.getColumn() + " " + sortKey.getSortOrder() + "] ");
		} // for
		System.out.println();
	}

	// _____ ACCESSORS ________________________________________________________

	public int getPrimarySortingColumnIndex() {
		return primarySortingColumnIndex;
	}

	public void setPrimarySortingColumnIndex(int primarySortingColumnIndex) {
		this.primarySortingColumnIndex = primarySortingColumnIndex;
		List<RowSorter.SortKey> newKeys = new ArrayList<SortKey>(getMaxSortKeys());
		newKeys.add(new RowSorter.SortKey(primarySortingColumnIndex, SortOrder.ASCENDING));
		// TODO: what in case the first column is the primary column!?
		newKeys.add(new RowSorter.SortKey(secondarySortingColumnIndex, SortOrder.ASCENDING));
		super.setSortable(primarySortingColumnIndex, true);
		super.setSortable(secondarySortingColumnIndex, true);
		super.setSortKeys(newKeys);
	} // setPrimarySortingColumnIndex()

	public int getSecondarySortingColumnIndex() {
		return secondarySortingColumnIndex;
	}

	public void setSecondarySortingColumnIndex(int secondarySortingColumnIndex) {
		if (primarySortingColumnIndex != -1) {
			this.secondarySortingColumnIndex = secondarySortingColumnIndex;
		}
	} // setSecondarySortingColumnIndex() method
	
} // AreenTableRowSorter class

// $Id: AreenTableRowSorter.java 753 2010-03-03 14:27:52Z dejan $
