package gov.nbcs.rp.sync.ui;

/**
 * ����ͬ��������
 * 
 */

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.table.CustomTable;
import gov.nbcs.rp.sync.action.SyncServiceStub;
import gov.nbcs.rp.sync.ibs.SyncInterface;
import gov.nbcs.rp.sync.util.MainInfoOper;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import com.foundercy.pf.control.ControlException;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.framework.systemmanager.FModulePanel;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.service.Log;
import com.foundercy.pf.control.FTextArea;

public class SyncMainUI extends FModulePanel {

	protected static final long serialVersionUID = 1L;
	// ҵ������
	protected MainInfoOper oper = null;
	// �����
	public FPanel mainPanel = null;
	// topPanel
	public FPanel topPanel = null;

	public FTextField dbLinkName;
	public FComboBox exportLineComboBox;
	public FComboBox execFuncComboBox1;
	public FComboBox execFuncComboBox2;

	// middlePanel
	public FPanel middlePanel = null;
	// ִ��ǰ�ű�Field
	public FTextArea beforeTField;
	// ִ�к�ű�Field
	public FTextArea afterTField;

	public FComboBox schemeComboBox;

	// bottomPanel
	public FPanel bottomPanel;

	public CustomTable tbDetail;

	SyncInterface syncService;
	
	

	/**
	 * ���췽��
	 */
	public SyncMainUI() {
	}

