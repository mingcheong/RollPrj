/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.datactrl;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.collections.iterators.EmptyListIterator;

/**
 * The Class DataSetOperator.
 *
 * DataSet的操作类，实现线程安全，
 *
 * @author qj
 */
public class DataSetWrapper {

	/** The data set. */
	private DataSet dataSet;

	/**
	 * Instantiates a new data set wrapper.
	 *
	 * @param dataSet
	 *            the data set
	 */
	public DataSetWrapper(DataSet dataSet) {
		super();
		this.dataSet = dataSet;
	}

	/**
	 * Gets the data set.
	 *
	 * @return the dataSet
	 */
	public DataSet getDataSet() {
		return dataSet;
	}

	/**
	 * Sets the data set.
	 *
	 * @param dataSet
	 *            the dataSet to set
	 */
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}

	// Iterators

	/**
	 * Returns an iterator over the elements in this list in proper sequence.
	 * <p>
	 *
	 * This implementation returns a straightforward implementation of the
	 * iterator interface, relying on the backing list's <tt>size()</tt>,
	 * <tt>get(int)</tt>, and <tt>remove(int)</tt> methods.
	 * <p>
	 *
	 * Note that the iterator returned by this method will throw an
	 * <tt>UnsupportedOperationException</tt> in response to its
	 * <tt>remove</tt> method unless the list's <tt>remove(int)</tt> method
	 * is overridden.
	 * <p>
	 *
	 * This implementation can be made to throw runtime exceptions in the face
	 * of concurrent modification, as described in the specification for the
	 * (protected) <tt>modCount</tt> field.
	 *
	 * @return an iterator over the elements in this list in proper sequence.
	 *
	 * @see #modCount
	 */
	public Iterator iterator() {
		return listIterator();
	}

	/**
	 * Returns an iterator of the elements in this list (in proper sequence).
	 * This implementation returns <tt>listIterator(0)</tt>.
	 *
	 * @return an iterator of the elements in this list (in proper sequence).
	 *
	 * @see #listIterator(int)
	 */
	public ListIterator listIterator() {
		return listIterator(0);
	}

	/**
	 * Returns a list iterator of the elements in this list (in proper
	 * sequence), starting at the specified position in the list. The specified
	 * index indicates the first element that would be returned by an initial
	 * call to the <tt>next</tt> method. An initial call to the
	 * <tt>previous</tt> method would return the element with the specified
	 * index minus one.
	 * <p>
	 *
	 * This implementation returns a straightforward implementation of the
	 * <tt>ListIterator</tt> interface that extends the implementation of the
	 * <tt>Iterator</tt> interface returned by the <tt>iterator()</tt>
	 * method. The <tt>ListIterator</tt> implementation relies on the backing
	 * list's <tt>get(int)</tt>, <tt>set(int, Object)</tt>,
	 * <tt>add(int, Object)</tt> and <tt>remove(int)</tt> methods.
	 * <p>
	 *
	 * Note that the list iterator returned by this implementation will throw an
	 * <tt>UnsupportedOperationException</tt> in response to its
	 * <tt>remove</tt>, <tt>set</tt> and <tt>add</tt> methods unless the
	 * list's <tt>remove(int)</tt>, <tt>set(int, Object)</tt>, and
	 * <tt>add(int, Object)</tt> methods are overridden.
	 * <p>
	 *
	 * This implementation can be made to throw runtime exceptions in the face
	 * of concurrent modification, as described in the specification for the
	 * (protected) <tt>modCount</tt> field.
	 *
	 * @param index
	 *            index of the first element to be returned from the list
	 *            iterator (by a call to the <tt>next</tt> method).
	 *
	 * @return a list iterator of the elements in this list (in proper
	 *         sequence), starting at the specified position in the list.
	 *
	 * @throws IndexOutOfBoundsException
	 *             if the specified index is out of range (<tt>index &lt; 0 || index &gt; size()</tt>).
	 *
	 * @see #modCount
	 */
	public ListIterator listIterator(final int index) {
		if (dataSet != null) {
			if ((index < 0) || (index > dataSet.getRecordCount())) {
				throw new IndexOutOfBoundsException("Index: " + index);
			}
			if ((dataSet != null) && (dataSet.getDataCache() != null)) {
				List dataCache = dataSet.getDataCache();
				return new ListItr(dataCache.listIterator(index));
			} else {
				return EmptyListIterator.INSTANCE;
			}
		} else {
			return EmptyListIterator.INSTANCE;
		}
	}

	private class ListItr implements ListIterator {
		int cursor = 0;

		ListIterator listIterator;

		public ListItr(ListIterator listIterator) {
			this.listIterator = listIterator;
		}

		/**
		 * @return
		 * @see java.util.ListIterator#hasNext()
		 */
		public boolean hasNext() {
			return listIterator.hasNext() && (cursor != dataSet.getRecordCount());
		}

		/**
		 * @return
		 * @see java.util.ListIterator#hasPrevious()
		 */
		public boolean hasPrevious() {
			return listIterator.hasPrevious() && (cursor != 0);
		}

		/**
		 * @return
		 * @see java.util.ListIterator#next()
		 */
		public Object next() {
			Record r;
			do {
				r = (Record) listIterator.next();
			} while (r.getState() == DataSet.FOR_DELETE);
			cursor++;
			return r;
		}

		/**
		 * @return
		 * @see java.util.ListIterator#nextIndex()
		 */
		public int nextIndex() {
			return cursor;
		}

		/**
		 * @return
		 * @see java.util.ListIterator#previous()
		 */
		public Object previous() {
			Record r;
			do {
				r = (Record) listIterator.previous();
			} while (r.getState() == DataSet.FOR_DELETE);
			cursor--;
			return r;
		}

		/**
		 * @return
		 * @see java.util.ListIterator#previousIndex()
		 */
		public int previousIndex() {
			return cursor - 1;
		}

		/**
		 * @param o
		 * @see java.util.ListIterator#add(java.lang.Object)
		 */
		public void add(Object o) {
			// TODO not implemented
			// listIterator.add(o);
			throw new UnsupportedOperationException("not yet implemented");
		}

		/**
		 *
		 * @see java.util.ListIterator#remove()
		 */
		public void remove() {
			// TODO not implemented
			// listIterator.remove();
			throw new UnsupportedOperationException("not yet implemented");

		}

		/**
		 * @param o
		 * @see java.util.ListIterator#set(java.lang.Object)
		 */
		public void set(Object o) {
			// TODO not implemented
			// listIterator.set(o);
			throw new UnsupportedOperationException("not yet implemented");
		}
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if checks if is empty
	 *
	 * @see gov.nbcs.rp.common.datactrl.DataSet#isEmpty()
	 */
	public boolean isEmpty() {
		return (dataSet == null) || (dataSet.getDataCache() == null)
				|| (dataSet.getDataCache().size() == 0) || (getRecordCount() <= 0);
	}

	/**
	 * Gets the state.
	 *
	 * @param record
	 *            the record
	 *
	 * @return the state
	 */
	public int getState(Record record) {
//		return ((Integer) record.get(DataSet.RECORD_STATE_ID)).intValue();
		return record.getState();
	}

	/**
	 * Gets the record count
	 *
	 * 记录数，不计算FOR_DELETE状态的记录
	 *
	 * @return
	 */
	public int getRecordCount() {
		return dataSet.getRecordCount();
	}
}
