/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.tree;

import com.foundercy.pf.control.Control;
import com.foundercy.pf.control.PfTreeNode;
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.MyMap;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.Field;
import gov.nbcs.rp.common.datactrl.event.DataChangeEvent;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


public class CustomTree extends JTree implements Control {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private DataSet dataSet;

	private DefaultMutableTreeNode root;

	private String idName;

	private String parentIdName;

	private String textName;

	private SysCodeRule codeRule;

	private String rootName;

	private String sortKey;

	// private boolean hasCheck;

	private boolean checkEnable;

	private boolean hasRelation = true;

	private List mouseListeners;

	/**
	 * ��㻺�壬������һ�����ʱ�øý��ĸ����ID�ڴ�Cache�п��ٲ��Ҹ�������
	 */
	Map nodeCache = new MyMap();

	private LinkedList treeSelectionListeners;

	private boolean maskValueChange;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#addMouseListener(java.awt.event.MouseListener)
	 */
	public void addMouseListener(MouseListener ml) {
		if (mouseListeners == null) {
			mouseListeners = new ArrayList();
		}
		this.mouseListeners.add(ml);
	}

	public DataSet getDataSet() {
		return dataSet;
	}

	/**
	 * ����DataSet��������������¼�
	 * 
	 * @param dataSet
	 */
	public void setDataSet(DataSet ds) {
		if (ds != this.dataSet) {
			this.dataSet = ds;
			this.dataSet.addCancelListener(new CancelActionListener(this));
			this.dataSet.addDeleteListener(new DeleteActionListener(this));
			this.dataSet.addDataChangeListener(new DataChangeActionListener(
					this));
			this.dataSet.addInsertListener(new InsertActionListener(this));
		}
	}

	/**
	 * չ�����������
	 */
	public void expandAll() {
		for (int i = 0; i < this.getRowCount(); i++) {
			this.expandRow(i);
		}
	}

	/**
	 * �������������
	 */
	public void collapseAll() {
		for (int i = 0; i < this.getRowCount(); i++) {
			this.collapseRow(i);
		}
	}

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public String getParentIdName() {
		return parentIdName;
	}

	public void setParentIdName(String parentIdName) {
		this.parentIdName = parentIdName;
	}

	public String getTextName() {
		return textName;
	}

	public void setTextName(String textName) {
		this.textName = textName;
	}

	public SysCodeRule getCodeRule() {
		return codeRule;
	}

	public void setCodeRule(SysCodeRule codeRule) {
		this.codeRule = codeRule;
	}

