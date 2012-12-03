/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.datactrl.TableColumnInfo;
import gov.nbcs.rp.common.pubinterface.ibs.IPubInterface;
import gov.nbcs.rp.queryreport.definereport.ibs.ICustomStatisticCaliber;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
//import gov.nbcs.rp.sys.sysrefcol.ui.SysRefColPub;
import com.foundercy.pf.control.MessageBox;
import com.foundercy.pf.reportcy.summary.iface.IToSource;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.iface.cell.IMeasureAttr;
import com.foundercy.pf.reportcy.summary.iface.cell.IStatisticCaliber;
import com.foundercy.pf.reportcy.summary.iface.enumer.IEnumSource;
import com.foundercy.pf.reportcy.summary.iface.paras.IParameter;
import com.foundercy.pf.reportcy.summary.iface.source.IDataSource;
import com.foundercy.pf.reportcy.summary.iface.source.IDataSourceRelations;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.cellvalue.FunctionRef;
import com.foundercy.pf.reportcy.summary.object.source.RefEnumSource;
import com.foundercy.pf.reportcy.summary.object.source.RefSource;
import com.foundercy.pf.reportcy.summary.object.source.SummaryDataSourceRelationsImpl;


public class BuildMidQuerySql {

	private List lstCells;

	private Map fieldEnameMap;

	private ReportQuerySource querySource;

	private final String SQL_FIELD = "SLQFIELD";

	private final String SQL_VALUE = "SQLVALUE";

	// �Ƿ�ҵ���һ��ܣ�Ĭ�Ϸ�
	private boolean isMbSummary = false;

	public BuildMidQuerySql(BuildSql buildSql) {
		this.lstCells = buildSql.getLstCells();
		this.fieldEnameMap = buildSql.getFieldEnameMap();
		this.querySource = buildSql.getQuerySource();
	}

	/**
	 * MIDQUERY
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List getMidQuery() throws Exception {
		if (lstCells == null)
			return null;

		// �õ�����Դ
		IDataSource[] dataSource = querySource.getDataSourceManager()
				.getDataSourceArray();

		// �õ�����ԴID���洢��֯��sql
		Map mapSql = getDataSourceID(dataSource);

		// ��Ԫ��ֵ
		Object value;
		List lstShowAll_A = null;
		StringBuffer strUpdateSql = new StringBuffer();
		for (int i = 0; i < lstCells.size(); i++) {
			// �õ���Ԫ��ֵ
			value = lstCells.get(i);
			if (value == null) {
				continue;
			}

			if (value instanceof MyGroupValueImpl) {// ������
				buildMidGroupSql(value, mapSql, i, dataSource);
				List lstShowAll = this.getAllShowSql(value);
				if (lstShowAll != null) {
					if (lstShowAll_A == null) {
						lstShowAll_A = new ArrayList();
					}
					lstShowAll_A.addAll(lstShowAll);
				}
				// ö�������Ͳ���벻��ͬһ�ֶθ������
				String sUpdateSql = this.buildUpdateSqlWithLvlDiffPk(value, i);
				if (!Common.isNullStr(sUpdateSql)) {
					DefinePubOther.addComma(strUpdateSql);
					strUpdateSql.append(sUpdateSql);
				}
			} else if (value instanceof MyCalculateValueImpl) {// ������
				buildMidCalcSql(value, mapSql, i);
			}
		}

		List lstResult = buildMidLastSql(dataSource, mapSql);
		if (lstShowAll_A != null)
			lstResult.addAll(lstShowAll_A);
		// ������ʱ���ֶ�ֵ��䣨�紦��֧����Ŀ���
		if (!Common.isNullStr(strUpdateSql.toString())) {
			String sUpdateSql = this
					.buildUpdateLastSql(strUpdateSql.toString());
			lstResult.add(DefinePubOther.getBuildSqlMap(sUpdateSql, "",
					IDefineReport.MID_QUERY));
		}

		return lstResult;
	}

	/**
	 * ��֯�������м�sql
	 * 
	 */
	private void buildMidCalcSql(Object value, Map mapSql, int curCol) {
		if (!(value instanceof MyCalculateValueImpl))
			return;

		MyCalculateValueImpl calcValue = (MyCalculateValueImpl) value;

		IMeasureAttr measureAttr;
		String sSqlField;
		String sSqlValue;
		String sFieldEname;
		String sourceID;
		for (int j = 0; j < calcValue.getMeasureArray().length; j++) {
			measureAttr = calcValue.getMeasureArray()[j];
			sourceID = measureAttr.getSourceID();
			// �ж��ڵ�ǰ��ǰ�Ƿ������ͬ�ֶ�
			if (CheckSameCol.isExistSameColCacl(measureAttr, lstCells, curCol))
				continue;

			sFieldEname = fieldEnameMap.get(measureAttr.getMeasureID())
					.toString();
			if (!mapSql.containsKey(sourceID)) {
				continue;
			}

			if (mapSql.get(sourceID) == null) {
				sSqlField = "";
				sSqlValue = "";
			} else {

				sSqlField = ((Map) mapSql.get(sourceID)).get(SQL_FIELD)
						.toString();

				sSqlValue = ((Map) mapSql.get(sourceID)).get(SQL_VALUE)
						.toString();
			}

			sSqlField = DefinePubOther.addComma(sSqlField);
			sSqlField = sSqlField + sFieldEname;

			sSqlValue = DefinePubOther.addComma(sSqlValue);
			sSqlValue = sSqlValue
					+ getCaseWhenSql(sourceID, measureAttr.getSourceColID(),
							calcValue);
			if (mapSql.get(sourceID) == null)
				mapSql.put(sourceID, new HashMap());
			((Map) mapSql.get(sourceID)).put(SQL_FIELD, sSqlField);
			((Map) mapSql.get(sourceID)).put(SQL_VALUE, sSqlValue);
		}
	}

