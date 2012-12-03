/**
 * @# GeneralFilter.java <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.FilterPro;

import java.awt.BorderLayout;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;

/**
 * 功能说明:通用的条件面板，有年度，批次，数据类型
 * <P>
 * Copyright
 * <P>

 */
public class ProjectFilter extends AbastractFilterPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private FTextField txtPrjName;

	private CustomTree trePrjSort;

	private static final String AllYesNo = "-1#全部+是#是+否#否";


	public boolean isSelect() {
		return !Common.isNullStr((String) txtPrjName.getValue())			
				|| trePrjSort.getSelectedNodeCount(true) > 0;
	}

	public String getFilter(String align) {
		StringBuffer sb = new StringBuffer();

		String name = (String) txtPrjName.getValue();

		String alignDot = Common.isNullStr(align) ? "" : align + ".";

		if (!Common.isNullStr(name)) {
			name.replaceAll("'", "''");
			if (sb.length() != 0)
				sb.append(" and ");
			sb.append(alignDot + "prj_name like '%").append(name).append("%' ");
		}

		if (trePrjSort.getSelectedNodeCount(true) > 0) {
			MyTreeNode[] nodes = trePrjSort.getSelectedNodes(true);
			int iCount = nodes.length;
			StringBuffer sbPrj = new StringBuffer();
			for (int i = 0; i < iCount; i++) {
				sbPrj.append("'").append(nodes[i].sortKeyValue()).append("',");
			}
			if (sb.length() != 0)
				sb.append(" and ");
			sb.append(alignDot + "prjsort_code in (").append(
					sbPrj.substring(0, sbPrj.length() - 1)).append(")");
		}

		

		return sb.toString();
	}

	public void reset() {
		txtPrjName.setValue("");
		trePrjSort.cancelSelected(false);
		trePrjSort.repaint();
	}

	public void init() {
		try {
			this.setTitle("项目条件选择");
			FPanel pnlBack = new FPanel();
			pnlBack.setLayout(new RowPreferedLayout(1));

			txtPrjName = new FTextField();
			txtPrjName.setTitle(" 项目名称");
			txtPrjName.setTitleAdapting(false);
			pnlBack.add(txtPrjName, new TableConstraints(1, 1, true, true));

			pnlBody.add(pnlBack, BorderLayout.NORTH);
			pnlBody.add(new FScrollPane(trePrjSort), BorderLayout.CENTER);
			trePrjSort.setIsCheckBoxEnabled(true);
			trePrjSort.expandAll();
			initSelection();
		} catch (Exception e) {
			new MessageBox("初始化界面失败!", e.getMessage(), MessageBox.ERROR,
					MessageBox.BUTTON_OK).show();
			e.printStackTrace();
		}
	}

	// 设置为当前的数据的选择项
	private void initSelection() {

	}
}
