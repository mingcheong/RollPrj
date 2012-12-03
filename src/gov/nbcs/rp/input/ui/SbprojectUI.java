package gov.nbcs.rp.input.ui;

import gov.nbcs.rp.audit.action.PrjAuditAction;
import gov.nbcs.rp.audit.action.PrjAuditDTO;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.PubInterfaceStub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.CustomTreeFinder;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.input.action.PrjInputDTO;
import gov.nbcs.rp.input.action.PrjInputStub;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.dictionary.control.FTreeAssistInput;
import com.foundercy.pf.report.systemmanager.glquery.util.UUIDRandom;
import com.foundercy.pf.reportcy.report.op.ReportCore;
import com.foundercy.pf.reportcy.report.ui.ReportDisplayPanel;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.XMLData;

public class SbprojectUI extends RpModulePanel implements PrjActionUI {

	protected AcctSelectDialog acctSelect = new AcctSelectDialog();

	private static final long serialVersionUID = 1L;

	/** -1 ���״̬ 0:���� 1���޸� */
	private int state = -1;

	CustomTree treeEn = null;

	CustomTable tbPrj = null;

	DataSet dsPrj = null;

	/** ��Ŀ�� */
	CustomTree treePrj = null;

	/** ��Ŀ���� */
	FTextField tfPrjCode = null; // ��Ŀ����

	/** ��Ŀ���� */
	FTextField tfPrjName = null; // ��Ŀ����

	/** ��ʼ��� */
	FComboBox cbYearBegin = null; // ��ʼ���

	/** ������� */
	FComboBox cbYearEnd = null; // �������

	/** ִ������ */
	FComboBox cbPrjCir = null; // ִ������

	/** ��Ŀ���� */
	FComboBox cbPrjSort = null; // ��Ŀ����

	/** ��Ŀ���� */
	FTreeAssistInput fxmsx = null;// ��Ŀ����

	/** ��Ŀ״̬ */
	FComboBox cbPrjState = null; // ��Ŀ״̬

	/** ��Ŀѡ�� */
	FTextField tfAcct = null; // ��Ŀѡ��

	/** ���ܿ�Ŀѡ��ť */
	FButton btnAcct;

	/** ѡ�񷵻صĹ��ܿ�Ŀ���� */
	private String[] sAcctCodes;

	/** ѡ�񷵻صĹ��ܿ�Ŀ���� */
	private String[] sAcctNames;

	/** ѡ�񷵻صĹ�����ˮ�� */
	private String[] sAcctBsIDs;

	/** ��Ŀ״̬ */
	private List xmztList;

	/** ��Ŀ���� */
	private List xmflList;

	private PrjInputDTO dto = new PrjInputDTO();

	private FComboBox cbQueryType;

	private FTextArea fSbly;

	private TbPrjAuditAffix tpaa;

	FComboBox cbYear = null; // ��ѡ���
	String selectYear = GlobalEx.loginYear;
	// private FLabel f2;

	String moduleid = Global.getModuleId();

	public void initize() {
		try {
			this.add(getBasePanel());
			this.createToolBar();
			setPanelEnable(false);
			setButtonState(true);
		} catch (Exception e) {
			ErrorInfo.showErrorDialog(e, "�����ʼ��ʧ��");
		}
	}