	/**
	 * ��֯�������м�sql
	 * 
	 * @throws Exception
	 * 
	 */
	private void buildMidGroupSql(Object value, Map mapSql, int curCol,
			IDataSource[] dataSource) {
		// ������
		MyGroupValueImpl groupValueImpl = (MyGroupValueImpl) value;

		IGroupAble groupAble;
		// ���ϻ��ܲ����
		String sLevel;
		// �����
		String[] levArray;

		FunctionRef[] functionRefArray;
		FunctionRef functionRef;
		// �ֶα���
		String sField = "";
		// ����Դ
		String sourceID;
		// ������
		String sourceColID;

		String sSqlField;
		String sSqlValue;

		String fieldInfo;

		// ѭ������ÿ��ÿ���ֶ�
		int groupArrayLen = groupValueImpl.getGroupAbleArray().length;
		for (int j = 0; j < groupArrayLen; j++) {
			groupAble = groupValueImpl.getGroupAbleArray()[j];
			if (groupAble.getIsMbSummary() == 1) {
				isMbSummary = true;
			}
			// ���ϻ��ܲ����
			sLevel = groupAble.getLevel();
			// ѭ��������ֶ�ÿ��
			functionRefArray = groupAble.getFormula().getFunctionRefArray();
			int refArrayLen = functionRefArray.length;
			for (int k = 0; k < refArrayLen; k++) {
				functionRef = functionRefArray[k];

				// �ж��ڵ�ǰ��ǰ�Ƿ������ͬ�ֶ�
				if (CheckSameCol.isExistSameColGroup(functionRef, lstCells,
						curCol))
					continue;

				sField = fieldEnameMap.get(functionRef.getRefID()).toString();
				// ѭ������ÿһ������Դ
				for (int p = 0; p < dataSource.length; p++) {

					sourceID = dataSource[p].getSourceID();
					// �ж��ǲ���ͬһ����Դ
					if (sourceID.equalsIgnoreCase(functionRef.getSourceID())) {
						sourceColID = functionRef.getSourceColID();
					} else {
						// �ж��Ƿ�ö���ֶ�
						if (DefinePub.judgetEnumWithColID(querySource,
								functionRef.getSourceID(), functionRef
										.getSourceColID(), false)) {// ��ö���ֶ�
							// ������ID�õ���Ӧö��RefEnumSource
							RefEnumSource refEnumSource = DefinePub
									.getEnumRefWithColID(querySource,
											functionRef.getSourceID(),
											functionRef.getSourceColID());
							sourceColID = DefinePub.getSourceColID(querySource,
									refEnumSource, sourceID);

						} else {// ����ö���ֶ�
							sourceColID = getRelaSourcColID(functionRef
									.getSourceID(), functionRef
									.getSourceColID(), sourceID);
						}
					}

					if (Common.isNullStr(sourceColID))
						continue;

					// ��֯�ֶ�ֵ���ࡢ���ʱ��Ӧ�������ֶ�Ҫ������֯,��Ŀ����Acct_name���⴦��
					if (IPubInterface.ACCT_NAME.equalsIgnoreCase(sourceColID)) {
						sourceColID = getFieldValue(lstCells, sourceID,
								sourceColID, dataSource[p].getSourceAlais());
					}

					if (mapSql.get(sourceID) == null) {
						sSqlField = "";
						sSqlValue = "";
					} else {
						sSqlField = ((Map) mapSql.get(sourceID)).get(SQL_FIELD)
								.toString();
						sSqlValue = ((Map) mapSql.get(sourceID)).get(SQL_VALUE)
								.toString();
					}

					// �ж��Ƿ�ö�ٱ����ֶ��Ҳ����������벻��ͬ
					boolean isDiffer = this.isDifferEnumLvlWithPk(functionRef);

					sSqlField = DefinePubOther.addComma(sSqlField);
					sSqlField = sSqlField + sField;
					sSqlValue = DefinePubOther.addComma(sSqlValue);
					// ��������������벻��ͬ���ϻ�������(�磺֧����Ŀ���)
					if (isDiffer
							&& DefinePub.judgetEnumWithColID(querySource,
									functionRef.getSourceID(), functionRef
											.getSourceColID(), true)) {
						String enumId = DefinePub.getEnumIDWithColID(
								this.querySource, sourceID, sourceColID);
						IEnumSource enumSource = DefinePub.getEnumInfoWithID(
								this.querySource, enumId);
						sSqlValue = sSqlValue
								+ this.getEnumLvlPkDifferCode(dataSource[p]
										.getSourceAlais(), sourceColID,
										enumSource);
					} else {
						sSqlValue = sSqlValue + sourceColID;
					}

					// �ж�sLevel�ǲ���Ϊ�����ж��ǲ��ǵ�ǰ�ֶ�,��ö�ٲ������������ͬ
					if (!Common.isNullStr(sLevel) && !isDiffer) {
						levArray = sLevel.split(",");
						boolean isEnumName = DefinePub.judgetEnumNameWithColID(
								querySource, sourceID, sourceColID);

						for (int m = 0; m < levArray.length; m++) {
							if (isEnumName) {
								fieldInfo = getFieldInfoWithName(Integer
										.parseInt(levArray[m]), sourceID,
										dataSource[p].getSourceAlais(),
										sourceColID, groupAble.getIsTotal());
							} else {
								fieldInfo = getFieldInfoWithCode(Integer
										.parseInt(levArray[m]), sourceID,
										dataSource[p].getSourceAlais(),
										sourceColID, groupAble.getIsTotal(),
										functionRef.getSourceColID());

							}
							if (fieldInfo == null)
								continue;
							sSqlField = sSqlField + "," + sField + "_"
									+ levArray[m] + "J";
							sSqlValue = sSqlValue + ",";
							sSqlValue = sSqlValue + fieldInfo;
						}
					}
					if (mapSql.get(sourceID) == null)
						mapSql.put(sourceID, new HashMap());
					((Map) mapSql.get(sourceID)).put(SQL_FIELD, sSqlField);
					((Map) mapSql.get(sourceID)).put(SQL_VALUE, sSqlValue);
				}
			}
		}
	}

	/**
	 * �õ���ʾ������Ϣsql�б�
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private List getAllShowSql(Object value) throws Exception {
		List lstAllshow = null;
		// ������
		MyGroupValueImpl groupValueImpl = (MyGroupValueImpl) value;
		IGroupAble groupAble;

		// ѭ������ÿ��ÿ���ֶ�
		int groupArrayLen = groupValueImpl.getGroupAbleArray().length;
		for (int j = 0; j < groupArrayLen; j++) {
			groupAble = groupValueImpl.getGroupAbleArray()[j];
			// ����������ʾ
			if (groupAble.getIsCorss() == 1) {
				if (lstAllshow == null)
					lstAllshow = new ArrayList();
				lstAllshow.add(buildShowAllSql(groupAble));
			}
		}
		return lstAllshow;
	}

	/**
	 * ��֯�м�����sql
	 * 
	 * @param dataSource
	 * @param isMbSummary
	 * @return
	 */
	private List buildMidLastSql(IDataSource[] dataSource, Map mapSql) {
		List lstResult = new ArrayList();
		StringBuffer sSql;
		Map mapResult;
		String sFilter;
		Map mapTmp;
		String sourceID;
		StringBuffer sSqlField;
		StringBuffer sSqlValue;
		for (int i = 0; i < dataSource.length; i++) {
			sSqlField = new StringBuffer();
			sSqlValue = new StringBuffer();
			sSql = new StringBuffer();
			sourceID = dataSource[i].getSourceID();
			mapTmp = (Map) mapSql.get(sourceID);
			if (mapTmp == null)
				continue;
			sSqlField.append(mapTmp.get(SQL_FIELD).toString());
			sSqlValue.append(mapTmp.get(SQL_VALUE).toString());

			// ҵ���һ���sql����
			buildIsMbSummarySql(sSqlField, sSqlValue, dataSource[i]
					.getSourceAlais());

			// �õ�����Դ��������
			sFilter = getWhereSql(this.querySource, sourceID);

			sSql.append("insert into {" + IDefineReport.TEMP_TABLE + "} ("
					+ sSqlField.toString() + ") select " + sSqlValue.toString()
					+ " from ");

			if (dataSource[i].getSourceType() == 2) {
				sSql.append("(" + dataSource[i].getSource() + ") ");
			} else {
				sSql.append(dataSource[i].getSource() + " ");
			}

			sSql.append(dataSource[i].getSourceAlais());
			sSql.append(" where 1=1 " + sFilter);

			mapResult = DefinePubOther.getBuildSqlMap(sSql.toString(),
					sourceID, IDefineReport.MID_QUERY);
			lstResult.add(mapResult);
		}
		return lstResult;
	}

