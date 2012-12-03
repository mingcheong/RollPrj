package gov.nbcs.rp.common.tree;

/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
public interface Node extends Cloneable{
 /**
  * ��ʶ�����ĳ��� 
  */
  public int ROOT=-1;//������ͣ�����֦��Ҷ
  /**
   * ��ʶ֦ͷ�ĳ��� 
   */  
  public int BRANCH=1;
  /**
   * ��ʶҶ���ĳ��� 
   */
  public int LEAF=0;
  
  /**
   * ��ý������
   * @return �������
   */
  public int getNodeType();
  /**
   * ��ø����
   * @return �����
   */
  public Node getParent();
  
  /**
   * ��ý��ֵ
   * @return ���ֵ
   */
  public Object getValue();
  /**
   * �õ���i���ӽ��
   * @param i �±�
   * @return �ӽ��
   */
  public Node getChildAt(int i);
  /**
   * �õ��ӽ����
   * @return �ӽ����
   */
  public int getChildrenCount();
  
  public Object getIdentifier();
  
  public Object getSortByValue();
  
  /**
   * ��ָ����㿪ʼ��������������Ϊ��������������ֵ
   * @param node ������ʼ���
   * @param attrName ������
   * @param attrValue ����ֵ
   * @return �������Ľ��
   */
  public Node search(Node node,String attrName,String attrValue);
  /**
   * ���һ���ӽ��
   * @param child �ӽ��
   */
  public void append(Node child);
  
  /**
   * ָ���±괦����һ���ӽ��
   * @param child 
   * @param i
   */
  public void insert(Node child,int i);
  
  /**
   * ���ý��һ������
   * @param key ������
   * @param value ����ֵ
   */
  public void setAttribute(Object key,Object value);
  
  /**
   * ��ý�������ֵ
   * @param key ������
   * @return ����ֵ
   */
  public Object getAttribute(Object key);
  
  /**
   * �õ�������е���������
   * @return �������ּ��ϵĵ�����
   */
  public java.util.Iterator getAttributeNames();
  
  /**
   * Cloneһ�����
   * @return clone�Ľ��
   */
  public Object clone();
  
  /**
   * ���ý��ֵ
   * @param value ���ֵ
   */
  public void setValue(Object value);
  
  /**
   * �õ��������㼶
   * @return �㼶
   */
  public int getLevel();
  
  /**
   * ������������
   * @return
   */
  public int getHeight();
  
  /**
   * ��������Ŀ�ȣ�������ĩ���ӽ����
   * @return
   */
  public int getWidth(); 
  
  public String getText();
  
  /**
   * �ж��ӽ�����ڵ��±�
   * @param child
   * @return
   */
  public int indexOf(Node child);
   
  /**
   * ���������н�㰴�չ�����Ȼ�Ϊ��ά����ʽ��һά�±���ǽ�������Ĳ�Σ��������������ʼ����㣩
   * @return
   */
  public Node[][] toArray();
  
  /**ɾ��һ���¼��ڵ�*/
  public boolean deleteSubNode(Node node);
}