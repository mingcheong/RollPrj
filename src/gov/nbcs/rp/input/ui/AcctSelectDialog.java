package gov.nbcs.rp.input.ui;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.CustomTreeFinder;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FToolBar;
import com.foundercy.pf.control.PfTreeNode;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;




public class AcctSelectDialog extends FDialog
{

	private static final long serialVersionUID = 1L;

	private CustomTree tree;

	private String[] sValue;

	public boolean isOK = false;

	private String xmxh;



	public AcctSelectDialog()
	{
		super(Global.mainFrame);

		FPanel pnlBase = new FPanel();
		RowPreferedLayout layBase = new RowPreferedLayout(1);
		pnlBase.setLayout(layBase);

		FButton btnOK = new FButton("", "确定");
		btnOK.setIcon("images/rp/save.gif");
		btnOK.setVerticalTextPosition(SwingConstants.BOTTOM);

		FButton btnCancel = new FButton("", "取消");
		btnCancel.setIcon("images/rp/close.gif");
		btnCancel.setVerticalTextPosition(SwingConstants.BOTTOM);

		FToolBar toolbar = new FToolBar();
		FPanel pnlTop = new FPanel();

		toolbar.addControl(btnOK);
		toolbar.addControl(btnCancel);

		RowPreferedLayout layTop = new RowPreferedLayout(1);
		pnlTop.setLayout(layTop);
		layTop.setRowHeight(50);
		pnlTop.addControl(toolbar, new TableConstraints(1, 1, true, true));

		try
		{
			StringBuffer sql = new StringBuffer();
			// if (Common.isNullStr(xmxh)) {
			sql.append("select SET_YEAR,BS_ID,ACCT_CODE,");
			sql.append("'['||ACCT_CODE||']'|| ACCT_NAME as acct_name,");
			sql.append("  substr('                             ',1,");
			sql.append(" ((length(ACCT_CODE) - 3) / 2) * 2)");
			sql.append(" || ACCT_NAME AS SHOWNAME,");
			sql.append(" IS_LEAF,PARENT_ID ");
			sql.append("from vw_fb_acct_func ");
			sql.append(" where set_year = " + Global.loginYear);
			sql.append("order by acct_code");
			// } else {
			// sql.append("select a.SET_YEAR,BS_ID,ACCT_CODE,");
			// sql.append("'['||ACCT_CODE||']'|| ACCT_NAME as acct_name,");
			// sql.append(" substr(' ',1,");
			// sql.append(" ((length(ACCT_CODE) - 3) / 2) * 2)");
			// sql.append(" || ACCT_NAME AS SHOWNAME,");
			// sql.append(" IS_LEAF,PARENT_ID ");
			// sql.append("from vw_fb_acct_func a,rp_xmjl_km b");
			// sql.append(" where a.set_year = "+Global.loginYear);
			// sql.append(" and a.BS_ID = b.kmdm");
			// sql.append(" and a.set_year = b.set_year ");
			// sql.append(" and b.xmxh = '"+xmxh+"'");
			// sql.append("order by acct_code");
			// }
			DataSet ds = DBSqlExec.client().getDataSet(sql.toString());
			tree = new CustomTree("科目列表", ds, "BS_ID", "ACCT_NAME", "PARENT_ID", null, "ACCT_CODE", true);
		}
		catch (Exception e2)
		{
			ErrorInfo.showErrorDialog(e2.getMessage());
		}

		// tree.setIsCheckBoxEnabled(true);
		// System.out.print(tree.getId().toString());
		tree.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 1)
				{

					int row = tree.getRowForLocation(e.getX(), e.getY());
					if (row >= 0)
					{
						TreePath path = tree.getPathForRow(row);
						if (path != null)
						{
							// 对CheckBox的勾选/反勾选操作只在点击CheckBox触发 by qinj at
							// 2010-03-31
							if (e.getX() > tree.getPathBounds(path).x)
							{
								MyTreeNode node = tree.getSelectedNode();
								if (node != null)
								{
									try
									{
										PfTreeNode pNode = (PfTreeNode) node.getUserObject();
										if (pNode.getIsSelect())
											pNode.setIsSelect(false);
										else
											pNode.setIsSelect(true);
									}
									catch (Exception e1)
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}
						}
					}

				}
			}
		});

		CustomTreeFinder cf = new CustomTreeFinder(tree);
		pnlBase.addControl(pnlTop, new TableConstraints(2, 1, false, true));
		pnlBase.addControl(cf, new TableConstraints(1, 1, false, true));
		FScrollPane pnlTree = new FScrollPane();
		pnlTree.addControl(tree);
		pnlBase.addControl(pnlTree, new TableConstraints(10, 1, true, true));
		btnOK.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				isOK = true;
				setVisible(false);
			}
		});
		btnCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				isOK = false;
				setVisible(false);
			}
		});
		pnlBase.setLeftInset(10);
		pnlBase.setRightInset(10);
		pnlBase.setBottomInset(10);
		this.setSize(400, 500);
		this.getContentPane().add(pnlBase);
		this.dispose();
		this.setTitle("科目选择");
		this.setModal(true);
	}


	public void showAcctSelect(String oldValue)
	{
		isOK = false;
		if (!Common.isNullStr(oldValue))
			sValue = oldValue.split(";");
		try
		{
			setTreeSelect();
		}
		catch (Exception ee)
		{
			ErrorInfo.showErrorDialog(ee, "初始化功能科目失败");
		}
	}


	public void showAcctSelect(String xmxh, String oldValue)
	{
		this.xmxh = xmxh;
		if (!Common.isNullStr(oldValue))
			sValue = oldValue.split(";");
		try
		{
			setTreeSelect();
		}
		catch (Exception ee)
		{
			ErrorInfo.showErrorDialog(ee, "初始化功能科目失败");
		}
	}


	private void setTreeSelect() throws Exception
	{

		MyTreeNode root = (MyTreeNode) tree.getRoot(); // 根节点
		Enumeration enumeration = root.breadthFirstEnumeration();
		// 广度优先遍历，枚举变量
		if (sValue != null && sValue.length != 0)
		{
			Arrays.sort(sValue);
			while (enumeration.hasMoreElements())
			{ // 开始遍历
				MyTreeNode node = (MyTreeNode) enumeration.nextElement();
				if (!node.isLeaf())
					continue;
				PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //

				if (pNode == null)
					continue;
				if (Arrays.binarySearch(sValue, pNode.getShowContent()) >= 0)
				{
					pNode.setIsSelect(true);
				}
				else
					pNode.setIsSelect(false);
			}
		}
	}


	public List getTreeSelect()
	{
		List list = new ArrayList();
		MyTreeNode root = (MyTreeNode) tree.getRoot(); // 根节点
		Enumeration enumeration = root.breadthFirstEnumeration(); // 广度优先遍历，枚举变量
		String[] acs = new String[tree.getSelectedNodes(true).length];
		String[] ans = new String[tree.getSelectedNodes(true).length];
		String[] abs = new String[tree.getSelectedNodes(true).length];

		String value = "";
		int i = 0;
		while (enumeration.hasMoreElements())
		{ // 开始遍历
			MyTreeNode node = (MyTreeNode) enumeration.nextElement();
			if (!node.isLeaf())
				continue;
			PfTreeNode pNode = (PfTreeNode) node.getUserObject(); //
			if (pNode.getIsSelect())
			{
				if (i == 0)
				{
					value = pNode.getShowContent();
				}
				else
				{
					value = value + ";" + pNode.getShowContent();
				}
				if (pNode.getShowContent().indexOf("]") < 0)
					continue;
				acs[i] = pNode.getShowContent().substring(1, pNode.getShowContent().indexOf("]"));
				ans[i] = pNode.getShowContent().substring(pNode.getShowContent().indexOf("]") + 1);
				abs[i] = pNode.getValue();
				i++;

			}

			else
			{

			}
		}
		if (i > 5)
		{
			ErrorInfo.showMessageDialog("功能科目过多，请重新选择");
		}
		list.add(0, acs);
		list.add(1, ans);
		list.add(2, value);
		list.add(3, abs);
		return list;
	}
}
