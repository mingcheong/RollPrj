/**
 * @# FieldSelection.java    <文件名>
 */
package gov.nbcs.rp.queryreport.rowset.ui;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * 功能说明:
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>

 */
public class ObjectSelection implements Transferable, ClipboardOwner {

	public static final int STRING = 0;

	public  static final int OBJECT = 1;

	public static final DataFlavor objectDataFlavor = new DataFlavor(
			Object.class, "otherObject");

	private static final DataFlavor[] flavors = { DataFlavor.stringFlavor,
			objectDataFlavor // 可以传输字段类
	};

	private Object aObj;

	public ObjectSelection(Object object) {
		this.aObj = object;
	}

	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (flavor.equals(flavors[STRING])) {
			return aObj.toString();
		} else if (flavor.equals(flavors[OBJECT])) {
			return aObj;
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
	}

	public DataFlavor[] getTransferDataFlavors() {

		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for (int i = 0; i < flavors.length; i++) {
			if (flavor.equals(flavors[i])) {
				return true;
			}
		}
		return false;
	}

	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO 自动生成方法存根

	}

}