	/**
	 * ҵ���һ���sql����
	 * 
	 * @param sSqlField
	 *            �ֶ���
	 * @param sSqlValue
	 *            �ֶ�ֵ
	 * @param sourceAlais
	 *            ����Դ����
	 */
	private void buildIsMbSummarySql(StringBuffer sSqlField,
			StringBuffer sSqlValue, String sourceAlais) {
		if (isMbSummary) {
			DefinePubOther.addComma(sSqlField);
			sSqlField.append("dep_code,dep_name");
			DefinePubOther.addComma(sSqlValue);
			sSqlValue.append(getDepSql(sourceAlais));
		}
	}

	/**
	 * ����������ʾ,���⴦��
	 * 
	 * @return
	 * @throws Exception
	 */
	private Map buildShowAllSql(IGroupAble groupAbleValue) throws Exception {

		String sourceID_A = groupAbleValue.getSourceID();
		String sourceColID_A = groupAbleValue.getSourceColID();

		// ��Ԫ��
		Object value;
		// ������ֵ
		IGroupAble[] groupAbleArray;
		IGroupAble groupAble;
		FunctionRef functionRef;

		// �ֶα���
		String sFieldEname = "";
		// ���ϻ��ܲ����
		String sLevel;
		// �����
		String[] levArray;

		StringBuffer sSqlField = new StringBuffer();
		StringBuffer sSqlValue = new StringBuffer();

		// ����Դ
		String sourceID;
		String sourceColID;
		// ����Դ����
		String sourceAlais = "A";

		String fieldInfo;

		// �õ�ö��ID
		String enumID = DefinePub.getEnumIDWithColID(querySource, sourceID_A,
				sourceColID_A);
		// �õ�ö����Ϣ����ö��ID
		IEnumSource enumSource = DefinePub.getEnumInfoWithID(querySource,
				enumID);
		// ������RefEnumSource��Ϣ
		RefEnumSource refEnumSource = DefinePub.getEnumRefWithColID(
				querySource, sourceID_A, sourceColID_A);
		// �õ��������ֶ������ݱ������ֶ���
		String sourceColID_Name = DefinePub.getNameWithCode(querySource,
				sourceID_A, sourceColID_A);
		// ������RefEnumSource��Ϣ
		RefEnumSource refEnumSource_Name = DefinePub.getEnumRefWithColID(
				querySource, sourceID_A, sourceColID_Name);

		for (int i = 0; i < lstCells.size(); i++) {
			// �õ���Ԫ��ֵ
			value = lstCells.get(i);
			if (value == null) {
				continue;
			}
			// Ϊÿ������enameֵ
			if (!(value instanceof MyGroupValueImpl)) {// ������
				continue;
			}

			groupAbleArray = ((MyGroupValueImpl) value).getGroupAbleArray();
			int groupLen = groupAbleArray.length;
			// ѭ������ÿ��ÿ���ֶ�
			for (int j = 0; j < groupLen; j++) {
				groupAble = groupAbleArray[j];

				// ���ϻ��ܲ����
				sLevel = groupAble.getLevel();
				// ѭ��������ֶ�ÿ��
				for (int k = 0; k < groupAbleArray[j].getFormula()
						.getFunctionRefArray().length; k++) {
					functionRef = groupAbleArray[j].getFormula()
							.getFunctionRefArray()[k];
					sourceID = functionRef.getSourceID();
					sourceColID = functionRef.getSourceColID();

					// �ж��ڵ�ǰ��ǰ�Ƿ������ͬ�ֶ�
					if (CheckSameCol.isExistSameColGroup(functionRef, lstCells,
							i))
						continue;

					// �Ƿ�ö������
					boolean isEnumName = DefinePub.judgetEnumNameWithColID(
							querySource, sourceID, sourceColID);
					RefEnumSource refEnumSourceTmp = DefinePub
							.getEnumRefWithColID(querySource, sourceID,
									sourceColID);
					if (refEnumSourceTmp == null)
						continue;

					if (isEnumName) {
						if (!(refEnumSourceTmp.getEnumID().equalsIgnoreCase(
								refEnumSource_Name.getEnumID()) && refEnumSourceTmp
								.getRefEnumColCode().equalsIgnoreCase(
										refEnumSource_Name.getRefEnumColCode()))) {
							continue;
						}

					} else {
						if (!(refEnumSourceTmp.getEnumID().equalsIgnoreCase(
								refEnumSource.getEnumID()) && refEnumSourceTmp
								.getRefEnumColCode().equalsIgnoreCase(
										refEnumSource.getRefEnumColCode()))) {
							continue;
						}
					}

					sFieldEname = fieldEnameMap.get(functionRef.getRefID())
							.toString();

					DefinePubOther.addComma(sSqlField);
					sSqlField.append(sFieldEname);
					DefinePubOther.addComma(sSqlValue);

					if (isEnumName) {
						sSqlValue
								.append(refEnumSource_Name.getRefEnumColCode());
					} else {
						sSqlValue.append(refEnumSource.getRefEnumColCode());
					}

					// �ж��ǲ���Ϊ�����ж��ǲ��ǵ�ǰ�ֶ�
					if (!Common.isNullStr(sLevel)) {
						levArray = sLevel.split(",");

						for (int m = 0; m < levArray.length; m++) {
							if (isEnumName) {
								fieldInfo = getFieldInfoWithName(Integer
										.parseInt(levArray[m]), sourceID,
										sourceAlais, refEnumSource_Name
												.getRefEnumColCode(), groupAble
												.getIsTotal(), enumSource,
										refEnumSource.getRefEnumColCode());
							} else {
								fieldInfo = getFieldInfoWithCode(Integer
										.parseInt(levArray[m]), sourceID,
										sourceAlais, refEnumSource
												.getRefEnumColCode(), groupAble
												.getIsTotal(), enumSource,
										refEnumSource.getRefEnumColCode());

							}
							if (fieldInfo == null)
								continue;
							sSqlField.append("," + sFieldEname + "_"
									+ levArray[m] + "J");
							sSqlValue.append("," + fieldInfo);
						}
					}
				}
			}
		}

		TableColumnInfo[] colInfo = this.getColInfo(enumSource);
		if (this.checkFieldIsExist(colInfo, IPubInterface.DIV_CODE)) {
			// ҵ���һ���sql����
			buildIsMbSummarySql(sSqlField, sSqlValue, sourceAlais);
		}

		StringBuffer sSql = new StringBuffer();
		sSql.append("insert into {" + IDefineReport.TEMP_TABLE + "} ("
				+ sSqlField + ") select " + sSqlValue + " from ");
		sSql.append("(" + enumSource.getSource() + ") " + sourceAlais);
		String sLeafField = this.getLeafField(colInfo);
		sSql.append(" where 1=1");
		if (!Common.isNullStr(sLeafField)) {
			sSql.append(" and " + sLeafField + " =1");
		}
		sSql.append(" and set_year =getcuryear()");

		return DefinePubOther.getBuildSqlMap(sSql.toString(), "",
				IDefineReport.MID_QUERY);

	}

