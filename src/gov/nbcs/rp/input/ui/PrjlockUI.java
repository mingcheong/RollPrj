package gov.nbcs.rp.input.ui;

/**
 * @author 谢昀荪
 * 
 * @version 创建时间：Aug 10, 20123:06:32 PM
 * 
 * @Description
 */
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.action.OperatorUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.CustomTreeFinder;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.input.action.PrjInputStub;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JSplitPane;

import mappingfiles.sysdb.RpDwkzs;

import org.apache.commons.lang.StringUtils;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.report.systemmanager.glquery.util.UUIDRandom;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;




public class PrjlockUI extends RpModulePanel implements OperatorUI
{

	private static final long serialVersionUID = 1L;

	CustomTree treeEn = null;

	CustomTable ct = null;

	private DataSet dsKzs;
	boolean isOK = false;



	public void initize()
	{
		// TODO Auto-generated method stub
		try
		{
			// 初始化数据
			initData();
			// 添加主面板
			this.add(getBasePanel());
			this.createToolBar();
			this.setButtonState(true);
			setCompEnable(true);
			// 设置初始化界面和按钮状态

		}
		catch (Exception e)
		{
			// 开发时使用 e.printStackTrace();
			ErrorInfo.showErrorDialog(e, "界面初始化失败");
		}
	}


	private void initData() throws Exception
	{

	}


