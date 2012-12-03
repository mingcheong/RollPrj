package gov.nbcs.rp.common.print.bs;

import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.print.ibs.IPrint;

public class PrintBO implements IPrint{

	public DataSet getDataSet(String sql) throws Exception {
		// TODO Auto-generated method stub
		return DBSqlExec.getDataSet(sql);
	}

}