	/**
	 * ��֯case when �����ֶ�����
	 * 
	 * @param sourceID
	 * @param sourceColID
	 * @param calculateValueImpl
	 * @return
	 */
	private String getCaseWhenSql(String sourceID, String sourceColID,
			MyCalculateValueImpl calculateValueImpl) {
		String sWhereSql = getCaseWhenWhere(sourceID, calculateValueImpl);
		String sCaseSql;
		if (Common.isNullStr(sWhereSql))
			sCaseSql = sourceColID;
		else {
			sCaseSql = "case when " + sWhereSql + " then " + sourceColID
					+ " end";
		}
		// 1:Ԫת��Ϊ��Ԫ�������������ٻ���
		if (DefinePub.getMoneyOp(querySource) == 1) {
			sCaseSql = "Round((" + sCaseSql + ")/10000,2)";
		}
		return sCaseSql;
	}

	/**
	 * �õ�������case when ����
	 * 
	 * @param sourceID
	 * @param calculateValueImpl
	 * @return
	 */
	private String getCaseWhenWhere(String sourceID,
			MyCalculateValueImpl calculateValueImpl) {
		IStatisticCaliber[] statisticCaliber = calculateValueImpl
				.getStatisticCaliberArray();
		if (statisticCaliber == null)
			return "";
		String sValue;
		String addSql;
		String sSqlTmp = "";
		String sSqlAddTemp = "";
		for (int i = 0; i < statisticCaliber.length; i++) {
			if (!sourceID.equalsIgnoreCase(statisticCaliber[i].getSourceID())) {
				continue;
			}

			sValue = statisticCaliber[i].getValue();
			if (sValue != null && !Common.isNullStr(sValue)) {
				if (statisticCaliber[i].getCompareType().equalsIgnoreCase(
						IDefineReport.OPE_LIKE)
						|| statisticCaliber[i].getCompareType()
								.equalsIgnoreCase(IDefineReport.OPE_NOTLIKE))
					sValue = sValue.substring(0, sValue.length() - 1) + "%"
							+ sValue.substring(sValue.length() - 1);

				if (!Common.isNullStr(sSqlTmp)) {
					sSqlTmp = sSqlTmp + " "
							+ statisticCaliber[i].getJoinBefore();
				}
				if (statisticCaliber[i] instanceof ICustomStatisticCaliber) {
					if (((ICustomStatisticCaliber) statisticCaliber[i])
							.getLParenthesis() != null) {
						sSqlTmp = sSqlTmp
								+ ((ICustomStatisticCaliber) statisticCaliber[i])
										.getLParenthesis();
					}
				}
				sSqlTmp = sSqlTmp + " " + statisticCaliber[i].getSourceColID()
						+ " " + statisticCaliber[i].getCompareType() + " "
						+ sValue;

				if (statisticCaliber[i] instanceof ICustomStatisticCaliber) {
					if (((ICustomStatisticCaliber) statisticCaliber[i])
							.getLParenthesis() != null) {
						sSqlTmp = sSqlTmp
								+ ((ICustomStatisticCaliber) statisticCaliber[i])
										.getRParenthesis();
					}
				}
			}
			addSql = statisticCaliber[i].getAddSQL();
			if (addSql != null && !Common.isNullStr(addSql)) {
				if (!Common.isNullStr(sSqlTmp)
						|| !Common.isNullStr(sSqlAddTemp)) {
					sSqlAddTemp = sSqlAddTemp + " "
							+ statisticCaliber[i].getJoinBefore();
				}
				sSqlAddTemp = sSqlAddTemp + addSql;
			}
		}
		if (!Common.isNullStr(sSqlTmp)) {
			sSqlTmp = "(" + sSqlTmp + ")";
		}

		sSqlTmp = sSqlTmp + sSqlAddTemp;
		return sSqlTmp;
	}

