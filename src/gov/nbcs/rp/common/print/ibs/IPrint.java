package gov.nbcs.rp.common.print.ibs;

import gov.nbcs.rp.common.datactrl.DataSet;

public interface IPrint {

	public DataSet getDataSet(String sql) throws Exception;
}
