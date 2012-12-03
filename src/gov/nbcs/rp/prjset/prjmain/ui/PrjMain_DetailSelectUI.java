/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.prjset.prjmain.ui;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.RefActionListener;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.util.BeanFactoryUtil;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.service.Log;
import gov.nbcs.rp.prjset.prjmain.action.PrjMain_DetailSelectListener;
import gov.nbcs.rp.prjset.prjmain.ibs.PrjMainIBS;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.beans.factory.BeanFactory;

/**
 * <p>
 * Title:�ؼ�ѡ��Ԫ����
 * </p>
 * <p>
 * Description:���ӣ��޸ģ���������ʱ�����������湩ѡ����Ҫ��ӵĿؼ�
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 �㽭�������޹�˾
 * </p>
 * <p>
 * Company: �㽭�������޹�˾
 * </p>
 * <p>
 * CreateData 2011-3-17
 * </p>
 * 
 * @author ������
 * @version 1.0
 */
public class PrjMain_DetailSelectUI extends JDialog {

	private static final long serialVersionUID = 1L;

	/** ҵ������ IBS */
	private PrjMainIBS prjMainService = null;

	/**
	 * @return ҵ������ IBS
	 */
	public PrjMainIBS getPrjMainService() {
		return prjMainService;
	}

	/** ���ص����ݼ� */
	private List returnData = null;

	/**
	 * @return ���ص����ݼ�
	 */
	public List getReturnData() {
		return returnData;
	}

	/** ������ϸ���ݵı��ؼ������ڸ��� */
	private List allDetailList = null;

	/**
	 * ������
	 */
	private PrjMain_DetailSelectListener prjMainDetailSelectListener = null;

	/*
	 * -1-------------------����� -1.1-----------------�м�Split���
	 * -1.1.1---------------��ѡ����� -1.1.1.1-------------��ѡ��Table
	 * -1.1.1.2-------------�ϱ༭��� -1.1.1.2.1-----------�Ƿ����
	 * -1.1.2---------------��ϸ������� -1.1.2.1-------------��ϸ����Table
	 * -1.2-----------------��ť��� -1.2.1---------------"����"��ť
	 * -1.2.2---------------"ȷ��"��ť -1.2.3---------------"ȡ��"��ť
	 */
	/** -1-------------------����� */
	private FPanel panel_main = null;

	/**
	 * @return -1-------------------�����
	 */
	public FPanel getPanel_main() {
		return panel_main;
	}

	/** -1.1-----------------�м�Split��� */
	private FSplitPane spt_main = null;

	/**
	 * @return -1.1-----------------�м�Split���
	 */
	public FSplitPane getSpt_main() {
		return spt_main;
	}

	/** -1.1.1---------------��ѡ����� */
	private FPanel panel_top = null;

	/**
	 * @return -1.1.1---------------��ѡ�����
	 */
	public FPanel getPanel_top() {
		return panel_top;
	}

	/** -1.1.1.1-------------��ѡ��Table */
	private FTable table_top = null;

	/**
	 * @return -1.1.1.1-------------��ѡ��Table
	 */
	public FTable getTable_top() {
		return table_top;
	}

	/** -1.1.1.2-------------�ϱ༭��� */
	private FPanel panel_top_edit = null;

	/**
	 * @return -1.1.1.2-------------�ϱ༭���
	 */
	public FPanel getPanel_top_edit() {
		return panel_top_edit;
	}

	/** -1.1.1.2.1-----------�Ƿ���� */
	private FCheckBox cbx_is_must = null;

	/**
	 * @return -1.1.1.2.1-----------�Ƿ����
	 */

	public FCheckBox getCbx_is_must() {
		return cbx_is_must;
	}

	/** -1.1.2---------------��ϸ������� */
	private FPanel panel_center = null;

	/**
	 * @return -1.1.2---------------��ϸ�������
	 */
	public FPanel getPanel_center() {
		return panel_center;
	}

	/** -1.1.2.1-------------��ϸ����Table */
	private FTable table_center = null;

	/**
	 * @return -1.1.2.1-------------��ϸ����Table
	 */
	public FTable getTable_center() {
		return table_center;
	}

	/** -1.2-----------------��ť��� */
	private FFlowLayoutPanel panel_bottom = null;

