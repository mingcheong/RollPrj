package gov.nbcs.rp.dicinfo.prjdetail.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.dicinfo.prjdetail.ibs.IPrjDetail;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FDialog;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.util.Global;

public class SelAcct extends FDialog {
	private DataSet ds;

	private String[] returnvalue = new String[3];

	CustomTable table = null;

	private String sSourceValue = null;

	private String sOldSourceValue = null;

	private FTextField tfTitle;
	
	private FButton btnNext;

	private String sOldvalue;
	
	private FTextField tfDefCode ;
	private FTextField tfDefName ;
	private FButton btnDef;
	
	private Boolean isOldDefine;
	
	private boolean isDefine = false;
	
	private int iDefType;

	public SelAcct(String aOldValue, boolean aisDefine,int aDefType) {
		super(Global.mainFrame);
		this.isDefine = aisDefine;
		try {
			sOldvalue = null;
			isOldDefine = new Boolean(aisDefine);
			this.iDefType = aDefType;
			if (!Common.isNullStr(aOldValue) && iDefType == -1) {
				int iPlace = aOldValue.indexOf("|");
				iPlace = iPlace + 1;
				sOldvalue = aOldValue.substring(iPlace);
			}
			this.sOldSourceValue = aOldValue;
			IPrjDetail itserv = PrjDetailStub.getMethod();
			// String sFilter = IPrjDetail.SET_YEAR + "="+ Global.loginYear+ "
			// order by "+IPubInterface.ACCT_CODE_JJ;

			ds = itserv.getAcctData();

			FPanel pnlBlank = new FPanel();
			FPanel pnlBase = new FPanel();
			RowPreferedLayout layBase = new RowPreferedLayout(8);
			pnlBase.setLayout(layBase);
			layBase.setColumnGap(2);
//			layBase.setRowGap(10);
			 layBase.setRowGap( 8 );
			layBase.setColumnWidth(80);
			FLabel lbFilter = new FLabel();
			lbFilter.setText(" ѡ  ��");
			FLabel lbDefine = new FLabel();
			lbDefine.setText(" �Զ��壺");
			tfTitle = new FTextField("�� Ŀ ");
			tfTitle.setProportion(0.1f);
			FButton btnOK = new FButton("btn1", "ȷ��");
			FButton btnCancel = new FButton("btn2", "ȡ��");
			btnNext = new FButton("btnNext", "����");
			
			tfDefCode = new FTextField("�� ��");
			tfDefCode.setProportion(0.25f);
			tfDefName = new FTextField(" �� ��");
			tfDefName.setProportion(0.2f);
			tfDefCode.setEnabled(isDefine);
			tfDefName.setEnabled(isDefine);
			btnDef = new FButton("btnNext", "�Զ���");
			
			tfDefCode.setEnabled(isDefine);
			tfDefCode.setEditable(isDefine);
			tfDefName.setEnabled(isDefine);
			tfDefName.setEditable(isDefine);
			tfTitle.setEnabled(!isDefine);
			tfTitle.setEditable(!isDefine);
			btnNext.setEnabled(!isDefine);
			if (isDefine){
				btnDef.setText("���Զ���");
				tfDefCode.setFocus();
				int acpos = aOldValue.indexOf("|");
				switch(iDefType){
				case 0:
					tfDefCode.setValue(aOldValue.substring(0, acpos));
					tfDefName.setValue(aOldValue.substring(acpos + 1, aOldValue.length()));
					break;
				case 1:
					tfDefCode.setValue(aOldValue);
					break;
				case 2:
					tfDefName.setValue(aOldValue);
					break;
				}
			}else{
				btnDef.setText("�Զ���");
				tfTitle.setFocus();
			}
			
			btnDef.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if (!btnDef.isEnabled())
						return;
					if (ds==null || ds.isEmpty()){
						//������ÿ�ĿΪ�գ���ֻ��ʹ���Զ���
						btnDef.setText("���Զ���");
						tfTitle.setEnabled(false);
						btnDef.setEnabled(false);
						isDefine = true;
						return;
					}
					isDefine = !isDefine;
					tfDefCode.setEnabled(isDefine);
					tfDefCode.setEditable(isDefine);
					tfDefName.setEnabled(isDefine);
					tfDefName.setEditable(isDefine);
					tfTitle.setEnabled(!isDefine);
					tfTitle.setEditable(!isDefine);
					btnNext.setEnabled(!isDefine);
					if (isDefine){
						btnDef.setText("���Զ���");
						tfDefCode.setFocus();
					}
					else{
						btnDef.setText("�Զ���");
						tfTitle.setFocus();
					}
				}
			});
			
			table = new CustomTable(new String[] { "����", "����" }, new String[] {
					IPubInterface.ACCT_CODE_JJ, "SHOWNAME" }, ds, false, null);
			table.reset();
			pnlBase.addControl(lbFilter, new TableConstraints(1, 1, false,
					false));
			pnlBase.addControl(tfTitle,
					new TableConstraints(1, 5, false, false));
			pnlBase.addControl(btnNext,
					new TableConstraints(1, 1, false, false));
			
			pnlBase.addControl(pnlBlank, new TableConstraints(1, 1, false,
					false));
			pnlBase.addControl(lbDefine, new TableConstraints(1, 1, false,
					false));
			pnlBase.addControl(tfDefCode, new TableConstraints(1, 2, false,
					false));
			pnlBase.addControl(tfDefName,
					new TableConstraints(1, 3, false, false));
			pnlBase.addControl(btnDef,
					new TableConstraints(1, 1, false, false));
			
			
			pnlBase
					.addControl(table,
							new TableConstraints(17, 7, false, false));