	public String getRootName() {
		return rootName;
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	public CustomTree(DataSet ds, String idName, String textName,
			String parentIdName) throws Exception {
		this(null, ds, idName, textName, parentIdName, null, null);
	}

	public CustomTree(DataSet ds, String idName, String textName,
			SysCodeRule codeRule) throws Exception {
		this(null, ds, idName, textName, codeRule);
	}

	public CustomTree(String rootName, DataSet ds, String idName,
			String textName, SysCodeRule codeRule) throws Exception {
		this(rootName, ds, idName, textName, null, codeRule, null);
	}
	public CustomTree(String rootName, DataSet ds, String idName,
			String textName, SysCodeRule codeRule,String sortKey) throws Exception {
		this(rootName, ds, idName, textName, null, codeRule, sortKey);
	}

	public CustomTree() throws Exception {
		this(null, null, null, null, null, null, null);
	}

	public CustomTree(String rootName, DataSet ds, String idName,
			String textName, String parentIdName, SysCodeRule codeRule)
			throws Exception {
		this(rootName, ds, idName, textName, parentIdName, codeRule, null);
	}

	public CustomTree(String rootName, DataSet ds, String idName,
			String textName, String parentIdName, SysCodeRule codeRule,
			String sortKey) throws Exception {
		this(rootName, ds, idName, textName, parentIdName, codeRule, sortKey,
				false);
	}

	/**
	 * ����һ�����Ϳؼ�
	 * 
	 * @param rootName
	 *            ���ڵ���ʾ��
	 * @param ds
	 *            ���ݼ�
	 * @param idName
	 *            �����ID�ֶ���
	 * @param textName
	 *            �������ʾ�ı��ֶ���
	 * @param parentIdName
	 *            ����㸸���ID�ֶ���
	 * @param sortKey
	 *            ����ؼ��֣������ָ��ʹ��ID��Ϊ����ؼ���
	 * @throws Exception
	 */
	public CustomTree(String rootName, DataSet ds, String idName,
			String textName, String parentIdName, SysCodeRule codeRule,
			String sortKey, boolean isCheck) throws Exception {
		super();
		this.setIdName(idName);
		this.setParentIdName(parentIdName);
		this.setTextName(textName);
		this.setCodeRule(codeRule);
		this.setRootName(rootName);
		this.setDataSet(ds);
		this.setSortKey(sortKey);
		// �����˴���������ִ�λ������ by qinj at Jun 3, 2009
		// this.setCellRenderer(new DefaultCellRenderer());
		if (isCheck) {
			this.setCellRenderer(new CheckBoxCellRenderer());
		} else {
			this.setCellRenderer(new DefaultCellRenderer());
		}
		this.reset();
		this.getSelectionModel().addTreeSelectionListener(
				new MyTreeSelectionListener());
		super.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (CustomTree.this.getIsCheckBoxEnabled()
						&& (e.getButton() == MouseEvent.BUTTON1)) {
					int row = CustomTree.this.getRowForLocation(e.getX(), e
							.getY());
					if (row >= 0) {
						TreePath path = CustomTree.this.getPathForRow(row);
						if (path != null) {
							// ��CheckBox�Ĺ�ѡ/����ѡ����ֻ�ڵ��CheckBox���� by qinj at 2010-03-31 
							if (e.getX() > CustomTree.this.getPathBounds(path).x + 12) {
								return;
							}

							MyTreeNode node = (MyTreeNode) path
									.getLastPathComponent();
							if (node != null) {
								PfTreeNode pNode = ((PfTreeNode) node
										.getUserObject());
								pNode.setIsSelect(!pNode.getIsSelect());
								((DefaultTreeModel) getModel())
										.nodeChanged(node);
							}
						}
					}
				}
				// ˢ����������ѡ�к�ڵ�ͼ�겻��ʱˢ�µ����⣨JDK5+�� by qinj at Dec 19, 2009
				CustomTree.this.repaint();
				for (int i = mouseListeners.size() - 1; i >= 0; i--) {
					((MouseListener) mouseListeners.get(i)).mouseClicked(e);
				}
			}

			public void mouseEntered(MouseEvent e) {
				for (int i = mouseListeners.size() - 1; i >= 0; i--) {
					((MouseListener) mouseListeners.get(i)).mouseEntered(e);
				}
			}

			public void mouseExited(MouseEvent e) {
				for (int i = mouseListeners.size() - 1; i >= 0; i--) {
					((MouseListener) mouseListeners.get(i)).mouseExited(e);
				}
			}

			public void mousePressed(MouseEvent e) {
				for (int i = mouseListeners.size() - 1; i >= 0; i--) {
					((MouseListener) mouseListeners.get(i)).mousePressed(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				for (int i = mouseListeners.size() - 1; i >= 0; i--) {
					((MouseListener) mouseListeners.get(i)).mouseReleased(e);
				}
			}
		});
		this.addComponentListener(new ComponentListener() {
			public void componentHidden(ComponentEvent e) {
			}

			public void componentMoved(ComponentEvent e) {
				CustomTree.this.repaint();
			}

			public void componentResized(ComponentEvent e) {
				CustomTree.this.repaint();
			}

			public void componentShown(ComponentEvent e) {
			}
		});
	}

	public void addTreeSelectionListener(TreeSelectionListener listener) {
		if (treeSelectionListeners == null) {
			treeSelectionListeners = new LinkedList();
		}
		treeSelectionListeners.addFirst(listener);

	}

