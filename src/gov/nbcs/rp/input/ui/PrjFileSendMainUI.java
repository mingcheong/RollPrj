package gov.nbcs.rp.input.ui;

import gov.nbcs.rp.audit.action.PrjAuditStub;
import gov.nbcs.rp.audit.ui.PrjAuditActionUI;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.action.OperatorUI;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.CustomTreeFinder;
import gov.nbcs.rp.common.ui.tree.MyPfNode;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.input.action.PrjInputDTO;
import gov.nbcs.rp.input.action.PrjInputStub;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import com.foundercy.pf.control.FBlankPanel;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.reportcy.report.op.ReportCore;
import com.foundercy.pf.reportcy.report.ui.ReportDisplayPanel;
import com.foundercy.pf.util.Global;




public class PrjFileSendMainUI extends RpModulePanel implements PrjAuditActionUI,OperatorUI
{

	private static final long serialVersionUID = 1L;

	/** -1 ���״̬ 0:�޸� 1������ */
	private int state = -1;

	CustomTree treeEn = null;

	CustomTable tbPrj = null;

	DataSet dsPrj = null;

	/** ��Ŀ�� */
	CustomTree treePrj = null;

	/** ѡ�񷵻صĹ��ܿ�Ŀ���� */
	private String[] sAcctCodes;

	/** ѡ�񷵻صĹ��ܿ�Ŀ���� */
	private String[] sAcctNames;

	/** ѡ�񷵻صĹ�����ˮ�� */
	private String[] sAcctBsIDs;

	private PrjInputDTO dto = new PrjInputDTO();

	private FComboBox cbQueryType;

	private FTextArea fSbly;

	private TbPrjAuditAffix tpaa;

	// private FLabel f2;

	String moduleid = Global.getModuleId();

	private JFileChooser fileChooser;

	private FTextArea dw_advice;
	private FTextArea cz_advice;

	private FPanel panel_dw_up;
	private FPanel panel_cz_up;



	// private CustomComboBox cbQueryType;// ��Ŀ״̬

