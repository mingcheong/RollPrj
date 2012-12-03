/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.TableColumnInfo;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.cellvalue.FunctionRef;


public class BuildTempTableSql {

	private List lstCells;

	private Map fieldEnameMap;

	private ReportQuerySource querySource;

	final private String _CHAR_TYPE = " VARCHAR2(300)";

	final private String _NUMBER_TYPE = " NUMBER(16,2)";

	public BuildTempTableSql(BuildSql buildSql) {
		this.lstCells = buildSql.getLstCells();
		this.fieldEnameMap = buildSql.getFieldEnameMap();
		this.querySource = buildSql.getQuerySource();
	}

	/**
	 * ��֯������ʱ��sql
	 * 
	 * @return
	 */
	public Map getTempTableSql() throws Exception {
		if (lstCells == null)
			return null;
		// ��Ԫ��
		Object value;
		// ������ֵ
		MyGroupValueImpl groupValueImpl;
		// ������ֵ
		MyCalculateValueImpl calculateValueImpl;

		FunctionRef functionRef;
		IGroupAble groupAble;

		StringBuffer sSql = new StringBuffer();
		// �ֶ�����
		String fieldTypeSave = null;
		// �ֶα���
		String sField = "";
		// ���ϻ��ܲ����
		String sLevel;
		// �����
		String[] levArray;

		boolean isMbSummary = false;

		// ����Դ����Ϣ
		Map mapColfInfo = new HashMap();

		for (int i = 0; i < lstCells.size(); i++) {
			// �õ���Ԫ��ֵ
			value = lstCells.get(i);
			if (value == null) {
				continue;
			}
			// Ϊÿ������enameֵ
			if (value instanceof MyGroupValueImpl) {// ������
				groupValueImpl = (MyGroupValueImpl) value;
				// ѭ������ÿ��ÿ���ֶ�
				for (int j = 0; j < groupValueImpl.getGroupAbleArray().length; j++) {
					groupAble = groupValueImpl.getGroupAbleArray()[j];
					// �Ƿ������˻��ܵ�ҵ����
					if (groupAble.getIsMbSummary() == 1) {
						isMbSummary = true;
					}
					// ���ϻ��ܲ����
					sLevel = groupAble.getLevel();
					// ѭ��������ֶ�ÿ��
					for (int k = 0; k < groupAble.getFormula()
							.getFunctionRefArray().length; k++) {
						functionRef = groupAble.getFormula()
								.getFunctionRefArray()[k];
						// �ж��ڵ�ǰ��ǰ�Ƿ������ͬ�ֶ�
						if (CheckSameCol.isExistSameColGroup(functionRef,
								lstCells, i))
							continue;

						sField = fieldEnameMap.get(functionRef.getRefID())
								.toString();
						DefinePubOther.addComma(sSql);
						int colDisplaySize = getColInfo(mapColfInfo,
								functionRef);
						if (colDisplaySize > 300) {
							fieldTypeSave = " VARCHAR2(" + colDisplaySize + ")";
						} else {
							fieldTypeSave = this._CHAR_TYPE;
						}
						sSql.append(sField);
						sSql.append(fieldTypeSave);

						// �ж��ǲ���Ϊ�����ж��ǲ��ǵ�ǰ�ֶ�
						if (!Common.isNullStr(sLevel)) {
							levArray = sLevel.split(",");
							for (int m = 0; m < levArray.length; m++) {
								sSql.append("," + sField + "_" + levArray[m]
										+ "J" + fieldTypeSave);
							}
						}

					}
				}
			} else if (value instanceof MyCalculateValueImpl) {// ������
				calculateValueImpl = (MyCalculateValueImpl) value;
				if (calculateValueImpl.getMeasureArray() == null)
					continue;
				for (int j = 0; j < calculateValueImpl.getMeasureArray().length; j++) {
					// �ж��ڵ�ǰ��ǰ�Ƿ������ͬ�ֶ�
					if (CheckSameCol.isExistSameColCacl(calculateValueImpl
							.getMeasureArray()[j], lstCells, i))
						continue;

					DefinePubOther.addComma(sSql);
					sField = fieldEnameMap.get(
							calculateValueImpl.getMeasureArray()[j]
									.getMeasureID()).toString();
					if (DefinePubOther.isCountField(calculateValueImpl
							.getMeasureArray()[j])) {
						fieldTypeSave = this._CHAR_TYPE;
					} else {
						fieldTypeSave = this._NUMBER_TYPE;
					}
					sSql.append(sField);
					sSql.append(fieldTypeSave);
				}
			}
		}

		// �Ƿ���ҵ���һ���
		if (isMbSummary) {
			DefinePubOther.addComma(sSql);
			fieldTypeSave = this._CHAR_TYPE;
			sSql.append("DEP_CODE");
			sSql.append(fieldTypeSave);

			DefinePubOther.addComma(sSql);
			sSql.append("DEP_Name");
			sSql.append(fieldTypeSave);
		}

		sSql.insert(0, "create table {" + IDefineReport.TEMP_TABLE + "} (");
		sSql.append(")");

		return DefinePubOther.getBuildSqlMap(sSql.toString(), "",
				IDefineReport.CREATE_QUERY);
	}

	/**
	 * �õ�����Դ����Ϣ
	 * 
	 * @param mapColfInfo
	 * @param functionRef
	 * @return
	 * @throws Exception
	 */
	private int getColInfo(Map mapColfInfo, FunctionRef functionRef)
			throws Exception {
		TableColumnInfo[] colInfo;
		String sSourceID = functionRef.getSourceID();
		String sSrouceColID = functionRef.getSourceColID();
		if (mapColfInfo.containsKey(sSourceID)) {
			colInfo = (TableColumnInfo[]) mapColfInfo.get(sSourceID);
		} else {
			String sDataSourceName = DefinePub.getDataSourceWithID(querySource,
					functionRef.getSourceID()).getSource();
			colInfo = DefineReportI.getMethod().getFieldInfo(sDataSourceName);
			mapColfInfo.put(functionRef.getSourceID(), colInfo);
		}
		int len = colInfo.length;
		for (int i = 0; i < len; i++) {
			if (sSrouceColID.equalsIgnoreCase(colInfo[i].getColumnName())) {
				return colInfo[i].getColumnDisplaySize();
			}
		}

		return -1;
	}

}