	public void reset() throws Exception {
		if ((dataSet == null) || dataSet.isEmpty()) {
			TreeModel model = null;
			if (rootName != null) {
				root = createNode(rootName, null, null, null, this);
				model = new DefaultTreeModel(root);
			} else {
				model = new DefaultTreeModel(createNode("", null, null, null,
						this));
			}
			this.setModel(model);
			return;
		}
		if (root != null) {
			root.removeAllChildren();
		}
		createByDatas();
		TreeModel model = new DefaultTreeModel(root);
		this.setModel(model);
	}

	/**
	 * ��ø����
	 */
	public DefaultMutableTreeNode getRoot() {
		return this.root;
	}

	/**
	 * Ϊ��������һ���ӽ�㣬���������
	 * 
	 * @param parent
	 *            �����
	 * @param node
	 *            �ӽ��
	 */
	public void addChild(DefaultMutableTreeNode parent,
			DefaultMutableTreeNode node, DefaultTreeModel model) {
		Enumeration children = parent.children();
		String id = Common.isNullStr(this.sortKey) ? ((PfTreeNode) node
				.getUserObject()).getValue() : ((MyTreeNode) node).sortKeyValue;
		if (children != null) {
			for (int i = 0; children.hasMoreElements(); i++) {
				DefaultMutableTreeNode sibling = (DefaultMutableTreeNode) children
						.nextElement();
				String siblingId = Common.isNullStr(this.sortKey) ? ((PfTreeNode) sibling
						.getUserObject()).getValue()
						: ((MyTreeNode) sibling).sortKeyValue;

				if (id.compareTo(siblingId) < 0) {
					if (model != null) {
						model.insertNodeInto(node, parent, i);
					}
					parent.insert(node, i);
					return;
				}
			}
		}
		node.setParent(null);
		if (model != null) {
			model.insertNodeInto(node, parent, parent.getChildCount());
		}
		parent.add(node);
	}

