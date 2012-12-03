/*
 * $Id: SelectPrjDetailPanel.java,v 1.2 2010/06/08 12:33:13 xiexianglong Exp $
 *
 * Copyright 2008 by Founder March 19, Inc. All rights reserved.
 */

package gov.nbcs.rp.sys.prjsort.ui;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.sys.prjsort.ibs.IPrjDetail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;


/**
 * <p>
 * Title:项目明细的选取类
 * </p>

 * </p>
 * 
 * @author 钱自成
 * @version 1.2
 */

public class SelectPrjDetailPanel extends FDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 项目明细树
	private CustomTree treePreDetail;

	// 项目明细列表数据集
	private DataSet ds;

	// 返回的项目明细编码
	private String[] bmkPrjDetail;

	// 主函数
	public SelectPrjDetailPanel(DataSet dsRec, DataSet dsDetail) {
		super(Global.mainFrame); // 指定窗体父亲
		try {
			ds = dsDetail;
			// 创建项目明细树
			treePreDetail = new CustomTree("项目明细", ds, IPrjDetail.DETAIL_ID,
					IPrjDetail.DETAIL_NAME, IPrjDetail.PAR_ID, null);
			treePreDetail.setIsCheckBoxEnabled(true);
			treePreDetail.setIsCheck(true);

			// 遍历TREE
			MyTreeNode root = (MyTreeNode) treePreDetail.getRoot(); // 根节点
			Enumeration enumeration = root.breadthFirstEnumeration(); // 广度优先遍历，枚举变量
			while (enumeration.hasMoreElements()) { // 开始遍历
				MyTreeNode node = (MyTreeNode) enumeration.nextElement();
				if (node != root) { // 过滤掉根节点
					String bk1 = node.getBookmark(); // 获取该节点的BOOKMARK
					ds.gotoBookmark(bk1);
					String sCode = ds.fieldByName(IPrjDetail.DETAIL_CODE)
							.getString();
					if (dsRec.isEmpty()) {
						PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
						pNode.setIsSelect(false); // 设置该节点为选取状态
					}
					dsRec.beforeFirst();
					while (dsRec.next()) {
						if (sCode.equals(dsRec.fieldByName(
								IPrjDetail.DETAIL_CODE).getString())) { // 如果该节点与传递过来的dataset里面有的值相等，则设置选取状态
							PfTreeNode pNode = (PfTreeNode) node
									.getUserObject(); //
							pNode.setIsSelect(true); // 设置该节点为选取状态
						} else {
							PfTreeNode pNode = (PfTreeNode) node
									.getUserObject(); //
							pNode.setIsSelect(false); // 设置该节点为选取状态
						}
					}
				}
			}

			// 界面元素定义
			FPanel pnlBase = new FPanel();
			FPanel pnlUnder = new FPanel(); // 下面放按钮的面板
			FButton btnOK = new FButton();
			btnOK.setText("确定");
			FButton btnCancel = new FButton();
			btnCancel.setText("取消");
			pnlUnder.addControl(btnOK);
			pnlUnder.addControl(btnCancel);
			RowPreferedLayout layBase = new RowPreferedLayout(1);
			pnlBase.setLayout(layBase);
			FScrollPane pnlTree = new FScrollPane();
			pnlTree.addControl(treePreDetail);
			pnlBase.addControl(pnlTree,
					new TableConstraints(14, 1, false, true));

			pnlBase
					.addControl(pnlUnder,
							new TableConstraints(2, 1, true, true));
			this.setSize(300, 400); // 设置窗体大小
			this.setResizable(false); // 设置窗体大小是否可变
			this.getContentPane().add(pnlBase);
			this.dispose(); // 窗体组件自动充满。
			this.setTitle("项目明细选择界面"); // 设置窗体标题
			this.setModal(true); // 设置窗体模态显示

			// 取消按钮的监听事件，关闭当前页面
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						setSelectDetailBMK();
						setVisible(false);
					} catch (Exception ek) {
						ek.printStackTrace();
					}
				}
			});
			// tree的监听事件，返回已选择的节点的信息
			btnOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						setSelectDetailBMK();
						setVisible(false);
					} catch (Exception ek) {
						ek.printStackTrace();
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取当前的明细数据集的标签
	 * @return
	 */
	public String[] getSelectDetailBMK() {
		return bmkPrjDetail;
	}

	private void setSelectDetailBMK() throws Exception {
		ds.beforeFirst();
		MyTreeNode[] nodes = treePreDetail.getSelectedNodes(true);
		MyTreeNode root = (MyTreeNode) treePreDetail.getRoot();
		bmkPrjDetail = new String[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != root) { // 过滤掉根节点
				// 选择该节点的detail_code
				// ,与sort_to_detail相比较,如果相等,则记录sort_detail_code的bmk,保存起来
				bmkPrjDetail[i] = nodes[i].getBookmark();
			}
		}
	}
}