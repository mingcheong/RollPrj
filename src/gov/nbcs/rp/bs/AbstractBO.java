package gov.nbcs.rp.bs;

import gov.nbcs.rp.dao.PrjectDAO;




/**
 * @author 陈宪标
 * 
 * @version 创建时间：2012-3-14 下午04:24:18
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
