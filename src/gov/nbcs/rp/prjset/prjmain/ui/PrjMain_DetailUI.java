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

import com.foundercy.pf.control.Control;
import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RefActionListener;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.util.BeanFactoryUtil;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.service.Log;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.prjset.prjmain.action.PrjMain_DetailListener;
import gov.nbcs.rp.prjset.prjmain.ibs.PrjMainIBS;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JDialog;

import org.springframework.beans.factory.BeanFactory;

/**
 * <p>
 * Title:��Ŀ�걨�����������ӣ��޸ģ���Ԫ����
 * </p>
 * <p>
 * Description:��Ŀ�걨�������õ�Ԫ�����ϵ�������ӡ����ߡ��޸ġ�ʱ�����Ľ���
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
public class PrjMain_DetailUI extends JDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * �����洫�ݽ��������ݼ�
	 */
	private DataSet mainData = null;

	/**
	 * @return �����洫�ݽ��������ݼ�
	 */
	public DataSet getMainData() {
		return mainData;
	}

	/**
	 * ��������
	 */
	private int operationType = PrjMainIBS.OPERATION_ADD;

	/**
	 * @return ��������
	 */
	public int getOperationType() {
		return operationType;
	}

	/**
	 *  ҵ������ IBS 
	 */
	private PrjMainIBS prjMainService = null;

	/**
	 * @return ҵ������ IBS 
	 */
	public PrjMainIBS getPrjMainService() {
		return prjMainService;
	}

	/**
	 * ������
	 */
	private PrjMain_DetailListener prjMainDetailListener = null;

	/*
	 * -1-------------------����� 
	 * -1.1-----------------���������
	 * -1.1.1---------------��������� 
	 * -1.1.1.1-------------���ñ���
	 * -1.1.1.2-------------���ñ����� 
	 * -1.1.1.3-------------���ñ���
	 * -1.1.1.4-------------���ñ�ȫ�� 
	 * -1.1.1.5-------------Ĭ������
	 * -1.1.2---------------��ϸ������� 
	 * -1.1.2.1-------------��ϸ���ݲ�����ť���
	 * -1.1.2.1.1-----------"��������"��ť 
	 * -1.1.2.1.2-----------"����"��ť
	 * -1.1.2.1.3-----------"����"��ť 
	 * -1.1.2.2-------------��ϸ������ʾ���
	 * -1.1.2.2.1-----------��ϸ������ʾTable 
	 * -1.2-----------------��ť���
	 * -1.2.1---------------"ȷ��"��ť 
	 * -1.2.2---------------"ȡ��"��ť
	 */
	/** -1-------------------����� */
	private FPanel panel_main = null;

	/**
	 * @return -1-------------------�����
	 */
	public FPanel getPanel_main() {
		return panel_main;
	}

	/** -1.1-----------------��������� */
	private FPanel panel_data = null;

	/**
	 * @return -1.1-----------------���������
	 */
	public FPanel getPanel_data() {
		return panel_data;
	}

	/** -1.1.1---------------��������� */
	private FPanel panel_data_main = null;

	/**
	 * @return -1.1.1---------------���������
	 */
	public FPanel getPanel_data_main() {
		return panel_data_main;
	}

	/** -1.1.1.1-------------���ñ��� */
	private FTextField input_set_id = null;

	/**
	 * @return -1.1.1.1-------------���ñ���
	 */
	public FTextField getInput_set_id() {
		return input_set_id;
	}

	/** -1.1.1.2-------------���ñ����� */
	private FTextField input_set_name = null;

	/**
	 * @return -1.1.1.2-------------���ñ�����
	 */
	public FTextField getInput_set_name() {
		return input_set_name;
	}

	/** -1.1.1.3-------------���ñ��� */
	private FTextField input_set_shortname = null;

	/**
	 * @return -1.1.1.3-------------���ñ���
	 */
	public FTextField getInput_set_shortname() {
		return input_set_shortname;
	}

	/** -1.1.1.4-------------���ñ�ȫ�� */
	private FTextField input_set_longname = null;

	/**
	 * @return -1.1.1.4-------------���ñ�ȫ��
	 */
	public FTextField getInput_set_longname() {
		return input_set_longname;
	}

	/** -1.1.1.5-------------Ĭ������ */
	private FCheckBox is_default = null;

	/**
	 * @return -1.1.1.5-------------Ĭ������
	 */
	public FCheckBox getIs_default() {
		return is_default;
	}

	/** -1.1.2---------------��ϸ������� */
	private FPanel panel_data_detail = null;

	/**
	 * @return -1.1.2---------------��ϸ�������
	 */
	public FPanel getPanel_data_detail() {
		return panel_data_detail;
	}

	/** -1.1.2.1-------------��ϸ���ݲ�����ť��� */
	private FFlowLayoutPanel panel_data_detail_button = null;

	/**
	 * @return -1.1.2.1-------------��ϸ���ݲ�����ť���
	 */
	public FFlowLayoutPanel getPanel_data_detail_button() {
		return panel_data_detail_button;
	}

	/** -1.1.2.1.1-----------"��������"��ť */
	private FButton btn_mod = null;

	/**
	 * @return -1.1.2.1.1-----------"��������"��ť
	 */
	public FButton getBtn_mod() {
		return btn_mod;
	}

	/** -1.1.2.1.2-----------"����"��ť */
	private FButton btn_up = null;

	/**
	 * @return -1.1.2.1.2-----------"����"��ť
	 */
	public FButton getBtn_up() {
		return btn_up;
	}

	/** -1.1.2.1.3-----------"����"��ť */
	private FButton btn_down = null;

	/**
	 * @return -1.1.2.1.3-----------"����"��ť
	 */
	public FButton getBtn_down() {
		return btn_down;
	}

	/** -1.1.2.2-------------��ϸ������ʾ��� */
	private FPanel panel_data_detail_table = null;

	/**
	 * @return -1.1.2.2-------------��ϸ������ʾ���
	 */
	public FPanel getPanel_data_detail_table() {
		return panel_data_detail_table;
	}

	/** -1.1.2.2.1-----------��ϸ������ʾTable */
	private FTable table_detail = null;

	/**
	 * @return -1.1.2.2.1-----------��ϸ������ʾTable
	 */
	public FTable getTable_detail() {
		return table_detail;
	}

	/** -1.2-----------------��ť��� */
	private FFlowLayoutPanel panel_buttons = null;

	/**
	 * @return -1.2-----------------��ť���
	 */
	public FFlowLayoutPanel getPanel_buttons() {
		return panel_buttons;
	}

	/** -1.2.1---------------"ȷ��"��ť */
	private FButton btn_ok = null;

	/**
	 * @return -1.2.1---------------"ȷ��"��ť
	 */
	public FButton getBtn_ok() {
		return btn_ok;
	}

	/** -1.2.2---------------"ȡ��"��ť */
	private FButton btn_cancel = null;

	/**
	 * @return -1.2.2---------------"ȡ��"��ť
	 */
	public FButton getBtn_cancel() {
		return btn_cancel;
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
	 * ���캯������ʼ��������
	 * @param dataSet ���������ݼ�
	 * @param operType ��������
	 */
	public PrjMain_DetailUI(DataSet dataSet, int operType) {
		//���ݲ���������ʾ��ҳ��ı���
		super(Global.mainFrame, "���", true);
		if ( operType == PrjMainIBS.OPERATION_MOD ) {
			this.setTitle("��Ŀ�걨��������(�޸�)");
		} else {
			this.setTitle("��Ŀ�걨��������(���)");
		}
		//��ʼ�����񣬼����ȶ���
		initService();
		try {
			//��ȡȫ�ֵĸ��������ݼ�
			this.mainData = dataSet;
			//��ȡ��������
			operationType = operType;
			//��ʼ������
			dgInit();
		} catch ( Exception e ) {
			Log.error(e.getMessage());
		}
	}

	/**
	 * ��ʼ�����񣬼����ȶ���
	 */
	private void initService() {
		//��ʼ��ҵ�������
		BeanFactory beanfactory = BeanFactoryUtil.getBeanFactory("gov/nbcs/rp/prjset/prjmain/conf/PrjMainConf.xml");
		prjMainService = (PrjMainIBS) beanfactory.getBean("rp.PrjMainService");
		//��ʼ������
		prjMainDetailListener = new PrjMain_DetailListener(this);
	}

	/**
	 * ��ʼ����������������ʾ���
	 * @return ��������ʾ���
	 */
	private Control getMainPanel() {
		//"���ñ���"�ؼ��Ĵ���������
		input_set_id = new FTextField("���ñ���");
		input_set_id.setProportion(0.24f);
		input_set_id.setEditable(false);
		input_set_id.setVisible(true);
		//"���ñ�����"�ؼ��Ĵ���������
		input_set_name = new FTextField("���ñ�����");
		input_set_name.setProportion(0.24f);
		input_set_name.setEditable(true);
		input_set_name.setVisible(true);
		//"���ñ���"�ؼ��Ĵ���������
		input_set_shortname = new FTextField("���ñ���");
		input_set_shortname.setProportion(0.24f);
		input_set_shortname.setEditable(true);
		input_set_shortname.setVisible(true);
		//"���ñ�ȫ��"�ؼ��Ĵ���������
		input_set_longname = new FTextField("���ñ�ȫ��");
		input_set_longname.setProportion(0.24f);
		input_set_longname.setEditable(true);
		input_set_longname.setVisible(true);
		//"Ĭ������"�ؼ��Ĵ���������
		is_default = new FCheckBox("Ĭ������");
		is_default.setProportion(0.24f);
		is_default.setEnabled(true);
		is_default.setVisible(true);
		//�����صļ���
		input_set_name.addValueChangeListener(new ValueChangeListener() {
			public void valueChanged(ValueChangeEvent arg0) {
				prjMainDetailListener.input_set_nameValueChanged(arg0);
			}
		});
		//��������չʾ����������
		panel_data_main = new FPanel();
		RowPreferedLayout rowPreferedLayout = new RowPreferedLayout(1);
		rowPreferedLayout.setColumnWidth(15);
		rowPreferedLayout.setColumnGap(6);
		panel_data_main.setLayout(rowPreferedLayout);
		panel_data_main.setTitle("��������");
		//���ؼ���˳����ӽ������
		panel_data_main.addControl(input_set_id, new TableConstraints(1, 1, false, true));
		panel_data_main.addControl(input_set_name, new TableConstraints(1, 2, false, true));
		panel_data_main.addControl(input_set_shortname, new TableConstraints(1, 3, false, true));
		panel_data_main.addControl(input_set_longname, new TableConstraints(1, 4, false, true));
		panel_data_main.addControl(is_default, new TableConstraints(1, 5, false, true));
		return panel_data_main;
	}

	/**
	 * ��ʼ����������ϸ������ʾ���
	 * @return ��ϸ������ʾ���
	 */
	private FPanel getDetailPanel() {
		//��ϸ���ݲ�����ť-"������ϸ"��ť�Ĵ���������
		btn_mod = new FButton();
		btn_mod.setTitle("������ϸ");
		btn_mod.setEnabled(true);
		btn_mod.addActionListener(new RefActionListener(prjMainDetailListener, "btn_modAction"));
		//��ϸ���ݲ�����ť-"����"��ť�Ĵ���������
		btn_up = new FButton();
		btn_up.setTitle("��");
		btn_up.setEnabled(true);
		btn_up.addActionListener(new RefActionListener(prjMainDetailListener, "btn_upAction"));
		//��ϸ���ݲ�����ť-"����"��ť�Ĵ���������
		btn_down = new FButton();
		btn_down.setTitle("��");
		btn_down.setEnabled(true);
		btn_down.addActionListener(new RefActionListener(prjMainDetailListener, "btn_downAction"));
		//������ϸ��ť��壬������ť��˳����ӽ�ȥ
		panel_data_detail_button = new FFlowLayoutPanel();
		panel_data_detail_button.setAlignment(2);
		panel_data_detail_button.addControl(btn_mod);
		panel_data_detail_button.addControl(btn_up);
		panel_data_detail_button.addControl(btn_down);
		//��ϸ����չʾ��Ĵ���������
		try {
			table_detail = new FTable();
			//��˳����Ӹ���ʾ��
			table_detail.addColumn(returnFtableColumn("comp_id", "�ؼ�����", FBaseTableColumn.RIGHT, 60));
			table_detail.addColumn(returnFtableColumn("is_must_name", "�Ƿ����", FBaseTableColumn.RIGHT, 60));
			table_detail.addColumn(returnFtableColumn("comp_name", "�ؼ�����", FBaseTableColumn.RIGHT, 100));
			table_detail.addColumn(returnFtableColumn("comp_ename", "�ֶ�����", FBaseTableColumn.RIGHT, 60));
			table_detail.addColumn(returnFtableColumn("field_type_name", "�ؼ�����", FBaseTableColumn.RIGHT, 60));
//			table_detail.addColumn(returnFtableColumn("is_comm", "�����ֶ�", FBaseTableColumn.RIGHT, 60));
//			table_detail.addColumn(returnFtableColumn("comp_min_len", "��С����", FBaseTableColumn.RIGHT, 60));
//			table_detail.addColumn(returnFtableColumn("comp_max_len", "��󳤶�", FBaseTableColumn.RIGHT, 60));
//			table_detail.addColumn(returnFtableColumn("comp_min_value", "��Сֵ", FBaseTableColumn.RIGHT, 80));
//			table_detail.addColumn(returnFtableColumn("comp_max_value", "���ֵ", FBaseTableColumn.RIGHT, 80));
			table_detail.addColumn(returnFtableColumn("is_inner", "���ؿؼ�", FBaseTableColumn.RIGHT, 60));
//			table_detail.addColumn(returnFtableColumn("data_format", "��ʽ���ַ���", FBaseTableColumn.RIGHT, 80));
			table_detail.setEnabled(false);
		} catch ( Exception e ) {
			Log.error(e.getMessage());
		}
		//������ϸ����չʾ������Ĵ���������
		panel_data_detail_table = new FPanel();
		panel_data_detail_table.setLayout(new BorderLayout());
		panel_data_detail_table.addControl(table_detail, BorderLayout.CENTER);
		//��ϸ���ݲ�����չʾ�������õ���ϸ�����
		panel_data_detail = new FPanel();
		panel_data_detail.setLayout(new BorderLayout());
		panel_data_detail.addControl(panel_data_detail_button, BorderLayout.NORTH);
		panel_data_detail.addControl(panel_data_detail_table, BorderLayout.CENTER);
		return panel_data_detail;
	}

	/**
	 * ȫ�����ݲ������Ĵ���
	 * @return ȫ�����ݲ������
	 */
	private FPanel getUpPanel() {
		//���ݲ������Ĵ���
		panel_data = new FPanel();
		panel_data.setLayout(new BorderLayout());
		//�����ķ��ã�ʢ��������
		panel_data.addControl(this.getMainPanel(), BorderLayout.NORTH);
		//�����ķ��ã�ʢ����ϸ����
		panel_data.addControl(this.getDetailPanel(), BorderLayout.CENTER);
		return panel_data;
	}

	/**
	 * ��ť���Ĵ���
	 * @return ��ť���
	 */
	private FFlowLayoutPanel getDownPanel() {
		//"ȷ��"��ť�Ĵ���������
		btn_ok = new FButton();
		btn_ok.setTitle("ȷ  ��");
		btn_ok.setEnabled(true);
		//"ȡ��"��ť�Ĵ���������
		btn_cancel = new FButton();
		btn_cancel.setTitle("ȡ  ��");
		btn_cancel.setEnabled(true);
		//���Ӹ���ť�ļ���
		btn_ok.addActionListener(new RefActionListener(prjMainDetailListener, "btn_okAction"));
		btn_cancel.addActionListener(new RefActionListener(prjMainDetailListener, "btn_cancelAction"));
		//��ť���Ĵ���������
		panel_buttons = new FFlowLayoutPanel();
		panel_buttons.setAlignment(2);
		panel_buttons.addControl(btn_ok);
		panel_buttons.addControl(btn_cancel);
		return panel_buttons;
	}

	/**
	 * ��������壬�������ݲ������Ͱ�ť���
	 */
	private void createMainPanel() {
		//��ť���
		panel_main.addControl(this.getDownPanel(), BorderLayout.SOUTH);
		//���ݲ������
		panel_main.addControl(this.getUpPanel(), BorderLayout.CENTER);

	}

	/**
	 * ����ĳ�ʼ��
	 */
	private void dgInit() {
		try {
			//�����Ĵ���
			panel_main = new FPanel();
			panel_main.setLayout(new BorderLayout());
			//���������
			this.createMainPanel();
			this.getContentPane().add(panel_main);
			//���ñ���������
			Dimension d = new Dimension(500, 600);
			this.setSize(d);
			this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - d.width) / 2 + 60, (Toolkit.getDefaultToolkit().getScreenSize().height - d.height) / 2 - 60);
			this.setAllValues();
			//��ʾ������
			this.setVisible(true);
		} catch ( Exception e ) {
			Log.error("PrjMain_DetailUI.class�����ʼ��ʧ�ܣ�");
			Log.error(e.getMessage());
		}
	}

	/**
	 * ��ʼ�����ؼ���ֵ
	 */
	private void setAllValues() {
		//�����"���"�������˳�
		if ( operationType == PrjMainIBS.OPERATION_ADD ) {
			return;
		}
		//û�����ݼ��˳�
		if ( mainData == null ) {
			return;
		}
		try {
			//������������
			input_set_id.setValue(mainData.fieldByName("input_set_id").getString());
			input_set_name.setValue(mainData.fieldByName("input_set_name").getString());
			input_set_shortname.setValue(mainData.fieldByName("input_set_shortname").getString());
			input_set_longname.setValue(mainData.fieldByName("input_set_longname").getString());
			is_default.setValue(Boolean.valueOf(mainData.fieldByName("is_default").getString().equals("1")));
			table_detail.deleteAllRows();
			try {
				List list = prjMainService.getSetDetailValues(input_set_id.getValue().toString());
				table_detail.setData(list);
			} catch ( Exception e ) {
				Log.error(e.getMessage());
			}
		} catch ( Exception e ) {
			Log.error(e.getMessage());
		}
	}

}
