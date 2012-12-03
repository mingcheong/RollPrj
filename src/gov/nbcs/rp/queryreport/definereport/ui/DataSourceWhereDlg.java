/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.sys.sysrefcol.ibs.ISysRefCol;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;

/**
 * <p>
 * Title:数据源过滤条件值选择对话框
 * </p>
 * <p>
 * Description:数据源过滤条件值选择对话框
 * </p>
 * <p>

 */
public class DataSourceWhereDlg extends FDialog {

	private static final long serialVersionUID = 1L;

	/* 条件值树 */
	private CustomTree ftreWhereValue = null;

	private Map mapWhereValue = null;

	private String resultValue = null;

	private String whereValue;

	public DataSourceWhereDlg(DataSourceSet dataSourceSet, Map mapWhereValue,
			String whereValue) {
		super(dataSourceSet);
		this.mapWhereValue = mapWhereValue;
		this.whereValue = whereValue;
		this.setSize(500, 375);
		this.setTitle("选择");
		this.setModal(true);
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(DataSourceWhereDlg.this,
					"显示界面发生错误，错误信息：" + e.getMessage(), "提示",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void init() throws Exception {
		ftreWhereValue = new CustomTree("数据", null, "code", "name", null, null,
				"code", true);
		ftreWhereValue.setIsCheckBoxEnabled(true);
		FScrollPane fspnlWhereValue = new FScrollPane(ftreWhereValue);
		// 定义“确定”按钮
		FButton okBtn = new FButton("okBtn", "确定");
		okBtn.addActionListener(new OkActionListener());
		// 定义”取消“按钮
		FButton cancelBtn = new FButton("cancelBtn", "取 消");
		// 实现“取消”按钮点击事件
		cancelBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				DataSourceWhereDlg.this.setVisible(false);
			}
		});

		// 定义按钮面板
		FFlowLayoutPanel btnFpnl = new FFlowLayoutPanel();
		// 设置靠右显示
		btnFpnl.setAlignment(FlowLayout.RIGHT);
		// “确定”按钮加入按钮面板
		btnFpnl.addControl(okBtn);
		// “取消”按钮加入按钮面板
		btnFpnl.addControl(cancelBtn);

		// 定义主面板及布局
		FPanel mainPnl = new FPanel();
		RowPreferedLayout mainRlay = new RowPreferedLayout(1);
		mainRlay.setRowHeight(30);
		mainPnl.setLayout(mainRlay);
		// 数据源表格加入主面板
		mainPnl.add(fspnlWhereValue, new TableConstraints(1, 1, true, true));
		// 按钮面板加入主面板
		mainPnl.add(btnFpnl, new TableConstraints(1, 1, false, true));
		this.getContentPane().add(mainPnl);

