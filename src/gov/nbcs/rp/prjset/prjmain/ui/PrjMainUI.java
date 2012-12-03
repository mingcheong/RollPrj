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

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.RpModulePanel;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.prjset.prjmain.action.PrjMainListener;
import gov.nbcs.rp.prjset.prjmain.ibs.PrjMainIBS;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.springframework.beans.factory.BeanFactory;

import com.foundercy.pf.control.FButton;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.control.FScrollPane;
import com.foundercy.pf.control.FSplitPane;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RefActionListener;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.control.table.FBaseTableColumn;
import com.foundercy.pf.control.table.FTable;
import com.foundercy.pf.framework.systemmanager.FToolBarPanel;
import com.foundercy.pf.util.BeanFactoryUtil;
import com.foundercy.pf.util.service.Log;

/**
 * <p>
 * Title:��Ŀ�걨�������õ�Ԫ����
 * </p>
 * <p>
 * Description:��Ŀ�걨�������ã���Ҫ��ʾ�Ѿ����õ���Ϣ�����ṩ��ɾ�ĵĲ���
 * </p>
 * <p>
 * CreateData 2011-4-27
 * </p>
 * 
 * @author qzc
 * @version 1.0
 */
public class PrjMainUI extends RpModulePanel {
	private static final long serialVersionUID = 1L;

	/** ҵ������ IBS */
	private PrjMainIBS prjMainService;

	/** ҵ������ IBS */
	public PrjMainIBS getPrjMainService() {
		return prjMainService;
	}

	/** ������ */
	private PrjMainListener prjMainListener;
	/*
	 * -1-------------------����� -1.0-----------------��ť���
	 * -1.0.1---------------"���"��ť -1.0.2---------------"ɾ��"��ť
	 * -1.0.3---------------"�޸�"��ť -1.0.4---------------"��ѯ"��ť
	 * -1.0.5---------------"ѡ��ά��"��ť -1.1-----------------�м����
	 * -1.1.1---------------�м���������壬�������νṹ -1.1.1.1-------------������ʾ��
	 * -1.1.2---------------�м�����ұ���壬����������ʾ����
	 * -1.1.2.1-------------����������ʾ��Ƭ,�����ϰ벿�� -1.1.2.1.1-----------���ñ���
	 * -1.1.2.1.2-----------���ñ����� -1.1.2.1.3-----------���ñ���
	 * -1.1.2.1.4-----------���ñ�ȫ�� -1.1.2.1.5-----------Ĭ������
	 * -1.1.2.2-------------��ϸ������ʾ��Ƭ,�����°벿�� -1.1.2.2.1-----------��ϸ������ʾ���
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

	/** -1.0.1---------------"���"��ť */
	private FButton btn_add = null;

	/**
	 * @return -1.0.1---------------"���"��ť
	 */
	public FButton getBtn_add() {
		return btn_add;
	}

	/** -1.0.2---------------"ɾ��"��ť */
	private FButton btn_del = null;

	/**
	 * @return -1.0.2---------------"ɾ��"��ť
	 */
	public FButton getBtn_del() {
		return btn_del;
	}

	/** -1.0.3---------------"�޸�"��ť */
	private FButton btn_mod = null;

	/**
	 * @return -1.0.3---------------"�޸�"��ť
	 */
	public FButton getBtn_mod() {
		return btn_mod;
	}

	/** -1.0.4---------------"��ѯ"��ť */
	private FButton btn_qry = null;

	// �رհ�ť
	private FButton btn_close = null;

	/**
	 * @return -1.0.4---------------"��ѯ"��ť
	 */
	public FButton getBtn_qry() {
		return btn_qry;
	}

	/** -1.0.5---------------"ѡ��ά��"��ť */
	private FButton btn_selectmod = null;

