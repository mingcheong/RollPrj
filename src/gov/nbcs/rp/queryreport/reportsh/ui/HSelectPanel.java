/**
 * @# TypeSelectPanel.java    <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;

import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.util.Global;

/**
 * 功能说明:类别选择面板，此面板为横向扩展的面板选择
 *<P> Copyright 
 * <P>All rights reserved.

 */
public class HSelectPanel extends FPanel {

	private static final long serialVersionUID = 1L;

	public static String TYPE_ACCT = "ACCT";

	public static String TYPE_ACCT_JJ = "ACCT_JJ";

	public static String TYPE_PRJ = "PRJ";

	public static String LVL_1 = "3";

	public static String LVL_2 = "5";

	public static String LVL_3 = "7";

	Map mapType = new HashMap();

	public static String REF_TYPE = TYPE_ACCT + "#功能科目+" + TYPE_ACCT_JJ
			+ "#经济科目+" + TYPE_PRJ + "#项目分类";

	public static String REF_LEVEL = LVL_1 + "#类+" + LVL_2 + "#款+" + LVL_3
			+ "#项";

	private FComboBox cbxType;

	private FScrollPane spnlTree;

	private FComboBox cbxLvl;

	private CustomTree treType;

	public HSelectPanel() {
		try {
			init();
		} catch (Exception e) {
			new MessageBox("初始化类别失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
	}

	private void init() throws Exception {
		cbxType = new FComboBox();
		cbxType.setTitle("来源类型");
		cbxType.setPreferredSize(new Dimension(100, 20));
		cbxType.setRefModel(REF_TYPE);
		cbxType.addValueChangeListener(new ValueChangeListener() {

			public void valueChanged(ValueChangeEvent arg0) {
				cbxLvl.setEnabled(!arg0.getNewValue().equals(TYPE_PRJ));
				setTypeChange("" + arg0.getNewValue(), "" + cbxLvl.getValue());
			}

		});

		cbxLvl = new FComboBox();
		cbxLvl.setTitle("级次选择");
		cbxLvl.setRefModel(REF_LEVEL);
		cbxLvl.setValue(LVL_1);
		cbxLvl.setPreferredSize(new Dimension(100, 20));
		cbxType.setValue(TYPE_ACCT);
		cbxLvl.addValueChangeListener(new ValueChangeListener() {
			public void valueChanged(ValueChangeEvent arg0) {
				setTypeChange("" + cbxType.getValue(), "" + arg0.getNewValue());
			}
		});

		treType = new CustomTree();
		spnlTree = new FScrollPane(treType);

		FPanel pnlCon = new FPanel();
		pnlCon.setLayout(new BorderLayout());
		FPanel pnlTop = new FPanel();
		pnlTop.setLayout(new BorderLayout());
		pnlTop.add(cbxType, BorderLayout.NORTH);
		pnlTop.add(cbxLvl, BorderLayout.CENTER);
		pnlTop.setPreferredSize(new Dimension(200, 40));
		pnlCon.add(pnlTop, BorderLayout.WEST);
		JLabel lbl = new JLabel();
		//		lbl.setPreferredSize(new Dimension(40, 50));
		pnlCon.add(lbl, BorderLayout.CENTER);
		this.setLayout(new BorderLayout());
		this.add(pnlCon, BorderLayout.NORTH);
		this.add(spnlTree, BorderLayout.CENTER);

		setTypeChange("" + cbxType.getValue(), "" + cbxLvl.getValue());

	}

	public void setTypeChange(String type, String lvl) {
		try {
			if (type.equals(TYPE_PRJ)) {
				if (treType != null)
					treType.setVisible(false);
			} else {
				spnlTree.remove(treType);
				DataSet ds = getSelectDs(type, lvl);
				if (type.equals(TYPE_ACCT))
					treType = new CustomTree("功能科目", ds, IPubInterface.BS_ID,
							IPubInterface.ACCT_JJ_FNAME,
							IPubInterface.BS_PARENT_ID, null,
							IPubInterface.ACCT_CODE, true);
				else
					treType = new CustomTree("经济科目", ds, IPubInterface.BSI_ID,
							IPubInterface.ACCT_FNAME,
							IPubInterface.BSI_PARENT_ID, null,
							IPubInterface.ACCT_CODE_JJ, true);
				treType.setIsCheckBoxEnabled(true);
				spnlTree.getViewport().add(treType);

			}
		} catch (Exception e) {
			new MessageBox("刷新数据失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();

		}
	}

	private DataSet getSelectDs(String type, String lvl) {
		try {
			String key = type + "_" + lvl;
			if (mapType.containsKey(key)) {
				return (DataSet) mapType.get(key);
			}
			DataSet ds;
			String filter = "set_year=" + Global.getSetYear();
			if (type.equals(TYPE_ACCT)) {
				filter = filter + " and length(acct_code)<=" + lvl;
				ds = PubInterfaceStub.getMethod().getAcctFunc(filter);
			} else {
				filter = filter + " and length(acct_code_JJ)<=" + lvl;
				ds = PubInterfaceStub.getMethod().getAcctEconomy(filter);
			}
			mapType.put(key, ds);

			return ds;
		} catch (Exception e) {
			new MessageBox("取得数据失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
		return null;
	}

	public String check() {
		if (!cbxType.getValue().equals(TYPE_PRJ)) {
			if (treType.getSelectedNodeCount(true) < 1)
				return "请选择科目信息";
			if (treType.getSelectedNodeCount(true) > 300) {
				return "选择的列超过了300列，请选择列数在300列以内！";
			}
		}
		return "";
	}

	public DataSet getSelectDataSet() {
		if (cbxType.getValue().equals(TYPE_PRJ))
			return null;
		MyTreeNode nodes[] = treType.getSelectedNodes(false);
		Map mapBook = new HashMap();
		int iCount = nodes.length;
		for (int i = 0; i < iCount; i++) {
			mapBook.put(nodes[i].getBookmark(), null);
		}
		DataSet typeDs = treType.getDataSet();

		String book = typeDs.toogleBookmark();
		try {
			DataSet ds = DataSet.create();
			typeDs.beforeFirst();
			while (typeDs.next()) {
				if (mapBook.containsKey(typeDs.toogleBookmark())) {
					ds.append();
					ds.setOriginData(typeDs.getOriginData());
				}
			}
			return ds;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				typeDs.gotoBookmark(book);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getSelectType() {
		return "" + cbxType.getValue();
	}

}