	/**
	 * ��ȡ�����
	 * 
	 * @return
	 * @throws Exception
	 */
	private FPanel getBasePanel() throws Exception {
		RowPreferedLayout xmLay = new RowPreferedLayout(8);
		FPanel xmxx = new FPanel();
		xmxx.setLayout(xmLay);
		tfPrjName = new FTextField("��Ŀ����");
		cbYearBegin = new FComboBox("��ʼ���");
		String qsndTmp = "";
		int numQs = Integer.parseInt(GlobalEx.loginYear) - 5;
		for (int i = 0; i <= 5; i++) {

			qsndTmp += (numQs + i) + "#" + (numQs + i) + "+";

		}
		if (qsndTmp.length() > 0) {
			qsndTmp = qsndTmp.substring(0, qsndTmp.length() - 1);
		}
		cbYearBegin.setRefModel(qsndTmp);
		cbYearBegin.setValue(Global.loginYear);

		cbYearEnd = new FComboBox("�������");
		String jsndTmp = "";
		int num = Integer.parseInt(GlobalEx.loginYear);
		for (int i = 0; i < 10; i++) {
			jsndTmp += (num + i) + "#" + (num + i) + "+";
		}
		if (jsndTmp.length() > 0) {
			jsndTmp = jsndTmp.substring(0, jsndTmp.length() - 1);
		}
		cbYearEnd.setRefModel(jsndTmp);
		cbYearEnd.setValue(Global.loginYear);
		cbPrjCir = new FComboBox("ִ������");
		try {
			List zxzqList = PrjInputStub.getMethod().getDmZxzq(
					Global.loginYear, Global.getCurrRegion());
			String zxzqTmp = "";
			for (int i = 0; i < zxzqList.size(); i++) {
				Map m = new HashMap();
				m = (Map) zxzqList.get(i);
				zxzqTmp += m.get("chr_code") + "#" + m.get("chr_name") + "+";
			}
			if (zxzqTmp.length() > 0) {
				zxzqTmp = zxzqTmp.substring(0, zxzqTmp.length() - 1);
			}
			cbPrjCir.setRefModel(zxzqTmp);
			cbPrjCir.setValue("");
			cbPrjCir.addValueChangeListener(new ValueChangeListener() {
				public void valueChanged(ValueChangeEvent e) {
					if (cbPrjCir.getSelectedIndex() != 2) {
						// ���������ԣ��������ʼ���ֹ���ǵ�ǰ��ȣ������ɱ༭
						cbYearBegin.setSelectedIndex(5);
						cbYearEnd.setSelectedIndex(0);
						cbYearBegin.setEnabled(false);
						cbYearEnd.setEnabled(false);
						dto.setOneYearPrj(true);
					} else {
						// ����������ԣ�����Ա༭
						cbYearBegin.setEnabled(true);
						cbYearEnd.setEnabled(true);
					}
				}
			});
			xmflList = PrjInputStub.getMethod().getDmXmfl(Global.loginYear,
					Global.getCurrRegion());
			String xmflTmp = "";
			for (int i = 0; i < xmflList.size(); i++) {
				Map m = new HashMap();
				m = (Map) xmflList.get(i);
				xmflTmp += m.get("chr_code") + "#" + m.get("chr_name") + "+";
			}
			if (xmflTmp.length() > 0) {
				xmflTmp = xmflTmp.substring(0, xmflTmp.length() - 1);
			}
			cbPrjSort = new FComboBox("��Ŀ���");
			cbPrjSort.setRefModel(xmflTmp);
			cbPrjSort.setValue("");
			List xmsxList = PrjInputStub.getMethod().getDmXmsx(
					Global.loginYear, Global.getCurrRegion());
			fxmsx = new FTreeAssistInput("��Ŀ����");
			XMLData treeData = new XMLData();
			treeData.put("row", xmsxList);
			fxmsx.setData(treeData);
			fxmsx.setIsCheck(true);
			fxmsx.setOnlyLeafCanBeSelected(true);
			fxmsx.addValueChangeListener(new ValueChangeListener() {
				public void valueChanged(ValueChangeEvent e) {
					String[] fxmsxStr = fxmsx.getValue() == null ? null
							: (String[]) fxmsx.getValue();
					if (fxmsxStr == null)
						return;
					String[] fxmsxf1 = new String[fxmsxStr.length];
					String xmsxstr = "";

					if (fxmsxStr != null && fxmsxStr.length > 0) {
						StringBuffer chrIds = new StringBuffer();
						for (int i = 0; i < fxmsxStr.length; i++) {
							chrIds.append("'").append(fxmsxStr[i]).append("',");
						}
						try {
							DataSet ds_fxmsx = DBSqlExec.client().getDataSet(
									"select * from dm_xmsx  where set_year = "
											+ Global.loginYear
											+ " and chr_id in ("
											+ chrIds.substring(0, chrIds
													.length() - 1) + ")");
							ds_fxmsx.getRecordCount();
							ds_fxmsx.beforeFirst();
							int i = 0;
							while (ds_fxmsx.next()) {
								xmsxstr += ds_fxmsx.fieldByName("chr_code")
										.getValue().toString()
										+ " "
										+ ds_fxmsx.fieldByName("chr_name")
												.getValue().toString() + ";";
								fxmsxf1[i] = ds_fxmsx.fieldByName("chr_code1")
										.getValue().toString();
								if (i > 0) {
									if (!fxmsxf1[i].equals(fxmsxf1[i - 1])) {
										JOptionPane.showMessageDialog(
												Global.mainFrame,
												"��Ŀ���Դ����������޸�,�������ͷ������ظ�ѡ��");
										fxmsx.setValue("");
									}
								}
								if (xmsxstr.length() > 0) {
									xmsxstr = xmsxstr.substring(0, xmsxstr
											.length() - 1);
								}
								i++;
							}

						} catch (Exception ex) {
							ErrorInfo.showErrorDialog(ex.getMessage());
						}
					}
				}
			});
			xmztList = PrjInputStub.getMethod().getDmXmzt(Global.loginYear,
					Global.getCurrRegion());
			String xmztTmp = "";
			for (int i = 0; i < xmztList.size(); i++) {
				Map m = new HashMap();
				m = (Map) xmztList.get(i);
				xmztTmp += m.get("chr_code") + "#" + m.get("chr_name") + "+";
			}
			if (xmztTmp.length() > 0) {
				xmztTmp = xmztTmp.substring(0, xmztTmp.length() - 1);
			}
			cbPrjState = new FComboBox("��Ŀ״̬");
			cbPrjState.setRefModel(xmztTmp);
			cbPrjState.setValue("");
			cbPrjState.setEditable(false);
			tfAcct = new FTextField("��Ŀѡ��");
			tfAcct.setProportion(0.081f);
			tfAcct.setEnabled(false);
			tfAcct.setEditable(false);
			btnAcct = new FButton("btnAcct", "ѡ���Ŀ");
			btnAcct.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					acctSelect.showAcctSelect(Common.nonNullStr(tfAcct
							.getValue()));
					Tools.centerWindow(acctSelect);
					acctSelect.setVisible(true);
					if (acctSelect.isOK) {
						List value = acctSelect.getTreeSelect();
						if (value != null && !value.isEmpty()) {
							sAcctCodes = (String[]) value.get(0);
							sAcctNames = (String[]) value.get(1);
							String rvalue = Common.nonNullStr(value.get(2));
							sAcctBsIDs = (String[]) value.get(3);
							tfAcct.setValue(rvalue);
						}
					}
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xmxx.setTopInset(10);
		xmxx.setLeftInset(10);
		xmxx.setRightInset(10);
		xmxx.setBottomInset(10);
		xmxx.addControl(tfPrjName, new TableConstraints(1, 2, false, true));
		xmxx.addControl(cbPrjState, new TableConstraints(1, 2, false, true));
		xmxx.addControl(fxmsx, new TableConstraints(1, 2, false, true));
		xmxx.addControl(cbPrjSort, new TableConstraints(1, 2, false, true));
		xmxx.addControl(cbPrjCir, new TableConstraints(1, 2, false, true));
		xmxx.addControl(cbYearBegin, new TableConstraints(1, 2, false, true));
		xmxx.addControl(cbYearEnd, new TableConstraints(1, 2, false, true));
		xmxx.addControl(tfAcct, new TableConstraints(1, 7, false, true));
		xmxx.addControl(btnAcct, new TableConstraints(1, 1, false, true));
		xmxx.addControl(getPrjDownPanel(), new TableConstraints(2, 1000, true,
				true));

		tbPrj = new CustomTable(new String[] { "��λ����", "��λ����", "��Ŀ����", "��ʼ���",
				"�������", "ִ������", "��Ŀ����", "��Ŀ����", "��Ŀ" }, new String[] {
				"div_code", "div_name", "xmmc", "c1", "c2", "c3", "c4", "c5",
				"accts" }, null, true, null);
		tbPrj.reset();
		tbPrj.getTable().getColumnModel().getColumn(1).setPreferredWidth(50);
		tbPrj.getTable().getColumnModel().getColumn(2).setPreferredWidth(100);
		tbPrj.getTable().getColumnModel().getColumn(7).setPreferredWidth(100);
		tbPrj.setSize(2000, 2000);
		tbPrj.getTable().getTableHeader().setBackground(
				new Color(250, 228, 184));

		FPanel pnlBase = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(1);
		pnlBase.setLayout(lay);
		FSplitPane pnlInfo = new FSplitPane();
		pnlInfo.setOrientation(FSplitPane.HORIZONTAL_SPLIT);
		pnlInfo.setDividerLocation(200);
		FScrollPane pnlTree = new FScrollPane();
		DataSet ds = PubInterfaceStub.getMethod().getDivDataPop(
				GlobalEx.loginYear);
		treeEn = new CustomTree("Ԥ�㵥λ", ds, "en_id", "code_name", "parent_id",
				null, "div_code");
		treeEn.reset();
		treeEn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1 && state == -1) {
					selectYear = cbYear.getValue().toString();
					refreshPrjData(selectYear);
					setPanelValue(true);
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
		RowPreferedLayout layXM = new RowPreferedLayout(4);
		pnlXM.setLayout(layXM);

		FSplitPane pnlff = new FSplitPane();
		pnlff.setOrientation(FSplitPane.VERTICAL_SPLIT);
		pnlff.setDividerLocation(200);
		// f2 = new FLabel();
		// f2.setForeground(Color.red);

		FPanel pnlfftop = new FPanel();
		RowPreferedLayout pnlfftoplay = new RowPreferedLayout(4);
		pnlfftop.setLayout(pnlfftoplay);
		pnlfftop.addControl(getQueryType(), new TableConstraints(1, 1, false,
				true));
		pnlfftop.addControl(getYear(), new TableConstraints(1, 1, false, true));

		// pnlfftop.addControl(f2, new TableConstraints(1, 3, false, true));
		pnlfftop.addControl(tbPrj, new TableConstraints(1, 4, true, true));
		pnlfftop.setTopInset(10);
		pnlfftop.setLeftInset(10);
		pnlfftop.setRightInset(10);
		pnlfftop.setBottomInset(10);
		pnlff.addControl(pnlfftop);
		pnlff.addControl(xmxx);

		pnlXM.addControl(pnlff, new TableConstraints(4, 4, true, true));

		pnlXM.setTopInset(10);
		pnlXM.setLeftInset(10);
		pnlInfo.addControl(pnlCF);
		pnlInfo.addControl(pnlXM);

		pnlInfo.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		pnlBase.addControl(pnlInfo, new TableConstraints(1, 1, true, true));
		return pnlBase;
	}

	private void refreshPrjData() {
		refreshPrjData(Global.loginYear);
	}

	private void refreshPrjData(String year) {
		try {
			StringBuffer cond = new StringBuffer();
			cond.append(" AND a.SET_YEAR = " + year);
			cond.append(" AND a.RG_CODE = " + Global.getCurrRegion());
			MyTreeNode nodeSel = (MyTreeNode) treeEn.getSelectedNode();
			if (nodeSel != null) {
				DataSet ds = treeEn.getDataSet();// ��ȡԤ�㵥λ
				if (treeEn.getSelectedNode() != treeEn.getRoot()) {
					if (ds != null && !ds.isEmpty() && !ds.bof() && !ds.eof()) {
						String divCode = ds.fieldByName("div_code").getString();
						cond.append(" AND DIV_CODE LIKE '" + divCode + "%'");
					}
				}
			}
			String flowstatus = "001";// Ĭ��ֵ
			// TODO ��ѯ
			switch (this.cbQueryType.getSelectedIndex()) {
			case 0:
				// δ����
				flowstatus = "001";
				break;
			case 1:
				// ������
				flowstatus = "002";
				break;
			case 2:
				flowstatus = "004";
				// ���˻�
				break;
			case 3:
				flowstatus = "000";
				// ȫ����Ŀ
				break;
			}
			DataSet data = PrjInputStub.getMethod().getPrjSbCreateInfo(
					cond.toString(), flowstatus, moduleid);
			tbPrj.setDataSet(data);
			tbPrj.reset();
			setTableProp();
			setPanelValue(true);
		} catch (Exception ee) {
			ErrorInfo.showErrorDialog(ee, "ˢ�µ�λ��Ϣ����");
			return;
		}

	}

	private FTabbedPane getPrjDownPanel() throws Exception {
		FTabbedPane tabpane = new FTabbedPane();
		tabpane.addControl("�걨����", getPrjRemarkPanel());
		tabpane.addControl("��    ��", getPrjAffixPanel());
		return tabpane;
	}

	private FPanel getPrjRemarkPanel() {
		FPanel ply = new FPanel();
		RowPreferedLayout plyRpl = new RowPreferedLayout(1);
		ply.setLayout(plyRpl);
		ply.setTopInset(10);
		ply.setLeftInset(10);
		ply.setRightInset(10);
		ply.setBottomInset(10);
		fSbly = new FTextArea();
		fSbly.setProportion(0.001f);

		fSbly.getEditor().setBackground(Color.white);
		ply.addControl(fSbly, new TableConstraints(1, 1, true, true));
		return ply;
	}

	private FPanel getPrjAffixPanel() throws Exception {
		PrjAuditDTO pd = new PrjAuditDTO();
		PrjAuditAction pa = new PrjAuditAction(pd);
		tpaa = new TbPrjAuditAffix(pa);
		return tpaa.setBottomPanel();
	}

	private FComboBox getQueryType() throws Exception {
		cbQueryType = new FComboBox("");
		cbQueryType.setTitle("��Ŀ״̬��");
		cbQueryType.setProportion(0.4f);
		cbQueryType.addValueChangeListener(new ValueChangeListener() {
			public void valueChanged(ValueChangeEvent arg0) {
				refreshPrjData();
				setPanelValue(true);
			}
		});
		setStatusCbx(cbQueryType);
		try {
			createToolBar();
		} catch (Exception e) {

		}
		return cbQueryType;
	}

	/**
	 * ���ù������İ�ť״̬
	 * 
	 * @param aState
	 */
	public void setButtonState1(boolean state) {
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++) {
			FButton btnGet = (FButton) controls.get(i);
			if ("�ر�".equals(btnGet.getText().trim()))
				continue;
			btnGet.setEnabled(state);
			;
		}
	}

