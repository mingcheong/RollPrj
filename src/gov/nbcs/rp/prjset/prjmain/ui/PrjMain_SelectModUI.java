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
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FFlowLayoutPanel;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RefActionListener;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.ValueChangeEvent;
import com.foundercy.pf.control.ValueChangeListener;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.framework.systemmanager.FToolBarPanel;
import com.foundercy.pf.util.BeanFactoryUtil;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.service.Log;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.prjset.prjmain.action.PrjMain_SelectModListener;
import gov.nbcs.rp.prjset.prjmain.ibs.PrjMainIBS;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.springframework.beans.factory.BeanFactory;


/**
 * <p>
 * Title:��Ŀ�걨�ؼ�ά������
 * </p>
 * <p>
 * Description:��Ŀ�걨�����涨��ʱ����ؿؼ���ά������
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
public class PrjMain_SelectModUI extends JDialog {
	/***/
	private static final long serialVersionUID = 1L;

	/** ģ���״̬-NORMAL_STATUS��ADD_STATUS��MOD_STATUS��DEL_STATUS */
	private int modStatus = PrjMainIBS.NORMAL_STATUS;

	/**
	 * @return ģ���״̬-NORMAL_STATUS��ADD_STATUS��MOD_STATUS��DEL_STATUS
	 */
	public int getModStatus() {
		return modStatus;
	}

	/**
	 * ����ģ���״̬�����ҽ��水ť������Ҫ�����ı�
	 * 
	 * @param modStatus
	 *            ģ���״̬-NORMAL_STATUS��ADD_STATUS��MOD_STATUS��DEL_STATUS
	 */
	public void setModStatus(int modStatus) {
		this.modStatus = modStatus;
		// �������ð�ť��״̬
		this.setButtons();
	}

	/** ҵ������ */
	private PrjMainIBS prjMainService;

	/**
	 * @return ҵ������
	 */
	public PrjMainIBS getPrjMainService() {
		return prjMainService;
	}

	/** ������ */
	private PrjMain_SelectModListener prjMain_SelectModListener;

	/*
	 * -1-------------------����� -1.0-----------------��ť���
	 * -1.0.1---------------"�ر�"��ť -1.0.2---------------"���"��ť
	 * -1.0.3---------------"ɾ��"��ť -1.0.4---------------"�޸�"��ť
	 * -1.0.5---------------"����"��ť -1.0.6---------------"ȡ��"��ť
	 * -1.0.7---------------"��ѯ"��ť -1.0.8---------------"����"��ť
	 * -1.1-----------------�м���� -1.1.1---------------�м���������壬�������νṹ
	 * -1.1.1.1-------------������ʾ�� -1.1.2---------------�м�����ұ���壬����������ʾ����
	 * -1.1.2.1-------------����������ʾ��Ƭ,�����ϰ벿�� -1.1.2.1.1-----------�ؼ����
	 * -1.1.2.1.2-----------�ؼ��������� -1.1.2.1.3-----------�ؼ��ֶ�����
	 * -1.1.2.1.4-----------�ؼ����� 1:�ַ��� 2:���� 3:������ 4:������ 5:���� 6:ʱ��
	 * -1.1.2.1.5-----------�Ƿ������ -1.1.2.1.6-----------�ؼ���С����
	 * -1.1.2.1.7-----------�ؼ���󳤶� -1.1.2.1.8-----------�ؼ���Сֵ
	 * -1.1.2.1.9-----------�ؼ����ֵ -1.1.2.1.10----------�Ƿ�����
	 * -1.1.2.1.11----------��ʽ���ַ��� -1.1.2.2-------------��ϸ������ʾ��Ƭ,�����°벿��
	 * -1.1.2.2.1-----------��ϸ���ݱ༭���� -1.1.2.2.1.1---------��ϸ���ݱ༭���������ť���
	 * -1.1.2.2.1.1.1-------"�����ϸ"��ť -1.1.2.2.1.1.2-------"�޸���ϸ"��ť
	 * -1.1.2.2.1.1.3-------"ɾ����ϸ"��ť -1.1.2.2.1.2---------��ϸ���ݱ༭������������
	 * -1.1.2.2.1.2.1-------�������Ӧ���ݵı��� -1.1.2.2.1.2.2-------�������Ӧ���ݵ�����
	 * -1.1.2.2.2-----------��ϸ������ʾ��� -1.1.2.2.2.1---------��ϸ������ʾ�ؼ�
	 */
	/** -1-------------------����� */
	private FPanel panel_main = null;

	/**
	 * @return -1-------------------�����
	 */
	public FPanel getPanel_main() {
		return panel_main;
	}

	/** -1.0-----------------��ť��� */
	private FToolBarPanel toolBar = null;

	/**
	 * @return -1.0-----------------��ť���
	 */
	public FToolBarPanel getToolBar() {
		return toolBar;
	}

	/** -1.0.1-"�ر�"��ť */
	private FButton btn_close = null;

	/**
	 * @return -1.0.1-"�ر�"��ť
	 */
	public FButton getBtn_close() {
		return btn_close;
	}

	/** -1.0.2-"���"��ť */
	private FButton btn_add = null;

	/**
	 * @return -1.0.2-"���"��ť
	 */
	public FButton getBtn_add() {
		return btn_add;
	}

	/** -1.0.3-"ɾ��"��ť */
	private FButton btn_del = null;

	/**
	 * @return -1.0.3-"ɾ��"��ť
	 */
	public FButton getBtn_del() {
		return btn_del;
	}

	/** -1.0.4-"�޸�"��ť */
	private FButton btn_mod = null;

	/**
	 * @return -1.0.4-"�޸�"��ť
	 */
	public FButton getBtn_mod() {
		return btn_mod;
	}

	/** -1.0.5-"����"��ť */
	private FButton btn_save = null;

	/**
	 * @return -1.0.5-"����"��ť
	 */
	public FButton getBtn_save() {
		return btn_save;
	}

	/** -1.0.6-"ȡ��"��ť */
	private FButton btn_cancel = null;

	/**
	 * @return -1.0.6-"ȡ��"��ť
	 */
	public FButton getBtn_cancel() {
		return btn_cancel;
	}

	/** -1.0.7-"��ѯ"��ť */
	// private FButton btn_qry = null;
	/**
	 * @return -1.0.7-"��ѯ"��ť
	 */
	// public FButton getBtn_qry() {
	// return btn_qry;
	// }
	/** -1.1-----------------�м���� */
	private FSplitPane spt_main = null;

	/**
	 * @return -1.1-----------------�м����
	 */
	public FSplitPane getSpt_main() {
		return spt_main;
	}

	/** -1.1.1---------------�м���������壬�������νṹ */
	private FScrollPane spn_left = null;

	/**
	 * @return -1.1.1---------------�м���������壬�������νṹ
	 */
	public FScrollPane getSpn_left() {
		return spn_left;
	}

	/** -1.1.1.1-------------������ʾ�� */
	private CustomTree tree_data = null;

	/**
	 * @return -1.1.1.1-------------������ʾ��
	 */
	public CustomTree getTree_data() {
		return tree_data;
	}

	/** -1.1.2---------------�м�����ұ���壬����������ʾ���� */
	private FPanel panel_right = null;

	/**
	 * @return -1.1.2---------------�м�����ұ���壬����������ʾ����
	 */
	public FPanel getSpt_right() {
		return panel_right;
	}

	/** -1.1.2.1-------------����������ʾ��Ƭ,�����ϰ벿�� */
	private FPanel panel_right_up = null;

	/**
	 * @return -1.1.2.1-------------����������ʾ��Ƭ,�����ϰ벿��
	 */
	public FPanel getPanel_right_up() {
		return panel_right_up;
	}

	/** -1.1.2.1.1-�ؼ���� */
	private FTextField comp_id = null;

	/**
	 * @return -1.1.2.1.1-�ؼ����
	 */
	public FTextField getComp_id() {
		return comp_id;
	}

	/** -1.1.2.1.2-�ؼ��������� */
	private FTextField comp_name = null;

	/**
	 * @return -1.1.2.1.2-�ؼ���������
	 */
	public FTextField getComp_name() {
		return comp_name;
	}

	/** -1.1.2.1.3-�ؼ��ֶ����� */
	private FTextField comp_ename = null;

	/**
	 * @return -1.1.2.1.3-�ؼ��ֶ�����
	 */
	public FTextField getComp_ename() {
		return comp_ename;
	}

	/** -1.1.2.1.4-�ؼ����� 1:�ַ��� 2:���� 3:������ 4:������ 5:���� 6:ʱ�� */
	private FComboBox field_type = null;

	/**
	 * @return-1.1.2.1.4-�ؼ����� 1:�ַ��� 2:���� 3:������ 4:������ 5:���� 6:ʱ��
	 */
	public FComboBox getField_type() {
		return field_type;
	}

	/** -1.1.2.1.5-�Ƿ������ */
	// private FCheckBox is_comm = null;
	/**
	 * @return -1.1.2.1.5-�Ƿ������
	 */
	// public FCheckBox getIs_comm() {
	// return is_comm;
	// }
	/** -1.1.2.1.6-�ؼ���С���� */
	// private FIntegerField comp_min_len = null;
	/**
	 * @return -1.1.2.1.6-�ؼ���С����
	 */
	// public FIntegerField getComp_min_len() {
	// return comp_min_len;
	// }
	/** -1.1.2.1.7-�ؼ���󳤶� */
	// private FIntegerField comp_max_len = null;
	/**
	 * @return -1.1.2.1.7-�ؼ���󳤶�
	 */
	// public FIntegerField getComp_max_len() {
	// return comp_max_len;
	// }
	/** -1.1.2.1.8-�ؼ���Сֵ */
	// private FTextField comp_min_value = null;
	/**
	 * @return -1.1.2.1.8-�ؼ���Сֵ
	 */
	// public FTextField getComp_min_value() {
	// return comp_min_value;
	// }
	/** -1.1.2.1.9-�ؼ����ֵ */
	// private FTextField comp_max_value = null;
	/**
	 * @return -1.1.2.1.9-�ؼ����ֵ
	 */
	// public FTextField getComp_max_value() {
	// return comp_max_value;
	// }
	/** -1.1.2.1.10-�Ƿ����� */
	private FCheckBox is_hide = null;

	/**
	 * @return -1.1.2.1.10-�Ƿ�����
	 */
	public FCheckBox getIs_hide() {
		return is_hide;
	}

	/** -1.1.2.1.11-��ʽ���ַ��� */
	// private FTextField data_format = null;
	/**
	 * @return -1.1.2.1.11-��ʽ���ַ���
	 */
	// public FTextField getData_format() {
	// return data_format;
	// }
	/** -1.1.2.2-------------��ϸ������ʾ��Ƭ,�����°벿�� */
	private FPanel panel_right_down = null;

	/**
	 * @return -1.1.2.2-------------��ϸ������ʾ��Ƭ,�����°벿��
	 */
	public FPanel getPanel_right_down() {
		return panel_right_down;
	}

	/** -1.1.2.2.1-----------��ϸ���ݱ༭���� */
	private FPanel panel_right_down_detail = null;

	/**
	 * @return -1.1.2.2.1-----------��ϸ���ݱ༭����
	 */
	public FPanel getPanel_right_down_detail() {
		return panel_right_down_detail;
	}

	/** -1.1.2.2.1.1---------��ϸ���ݱ༭���������ť��� */
	private FFlowLayoutPanel panel_right_down_buttons = null;

	/**
	 * @return -1.1.2.2.1.1---------��ϸ���ݱ༭���������ť���
	 */
	public FFlowLayoutPanel getPanel_right_down_buttons() {
		return panel_right_down_buttons;
	}

	/** -1.1.2.2.2.1-"�����ϸ"��ť */
	private FButton btn_adddetail = null;

	/**
	 * @return -1.1.2.2.2.1-"�����ϸ"��ť
	 */
	public FButton getBtn_adddetail() {
		return btn_adddetail;
	}

	/** -1.1.2.2.2.2-"�޸���ϸ"��ť */
	private FButton btn_moddetail = null;

	/**
	 * @return -1.1.2.2.2.2-"�޸���ϸ"��ť
	 */
	public FButton getBtn_moddetail() {
		return btn_moddetail;
	}

	/** -1.1.2.2.2.3-"ɾ����ϸ"��ť */
	private FButton btn_deldetail = null;

	/**
	 * @return -1.1.2.2.2.3-"ɾ����ϸ"��ť
	 */
	public FButton getBtn_deldetail() {
		return btn_deldetail;
	}

	/** -1.1.2.2.1.2---------��ϸ���ݱ༭������������ */
	private FPanel panel_right_down_edit = null;

	/**
	 * @return -1.1.2.2.1.2---------��ϸ���ݱ༭������������
	 */
	public FPanel getPanel_right_down_edit() {
		return panel_right_down_edit;
	}

	/** -1.1.2.2.1.2.1-�������Ӧ���ݵı��� */
	private FTextField item_code = null;

	/**
	 * @return -1.1.2.2.1.2.1-�������Ӧ���ݵı���
	 */
	public FTextField getItem_code() {
		return item_code;
	}

	/** -1.1.2.2.1.2.2-�������Ӧ���ݵ����� */
	private FTextField item_name = null;

	/**
	 * @return -1.1.2.2.1.2.2-�������Ӧ���ݵ�����
	 */
	public FTextField getItem_name() {
		return item_name;
	}

	/** -1.1.2.2.2-----------��ϸ������ʾ��� */
	private FPanel panel_right_down_table = null;

	/**
	 * @return -1.1.2.2.2-----------��ϸ������ʾ���
	 */
	public FPanel getPanel_right_down_table() {
		return panel_right_down_table;
	}

	/** -1.1.2.2.2.1---------��ϸ������ʾ�ؼ� */
	private FTable table_data_detail = null;

	/**
	 * @return -1.1.2.2.2.1---------��ϸ������ʾ�ؼ�
	 */
	public FTable getTable_data_detail() {
		return table_data_detail;
	}

	/**
	 * ���캯����������Ӧ�ĳ�ʼ������
	 */
	public PrjMain_SelectModUI() {
		// ��������
		super(Global.mainFrame, "�ؼ�ά��", true);
		// ��ʼ��ҵ������ͼ�����
		initService();
		try {
			// ��ʼ������
			dgInit();
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
	}

	/**
	 * ��ʼ��ҵ������ͼ�����
	 */
	private void initService() {
		BeanFactory beanfactory = BeanFactoryUtil
				.getBeanFactory("gov/nbcs/rp/prjset/prjmain/conf/PrjMainConf.xml");
		// ���ҵ������
		prjMainService = (PrjMainIBS) beanfactory.getBean("rp.PrjMainService");
		// ����������
		prjMain_SelectModListener = new PrjMain_SelectModListener(this);
	}

	/**
	 * ���ܰ�ť�ĳ�ʼ��:"�ر�"��"����"��"ɾ��"��
	 */
	private void createButtons() {
		// "�ر�"��ť�Ĵ��������
		btn_close = new FButton();
		btn_close.setTitle("�ر�");
		btn_close.setIcon("images/rp/close.gif");
		// "����"��ť�Ĵ��������
		btn_add = new FButton();
		btn_add.setTitle("����");
		btn_add.setIcon("images/rp/add.gif");
		// "ɾ��"��ť�Ĵ��������
		btn_del = new FButton();
		btn_del.setTitle("ɾ��");
		btn_del.setIcon("images/rp/del.gif");
		// "�޸�"��ť�Ĵ��������
		btn_mod = new FButton();
		btn_mod.setTitle("�޸�");
		btn_mod.setIcon("images/rp/mod.gif");
		// "����"��ť�Ĵ��������
		btn_save = new FButton();
		btn_save.setTitle("����");
		btn_save.setIcon("images/rp/save.gif");
		// "ȡ��"��ť�Ĵ��������
		btn_cancel = new FButton();
		btn_cancel.setTitle("ȡ��");
		btn_cancel.setIcon("images/rp/cancl.gif");
		// "ˢ��"��ť�Ĵ��������
		// btn_qry = new FButton();
		// btn_qry.setTitle("ˢ��");
		// btn_qry.setIcon("images/rp/query.gif");
		// ��Ӽ���
		btn_close.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_closeAction"));
		btn_add.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_addAction"));
		btn_del.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_delAction"));
		btn_mod.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_modAction"));
		btn_save.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_saveAction"));
		btn_cancel.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_cancelAction"));
		// btn_qry.addActionListener(new
		// RefActionListener(prjMain_SelectModListener, "btn_qryAction"));
		// ������ť�����ҽ���ť��˳����ӽ�ȥ
		toolBar = new FToolBarPanel();
		toolBar.addControl(btn_close);
		toolBar.addSeparator();// ���һ���ָ���
		toolBar.addControl(btn_add);
		toolBar.addControl(btn_del);
		toolBar.addControl(btn_mod);
		toolBar.addControl(btn_save);
		toolBar.addControl(btn_cancel);
		// toolBar.addControl(btn_qry);
		// ��ť��Ƕ��ģ��
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(toolBar, BorderLayout.NORTH);
	}

	/**
	 * �������벿�ֵĳ�ʼ����������ʾ��
	 * 
	 * @return �������벿��
	 */
	private FScrollPane getLeftPanel() {
		try {
			// ������ʾ���Ĵ���
			tree_data = new CustomTree();
			tree_data.setEditable(false);
			// ��������
			tree_data.addTreeSelectionListener(new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent e) {
					prjMain_SelectModListener.tree_datavalueChanged(e);
				}
			});
			tree_data.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					prjMain_SelectModListener.tree_dataClicked(e);
				}
			});
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// �����Ĵ���
		spn_left = new FScrollPane();
		spn_left.addControl(tree_data);
		return spn_left;
	}

	/**
	 * ������Ұ벿�������ĳ�ʼ������ʾ��������
	 */
	private void setPanelDataMain() {
		comp_id = new FTextField("�ؼ����:");
		comp_id.setProportion(0.24f);
		comp_id.setEditable(false);
		comp_id.setVisible(true);
		// "�ؼ�����"�ؼ��Ĵ����������Լ����
		comp_name = new FTextField("�ؼ�����:");
		comp_name.setProportion(0.24f);
		comp_name.setEditable(false);
		comp_name.setVisible(true);
		// "�ؼ��ֶ�����"�ؼ��Ĵ����������Լ����
		comp_ename = new FTextField("�ؼ��ֶ�����:");
		comp_ename.setProportion(0.24f);
		comp_ename.setEditable(false);
		comp_ename.setVisible(true);
		// "�ؼ�����"�ؼ��Ĵ����������Լ����
		field_type = new FComboBox("�ؼ�����:");
		field_type.setProportion(0.24f);
		field_type.setEditable(false);
		field_type.setEnabled(false);
		field_type.setRefModel("#+1#¼���+4#������");
		field_type.setVisible(true);
		// "������"�ؼ��Ĵ����������Լ����
		// is_comm = new FCheckBox("������:");
		// is_comm.setProportion(0.24f);
		// is_comm.setEditable(false);
		// is_comm.setVisible(true);
		// "��С����"�ؼ��Ĵ����������Լ����
		// comp_min_len = new FIntegerField("��С����:");
		// comp_min_len.setProportion(0.24f);
		// comp_min_len.setEditable(false);
		// comp_min_len.setVisible(true);
		// // "��󳤶�"�ؼ��Ĵ����������Լ����
		// comp_max_len = new FIntegerField("��󳤶�:");
		// comp_max_len.setProportion(0.24f);
		// comp_max_len.setEditable(false);
		// comp_max_len.setVisible(true);
		// // "�ֶ���Сֵ"�ؼ��Ĵ����������Լ����
		// comp_min_value = new FTextField("�ֶ���Сֵ:");
		// comp_min_value.setProportion(0.24f);
		// comp_min_value.setEditable(false);
		// comp_min_value.setVisible(true);
		// // "�ֶ����ֵ"�ؼ��Ĵ����������Լ����
		// comp_max_value = new FTextField("�ֶ����ֵ:");
		// comp_max_value.setProportion(0.24f);
		// comp_max_value.setEditable(false);
		// comp_max_value.setVisible(true);
		// "����"�ؼ��Ĵ����������Լ����
		is_hide = new FCheckBox("�Ƿ�����:");
		is_hide.setProportion(0.24f);
		is_hide.setEditable(false);
		is_hide.setVisible(true);
		// "�ֶθ�ʽ"�ؼ��Ĵ����������Լ����
		// data_format = new FTextField("�ֶθ�ʽ:");
		// data_format.setProportion(0.24f);
		// data_format.setEditable(false);
		// data_format.setVisible(true);
		// ������������
		field_type.addValueChangeListener(new ValueChangeListener() {
			public void valueChanged(ValueChangeEvent arg0) {
				field_typeValueChanged(arg0);
			}
		});
		// ��������չʾ����������
		panel_right_up = new FPanel();
		RowPreferedLayout rowPreferedLayout = new RowPreferedLayout(1);
		rowPreferedLayout.setColumnWidth(15);
		rowPreferedLayout.setColumnGap(13);
		panel_right_up.setLayout(rowPreferedLayout);
		panel_right_up.setTitle("��������");
		panel_right_up.addControl(comp_id, new TableConstraints(1, 1, false,
				true));
		panel_right_up.addControl(comp_name, new TableConstraints(1, 2, false,
				true));
		panel_right_up.addControl(comp_ename, new TableConstraints(1, 3, false,
				true));
		panel_right_up.addControl(field_type, new TableConstraints(1, 4, false,
				true));
		// panel_right_up.addControl(is_comm, new TableConstraints(1, 5, false,
		// true));
		// panel_right_up.addControl(comp_min_len, new TableConstraints(1, 6,
		// false, true));
		// panel_right_up.addControl(comp_max_len, new TableConstraints(1, 7,
		// false, true));
		// panel_right_up.addControl(comp_min_value, new TableConstraints(1, 8,
		// false, true));
		// panel_right_up.addControl(comp_max_value, new TableConstraints(1, 9,
		// false, true));
		panel_right_up.addControl(is_hide, new TableConstraints(1, 10, false,
				true));
		// panel_right_up.addControl(data_format, new TableConstraints(1, 11,
		// false, true));
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
	 * ������Ұ벿�������ĳ�ʼ������ʾ�ӱ�����
	 */
	private void setPanelDataDetail() {
		// "�����ϸ"��ť�Ĵ��������
		btn_adddetail = new FButton();
		btn_adddetail.setTitle("�����ϸ");
		btn_adddetail.setEnabled(true);
		btn_adddetail.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_adddetailAction"));
		// "�޸���ϸ"��ť�Ĵ��������
		btn_moddetail = new FButton();
		btn_moddetail.setTitle("�޸���ϸ");
		btn_moddetail.setEnabled(true);
		btn_moddetail.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_moddetailAction"));
		// "ɾ����ϸ"��ť�Ĵ��������
		btn_deldetail = new FButton();
		btn_deldetail.setTitle("ɾ����ϸ");
		btn_deldetail.setEnabled(true);
		btn_deldetail.addActionListener(new RefActionListener(
				prjMain_SelectModListener, "btn_deldetailAction"));
		// ������ť��岢����ť���ý�ȥ
		panel_right_down_buttons = new FFlowLayoutPanel();
		panel_right_down_buttons.setAlignment(2);
		panel_right_down_buttons.addControl(btn_adddetail);
		panel_right_down_buttons.addControl(btn_moddetail);
		panel_right_down_buttons.addControl(btn_deldetail);
		// /////////////////////////////////////////////////////////////
		// "��ϸ���ݱ���"�ؼ��Ĵ����������Լ����
		item_code = new FTextField("��ϸ����:");
		item_code.setProportion(0.24f);
		item_code.setEditable(true);
		item_code.setVisible(true);
		// "��ϸ��������"�ؼ��Ĵ����������Լ����
		item_name = new FTextField("��ϸ����:");
		item_name.setProportion(0.24f);
		item_name.setEditable(true);
		item_name.setVisible(true);
		// �����༭��岢���ؼ����ý�ȥ
		RowPreferedLayout layoutPanelTexts = new RowPreferedLayout(2);
		layoutPanelTexts.setColumnWidth(15);
		layoutPanelTexts.setColumnGap(1);
		panel_right_down_edit = new FPanel();
		panel_right_down_edit.setLayout(layoutPanelTexts);
		panel_right_down_edit.addControl(item_code);
		panel_right_down_edit.addControl(item_name);
		// /////////////////////////////////////////////////////////////
		// ����������������
		panel_right_down_detail = new FPanel();
		panel_right_down_detail.setLayout(new BorderLayout());
		panel_right_down_detail.addControl(panel_right_down_buttons,
				BorderLayout.NORTH);
		panel_right_down_detail.addControl(panel_right_down_edit,
				BorderLayout.CENTER);
		panel_right_down_detail.setTopInset(0);
		panel_right_down_detail.setBottomInset(5);
		panel_right_down_detail.setLeftInset(5);
		panel_right_down_detail.setRightInset(5);
		// /////////////////////////////////////////////////////////////
		// ��ϸ����չʾ����
		try {
			// ������ϸ������ʾ�������
			table_data_detail = new FTable();
			table_data_detail.addColumn(returnFtableColumn("item_code", "����",
					FBaseTableColumn.RIGHT, 60));
			table_data_detail.addColumn(returnFtableColumn("item_name", "����",
					FBaseTableColumn.RIGHT, 160));
			table_data_detail.setRowSelectionAllowed(true);
			table_data_detail.setColumnSelectionAllowed(false);
			table_data_detail
					.addListSelectionListener(new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent e) {
							prjMain_SelectModListener
									.table_data_detailValueChanged(e);
						}
					});
		} catch (Exception e) {
		}
		// ��ϸ����չʾ���Ĵ���������
		panel_right_down_table = new FPanel();
		panel_right_down_table.setLayout(new BorderLayout());
		panel_right_down_table.addControl(table_data_detail,
				BorderLayout.CENTER);
		// ��ϸ����չʾ��� �� ��ϸ���ݲ������ ������
		panel_right_down = new FPanel();
		panel_right_down.setLayout(new BorderLayout());
		panel_right_down
				.addControl(panel_right_down_detail, BorderLayout.NORTH);
		panel_right_down
				.addControl(panel_right_down_table, BorderLayout.CENTER);
	}

	/**
	 * ������Ұ벿�ֵĳ�ʼ������ϸ���ݷ������������
	 * 
	 * @return ������Ұ벿��
	 */
	private FPanel getRightPanel() {
		// ��������壬������������������Ĵ�С
		panel_right = new FPanel();
		// ����������ݵĴ���
		this.setPanelDataMain();
		// �������ϸ���ݵĴ���
		this.setPanelDataDetail();
		panel_right.setLayout(new BorderLayout());
		// �������ķ���
		panel_right.addControl(panel_right_up, BorderLayout.NORTH);
		panel_right.addControl(panel_right_down, BorderLayout.CENTER);
		return panel_right;
	}

	/**
	 * �����ĳ�ʼ������ϸ���ݷ������������
	 */
	private void createMainPanel() {
		// ����Split��������������������Ĵ�С
		spt_main = new FSplitPane();
		spt_main.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		spt_main.setResizeWeight(0.40);
		// �ֱ���������������
		spt_main.addControl(this.getLeftPanel());
		spt_main.addControl(this.getRightPanel());
		panel_main.add(spt_main);
	}

	/**
	 * ��ʼ������
	 */
	private void dgInit() {
		try {
			// ������ť��
			this.createButtons();
			panel_main = new FPanel();
			panel_main.setLayout(new RowPreferedLayout(1));
			// ������������
			this.createMainPanel();
			// ������������
			this.getContentPane().add(panel_main);
			Dimension d = new Dimension(800, 600);
			this.setSize(d);
			this
					.setLocation(
							(Toolkit.getDefaultToolkit().getScreenSize().width - d.width) / 2 + 60,
							(Toolkit.getDefaultToolkit().getScreenSize().height - d.height) / 2 - 60);
			this.setResizable(false);
			// ��ʼ��ĳЩ�ؼ���ֵ
			this.initComponent();
			// ��ʾ����
			this.setVisible(true);
		} catch (Exception e) {
			Log.error("PrjMain_SelectModUI.class�����ʼ��ʧ�ܣ�");
			Log.error(e.getMessage());
		}
	}

	/** ��ʼ��ĳЩ�ؼ���ֵ */
	private void initComponent() {
		// ��ʼ�����ݱ�ʾ����ֵ
		this.initTree();
		// ���ð�ť��״̬
		// ���������ѡ��ؼ���Ҫ��ʾ��ϸ����
		// "4"��ָ������
		if (field_type.getValue().equals("4")) {
			// ��岼�ֵ����²���
			panel_right.removeAll();
			panel_right.setLayout(new BorderLayout());
			panel_right.addControl(panel_right_up, BorderLayout.NORTH);
			panel_right.addControl(panel_right_down, BorderLayout.CENTER);
			panel_right.updateUI();
		} else {
			// ��岼�ֵ����²���
			panel_right.removeAll();
			panel_right.setLayout(new BorderLayout());
			panel_right.addControl(panel_right_up, BorderLayout.CENTER);
			panel_right.updateUI();
		}
		this.setModStatus(PrjMainIBS.NORMAL_STATUS);
	}

	/**
	 * ��ʼ�����ݱ�ʾ����ֵ
	 */
	public void initTree() {
		// ����ҵ�����������ṹ��xmldata
		DataSet ds_tree = prjMainService.getComponentsTreeData();
		try {
			// ���������Ϣ
			tree_data.setRootName("���пؼ�");
			tree_data.setDataSet(ds_tree);
			tree_data.setIdName("CHR_ID");
			tree_data.setTextName("CHR_NAME");
			tree_data.setParentIdName("PARENT_ID");
			// �Զ��������νṹ
			tree_data.reset();
		} catch (Exception e) {
			Log.error("��ʼ�����ݱ�ʾ������");
			Log.error(e.getMessage());
		}
	}

	/**
	 * ���ð�ť��״̬
	 */
	private void setButtons() {
		switch (modStatus) {
		// ��ͨ״̬�Ŀؼ���������
		case PrjMainIBS.NORMAL_STATUS: {
			btn_add.setEnabled(true);
			btn_del.setEnabled(true);
			btn_mod.setEnabled(true);
			btn_save.setEnabled(false);
			btn_cancel.setEnabled(false);
			// btn_qry.setEnabled(true);
			tree_data.setEnabled(true);
			comp_id.setEditable(false);
			comp_name.setEditable(false);
			comp_ename.setEditable(false);
			field_type.setEnabled(false);
			// is_comm.setEditable(false);
			// comp_min_len.setEditable(false);
			// comp_max_len.setEditable(false);
			// comp_min_value.setEditable(false);
			// comp_max_value.setEditable(false);
			is_hide.setEditable(false);
			// data_format.setEditable(false);
			// ��ϸ���ݵ���ʾ״̬
			panel_right_down.removeAll();
			panel_right_down.setLayout(new BorderLayout());
			panel_right_down.addControl(panel_right_down_table,
					BorderLayout.CENTER);
			panel_right_down.updateUI();
			panel_right_down.setEnabled(false);
			// �ؼ�������"������"ʱ
			// "4"��ָ������
			if (field_type.getValue().equals("4")) {
				panel_right.removeAll();
				panel_right.setLayout(new BorderLayout());
				panel_right.addControl(panel_right_up, BorderLayout.NORTH);
				panel_right.addControl(panel_right_down, BorderLayout.CENTER);
				panel_right.updateUI();
			} else {
				panel_right.removeAll();
				panel_right.setLayout(new BorderLayout());
				panel_right.addControl(panel_right_up, BorderLayout.CENTER);
				panel_right.updateUI();
			}
		}
			break;
		// ���״̬�Ŀؼ���������
		case PrjMainIBS.ADD_STATUS: {
			btn_add.setEnabled(false);
			btn_del.setEnabled(false);
			btn_mod.setEnabled(false);
			btn_save.setEnabled(true);
			btn_cancel.setEnabled(true);
			// btn_qry.setEnabled(false);
			tree_data.setEnabled(false);
			comp_id.setEditable(false);
			comp_name.setEditable(true);
			comp_ename.setEditable(false);
			field_type.setEnabled(true);
			// is_comm.setEditable(true);
			// comp_min_len.setEditable(true);
			// comp_max_len.setEditable(true);
			// comp_min_value.setEditable(true);
			// comp_max_value.setEditable(true);
			is_hide.setEditable(false);
			// data_format.setEditable(true);
			// ��ϸ���ݵ���ʾ״̬
			panel_right_down.removeAll();
			panel_right_down.setLayout(new BorderLayout());
			panel_right_down.addControl(panel_right_down_detail,
					BorderLayout.NORTH);
			panel_right_down.addControl(panel_right_down_table,
					BorderLayout.CENTER);
			panel_right_down.updateUI();
			panel_right_down.setEnabled(true);
			// �ؼ�������"������"ʱ
			// "4"��ָ������
			if (field_type.getValue().equals("4")) {
				panel_right.removeAll();
				panel_right.setLayout(new BorderLayout());
				panel_right.addControl(panel_right_up, BorderLayout.NORTH);
				panel_right.addControl(panel_right_down, BorderLayout.CENTER);
				panel_right.updateUI();
			} else {
				panel_right.removeAll();
				panel_right.setLayout(new BorderLayout());
				panel_right.addControl(panel_right_up, BorderLayout.CENTER);
				panel_right.updateUI();
			}
		}
			break;
		// �޸�״̬�Ŀؼ���������
		case PrjMainIBS.MOD_STATUS: {
			btn_add.setEnabled(false);
			btn_del.setEnabled(false);
			btn_mod.setEnabled(false);
			btn_save.setEnabled(true);
			btn_cancel.setEnabled(true);
			// btn_qry.setEnabled(false);
			tree_data.setEnabled(false);
			comp_id.setEditable(false);
			comp_name.setEditable(true);
			comp_ename.setEditable(false);
			// ��������ò��ɱ༭
			if (((Boolean) is_hide.getValue()).booleanValue()) {
				field_type.setEnabled(false);
			} else {
				field_type.setEnabled(true);
			}
			// is_comm.setEditable(true);
			// comp_min_len.setEditable(true);
			// comp_max_len.setEditable(true);
			// comp_min_value.setEditable(true);
			// comp_max_value.setEditable(true);
			is_hide.setEditable(false);
			// data_format.setEditable(true);
			// ��ϸ���ݵ���ʾ״̬
			panel_right_down.setEnabled(true);
			panel_right_down.removeAll();
			panel_right_down.setLayout(new BorderLayout());
			panel_right_down.addControl(panel_right_down_detail,
					BorderLayout.NORTH);
			panel_right_down.addControl(panel_right_down_table,
					BorderLayout.CENTER);
			panel_right_down.updateUI();
			// �ؼ�������"������"ʱ
			// "4"��ָ������
			if (field_type.getValue().equals("4")) {
				panel_right.removeAll();
				panel_right.setLayout(new BorderLayout());
				panel_right.addControl(panel_right_up, BorderLayout.NORTH);
				panel_right.addControl(panel_right_down, BorderLayout.CENTER);
				panel_right.updateUI();
			} else {
				panel_right.removeAll();
				panel_right.setLayout(new BorderLayout());
				panel_right.addControl(panel_right_up, BorderLayout.CENTER);
				panel_right.updateUI();
			}
		}
			break;
		// ɾ��״̬�Ŀؼ���������
		case PrjMainIBS.DEL_STATUS: {
		}
			break;
		default:
			break;
		}
	}

	/**
	 * �ؼ�����������仯ʱ״̬�ı仯
	 * 
	 * @param arg0
	 *            �¼���װ����
	 */
	private void field_typeValueChanged(ValueChangeEvent arg0) {
		setButtons();
	}

}
