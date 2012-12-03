package gov.nbcs.rp.input.ui;

/**
 * @author л��ݥ
 * 
 * @version ����ʱ�䣺Jun 8, 20122:54:21 PM
 * 
 * @Description
 */
import gov.nbcs.rp.audit.action.PrjAuditAction;
import gov.nbcs.rp.audit.action.PrjAuditDTO;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.GlobalEx;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.input.action.PrjInputDTO;
import gov.nbcs.rp.input.action.PrjInputStub;
import gov.nbcs.rp.statc.ConstStr;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.foundercy.pf.control.FBlankPanel;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTabbedPane;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.dictionary.control.FTreeAssistInput;
import com.foundercy.pf.report.systemmanager.glquery.util.UUIDRandom;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.Tools;
import com.foundercy.pf.util.XMLData;




/**
 * @author л��ݥ
 * 
 * @version ����ʱ�䣺May 17, 20129:12:21 AM
 * 
 * @Description
 */
public class BtModPanel extends JDialog
{
	private static final long serialVersionUID = 1L;
	// private CustomComboBox cbQueryType;// ��Ŀ״̬

	// FTextField tfPrjCode = null; // ��Ŀ����

	FTextField tfPrjName = null; // ��Ŀ����

	private FComboBox cbYearBegin = null; // ��ʼ���

	private FComboBox cbYearEnd = null; // �������

	FComboBox cbPrjCir = null; // ִ������

	FComboBox cbPrjSort = null; // ��Ŀ����

	FTreeAssistInput fxmsx = null;// ��Ŀ����
	// FTreeAssistInput gkdw = null;// ��ڵ�λ
	FComboBox cbPrjState = null; // ��Ŀ״̬
	private FTextField tfAcct = null; // ��Ŀѡ��
	private String[] sAcctBsIDs;// ѡ���ܿ�Ŀ���صĹ�����ˮ����
	String selectYear = GlobalEx.loginYear;

	private FTextArea fSbly;// �걨����
	// private SbAffix tbaffix; // �������
	private PrjInputDTO prj_tb = null;
	private FButton btnSave = null;// ����
	private FButton btnNoSave = null;// ȡ��
	public DataSet[] ds = null;
	String chr_id = "";
	// RpModulePanel modPanel=null;
	// TbProjectUI tbprojectui=null;
	// TbProjectExamineUI tbprojectexamineui=null;
	PrjComplete prjcomplete = null;
	DataSet ds_prjcomplete = DataSet.create();
	private TbPrjAuditAffix tpaa;



	public BtModPanel(RpModulePanel tb)
	{
		super(Global.mainFrame, "�޸�", true);
		this.setTitle("��Ŀ�������");
		try
		{

			prjcomplete = (PrjComplete) tb;

			// ds_table=(DataSet)prjcomplete.tbPrj.getDataSet().clone();
			ds_prjcomplete = (DataSet) prjcomplete.prj_tb.getDsBody().clone();

			prj_tb = new PrjInputDTO();
			prj_tb.setOneYearPrj(true);
			prj_tb.setCurState(1);
			ds_prjcomplete.next();
			refreshPrjDetailData(ds_prjcomplete);

			getBasePanel();
			if (!prjcomplete.tbPrj.getDataSet().gotoBookmark(prjcomplete.tbPrj.rowToBookmark(prjcomplete.tbPrj.getTable().getSelectedRow())))
				return;
			else
			{
				if ("".equals(chr_id))
					chr_id = UUIDRandom.generate();
				else
					chr_id = prjcomplete.tbPrj.getDataSet().fieldByName("xmxh").getString();

				tpaa.setAuditXmbm(prjcomplete.tbPrj.getDataSet().fieldByName("xmbm").getString());
				tpaa.setAuditXmxh(prjcomplete.tbPrj.getDataSet().fieldByName("xmxh").getString());

				tpaa.setEn_code(prjcomplete.tbPrj.getDataSet().fieldByName("div_code").getString());
				tpaa.setEn_id(prjcomplete.tbPrj.getDataSet().fieldByName("en_id").getString());
				tpaa.setTableData();
				setPanelValue(prjcomplete.tbPrj.getDataSet());
			}
		}
		catch (Exception ex)
		{
			ErrorInfo.showErrorDialog(ex, "��Ŀ������޸Ľ����ʼ��ʧ��");
		}
	}