	/**
	 * @return -1.2-----------------��ť���
	 */
	public FFlowLayoutPanel getPanel_bottom() {
		return panel_bottom;
	}

	/** -1.2.1---------------"����"��ť */
	private FButton btn_set = null;

	/**
	 * @return -1.2.1---------------"����"��ť
	 */
	public FButton getBtn_set() {
		return btn_set;
	}

	/** -1.2.2---------------"ȷ��"��ť */
	private FButton btn_ok = null;

	/**
	 * @return -1.2.2---------------"ȷ��"��ť
	 */
	public FButton getBtn_ok() {
		return btn_ok;
	}

	/** -1.2.3---------------"ȡ��"��ť */
	private FButton btn_cancel = null;

	/**
	 * @return -1.2.3---------------"ȡ��"��ť
	 */
	public FButton getBtn_cancel() {
		return btn_cancel;
	}

	/**
	 * �����Ҫ���ص�����
	 */
	public void clearReturnData() {
		this.returnData = null;
	}

	/**
	 * ���ݴ򹳵�ѡ������ѡ������
	 */
	public void selectReturnData() {
		this.returnData = this.table_top.getSelectedDataByCheckBox();
	}

	/**
	 * ���캯��
	 * 
	 * @param data
	 *            ���ݽ��������ݼ�����������Ĭ����ʾ
	 */
	public PrjMain_DetailSelectUI(List data) {
		// ��ʼ�����棬����ʾ����
		super(Global.mainFrame, "ѡ��ؼ�", true);
		// ��ʼ�����񣬼����ȶ���
		initService();
		try {
			// Ĭ�Ϸ���ֵ�Ǵ��ݽ�����ֵ
			this.returnData = data;
			// ��ʼ������
			dgInit(data);
		} catch ( Exception e ) {
			Log.error(e.getMessage());
		}
	}

