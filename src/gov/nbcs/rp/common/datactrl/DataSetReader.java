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
import java.util.ListIterator;

public abstract class DataSetReader {

	/** The data set. */
	protected DataSet dataSet;

	/**
	 * Instantiates a new data set reader.
	 * 
	 * @param dataSet
	 *            the data set
	 */
	public DataSetReader(DataSet dataSet) {
		super();
		if (dataSet == null) {
			throw new NullPointerException();
		}
		this.dataSet = dataSet;
	}

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
	public abstract Iterator iterator();

	/**
	 * Returns an iterator of the elements in this list (in proper sequence).
	 * This implementation returns <tt>listIterator(0)</tt>.
	 * 
	 * @return an iterator of the elements in this list (in proper sequence).
	 * 
	 * @see #listIterator(int)
	 */
	public abstract ListIterator listIterator();

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
	public abstract ListIterator listIterator(final int index);

	/**
	 * Gets the data set.
	 * 
	 * @return the data set
	 */
	public DataSet getDataSet() {
		return dataSet;
	}

	/**
	 * Sets the data set.
	 * 
	 * @param dataSet
	 *            the new data set
	 */
	public void setDataSet(DataSet dataSet) {
		if (dataSet == null) {
			throw new NullPointerException();
		}
		this.dataSet = dataSet;
	}

	/**
	 * Checks if is empty.
	 * 
	 * @return true, if is empty
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
	 * Gets the record count.
	 * 
	 * @return the record count
	 */
	public int getRecordCount() {
		return dataSet.getRecordCount();
	}

}