//			pnlBase.addControl(pnlBlank, new TableConstraints(1, 7, false,
//					false));
			pnlBase.addControl(pnlBlank, new TableConstraints(1, 2, false,
					false));
			pnlBase.addControl(btnOK, new TableConstraints(1, 1, false, false));
			pnlBase.addControl(pnlBlank, new TableConstraints(1, 1, false,
					false));
			pnlBase.addControl(btnCancel, new TableConstraints(1, 1, false,
					false));
			pnlBase.setLeftInset(10);
			pnlBase.setRightInset(10);
			pnlBase.setTopInset(10);
			pnlBase.setBottomInset(10);
			this.setSize(600, 600);
			this.getContentPane().add(pnlBase);
			this.dispose();
			this.setTitle("���ÿ�Ŀ����");
			this.setModal(true);
			tfTitle.addKeyListener(new FilterChangeListener(tfTitle));
			table.getTable().getSelectionModel().setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					isDefine = isOldDefine.booleanValue();
					sSourceValue = sOldSourceValue;
					setVisible(false);
					returnvalue = null;
				}
			});
			btnOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (!isDefine) {
							iDefType = -1;
							// ��������Զ���
							if (ds.isEmpty()
									|| ds.bof()
									|| ds.eof()
									|| Common.isNullStr(String.valueOf(tfTitle
											.getValue())))
								sSourceValue = "";
							else {
								String sIsLeaf = ds.fieldByName("IS_LEAF")
										.getString();
								if ("0".equals(sIsLeaf))
									return;
								sSourceValue = ds.fieldByName(
										IPubInterface.ACCT_CODE_JJ).getString()
										+ "|"
										+ ds.fieldByName(
												IPubInterface.ACCT_NAME_JJ)
												.getString();
								returnvalue[0] = ds.fieldByName(
										IPubInterface.BSI_ID).getString();
								returnvalue[1] = ds.fieldByName(
										IPubInterface.ACCT_CODE_JJ).getString();
								returnvalue[2] = ds.fieldByName(
										IPubInterface.ACCT_NAME_JJ).getString();
							}
						} else {
							// ������Զ���
							String dcode = Common.nonNullStr(tfDefCode.getValue());
							String dname = Common.nonNullStr(tfDefName.getValue());
							if (Common.isNullStr(dcode)
									&& !Common.isNullStr(dname)){
								sSourceValue = dname;
								iDefType = 2;
							}
							else if (!Common.isNullStr(dcode)
									&& Common.isNullStr(dname)){
								sSourceValue = dcode;
								iDefType = 1;
							}
							else if (Common.isNullStr(dcode) && Common.isNullStr(dname)){
								sSourceValue = "";
								iDefType = 3;
							}else{
								sSourceValue = dcode + "|" + dname;
								iDefType = 0;
							}
							returnvalue[0] = "";
							returnvalue[1] = dcode;
							returnvalue[2] = dname;
						}
						setVisible(false);
					} catch (Exception ek) {
						ek.printStackTrace();
					}
				}
			});
			table.getTable().addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					try {
						if (ds != null && !ds.isEmpty() && !ds.bof()
								&& !ds.eof())
							tfTitle.setValue(ds.fieldByName(
									IPubInterface.ACCT_NAME_JJ).getString());
					} catch (Exception ee) {
						ee.printStackTrace();
					}
					if (e.getClickCount() == 2) {
						try {
							isDefine = false;
							iDefType = -1;
							tfDefCode.setValue("");
							tfDefName.setValue("");
							btnDef.setText("�Զ���");
							
							if (ds.isEmpty() || ds.bof() || ds.eof())
								sSourceValue = "";
							else {
                                String sIsLeaf = ds.fieldByName("IS_LEAF").getString();
                                if ("0".equals(sIsLeaf))
                                    return ;
								sSourceValue = ds.fieldByName(
										IPubInterface.ACCT_CODE_JJ).getString()
										+ "|"
										+ ds.fieldByName(
												IPubInterface.ACCT_NAME_JJ)
												.getString();
								returnvalue[0] = ds.fieldByName(
										IPubInterface.BSI_ID).getString();
								returnvalue[1] = ds.fieldByName(
										IPubInterface.ACCT_CODE_JJ).getString();
								returnvalue[2] = ds.fieldByName(
										IPubInterface.ACCT_NAME_JJ).getString();

							}
							setVisible(false);
						} catch (Exception ek) {
							ek.printStackTrace();
						}
					}
				}
			});
			tfTitle.setValue(sOldvalue);
			btnNext.addActionListener(new slectNextListener());