	private FComboBox getYear() {
		cbYear = new FComboBox("��ȣ�");
		String[] years = new String[] { "2010", "2011", "2012", "2013" };
		String YearValue = "";
		for (int i = 0; i < years.length; i++) {
			YearValue += years[i] + "#" + years[i] + "+";
		}
		if (YearValue.length() > 0) {
			YearValue = YearValue.substring(0, YearValue.length() - 1);
		}
		cbYear.setRefModel(YearValue);
		cbYear.setValue(Global.loginYear);
		cbYear.addValueChangeListener(new ValueChangeListener() {
			public void valueChanged(ValueChangeEvent arg0) {
				selectYear = cbYear.getValue().toString();
				if (!selectYear.equals(Global.loginYear)) {
					cbQueryType.setSelectedIndex(3);
					cbQueryType.setEnabled(false);
					setButtonState1(false);
				} else {
					cbQueryType.setSelectedIndex(0);
					cbQueryType.setEnabled(true);
					setButtonState1(true);
				}
				// ��̬������ʼ�������
				String qsndTmp = "";
				int numQs = Integer.parseInt(selectYear) - 5;
				for (int i = 0; i <= 5; i++) {
					qsndTmp += (numQs + i) + "#" + (numQs + i) + "+";
				}
				if (qsndTmp.length() > 0) {
					qsndTmp = qsndTmp.substring(0, qsndTmp.length() - 1);
				}

				cbYearBegin.setRefModel(qsndTmp);
				qsndTmp = "";
				int num = Integer.parseInt(cbYear.getValue().toString());
				for (int i = 0; i < 10; i++) {

					qsndTmp += (num + i) + "#" + (num + i) + "+";

				}
				if (qsndTmp.length() > 0) {
					qsndTmp = qsndTmp.substring(0, qsndTmp.length() - 1);
				}
				cbYearEnd.setRefModel(qsndTmp);
				refreshPrjData(selectYear);
			}

		});
		return cbYear;
	}

