package gov.nbcs.rp.common.export.action;

import java.util.List;

import gov.nbcs.rp.common.datactrl.DataSet;

public interface ExportBatchProp {
	/**
	 * ��ȡ�����ֶ���
	 * 
	 * @return �����ֶ���
	 */
	public String[] getFieldTName();

	/**
	 * ��ȡ��ʽ���ֶ���
	 * 
	 * @return ��ʽ���ֶ���
	 */
	public String[] getFieldFName();

	/**
	 * ��ȡ�ֶ��п��ֶ���
	 * 
	 * @return �п��ֶ���
	 */
	public String[] getFieldWName();

//	/**
//	 * ��ȡ��ͷ���ݵ�sql���
//	 * 
//	 * @return ��ͷ��sql���
//	 */
//	public String[] getSqlHeader();
//
//	/**
//	 * ���������sql���
//	 * 
//	 * @return �����sql���
//	 */
//	public String[] getSqlBody();

//	/**
//	 * ��ȡ��ͷ���ݼ�(����sql����ʱ������null)
//	 * 
//	 * @return ��ͷ���ݼ�
//	 */
//	public DataSet[] getDsHeader();
//
//	/**
//	 * ��ȡ�������ݼ�(���ݱȽ��ٵ�ʱ����ã�����sql����ʱ������null)
//	 * 
//	 * @return �������ݼ�
//	 */
//	public DataSet[] getDsBody();

	/**
	 * ��ȡ�����
	 * 
	 * @return
	 */
	public String[] getTitles();

	/**
	 * ��ȡС����(ÿ��list��¼���һ��string[])
	 * 
	 * @return
	 */
	public List getTitle_Childs();

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
	 * ������ִ�в�ѯ�������֮ǰҪ���еĲ��� (�洢��ʽ��list+list+string)
	 * @param list_Before ����֮ǰҪִ�е����
	 */
	public List getSqls_BeforeGetBody();

	/**
	 * ������ִ�в�ѯ�������֮��Ҫ���еĲ��� (�洢��ʽ��list+list+string)
	 * @param list_Before ����֮��Ҫִ�е����
	 */
	public List getSqls_AfterGetBody();
	
	public DataSet[] getFaceData();
    
    /**
     * ����ͷ���Ǵӿ����ȡ��ʱ�������Ҫ�����п������øõط�
     * @return
     */
    public List getWidths();
    
    /**
     * ����ͷ���Ǵӿ����ȡ��ʱ�������Ҫ���ø�ʽ�������øõط�
     * @return
     */
    public List getFormats();
    
    /**
     * ����ͷ���Ǵӿ����ȡ��ʱ�������Ҫ�������������øõط�
     * @return
     */
    public List getTypes();
}