	public void getBasePanel() throws Exception
	{
		// �����
		FPanel pnlBase = new FPanel();
		RowPreferedLayout lay = new RowPreferedLayout(1);
		pnlBase.setLayout(lay);
		// �������
		FPanel info = new FPanel();
		RowPreferedLayout infolay = new RowPreferedLayout(1);
		info.setLayout(infolay);
		info.setTopInset(10);
		info.setLeftInset(10);
		info.setRightInset(10);
		info.setBottomInset(10);
		info.addControl(getBotFpanel(), new TableConstraints(4, 1, false, true));
		info.addControl(getPrjFilePanel(), new TableConstraints(12, 1, false, true));
		info.addControl(getPrjDetailButtonPanel(), new TableConstraints(1, 1, false, true));
		info.addControl(new FBlankPanel(), new TableConstraints(1, 1, false, true));
		info.addControl(getPrjSaveButton(), new TableConstraints(2, 1, false, true));

		pnlBase.add(info);
		this.getContentPane().add(pnlBase);
		// ���ñ���������

		// try {
		// String step_id=ds_table.fieldByName("STEP_ID").getValue().toString();
		// if(step_id.equals("�걨"))
		// setPanelEnable(true);
		// else if(step_id.equals("δ����")||step_id.equals("�Ѳ���"))
		// setPanelEnable(true);
		// else
		// setPanelEnable(false);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}


	public FPanel getPrjSaveButton()
	{
		btnSave = new FButton("save", "��  ��");
		btnNoSave = new FButton("nosave", "ȡ ��");
		FPanel info = new FPanel();
		RowPreferedLayout layRpButton = new RowPreferedLayout(20);
		layRpButton.setColumnGap(100);
		info.addControl(btnSave, new TableConstraints(1, 3, false, true));
		info.addControl(new FBlankPanel(), new TableConstraints(1, 4, false, true));

		info.addControl(btnNoSave, new TableConstraints(1, 3, false, true));

		btnSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				InfoPackage info = checkData();
				if (!info.getSuccess())
				{
					JOptionPane.showMessageDialog(Global.mainFrame, "����ʧ�ܣ�������ϢΪ��\n" + info.getsMessage());
					return;
				}
				String msg = doSave();
				if ("����ɹ�".equals(msg))
				{
					JOptionPane.showMessageDialog(null, "����ɹ�");
					prjcomplete.refreshPrjData();
					prjcomplete.prj_tb.getReportUI().repaint();
					try
					{
						prjcomplete.tpaa.setTableData();
					}
					catch (Exception e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					BtModPanel.this.dispose();
				}
				else
					;
			}
		});

		btnNoSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				BtModPanel.this.dispose();
			}
		});
		return info;
	}


	public String doSave()
	{
		InfoPackage info = getMsg();// ����֤
		if (!info.getSuccess())
		{
			JOptionPane.showMessageDialog(Global.mainFrame, info.getsMessage());
			return "����֤��ͨ��";
		}
		else
		{
			String msg = "";
			try
			{
				String xmmcStr = tfPrjName.getValue().toString();// ��Ŀ����
				String qsndStr = cbYearBegin.getValue() == null ? "" : cbYearBegin.getValue().toString();// ��ʼ���
				String jsndStr = cbYearEnd.getValue() == null ? "" : cbYearEnd.getValue().toString();// �������
				String zxzqStr = cbPrjCir.getValue() == null ? "" : cbPrjCir.getValue().toString();// ִ������
				String xmflStr = ConstStr.nonNullStr(this.cbPrjSort.getValue());// ��Ŀ����
				String xmsxStr = fxmsx.getText() == null ? "" : fxmsx.getText().toString();// ��Ŀ����
				// String gkdwStr = gkdw.getText() == null ? "" : gkdw.getText()
				// .toString();// ��ڵ�λ
				String count[] = xmsxStr.split(";");
				if (count.length > 5)
				{
					JOptionPane.showMessageDialog(Global.mainFrame, "��Ŀ���Գ���5����������ѡ��");
					return "��Ŀ���Գ���5����������ѡ��";
				}
				// count = gkdwStr.split(";");
				// if (count.length > 1) {
				// JOptionPane.showMessageDialog(Global.mainFrame,
				// "��ڵ�λ����1����������ѡ��");
				// return "��ڵ�λ����1����������ѡ��";
				// }

				// String chrId = ds_table.fieldByName("xmxh").getString();

				prj_tb.calData();// �������ܼ�
				DataSet data_detail = ((Report) prj_tb.getReportUI().getReport()).getBodyData();// ��ȡ����ϸ���ݼ�
				// double currentPrjMoney=0; //��ǰ��Ŀ�Ľ��
				data_detail.beforeFirst();
				// while (data_detail.next())
				// {
				// if(data_detail.fieldByName("sb_type").getString().equals("����Ԥ��"))
				// {
				// currentPrjMoney=ConstStr.isNullStr(data_detail.fieldByName("total_sum").getString())
				// ? 0 : data_detail.fieldByName("total_sum").getFloat();
				// }
				// }

				/* ��ȡ����λ������Ŀ���ܽ�� */
				String en_id = prjcomplete.tbPrj.getDataSet().fieldByName("en_id").getString();

				String xmztStr = ConstStr.nonNullStr(cbPrjState.getValue());
				String accts = Common.nonNullStr(tfAcct.getValue());
				String SblyStr = fSbly.getValue() == null ? "" : fSbly.getValue().toString();

				String xmbm = prjcomplete.tbPrj.getDataSet().fieldByName("xmbm").getValue().toString();
				String set_year = prjcomplete.tbPrj.getDataSet().fieldByName("set_year").getValue().toString();
				String rg_code = prjcomplete.tbPrj.getDataSet().fieldByName("rg_code").getValue().toString();
				String div_code = prjcomplete.tbPrj.getDataSet().fieldByName("div_code").getValue().toString();
				String div_name = prjcomplete.tbPrj.getDataSet().fieldByName("div_name").getValue().toString();

				Map map = new HashMap();
				map.put("xmbm", xmbm);
				map.put("xmxh", chr_id);
				map.put("xmmc", xmmcStr);
				map.put("c1", qsndStr);
				map.put("c2", jsndStr);
				map.put("c3", zxzqStr);
				// map.put("gkdw_name",gkdwStr);
				map.put("c4", xmflStr);
				map.put("c5", xmsxStr);
				map.put("c6", xmztStr);
				map.put("accts", accts);
				map.put("sb_ly", SblyStr);
				map.put("set_year", set_year);
				map.put("rg_code", rg_code);
				map.put("div_code", div_code);
				map.put("div_name", div_name);
				map.put("en_id", en_id);

				msg = PrjInputStub.getMethod().saveTbPrj(map);// ������Ŀ���Ϣ
				if (msg == null || msg.equals(""))
				{

					msg = PrjInputStub.getMethod().savePrjTbDetailInfo("", chr_id, data_detail, selectYear, GlobalEx.getCurrRegion(), Global.getUserId(), Tools.getCurrDate());// ������Ŀ���ϸ��Ϣ
					msg = PrjInputStub.getMethod().saveTbPrjSubject(selectYear, Global.getCurrRegion(), sAcctBsIDs, chr_id);// ������Ŀ����ܿ�Ŀ��Ϣ
				}

				// tbaffix.setButtonState(true);// ��ֹ����
				if (msg == null || msg.equals(""))
				{
					msg = "����ɹ�";
				}
				return msg;
			}
			catch (Exception ex)
			{
				ErrorInfo.showErrorDialog(ex, "����ʧ��");
				return "����ʧ��";
			}
		}
	}


	// ȷ��������λ��
	private int getIndexByName(FComboBox cb, String name)
	{
		int index = -1;
		if (ConstStr.isNullStr(name))
			return index;
		for (int i = 0; i < cb.getItemsCount(); i++)
		{
			if (cb.getItemAt(i) != null)
			{
				if (name.trim().equalsIgnoreCase(ConstStr.nonNullStr(cb.getItemAt(i)))) { return i; }
			}
		}
		return index;
	}


	/**
	 * ��֤�����ݸ�ʽ�Ƿ���ȷ
	 * 
	 * @return InfoPackage
	 */
	private InfoPackage getMsg()
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		if (Common.isNullStr(Common.nonNullStr(tfPrjName.getValue())))
		{
			info.setSuccess(false);
			info.setsMessage("��Ŀ���Ʋ�����Ϊ��");
			tfPrjName.setFocus();
		}
		if (Common.isNullStr(Common.nonNullStr(cbPrjCir.getValue())))
		{
			info.setSuccess(false);
			info.setsMessage("ִ�����ڲ���Ϊ��");
			cbPrjCir.setFocus();
		}
		if (Common.isNullStr(Common.nonNullStr(cbPrjSort.getValue())))
		{
			info.setSuccess(false);
			info.setsMessage("��Ŀ�����Ϊ��");
			cbPrjSort.setFocus();
		}
		if (Common.isNullStr(Common.nonNullStr(cbPrjState.getValue())))
		{
			info.setSuccess(false);
			info.setsMessage("��Ŀ״̬����Ϊ��");
			cbPrjState.setFocus();
		}
		if (Common.isNullStr(Common.nonNullStr(fxmsx.getText())))
		{
			info.setSuccess(false);
			info.setsMessage("��Ŀ����Ϊ��,��ѡ��");
			fxmsx.setFocus();
		}
		// if (Common.isNullStr(Common.nonNullStr(gkdw.getText()))) {
		// info.setSuccess(false);
		// info.setsMessage("��ڵ�λΪ��,��ѡ��");
		// gkdw.setFocus();
		// }
		return info;
	}


	/**
	 * ��ʾ��Ŀ���ϸ��Ϣ
	 */
	private void refreshPrjDetailData(DataSet ds_prjcomplete)
	{
		try
		{
			((Report) prj_tb.getReportUI().getReport()).setBodyData(ds_prjcomplete);
			prj_tb.setDsBody(ds_prjcomplete);
			((Report) prj_tb.getReportUI().getReport()).refreshBody();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	public FPanel getPrjDetailButtonPanel()
	{
		FPanel pnlRpButton = new FPanel();
		RowPreferedLayout layRpButton = new RowPreferedLayout(20);
		layRpButton.setColumnGap(25);
		pnlRpButton.setLayout(layRpButton);
		// pnlRpButton.addControl(new FBlankPanel(), new TableConstraints(1, 2,
		// false, true));
		FLabel dwwy = new FLabel();
		dwwy.setText("��λ����Ԫ");
		pnlRpButton.addControl(dwwy, new TableConstraints(1, 3, true, true));
		// this.setbtn(false, false, false, false
		return pnlRpButton;
	}


	/**
	 * ʵ���ұ�����²������
	 * 
	 * @return FPanel
	 */
	public FPanel getBotFpanel()
	{
		// �ұ�����²������
		FPanel botInfo = new FPanel();
		RowPreferedLayout botlay = new RowPreferedLayout(1);
		botInfo.setLayout(botlay);

		// �������
		FPanel botdiv = new FPanel();
		RowPreferedLayout botdivlay = new RowPreferedLayout(8);
		botdivlay.setRowGap(10);
		botdiv.setLayout(botdivlay);
		// botdiv.setTopInset(10);
		// botdiv.setLeftInset(10);
		// botdiv.setRightInset(10);
		// botdiv.setBottomInset(10);

		try
		{
			tfPrjName = new FTextField("��Ŀ���ƣ�");
			tfPrjName.setTitleAdapting(true);
			List prjstateList = PrjInputStub.getMethod().getDmXmzt(selectYear, Global.getCurrRegion());// ��ȡ��Ŀ״̬
			String prjstateValue = "";
			for (int i = 0; i < prjstateList.size(); i++)
			{
				Map m = new HashMap();
				m = (Map) prjstateList.get(i);
				prjstateValue += m.get("chr_code") + "#" + m.get("chr_name") + "+";
			}
			if (prjstateValue.length() > 0)
			{
				prjstateValue = prjstateValue.substring(0, prjstateValue.length() - 1);
			}
			cbPrjState = new FComboBox("��Ŀ״̬��");
			cbPrjState.setRefModel(prjstateValue);
			cbPrjState.setValue("");
			cbPrjState.setTitleAdapting(true);
			List prjattributeList = PrjInputStub.getMethod().getDmXmsx(selectYear, Global.getCurrRegion());// ��ȡ��Ŀ����
			fxmsx = new FTreeAssistInput("��Ŀ���ԣ�");
			// fxmsx.setEditable(false);

			XMLData treeData = new XMLData();
			treeData.put("row", prjattributeList);
			fxmsx.setData(treeData);
			fxmsx.setIsCheck(true);
			fxmsx.setOnlyLeafCanBeSelected(true);
			fxmsx.setTitleAdapting(true);

			// List list = PrjInputStub.getMethod().getgkdw(selectYear,
			// Global.getCurrRegion());// ��ȡ��Ŀ����
			// List lists=new ArrayList();
			// for(int i=0;i<list.size();i++)
			// lists.add(((Map)list.get(i)).values());
			// gkdw = new FTreeAssistInput("��ڵ�λ��");
			// gkdw.setEditable(false);
			// XMLData treeData2 = new XMLData();
			// treeData2.put("row", list);
			// gkdw.setData(treeData2);
			// //gkdw.setIsCheck(true);
			// gkdw.setOnlyLeafCanBeSelected(true);
			// gkdw.setTitleAdapting(true);

			List prjsortList = PrjInputStub.getMethod().getDmXmfl(selectYear, Global.getCurrRegion());// ��ȡ��Ŀ���
			String prjsortValue = "";
			for (int i = 0; i < prjsortList.size(); i++)
			{
				Map m = new HashMap();
				m = (Map) prjsortList.get(i);
				prjsortValue += m.get("chr_code") + "#" + m.get("chr_name") + "+";
			}
			if (prjsortValue.length() > 0)
			{
				prjsortValue = prjsortValue.substring(0, prjsortValue.length() - 1);
			}
			cbPrjSort = new FComboBox("��Ŀ���");
			cbPrjSort.setRefModel(prjsortValue);
			cbPrjSort.setValue("");
			cbPrjSort.setTitleAdapting(true);
			List execycleList = PrjInputStub.getMethod().getDmZxzq(selectYear, Global.getCurrRegion());// ��ȡ��Ŀִ������
			String execycleValue = "";
			for (int i = 0; i < execycleList.size(); i++)
			{
				Map m = new HashMap();
				m = (Map) execycleList.get(i);
				execycleValue += m.get("chr_code") + "#" + m.get("chr_name") + "+";
			}
			if (execycleValue.length() > 0)
			{
				execycleValue = execycleValue.substring(0, execycleValue.length() - 1);
			}
			cbPrjCir = new FComboBox("ִ�����ڣ�");
			cbPrjCir.setRefModel(execycleValue);
			cbPrjCir.setValue("");
			cbPrjCir.setTitleAdapting(true);
			cbPrjCir.addValueChangeListener(new ValueChangeListener()
			{
				public void valueChanged(ValueChangeEvent e)
				{
					if (cbPrjCir.getSelectedIndex() != 2)
					{
						// ���������ԣ��������ʼ���ֹ���ǵ�ǰ��ȣ������ɱ༭
						cbYearBegin.setSelectedIndex(5);
						cbYearEnd.setSelectedIndex(0);
						cbYearBegin.setEnabled(false);
						cbYearEnd.setEnabled(false);
					}
					else
					{
						// ����������ԣ�����Ա༭
						cbYearBegin.setEnabled(true);
						cbYearEnd.setEnabled(true);
					}

				}
			});

			cbYearBegin = new FComboBox("��ʼ��ȣ�");
			String startYearValue = "";
			int numQs = Integer.parseInt(GlobalEx.loginYear) - 5;
			for (int i = 0; i <= 5; i++)
			{

				startYearValue += (numQs + i) + "#" + (numQs + i) + "+";

			}
			if (startYearValue.length() > 0)
			{
				startYearValue = startYearValue.substring(0, startYearValue.length() - 1);
			}
			cbYearBegin.setRefModel(startYearValue);
			cbYearBegin.setValue(Global.loginYear);
			cbYearBegin.setTitleAdapting(true);
			cbYearEnd = new FComboBox("������ȣ�");
			String endYearValue = "";
			int num = Integer.parseInt(GlobalEx.loginYear);
			for (int i = 0; i < 10; i++)
			{
				endYearValue += (num + i) + "#" + (num + i) + "+";
			}
			if (endYearValue.length() > 0)
			{
				endYearValue = endYearValue.substring(0, endYearValue.length() - 1);
			}
			cbYearEnd.setRefModel(endYearValue);
			cbYearEnd.setValue(Global.loginYear);
			cbYearEnd.setTitleAdapting(true);
			tfAcct = new FTextField("��Ŀѡ��");
			tfAcct.setTitleAdapting(true);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		tfAcct.setEnabled(false);
		tfAcct.setEditable(false);
		tfPrjName.setEditable(false);
		tfPrjName.setEnabled(false);
		tfAcct.setProportion(0.081f);

		// �����Ž��������
		botdiv.addControl(tfPrjName, new TableConstraints(1, 2, false, true));
		botdiv.addControl(cbPrjState, new TableConstraints(1, 2, false, true));
		botdiv.addControl(fxmsx, new TableConstraints(1, 2, false, true));
		botdiv.addControl(cbPrjSort, new TableConstraints(1, 2, false, true));
		botdiv.addControl(cbPrjCir, new TableConstraints(1, 2, false, true));
		botdiv.addControl(cbYearBegin, new TableConstraints(1, 2, false, true));
		botdiv.addControl(cbYearEnd, new TableConstraints(1, 2, false, true));
		// botdiv.addControl(gkdw, new TableConstraints(1, 2, false, true));
		botdiv.addControl(tfAcct, new TableConstraints(1, 8, false, true));
		// ���������Ž��²������
		botInfo.add(botdiv);
		return botInfo;
	}


	/**
	 * ����ȡ�ؼ��Ž�������
	 */
	/**
	 * ���ǩ
	 * 
	 * @return FTabbedPane
	 */
	public FTabbedPane getPrjFilePanel() throws Exception
	{
		FTabbedPane tabpane = new FTabbedPane();
		tabpane.addControl("��Ŀ�", getPrjTbPanel());
		tabpane.addControl("��    ��", getPrjAffixPanel());
		tabpane.addControl("�걨����", getPrjRemarkPanel());
		return tabpane;
	}


	/**
	 * ��Ŀ�
	 */
	private FPanel getPrjTbPanel()
	{
		FPanel table_div = new FPanel();
		RowPreferedLayout table_div_lay = new RowPreferedLayout(1);
		table_div.setLayout(table_div_lay);
		table_div.setTopInset(10);
		table_div.setLeftInset(10);
		table_div.setRightInset(10);
		table_div.setBottomInset(10);
		table_div.add(prj_tb.getReportUI(), new TableConstraints(1, 1, true, true));
		// prj_tb.getReportUI().getGrid().addMouseListener(new MouseAdapter() {
		// // �����б��д����¼�
		// public void mouseClicked(MouseEvent e) {
		// try {
		// Cell cell = (Cell) ((Report) prj_tb.getReportUI()
		// .getReport()).getSelectedCell();
		// if (((Report) prj_tb.getReportUI().getReport())
		// .getBodyData().gotoBookmark(cell.getBookmark())) {
		// String supType = ((Report)
		// prj_tb.getReportUI().getReport()).getBodyData().fieldByName("SB_CODE").getString();
		// if ("111".equalsIgnoreCase(supType)||
		// "222".equalsIgnoreCase(supType)|| "333".equalsIgnoreCase(supType))
		// {
		//							
		// return;
		// }
		//							
		// String xmxh = ((Report)
		// prj_tb.getReportUI().getReport()).getBodyData().fieldByName("xmxh").getString();
		//
		// double sum =0;
		// if(((Report)
		// prj_tb.getReportUI().getReport()).getBodyData().fieldByName("TOTAL_SUM")!=null)
		// sum = ((Report)
		// prj_tb.getReportUI().getReport()).getBodyData().fieldByName("TOTAL_SUM").getDouble();
		// prj_tb.getReportUI().repaint();
		// }
		// } catch (Exception ex) {
		// // TODO Auto-generated catch block
		// ex.printStackTrace();
		// }
		// }
		// });
		return table_div;
	}


	/**
	 * ���ǩ���걨����
	 * 
	 * @return FPanel
	 */
	private FPanel getPrjRemarkPanel()
	{
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


	/**
	 * ���ǩ�и���
	 * 
	 * @return FPanel
	 */
	// private FPanel getPrjAffixPanel() {
	// String[] columnText = new String[] { "��������", "�����ļ�����", "��������" };
	// String[] columnField = new String[] { "affix_title", "affix_file",
	// "affix_type" };
	// tbaffix.setColumnText(columnText);
	// tbaffix.setColumnField(columnField);
	// tbaffix.setBtn(true);
	// tbaffix.setSelectYear(selectYear);
	// tbaffix.setTableName("RP_PRJ_AXFFIX");// ����
	// FPanel affix = tbaffix.setBasePanel();// ��ʼ������������
	// tbaffix.setButtonState(true);// ��ֹ������ť
	//		
	// //��ʾ������Ϣ
	// // tbaffix.setSelectYear(selectYear);
	// try{String xmxh=this.ds_table.fieldByName("XMXH").getString();
	// tbaffix.setChrId(xmxh);
	// }catch(Exception ex){}
	// tbaffix.refreshPrjData();// ��ʾ��Ŀ����
	//		
	// return affix;
	// }
	/**
	 * ��ֵ��ֵ�����ؼ���
	 */
	public void setPanelValue(DataSet ds_table)
	{
		try
		{
			String mc = ConstStr.nonNullStr(ds_table.fieldByName("xmmc").getValue());// ��Ŀ����
			String sx = ConstStr.nonNullStr(ds_table.fieldByName("c5").getValue());// ��Ŀ����
			String zt = ConstStr.nonNullStr(ds_table.fieldByName("c6").getValue());// ��Ŀ״̬
			String fl = ConstStr.nonNullStr(ds_table.fieldByName("c4").getValue());// ��Ŀ���
			String zq = ConstStr.nonNullStr(ds_table.fieldByName("c3").getValue());// ִ������
			String qn = ConstStr.nonNullStr(ds_table.fieldByName("c1").getValue());// ��ʼ���
			String jn = ConstStr.nonNullStr(ds_table.fieldByName("c2").getValue());// �������
			String km = ConstStr.nonNullStr(ds_table.fieldByName("accts").getValue());// ѡ���Ŀ
			String ly = ConstStr.nonNullStr(ds_table.fieldByName("sb_ly").getValue());// �걨����
			// String gkdwname =
			// ConstStr.nonNullStr(ds_table.fieldByName("DIV_NAME")
			// .getValue());// ��ڵ�λ
			tfPrjName.setValue(mc);
			fxmsx.setText(sx);
			fxmsx.setValue(sx);
			// gkdw.setText(gkdwname);
			cbPrjState.setSelectedIndex(getIndexByName(cbPrjState, zt));
			cbPrjSort.setSelectedIndex(getIndexByName(cbPrjSort, fl));
			cbPrjCir.setSelectedIndex(getIndexByName(cbPrjCir, zq));
			cbYearBegin.setSelectedIndex(getIndexByName(cbYearBegin, qn));
			cbYearEnd.setSelectedIndex(getIndexByName(cbYearEnd, jn));
			tfAcct.setValue(km);
			fSbly.setValue(ly);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	private InfoPackage checkData()
	{
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		try
		{
			ReportUI reportUI = prj_tb.getReportUI();
			String st = "";
			String sl = "";
			String sn = "";

			for (int i = 6; i < 13; i++)
			{
				if (9 == i)
					continue;
				// �ϼ��м���
				st = Common.isNullStr(Common.nonNullStr(reportUI.getReport().getCellElement(i, 2).getValue())) ? "0" : Common.nonNullStr(reportUI.getReport().getCellElement(i, 2).getValue());
				sl = Common.isNullStr(Common.nonNullStr(reportUI.getReport().getCellElement(i, 3).getValue())) ? "0" : Common.nonNullStr(reportUI.getReport().getCellElement(i, 3).getValue());
				sn = Common.isNullStr(Common.nonNullStr(reportUI.getReport().getCellElement(i, 4).getValue())) ? "0" : Common.nonNullStr(reportUI.getReport().getCellElement(i, 4).getValue());
				if (Double.parseDouble(st) < (Double.parseDouble(sl) + Double.parseDouble(sn)) && cbYearEnd.getSelectedIndex() != 0)
				{
					info.setSuccess(false);
					switch (i)
					{
						// case 4:
						// info.setsMessage("�ʽ���Դ�����ϼơ��ġ�����Ԥ���������ϡ����갲������������Ԥ��");
						// break;
						case 6:
							info.setsMessage("�ʽ���Դ����һ��Ԥ�㡱�ġ�����Ԥ���������ϡ����갲������������Ԥ��");
							break;
						case 7:
							info.setsMessage("�ʽ���Դ��������Ԥ�㡱�ġ�����Ԥ���������ϡ����갲������������Ԥ��");
							break;
						case 8:
							info.setsMessage("�ʽ���Դ�����������ġ�����Ԥ���������ϡ����갲������������Ԥ��");
							break;

						case 10:
							info.setsMessage("�ʽ���Դ���ϼ� ��һ��Ԥ�㡱�ġ�����Ԥ���������ϡ����갲������������Ԥ��");
							break;
						case 11:
							info.setsMessage("�ʽ���Դ���ϼ� ������Ԥ�㡱�ġ�����Ԥ���������ϡ����갲������������Ԥ��");
							break;
						case 12:
							info.setsMessage("�ʽ���Դ���ϼ� ���������ġ�����Ԥ���������ϡ����갲������������Ԥ��");
							break;

					}
					return info;
				}
				if (Double.parseDouble(st) != (Double.parseDouble(sl) + Double.parseDouble(sn)) && cbYearEnd.getSelectedIndex() == 0)
				{
					info.setSuccess(false);
					switch (i)
					{
						// case 4:
						// info.setsMessage("�ʽ���Դ�����ϼơ��ġ�����Ԥ���������ϡ����갲������������Ԥ��");
						// break;
						case 6:
							info.setsMessage("�������Ϊ���� �ʽ���Դ����һ��Ԥ�㡱�ġ�����Ԥ���������ϡ����갲��������������Ԥ��");
							break;
						case 7:
							info.setsMessage("�������Ϊ���� �ʽ���Դ��������Ԥ�㡱�ġ�����Ԥ���������ϡ����갲��������������Ԥ��");
							break;
						case 8:
							info.setsMessage("�������Ϊ���� �ʽ���Դ�����������ġ�����Ԥ���������ϡ����갲��������������Ԥ��");
							break;

						case 10:
							info.setsMessage("�������Ϊ���� �ϼ� �ʽ���Դ����һ��Ԥ�㡱�ġ�����Ԥ���������ϡ����갲��������������Ԥ��");
							break;
						case 11:
							info.setsMessage("�������Ϊ���� �ϼ� �ʽ���Դ��������Ԥ�㡱�ġ�����Ԥ���������ϡ����갲��������������Ԥ��");
							break;
						case 12:
							info.setsMessage("�������Ϊ���� �ϼ� �ʽ���Դ�����������ġ�����Ԥ���������ϡ����갲��������������Ԥ��");
							break;

					}
					return info;
				}
			}
			for (int i = 1; i < 5; i++)
			{
				// �ӵڶ��п�ʼ
				for (int j = 5; j < reportUI.getReport().getRowCount(); j++)
				{
					if (Common.isNullStr(Common.nonNullStr(reportUI.getReport().getCellElement(i, j).getValue())))
					{
						info.setSuccess(false);
						String value = "��" + (j + 1) + "�У���" + i + "��";
						if (i == 1)
							value += "  Ԥ�㼶�β���Ϊ��";
						else if (i == 2)
							value += "  ���ܿ�Ŀ����Ϊ��";
						else if (i == 3)
							value = "  ���ÿ�Ŀ����Ϊ��";
						else if (i == 4)
							value = "  �ʽ�Ϊ0";
						else
							value = "Ԥ�㼶�Ρ����ܿ�Ŀ�����ÿ�Ŀ ������Ϊ�ա�";
						info.setsMessage(value);
						return info;
					}
				}
			}
			DataSet ds = (((Report) prj_tb.getReportUI().getReport()).getBodyData());
			int k = 1;
			String type = "";
			ds.beforeFirst();
			while (ds.next())
			{
				type = ds.fieldByName("SB_CODE").getString();
				if ("111".equals(type) || "222".equals(type) || "333".equals(type))
					continue;
				ds.fieldByName("SB_TYPE").setValue(String.valueOf(k++));
				ds.fieldByName("SB_CODE").setValue("40000" + k);
			}
		}
		catch (Exception e1)
		{
			info.setSuccess(false);
			info.setsMessage("����ʧ�ܣ�������ϢΪ" + e1.getMessage());
			return info;
		}
		return info;
	}


	public void refreshPrjDetailData(String step_id, String xmbm, String div_code, String xmxh)
	{
		try
		{
			DataSet data_detail = null;
			if ("δ����".equals(step_id))
			{
				data_detail = PrjInputStub.getMethod().getPrjTbDetailInfo(selectYear, GlobalEx.getCurrRegion(), xmbm, div_code, xmxh);// ��ѯ��Ŀ���ϸ
			}
			if ("�Ѳ���".equals(step_id))
			{
				data_detail = PrjInputStub.getMethod().getPrjDetailInfo(Global.loginYear, xmxh, Global.getCurrRegion());
			}
			prj_tb.setDsBody(data_detail);
			prj_tb.refreshReportUI();
			((Report) prj_tb.getReportUI().getReport()).setBodyData(prj_tb.getDsBody());
			((Report) prj_tb.getReportUI().getReport()).refreshBody();
			prj_tb.getReportUI().repaint();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	public FPanel getPrjAffixPanel() throws Exception
	{
		PrjAuditDTO pd = new PrjAuditDTO();
		PrjAuditAction pa = new PrjAuditAction(pd);
		tpaa = new TbPrjAuditAffix(pa);
		return tpaa.setBottomPanel();
	}
}
