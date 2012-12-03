package gov.nbcs.rp.input.ui;

import gov.nbcs.rp.common.Common;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import org.apache.commons.lang.StringUtils;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.report.systemmanager.glquery.util.UUIDRandom;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;




public class PrjDwkzsMainUI extends RpModulePanel implements OperatorUI
{

	private static final long serialVersionUID = 1L;

	CustomTree treeEn = null;

	CustomTable ct = null;

	private DataSet dsKzs;
	boolean isOK = false;



	public void initize()
	{
		try
		{
			// 添加主面板
			this.add(getBasePanel());
			this.createToolBar();
			this.setButtonState(true);
			setCompEnable(true);

		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, "界面初始化失败" + e.getMessage());
		}
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
					JOptionPane.showMessageDialog(Global.mainFrame, "未保存控制数，请保存!");
					return;
				}
				if (e.getClickCount() == 1)
				{
					if ((treeEn != null) && (treeEn.getSelectedNode() != null) && (treeEn.getSelectedNode() != null))
					{
						MyPfNode enPer = (MyPfNode) treeEn.getSelectedNode().getUserObject();
						if ((enPer == null) || "".equals(enPer.getValue())) { return; }
						if (enPer.getIsLeaf())
						{
							try
							{
								dsKzs = PrjInputStub.getMethod().getDwkzsByEnId(Global.loginYear, Global.getCurrRegion(), enPer.getValue());
								ct.setDataSet(dsKzs);
								ct.reset();
							}
							catch (Exception e1)
							{
								ErrorInfo.showErrorDialog(e1.getMessage());
							}
						}
						else
						{
							Map m = new HashMap();
							try
							{
								String enCode="";
								if(enPer.getValue()==null)
									enCode="";
								else
								{
									m = (Map) PrjInputStub.getMethod().getEnCode(enPer.getValue()).get(0);
									
									 enCode = m.get("chr_code").toString();
								}

								dsKzs = PrjInputStub.getMethod().getDwkzsByEnIdLike(Global.loginYear, Global.getCurrRegion(), enCode);
								ct.setDataSet(dsKzs);
								ct.reset();
							}
							catch (Exception e1)
							{
								ErrorInfo.showErrorDialog(e1.getMessage());
							}

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

		String[] columnText = new String[] { "预算单位", "单位编码", "一般预算", "基金预算", "是否启用", "备注" };
		String[] columnField = null;
		String[] editFields = null;

		columnField = new String[] { "en_name", "en_code", "f2", "f3", "enable", "bz" };
		editFields = new String[] { "f2", "f3", "bz" };

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


	public void reload()
	{
		if ((treeEn != null) && (treeEn.getSelectedNode() != null) && (treeEn.getSelectedNode() != null))
		{
			MyPfNode enPer = (MyPfNode) treeEn.getSelectedNode().getUserObject();
			if ((enPer == null) || "".equals(enPer.getValue())) { return; }
			try
			{
				if (enPer.getIsLeaf())
				{
					dsKzs = PrjInputStub.getMethod().getDwkzsByEnId(Global.loginYear, Global.getCurrRegion(), enPer.getValue());
				}
				else
				{   String enCode="";
					if(treeEn.getSelectedNode()==treeEn.getRoot()) {
						
					}
					else{
						enCode = Common.nonNullStr(((Map) PrjInputStub.getMethod().getEnCode(enPer.getValue()).get(0)).get("chr_code"));
					}
					dsKzs = PrjInputStub.getMethod().getDwkzsByEnIdLike(Global.loginYear, Global.getCurrRegion(), enCode);
				}
				ct.setDataSet(dsKzs);
				ct.reset();
				ct.repaint();
			}
			catch (Exception e1)
			{
				ErrorInfo.showErrorDialog(e1.getMessage());
			}
		}
	}


	public void doSave()
	{
		ct.getTable().setFocusable(true);
		List listSql = new ArrayList();
		try
		{
			for (int i = 0; i < ct.getTable().getRowCount(); i++)
			{
				String bmk = ct.rowToBookmark(i);
				Double f1 = null;
				Double f2 = null;
				Double f3 = null;
				Double f6 = null;
				Double f7 = null;
				String bz = null;

				if (ct.getDataSet().gotoBookmark(bmk))
				{
					// String dwkzsid =
					// ct.getDataSet().fieldByName("dwkzsid").getString();
					String enid = ct.getDataSet().fieldByName("en_id").getString();
					String enCode = ct.getDataSet().fieldByName("en_code").getString();
					if (!"".equals(ct.getDataSet().fieldByName("f2").getString()))
					{
						f2 = Double.valueOf(ct.getDataSet().fieldByName("f2").getString());
					}
					if (!"".equals(ct.getDataSet().fieldByName("f1").getString()))
					{
						f1 = Double.valueOf(ct.getDataSet().fieldByName("f1").getString());
					}
					if (!"".equals(ct.getDataSet().fieldByName("f3").getString()))
					{
						f3 = Double.valueOf(ct.getDataSet().fieldByName("f3").getString());
					}
					if (!"".equals(ct.getDataSet().fieldByName("f6").getString()))
					{
						f6 = Double.valueOf(ct.getDataSet().fieldByName("f6").getString());
					}
					if (!"".equals(ct.getDataSet().fieldByName("f7").getString()))
					{
						f7 = Double.valueOf(ct.getDataSet().fieldByName("f7").getString());
					}

					bz = ct.getDataSet().fieldByName("bz").getString();

					listSql.add("delete from rp_dwkzs where en_code = '" + enCode + "' and set_year = " + Global.loginYear);
					StringBuffer sql = new StringBuffer();
					sql.append("insert into rp_dwkzs");
					sql.append("(set_year, dwkzsid, en_id,f1,f2, f3,f6, f7, rg_code, lrr_dm, lrrq, xgr_dm, xgrq, bz, en_code)");
					sql.append(" values(");
					sql.append(Global.loginYear).append(",'");
					sql.append(UUIDRandom.generate()).append("','");
					sql.append(enid).append("',");
					sql.append(f1).append(",");
					sql.append(f2).append(",");
					sql.append(f3).append(",");
					sql.append(f6).append(",");
					sql.append(f7).append(",");
					sql.append(Global.getCurrRegion()).append(",'");
					sql.append(Global.getUserId()).append("','");
					sql.append(Tools.getCurrDate()).append("','");
					sql.append(Global.getUserId()).append("','");
					sql.append(Tools.getCurrDate()).append("','");
					sql.append(Common.nonNullStr(bz)).append("','");
					sql.append(enCode).append("'");
					sql.append(")");
					listSql.add(sql.toString());
				}
			}
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e.getMessage());
		}

		isOK = false;
		this.treeEn.setEnabled(true);
		try
		{
			QueryStub.getClientQueryTool().executeBatch(listSql);
			JOptionPane.showMessageDialog(Global.mainFrame, "保存成功!");
		}
		catch (Exception e1)
		{
			ErrorInfo.showErrorDialog(e1, "保存失败!");
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
		if (enPer.getIsLeaf())
		{
			try
			{
				dsKzs = PrjInputStub.getMethod().getDwkzsByEnId(Global.loginYear, Global.getCurrRegion(), enPer.getValue());
				ct.setDataSet(dsKzs);
				ct.reset();
				dsKzs.edit();

			}
			catch (Exception e1)
			{
				ErrorInfo.showErrorDialog(e1.getMessage());
			}
		}
		else
		{
			Map m = new HashMap();
			try
			{
				m = (Map) PrjInputStub.getMethod().getEnCode(enPer.getValue()).get(0);
				String enCode = m.get("chr_code").toString();
				dsKzs = PrjInputStub.getMethod().getDwkzsByEnIdLike(Global.loginYear, Global.getCurrRegion(), enCode);
				ct.setDataSet(dsKzs);
				ct.reset();
			}
			catch (Exception e1)
			{
				ErrorInfo.showErrorDialog(e1.getMessage());
			}
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
				if(treeEn.getSelectedNode()==treeEn.getRoot())
					divCode="";
				DataSet ds = PrjInputStub.getMethod().getControlMoney(divCode, Global.loginYear);
				ds.beforeFirst();
				List listSql = new ArrayList();
				String enCode = null;
				
				listSql.add("delete from rp_dwkzs where en_code like '" + divCode + "%' and set_year = " + Global.loginYear);

				while (ds.next())
				{
					enCode = ds.fieldByName("div_code").getString();
					// rpDwkzs.setEnable(enable);
					StringBuffer sql = new StringBuffer();
					// saveSql.add(rpDwkzs);
					sql.append("insert into rp_dwkzs");
					sql.append("(set_year, dwkzsid, en_id,f1,f2, f3,f6, f7, rg_code, lrr_dm, lrrq, xgr_dm, xgrq, bz, en_code)");
					sql.append(" values(");
					sql.append(Global.loginYear).append(",'");
					sql.append(UUIDRandom.generate()).append("','");
					sql.append(ds.fieldByName("en_id").getString()).append("',");
					sql.append("'',");
					if (StringUtils.isEmpty(ds.fieldByName("f2").getString()))
						sql.append("0,");
					else
						sql.append(ds.fieldByName("f2").getString()).append(",");

					if (StringUtils.isEmpty(ds.fieldByName("f3").getString()))
						sql.append("0,");
					else
						sql.append(ds.fieldByName("f3").getString()).append(",");

					sql.append("'','',");
					sql.append(Global.getCurrRegion()).append(",'");
					sql.append(Global.getUserId()).append("','");
					sql.append(Tools.getCurrDate()).append("','");
					sql.append(Global.getUserId()).append("','");
					sql.append(Tools.getCurrDate() + "','");
					sql.append("','");
					sql.append(enCode).append("'");
					sql.append(")");
					listSql.add(sql.toString());
				}
				QueryStub.getClientQueryTool().executeBatch(listSql);
				ErrorInfo.showMessageDialog("控制树初始化成功！");
				reload();
			}
			catch (Exception e1)
			{
				ErrorInfo.showErrorDialog(e1.getMessage());
			}
		}

	}


	public void doModify()
	{
		try
		{
			dsKzs.edit();
			setButtonState(false);
			setCompEnable(false);
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e.getMessage());
		}
	}


	public void doDisable()
	{
		if (ct == null)
			return;

		int[] rows = ct.getTable().getSelectedRows();
		if (rows == null || rows.length == 0)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "请至少选择一条记录进行操作!");
			return;
		}

		String enable;
		String codes = "";
		for (int i = 0; i < rows.length; i++)
		{
			enable = ct.getTable().getValueAt(rows[i], 7).toString();
			if (!enable.equals("否"))
			{
				codes += ct.getTable().getValueAt(rows[i], 2).toString() + ",";
			}
		}
		if (StringUtils.isNotEmpty(codes))
		{
			codes = codes.substring(0, codes.length() - 1);
			StringBuffer sql = new StringBuffer("update rp_dwkzs t set t.enable = 0 where t.en_code in ");
			sql.append("(").append(codes).append(") and t.set_year = ").append(Global.loginYear);
			try
			{
				QueryStub.getClientQueryTool().executeUpdate(sql.toString());
				JOptionPane.showMessageDialog(Global.mainFrame, "停用成功!");
				reload();
			}
			catch (Exception e1)
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "停用失败!");
			}

		}
		else
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "启用失败!");
		}
	}


	public void doEnable()
	{
		if (ct == null)
			return;

		int[] rows = ct.getTable().getSelectedRows();
		if (rows == null || rows.length == 0)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "请至少选择一条记录进行操作!");
			return;
		}

		String enable;
		String codes = "";
		for (int i = 0; i < rows.length; i++)
		{
			enable = ct.getTable().getValueAt(rows[i], 7).toString();
			if (!enable.equals("是"))
			{
				codes += ct.getTable().getValueAt(rows[i], 2).toString() + ",";
			}
		}
		if (StringUtils.isNotEmpty(codes))
		{
			codes = codes.substring(0, codes.length() - 1);
			StringBuffer sql = new StringBuffer("update rp_dwkzs t set t.enable = 1 where t.en_code in ");
			sql.append("(").append(codes).append(") and t.set_year = ").append(Global.loginYear);
			try
			{
				QueryStub.getClientQueryTool().executeUpdate(sql.toString());
				JOptionPane.showMessageDialog(Global.mainFrame, "启用成功!");
				reload();
			}
			catch (Exception e1)
			{
				JOptionPane.showMessageDialog(Global.mainFrame, "启用失败!");
			}
		}
		else
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "启用失败!");
		}

	}
}