	public void doAdd() {
		// TODO ���Ӳ���
		try {
			MyTreeNode nodeSel = (MyTreeNode) treeEn.getSelectedNode();
			if (nodeSel != null && nodeSel.isLeaf()) {
				state = 0;
				setPanelEnable(true);
				setPanelValue(true);
				setButtonState(false);
				String aPrjCode = PrjInputStub.getMethod()
						.getXmbm(
								treeEn.getDataSet().fieldByName("div_code")
										.getString(), Global.loginYear);
				String xmxh = UUIDRandom.generate();
				cbPrjState.setSelectedIndex(0);
				tfPrjName.setFocus();
				tbPrj.getDataSet().append();
				tbPrj.getDataSet().fieldByName("set_year").setValue(
						Global.loginYear);
				tbPrj.getDataSet().fieldByName("rg_code").setValue(
						Global.getCurrRegion());
				tbPrj.getDataSet().fieldByName("xmxh").setValue(xmxh);
				tbPrj.getDataSet().fieldByName("xmbm").setValue(aPrjCode);
				tbPrj.getDataSet().fieldByName("en_id").setValue(
						treeEn.getDataSet().fieldByName("en_id").getString());
				tbPrj.getDataSet().fieldByName("div_code")
						.setValue(
								treeEn.getDataSet().fieldByName("div_code")
										.getString());
				tbPrj.getDataSet().fieldByName("div_name")
						.setValue(
								treeEn.getDataSet().fieldByName("div_name")
										.getString());
				tbPrj.getDataSet().fieldByName("lrr_dm").setValue(
						GlobalEx.getUserId());
				tbPrj.getDataSet().fieldByName("lrrq").setValue(new Date());
				tbPrj.getDataSet().fieldByName("xgr_dm").setValue(
						GlobalEx.getUserId());
				tbPrj.getDataSet().fieldByName("xgrq").setValue(new Date());
				tbPrj.getDataSet().fieldByName("gkdw_id").setValue(
						treeEn.getDataSet().fieldByName("en_id").getString());
				tbPrj.getDataSet().fieldByName("gkdw_code")
						.setValue(
								treeEn.getDataSet().fieldByName("div_code")
										.getString());
				tbPrj.getDataSet().fieldByName("gkdw_name")
						.setValue(
								treeEn.getDataSet().fieldByName("div_name")
										.getString());
				tbPrj.getDataSet().fieldByName("step_id").setValue("0");
				tbPrj.getDataSet().fieldByName("is_back").setValue("0");
				tbPrj.getDataSet().fieldByName("sb_ly").setValue(
						fSbly.getValue() == null ? "" : fSbly.getValue()
								.toString());

				tpaa.setAuditXmbm(aPrjCode);
				tpaa.setAuditXmxh(xmxh);

				tpaa.setEn_code(tbPrj.getDataSet().fieldByName("div_code")
						.getString());
				tpaa.setEn_id(tbPrj.getDataSet().fieldByName("en_id")
						.getString());
				tpaa.setTableData();

			} else {
				JOptionPane.showMessageDialog(null, "��ѡ��Ԥ�㵥λ");
				return;
			}
		} catch (Exception e) {
			ErrorInfo.showErrorDialog(e, "����ʧ��");
		}
	}

