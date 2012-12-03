package gov.nbcs.rp.queryreport.qrbudget.common;

import java.util.ArrayList;
import java.util.List;

import gov.nbcs.rp.queryreport.definereport.ui.BuildLastQuerySql;
import gov.nbcs.rp.queryreport.definereport.ui.DefinePubOther;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.cellvalue.GroupBeanImpl;
import com.fr.report.GroupReport;

public class ReportUnt {

	public ReportUnt() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 单元格复制
	 * 
	 * @param lstCells
	 * @return
	 */
	public static List cloneCell(List lstCells) {
		if (lstCells == null)
			return null;
		int size = lstCells.size();
		if (size == 0)
			return new ArrayList();

		List lstResult = new ArrayList();
		Object value;
		for (int i = 0; i < size; i++) {
			value = lstCells.get(i);
			if (value instanceof IGroupAble) {
				lstResult.add(((GroupBeanImpl) value).clone());
			}
		}
		return lstResult;
	}

	/**
	 * 得到枚举列字段
	 * 
	 * @param groupReport
	 * @param querySource
	 * @return
	 */
	public static List geLevIsTotalField(GroupReport groupReport,
			ReportQuerySource querySource) {
		List lstCells = DefinePubOther.getCellsWithOutClone(groupReport);
		List lstRollup = BuildLastQuerySql.getLevIsTotalField(lstCells,
				querySource);
		List lstLevIsTotalField = ReportUnt.cloneCell(lstRollup);
		return lstLevIsTotalField;
	}
}