	/**
	 * @return -1.0.5---------------"ѡ��ά��"��ť
	 */
	public FButton getBtn_selectmod() {
		return btn_selectmod;
	}

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
	public FPanel getPanel_right() {
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

	/** -1.1.2.1.1-----------���ñ��� */
	private FTextField input_set_id = null;

	/**
	 * @return -1.1.2.1.1-----------���ñ���
	 */
	public FTextField getInput_set_id() {
		return input_set_id;
	}

	/** -1.1.2.1.2-----------���ñ����� */
	private FTextField input_set_name = null;

	/**
	 * @return -1.1.2.1.2-----------���ñ�����
	 */
	public FTextField getInput_set_name() {
		return input_set_name;
	}

	/** -1.1.2.1.3-----------���ñ��� */
	private FTextField input_set_shortname = null;

	/**
	 * @return -1.1.2.1.3-----------���ñ���
	 */
	public FTextField getInput_set_shortname() {
		return input_set_shortname;
	}

	/** -1.1.2.1.4-----------���ñ�ȫ�� */
	private FTextField input_set_longname = null;

	/**
	 * @return -1.1.2.1.4-----------���ñ�ȫ��
	 */
	public FTextField getInput_set_longname() {
		return input_set_longname;
	}

	/** -1.1.2.1.5-----------Ĭ������ */
	private FCheckBox is_default = null;

	/**
	 * @return -1.1.2.1.5-----------Ĭ������
	 */
	public FCheckBox getIs_default() {
		return is_default;
	}

	/** -1.1.2.2-------------��ϸ������ʾ��Ƭ,�����°벿�� */
	private FPanel panel_right_down = null;

	/**
	 * @return -1.1.2.2-------------��ϸ������ʾ��Ƭ,�����°벿��
	 */
	public FPanel getPanel_right_down() {
		return panel_right_down;
	}

	/** -1.1.2.2.1-----------��ϸ������ʾ��� */
	private FTable table_detail = null;

	/**
	 * @return -1.1.2.2.1-----------��ϸ������ʾ���
	 */
	public FTable getTable_detail() {
		return table_detail;
	}

	/**
	 * ���ܰ�ť�ĳ�ʼ��:"����"��"ɾ��"��
	 */
	private void createButtons() {
		// �����������ܰ�ť
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
		btn_selectmod = new FButton();
		btn_selectmod.setTitle("ѡ��ά��");
		btn_selectmod.setIcon("images/rp/set.gif");
		// "ˢ��"��ť�Ĵ��������
		btn_qry = new FButton();
		btn_qry.setTitle("ˢ��");
		btn_qry.setIcon("images/rp/query.gif");
		// "�ر�"��ť�Ĵ��������
		btn_close = new FButton();
		btn_close.setTitle("�ر�");
		btn_close.setIcon("images/rp/close.gif");
		// Ϊ������ť������Ӧ�ļ���
		btn_add.addActionListener(new RefActionListener(prjMainListener,
				"btn_addAction"));
		btn_del.addActionListener(new RefActionListener(prjMainListener,
				"btn_delAction"));
		btn_mod.addActionListener(new RefActionListener(prjMainListener,
				"btn_modAction"));
		btn_selectmod.addActionListener(new RefActionListener(prjMainListener,
				"btn_selectmodAction"));
		btn_qry.addActionListener(new RefActionListener(prjMainListener,
				"btn_qryAction"));
		btn_close.addActionListener(new RefActionListener(prjMainListener,
				"btn_closeAction"));
		// ������ť����������ť��˳�����
		toolBar = new FToolBarPanel();
		toolBar.addControl(btn_close);// yangxuefeng add 20080428
		toolBar.addControl(btn_add);
		toolBar.addControl(btn_del);
		toolBar.addControl(btn_mod);
		toolBar.addControl(btn_selectmod);
		// toolBar.addControl(btn_qry);
		// ��ť��Ƕ��ģ��
		this.setLayout(new BorderLayout());
		this.add(toolBar, BorderLayout.NORTH);
	}

	/**
	 * �������벿�ֵĳ�ʼ�����������νṹ
	 * 
	 * @return ���ع������
	 */
	private FScrollPane getLeftPanel() {

		try {
			// ������ʾ���Ĵ���
			tree_data = new CustomTree();
			tree_data.setEditable(false);
			// ѡ��ֵ�仯�ļ�������
			tree_data.addTreeSelectionListener(new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent e) {
					prjMainListener.tree_datavalueChanged(e);
				}
			});
			// ������ļ�������Ҫ����˫������
			tree_data.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ϵͳ�ṹ���ش�ʱ�������Բ���
					try {
						prjMainListener.tree_dataClicked(e);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
		} catch (Exception e1) {
			Log.error(e1.getMessage());
		}
		// ������壬���ҽ����ѽṹ���õ������
		spn_left = new FScrollPane();
		spn_left.addControl(tree_data);
		return spn_left;
	}