	public void doDelete() {
		// TODO ɾ������
		int count = tbPrj.getTable().getRowCount();
		int num = 0;
		for (int i = 0; i < count; i++) {
			Object check = tbPrj.getTable().getValueAt(i, 0);
			if (((Boolean) check).booleanValue()) {
				num++;
			}
		}
		if (num == 0) {
			JOptionPane.showMessageDialog(null, "��ѡ���걨��Ŀ��¼");
			return;
		}
		try {
			String xmxhs = "";
			for (int i = 0; i < count; i++) {
				Object check = tbPrj.getTable().getValueAt(i, 0);
				if (((Boolean) check).booleanValue()) {
					if (tbPrj.getDataSet().gotoBookmark(tbPrj.rowToBookmark(i))) {
						String xmxh = tbPrj.getDataSet().fieldByName("xmxh")
								.getString();
						xmxhs += "'" + xmxh + "',";
					}
				}
			}
			if (xmxhs.length() > 0) {
				xmxhs = xmxhs.substring(0, xmxhs.length() - 1);
			}
			if (!xmxhs.equals("")) {
				List isExists = PrjInputStub.getMethod().isExistsXm(xmxhs);
				String xmmcs = "";
				if (isExists.size() > 0) {
					for (int i = 0; i < isExists.size(); i++) {
						Map map = new HashMap();
						map = (Map) isExists.get(i);
						xmmcs += map.get("xmmc").toString() + ",";
					}
				}
				if (xmmcs.length() > 0) {
					xmmcs = xmmcs.substring(0, xmmcs.length() - 1);
					JOptionPane.showMessageDialog(null, "������Ŀ�Ѿ���ʹ�ã�����ɾ��\n"
							+ xmmcs);
					return;
				}
			}
			if (JOptionPane.showConfirmDialog(Global.mainFrame, "ȷ��ɾ����ѡ��Ŀ��",
					"��ʾ", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
				return;
			}

			for (int i = 0; i < count; i++) {
				Object check = tbPrj.getTable().getValueAt(i, 0);
				if (((Boolean) check).booleanValue()) {
					if (tbPrj.getDataSet().gotoBookmark(tbPrj.rowToBookmark(i))) {
						String xmxh = tbPrj.getDataSet().fieldByName("xmxh")
								.getString();
						List listSql = new ArrayList();
						listSql
								.add("delete from rp_xmjl_km_divsb where xmxh = '"
										+ xmxh
										+ "' and set_year ='"
										+ Global.loginYear + "'");
						listSql.add("delete from rp_xmjl_divsb where xmxh = '"
								+ xmxh + "' and set_year ='" + Global.loginYear
								+ "'");
						PrjInputStub.getMethod().postData(listSql);
					}
				}
			}
			refreshPrjData();
			JOptionPane.showMessageDialog(null, "ɾ���ɹ�");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "ɾ��ʧ��");
			e.printStackTrace();
		}
	}