	public void initize()
	{
		try
		{
			this.add(getBasePanel());
			this.createToolBar();
			setButtonState(true);
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, "�����ʼ��ʧ��");
		}
	}


	/**
	 * ��ȡ�����
	 * 
	 * @return
	 * @throws Exception
	 */

	private FPanel getBasePanel() throws Exception
	{

		RowPreferedLayout xmLay = new RowPreferedLayout(8);
		FPanel xmxx = new FPanel();
		xmxx.setLayout(xmLay);

		String qsndTmp = "";
		int numQs = Integer.parseInt(GlobalEx.loginYear) - 5;
		for (int i = 0; i <= 5; i++)
		{

			qsndTmp += (numQs + i) + "#" + (numQs + i) + "+";

		}
		if (qsndTmp.length() > 0)
		{
			qsndTmp = qsndTmp.substring(0, qsndTmp.length() - 1);
		}

		String jsndTmp = "";
		int num = Integer.parseInt(GlobalEx.loginYear);
		for (int i = 0; i < 10; i++)
		{
			jsndTmp += (num + i) + "#" + (num + i) + "+";
		}
		if (jsndTmp.length() > 0)
		{
			jsndTmp = jsndTmp.substring(0, jsndTmp.length() - 1);
		}

		List zxzqList = PrjInputStub.getMethod().getDmZxzq(Global.loginYear, Global.getCurrRegion());
		String zxzqTmp = "";
		for (int i = 0; i < zxzqList.size(); i++)
		{
			Map m = new HashMap();
			m = (Map) zxzqList.get(i);
			zxzqTmp += m.get("chr_code") + "#" + m.get("chr_name") + "+";
		}
		if (zxzqTmp.length() > 0)
		{
			zxzqTmp = zxzqTmp.substring(0, zxzqTmp.length() - 1);
		}

		xmxx.setTopInset(10);
		xmxx.setLeftInset(10);
		xmxx.setRightInset(10);
		xmxx.setBottomInset(10);
		tbPrj = new CustomTable(new String[] { "��λ����", "��������", "������", "����ʱ��", "��λȷ��", "����ȷ��" }, new String[] { "chr_code", "name", "user_ver", "last_ver", "DW_SURE", "CZ_SURE" }, null, true,
				new String[] { "user_ver" });

		tbPrj.getTable().addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if ((tbPrj.getDataSet() != null) && !tbPrj.getDataSet().isEmpty())
				{
					if (tbPrj.getTable().getSelectedRow() < 0) { return; }
					try
					{

						if (!tbPrj.getDataSet().gotoBookmark(tbPrj.rowToBookmark(tbPrj.getTable().getSelectedRow()))) { return; }
						String remark = tbPrj.getDataSet().fieldByName("remark").getString();
						String remark_cz = tbPrj.getDataSet().fieldByName("remark_cz").getString();
						cz_advice.setValue(remark_cz.toString());
						dw_advice.setValue(remark.toString());
						String czSure = tbPrj.getDataSet().fieldByName("CZ_SURE").getString();
						if (!GlobalEx.isFisVis())
						{
							List controls = getToolbarPanel().getSubControls();
							if (StringUtils.isNotEmpty(czSure))
							{
								for (int i = 0; i < controls.size(); i++)
								{
									FButton btnGet = (FButton) controls.get(i);
									if ("�޸�".equals(btnGet.getText()))
									{
										btnGet.setEnabled(false);
									}
									if ("ɾ��".equals(btnGet.getText()))
									{
										btnGet.setEnabled(false);
									}
								}
							}
							else
							{
								for (int i = 0; i < controls.size(); i++)
								{
									FButton btnGet = (FButton) controls.get(i);
									if ("�޸�".equals(btnGet.getText()))
									{
										btnGet.setEnabled(true);
									}
									if ("ɾ��".equals(btnGet.getText()))
									{
										btnGet.setEnabled(true);
									}
								}
							}
						}
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
				}
			}
		});
		FPanel pnlBase = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(1);
		pnlBase.setLayout(lay);
		FSplitPane pnlInfo = new FSplitPane();
		pnlInfo.setOrientation(FSplitPane.HORIZONTAL_SPLIT);
		pnlInfo.setDividerLocation(200);
		FScrollPane pnlTree = new FScrollPane();
		DataSet ds = PubInterfaceStub.getMethod().getDivDataPop(GlobalEx.loginYear);
		treeEn = new CustomTree("Ԥ�㵥λ", ds, "en_id", "code_name", "parent_id", null, "div_code");
		treeEn.reset();
		setTableAllProp();
		treeEn.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 1)
				{
					refreshPrjData();
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
		pnlCF.setTopInset(10);
		pnlCF.setLeftInset(10);
		pnlCF.setRightInset(10);
		pnlCF.setBottomInset(10);

		FPanel pnlXM = new FPanel();
		RowPreferedLayout layXM = new RowPreferedLayout(6);
		pnlXM.setLayout(layXM);

		FSplitPane pnlff = new FSplitPane();
		pnlff.setOrientation(FSplitPane.VERTICAL_SPLIT);
		pnlff.setDividerLocation(200);

		pnlff.addControl(xmxx);
		pnlXM.addControl(getQueryType(), new TableConstraints(1, 1, false, false));
		pnlXM.addControl(tbPrj, new TableConstraints(15, 6, false, false));

		dw_advice = new FTextArea();
		dw_advice.setEnabled(false);
		panel_dw_up = new FPanel();
		RowPreferedLayout rp = new RowPreferedLayout(1);
		rp.setColumnWidth(15);
		rp.setColumnGap(6);
		panel_dw_up.setLayout(rp);
		panel_dw_up.setTitle("��λ���:");
		// ���ؼ���˳��ڷŽ����
		panel_dw_up.addControl(dw_advice, new TableConstraints(6, 1, false, true));

		cz_advice = new FTextArea();
		panel_cz_up = new FPanel();
		RowPreferedLayout rp1 = new RowPreferedLayout(1);
		rp1.setColumnWidth(15);
		rp1.setColumnGap(6);
		panel_cz_up.setLayout(rp1);
		panel_cz_up.setTitle("�������:");
		// ���ؼ���˳��ڷŽ����
		panel_cz_up.addControl(cz_advice, new TableConstraints(6, 1, false, true));
		if (GlobalEx.isFisVis())
			dw_advice.setEnabled(false);
		else
		{
			cz_advice.setEnabled(false);
		}
		dw_advice.setProportion(0.2f);
		cz_advice.setProportion(0.2f);
		pnlXM.addControl(new FBlankPanel(), new TableConstraints(1, 6, false, false));

		pnlXM.addControl(panel_dw_up, new TableConstraints(8, 3, false, false));
		pnlXM.addControl(panel_cz_up, new TableConstraints(8, 3, false, false));

		pnlXM.setTopInset(10);
		pnlXM.setLeftInset(10);
		pnlInfo.addControl(pnlCF);
		pnlInfo.addControl(pnlXM);

		pnlInfo.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		pnlBase.addControl(pnlInfo, new TableConstraints(1, 1, true, true));

		return pnlBase;
	}


	private void refreshPrjData()
	{
		if (treeEn.getSelectedNode() != null)
		{
			MyPfNode node = (MyPfNode) treeEn.getSelectedNode().getUserObject();
			if (node.getIsLeaf())
			{
				setButtonState(true);
			}
			else
			{
				setButtonState1(false);
			}
			if (node == null)
				return;
			if (state != -1)
				return;

			if (GlobalEx.isFisVis())
				cz_advice.setEnabled(false);
			else
			{
				dw_advice.setEnabled(false);
			}
			StringBuffer filter = new StringBuffer();
			filter.append(" and t.set_year = " + Global.loginYear);
			filter.append(" and t.rg_code = " + Global.getCurrRegion());
			DataSet ds = treeEn.getDataSet();
			try
			{
				DataSet data = null;
				String flowstatus = "001";// Ĭ��ֵ
				// TODO ��ѯ
				switch (this.cbQueryType.getSelectedIndex())
				{
					case 0:
						// δ����
						flowstatus = "001";
						break;
					case 1:
						// ������
						flowstatus = "002";
						break;

				}
				if (treeEn.getSelectedNode() != treeEn.getRoot())
				{
					if (ds != null && !ds.isEmpty() && !ds.bof() && !ds.eof())
					{
						String divCode = ds.fieldByName("div_code").getString();
						divCode = divCode.substring(0, 3);
						filter.append(" and t.en_id like '" + divCode + "%'");

						data = PrjInputStub.getMethod().getFjFiles(filter.toString(), flowstatus);

					}
				}
				else
				{
					data = PrjInputStub.getMethod().getFjFiles("", flowstatus);
				}
				tbPrj.setDataSet(data);
				tbPrj.reset();
				setTableAllProp();
				// this.setButtonState(true);
			}
			catch (Exception ee)
			{
				ErrorInfo.showErrorDialog(ee, "ˢ�µ�λ��Ϣ����");
				return;
			}
		}

	}


	/**
	 * �����ļ�ѡ����
	 * 
	 * @throws Exception
	 */
	private void createChoose() throws Exception
	{
		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setDialogTitle("ѡ�񸽼�");
		// �趨���õ��ļ��ĺ�׺��
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter()
		{
			public boolean accept(File f)
			{
				String fileName = f.getName();
				if (fileName != null && (fileName.indexOf(".xls") > 0 || fileName.indexOf(".xlsx") > 0 || f.isDirectory())) { return true; }
				return false;
			}


			public String getDescription()
			{
				return "Excel�ļ�(*.xls,*.xlsx)";
			}
		});
	}


	public void doAdd()
	{
		// TODO ���Ӳ���
		try
		{

			MyTreeNode nodeSel = (MyTreeNode) treeEn.getSelectedNode();
			if (nodeSel != null && nodeSel != treeEn.getRoot())
			{
				createChoose();
				int returnval = fileChooser.showOpenDialog(Global.mainFrame);
				if (returnval == JFileChooser.CANCEL_OPTION) { return; }

				File file = fileChooser.getSelectedFile();
				String divCode = treeEn.getDataSet().fieldByName("div_code").getString();

				MyPfNode node = (MyPfNode) nodeSel.getUserObject();
				String path = file.getPath();
				String type = path.substring(path.lastIndexOf("."), path.length());
				DataSet ds = tbPrj.getDataSet();
				// if(ds!=null)
				// ds.fieldByName("name").getValue().toString().lastIndexOf(".",
				// 1);
				divCode = divCode.substring(0, 3);
				int num = DBSqlExec.client().getIntValue("SELECT COUNT(1) FROM RP_FJ_FILES WHERE EN_ID LIKE '" + divCode + "%'");
				ds.append();
				ds.fieldByName("en_id").setValue(divCode);
				ds.fieldByName("chr_code").setValue(node.getShowContent());
				ds.fieldByName("name").setValue(file.getName().substring(0, file.getName().indexOf(".")) + "-" + num + type);
				ds.fieldByName("user_ver").setValue(Global.getUserName());
				// ds.fieldByName("last_ver").setValue(new Date());
				ds.fieldByName("remark").setValue("");
				ds.fieldByName("file").setValue(file);
			}
			else
			{
				JOptionPane.showMessageDialog(null, "��ѡ��Ԥ�㵥λ");
				return;
			}
			this.state = 1;
			cbQueryType.setEnabled(false);
			treeEn.setEnabled(false);
			this.setButtonState(false);
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, "����ʧ��");
		}
	}


	public void doDelete()
	{
		// TODO ɾ������
		int count = tbPrj.getTable().getRowCount();
		int num = 0;
		for (int i = 0; i < count; i++)
		{
			Object check = tbPrj.getTable().getValueAt(i, 0);
			if (((Boolean) check).booleanValue())
			{
				num++;
			}
		}
		if (num == 0)
		{
			JOptionPane.showMessageDialog(null, "��ѡ���걨��Ŀ��¼");
			return;
		}
		try
		{
			String xmxhs = "";
			String names = "";
			for (int i = 0; i < count; i++)
			{
				Object check = tbPrj.getTable().getValueAt(i, 0);
				if (((Boolean) check).booleanValue())
				{
					if (tbPrj.getDataSet().gotoBookmark(tbPrj.rowToBookmark(i)))
					{
						String xmxh = tbPrj.getDataSet().fieldByName("en_id").getString();
						xmxhs += "'" + xmxh + "',";
						names += "'" + tbPrj.getDataSet().fieldByName("name").getString() + "',";
					}
				}
			}
			if (xmxhs.length() > 0)
			{
				xmxhs = xmxhs.substring(0, xmxhs.length() - 1);
				names = names.substring(0, names.length() - 1);
			}
			if (!xmxhs.equals(""))
			{
				if (JOptionPane.showConfirmDialog(Global.mainFrame, "ȷ��ɾ����ѡ��Ŀ��", "��ʾ", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) { return; }
				PrjAuditStub.getMethod().execute("DELETE FROM RP_FJ_FILES WHERE EN_ID in (" + xmxhs + ") and name in (" + names + ")");
				refreshPrjData();
			}
			JOptionPane.showMessageDialog(null, "ɾ���ɹ�");
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "ɾ��ʧ��");
			e.printStackTrace();
		}
	}


	public void doModify()
	{
		if (GlobalEx.isFisVis())
			cz_advice.setEnabled(true);
		else
		{
			dw_advice.setEnabled(true);
		}
		// int count = tbPrj.getTable().getRowCount();
		// int num = 0;
		// for (int i = 0; i < count; i++)
		// {
		// Object check = tbPrj.getTable().getValueAt(i, 0);
		// if (((Boolean) check).booleanValue())
		// {
		// num++;
		// }
		// }
		// if (num > 1)
		// {
		// JOptionPane.showMessageDialog(null, "��ѡ��һ����¼�����޸�");
		// return;
		// }
		// int row = tbPrj.getTable().getSelectedRow();
		// try
		// {
		// if (row >= 0)
		// {
		// String bmk = tbPrj.rowToBookmark(row);
		// if (!tbPrj.getDataSet().gotoBookmark(bmk))
		// {
		// JOptionPane.showMessageDialog(null, "��ѡ���¼");
		// return;
		// }
		// }
		// else
		// {
		// JOptionPane.showMessageDialog(null, "��ѡ���¼");
		// return;
		// }
		// }
		// catch (HeadlessException e1)
		// {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// catch (Exception e1)
		// {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// // PrjInfoDlg.setDoStates(PrjInfoDlg.IS_EDIT);
		// // PrjInfoDlg userInfo = new PrjInfoDlg(this, treeEn, tbPrj, dsPrj);
		// // setSizeAndLocation(userInfo);
		// // userInfo.setVisible(true);
		//

		setButtonState(false);
		// state = 0;
		// try
		// {
		// tbPrj.getDataSet().edit();
		// }
		// catch (Exception e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}


	public void uploadFile(File f, String fileName, String enCode) throws Exception
	{

		// ͨ�������浽������
		InputStream is = new FileInputStream(f);
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		int bit;
		while ((bit = is.read()) != -1)
		{
			data.write(bit);
		}
		// ���渽�������ظ������ڷ�����·��
		PrjAuditStub.getMethod().saveAffixFiles("RP_FJ_FILES", enCode, fileName, Global.loginYear, data.toByteArray(), "EN_PATH");
	}


	public void doSave()
	{
		JTable jt = tbPrj.getTable();
		int count = jt.getRowCount();
		if (count == 0)
			ErrorInfo.showErrorDialog("û�и������Ա��棡");

		try
		{
			String enId = "";
			String name = "";
			String remark = "", remark_cz = "";
			String sql = "";
			File file = null;
			List sqlList = new ArrayList();

			DataSet ds = tbPrj.getDataSet();
			ds.beforeFirst();
			String chrCode = treeEn.getDataSet().fieldByName("code_name").getValue().toString();
			if (GlobalEx.isFisVis())
				remark_cz = cz_advice.getValue().toString();
			else
			{
				remark = dw_advice.getValue().toString();
			}

			while (ds.next())
			{

				enId = ds.fieldByName("en_id").getString();
				name = ds.fieldByName("name").getString();
				// remark = ds.fieldByName("remark").getString();
				file = (File) ds.fieldByName("file").getValue();
				if (state == 1)
				{
					if (file == null)
						continue;
					uploadFile(file, name, enId);
					// sqlList.add("DELETE FROM RP_FJ_FILES WHERE EN_ID = '" +
					// enId + "' and NAME = '" + name + "'");
					sql = "insert into RP_FJ_FILES values ('" + Global.loginYear + "','','" + enId + "','" + chrCode + "','" + name + "','" + remark + "','" + GlobalEx.getCurrRegion()
							+ "',to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'" + Global.getUserName() + "','','','" + remark_cz + "')";
					sqlList.add(sql);
				}
				else
				{
					sql = "update RP_FJ_FILES set remark_cz= '" + remark_cz + "',remark ='" + remark + "' where EN_ID = '" + enId + "' and NAME = '" + name + "'";
					sqlList.add(sql);
				}

			}

			PrjAuditStub.getMethod().executeBatch(sqlList);
			// ����Ϣ���浽���ݿ�
			JOptionPane.showMessageDialog(Global.mainFrame, "�ϴ��ɹ�");
			this.state = -1;
			this.setButtonState(true);
			cbQueryType.setEnabled(true);
			treeEn.setEnabled(true);
			refreshPrjData();
		}
		catch (Exception e1)
		{
			ErrorInfo.showErrorDialog(e1.getMessage());
		}
	}


	private InfoPackage checkData()
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);

		return info;
	}


	private InfoPackage saveData()
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		try
		{

			String SblyStr = fSbly.getValue() == null ? "" : fSbly.getValue().toString();

			if (GlobalEx.isFisVis())
				tbPrj.getDataSet().fieldByName("step_id").setValue("1");
			else
				tbPrj.getDataSet().fieldByName("step_id").setValue("0");
			tbPrj.getDataSet().fieldByName("is_back").setValue("0");
			tbPrj.getDataSet().fieldByName("sb_ly").setValue(SblyStr);
			PrjInputStub.getMethod().savePrjCreateSbInfo(tbPrj.getDataSet(), Global.loginYear, tbPrj.getDataSet().fieldByName("xmxh").getString(), sAcctBsIDs, Global.getCurrRegion());
		}
		catch (Exception ee)
		{
			info.setSuccess(false);
			info.setsMessage("����ʧ�ܣ�������ϢΪ:" + ee.getMessage());
		}
		return info;
	}


	public void doCancel()
	{
		state = -1;
		setButtonState(true);
		cbQueryType.setEnabled(true);
		treeEn.setEnabled(true);
		if (GlobalEx.isFisVis())
			cz_advice.setEnabled(false);
		else
		{
			dw_advice.setEnabled(false);
		}
		try
		{
			tbPrj.getDataSet().cancel();
			tbPrj.getDataSet().applyUpdate();
			refreshPrjData();
		}
		catch (Exception ee)
		{

		}
	}


	public void doImpProject()
	{

	}


	/**
	 * �������ֵ
	 * 
	 * @param bm
	 *            ��Ŀ����
	 * @param mc
	 *            ����
	 * @param qn
	 *            ��ʼ���
	 * @param jn
	 *            �������
	 * @param zq
	 *            ִ������
	 * @param fl
	 *            ��Ŀ����
	 * @param sx
	 *            ��Ŀ����
	 * @param zt
	 *            ��Ŀ״̬
	 * @param km
	 *            ��Ŀ��Ϣ
	 * @throws Exception
	 */
	private void setPanelValue(String bm, String mc, String qn, String jn, String zq, String fl, String sx, String zt, String km, String ly) throws Exception
	{
		//		

		// tfPrjCode.setValue(bm.toString());

		fSbly.setValue(ly);

	}


	public void doPrint()
	{

		int count = tbPrj.getTable().getSelectedRow();

		if (count < 0)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ��һ����¼���д�ӡ");
			return;
		}
		String bmk = tbPrj.rowToBookmark(count);
		String xmxhpt = "";
		try
		{
			if (tbPrj.getDataSet().gotoBookmark(bmk))
			{

				xmxhpt = tbPrj.getDataSet().fieldByName("xmxh").getString();
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		Map mmdate = new HashMap();
		mmdate.put("xmxh", xmxhpt);

		// mmdate.put("prj_name",tfAcct.getValue().toString());

		try
		{
			preview("rp0002", mmdate);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(Global.mainFrame, "��ӡ����");
		}

	}


	public static void preview(String report_id, Map data) throws Exception
	{
		if (null != report_id && null != data)
		{
			ReportCore reportCore = new ReportCore(report_id, data);
			reportCore.execute();
			ReportDisplayPanel reportDisplay = new ReportDisplayPanel(reportCore);
			reportDisplay.repaintNoQueryPanel();

			JDialog aa = new JDialog();
			aa.setSize(800, 600);
			aa.getContentPane().add(reportDisplay);
			aa.setLocationRelativeTo(null);
			aa.setTitle("�걨����ӡ");
			aa.show();

		}
		else
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��������");

		}
	}


	public void doAuditToBack()
	{
		// TODO Auto-generated method stub

	}


	public void doExpExcel()
	{
		if ((tbPrj.getDataSet() != null) && !tbPrj.getDataSet().isEmpty() && !tbPrj.getDataSet().bof() && !tbPrj.getDataSet().eof())
		{}
		else
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ����帽��");
			return;
		}
		try
		{
			String title = tbPrj.getDataSet().fieldByName("name").getString();
			String enId = tbPrj.getDataSet().fieldByName("en_id").getString();
			// �����ı�ѡ����
			JFileChooser chooser = new JFileChooser();
			chooser.setSelectedFile(new File(title));

			int status = chooser.showSaveDialog(Global.mainFrame);
			if (status == JFileChooser.APPROVE_OPTION)
			{
				if (chooser.getSelectedFile() == null)
				{
					JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ���ļ������ַ");
					return;
				}
				// Ŀ���ļ���ַ
				saveAffixFileForXm(enId, title, chooser.getSelectedFile());
				// PrjAuditStub.getMethod().saveAffixFile("rp_xm_audit_affix",
				// tableAffix.getDataSet(),
				// c,Global.loginYear);
				JOptionPane.showMessageDialog(Global.mainFrame, "����ɹ�");
			}

		}
		catch (Exception ee)
		{
			JOptionPane.showMessageDialog(Global.mainFrame, "����ʧ��");
		}

	}


	// ��Ŀ���Ϊ
	public boolean saveAffixFileForXm(String enId, String fileName, File outFile)
	{
		// ����ԭ�ļ������
		FileOutputStream fileOut = null;
		BufferedOutputStream bufOut = null;
		try
		{
			if (outFile.exists())
			{
				outFile.delete();
			}
			fileOut = new FileOutputStream(outFile);
			bufOut = new BufferedOutputStream(fileOut);
			// ������ȡblob��ת��Ϊ�ֽ�,���������
			// int count = 1;
			boolean isSuccessful = true;
			// while (true) {
			// byte[] buf = PrjAuditStub.getMethod().getDocBlob(
			// tableName,
			// count++,
			// dsAffix.fieldByName("row_id")
			// .getString(), Global.loginYear);
			byte[] buf = PrjAuditStub.getMethod().getFileFinds(enId, fileName, Global.loginYear, "EN_PATH", Global.getCurrRegion());
			// if (buf == null) {
			// isSuccessful = false;
			// break;
			// } else {
			bufOut.write(buf);
			// if (buf.length < 1024 * 350) {
			// break;
			// }
			// if (buf.length <= 0) {
			// isSuccessful = false;
			// break;
			// }
			// }
			// }
			/* ���� */
			bufOut.flush();
			/* �ͷ�����Դ */
			// bufOut = null;
			// fileOut = null;
			if (!isSuccessful && outFile.exists())
			{
				outFile.delete();
			}
			if (outFile.length() == 0)
			{
				isSuccessful = false;
				outFile.delete();
			}
			return isSuccessful;
		}
		catch (Exception e)
		{
			return false;
		}
		finally
		{
			/* �ر��� */
			try
			{
				outFile.canRead();
				outFile.canWrite();
				fileOut.close();
				bufOut.close();
				fileOut = null;
				bufOut = null;
			}
			catch (Exception eek)
			{
				return false;
			}
		}
	}


	public void doUpdatedlg()
	{
		// TODO Auto-generated method stub

	}


	public void doExport()
	{
		// TODO Auto-generated method stub

	}


	public void backToAuditback()
	{
		// TODO Auto-generated method stub

	}


	public void doBackAudit()
	{
		// TODO Auto-generated method stub

	}


	public void doBackToDivAudit()
	{
		// TODO Auto-generated method stub

	}


	public void doExprtOtherInfo()
	{
		// TODO Auto-generated method stub

	}


	public void doImportData()
	{
		// TODO Auto-generated method stub

	}


	public void doSendAudit()
	{
		// TODO Auto-generated method stub

	}


	public void doSendTo()
	{
		// TODO Auto-generated method stub

	}


	public void dosearch()
	{
		try
		{
			viewFile(true);
		}
		catch (Exception ee)
		{
			ErrorInfo.showErrorDialog(ee, "�����鿴ʧ��");
		}
	}


	/**
	 * �����ʱ�ļ����зǵ�����ļ�
	 */
	private void delOldFileUnderDir(File f)
	{
		if (f.isDirectory())
		{
			/* �г���Ŀ¼�µ������ļ� */
			File[] entries = f.listFiles();
			int size = entries.length;
			/* ���ɾ�� */
			for (int i = 0; i < size; i++)
			{
				long last = entries[i].lastModified() / 1000 / 3600 / 24;
				long today = System.currentTimeMillis() / 1000 / 3600 / 24;
				if (last < today)
				{
					delFile(entries[i]);
				}
			}
		}
	}


	/**
	 * ɾ���ļ�
	 */
	public void delFile(File f)
	{
		if (f.isFile())
		{// ���ļ�
			f.delete();
		}
		else if (f.isDirectory())
		{// ��Ŀ¼
			/* �г���Ŀ¼�µ������ļ� */
			File[] entries = f.listFiles();
			int size = entries.length;
			/* ���ɾ�� */
			for (int i = 0; i < size; i++)
			{
				if (entries[i].isDirectory())
				{
					delFile(entries[i]);
				}
				else
				{
					entries[i].delete();
				}
			}
		}
	}


	/**
	 * �鿴(ͨ��WINDOWS�ӿڴ�)
	 * 
	 * @throws Exception
	 */
	protected void viewFile(boolean isCanEdit) throws Exception
	{
		int row = tbPrj.getTable().getSelectedRow();

		try
		{
			if (row >= 0)
			{
				String bmk = tbPrj.rowToBookmark(row);
				if (!tbPrj.getDataSet().gotoBookmark(bmk))
				{
					JOptionPane.showMessageDialog(null, "��ѡ���¼");
					return;
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "��ѡ���¼");
				return;
			}
		}
		catch (Exception e)
		{
			ErrorInfo.showMessageDialog(e.toString());
		}

		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try
		{
			// ���ݼ�¼��������ȡ�ļ�����
			// �����ʱ�ļ����е�����ǰ����ʷ�ļ�
			File cleaner = File.createTempFile("temp", ".temp");
			delOldFileUnderDir(cleaner.getParentFile());

			// ������ʱ�ļ�
			String sAddr = (Common.nonNullStr(this.tbPrj.getDataSet().fieldByName("name").getString()));
			String enId = (Common.nonNullStr(this.tbPrj.getDataSet().fieldByName("en_id").getString()));
			int ilastdot = sAddr.lastIndexOf(".");
			String tempFileType = sAddr.substring(ilastdot, sAddr.length());
			File temp = File.createTempFile("temp", tempFileType);

			// �����ļ������
			fos = new FileOutputStream(temp);
			// ���뻺������������Ч��
			bos = new BufferedOutputStream(fos);
			// ������ȡblob��ת��Ϊ�ֽ�,���������
			int count = 1;
			boolean isSuccessful = true;
			while (true)
			{
				// byte[] buf = PrjAuditStub.getMethod().getDocBlob(
				// "rp_xm_audit_affix",
				// count++,
				// this.tableAffix.getDataSet().fieldByName("row_id").getString(),
				// Global.loginYear);
				byte[] buf = PrjAuditStub.getMethod().getFileFinds(enId, sAddr, Global.loginYear, "EN_PATH", Global.getCurrRegion());
				if (buf == null)
				{
					JOptionPane.showMessageDialog(Global.mainFrame, "��ȡ�ļ�ʧ��!", "��Ϣ", JOptionPane.INFORMATION_MESSAGE);
					isSuccessful = false;
					break;
				}
				else
				{
					bos.write(buf);
					if (buf.length < 1024 * 350)
					{
						break;
					}
				}
			}
			bos.flush();
			// ����ֻ��
			if (!isCanEdit)
			{
				temp.setReadOnly();
			}
			// ���ò���ϵͳ�еĹ�����ʽ���ļ�
			if (isSuccessful)
			{
				Runtime rt = Runtime.getRuntime();
				rt.exec("cmd  /c  start  " + temp.getAbsolutePath());
			}
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, "��ȡ�ļ�ʧ�ܣ�");
		}
		finally
		{
			/* �ر��� */
			try
			{
				bos.close();
				fos.close();
			}
			catch (IOException e)
			{
				// ���쳣���ظ��û���������Ӱ�����������
			}
			finally
			{
				bos = null;
				fos = null;
			}
		}
	}


	private FComboBox getQueryType() throws Exception
	{
		cbQueryType = new FComboBox("");
		cbQueryType.setTitle("��Ŀ״̬��");
		cbQueryType.setProportion(0.4f);
		cbQueryType.addValueChangeListener(new ValueChangeListener()
		{
			public void valueChanged(ValueChangeEvent arg0)
			{
				refreshPrjData();;
			}
		});
		setStatusCbx(cbQueryType);
		try
		{
			createToolBar();
		}
		catch (Exception e)
		{

		}
		return cbQueryType;
	}


	private void setTableAllProp() throws Exception
	{
		tbPrj.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tbPrj.reset();
		tbPrj.getTable().getColumnModel().getColumn(1).setPreferredWidth(150);
		tbPrj.getTable().getColumnModel().getColumn(2).setPreferredWidth(200);
		tbPrj.getTable().getColumnModel().getColumn(3).setPreferredWidth(150);
		tbPrj.getTable().getColumnModel().getColumn(4).setPreferredWidth(150);
		tbPrj.getTable().getColumnModel().getColumn(5).setPreferredWidth(80);
		tbPrj.getTable().getColumnModel().getColumn(6).setPreferredWidth(80);
		// tableAll.getTable().getColumnModel().getColumn(5)
		// .setPreferredWidth(250);
		tbPrj.getTable().setRowHeight(30);
		tbPrj.getTable().getTableHeader().setBackground(new Color(250, 228, 184));
		tbPrj.getTable().repaint();

	}


	public void doDisable()
	{
		// TODO Auto-generated method stub
		// int user=0;// 0 dw 1 cz
		String sql = "", name = "";
		if (tbPrj.getTable().getValueAt(0, 2) != null)
			name = tbPrj.getTable().getValueAt(0, 2).toString();
		else
			JOptionPane.showMessageDialog(Global.mainFrame, "���Ÿ���δ����", "��Ϣ", JOptionPane.INFORMATION_MESSAGE);

		if (GlobalEx.isFisVis())
		{
			sql = "UPDATE RP_FJ_FILES SET CZ_SURE=NULL WHERE NAME='" + name + "'";

		}
		else
		{
			sql = "UPDATE RP_FJ_FILES SET DW_SURE=NULL WHERE NAME='" + name + "'";
		}

		try
		{
			QueryStub.getClientQueryTool().executeUpdate(sql);
			refreshPrjData();
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, "ȡ��ȷ�ϳ���");
		}

	}


	public void doEnable()
	{
		// TODO Auto-generated method stub
		String sql = "", name = "";

		if (tbPrj.getTable().getValueAt(0, 2) != null)
			name = tbPrj.getTable().getValueAt(0, 2).toString();
		else
			JOptionPane.showMessageDialog(Global.mainFrame, "���Ÿ���δ����", "��Ϣ", JOptionPane.INFORMATION_MESSAGE);

		if (GlobalEx.isFisVis())
		{
			sql = "UPDATE RP_FJ_FILES SET CZ_SURE =1 WHERE NAME='" + name + "'";

		}
		else
		{
			sql = "UPDATE RP_FJ_FILES SET DW_SURE=1 WHERE NAME='" + name + "'";
		}

		try
		{
			QueryStub.getClientQueryTool().executeUpdate(sql);
			refreshPrjData();
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, "ȡ��ȷ�ϳ���");
		}
	}


	public void setButtonState(boolean isEditState)
	{
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++)
		{
			FButton btnGet = (FButton) controls.get(i);
			if ("����".equals(btnGet.getText()))
			{
				btnGet.setEnabled(isEditState);
			}
			if ("�޸�".equals(btnGet.getText()))
			{
				btnGet.setEnabled(isEditState);
			}
			if ("ɾ��".equals(btnGet.getText()))
			{
				btnGet.setEnabled(isEditState);
			}
			if ("����".equals(btnGet.getText()))
			{
				btnGet.setEnabled(!isEditState);
			}
			if ("ȡ��".equals(btnGet.getText()))
			{
				btnGet.setEnabled(!isEditState);
			}
			boolean ok = true;
			if (state == 1)
				ok = false;
			else
				ok = true;

			if ("����ȷ��".equals(btnGet.getText()))
			{
				btnGet.setEnabled(ok);
			}
			if ("ȡ��ȷ��".equals(btnGet.getText()))
			{
				btnGet.setEnabled(ok);
			}

		}
	}


	public void setButtonState1(boolean isEditState)
	{
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++)
		{
			FButton btnGet = (FButton) controls.get(i);
			if ("����".equals(btnGet.getText()))
			{
				btnGet.setEnabled(isEditState);
			}
			if ("�޸�".equals(btnGet.getText()))
			{
				btnGet.setEnabled(!isEditState);
			}
			if ("ɾ��".equals(btnGet.getText()))
			{
				btnGet.setEnabled(!isEditState);
			}
			if ("����".equals(btnGet.getText()))
			{
				btnGet.setEnabled(isEditState);
			}
			if ("ȡ��".equals(btnGet.getText()))
			{
				btnGet.setEnabled(isEditState);
			}
			boolean ok = true;
			if (state == 1)
				ok = false;
			else
				ok = true;

			if ("����ȷ��".equals(btnGet.getText()))
			{
				btnGet.setEnabled(ok);
			}
			if ("ȡ��ȷ��".equals(btnGet.getText()))
			{
				btnGet.setEnabled(ok);
			}

		}
	}


	public void doBackToAudit()
	{
		// TODO Auto-generated method stub

	}

}