	/**
	 * ��ȡ����ѡ��Ľ��
	 * 
	 * @param onlyLeaf
	 *            �Ƿ�ֻ���Ҷ�ӽ��
	 * @return
	 */
	public MyTreeNode[] getSelectedNodes(boolean onlyLeaf) {
		if (root != null) {
			List cache = new ArrayList();
			for (Enumeration enumeration = root.breadthFirstEnumeration(); enumeration
					.hasMoreElements();) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration
						.nextElement();
				if (!onlyLeaf || (onlyLeaf && node.isLeaf())) {
					PfTreeNode pfNode = (PfTreeNode) node.getUserObject();
					if (pfNode.getIsSelect()) {
						cache.add(node);
					}
				}
			}
			MyTreeNode result[] = new MyTreeNode[cache.size()];
			System.arraycopy(cache.toArray(), 0, result, 0, result.length);
			return result;
		}
		return null;
	}

	/**
	 * ��ȡ����ѡ��Ľ������
	 * 
	 * @param onlyLeaf
	 *            �Ƿ�ֻ���Ҷ�ӽ��
	 * @return
	 */
	public int getSelectedNodeCount(boolean onlyLeaf) {
		return this.getNodeCount(onlyLeaf, true);
	}

	/**
	 * ��ȡ���н������
	 * 
	 * @param onlyLeaf
	 * @param onlySelected
	 * @return
	 */
	public int getNodeCount(boolean onlyLeaf, boolean onlySelected) {
		int count = 0;
		if (root != null) {
			for (Enumeration enumeration = root.breadthFirstEnumeration(); enumeration
					.hasMoreElements();) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration
						.nextElement();
				if (!onlyLeaf || (onlyLeaf && node.isLeaf())) {
					PfTreeNode pfNode = (PfTreeNode) node.getUserObject();
					if (!onlySelected || (onlySelected && pfNode.getIsSelect())) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * ��ѡ�е����ڵ㣬ƴ�ɲ�ѯ���� 
	 * @param queryFiled
	 * @param isViewText = true,��ʾ����ʾ�ڵ��л�ȡ����[001]��λ����ȡ[]��001��=false ,�ӽڵ������ֵȡ
	 * @param onlyLeaf
	 * @author sst
	 * @return
	 * @throws Exception 
	 */
	public String getSelectNodesCondition(String queryFiled,
			boolean isViewText, boolean onlyLeaf) throws Exception {
		//���յ�ƴ�ɵ�ѡ������
		String selectNodeCondition = "";
		//�����ڲ���ѡ��״̬�ĺ���ĩ����û���ӽڵ�ĵ�λ������in��������ѡ�е���like����
		StringBuffer sbIn = new StringBuffer("");
		StringBuffer sbLike = new StringBuffer("");
		MyPfNode selectNode = null;
		if (root != null) {
			StringBuffer[] two = null;
			selectNode = (MyPfNode) root.getUserObject();
			//ѭ�����������ӽڵ�
			for (int i = 0; (selectNode != null)
					&& (i < selectNode.getNode().getChildCount()); i++) {
				two = getSelectNodesCon((MyPfNode) (((MyTreeNode) selectNode
						.getNode().getChildAt(i)).getUserObject()), queryFiled,
						isViewText, onlyLeaf);
				sbIn.append(two[0]);
				sbLike.append(two[1]);
			}
			if (sbIn.length() > 0) {
				String div_code = sbIn.toString();
				if (!"".equals(div_code)) {
					div_code = div_code.substring(1);
				}
				//�����е�λ��ֳ�500��һ�������� 
				int count = 0;
				String divCodes = "";
				ArrayList alDivCode = new ArrayList();
				String[] auditDivCode = div_code.split(",");
				for (int i = 0; (auditDivCode != null) && (i < auditDivCode.length); i++) {
					if (count < 500) {
						divCodes += "," + auditDivCode[i];
						count++;
					} else {
						//���ϵ�ǰ�ĵ�λ���������©�������
						alDivCode.add(auditDivCode[i] + divCodes);
						divCodes = "";
						count = 0;
					}
				}
				if (count < 500) {
					alDivCode.add(divCodes.substring(1));
				}

				//����500�����һ��OR
				String divCodeNew = "";
				for (int i = 0; i < alDivCode.size(); i++) {
					if (i == 0) {
						divCodeNew = " " + queryFiled + " in("
								+ (String) alDivCode.get(i) + ")";
					} else {
						divCodeNew += " or " + queryFiled + " in("
								+ (String) alDivCode.get(i) + ")";
					}
				}
				selectNodeCondition = "(" + divCodeNew + ") ";
			}
			if (sbLike.length() > 0) {
				if (!"".equals(selectNodeCondition)) {
					selectNodeCondition = "(" + selectNodeCondition + " or ("
							+ sbLike.substring(3) + ")) ";
				} else {
					selectNodeCondition += " (" + sbLike.substring(3) + ") ";
				}
			}
		}

		return selectNodeCondition;
	}

	/**
	 * ��ѡ�е����ڵ㣬ƴ�ɲ�ѯ���� ,=0 �� in ������ =1 �� like ����
	 * @param queryFiled
	 * @param isViewText = true,��ʾ����ʾ�ڵ��л�ȡ����[001]��λ����ȡ[]��001��=false ,�ӽڵ������ֵȡ
	 * @param onlyLeaf
	 * @return
	 * @throws Exception 
	 */
	private StringBuffer[] getSelectNodesCon(MyPfNode node, String queryFiled,
			boolean isViewText, boolean onlyLeaf) throws Exception {
		//�����ڲ���ѡ��״̬�ĺ���ĩ����û���ӽڵ�ĵ�λ������in��������ѡ�е���like����
		StringBuffer sbIn = new StringBuffer("");
		StringBuffer sbLike = new StringBuffer("");
		String value = "";
		if (node != null) {
			if (isViewText) {
				//����ʾ�л�ȡ 
				value = node.getNode().getUserObject().toString();
				if (!((value == null) || (value.trim().length() == 0))) {
					if ((value.indexOf("]") > -1) && (value.indexOf("[") > -1)) {
						value = value.substring(value.indexOf("[") + 1, value
								.indexOf("]"));
					} else {
						value = "-1";//��ʾ���ǵ�λ�ı���
					}
				}
			} else {
				//�Ӻ�̨ȡ ������ҵ���ң��Ͳ�Ӧ�û�ȡ
				String nodeValue = node.getNode().getUserObject().toString();
				if (!((nodeValue == null) || (nodeValue.trim().length() == 0))) {
					if ((nodeValue.indexOf("]") > -1)
							&& (nodeValue.indexOf("[") > -1)) {
						//ҳ�治��ʾ�Ŀ϶��Ƿǵ�λ 
						this.dataSet.gotoBookmark(node.getNode().getBookmark());
						value = this.dataSet.fieldByName(queryFiled).getValue()
								.toString();
					}
				}
			}

			//��ѡ��״̬
			if (node.getSelectStat() == MyPfNode.SELECT) {
				if (isViewText && "-1".equals(value)) {
					//��ʾ���ݲ��ǵ�λ���룬ֱ��ȡ�¼�
					for (int i = 0; i < node.getNode().getChildCount(); i++) {
						StringBuffer[] two = getSelectNodesCon(
								(MyPfNode) (((MyTreeNode) node.getNode()
										.getChildAt(i)).getUserObject()),
								queryFiled, isViewText, onlyLeaf);
						sbIn.append(two[0]);
						sbLike.append(two[1]);
					}
				} else {
					if (onlyLeaf) {
						//Ҳȡ����
						if (!((value == null) || "".equals(value))) {
							sbIn.append(",'" + value + "'");
						}
						if (node.getNode().getChildCount() > 0) {
							for (int i = 0; i < node.getNode().getChildCount(); i++) {
								StringBuffer[] two = getSelectNodesCon(
										(MyPfNode) (((MyTreeNode) node
												.getNode().getChildAt(i))
												.getUserObject()), queryFiled,
										isViewText, onlyLeaf);
								sbIn.append(two[0]);
								sbLike.append(two[1]);
							}
						}
					} else if (node.getNode().getChildCount() > 0) {
						if (!((value == null) || "".equals(value))) {
							sbLike.append(" or ").append(queryFiled).append(
									" like '" + value + "%'");
						}
					} else {
						//�ж��Ƿ��ȡҶ�ӽڵ�
						if (!onlyLeaf) {
							if (!((value == null) || "".equals(value))) {
								sbIn.append(",'" + value + "'");
							}
						}
					}
				}
			} else if (node.getSelectStat() == MyPfNode.PARTSELECT) {
				if (!onlyLeaf) {
					//�ж��Ƿ��ȡҶ�ӽڵ㣬ע�⣬����ĩ���ڵ㣬�䲻�����ǰ�ѡ״̬ 
					if (!((value == null) || "".equals(value))) {
						sbIn.append(",'" + value + "'");
					}
				}
				//����϶�����Ҷ�ӽڵ㣬�������ǽ�ȡҶ�ӽڵ�ģ���ֱ�Ӳ����¼�
				for (int i = 0; i < node.getNode().getChildCount(); i++) {
					StringBuffer[] two = getSelectNodesCon(
							(MyPfNode) (((MyTreeNode) node.getNode()
									.getChildAt(i)).getUserObject()),
							queryFiled, isViewText, onlyLeaf);
					sbIn.append(two[0]);
					sbLike.append(two[1]);
				}
			}
		}

		return new StringBuffer[] { sbIn, sbLike };
	}

	public String setFristChildNodeSelect() {
		boolean isnolyOne = true;
		if (root != null) {
			List cache = new ArrayList();
			for (Enumeration enumeration = root.breadthFirstEnumeration(); enumeration
					.hasMoreElements();) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration
						.nextElement();
				if (node.isLeaf()) {
					PfTreeNode pfNode = (PfTreeNode) node.getUserObject();
					cache.add(pfNode);
					if (cache.size() > 1) {
						isnolyOne = false;
						break;
					}
					// pfNode.setIsSelect(true);

				}
			}
			if (isnolyOne && (cache.size() > 0)) {
				PfTreeNode node = (PfTreeNode) cache.get(0);
				return node.getValue().trim();

			}
		}
		return null;

	}

	/**
	 * ȡ��������ѡ��Ľڵ�
	 * 
	 * @param onlyLeaf
	 * 
	 */
	public void cancelSelected(boolean onlyLeaf) {
		if (root != null) {
			for (Enumeration enumeration = root.breadthFirstEnumeration(); enumeration
					.hasMoreElements();) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration
						.nextElement();
				if (!onlyLeaf || (onlyLeaf && node.isLeaf())) {
					PfTreeNode pfNode = (PfTreeNode) node.getUserObject();
					if (pfNode.getIsSelect()) {
						pfNode.setIsSelect(false);
					}
				}
			}
		}
	}

	/**Z
	 * ��ȡѡ�еĽ��
	 * 
	 * @return
	 */
	public MyTreeNode getSelectedNode() {
		TreePath path = this.getSelectionPath();
		return path == null ? null : (MyTreeNode) path.getLastPathComponent();
	}

	public PfTreeNode getSelectedPfTreeNode() {
		MyTreeNode node = getSelectedNode();
		if (node != null) {
			return (PfTreeNode) node.getUserObject();
		}
		return null;
	}

	/**
	 * ��ȡ�����ID
	 * 
	 * @return
	 */
	String getParentId(DataSet ds, String code) throws Exception {
		if (codeRule != null) {
			return codeRule.previous(code);
		} else {
			return ds.fieldByName(this.parentIdName).getString();
		}
	}

	/**
	 * ����һ��DataSet�����������ݽṹ
	 * 
	 * @param rootName
	 *            ���ĸ��ڵ���ʾ����
	 * @param datas
	 *            DataSet���Ͷ���
	 * @param idName
	 *            ������ID�ֶ���
	 * @param textName
	 *            �������ı��ֶ���
	 * @param parentIdName
	 *            �����ĸ����ID�ֶ���
	 * @param codeRule
	 *            ���ι�������˲�����Ϊnull����ô��ʹ�ô˶����ȡ�����ı���
	 * @return
	 */
	protected void createByDatas() throws Exception {
		this.nodeCache.clear();
		if (rootName != null) {
			root = createNode(rootName, null, null, null, this);
		}
		dataSet.maskDataChange(true);
		dataSet.beforeFirst();
		while (dataSet.next()) {
			String id = dataSet.fieldByName(idName).getString();
			Field fld = dataSet.fieldByName(textName);
			DefaultMutableTreeNode node = createNode(Common.nonNullStr(fld
					.getValue()), id, dataSet.toogleBookmark(), dataSet
					.fieldByName(sortKey).getString(), this);
			nodeCache.put(id, node);
		}
		dataSet.maskDataChange(false);
		parseTreeRelation(nodeCache);
	}

	/**
	 * �����ֶ�����ֵ��λ����㣬����DataSet�ᶨλ����Ӧ�ļ�¼
	 * 
	 * @param name
	 * @param value
	 */
	public void expandTo(String name, Object value) throws Exception {
		if (dataSet.locate(name, value)) {
			String id = dataSet.fieldByName(getIdName()).getString();
			if (!Common.isNullStr(id)) {
				TreeNode node = (TreeNode) nodeCache.get(id);
				if (node != null) {
					TreePath path = new TreePath(((DefaultTreeModel) this
							.getModel()).getPathToRoot(node));
					this.expandPath(path);
					this.getSelectionModel().setSelectionPath(path);
					this.scrollPathToVisible(path);
				}
			}
		}
	}

	/**
	 * �����ֶ�����ֵ��������ʽ��λ����㣬����DataSet�ᶨλ����Ӧ�ļ�¼
	 * 
	 * @param name
	 * @param value
	 * @param isbeforeFirst
	 *            Ҫ��Ҫ��dataSet�ص���һ����¼
	 * @throws Exception
	 */

	public boolean blurExpandTo(String name, String value, boolean isbeforeFirst)
			throws Exception {
		MyTreeNode rootnode;
		boolean falg = false;
		PfTreeNode pfnode = null;
		Pattern p = Pattern.compile(value);
		if (isbeforeFirst) {
			rootnode = (MyTreeNode) this.getRoot();
		} else {
			rootnode = this.getSelectedNode();
			if (rootnode == null) {
				rootnode = (MyTreeNode) this.getRoot();
			}
		}

		while ((rootnode != null) && (rootnode.getNextNode() != null)) {
			pfnode = (PfTreeNode) rootnode.getNextNode().getUserObject();
			if (p.matcher(pfnode.getShowContent()).find()) {
				falg = true;
				break;
			}
			rootnode = (MyTreeNode) rootnode.getNextNode();
		}
		String bookmark = dataSet.toogleBookmark();
		if (falg == true) {
			if (dataSet.locate(name, pfnode.getShowContent())) {
				String id = dataSet.fieldByName(getIdName()).getString();
				if (!Common.isNullStr(id)) {
					TreeNode node = (TreeNode) nodeCache.get(id);
					if (node != null) {
						TreePath path = new TreePath(
								((DefaultTreeModel) getModel())
										.getPathToRoot(node));
						expandPath(path);
						getSelectionModel().setSelectionPath(path);
						this.scrollPathToVisible(path);
						return true;
					}
				}
			}
		} else {
			dataSet.gotoBookmark(bookmark);
		}
		return false;
	}

	/**
	 * �Ѷ�Ӧ�����ڵ�����Ϊѡ��
	 * 
	 * 
	 * @throws Exception
	 */
	public void setTreeSelect(String name, Object value) throws Exception {
		if (value != null) {
			if (dataSet.locate(name, value)) {
				String id = dataSet.fieldByName(getIdName()).getString();
				if (!Common.isNullStr(id)) {
					MyTreeNode node = (MyTreeNode) nodeCache.get(id);
					if (node != null) {
						((PfTreeNode) node.getUserObject()).setIsSelect(true);
					}
				}
			}
		}
	}

	/**
	 * �����ֶ������ҵ��ڵ㲢����Ϊ��ѡ��״̬
	 * 
	 * 
	 * @throws Exception
	 */
	public void setTreeSelectCancel(String name, Object value) throws Exception {
		if (value != null) {
			if (dataSet.locate(name, value)) {
				String id = dataSet.fieldByName(getIdName()).getString();
				if (!Common.isNullStr(id)) {
					MyTreeNode node = (MyTreeNode) nodeCache.get(id);
					if (node != null) {
						((PfTreeNode) node.getUserObject()).setIsSelect(false);
					}
				}
			}
		}
	}

	/**
	 * չ����ĳ�����
	 * 
	 * @param node
	 */
	public void expandTo(DefaultMutableTreeNode node) {
		this.expandPath(new TreePath(node.getPath()));
	}

	/**
	 * ������������
	 * 
	 * @param text
	 *            ��ʾ�ı�
	 * @param value
	 *            ���ֵ���������DataSet��һ��bookmark
	 * @return
	 */
	public static DefaultMutableTreeNode createNode(String text, String value,
			String bookmark, String sortKeyValue, CustomTree tree) {
		MyPfNode nodeObject = new MyPfNode();
		nodeObject.setShowContent(text);
		nodeObject.setValue(value);
		nodeObject.setTree(tree);
		MyTreeNode node = new MyTreeNode(nodeObject);
		nodeObject.setNode(node);
		node.bookmark = bookmark;
		node.sortKeyValue = sortKeyValue;
		return node;
	}

	/**
	 * �ѽ�㻺���еĽ���γ����ι�ϵ
	 * 
	 * @param nodeCache
	 *            ����㻺��
	 * @param codeRule
	 *            ���ι�������˲�����Ϊnull����ô��ʹ�ô˶����ȡ�����ı���
	 * @return
	 */
	void parseTreeRelation(Map nodeCache) throws Exception {
		for (Iterator it = nodeCache.values().iterator(); it.hasNext();) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) it.next();
			String id = ((PfTreeNode) node.getUserObject()).getValue();
			if (this.dataSet.gotoBookmark(((MyTreeNode) node).getBookmark())) {
				String parentId = getParentId(dataSet, id);
				if (Common.isNullStr(parentId)) {
					if (root == null) {
						root = node;
					} else {
						addChild(root, node, null);
					}
				} else {
					DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) nodeCache
							.get(parentId);
					if (parentNode != null) {
						addChild(parentNode, node, null);
						node.setParent(parentNode);
					} else {
						addChild(root, node, null);
					}
				}
			}
		}
	}

	/**
	 * �Ƿ���ʱֹͣ������ValueChange��Ӧ
	 * 
	 * @param maskValueChange
	 */
	public void maskValueChange(boolean maskValueChange) {
		this.maskValueChange = maskValueChange;
	}

	/**
	 * ���ø�����Ƿ�check
	 * 
	 */
	protected void setParentCheck(DefaultMutableTreeNode node,
			boolean checkEstimate) {
		if (node != null) {
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
					.getParent();
			if (parent != null) {
				PfTreeNode pChild = (PfTreeNode) node.getUserObject();
				PfTreeNode pNode = (PfTreeNode) parent.getUserObject();
				boolean checked = checkEstimate && !pChild.getIsSelect();
				if (checked) {
					pNode.setIsSelect(true);
				} else {
					int i = 0;
					for (; i < parent.getChildCount(); i++) {
						DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent
								.getChildAt(i);
						pChild = (PfTreeNode) child.getUserObject();
						if (pChild.getIsSelect() && (child != node)) {
							pNode.setIsSelect(true);
							break;
						}
					}
					if (i >= parent.getChildCount()) {
						pNode.setIsSelect(false);
					}
				}
				setParentCheck(parent, false);
			}
		}
	}

	class MyTreeSelectionListener implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {
			if (!maskValueChange) {
				Object node = e.getPath().getLastPathComponent();

				if ((node != null) && (node instanceof MyTreeNode)) {
					try {
						//Modify by LM 2008/3/18

						dataSet.maskDataChange(true);
						dataSet.gotoBookmark(((MyTreeNode) node).bookmark);
						dataSet.maskDataChange(false);
						dataSet.fireDataChange(node, null,
								DataChangeEvent.CURSOR_MOVED);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				if (treeSelectionListeners != null) {
					for (int i = treeSelectionListeners.size() - 1; i >= 0; i--) {
						((TreeSelectionListener) treeSelectionListeners.get(i))
								.valueChanged(e);
					}
				}
			}
		}
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	public void setId(String arg0) {
	}

	public String getId() {
		return "";
	}

	public Control getParentControl() {
		return null;
	}

	public void setParentControl(Control arg0) {
	}

	public void setIsCheckBoxEnabled(boolean checkBoxEnabled) {
		this.checkEnable = checkBoxEnabled;
	}

	/**
	 * �����Ƿ�����checkbox�����ø÷����������reset()�������»��ơ�
	 * 
	 * @param isCheck
	 * 
	 * @deprecated isCheck���Է��빹�캯����
	 */
	public void setIsCheck(boolean isCheck) {
		if (isCheck) {
			this.setCellRenderer(new CheckBoxCellRenderer());
		} else {
			this.setCellRenderer(new DefaultCellRenderer());
		}
		//		this.hasCheck = isCheck;
	}

	//	public boolean getIsCheck() {
	//		return hasCheck;
	//	}

	public boolean getIsCheckBoxEnabled() {
		return checkEnable;
	}

	public boolean isHasRelation() {
		return hasRelation;
	}

	public void setHasRelation(boolean hasRelation) {
		this.hasRelation = hasRelation;
	}

	public MyTreeNode getNodeById(Object nodeId) {
		return (MyTreeNode) nodeCache.get(nodeId);
	}

	public void setAllNodeStatus(boolean isCheck) {
		if (!(this.getCellRenderer()instanceof CheckBoxCellRenderer)) {
			return;
		}
		if ((nodeCache == null) || nodeCache.isEmpty()) {
			return;
		}
		Iterator it = nodeCache.values().iterator();
		while (it.hasNext()) {
			((PfTreeNode) ((MyTreeNode) it.next()).getUserObject())
					.setIsSelect(isCheck);
		}
	}
}
