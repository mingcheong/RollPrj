/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.prjset.prjmain.action;


/**
 * FieldInfo.java
 * <p>
 * Title:�ֶ���Ϣ
 * </p>
 * <p>
 * Description:
 * C1,C2...C20...C
 * ��Ҫ��������������Ϣ
 * �ô����һĿ����Ϊ�˽�(C+����)��Ϣ�����е�����������
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008 �㽭�������޹�˾
 * </p>
 * <p>
 * Company: �㽭�������޹�˾
 * </p>
 * <p>
 * CreateData: May 19, 2008
 * </p>
 * 
 * @author GeXinying
 * @version 1.0
 */
public class FieldInfo implements Comparable {
	//������Ϣ
	private String name;
	//������Ϣ�е� ����
	private int num;
	public FieldInfo(String name) {
		this.name = name;
		num = Integer.parseInt(name.substring(1,name.length()));
	}
	public String getName() {
		return name;
	}
	public int getNum() {
		return num;
	}
	/**
	 * �ȽϷ���
	 */
	public int compareTo(Object arg0) {
		int second = ((FieldInfo)arg0).getNum();
		return num>second?1:-1;
	}

}
