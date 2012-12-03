/**
 * @# DataSourcePanel.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.reportsh.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.reportsh.ibs.IRepDisplay;
import gov.nbcs.rp.queryreport.reportsh.ui.FilterPro.AbastractFilterPanel;
import gov.nbcs.rp.queryreport.reportsh.ui.FilterPro.IFilterProvider;

import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;

/**
 * ����˵��:�ʽ���Դѡ����
 *<P> Copyright 
 * <P>All rights reserved.

 */
public class FundSourcePanel extends FPanel {
	private static final String ref = "1#һ��+2#����+3#����+4#�ļ�+5#�弶";

	private CustomTree treFun;

	private FComboBox cbxLvl;

	private FScrollPane sclFun;

	public FundSourcePanel() {
		try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void init() throws Exception {
		cbxLvl = new FComboBox();
		cbxLvl.setRefModel(ref);
		cbxLvl.setTitle("����ѡ��");
		cbxLvl.setPreferredSize(new Dimension(200, 20));
		cbxLvl.setValue("1");
		cbxLvl.addValueChangeListener(new ValueChangeListener() {

			public void valueChanged(ValueChangeEvent arg0) {
				setTypeChange((String) cbxLvl.getValue());

			}

		});

		this.setLayout(new BorderLayout());
		this.add(cbxLvl, BorderLayout.NORTH);
		DataSet dsFun = AbastractFilterPanel.getSelectDs(
				IFilterProvider.TYPE_FUN, "1");

		treFun = new CustomTree("�ʽ���Դ", dsFun, "lvl_id", "pfs_Name", "par_id",
				null, "lvl_id", true);
		sclFun = new FScrollPane(treFun);
		treFun.setIsCheckBoxEnabled(true);

		this.add(sclFun, BorderLayout.CENTER);

	}

	public String check() {
		if (treFun.getSelectedNodeCount(true) < 1) {
			return "��ѡ���ʽ���Դ";
		}
		return "";
	}

	public List getSelectSource() {
		try {
			List lstSelect = new ArrayList();

			MyTreeNode[] nodes = treFun.getSelectedNodes(true);
			if (nodes == null)
				return null;
			int iCount = nodes.length;
			String book = treFun.getDataSet().toogleBookmark();
			for (int i = 0; i < iCount; i++) {
				String bookTemp = nodes[i].getBookmark();
				treFun.getDataSet().gotoBookmark(bookTemp);
				String field = treFun.getDataSet().fieldByName("lvl_id")
						.getString();
				if (field.equals(IRepDisplay.BUDGET_TOTAL)) {
					treFun.getDataSet().gotoBookmark(book);
					lstSelect.add(IRepDisplay.BUDGET_TOTAL);
					return lstSelect;
				}

				lstSelect.add(field);
			}
			treFun.getDataSet().gotoBookmark(book);
			return lstSelect;
		} catch (Exception e) {
			new MessageBox("ȡ���ʽ���Դʧ��!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			return null;
		}
	}

	public void setTypeChange(String lvl) {
		DataSet ds = AbastractFilterPanel.getSelectDs(
				IFilterProvider.TYPE_FUN, lvl);
		try {

			sclFun.remove(treFun);
			treFun = new CustomTree("�ʽ���Դ", ds, "lvl_id", "pfs_Name", "par_id",
					null, "lvl_id", true);

			if (ds.getRecordCount() < 200)
				treFun.expandAll();
			treFun.setIsCheckBoxEnabled(true);
			sclFun.getViewport().add(treFun);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
