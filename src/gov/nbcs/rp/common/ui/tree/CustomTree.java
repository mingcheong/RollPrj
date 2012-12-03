/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
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
	 * 结点缓冲，遍历到一个结点时用该结点的父结点ID在此Cache中快速查找父结点对象
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
	 * 设置DataSet，并且添加侦听事件
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
	 * 展开所有树结点
	 */
	public void expandAll() {
		for (int i = 0; i < this.getRowCount(); i++) {
			this.expandRow(i);
		}
	}

	/**
	 * 收起所有树结点
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
	 * 创建一个树型控件
	 * 
	 * @param rootName
	 *            根节点显示名
	 * @param ds
	 *            数据集
	 * @param idName
	 *            树结点ID字段名
	 * @param textName
	 *            树结点显示文本字段名
	 * @param parentIdName
	 *            树结点父结点ID字段名
	 * @param sortKey
	 *            排序关键字，如果不指定使用ID作为排序关键字
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
		// 移至此处，解决文字错位的问题 by qinj at Jun 3, 2009
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
							// 对CheckBox的勾选/反勾选操作只在点击CheckBox触发 by qinj at 2010-03-31 
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
				// 刷新树，修正选中后节点图标不及时刷新的问题（JDK5+） by qinj at Dec 19, 2009
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
	 * 获得根结点
	 */
	public DefaultMutableTreeNode getRoot() {
		return this.root;
	}

	/**
	 * 为父结点添加一个子结点，按排序加入
	 * 
	 * @param parent
	 *            父结点
	 * @param node
	 *            子结点
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
	 * 获取所有选择的结点
	 * 
	 * @param onlyLeaf
	 *            是否只获得叶子结点
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
	 * 获取所有选择的结点数量
	 * 
	 * @param onlyLeaf
	 *            是否只获得叶子结点
	 * @return
	 */
	public int getSelectedNodeCount(boolean onlyLeaf) {
		return this.getNodeCount(onlyLeaf, true);
	}

	/**
	 * 获取所有结点数量
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
	 * 将选中的树节点，拼成查询条件 
	 * @param queryFiled
	 * @param isViewText = true,表示从显示节点中获取比如[001]单位，就取[]的001，=false ,从节点的隐含值取
	 * @param onlyLeaf
	 * @author sst
	 * @return
	 * @throws Exception 
	 */
	public String getSelectNodesCondition(String queryFiled,
			boolean isViewText, boolean onlyLeaf) throws Exception {
		//最终的拼成的选择条件
		String selectNodeCondition = "";
		//将处于部分选中状态的和最末级且没有子节点的单位编码用in处理，其他选中的用like处理
		StringBuffer sbIn = new StringBuffer("");
		StringBuffer sbLike = new StringBuffer("");
		MyPfNode selectNode = null;
		if (root != null) {
			StringBuffer[] two = null;
			selectNode = (MyPfNode) root.getUserObject();
			//循环查找所有子节点
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
				//把所有单位拆分成500个一组进行审核 
				int count = 0;
				String divCodes = "";
				ArrayList alDivCode = new ArrayList();
				String[] auditDivCode = div_code.split(",");
				for (int i = 0; (auditDivCode != null) && (i < auditDivCode.length); i++) {
					if (count < 500) {
						divCodes += "," + auditDivCode[i];
						count++;
					} else {
						//加上当前的单位，否则会遗漏而不审核
						alDivCode.add(auditDivCode[i] + divCodes);
						divCodes = "";
						count = 0;
					}
				}
				if (count < 500) {
					alDivCode.add(divCodes.substring(1));
				}

				//按照500个组成一个OR
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
	 * 将选中的树节点，拼成查询条件 ,=0 是 in 条件， =1 是 like 条件
	 * @param queryFiled
	 * @param isViewText = true,表示从显示节点中获取比如[001]单位，就取[]的001，=false ,从节点的隐含值取
	 * @param onlyLeaf
	 * @return
	 * @throws Exception 
	 */
	private StringBuffer[] getSelectNodesCon(MyPfNode node, String queryFiled,
			boolean isViewText, boolean onlyLeaf) throws Exception {
		//将处于部分选中状态的和最末级且没有子节点的单位编码用in处理，其他选中的用like处理
		StringBuffer sbIn = new StringBuffer("");
		StringBuffer sbLike = new StringBuffer("");
		String value = "";
		if (node != null) {
			if (isViewText) {
				//从显示中获取 
				value = node.getNode().getUserObject().toString();
				if (!((value == null) || (value.trim().length() == 0))) {
					if ((value.indexOf("]") > -1) && (value.indexOf("[") > -1)) {
						value = value.substring(value.indexOf("[") + 1, value
								.indexOf("]"));
					} else {
						value = "-1";//表示不是单位的编码
					}
				}
			} else {
				//从后台取 ，若是业务处室，就不应该获取
				String nodeValue = node.getNode().getUserObject().toString();
				if (!((nodeValue == null) || (nodeValue.trim().length() == 0))) {
					if ((nodeValue.indexOf("]") > -1)
							&& (nodeValue.indexOf("[") > -1)) {
						//页面不显示的肯定是非单位 
						this.dataSet.gotoBookmark(node.getNode().getBookmark());
						value = this.dataSet.fieldByName(queryFiled).getValue()
								.toString();
					}
				}
			}

			//是选择状态
			if (node.getSelectStat() == MyPfNode.SELECT) {
				if (isViewText && "-1".equals(value)) {
					//显示内容不是单位编码，直接取下级
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
						//也取父级
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
						//判断是否仅取叶子节点
						if (!onlyLeaf) {
							if (!((value == null) || "".equals(value))) {
								sbIn.append(",'" + value + "'");
							}
						}
					}
				}
			} else if (node.getSelectStat() == MyPfNode.PARTSELECT) {
				if (!onlyLeaf) {
					//判断是否仅取叶子节点，注意，若是末级节点，其不可能是半选状态 
					if (!((value == null) || "".equals(value))) {
						sbIn.append(",'" + value + "'");
					}
				}
				//这里肯定还有叶子节点，所有若是仅取叶子节点的，就直接查找下级
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
	 * 取消所有已选择的节点
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
	 * 获取选中的结点
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
	 * 获取父结点ID
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
	 * 根据一个DataSet创建树形数据结构
	 * 
	 * @param rootName
	 *            树的根节点显示名字
	 * @param datas
	 *            DataSet类型对象
	 * @param idName
	 *            树结点的ID字段名
	 * @param textName
	 *            树结点的文本字段名
	 * @param parentIdName
	 *            树结点的父结点ID字段名
	 * @param codeRule
	 *            级次规则，如果此参数不为null，那么就使用此对象获取父结点的编码
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
	 * 根据字段名、值定位树结点，并且DataSet会定位到相应的记录
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
	 * 根据字段名和值的正则表达式定位树结点，并且DataSet会定位到相应的记录
	 * 
	 * @param name
	 * @param value
	 * @param isbeforeFirst
	 *            要不要让dataSet回到第一条记录
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
	 * 把对应的树节点设置为选中
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
	 * 根据字段名查找到节点并设置为非选择状态
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
	 * 展开到某个结点
	 * 
	 * @param node
	 */
	public void expandTo(DefaultMutableTreeNode node) {
		this.expandPath(new TreePath(node.getPath()));
	}

	/**
	 * 创建树结点对象
	 * 
	 * @param text
	 *            显示文本
	 * @param value
	 *            结点值，这里就是DataSet的一个bookmark
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
	 * 把结点缓冲中的结点形成树形关系
	 * 
	 * @param nodeCache
	 *            树结点缓冲
	 * @param codeRule
	 *            级次规则，如果此参数不为null，那么就使用此对象获取父结点的编码
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
	 * 是否暂时停止树结点的ValueChange响应
	 * 
	 * @param maskValueChange
	 */
	public void maskValueChange(boolean maskValueChange) {
		this.maskValueChange = maskValueChange;
	}

	/**
	 * 设置父结点是否check
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
	 * 设置是否启用checkbox。调用该方法后，需调用reset()方法重新绘制。
	 * 
	 * @param isCheck
	 * 
	 * @deprecated isCheck属性放入构造函数。
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