	/**
	 * ��ʼ�����񣬼����ȶ���
	 */
	private void initService() {
		// ��ʼ��ҵ�������
		BeanFactory beanfactory = BeanFactoryUtil.getBeanFactory("gov/nbcs/rp/prjset/prjmain/conf/PrjMainConf.xml");
		prjMainService = (PrjMainIBS) beanfactory.getBean("rp.PrjMainService");
		// ��ʼ������
		prjMainDetailSelectListener = new PrjMain_DetailSelectListener(this);
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
	private FBaseTableColumn returnFtableColumn(String column_id,
			String column_name, String alignment, int width) {
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

	/**
	 * ��ȡ���������
	 * 
	 * @return ���������
	 */
	private FPanel getUpPanel() {
		try {
			// ����չʾTable�ؼ�
			table_top = new FTable(false);
			// ����Checkboxѡ��ؼ�
			table_top.setIsCheck(true);
			table_top.addColumn(returnFtableColumn("comp_id", "�ؼ�����", FBaseTableColumn.RIGHT, 60));
			table_top.addColumn(returnFtableColumn("is_must_name", "�Ƿ����", FBaseTableColumn.RIGHT, 60));
			table_top.addColumn(returnFtableColumn("comp_name", "�ؼ�����", FBaseTableColumn.RIGHT, 100));
			table_top.addColumn(returnFtableColumn("comp_ename", "�ֶ�����", FBaseTableColumn.RIGHT, 60));
			table_top.addColumn(returnFtableColumn("field_type_name", "�ؼ�����", FBaseTableColumn.RIGHT, 60));
			table_top.addColumn(returnFtableColumn("is_comm", "�����ֶ�", FBaseTableColumn.RIGHT, 60));
			table_top.addColumn(returnFtableColumn("comp_min_len", "��С����", FBaseTableColumn.RIGHT, 60));
			table_top.addColumn(returnFtableColumn("comp_max_len", "��󳤶�", FBaseTableColumn.RIGHT, 60));
			table_top.addColumn(returnFtableColumn("comp_min_value", "��Сֵ", FBaseTableColumn.RIGHT, 80));
			table_top.addColumn(returnFtableColumn("comp_max_value", "���ֵ", FBaseTableColumn.RIGHT, 80));
			table_top.addColumn(returnFtableColumn("is_hide", "���ؿؼ�", FBaseTableColumn.RIGHT, 60));
			table_top.addColumn(returnFtableColumn("data_format", "��ʽ���ַ���", FBaseTableColumn.RIGHT, 80));
			// ��������
			table_top.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					prjMainDetailSelectListener.table_topValueChanged(e);
				}
			});
			table_top.setCheckBoxAffectedByClickRow(false);
		} catch ( Exception e ) {
		}
		cbx_is_must = new FCheckBox("����");
		cbx_is_must.setEnabled(true);
		cbx_is_must.addValueChangeListener(new ValueChangeListener(){

			public void valueChanged(ValueChangeEvent arg0) {
				prjMainDetailSelectListener.cbx_is_mustValueChanged(arg0);
			}
			
		});
		RowPreferedLayout rowPreferedLayout = new RowPreferedLayout(1);
		rowPreferedLayout.setColumnWidth(15);
		rowPreferedLayout.setColumnGap(2);
		panel_top_edit = new FPanel();
		panel_top_edit.setLayout(rowPreferedLayout);
		panel_top_edit.addControl(cbx_is_must, new TableConstraints(1, 1, false, true));
		// ���������ĳ�ʼ�������ҷ������Table
		panel_top = new FPanel();
		panel_top.setLayout(new BorderLayout());
		panel_top.addControl(panel_top_edit, BorderLayout.SOUTH);
		panel_top.addControl(table_top, BorderLayout.CENTER);
		return panel_top;
	}

	/**
	 * ��ȡ��ϸ�������
	 * 
	 * @return ��ϸ���
	 */
	private FPanel getDownPanel() {
		try {
			// ����չʾTable�ؼ�
			table_center = new FTable(false);
			table_center.addColumn(returnFtableColumn("item_code", "����", FBaseTableColumn.RIGHT, 60));
			table_center.addColumn(returnFtableColumn("item_name", "����", FBaseTableColumn.RIGHT, 160));
			table_center.setRowSelectionAllowed(true);
			table_center.setColumnSelectionAllowed(false);
		} catch ( Exception e ) {
		}
		// ��ϸ�������ĳ�ʼ�������ҷ������Table
		panel_center = new FPanel();
		panel_center.setLayout(new BorderLayout());
		panel_center.addControl(table_center, BorderLayout.CENTER);
		return panel_center;
	}

	/**
	 * ������壨���Ե�����С��Split���Ĵ���
	 * 
	 * @return �������
	 */
	private FSplitPane getSplitPanel() {
		// ���Ĵ���
		spt_main = new FSplitPane();
		spt_main.setOrientation(JSplitPane.VERTICAL_SPLIT);
		spt_main.setResizeWeight(0.60);
		// ���������
		spt_main.addControl(this.getUpPanel());
		// �������
		spt_main.addControl(this.getDownPanel());
		return spt_main;
	}

	/**
	 * ��ť���Ĵ���
	 * 
	 * @return ��ť���
	 */
	private FFlowLayoutPanel getButtonPanel() {
		// "����"��ť�Ĵ������������
		btn_set = new FButton();
		btn_set.setTitle("��  ��");
		btn_set.setEnabled(true);
		// "ȷ��"��ť�Ĵ������������
		btn_ok = new FButton();
		btn_ok.setTitle("ȷ  ��");
		btn_ok.setEnabled(true);
		// "ȡ��"��ť�Ĵ������������
		btn_cancel = new FButton();
		btn_cancel.setTitle("ȡ  ��");
		btn_cancel.setEnabled(true);
		// �����صļ�������
		btn_set.addActionListener(new RefActionListener(prjMainDetailSelectListener, "btn_setAction"));
		btn_ok.addActionListener(new RefActionListener(prjMainDetailSelectListener, "btn_okAction"));
		btn_cancel.addActionListener(new RefActionListener(prjMainDetailSelectListener, "btn_cancelAction"));
		// ��ť���Ĵ����ͷ���
		panel_bottom = new FFlowLayoutPanel();
		panel_bottom.setAlignment(2);
		panel_bottom.addControl(btn_set);
		panel_bottom.addControl(btn_ok);
		panel_bottom.addControl(btn_cancel);
		return panel_bottom;
	}

	/**
	 * �������岼��
	 */
	private void createMainPanel() {
		// ��ť���ķ���
		panel_main.addControl(this.getButtonPanel(), BorderLayout.SOUTH);
		// huju���ķ���
		panel_main.addControl(this.getSplitPanel(), BorderLayout.CENTER);
	}

	/**
	 * ����ĳ�ʼ��
	 * 
	 * @param data
	 */
	private void dgInit(List data) {
		try {
			// �����Ĵ���
			panel_main = new FPanel();
			panel_main.setLayout(new BorderLayout());
			// ���������岢���ý�������
			this.createMainPanel();
			this.getContentPane().add(panel_main);
			// ���������������
			Dimension d = new Dimension(600, 500);
			this.setSize(d);
			this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - d.width) / 2 + 60, (Toolkit.getDefaultToolkit().getScreenSize().height - d.height) / 2 - 60);
			// ��ʼ�������ݣ��������ݼ��ӱ�Ļ��棩
			this.initTableData();
			// ����Ĭ��ֵ�趨�����
			this.setTableData(data);
			// ��ʾ����
			this.setVisible(true);
		} catch ( Exception e ) {
			Log.error("PrjMain_DetailSelectUI.class�����ʼ��ʧ�ܣ�");
			Log.error(e.getMessage());
		}
	}

	/**
	 * ��ʼ��������
	 */
	public void initTableData() {
		// ��ɾ����������
		this.table_top.deleteAllRows();
		// ��ȡ��������ݼ�
		List list = this.prjMainService.getAllComponetsList();
		// ������������ݼ�
		this.table_top.setData(list);
		// ��ȡ�ӱ�����ݼ�
		allDetailList = this.prjMainService.getAllComBoxValueList();
	}

	/**
	 * ������������
	 * 
	 * @param data
	 */
	private void setTableData(List data) {
		// �������ݵı���б�
		List allItem = new Vector();

		// �����ݵı���б�
		List selectItemID = new Vector();
		List selectItemIsMust = new Vector();
		List selectItemIsMustName = new Vector();
		// ��ȡ�������ݵı���б�
		if ( table_top.getData() != null ) {
			for ( int i = 0 ; i < this.table_top.getData().size() ; i++ ) {
				String s = ((XMLData) table_top.getData().get(i)).get("comp_id").toString();
				allItem.add(s);

			}
		}
		// /��ȡ�����ݵı���б�
		if ( data != null ) {
			for ( int i = 0 ; i < data.size() ; i++ ) {
				String s = ((XMLData) data.get(i)).get("comp_id").toString();
				selectItemID.add(s);
				s = ((XMLData) data.get(i)).get("is_must").toString();
				selectItemIsMust.add(s);
				s = ((XMLData) data.get(i)).get("is_must_name").toString();
				selectItemIsMustName.add(s);
			}
		}
		// �жϲ��Ҷ����ݽ��д�
		for ( int i = 0 ; i < allItem.size() ; i++ ) {
			table_top.setCheckBoxSelectedAtRow(i, false);
			// �ж��Ƿ���ڴ򹳵�����
			int index = selectItemID.indexOf(allItem.get(i));
			if ( index != -1 ) {
				Map map = (Map) table_top.getData().get(i);
				map.put("is_must", selectItemIsMust.get(index));
				map.put("is_must_name", selectItemIsMustName.get(index));
				table_top.getData().set(i, map);
				table_top.setCheckBoxSelectedAtRow(i, true);
			}
		}
	}

	/**
	 * ������ϸ�������
	 */
	public void setDetailTableData() {
		// ��ɾ��������
		table_center.deleteAllRows();
		// ��ȡ��ǰ��������
		XMLData currentRow = table_top.getCurrentRow();
		// ���û���������ݣ��˳�
		if ( currentRow == null ) {
			return;
		}
		// �õ��ؼ����
		String comp_id = currentRow.get("comp_id").toString();
		List list = new Vector();
		// ѭ���õ������ϸ����
		for ( int i = 0 ; i < this.allDetailList.size() ; i++ ) {
			String newComp_id = ((Map) allDetailList.get(i)).get("comp_id").toString();
			// �ж��Ƿ�Ŀؼ���Ӧ������
			if ( comp_id.equals(newComp_id) ) {
				XMLData xmlData = new XMLData();
				xmlData.put("comp_id", ((Map) allDetailList.get(i)).get("comp_id"));
				xmlData.put("item_code", ((Map) allDetailList.get(i)).get("item_code"));
				xmlData.put("item_name", ((Map) allDetailList.get(i)).get("item_name"));
				list.add(xmlData);
			}
		}
		// ����Data
		this.table_center.setData(list);
	}

}
