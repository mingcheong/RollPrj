package gov.nbcs.rp.common.tree;

import gov.nbcs.rp.common.MyMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * <p>
 * Title: 树结点的实现类，方法说明见@see com.jsxcsoft.common.tree.Node
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: 浙江易桥
 * </p>
 * 
 * @author 局端财政开发
 * @version 1.0
 */
public class TreeNode implements Node, Cloneable {
	Map attributes;

	Object value;

	int level = 0;

	/**
	 * 子树集合
	 */
	ArrayList children;

	/**
	 * 父结点
	 */
	Node parent;

	private int height = -1;

	private int width = 0;

	private Map levelNodes = new HashMap();

	private Set levelNodesExist = new HashSet();

	private Object id;

	private Object sortValue;

	private String text;

	TreeNode(Object id, String text, Object sortValue, Object value,
			Map attributes) {
		this.value = value;
		this.attributes = attributes;
		this.id = id;
		this.sortValue = sortValue;
		this.text = text;
	}

	TreeNode(Map attributes) {
		this(null, null, null, null, attributes);
	}

	public Iterator getAttributeNames() {
		return attributes.keySet().iterator();
	}

	public Node search(Node node, String attrName, String attrValue) {
		Set v = new HashSet(1);
		search(node, v, attrName, attrValue);
		if (v.size() > 0) {
			return (Node) v.iterator().next();
		} else {
			return node;
		}
	}

	private void search(Node node, Set result, String attrName, String attrValue) {
		if (matchAttr(node, attrName, attrValue)) {
			result.add(node);
			return;
		}
		for (int i = 0; i < node.getChildrenCount(); i++) {
			search(node.getChildAt(i), result, attrName, attrValue);
		}
	}

	protected boolean matchAttr(Node node, String attrName, String attrValue) {
		return ((attrName != null) && (attrValue != null))
				&& ((TreeNode) node).attributes.containsKey(attrName)
				&& ((TreeNode) node).attributes.get(attrName).equals(attrValue);
	}

	public void append(Node node) {
		if (children == null) {
			children = new ArrayList();
		}
		((TreeNode) node).parent = this;
		adjustChildLevel((TreeNode) node);
		children.add(node);
	}

	public Object getAttribute(Object key) {
		return attributes == null ? null : attributes.get(key);
	}

	public Map getAttributes() {
		return this.attributes;
	}

	public void setAttribute(Object name, Object value) {
		if (attributes == null) {
			attributes = new MyMap();
		}
		attributes.put(name, value);
	}

	public Object getValue() {
		return this.value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int getNodeType() {
		return parent == null ? Node.ROOT : (((children != null) && (children
				.size() > 0)) ? Node.BRANCH : Node.LEAF);
	}

	public int getChildrenCount() {
		return children == null ? 0 : children.size();
	}

	public Node getChildAt(int i) {
		return (Node) this.children.get(i);
	}

	public Node getParent() {
		return this.parent;
	}

	public int getLevel() {
		return level;
	}

	public Object clone() {
		Node cloned = new TreeNode(this.id, this.text, this.sortValue,
				this.value, this.attributes);
		for (int i = 0; i < getChildrenCount(); i++) {
			cloned.append((Node) getChildAt(i).clone());
		}
		return cloned;
	}

	public boolean equals(Object o) {
		if (!(o instanceof TreeNode)) {
			return false;
		}
		TreeNode node = (TreeNode) o;
		boolean b = (((this.value == null) && (node.value == null)) || ((this.value != null) && this.value
				.equals(node.value)))
				&& (o instanceof TreeNode);
		if (!b) {
			return b;
		}
		if (attributes != null) {
			for (Iterator it = node.attributes.keySet().iterator(); it
					.hasNext();) {
				String key = it.next().toString();
				String value = node.attributes.get(key).toString();
				b = b && matchAttr(this, key, value);
			}
		}
		return b;
	}

	public int getHeight() {
		doHeightStatistic(this);
		return height;
	}

	protected void doHeightStatistic(Node node) {
		int currentLevel = node.getLevel();
		addLevelNode(currentLevel, node);
		if ((node.getChildrenCount() == 0) && (currentLevel > height)) {
			if (node != this) {
				height = currentLevel;
			}
		} else {
			for (int i = 0; i < node.getChildrenCount(); i++) {
				doHeightStatistic(node.getChildAt(i));
			}
		}
	}

	protected void addLevelNode(int level, Node node) {
		Integer key = new Integer(level);
		List nodeList = null;
		if (this.levelNodes.containsKey(key)) {
			nodeList = (List) levelNodes.get(key);
		} else {
			nodeList = new ArrayList();
		}
		if (!levelNodesExist.contains(node)) {
			nodeList.add(node);
			levelNodesExist.add(node);
		}
		levelNodes.put(key, nodeList);
	}

	protected void doWidthStatistic(Node node) {
		if (node.getNodeType() == Node.LEAF) {
			width++;
		} else {
			for (int i = 0; i < node.getChildrenCount(); i++) {
				doWidthStatistic(node.getChildAt(i));
			}
		}
	}

	public int getWidth() {
		width = 0;
		doWidthStatistic(this);
		return width;
	}

	public Node[][] toArray() {
		levelNodes.clear();
		levelNodesExist.clear();
		doHeightStatistic(this);
		Node[][] nodes = new Node[levelNodes.size()][];
		for (int i = 0; i < nodes.length; i++) {
			List list = (List) levelNodes.get(new Integer(i));
			if (list != null) {
				nodes[i] = new Node[list.size()];
				System.arraycopy(list.toArray(), 0, nodes[i], 0,
						nodes[i].length);
			}
		}
		return nodes;
	}

	public void insert(Node child, int i) {
		if (this.children == null) {
			children = new ArrayList();
		}
		((TreeNode) child).parent = this;
		adjustChildLevel((TreeNode) child);
		children.add(i, child);
	}

	public int indexOf(Node child) {
		return this.children == null ? -1 : children.indexOf(child);
	}

	protected void adjustChildLevel(TreeNode node) {
		node.level = ((TreeNode) node.parent).level + 1;
		for (int i = 0; i < node.getChildrenCount(); i++) {
			adjustChildLevel((TreeNode) node.getChildAt(i));
		}
	}

	public Object getIdentifier() {
		// TODO Auto-generated method stub
		return this.id;
	}

	public Object getSortByValue() {
		// TODO Auto-generated method stub
		return this.sortValue;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String toString() {
		return getText();
	}

	public boolean deleteSubNode(Node node) {
		if (node == null) {
			return false;
		}
		int iCount = getChildrenCount();
		if (iCount <= 0) {
			return false;
		}
		for (int i = 0; i < iCount; i++) {
			if (getChildAt(i) == node) {
				children.remove(i);
				return true;
			}
		}
		return false;
	}
}