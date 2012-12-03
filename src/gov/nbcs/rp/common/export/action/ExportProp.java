/**
 * ������������
 */

package gov.nbcs.rp.common.export.action;

import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;

public interface ExportProp {	

	/**
	 * ��ȡ�����ֶ���
	 * 
	 * @return �����ֶ���
	 */
	public String getFieldTName();

	/**
	 * ��ȡ��ʽ���ֶ���
	 * 
	 * @return ��ʽ���ֶ���
	 */
	public String getFieldFName();

	/**
	 * ��ȡ�ֶ��п��ֶ���
	 * 
	 * @return �п��ֶ���
	 */
	public String getFieldWName();

	/**
	 * ��ȡ�����
	 * 
	 * @return
	 */
	public String getTitle();

	/**
	 * ��ȡС����
	 * 
	 * @return
	 */
	public String[] getTitle_Child();

	/**
	 * ��ֵ���ڱ��еı�����ʽ ��String[] number = new String[]{"����","������","������"}
	 * 
	 * @return
	 */
	public String[] getNumberViewInTable();

	/**
	 * ����Ĭ�ϱ����ļ�·���������ú��򲻹ܵ����Ƕ������Ҫͨ��ѡ���ѡ�񣬶���ֱ�ӱ��浱ǰ���õ�·���ļ�
	 * 
	 * @return
	 */
	public String getDefaultAddres();
	
	/**
	 * ������ִ�в�ѯ�������֮ǰҪ���еĲ��� 
	 * @param list_Before ����֮ǰҪִ�е����
	 */
	public List getSqls_BeforeGetBody();

	/**
	 * ������ִ�в�ѯ�������֮��Ҫ���еĲ��� 
	 * @param list_Before ����֮��Ҫִ�е����
	 */
	public List getSqls_AfterGetBody();
    
    /**
     * ���÷������� 
     * @return
     */
    public DataSet[] getDsFace();
    
    /**
     * ����ͷ���Ǵӿ����ȡ��ʱ�������Ҫ�����п������øõط�
     * @return
     */
    public float[] getWidths();
    
    /**
     * ����ͷ���Ǵӿ����ȡ��ʱ�������Ҫ���ø�ʽ�������øõط�
     * @return new String[]{"#","#,##0.00","#","###"}
     */
    public String[] getFormats();
    
    /**
     * ����ͷ���Ǵӿ����ȡ��ʱ�������Ҫ�������������øõط�
     * @return
     */
    public String[] getTypes();
    
    
}