	/**
	 * �����ʼ��
	 */
	public void initize() {
		syncService = SyncServiceStub.getMethod();
		try {
			this.createToolBar();
			this.add(getPanel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setVisible(true);

	}

	/**
	 * ȡ�ñ�����������
	 * 
	 * @return
	 * @throws ControlException
	 */
	public FPanel getPanel() throws Exception {
		if (mainPanel == null) {
			mainPanel = new FPanel();
			RowPreferedLayout rowPreferedLayout = new RowPreferedLayout(1);
			mainPanel.setLayout(rowPreferedLayout);
			mainPanel.addControl(getTopPanel(), new TableConstraints(1, 1,
					false, true));
			mainPanel.addControl(getMiddlePanel(), new TableConstraints(1, 1,
					false, true));
			mainPanel.addControl(getBottomPanel(), new TableConstraints(1, 1,
					true, true));
			mainPanel.setLeftInset(10);
			mainPanel.setRightInset(10);
			mainPanel.setBottomInset(10);
			mainPanel.setTopInset(10);
		}
		return mainPanel;
	}

	protected FPanel getTopPanel() throws ControlException {
		if (topPanel == null) {
			topPanel = new FPanel();
		}

		RowPreferedLayout rowPreferedLayout = new RowPreferedLayout(4);
		rowPreferedLayout.setColumnWidth(15);
		rowPreferedLayout.setColumnGap(1);
		topPanel.setLayout(rowPreferedLayout);

		dbLinkName = new FTextField("DB_LINK���ƣ�");
		dbLinkName.setValue("DBLINK");
		dbLinkName.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				if ((dbLinkName.getValue() != null)
						&& !"".equals(dbLinkName.getValue())) {
					if (dbLinkName.getValue().toString().length() > 10) {
						dbLinkName.setValue(dbLinkName.getValue().toString()
								.subSequence(0, 10));
					}
				}
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		dbLinkName.setProportion(0.30f);

		exportLineComboBox = new FComboBox(" ��������");
		String modelString = "0" + "#" + "��Ŀ�⵽ƽ̨" + "+";
		modelString += "1" + "#" + "ƽ̨����Ŀ��";
		exportLineComboBox.setRefModel(modelString);
		exportLineComboBox.setProportion(0.23f);

		execFuncComboBox1 = new FComboBox("  ������ʽ��");
		modelString = null;
		modelString = "0" + "#" + "ͬ��ǰ��ձ�����" + "+";
		modelString += "1" + "#" + "ͬ��ǰ����ձ�����";
		execFuncComboBox1.setRefModel(modelString);
		execFuncComboBox1.setProportion(0.23f);

		execFuncComboBox1.addValueChangeListener(new ValueChangeListener() {
			public void valueChanged(ValueChangeEvent arg0) {
				if (arg0 != null) {
					if (arg0.getNewValue() != null) {
						if ("0".equals(arg0.getNewValue().toString())) {
							execFuncComboBox2.setEditable(false);
							execFuncComboBox2.setEnabled(false);
						}
						if ("1".equals(arg0.getNewValue().toString())) {
							execFuncComboBox2.setEditable(true);
							execFuncComboBox2.setEnabled(true);
						}
					}
				}

			}
		});

		execFuncComboBox2 = new FComboBox("");
		modelString = null;
		modelString = "0" + "#" + "ɾ��ͬ���ظ�����" + "+";
		modelString += "1" + "#" + "��ɾ��ͬ���ظ�����";
		execFuncComboBox2.setRefModel(modelString);
		execFuncComboBox2.setProportion(0f);
		execFuncComboBox2.setEditable(false);
		execFuncComboBox2.setEnabled(false);

		topPanel
				.addControl(dbLinkName, new TableConstraints(1, 1, false, true));
		topPanel.addControl(exportLineComboBox, new TableConstraints(1, 1,
				false, true));
		topPanel.addControl(execFuncComboBox1, new TableConstraints(1, 1,
				false, true));
		topPanel.addControl(execFuncComboBox2, new TableConstraints(1, 1,
				false, true));
		return topPanel;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */

	protected FPanel getMiddlePanel() throws Exception {
		if (middlePanel == null) {
			middlePanel = new FPanel();
		}

		RowPreferedLayout rowPreferedLayout = new RowPreferedLayout(3);
		middlePanel.setLayout(rowPreferedLayout);
		beforeTField = new FTextArea("ͬ��ǰ�ű�¼�룺");
		beforeTField.setProportion(0.25f);
		afterTField = new FTextArea("ͬ����ű�¼�룺");
		afterTField.setProportion(0.25f);
		schemeComboBox = new FComboBox("������");
		schemeComboBox.setProportion(0.15f);

		List list = syncService.getSchemaData(null,null);
		String modelString ="all#ȫ��+";
		for (int i = 0; i < list.size(); i++) {
			modelString += ((XMLData) list.get(i)).get("type").toString()
					+ "#"
					+ ((XMLData) list.get(i)).get("name").toString()
					+ "+";
		}
		modelString = modelString.substring(0, modelString.length() - 1);
		schemeComboBox.setRefModel(modelString);
		schemeComboBox.addValueChangeListener(new ValueChangeListener() {
			public void valueChanged(ValueChangeEvent arg0) {
				setTableData(arg0.getNewValue().toString());
			}
		});
		middlePanel.addControl(schemeComboBox, new TableConstraints(1, 1,
				false, true));
		middlePanel.addControl(beforeTField, new TableConstraints(1, 1, false,
				true));
		middlePanel.addControl(afterTField, new TableConstraints(1, 1, false,
				true));

		return middlePanel;
	}

	protected void setTableData(String string) {
		tbDetail.setDataSet(setListData(string));
		try {
			tbDetail.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected DataSet setListData(String string) {
		DataSet dataSet = null;
		try {
			if ((string==null) ||"".equals(string) || "all".equals(string.trim())) {
				dataSet = syncService.getTableData("");
			} else {
				dataSet = syncService.getTableData(string);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSet;
	}

	/**
	 * 
	 * @return
	 * @throws ControlException
	 */
	protected FPanel getBottomPanel() throws ControlException {
		if (bottomPanel == null) {
			bottomPanel = new FPanel();
		}

		try {

			DataSet ds = this.setListData(null);
			tbDetail = new CustomTable(new String[]{"����","��������","���ձ���","���ձ���������"},new String[]{"table_ename","table_cname","pf_ename","pf_cname"},ds,true,null);
			tbDetail.reset();
			// ����ʾ�������ȥ
			bottomPanel.setLayout(new BorderLayout());
			bottomPanel.addControl(tbDetail, BorderLayout.CENTER);
			
			
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
		return bottomPanel;
	}

	public MainInfoOper getOper() {
		return oper;
	}

	/**
	 * ����Table��
	 * 
	 * @param column_id
	 *            �����ĺ�̨����
	 * @param column_name
	 *            �б���
	 * @param alignment
	 *            ֵ��ʾ��λ��
	 * @param width
	 *            Ĭ�ϳ���
	 * @return ���������������ɵ�Table��
	 */
	private FBaseTableColumn returnFtableColumn(String column_id, String column_name, String alignment, int width) {
		// ����Table��
		FBaseTableColumn column = new FBaseTableColumn();
		// ���ú�̨����
		column.setId(column_id);
		// ����ֵ��ʾ��λ��
		column.setAlignment(alignment);
		// ���ñ༭����
		column.setEditable(false);
		// �����б���
		column.setTitle(column_name);
		// ������ʾ����
		column.setHeadAlignment(FBaseTableColumn.CENTER);
		// ����Ĭ�ϳ���
		column.setWidth(width);
		return column;
	}
}