	public void doModify() {

		int count = tbPrj.getTable().getRowCount();
		int num = 0;
		for (int i = 0; i < count; i++) {
			Object check = tbPrj.getTable().getValueAt(i, 0);
			if (((Boolean) check).booleanValue()) {
				num++;
			}
		}
		if (num > 1) {
			JOptionPane.showMessageDialog(null, "��ѡ��һ����¼�����޸�");
			return;
		}
		int row = tbPrj.getTable().getSelectedRow();
		try {
			if (row >= 0) {
				String bmk = tbPrj.rowToBookmark(row);
				if (!tbPrj.getDataSet().gotoBookmark(bmk)) {
					JOptionPane.showMessageDialog(null, "��ѡ���¼");
					return;
				}
			} else {
				JOptionPane.showMessageDialog(null, "��ѡ���¼");
				return;
			}
		} catch (HeadlessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// PrjInfoDlg.setDoStates(PrjInfoDlg.IS_EDIT);
		// PrjInfoDlg userInfo = new PrjInfoDlg(this, treeEn, tbPrj, dsPrj);
		// setSizeAndLocation(userInfo);
		// userInfo.setVisible(true);

		setButtonState(false);
		state = 1;
		setPanelEnable(true);
		try {
			tbPrj.getDataSet().edit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doSave() {
		checkData();

		if ((cbPrjCir.getValue() == null) || cbPrjCir.getValue().equals("")) {
			JOptionPane.showMessageDialog(Global.mainFrame, "ִ�����ڲ���Ϊ��");
			return;
		}
		if ((cbPrjSort.getValue() == null) || cbPrjSort.getValue().equals("")) {
			JOptionPane.showMessageDialog(Global.mainFrame, "��Ŀ���಻��Ϊ��");
			return;
		}

		if ((cbPrjState.getValue() == null) || cbPrjState.getValue().equals("")) {
			JOptionPane.showMessageDialog(Global.mainFrame, "��Ŀ״̬����Ϊ��");
			return;
		}

		if (Common.isNullStr(Common.nonNullStr(tfAcct.getValue()))) {
			JOptionPane.showMessageDialog(Global.mainFrame, "���ܿ�Ŀ����Ϊ��");
			return;
		}

		String xmsx = this.fxmsx.getText();
		if (Common.isNullStr(xmsx)) {
			JOptionPane.showMessageDialog(Global.mainFrame, "��Ŀ����Ϊ�գ���ѡ��");
			return;
		}
		// TODO �������
		String bmk = tbPrj.getDataSet().toogleBookmark();
		InfoPackage info = checkData();
		if (!info.getSuccess()) {
			JOptionPane.showMessageDialog(Global.mainFrame, info.getsMessage());
			return;
		} else {
			info = saveData();
			if (!info.getSuccess()) {
				JOptionPane.showMessageDialog(Global.mainFrame, info
						.getsMessage());
				return;
			}
		}
		setPanelEnable(false);
		setButtonState(true);
		state = -1;
		refreshPrjData();
		try {
			if (tbPrj.getDataSet().gotoBookmark(bmk)) {
				DataSet ds = tbPrj.getDataSet();
				setPanelValue(ds.fieldByName("xmbm").getString(), ds
						.fieldByName("xmmc").getString(), ds.fieldByName("c1")
						.getString(), ds.fieldByName("c2").getString(), ds
						.fieldByName("c3").getString(), ds.fieldByName("c4")
						.getString(), ds.fieldByName("c5").getString(), ds
						.fieldByName("c6").getString(), ds.fieldByName("accts")
						.getString(), ds.fieldByName("sb_ly").getString());
			}

			if (tpaa.tableAffix != null) {
				tpaa.tableAffix.getDataSet().clearAll();
			}

		} catch (Exception e) {
			ErrorInfo.showErrorDialog(e.getMessage());
		}
	}

	private InfoPackage checkData() {
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);

		if (Common.isNullStr(Common.nonNullStr(tfPrjName.getValue()))) {
			info.setSuccess(false);
			info.setsMessage("��Ŀ���Ʋ�����Ϊ��");
			tfPrjName.setFocus();
		}

		return info;
	}

	private boolean checkPrjname(String xmmc, String div_code, String xmbm)
			throws Exception {
		String sql = "select count(1) from rp_xmjl_divsb where xmmc='" + xmmc
				+ "' and div_code=" + div_code + " and xmbm !=" + xmbm
				+ " and set_year = " + Global.loginYear;
		if (DBSqlExec.client().getIntValue(sql) > 0)
			return true;
		else
			return false;
	}

	private InfoPackage saveData() {
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		try {

			String xmmcStr = tfPrjName.getValue().toString();

			if (checkPrjname(tfPrjName.getValue().toString(), treeEn
					.getDataSet().fieldByName("div_code").getString(), tbPrj
					.getDataSet().fieldByName("xmbm").getString())) {
				info.setsMessage("����ʧ�ܣ���Ŀ�����ظ���");
				info.setSuccess(false);
				return info;
			}
			String qsndStr = cbYearBegin.getValue() == null ? "" : cbYearBegin
					.getValue().toString();
			String jsndStr = cbYearEnd.getValue() == null ? "" : cbYearEnd
					.getValue().toString();
			String zxzqStr = cbPrjCir.getValue() == null ? "" : cbPrjCir
					.getValue().toString();
			String xmflStr = getPrjSortCode();

			String xmsxStr = fxmsx.getText() == null ? "" : fxmsx.getText()
					.toString();
			String xmztStr = getPrjStateCode();
			String accts = Common.nonNullStr(tfAcct.getValue());

			String SblyStr = fSbly.getValue() == null ? "" : fSbly.getValue()
					.toString();
			tbPrj.getDataSet().fieldByName("xmmc").setValue(xmmcStr);
			tbPrj.getDataSet().fieldByName("c1").setValue(qsndStr);
			tbPrj.getDataSet().fieldByName("c2").setValue(jsndStr);
			tbPrj.getDataSet().fieldByName("c3").setValue(zxzqStr);
			tbPrj.getDataSet().fieldByName("c4").setValue(xmflStr);
			tbPrj.getDataSet().fieldByName("c5").setValue(xmsxStr);
			tbPrj.getDataSet().fieldByName("c6").setValue(xmztStr);
			tbPrj.getDataSet().fieldByName("accts").setValue(accts);
			if (GlobalEx.isFisVis())
				tbPrj.getDataSet().fieldByName("step_id").setValue("1");
			else
				tbPrj.getDataSet().fieldByName("step_id").setValue("0");
			tbPrj.getDataSet().fieldByName("is_back").setValue("0");
			tbPrj.getDataSet().fieldByName("sb_ly").setValue(SblyStr);
			PrjInputStub.getMethod().savePrjCreateSbInfo(tbPrj.getDataSet(),
					Global.loginYear,
					tbPrj.getDataSet().fieldByName("xmxh").getString(),
					sAcctBsIDs, Global.getCurrRegion());
		} catch (Exception ee) {
			info.setSuccess(false);
			info.setsMessage("����ʧ�ܣ�������ϢΪ:" + ee.getMessage());
		}
		return info;
	}

	public void doCancel() {
		// TODO ȡ������
		setPanelEnable(false);
		setButtonState(true);
		state = -1;
		try {
			tbPrj.getDataSet().cancel();
			tbPrj.getDataSet().applyUpdate();
			refreshPrjData();
			setPanelValue(true);
			if (tpaa.tableAffix != null) {
				tpaa.tableAffix.getDataSet().clearAll();
			}
		} catch (Exception ee) {

		}
	}

	public void doImpProject() {

	}

	private void setPanelEnable(boolean isEnable) {
		cbQueryType.setEnabled(!isEnable);
		treeEn.setEnabled(!isEnable);
		btnAcct.setEnabled(isEnable);
		tbPrj.setEnabled(!isEnable);
		tbPrj.getTable().setEnabled(!isEnable);
		tfPrjName.setEnabled(isEnable);
		tfPrjName.setEditable(isEnable);
		if (1 == state) {
			if (cbPrjCir.getValue() != null
					&& "003".equals(cbPrjCir.getValue().toString())) {
				cbYearBegin.setEnabled(isEnable);
				cbYearEnd.setEnabled(isEnable);
			}
		} else {
			cbYearBegin.setEnabled(false);
			cbYearEnd.setEnabled(false);

		}
		cbPrjCir.setEnabled(isEnable);
		cbPrjSort.setEnabled(isEnable);
		fxmsx.setEnabled(isEnable);
		cbPrjState.setEnabled(false);
		fSbly.setEnabled(isEnable);
		cbYear.setEnabled(!isEnable);

	}

	/**
	 * �Ƿ���ձ�
	 * 
	 * @param isClear
	 */
	private void setPanelValue(boolean isClear) {
		if (isClear) {
			tfPrjName.setValue("");
			cbYearBegin.setSelectedIndex(-1);
			cbYearEnd.setSelectedIndex(-1);
			cbPrjCir.setSelectedIndex(-1);
			cbPrjSort.setSelectedIndex(-1);
			fxmsx.setValue("");
			cbPrjState.setSelectedIndex(-1);
			tfAcct.setValue("");
			fSbly.setValue("");

		}
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
	private void setPanelValue(String bm, String mc, String qn, String jn,
			String zq, String fl, String sx, String zt, String km, String ly)
			throws Exception {
		//		

		// tfPrjCode.setValue(bm.toString());
		tfPrjName.setValue(mc);
		fxmsx.setText(sx);
		cbPrjState.setSelectedIndex(getIndexByName(cbPrjState, zt));
		cbPrjSort.setSelectedIndex(getIndexByName(cbPrjSort, fl));
		cbPrjCir.setSelectedIndex(getIndexByName(cbPrjCir, zq));
		cbYearBegin.setSelectedIndex(getIndexByName(cbYearBegin, qn));
		cbYearEnd.setSelectedIndex(getIndexByName(cbYearEnd, jn));
		tfAcct.setValue(km);
		fSbly.setValue(ly);

	}

	/**
	 * ���ù������İ�ť״̬
	 * 
	 * @param aState
	 */
	public void setButtonState(boolean isEditState) {
		List controls = this.getToolbarPanel().getSubControls();
		for (int i = 0; i < controls.size(); i++) {
			FButton btnGet = (FButton) controls.get(i);
			if ("����".equals(btnGet.getText())) {
				btnGet.setEnabled(isEditState);
			}
			if ("�޸�".equals(btnGet.getText())) {
				btnGet.setEnabled(isEditState);
			}
			if ("ɾ��".equals(btnGet.getText())) {
				btnGet.setEnabled(isEditState);
			}
			if ("����".equals(btnGet.getText())) {
				btnGet.setEnabled(!isEditState);
			}
			if ("ȡ��".equals(btnGet.getText())) {
				btnGet.setEnabled(!isEditState);
			}
		}
	}

	private void setTableProp() throws Exception {
		tbPrj.getTable().setRowHeight(25);
		if (tbPrj != null && tbPrj.getDataSet() != null) {
			tbPrj.getTable().addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (tbPrj.getTable() != null) {
						int row = tbPrj.getTable().getSelectedRow();
						if (row < 0)
							return;
						String bmk = tbPrj.rowToBookmark(row);
						try {
							if (tbPrj.getDataSet() != null
									&& !tbPrj.getDataSet().isEmpty()
									&& tbPrj.getDataSet().gotoBookmark(bmk)) {
								DataSet ds = tbPrj.getDataSet();
								if (!ds.isEmpty() && !ds.bof() && !ds.eof()) {
									setPanelValue(
											ds.fieldByName("xmbm").getString(),
											ds.fieldByName("xmmc").getString(),
											ds.fieldByName("c1").getString(),
											ds.fieldByName("c2").getString(),
											ds.fieldByName("c3").getString(),
											ds.fieldByName("c4").getString(),
											ds.fieldByName("c5").getString(),
											ds.fieldByName("c6").getString(),
											ds.fieldByName("accts").getString(),
											ds.fieldByName("sb_ly").getString());

									tpaa.setAuditXmbm(ds.fieldByName("xmbm")
											.getString());

									tpaa.setAuditXmxh(ds.fieldByName("xmxh")
											.getString());
									tpaa.setEn_code(tbPrj.getDataSet()
											.fieldByName("div_code")
											.getString());
									tpaa.setEn_id(tbPrj.getDataSet()
											.fieldByName("en_id").getString());
									tpaa.setTableData();
								}
								cbYearBegin.setEnabled(false);
								cbYearEnd.setEnabled(false);

							} else {
								setPanelValue(true);
							}
						} catch (Exception ee) {
							ErrorInfo.showErrorDialog(ee, "ˢ����Ŀ��Ϣ����������ϢΪ:"
									+ ee.getMessage());
						}
					}
				}
			});
		}
	}

