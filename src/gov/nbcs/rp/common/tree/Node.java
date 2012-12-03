package gov.nbcs.rp.common.tree;

/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
public interface Node extends Cloneable{
 /**
  * 标识根结点的常量 
  */
  public int ROOT=-1;//结点类型：根、枝、叶
  /**
   * 标识枝头的常量 
   */  
  public int BRANCH=1;
  /**
   * 标识叶结点的常量 
   */
  public int LEAF=0;
  
  /**
   * 获得结点类型
   * @return 结点类型
   */
  public int getNodeType();
  /**
   * 获得父结点
   * @return 父结点
   */
  public Node getParent();
  
  /**
   * 获得结点值
   * @return 结点值
   */
  public Object getValue();
  /**
   * 得到第i个子结点
   * @param i 下标
   * @return 子结点
   */
  public Node getChildAt(int i);
  /**
   * 得到子结点数
   * @return 子结点数
   */
  public int getChildrenCount();
  
  public Object getIdentifier();
  
  public Object getSortByValue();
  
  /**
   * 从指定结点开始搜索，搜索条件为给定的属性名和值
   * @param node 搜索起始结点
   * @param attrName 属性名
   * @param attrValue 属性值
   * @return 搜索到的结点
   */
  public Node search(Node node,String attrName,String attrValue);
  /**
   * 添加一个子结点
   * @param child 子结点
   */
  public void append(Node child);
  
  /**
   * 指定下标处插入一个子结点
   * @param child 
   * @param i
   */
  public void insert(Node child,int i);
  
  /**
   * 设置结点一个属性
   * @param key 属性名
   * @param value 属性值
   */
  public void setAttribute(Object key,Object value);
  
  /**
   * 获得结点的属性值
   * @param key 属性名
   * @return 属性值
   */
  public Object getAttribute(Object key);
  
  /**
   * 得到结点所有的属性名字
   * @return 属性名字集合的迭代子
   */
  public java.util.Iterator getAttributeNames();
  
  /**
   * Clone一个结点
   * @return clone的结点
   */
  public Object clone();
  
  /**
   * 设置结点值
   * @param value 结点值
   */
  public void setValue(Object value);
  
  /**
   * 得到结点的树层级
   * @return 层级
   */
  public int getLevel();
  
  /**
   * 获得子树的深度
   * @return
   */
  public int getHeight();
  
  /**
   * 获得子树的宽度，所有最末级子结点数
   * @return
   */
  public int getWidth(); 
  
  public String getText();
  
  /**
   * 判断子结点所在的下标
   * @param child
   * @return
   */
  public int indexOf(Node child);
   
  /**
   * 将树的所有结点按照广度优先化为二维表形式，一维下标就是结点所处的层次（相对于子树的起始根结点）
   * @return
   */
  public Node[][] toArray();
  
  /**删除一个下级节点*/
  public boolean deleteSubNode(Node node);
}