//			ds.addDataChangeListener(new DataChangeListener() {
//				private static final long serialVersionUID = 1L;
//
//				public void onDataChange(DataChangeEvent event)
//						throws Exception {
//					String sName = ds.fieldByName(IPubInterface.ACCT_NAME_JJ)
//							.getString();
//					tfTitle.setValue(sName);
//				}
//			});
            setTableColor(table,ds);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getDefineType(){
		return iDefType;
	} 

	/**
	 * ����ѡȡ��
	 */
	public String getValue() {
		return sSourceValue;
	}

	/**
	 * ��ȡ����ֵ
	 * 
	 * @return
	 */
	public String[] getReturnValue() {
		return returnvalue;
	}

	/**
	 * �ı�༭�����ݣ���λ����Ӧ�ļ�¼��
	 */
	private class FilterChangeListener implements KeyListener {

		private FTextField sNewValue = null;

		public FilterChangeListener(FTextField aNewValue) {
			sNewValue = aNewValue;
		}

		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			keyReleased(e);
		}

		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			try {
				ds.maskDataChange(true);
				JTable tb = new JTable();
				tb = table.getTable();
				String sValue = null;
				int idx = 0;
				String sFilterValue = (sNewValue.getValue() == null) ? ""
						: sNewValue.getValue().toString().toUpperCase();
				if (Common.isNullStr(sFilterValue))
					return;
				// �жϵ�һ���ַ��Ƿ�Ϊ���֣����Ϊ������������������ң������������������
				String sFieldName = null;
				String[] intarray = { "0", "1", "2", "3", "4", "5", "6", "7",
						"8", "9" };
				Arrays.sort(intarray);
				String sFirstChar = sFilterValue.substring(0, 1);
				int ifdx = Arrays.binarySearch(intarray, sFirstChar);
				if (ifdx < 0)
					sFieldName = "SHOWNAME";
				else
					sFieldName = IPubInterface.ACCT_CODE_JJ;

				if (Common.isNullStr(sFilterValue))
					return;
				if (ds.bof() || ds.eof())
					ds.beforeFirst();
				while (ds.next()) {
					sValue = ds.fieldByName(sFieldName).getString().trim();
					boolean bTrue = sValue.startsWith(sFilterValue);
					if (bTrue) {
						idx = table.bookmarkToRow(ds.toogleBookmark());
						tb.getSelectionModel().setLeadSelectionIndex(idx);
						tb.scrollRectToVisible(tb.getCellRect(idx, 1, true));
						return;
					}
				}
				if (ds.bof() || ds.eof())
					ds.beforeFirst();
				while (ds.next()) {
					sValue = ds.fieldByName(sFieldName).getString().trim();
					int idex = sValue.indexOf(sFilterValue);
					if (idex >= 0) {
						idx = table.bookmarkToRow(ds.toogleBookmark());
						tb.getSelectionModel().setLeadSelectionIndex(idx);
						tb.scrollRectToVisible(tb.getCellRect(idx, 1, true));
						return;
					}
				}
				ds.maskDataChange(false);
			} catch (Exception ek) {
				ek.printStackTrace();
			}
		}

		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			keyReleased(e);
		}

	}

	private class slectNextListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				if (!btnNext.isEnabled())
					return;
				if (ds == null || ds.isEmpty())
					return;
				JTable tb = new JTable();
				tb = table.getTable();
				String sValue = null;
				int idx = 0;
				String sFilterValue = (tfTitle.getValue() == null) ? ""
						: tfTitle.getValue().toString().toUpperCase();
				if (Common.isNullStr(sFilterValue))
					return;
				// �жϵ�һ���ַ��Ƿ�Ϊ���֣����Ϊ������������������ң������������������
				String sFieldName = null;
				String[] intarray = { "0", "1", "2", "3", "4", "5", "6", "7",
						"8", "9" };
				Arrays.sort(intarray);
				String sFirstChar = sFilterValue.substring(0, 1);
				int ifdx = Arrays.binarySearch(intarray, sFirstChar);
				if (ifdx < 0)
					sFieldName = "SHOWNAME";
				else
					sFieldName = IPubInterface.ACCT_CODE_JJ;

				if (Common.isNullStr(sFilterValue))
					return;
				ds.maskDataChange(true);
				if (ds.bof() || ds.eof()) {
					ds.beforeFirst();
					while (ds.next()) {
						sValue = ds.fieldByName(sFieldName).getString().trim();
						boolean bTrue = sValue.startsWith(sFilterValue);
						if (bTrue) {
							idx = table.bookmarkToRow(ds.toogleBookmark());
							tb.getSelectionModel().setLeadSelectionIndex(idx);
							tb
									.scrollRectToVisible(tb.getCellRect(idx, 1,
											true));
							ds.maskDataChange(false);
							return;
						}
					}
					ds.beforeFirst();
					while (ds.next()) {
						sValue = ds.fieldByName(sFieldName).getString().trim();
						int idex = sValue.indexOf(sFilterValue);
						if (idex >= 0) {
							idx = table.bookmarkToRow(ds.toogleBookmark());
							tb.getSelectionModel().setLeadSelectionIndex(idx);
							tb
									.scrollRectToVisible(tb.getCellRect(idx, 1,
											true));
							ds.maskDataChange(false);
							return;
						}
					}
				} else {
					String bmk = ds.toogleBookmark();
					while (ds.next()) {
						sValue = ds.fieldByName(sFieldName).getString().trim();
						int idex = sValue.indexOf(sFilterValue);
						if (idex >= 0) {
							idx = table.bookmarkToRow(ds.toogleBookmark());
							tb.getSelectionModel().setLeadSelectionIndex(idx);
							tb
									.scrollRectToVisible(tb.getCellRect(idx, 1,
											true));
							ds.maskDataChange(false);
							return;
						}
					}
					ds.gotoBookmark(bmk);
					while (ds.next()) {
						sValue = ds.fieldByName(sFieldName).getString().trim();
						boolean bTrue = sValue.startsWith(sFilterValue);
						if (bTrue) {
							idx = table.bookmarkToRow(ds.toogleBookmark());
							tb.getSelectionModel().setLeadSelectionIndex(idx);
							tb
									.scrollRectToVisible(tb.getCellRect(idx, 1,
											true));
							ds.maskDataChange(false);
							return;
						}
					}
				}
			} catch (Exception ek) {
				ek.printStackTrace();
			}
			ds.maskDataChange(false);
		}
	}
    
    /**
     * �������ɫ
     * 
     * @param tableStandard
     * @param dsContent
     * @throws Exception
     */
    public void setTableColor(CustomTable tableStandard,
            DataSet dsContent) throws Exception {
        for (int i = 0; i < tableStandard.getTable().getColumnCount(); i++)
            tableStandard.getTable().getColumnModel().getColumn(i)
                    .setCellRenderer(
                            new SetCellRenderer(ds, table));
    }
    
    /**
     * 
     * �ı�����ɫ
     * 
     */
    private class SetCellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = -209603841706635597L;

        private DataSet ds;

        private CustomTable ct;

        public SetCellRenderer(DataSet ds, CustomTable ct) {
            super();
            this.ds = ds;
            this.ct = ct;
        }

        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            Component c = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
            try {
                ds.maskDataChange(true);
                String bookmark = ct.rowToBookmark(row);
                ds.gotoBookmark(bookmark);
//                String sDivID = ds.fieldByName(IPubInterface.DIV_CODE).getString();
//                int ilen = sDivID.length();
                String sIsLeaf = ds.fieldByName("IS_LEAF").getString();
                if ("0".equals(sIsLeaf)) {
                    c.setBackground(new Color(189, 183, 191));
                    c.setForeground(Color.black);
                } else {
                    c.setBackground(new Color(254, 254, 254));
                    c.setForeground(Color.black);
                    if (isSelected) {
                        c.setBackground(new Color(2, 17, 98));
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                }

                String sValue = (value == null) ? "" : value.toString();
                sValue = sValue.replaceFirst(",", "");
                // if (!Common.isNullStr(sValue)) {
                if (Common.isNumber(sValue)) {
                    ((JLabel) c).setHorizontalAlignment(JLabel.RIGHT);
                }
                // }
                ds.maskDataChange(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return c;
        }
    }

}