	private String getPrjStateCode() {
		return Common.nonNullStr(cbPrjState.getValue());
	}

	private String getPrjAttrCode() {
		String[] fxmsxStr = fxmsx.getValue() == null ? null : (String[]) fxmsx
				.getValue();
		if (fxmsx.getValue() == null)
			return fxmsx.getText().toString();

		String[] fxmsxf1 = new String[fxmsxStr.length];
		String xmsxstr = "";
		try {

			for (int i = 0; i < fxmsxStr.length; i++) {

				DataSet ds1 = DBSqlExec.client().getDataSet(
						"select * from dm_xmsx  where chr_id ='" + fxmsxStr[i]
								+ "'");
				ds1.fieldByName("chr_code").getValue();
				xmsxstr += ds1.fieldByName("chr_code").getValue().toString()
						+ " "
						+ ds1.fieldByName("chr_name").getValue().toString()
						+ ";";
				fxmsxf1[i] = ds1.fieldByName("chr_code1").getValue().toString();
				if (i > 0) {
					if (!fxmsxf1[i].equals(fxmsxf1[i - 1])) {
						JOptionPane.showMessageDialog(Global.mainFrame,
								"��Ŀ���Դ����������޸�,�������ͷ������ظ�ѡ��");
						return "";
					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (xmsxstr.length() > 0) {
			xmsxstr = xmsxstr.substring(0, xmsxstr.length() - 1);
		}

		return Common.nonNullStr(xmsxstr);
	}

	private String getPrjSortCode() {
		return Common.nonNullStr(this.cbPrjSort.getValue());
	}

	private int getIndexByName(FComboBox cb, String name) {
		int index = -1;
		if (Common.isNullStr(name))
			return index;
		for (int i = 0; i < cb.getItemsCount(); i++) {
			if (cb.getItemAt(i) != null) {
				if (name.trim().equalsIgnoreCase(
						Common.nonNullStr(cb.getItemAt(i)))) {
					return i;
				}
			}
		}
		return index;
	}

	public void doSendToAudit() {
		int[] rows = tbPrj.getTable().getSelectedRows();
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�������Ŀ");
			return;
		}
		List xmxhs = new ArrayList();
		String xmxh = "";
		try {

			for (int i = 0; i < rows.length; i++) {
				String bmk = tbPrj.rowToBookmark(rows[i]);
				if (tbPrj.getDataSet().gotoBookmark(bmk)) {
					Map values = new HashMap();
					values.put("xmxh", tbPrj.getDataSet().fieldByName("xmxh")
							.getString());
					values.put("enid", tbPrj.getDataSet().fieldByName("en_id")
							.getString());
					xmxhs.add(values);
				} else
					continue;
			}

			String msg = PrjInputStub.getMethod().doSend(xmxhs,
					GlobalEx.getCurrRegion(), Global.loginYear, moduleid);
			if ("".equals(msg))
				JOptionPane.showMessageDialog(Global.mainFrame, "����ɹ�");
			refreshPrjData();

		} catch (Exception e) {
			e.printStackTrace();
			ErrorInfo.showErrorDialog(e, "����ʧ��");

		}

	}

	public void doBackToAudit() {
		int[] rows = tbPrj.getTable().getSelectedRows();
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ�������Ŀ");
			return;
		}
		List xmxhs = new ArrayList();
		String xmxh = "";
		try {

			for (int i = 0; i < rows.length; i++) {
				String bmk = tbPrj.rowToBookmark(rows[i]);
				if (tbPrj.getDataSet().gotoBookmark(bmk)) {

					xmxh = tbPrj.getDataSet().fieldByName("xmxh").getString();
					xmxhs.add(xmxh);
				} else
					continue;

			}
			PrjInputStub.getMethod().sendbackAudit(xmxhs,
					GlobalEx.getCurrRegion(), Global.loginYear,
					GlobalEx.getModuleId(), 1);

			JOptionPane.showMessageDialog(null, "�����ɹ�");
			refreshPrjData();

		} catch (Exception e) {
			e.printStackTrace();
			ErrorInfo.showErrorDialog(e, "����ʧ��");

		}
	}

	public void doPrint() {

		int count = tbPrj.getTable().getSelectedRow();

		if (count < 0) {
			JOptionPane.showMessageDialog(Global.mainFrame, "��ѡ��һ����¼���д�ӡ");
			return;
		}
		String bmk = tbPrj.rowToBookmark(count);
		String xmxhpt = "";
		try {
			if (tbPrj.getDataSet().gotoBookmark(bmk)) {

				xmxhpt = tbPrj.getDataSet().fieldByName("xmxh").getString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		Map mmdate = new HashMap();
		mmdate.put("xmxh", xmxhpt);

		// mmdate.put("prj_name",tfAcct.getValue().toString());

		try {
			preview("rp0002", mmdate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(Global.mainFrame, "��ӡ����");
		}

	}

	public static void preview(String report_id, Map data) throws Exception {
		if (null != report_id && null != data) {
			ReportCore reportCore = new ReportCore(report_id, data);
			reportCore.execute();
			ReportDisplayPanel reportDisplay = new ReportDisplayPanel(
					reportCore);
			reportDisplay.repaintNoQueryPanel();

			JDialog aa = new JDialog();
			aa.setSize(800, 600);
			aa.getContentPane().add(reportDisplay);
			aa.setLocationRelativeTo(null);
			aa.setTitle("�걨����ӡ");
			aa.show();

		} else {
			JOptionPane.showMessageDialog(Global.mainFrame, "��������");

		}
	}

	public void doAuditToBack() {
		// TODO Auto-generated method stub

	}

	public void doExport() {
		// TODO Auto-generated method stub

	}

	public void doUpdatedlg() {
		// TODO Auto-generated method stub

	}

}