	private void setButtonState(boolean isEditEnable)
	{
		if (this.getToolbarPanel() == null) { return; }
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++)
		{
			FButton btnGet = (FButton) controls.get(i);
			if ("修改".equals(btnGet.getText()))
			{
				btnGet.setEnabled(isEditEnable);
			}
			if ("保存".equals(btnGet.getText()))
			{
				btnGet.setEnabled(!isEditEnable);
			}
			if ("取消".equals(btnGet.getText()))
			{
				btnGet.setEnabled(!isEditEnable);
			}
		}
	}


	private void setCompEnable(boolean isEdit)
	{
		this.treeEn.setEnabled(isEdit);
		this.ct.setEnabled(isEdit);
	}


	/**
	 * 获取主面板
	 * 
	 * @return
	 * @throws Exception
	 */
	public FPanel getBasePanel() throws Exception
	{

		FPanel pnlBase = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(1);
		pnlBase.setLayout(lay);
		FSplitPane pnlInfo = new FSplitPane();
		pnlInfo.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		pnlInfo.setDividerLocation(200);
		FScrollPane pnlTree = new FScrollPane();
		DataSet ds = PubInterfaceStub.getMethod().getDivDataPop(Global.loginYear);
		treeEn = new CustomTree("预算单位", ds, "en_id", "code_name", "parent_id", null, "div_code");
		treeEn.reset();
		treeEn.addMouseListener(new MouseAdapter()
		{

			public void mouseClicked(MouseEvent e)
			{
				if (isOK)
				{
					new MessageBox("未保存控制数，请保存!", MessageBox.MESSAGE, MessageBox.OK).setVisible(true);
					return;
				}
				if (e.getClickCount() == 1)
				{
					if ((treeEn != null) && (treeEn.getSelectedNode() != null) && (treeEn.getSelectedNode() != treeEn.getRoot()))
					{
						try
						{
							refreshprj();
						}
						catch (Exception e1)
						{
							e1.printStackTrace();
						}

					}
				}
			}
		});
		CustomTreeFinder cf = new CustomTreeFinder(treeEn);
		pnlTree.addControl(treeEn);

		FPanel pnlCF = new FPanel();
		RowPreferedLayout layCF = new RowPreferedLayout(1);
		pnlCF.setLayout(layCF);
		pnlCF.addControl(cf, new TableConstraints(1, 1, false, true));
		pnlCF.addControl(pnlTree, new TableConstraints(1, 1, true, true));
		pnlCF.setLeftInset(10);
		pnlCF.setRightInset(10);
		pnlCF.setBottomInset(10);

		String[] columnText = new String[] { "项目编码", "项目名称", "单位编码", "预算单位", "一般预算", "基金预算", "是否锁定" };
		String[] columnField = null;
		String[] editFields = null;

		columnField = new String[] { "xmbm", "xmmc", "div_code", "div_name", "money1", "money2", "is_lock" };
		editFields = new String[] { "money1", "money2" };

		ct = new CustomTable(columnText, columnField, null, true, editFields);

		ct.reset();
		ct.getTable().getTableHeader().setPreferredSize(new Dimension(0, 30));
		ct.getTable().setRowHeight(30);
		ct.getTable().getTableHeader().setBackground(new Color(250, 228, 184));
		ct.getTable().repaint();

		FPanel lPanel = new FPanel();
		RowPreferedLayout lLay = new RowPreferedLayout(1);
		lPanel.setLayout(lLay);

		FLabel fl = new FLabel();

		fl.setTitle("                                                                            单位：万元");
		lPanel.addControl(fl, new TableConstraints(1, 1, false, true));
		lPanel.addControl(ct, new TableConstraints(1, 1, true, true));
		lPanel.setTopInset(10);
		lPanel.setLeftInset(10);
		lPanel.setRightInset(10);
		lPanel.setBottomInset(10);
		pnlInfo.addControl(pnlCF);
		pnlInfo.addControl(lPanel);
		pnlBase.addControl(pnlInfo, new TableConstraints(1, 1, true, true));
		return pnlBase;
	}


	public void doSave()
	{

		ct.getTable().setFocusable(true);
		List listSql = new ArrayList();
		String xmxh = "";
		String set_year = "";
		String rg_code = "";

		int[] rows = ct.getTable().getSelectedRows();
		if (rows == null || rows.length == 0)
		{
			new MessageBox("请至少选择一条记录进行操作!", MessageBox.MESSAGE, MessageBox.OK).setVisible(true);
			return;
		}
		try
		{
			DataSet ds = null;
			for (int i = 0; i < rows.length; i++)
			{
				String bmk = ct.rowToBookmark(rows[i]);
				if (ct.getDataSet().gotoBookmark(bmk))
				{
					ds = ct.getDataSet();
					String m1 = ds.fieldByName("money1").getString();
					String m2 = ds.fieldByName("money2").getString();
					if (!StringUtils.isNotEmpty(m1))
					{
						ErrorInfo.showMessageDialog("请设置一般预算金额!");
						return;
					}
					if (!StringUtils.isNumeric(m1))
					{
						ErrorInfo.showMessageDialog("一般预算金额只能为数字!");
						return;
					}

					if (!StringUtils.isNotEmpty(m2))
					{
						ErrorInfo.showMessageDialog("请设置基金预算金额!");
						return;
					}

					if (!StringUtils.isNumeric(m2))
					{
						ErrorInfo.showMessageDialog("基金预算金额只能为数字!");
						return;
					}
					xmxh = ds.fieldByName("xmxh").getString();
					set_year = ds.fieldByName("set_year").getString();
					rg_code = ds.fieldByName("rg_code").getString();

					// rpDwkzs.setEnable(enable);
					listSql.add("DELETE FROM RP_XMLOCK WHERE  XMXH='" + xmxh + "'");
					StringBuffer sql = new StringBuffer();
					// saveSql.add(rpDwkzs);
					sql.append(" INSERT INTO RP_XMLOCK VALUES('");
					sql.append(xmxh + "','");
					sql.append(set_year + "','");
					sql.append(rg_code + "',");
					sql.append(m1 + "," + m2 + ",'");
					sql.append(Global.getUserName());
					sql.append("',SYSDATE,");
					sql.append("'')");
					listSql.add(sql.toString());
				}
			}

			isOK = false;
			this.treeEn.setEnabled(true);
			QueryStub.getClientQueryTool().executeBatch(listSql);
			new MessageBox("保存成功!", MessageBox.MESSAGE, MessageBox.OK).setVisible(true);
		}
		catch (Exception e1)
		{
			new MessageBox("保存失败!", MessageBox.MESSAGE, MessageBox.OK).setVisible(true);
			return;
		}
		setButtonState(true);
		setCompEnable(true);
	}


	/**
	 * 取消
	 */
	public void doCancel()
	{
		this.treeEn.setEnabled(true);
		if ((treeEn == null) || (treeEn.getSelectedNode() == null)) { return; }
		MyPfNode enPer = (MyPfNode) treeEn.getSelectedNode().getUserObject();
		if ((enPer == null) || (enPer.getValue() == null) || enPer.getValue().equals("")) { return; }

		try
		{
			refreshprj();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setButtonState(true);
		setCompEnable(true);
	}


	public void doImpProject()
	{

		if ((treeEn != null) && (treeEn.getSelectedNode() != null) && (treeEn.getSelectedNode() != null))
		{
			MyPfNode enPer = (MyPfNode) treeEn.getSelectedNode().getUserObject();
			if ((enPer == null) || "".equals(enPer.getValue())) { return; }
			try
			{
				String divCode = enPer.getShowContent().split(" ")[0];

				DataSet ds = PrjInputStub.getMethod().getControlMoney(divCode, Global.loginYear);
				ds.beforeFirst();
				RpDwkzs rpDwkzs = null;
				List listSql = new ArrayList();
				String enCode = null;
				listSql.add("delete from rp_dwkzs where en_code like '" + divCode + "%' and set_year = " + Global.loginYear);
				while (ds.next())
				{
					rpDwkzs = new RpDwkzs();
					enCode = ds.fieldByName("div_code").getString();
					rpDwkzs.setSetYear(Long.valueOf(Global.loginYear));
					rpDwkzs.setDwkzsid(UUIDRandom.generate());

					rpDwkzs.setEnId(ds.fieldByName("en_id").getString());
					if (StringUtils.isEmpty(ds.fieldByName("f2").getString()))
						rpDwkzs.setF2(new Double(0));
					else
						rpDwkzs.setF2(Double.valueOf(ds.fieldByName("f2").getString()));

					if (StringUtils.isEmpty(ds.fieldByName("f3").getString()))
						rpDwkzs.setF3(new Double(0));
					else
						rpDwkzs.setF3(Double.valueOf(ds.fieldByName("f3").getString()));

					rpDwkzs.setRgCode(Global.getCurrRegion());
					rpDwkzs.setLrrDm(Global.getUserId());
					rpDwkzs.setLrrq(new Date());
					rpDwkzs.setXgrDm(Global.getUserId());
					rpDwkzs.setXgrq(new Date());
					rpDwkzs.setEnCode(enCode);
					// rpDwkzs.setEnable(enable);
					StringBuffer sql = new StringBuffer();
					// saveSql.add(rpDwkzs);
					sql.append("insert into rp_dwkzs");
					sql.append("(set_year, dwkzsid, en_id,f1,f2, f3,f6, f7, rg_code, lrr_dm, lrrq, xgr_dm, xgrq, bz, en_code)");
					sql.append(" values(");
					sql.append(rpDwkzs.getSetYear() + ",'");
					sql.append(rpDwkzs.getDwkzsid() + "','");
					sql.append(rpDwkzs.getEnId() + "',");
					sql.append(rpDwkzs.getF1() + ",");
					sql.append(rpDwkzs.getF2() + ",");
					sql.append(rpDwkzs.getF3() + ",");
					sql.append(rpDwkzs.getF6() + ",");
					sql.append(rpDwkzs.getF7() + ",");
					sql.append(rpDwkzs.getRgCode() + ",'");
					sql.append(rpDwkzs.getLrrDm() + "','");
					sql.append(Tools.getCurrDate() + "','");
					sql.append(rpDwkzs.getXgrDm() + "','");
					sql.append(Tools.getCurrDate() + "','");
					sql.append("','");
					sql.append(rpDwkzs.getEnCode() + "'");
					sql.append(")");
					listSql.add(sql.toString());
				}

				QueryStub.getClientQueryTool().executeBatch(listSql);
				ErrorInfo.showMessageDialog("控制树初始化成功！");
				refreshprj();

			}
			catch (Exception e1)
			{
				ErrorInfo.showErrorDialog(e1.getMessage());
			}
		}

	}


	public void doModify()
	{
		// TODO Auto-generated method stub
		try
		{
			dsKzs.edit();
			setButtonState(false);
			setCompEnable(false);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void doDisable()
	{
		if (ct == null)
			return;

		int[] rows = ct.getTable().getSelectedRows();
		if (rows == null || rows.length == 0)
		{
			new MessageBox("请至少选择一条记录进行操作!", MessageBox.MESSAGE, MessageBox.OK).setVisible(true);
			return;
		}
		String enable = "";
		String xmxh = "";
		try
		{
			for (int i = 0; i < rows.length; i++)
			{
				String bmk = ct.rowToBookmark(rows[i]);
				if (ct.getDataSet().gotoBookmark(bmk))
				{
					enable = ct.getDataSet().fieldByName("IS_LOCK").getString();
					if (enable.equals("是"))
					{
						if (ct.getDataSet().fieldByName("LOCK_XMXH") != null)
							xmxh = ct.getDataSet().fieldByName("LOCK_XMXH").getString();
					}
				}
				if (StringUtils.isNotEmpty(xmxh))
				{
					StringBuffer sql = new StringBuffer("UPDATE RP_XMLOCK T SET T.IS_LOCK = 0 WHERE T.XMXH IN ");
					sql.append("('").append(xmxh).append("') AND T.SET_YEAR = ").append(Global.loginYear);
					QueryStub.getClientQueryTool().executeUpdate(sql.toString());
				}
				else
				{
					new MessageBox("未填锁定金额", MessageBox.MESSAGE, MessageBox.OK).setVisible(true);
					return;
				}
			}
		}
		catch (Exception e1)
		{
			new MessageBox("解锁失败!", MessageBox.MESSAGE, MessageBox.OK).setVisible(true);
			return;
		}
		refreshprj();
		new MessageBox("解锁成功!", MessageBox.MESSAGE, MessageBox.OK).setVisible(true);
	}


	public void doEnable()
	{
		if (ct == null)
			return;

		int[] rows = ct.getTable().getSelectedRows();
		if (rows == null || rows.length == 0)
		{
			new MessageBox("请至少选择一条记录进行操作!", MessageBox.MESSAGE, MessageBox.OK).setVisible(true);
			return;
		}
		String enable = "";
		String xmxh = "";
		try
		{
			for (int i = 0; i < rows.length; i++)
			{
				String bmk = ct.rowToBookmark(rows[i]);
				if (ct.getDataSet().gotoBookmark(bmk))
				{
					enable = ct.getDataSet().fieldByName("IS_LOCK").getString();
					if (enable.equals("否"))
					{
						if (ct.getDataSet().fieldByName("XMXH") != null)
						{
							xmxh = ct.getDataSet().fieldByName("XMXH").getString();
							String money1 = ct.getDataSet().fieldByName("money1").getString();
							String money2 = ct.getDataSet().fieldByName("money2").getString();
							if (!StringUtils.isNotEmpty(money1))
							{
								ErrorInfo.showMessageDialog("请设置一般预算金额!");
								return;
							}
							if (!StringUtils.isNumeric(money1))
							{
								ErrorInfo.showMessageDialog("一般预算金额只能为数字!");
								return;
							}

							if (!StringUtils.isNotEmpty(money2))
							{
								ErrorInfo.showMessageDialog("请设置基金预算金额!");
								return;
							}

							if (!StringUtils.isNumeric(money2))
							{
								ErrorInfo.showMessageDialog("基金预算金额只能为数字!");
								return;
							}

							StringBuffer sql = new StringBuffer("UPDATE RP_XMLOCK T SET T.IS_LOCK = 1 WHERE T.XMXH IN ");
							sql.append("('").append(xmxh).append("') AND T.SET_YEAR = ").append(Global.loginYear);
							QueryStub.getClientQueryTool().executeUpdate(sql.toString());

						}
					}
				}
			}
		}
		catch (Exception e1)
		{
			new MessageBox("加锁失败!", MessageBox.MESSAGE, MessageBox.OK).setVisible(true);
			return;
		}
		refreshprj();
		new MessageBox("加锁成功!", MessageBox.MESSAGE, MessageBox.OK).setVisible(true);
	}


	public void refreshprj()
	{
		// TODO 查询
		try
		{
			StringBuffer filter = new StringBuffer();
			if (treeEn.getSelectedNode() != null)
			{
				MyPfNode node = (MyPfNode) treeEn.getSelectedNode().getUserObject();
				if (node == null)
					return;

				filter.append(" and a.set_year = " + Global.loginYear);
				filter.append(" and a.rg_code = " + Global.getCurrRegion());
				DataSet ds = treeEn.getDataSet();

				if (treeEn.getSelectedNode() != treeEn.getRoot())
				{
					if (ds != null && !ds.isEmpty() && !ds.bof() && !ds.eof())
					{
						String divCode = ds.fieldByName("div_code").getString();
						filter.append(" and a.div_code like '" + divCode + "%'");
					}
				}
			}

			String sqlString = "SELECT A.XMXH,B.XMXH LOCK_XMXH,A.RG_CODE,A.SET_YEAR,A.XMBM,A.XMMC,A.DIV_CODE,A.DIV_NAME,"
					+ "B.MONEY1,B.MONEY2,decode(B.IS_LOCK,'1','是','否')  IS_LOCK FROM (SELECT * FROM RP_XMJL A WHERE 1=1" + filter.toString() + ") A" + "　 LEFT JOIN  RP_XMLOCK B ON A.XMXH = B.XMXH ";

			dsKzs = DBSqlExec.client().getDataSet(sqlString);
			ct.setDataSet(dsKzs);
			ct.reset();
			// setTableAllProp();

		}
		catch (Exception ee)
		{
			ErrorInfo.showErrorDialog(ee, "项目刷新出错！");
		}

	}

}