	/**
	 * ����Դ��������
	 * 
	 * @param querySource
	 * @param sourceID
	 * @return
	 */
	private String getWhereSql(ReportQuerySource querySource, String sourceID) {
		String sWhereSql = "";
		IParameter[] parameter = querySource.getParameterArray();
		IToSource[] toSource;
		String chName;
		// �Ƚ�����
		String compareType;
		// ��������
		String joinBefore;
		// ֵ
		String sValue;
		int iCount = parameter.length;
		for (int i = 0; i < iCount; i++) {
			chName = parameter[i].getChName();
			// �ж��Ƿ�Ĭ�ϲ��������ǲ����д���
			if (DataSourceSet.checkDefaultPara(chName)) {
				continue;
			}
			toSource = parameter[i].getToSourceArray();
			for (int j = 0; j < toSource.length; j++) {
				if (sourceID.equals(toSource[j].getSourceID())) {
					// ֵ
					sValue = parameter[i].getValue().toString();
					// �Ƚ�����
					compareType = parameter[i].getCompareType();
					if (compareType.equalsIgnoreCase(IDefineReport.OPE_LIKE)
							|| compareType
									.equalsIgnoreCase(IDefineReport.OPE_NOTLIKE)) {
						sValue = sValue.substring(0, sValue.length() - 1) + "%"
								+ sValue.substring(sValue.length() - 1);
					}
					// ��������
					joinBefore = parameter[i].getJoinBefore();
					if (Common.isNullStr(sWhereSql)) {
						// ��һ������������and����
						sWhereSql = sWhereSql + " and (";
					} else {
						sWhereSql = sWhereSql + " " + joinBefore + " ";
					}
					// ��������
					if (parameter[i] instanceof MySummaryParameterImpl
							&& ((MySummaryParameterImpl) parameter[i])
									.getLParenthesis() != null) {

						sWhereSql = sWhereSql
								+ ((MySummaryParameterImpl) parameter[i])
										.getLParenthesis();
					}
					sWhereSql = sWhereSql + toSource[j].getSourceColID() + " "
							+ compareType + " " + sValue;
					// ��������
					if (parameter[i] instanceof MySummaryParameterImpl
							&& ((MySummaryParameterImpl) parameter[i])
									.getRParenthesis() != null) {
						sWhereSql = sWhereSql
								+ ((MySummaryParameterImpl) parameter[i])
										.getRParenthesis();
					}

				}
			}
		}
		if (!Common.isNullStr(sWhereSql))
			sWhereSql = sWhereSql + ")";
		return sWhereSql;
	}

