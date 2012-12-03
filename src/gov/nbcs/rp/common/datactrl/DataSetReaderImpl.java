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
 * The Class DataSetReaderImpl. 一个DataSetReader的实现，用于读取非FOR_DELETE状态的记录集
 */
public class DataSetReaderImpl extends DataSetReader {

	/**
	 * Instantiates a new data set wrapper.
	 * 
	 * @param dataSet
	 *            the data set
	 */
	public DataSetReaderImpl(DataSet dataSet) {
		super(dataSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.datactrl.DataSetReader#iterator()
	 */
	public Iterator iterator() {
		return listIterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.datactrl.DataSetReader#listIterator()
	 */
	public ListIterator listIterator() {
		return listIterator(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.common.datactrl.DataSetReader#listIterator(int)
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
			throw new UnsupportedOperationException("not supported");
		}

		/**
		 * 
		 * @see java.util.ListIterator#remove()
		 */
		public void remove() {
			// TODO not implemented
			// listIterator.remove();
			throw new UnsupportedOperationException("not supported");

		}

		/**
		 * @param o
		 * @see java.util.ListIterator#set(java.lang.Object)
		 */
		public void set(Object o) {
			// TODO not implemented
			// listIterator.set(o);
			throw new UnsupportedOperationException("not supported");
		}
	}

}
