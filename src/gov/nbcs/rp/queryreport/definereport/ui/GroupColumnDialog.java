/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import gov.nbcs.rp.sys.sysrefcol.ui.SysRefColPub;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FList;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.reportcy.summary.exception.SummaryConverException;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.iface.enumer.IEnumSource;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.cellvalue.AbstractFormula;
import com.foundercy.pf.reportcy.summary.util.ReportConver;
import com.fr.base.Constants;
import com.fr.dialog.BaseDialog;
import com.fr.report.CellElement;
import com.fr.report.GroupReport;
import com.fr.report.cellElement.CellGroupAttr;

/**
 * <p>
 * Title:��������Ϣ���ÿͷ���ҳ��
 * </p>
 * <p>
 * Description:��������Ϣ���ÿͷ���ҳ��
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 �㽭�������޹�˾
 * </p>
 * <p>
 * Company: �㽭�������޹�˾
 * </p>
 * <p>
 * CreateData 2011-3-27
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class GroupColumnDialog extends BaseDialog {

	private static final long serialVersionUID = 1L;

	public final static String OPER_SUBSTR = "substr";

	// ���������
	private FList flstGroupArray;

	// �Ƿ����
	private FCheckBox fchkIsMax = null;

	// �Ƿ�����
	private FCheckBox fchkIsVisual = null;

	// ��������ѡ��
	private FCheckBox fchkOrder = null;

	// ����������ʾ
	private FCheckBox fchkIsAll = null;

	private CellElement cellElement = null;

	private MyGroupValueImpl myGroupValueImpl = null;

	private JFrame frame = null;

	private GroupReport groupReport = null;

	// ֻ�浱ǰ������������
	private int curIndex = -1;

	// ����ˢ�±��
	private boolean refreshFlag = true;

	private ReportGuideUI reportGuideUI = null;

	// �������ݿ�ӿ�
	private IDefineReport definReportServ = null;

	private ReportQuerySource querySource = null;

	// ���ܽڴ�
	private CheckBoxList lstLevel;

	// ���ڴ�
	int maxLev[];

	/**
	 * ���캯��
	 */
	public GroupColumnDialog(JFrame frame) {
		super(frame);
		this.frame = frame;
		this.setSize(600, 650);
		this.setTitle("������");
		this.setModal(true);
		this.reportGuideUI = (ReportGuideUI) frame;
		this.groupReport = reportGuideUI.fpnlDefineReport.groupReport;
		this.definReportServ = reportGuideUI.definReportServ;
		this.querySource = reportGuideUI.querySource;

		// ��ʼ������
		init();
	}

	public GroupColumnDialog(Dialog dialog) {
		super(dialog);
		this.setSize(600, 650);
		this.setTitle("������");
		this.setModal(true);
		init();
	}

	/**
	 * ��ʼ������
	 * 
	 */
	private void init() {
		// ���������
		flstGroupArray = new FList();
		flstGroupArray.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent arg0) {

				// ���浱ǰҳ��������
				if (curIndex > -1) {

					GroupObj groupObj = (GroupObj) ((DefaultListModel) flstGroupArray
							.getModel()).get(curIndex);
					// ԭֵ
					IGroupAble iGroupAbleOld = (IGroupAble) groupObj
							.getObject();
					String sErr = saveGroupAble(iGroupAbleOld);

					if (!Common.isNullStr(sErr)) {
						new MessageBox(frame, sErr, MessageBox.MESSAGE,
								MessageBox.BUTTON_OK).show();
						flstGroupArray.setSelectedIndex(curIndex);
						refreshFlag = false;
						return;
					}

					cellElement.setValue(myGroupValueImpl);

				}

				IGroupAble[] iGroupAbleArray = myGroupValueImpl
						.getGroupAbleArray();
				IGroupAble iGroupAble = iGroupAbleArray[flstGroupArray
						.getSelectedIndex()];
				// ��ʾѡ������Ϣ
				if (refreshFlag) {
					refreshFlag = true;
					showDetail(iGroupAble, flstGroupArray.getSelectedIndex());
				}
				curIndex = flstGroupArray.getSelectedIndex();

				// �����Ƿ�ɱ༭
				// �ж��Ƿ������˶�Ӧ��ϵ
				if (!DefinePub.judgetEnumWithColID(querySource, iGroupAble
						.getSourceID(), iGroupAble.getSourceColID(), true)) {
					fchkIsAll.setEnabled(false);
				} else {
					fchkIsAll.setEnabled(true);
				}
			}
		});

		FScrollPane fspnlGroupyArray = new FScrollPane(flstGroupArray);
		// ������������
		FPanel fpnlGroupyArray = new FPanel();
		fpnlGroupyArray.setTitle("�ֶ���");
		fpnlGroupyArray.setLayout(new RowPreferedLayout(1));
		fpnlGroupyArray.addControl(fspnlGroupyArray);

		// �Ƿ�����
		fchkIsVisual = new FCheckBox("������");
		fchkIsVisual.setTitlePosition("RIGHT");

		// ��������
		fchkOrder = new FCheckBox("��Ϊ������");
		fchkOrder.setTitlePosition("RIGHT");

		// �Ƿ����
		fchkIsMax = new FCheckBox("�������(Group)");
		fchkIsMax.setTitlePosition("RIGHT");
		fchkIsMax.addValueChangeListener(new ValueChangeListener() {

			public void valueChanged(ValueChangeEvent evt) {
				// ���ÿؼ�״̬�����ò�����)
				setControlState(evt.getNewValue());
			}
		});

		// �Ƿ���ʾ���е�λ
		fchkIsAll = new FCheckBox("����������ʾ");
		fchkIsAll.setTitlePosition("RIGHT");

		lstLevel = new CheckBoxList();
		lstLevel.setIsCheckBoxEnabled(false);

		// �����������
		FPanel fpnlTotal = new FPanel();
		fpnlTotal.setTitle("���ܼ���ѡ��");
		fpnlTotal.setLayout(new RowPreferedLayout(1));
		fpnlTotal.add(lstLevel, new TableConstraints(1, 1, true, true));

		BtnPanel btnPanel = new BtnPanel();

		// ���������
		FPanel fpnlMain = new FPanel();
		fpnlMain.setLayout(new RowPreferedLayout(1));
		// ������
		fpnlMain.addControl(fpnlGroupyArray, new TableConstraints(1, 1, true,
				true));
		// �������
		fpnlMain.addControl(fchkOrder, new TableConstraints(1, 1, false, true));
		// �Ƿ����
		fpnlMain.add(fchkIsMax, new TableConstraints(1, 1, false, true));
		// ���ܽڴ����
		fpnlMain.addControl(fpnlTotal, new TableConstraints(6, 1, false, true));
		// �Ƿ���ʾ���е�λ
		fpnlMain.addControl(fchkIsAll, new TableConstraints(1, 1, false, true));
		// ������
		fpnlMain.addControl(fchkIsVisual, new TableConstraints(1, 1, false,
				true));

		// ��ť���
		fpnlMain.addControl(btnPanel, new TableConstraints(2, 1, false, true));
		// ���ñ߾�
		fpnlMain.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

		this.getContentPane().add(fpnlMain);
	}

	/**
	 * �õ����ڴ�
	 * 
	 * @param groupAble
	 * @return
	 * @throws Exception
	 */
	public static int[] getLevInfo(IGroupAble[] groupAble,
			IDefineReport definReportServ, ReportQuerySource querySource)
			throws Exception {
		int length = groupAble.length;
		String[] sTableName = new String[length];
		String[] sLevelCodeArray = new String[length];
		String[] sLevelInfoArray = new String[length];

		String enumID;
		IEnumSource enumSource;
		for (int i = 0; i < length; i++) {
			// �ж��Ƿ��������
			if (groupAble[i].getContent().toLowerCase().indexOf(OPER_SUBSTR) != -1)
				continue;
			// �ж��Ƿ��Ǳ�����
			if (!DefinePub.judgetEnumWithColID(querySource, groupAble[i]
					.getSourceID(), groupAble[i].getSourceColID(), true)) {
				continue;
			}

			enumID = DefinePub.getEnumIDWithColID(querySource, groupAble[i]
					.getSourceID(), groupAble[i].getSourceColID());
			if (enumID == null) {
				sTableName[i] = "";
				continue;
			}
			// �õ�ö��
			enumSource = DefinePub.getEnumInfoWithID(querySource, enumID);
			sTableName[i] = SysRefColPub.ReplaceRefColFixFlag(enumSource
					.getSource().toString().toUpperCase());
			sLevelCodeArray[i] = enumSource.getLevelCode();
			sLevelInfoArray[i] = enumSource.getLevelInfo();
		}

		// ���ڴ�
		return definReportServ.getMaxLevel(sTableName, sLevelCodeArray,
				sLevelInfoArray);
	}

	/**
	 * ���ÿؼ�״̬
	 * 
	 * @param obj
	 */
	private void setControlState(Object obj) {
		if (Common.estimate(obj)) {
			lstLevel.setIsCheckBoxEnabled(true);
		} else {
			lstLevel.setIsCheckBoxEnabled(false);
			lstLevel.cancelSelected();
		}
	}

	/**
	 * ���尴ť��
	 */
	private class BtnPanel extends FFlowLayoutPanel {

		private static final long serialVersionUID = 1L;

		public BtnPanel() {
			// ���ÿ�����ʾ
			this.setAlignment(FlowLayout.RIGHT);

			// ���塰ȷ������ť
			FButton okBtn = new FButton("saveBtn", "����");
			okBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					if (curIndex >= 0) {
						doOK();
						// ˢ����ʾ����
						reportGuideUI.reportOrderSet.showOrderSet(groupReport);

						// У��ö������summaryIndexֵ
						try {
							// ����querySource
							ReportQuerySource querySource = (ReportQuerySource) ReportConver
									.getReportQuerySource(groupReport);
							// У��ö��summaryIndexֵ
							CheckOutEnumName checkOutEnumName = new CheckOutEnumName(
									querySource);
							checkOutEnumName.checkEnumSummaryIndex(groupReport);
						} catch (SummaryConverException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(
									GroupColumnDialog.this, " �������󣬴�����Ϣ��"
											+ e.getMessage(), "��ʾ",
									JOptionPane.ERROR_MESSAGE);
						}

						// ˢ�����л�����ʾ˳��
						reportGuideUI.reportGroupSet.showGroupSet(groupReport);
					}

					cellElement.getStyle().deriveHorizontalAlignment(
							Constants.LEFT);
					// ���ø�������Ϊnull
					cellElement.setHighlightGroup(null);
					GroupColumnDialog.this.setVisible(false);
					GroupColumnDialog.this.dispose();
				}

			});
			// ���塱ȡ������ť
			FButton cancelBtn = new FButton("cancelBtn", "ȡ ��");
			// ʵ�֡�ȡ������ť����¼�
			cancelBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					doCancel();
				}

			});

			// ��ȷ������ť���밴ť���
			this.addControl(okBtn);
			// ��ȡ������ť���밴ť���
			this.addControl(cancelBtn);
		}
	}

	class FuncListObject {

		private String name;

		private String sCHName;

		public FuncListObject(String name, String sCHName) {
			this.name = name;
			this.sCHName = sCHName;
		}

		public String toString() {
			return sCHName;
		}

		public String getName() {
			return name;
		}
	}

	/**
	 * ע�룬��ʾ��Ϣ
	 * 
	 * @param cellElement
	 * @param value
	 */
	void populate(CellElement cellElement, Object value) {
		this.cellElement = cellElement;

		if (value instanceof MyGroupValueImpl) {
			// ��¡һ��MyGroupValueImpl�����ڴ˶����ϲ���
			myGroupValueImpl = (MyGroupValueImpl) ((MyGroupValueImpl) value)
					.clone();

			IGroupAble[] iGroupAbleArray = myGroupValueImpl.getGroupAbleArray();
			if (iGroupAbleArray == null)
				return;
			int iCount = iGroupAbleArray.length;
			String sDisplayValue = null;
			IGroupAble iGroupAble = null;
			GroupObj groupObj;
			DefaultListModel listModel = new DefaultListModel();

			// ѭ��ȡ��IGroupAble[]��������Ϣ
			for (int i = 0; i < iCount; i++) {
				iGroupAble = iGroupAbleArray[i];
				sDisplayValue = ((AbstractFormula) iGroupAble.getFormula())
						.getDisplayValue();
				groupObj = new GroupObj(sDisplayValue, iGroupAble);
				listModel.addElement(groupObj);
			}
			// �����ֶ��б�ֵ
			flstGroupArray.setModel(listModel);

			// �õ����ڴ�
			try {
				maxLev = getLevInfo(iGroupAbleArray, definReportServ,
						querySource);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(GroupColumnDialog.this,
						"������ڴγ������⣬������Ϣ:" + e.getMessage(), "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}

			// ��λ����һ�ڵ�
			if (listModel.getSize() > 0) {
				flstGroupArray.setSelectedIndex(0);
			}

		}
	}

	private class GroupObj {

		private String value = "";

		private Object object = null;

		public Object getObject() {
			return object;
		}

		public void setObject(Object object) {
			this.object = object;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public GroupObj(String value, Object object) {
			this.value = value;
			this.object = object;
		}

		public String toString() {
			return value;
		}

	}

	/**
	 * ��ʾ��ϸ��Ϣ
	 * 
	 * @param iGroupAble
	 */
	private void showDetail(IGroupAble iGroupAble, int index) {
		// �Ƿ����
		((JCheckBox) fchkIsMax.getEditor()).setSelected(!Common
				.estimate(new Integer(iGroupAble.getIsMax())));
		// �Ƿ�����
		((JCheckBox) fchkIsVisual.getEditor()).setSelected(!Common
				.estimate(new Integer(iGroupAble.getIsVisual())));
		// �Ƿ���ʾ���е�λ
		((JCheckBox) fchkIsAll.getEditor()).setSelected(Common
				.estimate(new Integer(iGroupAble.getIsCorss())));
		// ��������ѡ��
		if (iGroupAble.getOrderIndex() == null
				|| "".equals(iGroupAble.getOrderIndex())) {
			((JCheckBox) fchkOrder.getEditor()).setSelected(false);
		} else {
			((JCheckBox) fchkOrder.getEditor()).setSelected(true);
		}

		int lev = maxLev[index];
		int depFlag = 0;

		// �ж��Ƿ��ҵ����
		if (IDefineReport.DIV_CODE
				.equalsIgnoreCase(iGroupAble.getSourceColID())) {
			depFlag = 1;
		}

		InstallData[] levData = new InstallData[lev + depFlag];
		if (depFlag == 1) {
			levData[0] = new InstallData("ҵ����(��λ�����п�ѡ)",
					IDefineReport.DEP_CODE);
		}
		for (int i = 0; i < lev; i++) {
			if (i == lev) {
				levData[i + depFlag] = new InstallData("ĩ��", new Integer(i + 1));
			} else {
				levData[i + depFlag] = new InstallData("��" + (i + 1) + "��",
						new Integer(i + 1));
			}
		}

		lstLevel.setData(levData);

		List lstRow = null;
		// ҵ����
		if (iGroupAble.getIsMbSummary() == 1) {
			if (lstRow == null) {
				lstRow = new ArrayList();
			}
			lstRow.add(IDefineReport.DEP_CODE);
		}

		// �ڴ�
		String level = iGroupAble.getLevel();
		String[] levArray;
		if (!Common.isNullStr(level)) {
			levArray = level.split(",");
			int length = levArray.length;
			for (int i = 0; i < length; i++) {
				if (lstRow == null) {
					lstRow = new ArrayList();
				}
				lstRow.add(levArray[i]);
			}
		}

		// ���ܵ�ĩ��
		if (Common.estimate(iGroupAble.getIsTotal())) {
			if (lstRow == null) {
				lstRow = new ArrayList();
			}
			lstRow.add(String.valueOf(lev));
		}

		lstLevel.setSelected(lstRow, true);
	}

	/**
	 * ��֯������ʾ����
	 * 
	 * @param iGroupAble
	 * @return
	 */
	private String getShowValue(MyGroupValueImpl myGroupValueImpl) {

		IGroupAble aIGroupAbleArray[] = myGroupValueImpl.getGroupAbleArray();
		IGroupAble iGroupAble = null;
		int count = aIGroupAbleArray.length;
		String value = "";
		String valueTmp = "";
		for (int i = 0; i < count; i++) {
			iGroupAble = aIGroupAbleArray[i];

			// �ж�����ֶ��Ƿ�����
			if (iGroupAble.getOrderIndex() != null
					&& !"".equals(iGroupAble.getOrderIndex())) {// ����
				if (IDefineReport.ASC_FLAG.equals(iGroupAble.getOrderType())) {// ����
					valueTmp = IDefineReport.ASC_ARROW;
				} else if (IDefineReport.DESC_FLAG.equals(iGroupAble
						.getOrderType())) {// ����
					valueTmp = IDefineReport.DESC_ARROW;
				}
			} else {// ������
				valueTmp = "";
			}

			if (iGroupAble.getIsMax() == 0) {// ����Group
				valueTmp = valueTmp
						+ "Group("
						+ ((AbstractFormula) iGroupAble.getFormula())
								.getDisplayValue() + ")";
			} else {// ��Max
				valueTmp = valueTmp
						+ ((AbstractFormula) iGroupAble.getFormula())
								.getDisplayValue();
			}
			if (Common.isNullStr(value)) {
				value = valueTmp;
			} else {
				value = value + "\n" + valueTmp;
			}
			if (i == 0) {
				value = "=" + value;
			}

		}
		return value;
	}

	protected void doCancel() {
		this.setVisible(false);
		this.dispose();

	}

	/**
	 * ����IGroupAble������Ϣ
	 * 
	 * @param iGroupAble
	 * @throws NumberFormatException
	 * @throws SummaryConverException
	 */
	private String saveGroupAble(IGroupAble iGroupAble) {

		// ԭgroupAble����
		int oldType = DefinePubOther.getGroupAbleType(iGroupAble);

		InstallData[] dataArray = lstLevel.getData();
		int size = dataArray.length;
		if (size == 0) {
			// �Ƿ���ʾС��
			iGroupAble.setIsTotal("0");
			// ���ϻ��ܽڴ�
			iGroupAble.setLevel("");
			// �Ƿ���ܵ�ҵ����
			iGroupAble.setIsMbSummary(0);
		} else {
			// �Ƿ���ʾС��
			InstallData data = dataArray[size - 1];
			iGroupAble.setIsTotal(data.isSelected() ? "1" : "0");

			// ���ϻ��ܽڴ�
			String level = "";
			for (int i = 0; i < size - 1; i++) {
				data = dataArray[i];
				if (IDefineReport.DEP_CODE.equals(data.getValue())) {
					continue;
				}
				if (data.isSelected()) {
					if (!Common.isNullStr(level))
						level = level + ",";
					level = level + data.getValue();
				}
			}
			iGroupAble.setLevel(level);

			// �Ƿ���ܵ�ҵ����
			data = dataArray[0];
			if (IDefineReport.DEP_CODE.equals(data.getValue())) {
				iGroupAble.setIsMbSummary(data.isSelected() ? 1 : 0);
			} else {
				iGroupAble.setIsMbSummary(0);
			}
		}

		// ��groupAble����,
		int newType = DefinePubOther.getGroupAbleType(iGroupAble);
		// ���ɷŵ�IsMax���棬ԭ����õ�������summaryIndex
		if (newType != DefinePubOther.LEVISTOTAL_TYPE) {
			if (Common.estimate(fchkIsMax.getValue().toString())) {
				newType = DefinePubOther.GROUP_TYPE;
			} else {
				newType = DefinePubOther.DETAIL_TYPE;
			}
		}
		// ���ϻ����ֶ��Ⱥ���,1,2,3 ��ʶsummaryIndex
		SummaryIndexSet.setSummaryIndexChangeType(querySource, groupReport,
				iGroupAble, oldType, newType);

		// �����ֶ��Ⱥ���,1,2,3 ��ʶ
		if (Common.estimate(fchkOrder.getValue())) {
			if (Common.isNullStr(iGroupAble.getOrderIndex())) {
				iGroupAble.setOrderIndex(DefinePub.getOrderIndex(groupReport));
				// ��������,�������
				iGroupAble.setOrderType(IDefineReport.ASC_FLAG);
			}
		} else {
			iGroupAble.setOrderIndex("");
			iGroupAble.setOrderType("");
		}

		// ��Ԫ����ʾ������,1:��ʾ��0������
		IGroupAble[] groupAble = myGroupValueImpl.getGroupAbleArray();
		for (int i = 0; i < groupAble.length; i++) {
			groupAble[i].setIsVisual("false".equals(fchkIsVisual.getValue()
					.toString()) ? 1 : 0);
		}

		// ���ͣ���Group��Max��0:group,1:max
		iGroupAble.setIsMax("false".equals(fchkIsMax.getValue().toString()) ? 1
				: 0);
		iGroupAble
				.setIsCorss("true".equals(fchkIsAll.getValue().toString()) ? 1
						: 0);

		return "";
	}

	/**
	 * ���������÷���
	 */
	protected void doOK() {

		GroupObj groupObj = (GroupObj) ((DefaultListModel) flstGroupArray
				.getModel()).get(curIndex);
		// ԭֵ
		IGroupAble iGroupAble = (IGroupAble) groupObj.getObject();
		String sErr = saveGroupAble(iGroupAble);
		if (!Common.isNullStr(sErr)) {
			new MessageBox(frame, sErr, MessageBox.MESSAGE,
					MessageBox.BUTTON_OK).show();
			return;
		}

		String sShowValue = getShowValue(myGroupValueImpl);
		myGroupValueImpl.setSValue(sShowValue);
		cellElement.setValue(myGroupValueImpl);

		// �ж��Ƿ��Ƿ����У����Ƿ���������Ϊ:���ݺϲ�����(��ͬ����ʾ��һ�е�ֵ)
		// peter: ��������,
		CellGroupAttr cellGroupAttr = cellElement.getCellGroupAttr();
		if (cellGroupAttr == null) {
			cellGroupAttr = new CellGroupAttr();
		}
		if (judgetIsLevel(myGroupValueImpl)) {
			// ��ͬ����ʾ��һ�е�ֵ
			cellGroupAttr.setMergeSameCellsVertically(true);
			cellGroupAttr.setHideSameCellVertically(true);
		} else {
			cellGroupAttr.setMergeSameCellsVertically(false);
			cellGroupAttr.setHideSameCellVertically(false);
		}

		cellElement.setCellGroupAttr(cellGroupAttr);

	}

	/**
	 * �ж��Ƿ����ϻ���
	 * 
	 * @param myGroupValueImpl
	 * @return
	 */
	private boolean judgetIsLevel(MyGroupValueImpl myGroupValueImpl) {
		boolean bResult = false;
		IGroupAble iGroupAble[] = myGroupValueImpl.getGroupAbleArray();
		int iCount = iGroupAble.length;
		for (int i = 0; i < iCount; i++) {
			if (!Common.isNullStr(String.valueOf(iGroupAble[i].getLevel()))) {
				return true;
			}
		}
		return bResult;
	}

	public static void main(String a[]) {
		char c = 0x2191;
		c = 0x5355;
		c = 0x6536;
		System.out.println(c);
	}

}
