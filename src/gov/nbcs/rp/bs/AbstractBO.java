package gov.nbcs.rp.bs;

import gov.nbcs.rp.dao.PrjectDAO;




/**
 * @author ���ܱ�
 * 
 * @version ����ʱ�䣺2012-3-14 ����04:24:18
 * 
 * @Description AbstractBO
 */
public abstract class AbstractBO
{
	protected PrjectDAO dao;



	public PrjectDAO getDao()
	{
		return dao;
	}


	public void setDao(PrjectDAO dao)
	{
		this.dao = dao;
	}

}