	/**
	 * ��������ϲ��ֵĳ�ʼ��
	 * 
	 * @return ��������ϲ��ֵ����
	 */
	private FPanel setPanelDataMain() {
		// ����������ϸ�ؼ�
		// "���ñ���"�ؼ��Ĵ����������Լ����
		input_set_id = new FTextField("���ñ���");
		input_set_id.setProportion(0.10f);
		input_set_id.setEditable(false);
		input_set_id.setVisible(true);
		// "���ñ�����"�ؼ��Ĵ����������Լ����
		input_set_name = new FTextField("���ñ�����");
		input_set_name.setProportion(0.10f);
		input_set_name.setEditable(false);
		input_set_name.setVisible(true);
		// "���ñ���"�ؼ��Ĵ����������Լ����
		input_set_shortname = new FTextField("���ñ���");
		input_set_shortname.setProportion(0.10f);
		input_set_shortname.setEditable(false);
		input_set_shortname.setVisible(true);
		// "���ñ�ȫ��"�ؼ��Ĵ����������Լ����
		input_set_longname = new FTextField("���ñ�ȫ��");
		input_set_longname.setProportion(0.10f);
		input_set_longname.setEditable(false);
		input_set_longname.setVisible(true);
		// "Ĭ������"�ؼ��Ĵ����������Լ����
		is_default = new FCheckBox("Ĭ������");
		is_default.setProportion(0.10f);
		is_default.setEditable(false);
		is_default.setEnabled(false);
		is_default.setVisible(true);
		// ��������չʾ����������
		panel_right_up = new FPanel();
		RowPreferedLayout rowPreferedLayout = new RowPreferedLayout(1);
		rowPreferedLayout.setColumnWidth(15);
		rowPreferedLayout.setColumnGap(6);
		panel_right_up.setLayout(rowPreferedLayout);
		panel_right_up.setTitle("��������");
		// ���ؼ���˳��ڷŽ����
		panel_right_up.addControl(input_set_id, new TableConstraints(1, 1,
				false, true));
		panel_right_up.addControl(input_set_name, new TableConstraints(1, 2,
				false, true));
		panel_right_up.addControl(input_set_shortname, new TableConstraints(1,
				3, false, true));
		panel_right_up.addControl(input_set_longname, new TableConstraints(1,
				4, false, true));
		panel_right_up.addControl(is_default, new TableConstraints(1, 5, false,
				true));
		return panel_right_up;
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
	 * ��������²��ֵĳ�ʼ��
	 * 
	 * @return ��������²��ֵ����
	 */
	private FPanel setPanelDataDetail() {
		try {
			// ������ϸ��ʾTable�������������Ӧ����ʾ��
			table_detail = new FTable();
			table_detail.addColumn(returnFtableColumn("comp_id", "�ؼ�����",
					FBaseTableColumn.RIGHT, 60));
			table_detail.addColumn(returnFtableColumn("is_must_name", "�Ƿ����",
					FBaseTableColumn.RIGHT, 60));
			table_detail.addColumn(returnFtableColumn("comp_name", "�ؼ�����",
					FBaseTableColumn.RIGHT, 100));
			table_detail.addColumn(returnFtableColumn("comp_ename", "�ֶ�����",
					FBaseTableColumn.RIGHT, 60));
			table_detail.addColumn(returnFtableColumn("field_type_name",
					"�ؼ�����", FBaseTableColumn.RIGHT, 60));
			// table_detail.addColumn(returnFtableColumn("is_comm", "�����ֶ�",
			// FBaseTableColumn.RIGHT, 60));
			// table_detail.addColumn(returnFtableColumn("comp_min_len", "��С����",
			// FBaseTableColumn.RIGHT, 60));
			// table_detail.addColumn(returnFtableColumn("comp_max_len", "��󳤶�",
			// FBaseTableColumn.RIGHT, 60));
			// table_detail.addColumn(returnFtableColumn("comp_min_value",
			// "��Сֵ", FBaseTableColumn.RIGHT, 80));
			// table_detail.addColumn(returnFtableColumn("comp_max_value",
			// "���ֵ", FBaseTableColumn.RIGHT, 80));
			table_detail.addColumn(returnFtableColumn("is_inner", "�Ƿ�����",
					FBaseTableColumn.RIGHT, 60));
			// table_detail.addColumn(returnFtableColumn("data_format",
			// "��ʽ���ַ���", FBaseTableColumn.RIGHT, 80));
			table_detail.setRowSelectionAllowed(true);
			table_detail.setColumnSelectionAllowed(false);
			// ������壬���Ұ���ʾ�������ȥ
			panel_right_down = new FPanel();
			panel_right_down.setLayout(new BorderLayout());
			panel_right_down.addControl(table_detail, BorderLayout.CENTER);
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
		return panel_right_down;
	}

	/**
	 * ������Ұ벿�ֵĳ�ʼ������ϸ���ݷ������������
	 * 
	 * @return ������Ұ벿�ֵ����
	 */
	private FPanel getRightPanel() {
		// ����������Ұ벿�ֵ���壬���ҽ����������ַ��ý�ȥ
		panel_right = new FPanel();
		panel_right.setLayout(new BorderLayout());
		panel_right.addControl(this.setPanelDataMain(), BorderLayout.NORTH);
		panel_right.addControl(this.setPanelDataDetail(), BorderLayout.CENTER);
		return panel_right;
	}

	/**
	 * �����ĳ�ʼ�����������������
	 */
	private void createMainPanel() {
		// ����Split��������������������Ĵ�С
		spt_main = new FSplitPane();
		spt_main.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		spt_main.setResizeWeight(0.40);
		// ��Split���߷ֱ�����������
		spt_main.addControl(this.getLeftPanel());
		spt_main.addControl(this.getRightPanel());
		panel_main.add(spt_main);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.foundercy.pf.framework.systemmanager.FModulePanel#initize()
	 *      ��ʼ��PrjMainUI�Ľ���
	 */
	public void initize() {
		// �ṹ����
		try {
			// ���ҵ������
			BeanFactory beanfactory = BeanFactoryUtil
					.getBeanFactory("gov/nbcs/rp/prjset/prjmain/conf/PrjMainConf.xml");
			prjMainService = (PrjMainIBS) beanfactory
					.getBean("rp.PrjMainService");
			// ����������
			prjMainListener = new PrjMainListener(this);
			// �������п��ư�ť
			this.createButtons();
			// ���������
			panel_main = new FPanel();
			panel_main.setLayout(new RowPreferedLayout(1));
			// �������壬���ҽ��������õ�ҳ����
			this.createMainPanel();
			this.add(panel_main);
			// this.createPopupMenu();
			this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
			// ��ʼ��ĳЩ�ؼ���ֵ
			this.initComponent();
			// ҳ����ʾ
			this.setVisible(true);

			// yangxuefeng add 20080419 begin
			// ���ṹ�ر�ʱ��ֹ��ɾ�ģ����ṹ��ʱ������������ɾ��
			// �������ӿ�
			this.btn_add.setEnabled(true);
			this.btn_del.setEnabled(true);
			this.btn_mod.setEnabled(true);
			this.btn_selectmod.setEnabled(true);
		} catch (Exception e) {
			Log.error("PrjMainUI.class�����ʼ��ʧ�ܣ�");
			Log.error(e.getMessage());
		}
	}

	/**
	 * ��ʼ��ĳЩ�ؼ���ֵ��������ʾ�����ݵĳ�ʼ��
	 */
	private void initComponent() {
		// ��ʼ�����ݱ�ʾ����ֵ
		this.initTree();
	}

	/**
	 * ��ʼ�����ݱ�ʾ����ֵ���������������Ա��ⲿ����
	 */
	public void initTree() {
		// ����ҵ�����������ṹ��xmldata
		DataSet ds_tree = prjMainService.getInputSetTreeData();
		try {
			// ����CustomTree�ĸ��ڵ㣬Dataset��ID��ţ���ʾ���ƣ����ڵ�ID
			tree_data.setRootName("��������");
			tree_data.setDataSet(ds_tree);
			tree_data.setIdName("CHR_ID");
			tree_data.setTextName("CHR_NAME");
			tree_data.setParentIdName("PARENT_ID");
			// �Զ�������ʾ��
			tree_data.reset();
		} catch (Exception e) {
			Log.error("��ʼ�����ݱ�ʾ������");
			Log.error(e.getMessage());
		}
	}

	public FButton getBtn_close() {
		return btn_close;
	}

	public void setBtn_close(FButton btn_close) {
		this.btn_close = btn_close;
	}

	public void doImpProject() {
		// TODO Auto-generated method stub
		
	}

}
