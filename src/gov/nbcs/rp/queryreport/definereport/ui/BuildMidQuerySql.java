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

	// 是否业务处室汇总，默认否
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

		// 得到数据源
		IDataSource[] dataSource = querySource.getDataSourceManager()
				.getDataSourceArray();

		// 得到数据源ID，存储组织的sql
		Map mapSql = getDataSourceID(dataSource);

		// 单元格值
		Object value;
		List lstShowAll_A = null;
		StringBuffer strUpdateSql = new StringBuffer();
		for (int i = 0; i < lstCells.size(); i++) {
			// 得到单元格值
			value = lstCells.get(i);
			if (value == null) {
				continue;
			}

			if (value instanceof MyGroupValueImpl) {// 分组列
				buildMidGroupSql(value, mapSql, i, dataSource);
				List lstShowAll = this.getAllShowSql(value);
				if (lstShowAll != null) {
					if (lstShowAll_A == null) {
						lstShowAll_A = new ArrayList();
					}
					lstShowAll_A.addAll(lstShowAll);
				}
				// 枚举主键和层次码不是同一字段更新语句
				String sUpdateSql = this.buildUpdateSqlWithLvlDiffPk(value, i);
				if (!Common.isNullStr(sUpdateSql)) {
					DefinePubOther.addComma(strUpdateSql);
					strUpdateSql.append(sUpdateSql);
				}
			} else if (value instanceof MyCalculateValueImpl) {// 计算列
				buildMidCalcSql(value, mapSql, i);
			}
		}

		List lstResult = buildMidLastSql(dataSource, mapSql);
		if (lstShowAll_A != null)
			lstResult.addAll(lstShowAll_A);
		// 更新临时表字段值语句（如处理支出项目类别）
		if (!Common.isNullStr(strUpdateSql.toString())) {
			String sUpdateSql = this
					.buildUpdateLastSql(strUpdateSql.toString());
			lstResult.add(DefinePubOther.getBuildSqlMap(sUpdateSql, "",
					IDefineReport.MID_QUERY));
		}

		return lstResult;
	}

	/**
	 * 组织计算列中间sql
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
			// 判断在当前列前是否存在相同字段
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
	 * 组织分组列中间sql
	 * 
	 * @throws Exception
	 * 
	 */
	private void buildMidGroupSql(Object value, Map mapSql, int curCol,
			IDataSource[] dataSource) {
		// 分组列
		MyGroupValueImpl groupValueImpl = (MyGroupValueImpl) value;

		IGroupAble groupAble;
		// 向上汇总层次码
		String sLevel;
		// 层次码
		String[] levArray;

		FunctionRef[] functionRefArray;
		FunctionRef functionRef;
		// 字段别名
		String sField = "";
		// 数据源
		String sourceID;
		// 数据列
		String sourceColID;

		String sSqlField;
		String sSqlValue;

		String fieldInfo;

		// 循环处理每列每个字段
		int groupArrayLen = groupValueImpl.getGroupAbleArray().length;
		for (int j = 0; j < groupArrayLen; j++) {
			groupAble = groupValueImpl.getGroupAbleArray()[j];
			if (groupAble.getIsMbSummary() == 1) {
				isMbSummary = true;
			}
			// 向上汇总层次码
			sLevel = groupAble.getLevel();
			// 循环处理个字段每段
			functionRefArray = groupAble.getFormula().getFunctionRefArray();
			int refArrayLen = functionRefArray.length;
			for (int k = 0; k < refArrayLen; k++) {
				functionRef = functionRefArray[k];

				// 判断在当前列前是否存在相同字段
				if (CheckSameCol.isExistSameColGroup(functionRef, lstCells,
						curCol))
					continue;

				sField = fieldEnameMap.get(functionRef.getRefID()).toString();
				// 循环处理每一个数据源
				for (int p = 0; p < dataSource.length; p++) {

					sourceID = dataSource[p].getSourceID();
					// 判断是不是同一数据源
					if (sourceID.equalsIgnoreCase(functionRef.getSourceID())) {
						sourceColID = functionRef.getSourceColID();
					} else {
						// 判断是否枚举字段
						if (DefinePub.judgetEnumWithColID(querySource,
								functionRef.getSourceID(), functionRef
										.getSourceColID(), false)) {// 是枚举字段
							// 根据列ID得到对应枚举RefEnumSource
							RefEnumSource refEnumSource = DefinePub
									.getEnumRefWithColID(querySource,
											functionRef.getSourceID(),
											functionRef.getSourceColID());
							sourceColID = DefinePub.getSourceColID(querySource,
									refEnumSource, sourceID);

						} else {// 不是枚举字段
							sourceColID = getRelaSourcColID(functionRef
									.getSourceID(), functionRef
									.getSourceColID(), sourceID);
						}
					}

					if (Common.isNullStr(sourceColID))
						continue;

					// 组织字段值，类、款、项时相应的名称字段要进行组织,科目名称Acct_name特殊处理
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

					// 判断是否枚举编码字段且层次码和主键码不相同
					boolean isDiffer = this.isDifferEnumLvlWithPk(functionRef);

					sSqlField = DefinePubOther.addComma(sSqlField);
					sSqlField = sSqlField + sField;
					sSqlValue = DefinePubOther.addComma(sSqlValue);
					// 处理层次码和主键码不相同向上汇总问题(如：支出项目类别)
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

					// 判断sLevel是不是为空且判断是不是当前字段,且枚举层次码与主键相同
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
	 * 得到显示所有信息sql列表
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private List getAllShowSql(Object value) throws Exception {
		List lstAllshow = null;
		// 分组列
		MyGroupValueImpl groupValueImpl = (MyGroupValueImpl) value;
		IGroupAble groupAble;

		// 循环处理每列每个字段
		int groupArrayLen = groupValueImpl.getGroupAbleArray().length;
		for (int j = 0; j < groupArrayLen; j++) {
			groupAble = groupValueImpl.getGroupAbleArray()[j];
			// 无数据仍显示
			if (groupAble.getIsCorss() == 1) {
				if (lstAllshow == null)
					lstAllshow = new ArrayList();
				lstAllshow.add(buildShowAllSql(groupAble));
			}
		}
		return lstAllshow;
	}

	/**
	 * 组织中间最终sql
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

			// 业务处室汇总sql处理
			buildIsMbSummarySql(sSqlField, sSqlValue, dataSource[i]
					.getSourceAlais());

			// 得到数据源过滤条件
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
	 * 业务处室汇总sql处理
	 * 
	 * @param sSqlField
	 *            字段名
	 * @param sSqlValue
	 *            字段值
	 * @param sourceAlais
	 *            数据源别名
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
	 * 无数据仍显示,特殊处理
	 * 
	 * @return
	 * @throws Exception
	 */
	private Map buildShowAllSql(IGroupAble groupAbleValue) throws Exception {

		String sourceID_A = groupAbleValue.getSourceID();
		String sourceColID_A = groupAbleValue.getSourceColID();

		// 单元格
		Object value;
		// 分组列值
		IGroupAble[] groupAbleArray;
		IGroupAble groupAble;
		FunctionRef functionRef;

		// 字段别名
		String sFieldEname = "";
		// 向上汇总层次码
		String sLevel;
		// 层次码
		String[] levArray;

		StringBuffer sSqlField = new StringBuffer();
		StringBuffer sSqlValue = new StringBuffer();

		// 数据源
		String sourceID;
		String sourceColID;
		// 数据源别名
		String sourceAlais = "A";

		String fieldInfo;

		// 得到枚举ID
		String enumID = DefinePub.getEnumIDWithColID(querySource, sourceID_A,
				sourceColID_A);
		// 得到枚举信息根据枚举ID
		IEnumSource enumSource = DefinePub.getEnumInfoWithID(querySource,
				enumID);
		// 编码列RefEnumSource信息
		RefEnumSource refEnumSource = DefinePub.getEnumRefWithColID(
				querySource, sourceID_A, sourceColID_A);
		// 得到名称列字段名根据编码列字段名
		String sourceColID_Name = DefinePub.getNameWithCode(querySource,
				sourceID_A, sourceColID_A);
		// 名称列RefEnumSource信息
		RefEnumSource refEnumSource_Name = DefinePub.getEnumRefWithColID(
				querySource, sourceID_A, sourceColID_Name);

		for (int i = 0; i < lstCells.size(); i++) {
			// 得到单元格值
			value = lstCells.get(i);
			if (value == null) {
				continue;
			}
			// 为每列设置ename值
			if (!(value instanceof MyGroupValueImpl)) {// 分组列
				continue;
			}

			groupAbleArray = ((MyGroupValueImpl) value).getGroupAbleArray();
			int groupLen = groupAbleArray.length;
			// 循环处理每列每个字段
			for (int j = 0; j < groupLen; j++) {
				groupAble = groupAbleArray[j];

				// 向上汇总层次码
				sLevel = groupAble.getLevel();
				// 循环处理个字段每段
				for (int k = 0; k < groupAbleArray[j].getFormula()
						.getFunctionRefArray().length; k++) {
					functionRef = groupAbleArray[j].getFormula()
							.getFunctionRefArray()[k];
					sourceID = functionRef.getSourceID();
					sourceColID = functionRef.getSourceColID();

					// 判断在当前列前是否存在相同字段
					if (CheckSameCol.isExistSameColGroup(functionRef, lstCells,
							i))
						continue;

					// 是否枚举名称
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

					// 判断是不是为空且判断是不是当前字段
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
			// 业务处室汇总sql处理
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
	 * 组织case when 条件字段内容
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
		// 1:元转换为万元，先四舍五入再汇总
		if (DefinePub.getMoneyOp(querySource) == 1) {
			sCaseSql = "Round((" + sCaseSql + ")/10000,2)";
		}
		return sCaseSql;
	}

	/**
	 * 得到计算列case when 条件
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
	 * 数据源过滤条件
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
		// 比较类型
		String compareType;
		// 联接类型
		String joinBefore;
		// 值
		String sValue;
		int iCount = parameter.length;
		for (int i = 0; i < iCount; i++) {
			chName = parameter[i].getChName();
			// 判断是否默认参数，如是不进行处理
			if (DataSourceSet.checkDefaultPara(chName)) {
				continue;
			}
			toSource = parameter[i].getToSourceArray();
			for (int j = 0; j < toSource.length; j++) {
				if (sourceID.equals(toSource[j].getSourceID())) {
					// 值
					sValue = parameter[i].getValue().toString();
					// 比较类型
					compareType = parameter[i].getCompareType();
					if (compareType.equalsIgnoreCase(IDefineReport.OPE_LIKE)
							|| compareType
									.equalsIgnoreCase(IDefineReport.OPE_NOTLIKE)) {
						sValue = sValue.substring(0, sValue.length() - 1) + "%"
								+ sValue.substring(sValue.length() - 1);
					}
					// 联接类型
					joinBefore = parameter[i].getJoinBefore();
					if (Common.isNullStr(sWhereSql)) {
						// 第一个过滤条件用and联接
						sWhereSql = sWhereSql + " and (";
					} else {
						sWhereSql = sWhereSql + " " + joinBefore + " ";
					}
					// 加左括号
					if (parameter[i] instanceof MySummaryParameterImpl
							&& ((MySummaryParameterImpl) parameter[i])
									.getLParenthesis() != null) {

						sWhereSql = sWhereSql
								+ ((MySummaryParameterImpl) parameter[i])
										.getLParenthesis();
					}
					sWhereSql = sWhereSql + toSource[j].getSourceColID() + " "
							+ compareType + " " + sValue;
					// 加右括号
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
	 * 编码的上级查询语句
	 * 
	 * @param iLev层次
	 * @param sourceID数据源ID
	 * @param sourceColID数据源字段ID
	 * @param isTotal是否末级汇总
	 * @param enumSource枚举
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
				new MessageBox("得到末级字段名称出错！", e.getMessage(), MessageBox.ERROR,
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
	 * 编码的上级查询语句
	 * 
	 * @param iLev层次
	 * @param sourceID数据源ID
	 * @param sourceColID数据源字段ID
	 * @param isTotal是否末级汇总
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
		// 根据数据字段得到对应的枚举字段
		sourceColID = DefinePub.getEnumRefWithColID(querySource, sourceID,
				sourceColID).getRefEnumColCode();

		return this.getFieldInfoWithCode(iLev, sourceID, sourceAlais,
				sourceColID, isTotal, enumSource, codeColID);
	}

	/**
	 * 名称的上级查询语句
	 * 
	 * @param iLev层次
	 * @param sourceID数据源ID
	 * @param sourceColID数据源字段ID
	 * @param isTotal是否末级汇总
	 * @return
	 */
	private String getFieldInfoWithName(int iLev, String sourceID,
			String sourceAlais, String sourceColID, String isTotal) {
		// 得到枚举
		String enumID = DefinePub.getEnumIDWithColID(querySource, sourceID,
				sourceColID);
		IEnumSource enumSource = DefinePub.getEnumInfoWithID(querySource,
				enumID);
		// 得到编码列名称
		String codeColID = DefinePub.getCodeWithName(querySource, sourceID,
				sourceColID);

		// 根据数据字段得到对应的枚举字段
		sourceColID = DefinePub.getEnumRefWithColID(querySource, sourceID,
				sourceColID).getRefEnumColCode();

		return this.getFieldInfoWithName(iLev, sourceID, sourceAlais,
				sourceColID, isTotal, enumSource, codeColID);

	}

	/**
	 * 名称的上级查询语句
	 * 
	 * @param iLev层次
	 * @param sourceID数据源ID
	 * @param sourceColID数据源字段ID
	 * @param isTotal是否末级汇总
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
				new MessageBox("得到末级字段名称出错！", e.getMessage(), MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();
			}

			sSql = sSql + " and " + sLeafField + "=0)";
		} else {
			sSql = sSql + ")";
		}
		return sSql;
	}

	/**
	 * 名称的上级查询语句
	 * 
	 * @param iLen编码长度
	 * @param sourceID数据源ID
	 * @param sourceColID数据源字段ID
	 * @return
	 */
	private String getFieldInfoWithName(int iLen, String sourceID,
			String sourceAlais, String sourceColID) {
		// 得到枚举
		String enumID = DefinePub.getEnumIDWithColID(querySource, sourceID,
				sourceColID);
		IEnumSource enumSource = DefinePub.getEnumInfoWithID(querySource,
				enumID);

		// 得到编码列名称
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
	 * 得到列信息
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
	 * 判断末级点字段名
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
	 * 判断末级点字段名
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
	 * 得到数据源ID
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
	 * 判断数据源是否被使用
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
			if (value instanceof MyGroupValueImpl) {// 分组列
				IGroupAble[] groupAbleArray = ((MyGroupValueImpl) value)
						.getGroupAbleArray();
				int groupArrayLen = groupAbleArray.length;
				for (int j = 0; j < groupArrayLen; j++) {
					if (sourceID.equalsIgnoreCase(groupAbleArray[j]
							.getSourceID())) {
						return true;
					}
				}
			} else if (value instanceof MyCalculateValueImpl) {// 计算列
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
	 * 得到关联关系字段
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
	 * 得到业务处室编码sql
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
	 * 组织字段值，类、款、项时相应的名称字段要进行组织
	 * 
	 * @param list列列表
	 * @param sourceID数据源ID
	 * @param sourceColID字段ID
	 * @param sourceAlais数据源别名
	 * @return
	 */
	private String getFieldValue(List list, String sourceID,
			String sourceColID, String sourceAlais) {
		// 判断是否是名称
		boolean isEnumName = DefinePub.judgetEnumNameWithColID(querySource,
				sourceID, sourceColID);
		if (!isEnumName) {
			return sourceColID;
		}

		// 得到枚举ID
		String enumIdCode = DefinePub.getEnumIDWithColID(querySource, sourceID,
				sourceColID);

		Object value;
		IGroupAble[] groupAble;

		String enumID;
		boolean isEnumPri;
		String content;
		int index;
		String[] contentArray;

		// 存放编码最大数
		int max = 0;
		int maxTmp;
		// 得到相应的代码列，组织名称的字段值，主要处理类、款、项问题
		int size = list.size();
		for (int i = 0; i < size; i++) {
			value = list.get(i);
			if (value == null)
				continue;

			// 判断是否是分组列
			if (value instanceof MyGroupValueImpl) {
				groupAble = ((MyGroupValueImpl) value).getGroupAbleArray();
				for (int j = 0; j < groupAble.length; j++) {
					// 得到枚举ID
					enumID = DefinePub.getEnumIDWithColID(querySource,
							groupAble[j].getSourceID(), groupAble[j]
									.getSourceColID());
					// 判断是否对应同一枚举
					if (!enumIdCode.equals(enumID))
						continue;

					// 判断是不是枚举主键
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
	 * 得到对应的枚举，层次码和主键是否相同，相同返回false,不相同返回true
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
	 * 枚举主键和层次码不一致时（如支出项目类别），查询将主键改为层次
	 * 
	 * @param sourceAlais数据源别名
	 * @param sourceColID数据列
	 * @param enumSource枚举信息
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
	 * 向上汇总(处理枚举主键和层次码不是同一字段）
	 * 
	 * @param value
	 * @param curCol
	 * @return
	 */
	private String buildUpdateSqlWithLvlDiffPk(Object value, int curCol) {
		// 分组列
		MyGroupValueImpl groupValueImpl = (MyGroupValueImpl) value;

		IGroupAble groupAble;
		// 向上汇总层次码
		String sLevel;
		// 层次码
		String[] levArray;

		FunctionRef[] functionRefArray;
		FunctionRef functionRef;
		// 数据源id,数据源列id
		String sourceID, sourceColID;
		String sField = "";
		String fieldInfo = null;
		StringBuffer sReulst = new StringBuffer();

		// 循环处理每列每个字段
		int groupArrayLen = groupValueImpl.getGroupAbleArray().length;
		for (int j = 0; j < groupArrayLen; j++) {
			groupAble = groupValueImpl.getGroupAbleArray()[j];
			// 向上汇总层次码
			sLevel = groupAble.getLevel();
			// 循环处理个字段每段
			functionRefArray = groupAble.getFormula().getFunctionRefArray();
			int refArrayLen = functionRefArray.length;
			for (int k = 0; k < refArrayLen; k++) {
				functionRef = functionRefArray[k];

				// 判断在当前列前是否存在相同字段
				if (CheckSameCol.isExistSameColGroup(functionRef, lstCells,
						curCol))
					continue;

				// 判断是否枚举编码字段且层次码和主键码不相同
				boolean isDiffer = this.isDifferEnumLvlWithPk(functionRef);
				if (!isDiffer) {// 枚举层次码与主键相同
					continue;
				}

				sourceID = functionRef.getSourceID();
				sourceColID = functionRef.getSourceColID();
				sField = fieldEnameMap.get(functionRef.getRefID()).toString();

				// 判断sLevel是不是为空且判断是不是当前字段,且枚举层次码与主键相同
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
	 * 向上汇总编码列sql(处理枚举主键和层次码不是同一字段）
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
		// 根据数据字段得到对应的枚举字段
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
				new MessageBox("得到末级字段名称出错！", e.getMessage(), MessageBox.ERROR,
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
	 * 向上汇总名称列sql(处理枚举主键和层次码不是同一字段）
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

		// 得到枚举
		String enumID = DefinePub.getEnumIDWithColID(querySource, sourceID,
				sourceColID);
		IEnumSource enumSource = DefinePub.getEnumInfoWithID(querySource,
				enumID);
		// 得到编码列名称
		String codeColID = DefinePub.getCodeWithName(querySource, sourceID,
				sourceColID);

		// 根据数据字段得到对应的枚举字段
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
				new MessageBox("得到末级字段名称出错！", e.getMessage(), MessageBox.ERROR,
						MessageBox.BUTTON_OK).show();
			}

			sSql = sSql + " and " + sLeafField + "=0)";
		} else {
			sSql = sSql + ")";
		}
		return sSql;

	}

	/**
	 * 组织最终更新字段值语句(处理枚举主键和层次码不是同一字段）
	 * 
	 * @param sUpdateSql
	 * @return
	 */
	private String buildUpdateLastSql(String sUpdateSql) {
		return "update {" + IDefineReport.TEMP_TABLE + "} A set " + sUpdateSql;
	}

}