		// 显示树信息
		showTreeInfo();
		// 设置树节点为选中状态
		if (!Common.isNullStr(whereValue) && mapWhereValue != null) {
			boolean isDiffer = SubFieldSetDialog.isDiffer(mapWhereValue);
			if (isDiffer) {
				SubFieldSetDialog.setNodeSelect(this.getLvlWithPri(whereValue),
						ftreWhereValue);
			} else {
				SubFieldSetDialog.setNodeSelect(whereValue, ftreWhereValue);
			}
		}
	}

	/**
	 * 主键值和层次码值不同时，根据主键值得层次码值(主键值字符串如:021,022,023)
	 * 
	 * @param sPriValue
	 * @return
	 * @throws Exception
	 */
	private String getLvlWithPri(String sPriValue) throws Exception {
		String sLvlValue = "";
		String[] priArr = sPriValue.split(",");
		int len = priArr.length;
		for (int i = 0; i < len; i++) {
			DataSet dsEnum = (DataSet) mapWhereValue
					.get(IDefineReport.ENUM_DATA);
			String sLvlField = mapWhereValue.get(ISysRefCol.LVL_FIELD)
					.toString();
			String sPriField = mapWhereValue.get(ISysRefCol.PRIMARY_FIELD)
					.toString();
			if (dsEnum.locate(sPriField, priArr[i])) {
				sLvlValue = DefinePubOther.addComma(sLvlValue);
				sLvlValue += dsEnum.fieldByName(sLvlField).getString();
			}
		}
		return sLvlValue;
	}

	/**
	 * 显示树信息
	 * 
	 */
	private void showTreeInfo() {

		if (mapWhereValue == null)
			return;
		String sLvlField = mapWhereValue.get(ISysRefCol.LVL_FIELD).toString();
		// refcol_name
		String sRefcolName = mapWhereValue.get(ISysRefCol.REFCOL_NAME)
				.toString();
		// lvl_style
		String sLvlStyle = mapWhereValue.get(ISysRefCol.LVL_STYLE).toString();
		// 枚举源DataSet
		DataSet dsEnum = (DataSet) mapWhereValue.get(IDefineReport.ENUM_DATA);

		// 设置根名称
		ftreWhereValue.setRootName(sRefcolName);
		// 设置DataSet
		ftreWhereValue.setDataSet(dsEnum);
		// 设置IdName
		ftreWhereValue.setIdName(sLvlField);
		// 设置编码规则
		ftreWhereValue.setCodeRule(SysCodeRule.createClient(sLvlStyle));
		// 设置SortKey
		ftreWhereValue.setSortKey(sLvlField);
		try {
			ftreWhereValue.reset();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(DataSourceWhereDlg.this,
					"查询数据出现错误,错误信息：" + e.getMessage(), "提示",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * 确定按钮点击事件
	 * 
	 */
	private class OkActionListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			String priField = mapWhereValue.get(ISysRefCol.PRIMARY_FIELD)
					.toString();
			boolean isDiffer = SubFieldSetDialog.isDiffer(mapWhereValue);

			try {
				String filterValue = "";
				List lstValue = getWhere(ftreWhereValue, isDiffer, priField);
				if (lstValue == null) {
					JOptionPane.showMessageDialog(DataSourceWhereDlg.this,
							"请选择条件值!", "提示", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				for (Iterator itValue = lstValue.iterator(); itValue.hasNext();) {
					String value = (String) itValue.next();
					filterValue = DefinePubOther.addComma(filterValue);
					filterValue += value;
				}
				resultValue = filterValue;
				DataSourceWhereDlg.this.setVisible(false);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(DataSourceWhereDlg.this,
						"得到数据值发生错误，错误信息：" + e.getMessage(), "提示",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * 遍历树节点，得到查询条件
	 * 
	 * @param customTree树
	 * @param listCode编码列表
	 * @param listName名称列表
	 * @param isDiffer主键和层次码是否同一字段，不同true,相同false
	 * @param priField主键字段
	 * @throws Exception
	 */
	private List getWhere(CustomTree customTree, boolean isDiffer,
			String priField) throws Exception {
		MyTreeNode myTreeNode;
		MyPfNode myPfNode;
		// 判断根节点是否选中
		myTreeNode = (MyTreeNode) customTree.getRoot();
		myPfNode = (MyPfNode) (myTreeNode).getUserObject();
		if (myPfNode.getSelectStat() == MyPfNode.SELECT
				|| myPfNode.getSelectStat() == MyPfNode.UNSELECT) {
			return null;
		}
		List lstValue = new ArrayList();

		getSelectDiv(customTree.getDataSet(), myTreeNode, lstValue, isDiffer,
				priField);
		return lstValue;
	}

	/**
	 * 得到选中节点编码，使用了递归
	 * 
	 * @param myTreeNode树节点
	 * @param lstCode编码列表
	 * @param lstName名称列表
	 * @param isDiffer主键和层次码是否同一字段，不同true,相同false
	 * @param priField主键字段
	 * @throws Exception
	 * @throws Exception
	 */
	private void getSelectDiv(DataSet ds, MyTreeNode myTreeNode, List lstValue,
			boolean isDiffer, String priField) throws Exception {
		int iCount = myTreeNode.getChildCount();
		MyTreeNode myTreeNodeTmp;
		MyPfNode myPfNode;
		for (int i = 0; i < iCount; i++) {
			myTreeNodeTmp = (MyTreeNode) myTreeNode.getChildAt(i);
			myPfNode = (MyPfNode) (myTreeNodeTmp).getUserObject();

			if (myPfNode.getSelectStat() == MyPfNode.UNSELECT)
				continue;
			if (myPfNode.getSelectStat() == MyPfNode.SELECT) {
				// 主键和层次码是否同一字段
				if (isDiffer) {// 不同
					lstValue.add(getLeafValue(ds, myTreeNodeTmp, priField));
				} else {// 相同
					lstValue.add(myPfNode.getValue());
				}
				continue;
			}
			if (myPfNode.getSelectStat() == MyPfNode.PARTSELECT) {
				getSelectDiv(ds, myTreeNodeTmp, lstValue, isDiffer, priField);
			}
		}
	}

	/**
	 * 得到节点值
	 * 
	 * @param ds数据集
	 * @param node节点
	 * @param priField主键字段
	 * @return
	 * @throws Exception
	 */
	private String getLeafValue(DataSet ds, MyTreeNode node, String priField)
			throws Exception {
		String curBookmark = ds.toogleBookmark();
		// 判断是否叶节点
		if (node.isLeaf()) {
			ds.gotoBookmark(node.getBookmark());
			return ds.fieldByName(priField).getString();
		}

		StringBuffer sFileter = new StringBuffer();
		MyTreeNode myTreeNodeTmp;
		Enumeration enume = node.breadthFirstEnumeration();
		while (enume.hasMoreElements()) {
			myTreeNodeTmp = (MyTreeNode) enume.nextElement();
			if (!myTreeNodeTmp.isLeaf())
				continue;
			ds.gotoBookmark(myTreeNodeTmp.getBookmark());
			if (!Common.isNullStr(sFileter.toString())) {
				sFileter.append(",");
			}
			sFileter.append(ds.fieldByName(priField).getString());
		}

		ds.gotoBookmark(curBookmark);
		return sFileter.toString();
	}

	public String getResultValue() {
		return resultValue;
	}

}