	/**
	 * ������ϼ���ѯ���
	 * 
	 * @param iLev���
	 * @param sourceID����ԴID
	 * @param sourceColID����Դ�ֶ�ID
	 * @param isTotal�Ƿ�ĩ������
	 * @param enumSourceö��
	 * @return
	 */
	private String getFieldInfoWithCode(int iLev, String sourceID,
			String sourceAlais, String sourceColID, String isTotal,
			IEnumSource enumSource, String codeColID) {

		String levelInfo = enumSource.getLevelInfo();
		String levArray[] = levelInfo.split("-");
		int iLen = 0;
		for (int i = 0; i < iLev; i++) {
			iLen = iLen + Integer.parseInt(levArray[i]);
		}

		String enumID = enumSource.getEnumID();
		if (Common.estimate(isTotal)) {
			String sLeafField = null;
			try {
				sLeafField = getLeafField(enumSource);
			} catch (Exception e) {
				e.printStackTrace();
				new MessageBox("�õ�ĩ���ֶ����Ƴ���", e.getMessage(), MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();

			}

			String sSource = enumSource.getSource().toUpperCase();
			String levelCode = enumID + "." + enumSource.getLevelCode();
			return "(select " + enumID + "." + sourceColID + " from ("
					+ sSource + ") " + enumID + " where length(" + sourceAlais
					+ "." + codeColID + ")>=" + iLen + " and " + levelCode
					+ "=substr(" + sourceAlais + "." + codeColID + ",1," + iLen
					+ ") and " + sourceAlais + ".set_year =" + enumID
					+ ".set_year and " + sLeafField + "=0)";
		} else {
			String levelCode = sourceAlais + "." + sourceColID;
			return "(select subStr(" + levelCode + ",1," + iLen
					+ ") from dual " + "where length(" + levelCode + ")>="
					+ iLen + ")";
		}
	}

	/**
	 * ������ϼ���ѯ���
	 * 
	 * @param iLev���
	 * @param sourceID����ԴID
	 * @param sourceColID����Դ�ֶ�ID
	 * @param isTotal�Ƿ�ĩ������
	 * @return
	 */
	private String getFieldInfoWithCode(int iLev, String sourceID,
			String sourceAlais, String sourceColID, String isTotal,
			String codeColID) {
		String enumID = DefinePub.getEnumIDWithColID(querySource, sourceID,
				sourceColID);
		if (enumID == null)
			return null;
		IEnumSource enumSource = DefinePub.getEnumInfoWithID(querySource,
				enumID);
		// ���������ֶεõ���Ӧ��ö���ֶ�
		sourceColID = DefinePub.getEnumRefWithColID(querySource, sourceID,
				sourceColID).getRefEnumColCode();

		return this.getFieldInfoWithCode(iLev, sourceID, sourceAlais,
				sourceColID, isTotal, enumSource, codeColID);
	}

	/**
	 * ���Ƶ��ϼ���ѯ���
	 * 
	 * @param iLev���
	 * @param sourceID����ԴID
	 * @param sourceColID����Դ�ֶ�ID
	 * @param isTotal�Ƿ�ĩ������
	 * @return
	 */
	private String getFieldInfoWithName(int iLev, String sourceID,
			String sourceAlais, String sourceColID, String isTotal) {
		// �õ�ö��
		String enumID = DefinePub.getEnumIDWithColID(querySource, sourceID,
				sourceColID);
		IEnumSource enumSource = DefinePub.getEnumInfoWithID(querySource,
				enumID);
		// �õ�����������
		String codeColID = DefinePub.getCodeWithName(querySource, sourceID,
				sourceColID);

		// ���������ֶεõ���Ӧ��ö���ֶ�
		sourceColID = DefinePub.getEnumRefWithColID(querySource, sourceID,
				sourceColID).getRefEnumColCode();

		return this.getFieldInfoWithName(iLev, sourceID, sourceAlais,
				sourceColID, isTotal, enumSource, codeColID);

	}

	/**
	 * ���Ƶ��ϼ���ѯ���
	 * 
	 * @param iLev���
	 * @param sourceID����ԴID
	 * @param sourceColID����Դ�ֶ�ID
	 * @param isTotal�Ƿ�ĩ������
	 * @param enumSource
	 * @return
	 */
	private String getFieldInfoWithName(int iLev, String sourceID,
			String sourceAlais, String sourceColID, String isTotal,
			IEnumSource enumSource, String codeColID) {
		String levelInfo = enumSource.getLevelInfo();
		String levArray[] = levelInfo.split("-");
		int iLen = 0;
		for (int i = 0; i < iLev; i++) {
			iLen = iLen + Integer.parseInt(levArray[i]);
		}

		String enumID = enumSource.getEnumID();
		String levelCode = enumID + "." + enumSource.getLevelCode();

		String sSource = enumSource.getSource().toUpperCase();

		String sSql = "(select " + enumID + "." + sourceColID + " from ("
				+ sSource + ") " + enumID + " where length(" + sourceAlais
				+ "." + codeColID + ")>=" + iLen + " and " + levelCode
				+ "=substr(" + sourceAlais + "." + codeColID + ",1," + iLen
				+ ") and " + sourceAlais + ".set_year =" + enumID + ".set_year";
		if (Common.estimate(isTotal)) {

			String sLeafField = null;
			try {
				sLeafField = getLeafField(enumSource);
			} catch (Exception e) {
				e.printStackTrace();
				new MessageBox("�õ�ĩ���ֶ����Ƴ���", e.getMessage(), MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();
			}

			sSql = sSql + " and " + sLeafField + "=0)";
		} else {
			sSql = sSql + ")";
		}
		return sSql;
	}

	/**
	 * ���Ƶ��ϼ���ѯ���
	 * 
	 * @param iLen���볤��
	 * @param sourceID����ԴID
	 * @param sourceColID����Դ�ֶ�ID
	 * @return
	 */
	private String getFieldInfoWithName(int iLen, String sourceID,
			String sourceAlais, String sourceColID) {
		// �õ�ö��
		String enumID = DefinePub.getEnumIDWithColID(querySource, sourceID,
				sourceColID);
		IEnumSource enumSource = DefinePub.getEnumInfoWithID(querySource,
				enumID);

		// �õ�����������
		String codeColID = DefinePub.getCodeWithName(querySource, sourceID,
				sourceColID);

		String levelCode = enumID + "." + enumSource.getLevelCode();

		String sSource = enumSource.getSource().toUpperCase();

		String sSql = "(select " + enumID + "." + sourceColID + " from ("
				+ sSource + ") " + enumID + " where " + levelCode + "=substr("
				+ sourceAlais + "." + codeColID + ",1," + iLen + ") and "
				+ sourceAlais + ".set_year =" + enumID + ".set_year)";
		return sSql;
	}

	/**
	 * �õ�����Ϣ
	 * 
	 * @param enumSource
	 * @return
	 * @throws Exception
	 */
	private TableColumnInfo[] getColInfo(IEnumSource enumSource)
			throws Exception {
		String sSqlDet = enumSource.getSource();
//		sSqlDet = SysRefColPub.ReplaceRefColFixFlag(sSqlDet.toUpperCase());
//		return SysRefColI.getMethod().getFieldInfo(sSqlDet);
		return null;
	}

	private boolean checkFieldIsExist(TableColumnInfo[] colInfo,
			String sFieldName) {
		int size = colInfo.length;
		for (int i = 0; i < size; i++) {
			if (sFieldName.equalsIgnoreCase(colInfo[i].getColumnName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �ж�ĩ�����ֶ���
	 * 
	 * @param colInfo
	 * @return
	 * @throws Exception
	 */
	private String getLeafField(TableColumnInfo[] colInfo) throws Exception {
		int size = colInfo.length;
		for (int i = 0; i < size; i++) {
			if ("is_leaf".equalsIgnoreCase(colInfo[i].getColumnName())) {
				return "is_leaf";
			} else if ("end_flag".equalsIgnoreCase(colInfo[i].getColumnName())) {
				return "end_flag";
			}
		}
		return null;
	}

	/**
	 * �ж�ĩ�����ֶ���
	 * 
	 * @param enumSource
	 * @return
	 * @throws Exception
	 */
	private String getLeafField(IEnumSource enumSource) throws Exception {
//		String sSqlDet = enumSource.getSource();
////		sSqlDet = SysRefColPub.ReplaceRefColFixFlag(sSqlDet.toUpperCase());
////		TableColumnInfo[] colInfo = SysRefColI.getMethod()
////				.getFieldInfo(sSqlDet);
//		int size = colInfo.length;
//		for (int i = 0; i < size; i++) {
//			if ("is_leaf".equalsIgnoreCase(colInfo[i].getColumnName())) {
//				return "is_leaf";
//			} else if ("end_flag".equalsIgnoreCase(colInfo[i].getColumnName())) {
//				return "end_flag";
//			}
//		}
		return null;
	}

	/**
	 * �õ�����ԴID
	 * 
	 * @return
	 */
	private Map getDataSourceID(IDataSource[] dataSource) {
		Map resultMap = new HashMap();
		int dataSourecLen = dataSource.length;

		String sourceID;
		for (int i = 0; i < dataSourecLen; i++) {
			sourceID = dataSource[i].getSourceID();
			if (checkDataSourceIsUse(sourceID)) {
				resultMap.put(dataSource[i].getSourceID(), null);
			}
		}
		return resultMap;
	}

	/**
	 * �ж�����Դ�Ƿ�ʹ��
	 * 
	 * @param sourceID
	 * @return
	 */
	private boolean checkDataSourceIsUse(String sourceID) {
		if (Common.isNullStr(sourceID)) {
			return false;
		}
		int cellsSize = lstCells.size();
		Object value;
		for (int i = 0; i < cellsSize; i++) {
			value = lstCells.get(i);
			if (value instanceof MyGroupValueImpl) {// ������
				IGroupAble[] groupAbleArray = ((MyGroupValueImpl) value)
						.getGroupAbleArray();
				int groupArrayLen = groupAbleArray.length;
				for (int j = 0; j < groupArrayLen; j++) {
					if (sourceID.equalsIgnoreCase(groupAbleArray[j]
							.getSourceID())) {
						return true;
					}
				}
			} else if (value instanceof MyCalculateValueImpl) {// ������
				IMeasureAttr[] measureAttr = ((MyCalculateValueImpl) value)
						.getMeasureArray();
				int measureAttrLen = measureAttr.length;
				for (int j = 0; j < measureAttrLen; j++) {
					if (sourceID.equalsIgnoreCase(measureAttr[j].getSourceID())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * �õ�������ϵ�ֶ�
	 * 
	 * @param fromSourceID
	 * @param fromSourceColID
	 * @param ToSourceID
	 * @return
	 */
	private String getRelaSourcColID(String fromSourceID,
			String fromSourceColID, String ToSourceID) {
		IDataSourceRelations[] dataSourceRelations = querySource
				.getDataSourceManager().getDataSourceRelationsArray();
		if (dataSourceRelations == null || dataSourceRelations.length == 0)
			return null;
		return getRela(fromSourceID, fromSourceColID, ToSourceID, null,
				dataSourceRelations);
	}

	private String getRela(String fromSourceID, String fromSourceColID,
			String ToSourceID, String relaID,
			IDataSourceRelations[] dataSourceRelations) {
		RefSource[] refSource;
		for (int i = 0; i < dataSourceRelations.length; i++) {
			if (dataSourceRelations[i].getID().equals(relaID))
				continue;
			refSource = ((SummaryDataSourceRelationsImpl) dataSourceRelations[i])
					.getRefSourceArray();
			if (fromSourceID.equals(refSource[0].getSourceID())
					&& fromSourceColID.equals(refSource[0].getSourceColID())) {
				if (ToSourceID.equals(refSource[1].getSourceID()))
					return refSource[1].getSourceColID();
				else
					return getRela(refSource[1].getSourceID(), refSource[1]
							.getSourceColID(), ToSourceID,
							dataSourceRelations[i].getID(), dataSourceRelations);
			} else if (fromSourceID.equals(refSource[1].getSourceID())
					&& fromSourceColID.equals(refSource[1].getSourceColID())
					&& ToSourceID.equals(refSource[0].getSourceID())) {
				if (ToSourceID.equals(refSource[0].getSourceID()))
					return refSource[0].getSourceColID();
				else
					return getRela(refSource[0].getSourceID(), refSource[0]
							.getSourceColID(), ToSourceID,
							dataSourceRelations[i].getID(), dataSourceRelations);
			}
		}
		return null;
	}

	/**
	 * �õ�ҵ���ұ���sql
	 * 
	 * @param sourceID
	 * @return
	 */
	private String getDepSql(String sourceAlais) {
		return "(select dep_code from fb_u_deptodiv dep where dep.div_code = "
				+ sourceAlais
				+ ".div_code and dep.set_year = "
				+ sourceAlais
				+ ".set_year),(select dep_name from fb_u_deptodiv dep where dep.div_code = "
				+ sourceAlais + ".div_code and dep.set_year = " + sourceAlais
				+ ".set_year)";
	}

	/**
	 * ��֯�ֶ�ֵ���ࡢ���ʱ��Ӧ�������ֶ�Ҫ������֯
	 * 
	 * @param list���б�
	 * @param sourceID����ԴID
	 * @param sourceColID�ֶ�ID
	 * @param sourceAlais����Դ����
	 * @return
	 */
	private String getFieldValue(List list, String sourceID,
			String sourceColID, String sourceAlais) {
		// �ж��Ƿ�������
		boolean isEnumName = DefinePub.judgetEnumNameWithColID(querySource,
				sourceID, sourceColID);
		if (!isEnumName) {
			return sourceColID;
		}

		// �õ�ö��ID
		String enumIdCode = DefinePub.getEnumIDWithColID(querySource, sourceID,
				sourceColID);

		Object value;
		IGroupAble[] groupAble;

		String enumID;
		boolean isEnumPri;
		String content;
		int index;
		String[] contentArray;

		// ��ű��������
		int max = 0;
		int maxTmp;
		// �õ���Ӧ�Ĵ����У���֯���Ƶ��ֶ�ֵ����Ҫ�����ࡢ�������
		int size = list.size();
		for (int i = 0; i < size; i++) {
			value = list.get(i);
			if (value == null)
				continue;

			// �ж��Ƿ��Ƿ�����
			if (value instanceof MyGroupValueImpl) {
				groupAble = ((MyGroupValueImpl) value).getGroupAbleArray();
				for (int j = 0; j < groupAble.length; j++) {
					// �õ�ö��ID
					enumID = DefinePub.getEnumIDWithColID(querySource,
							groupAble[j].getSourceID(), groupAble[j]
									.getSourceColID());
					// �ж��Ƿ��Ӧͬһö��
					if (!enumIdCode.equals(enumID))
						continue;

					// �ж��ǲ���ö������
					isEnumPri = DefinePub.judgetEnumWithColID(querySource,
							groupAble[j].getSourceID(), groupAble[j]
									.getSourceColID(), true);
					if (!isEnumPri)
						continue;
					content = groupAble[j].getFormula().getContent();
					index = content.toLowerCase().indexOf(
							GroupColumnDialog.OPER_SUBSTR);
					if (index != -1) {
						content = content.substring(content.indexOf("("));
						content = content.substring(0, content.indexOf(")"));
						contentArray = content.split(",");
						if (contentArray.length == 2)
							return sourceColID;
						else {
							maxTmp = Integer.parseInt(contentArray[1])
									+ Integer.parseInt(contentArray[2]) - 1;
							if (max < maxTmp) {
								max = maxTmp;
							}
						}
					} else {
						return sourceColID;
					}
				}
			}
		}

		if (max > 0) {
			return getFieldInfoWithName(max, sourceID, sourceAlais, sourceColID);
		}
		return sourceColID;

	}

	/**
	 * �õ���Ӧ��ö�٣������������Ƿ���ͬ����ͬ����false,����ͬ����true
	 * 
	 * @param functionRef
	 * @return
	 */
	private boolean isDifferEnumLvlWithPk(FunctionRef functionRef) {
		String enumId = DefinePub.getEnumIDWithColID(querySource, functionRef
				.getSourceID(), functionRef.getSourceColID());
		if (Common.isNullStr(enumId))
			return false;
		IEnumSource enumSource = DefinePub.getEnumInfoWithID(this.querySource,
				enumId);
		if (enumSource.getLevelCode().equalsIgnoreCase(enumSource.getPk())) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * ö�������Ͳ���벻һ��ʱ����֧����Ŀ��𣩣���ѯ��������Ϊ���
	 * 
	 * @param sourceAlais����Դ����
	 * @param sourceColID������
	 * @param enumSourceö����Ϣ
	 * @return
	 */
	private String getEnumLvlPkDifferCode(String sourceAlais,
			String sourceColID, IEnumSource enumSource) {
		String enumID = enumSource.getEnumID();
		String pkCode = enumSource.getPk();
		String sSource = enumSource.getSource().toUpperCase();
		String levelCode = enumSource.getLevelCode();
		return "(select " + enumID + "." + levelCode + " from (" + sSource
				+ ") " + enumID + " where " + enumID + "." + pkCode + " ="
				+ sourceAlais + "." + sourceColID + " and " + sourceAlais
				+ ".set_year =" + enumID + ".set_year)";

	}

	/**
	 * ���ϻ���(����ö�������Ͳ���벻��ͬһ�ֶΣ�
	 * 
	 * @param value
	 * @param curCol
	 * @return
	 */
	private String buildUpdateSqlWithLvlDiffPk(Object value, int curCol) {
		// ������
		MyGroupValueImpl groupValueImpl = (MyGroupValueImpl) value;

		IGroupAble groupAble;
		// ���ϻ��ܲ����
		String sLevel;
		// �����
		String[] levArray;

		FunctionRef[] functionRefArray;
		FunctionRef functionRef;
		// ����Դid,����Դ��id
		String sourceID, sourceColID;
		String sField = "";
		String fieldInfo = null;
		StringBuffer sReulst = new StringBuffer();

		// ѭ������ÿ��ÿ���ֶ�
		int groupArrayLen = groupValueImpl.getGroupAbleArray().length;
		for (int j = 0; j < groupArrayLen; j++) {
			groupAble = groupValueImpl.getGroupAbleArray()[j];
			// ���ϻ��ܲ����
			sLevel = groupAble.getLevel();
			// ѭ��������ֶ�ÿ��
			functionRefArray = groupAble.getFormula().getFunctionRefArray();
			int refArrayLen = functionRefArray.length;
			for (int k = 0; k < refArrayLen; k++) {
				functionRef = functionRefArray[k];

				// �ж��ڵ�ǰ��ǰ�Ƿ������ͬ�ֶ�
				if (CheckSameCol.isExistSameColGroup(functionRef, lstCells,
						curCol))
					continue;

				// �ж��Ƿ�ö�ٱ����ֶ��Ҳ����������벻��ͬ
				boolean isDiffer = this.isDifferEnumLvlWithPk(functionRef);
				if (!isDiffer) {// ö�ٲ������������ͬ
					continue;
				}

				sourceID = functionRef.getSourceID();
				sourceColID = functionRef.getSourceColID();
				sField = fieldEnameMap.get(functionRef.getRefID()).toString();

				// �ж�sLevel�ǲ���Ϊ�����ж��ǲ��ǵ�ǰ�ֶ�,��ö�ٲ������������ͬ
				if (!Common.isNullStr(sLevel)) {
					levArray = sLevel.split(",");
					boolean isEnumName = DefinePub.judgetEnumNameWithColID(
							querySource, sourceID, sourceColID);

					for (int m = 0; m < levArray.length; m++) {
						if (isEnumName) {
							fieldInfo = getEnumLvlPkDifferUpName(Integer
									.parseInt(levArray[m]), sourceID, "A",
									sourceColID, groupAble.getIsTotal());
						} else {
							fieldInfo = getEnumLvlPkDifferUpCode(Integer
									.parseInt(levArray[m]), sourceID, "A",
									sourceColID, groupAble.getIsTotal(), sField);

						}
						DefinePubOther.addComma(sReulst);
						sReulst.append(sField + "_" + levArray[m] + "J" + "="
								+ fieldInfo);
					}
				}
			}
		}
		return sReulst.toString();
	}

	/**
	 * ���ϻ��ܱ�����sql(����ö�������Ͳ���벻��ͬһ�ֶΣ�
	 * 
	 * @param iLev
	 * @param sourceID
	 * @param sourceAlais
	 * @param sourceColID
	 * @param isTotal
	 * @param sField
	 * @return
	 */
	private String getEnumLvlPkDifferUpCode(int iLev, String sourceID,
			String sourceAlais, String sourceColID, String isTotal,
			String sField) {
		String enumID = DefinePub.getEnumIDWithColID(querySource, sourceID,
				sourceColID);
		if (enumID == null)
			return null;
		IEnumSource enumSource = DefinePub.getEnumInfoWithID(querySource,
				enumID);
		// ���������ֶεõ���Ӧ��ö���ֶ�
		sourceColID = DefinePub.getEnumRefWithColID(querySource, sourceID,
				sourceColID).getRefEnumColCode();

		String levelInfo = enumSource.getLevelInfo();
		String levArray[] = levelInfo.split("-");
		int iLen = 0;
		for (int i = 0; i < iLev; i++) {
			iLen = iLen + Integer.parseInt(levArray[i]);
		}

		if (Common.estimate(isTotal)) {
			String sLeafField = null;
			try {
				sLeafField = getLeafField(enumSource);
			} catch (Exception e) {
				e.printStackTrace();
				new MessageBox("�õ�ĩ���ֶ����Ƴ���", e.getMessage(), MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();

			}

			String sSource = enumSource.getSource().toUpperCase();
			String levelCode = enumID + "." + enumSource.getLevelCode();
			return "(select " + enumID + "." + sourceColID + " from ("
					+ sSource + ") " + enumID + " where length(" + sourceAlais
					+ "." + sField + ")>=" + iLen + " and " + levelCode
					+ "=substr(" + sourceAlais + "." + sField + ",1," + iLen
					+ ") and " + enumID + ".set_year= getcuryear() and "
					+ sLeafField + "=0)";
		} else {
			return "(select subStr(" + sField + ",1," + iLen + ") from dual "
					+ "where length(" + sField + ")>=" + iLen + ")";
		}
	}

	/**
	 * ���ϻ���������sql(����ö�������Ͳ���벻��ͬһ�ֶΣ�
	 * 
	 * @param iLev
	 * @param sourceID
	 * @param sourceAlais
	 * @param sourceColID
	 * @param isTotal
	 * @return
	 */
	private String getEnumLvlPkDifferUpName(int iLev, String sourceID,
			String sourceAlais, String sourceColID, String isTotal) {

		// �õ�ö��
		String enumID = DefinePub.getEnumIDWithColID(querySource, sourceID,
				sourceColID);
		IEnumSource enumSource = DefinePub.getEnumInfoWithID(querySource,
				enumID);
		// �õ�����������
		String codeColID = DefinePub.getCodeWithName(querySource, sourceID,
				sourceColID);

		// ���������ֶεõ���Ӧ��ö���ֶ�
		sourceColID = DefinePub.getEnumRefWithColID(querySource, sourceID,
				sourceColID).getRefEnumColCode();

		String levelInfo = enumSource.getLevelInfo();
		String levArray[] = levelInfo.split("-");
		int iLen = 0;
		for (int i = 0; i < iLev; i++) {
			iLen = iLen + Integer.parseInt(levArray[i]);
		}

		String levelCode = enumID + "." + enumSource.getLevelCode();

		String sSource = enumSource.getSource().toUpperCase();

		String sSql = "(select " + enumID + "." + sourceColID + " from ("
				+ sSource + ") " + enumID + " where length(" + sourceAlais
				+ "." + codeColID + ")>=" + iLen + " and " + levelCode
				+ "=substr(" + sourceAlais + "." + codeColID + ",1," + iLen
				+ ") and " + enumID + ".set_year=getcuryear()";
		if (Common.estimate(isTotal)) {

			String sLeafField = null;
			try {
				sLeafField = getLeafField(enumSource);
			} catch (Exception e) {
				e.printStackTrace();
				new MessageBox("�õ�ĩ���ֶ����Ƴ���", e.getMessage(), MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();
			}

			sSql = sSql + " and " + sLeafField + "=0)";
		} else {
			sSql = sSql + ")";
		}
		return sSql;

	}

	/**
	 * ��֯���ո����ֶ�ֵ���(����ö�������Ͳ���벻��ͬһ�ֶΣ�
	 * 
	 * @param sUpdateSql
	 * @return
	 */
	private String buildUpdateLastSql(String sUpdateSql) {
		return "update {" + IDefineReport.TEMP_TABLE + "} A set " + sUpdateSql;
	}

}
