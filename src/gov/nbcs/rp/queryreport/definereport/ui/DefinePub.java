/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.security.action.GetPropertyAction;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.queryreport.definereport.ibs.ICustomSummaryReportBasicAttr;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.reportcy.common.gui.util.CreateGroupReport;
import com.foundercy.pf.reportcy.common.util.DateEx;
import com.foundercy.pf.reportcy.summary.constants.RowConstants;
import com.foundercy.pf.reportcy.summary.iface.ISourceCol;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.iface.cell.IStatisticCaliber;
import com.foundercy.pf.reportcy.summary.iface.enumer.IEnumSource;
import com.foundercy.pf.reportcy.summary.iface.enumer.IEnumSourceManager;
import com.foundercy.pf.reportcy.summary.iface.source.IDataSource;
import com.foundercy.pf.reportcy.summary.object.DefaultDictionaryImpl;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;
import com.foundercy.pf.reportcy.summary.object.base.SummaryReportBasicAttr;
import com.foundercy.pf.reportcy.summary.object.enumer.SummaryEnumSourceManagerImpl;
import com.foundercy.pf.reportcy.summary.object.source.RefEnumSource;
import com.foundercy.pf.reportcy.summary.object.source.SummaryDataSourceManagerImpl;
import com.foundercy.pf.reportcy.summary.ui.core.SummaryReportPane;
import com.fr.cell.CellSelection;
import com.fr.report.GroupReport;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
 * </p>
 * <p>
 * CreateData 2011-3-25
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class DefinePub {

	private static int uuidIndex = 0;

	/**
	 * 得到选中的数据源
	 * 
	 * @param querySource
	 * @return 选中的数据源
	 */
	public static List getDataSource(ReportQuerySource querySource) {
		// 得到选中的数据源
		SummaryDataSourceManagerImpl summaryDataSourceManagerImpl = (SummaryDataSourceManagerImpl) querySource
				.getDataSourceManager();
		if (summaryDataSourceManagerImpl == null) {
			return null;
		}
		IDataSource[] summaryDataSourceImpl = summaryDataSourceManagerImpl
				.getDataSourceArray();

		List lstDataSource = new ArrayList();
		Map dataSourceMap = null;
		int iCount = summaryDataSourceImpl.length;
		for (int i = 0; i < iCount; i++) {
			dataSourceMap = new HashMap();
			dataSourceMap.put(IDefineReport.DATASOURCE_NAME,
					summaryDataSourceImpl[i].getDataSourceName());
			dataSourceMap.put(IDefineReport.SOURCE_ID, summaryDataSourceImpl[i]
					.getSourceID());
			lstDataSource.add(dataSourceMap);

		}
		return lstDataSource;
	}

	/**
	 * 根据数据源ID得到数据源信息
	 * 
	 * @param summaryReport
	 * @param sSourceID
	 *            数据源ID
	 * @return 数据源名称
	 */
	public static IDataSource getDataSourceWithID(
			ReportQuerySource querySource, String sSourceID) {
		if (sSourceID == null)
			return null;
		// 得到选中的数据源
		SummaryDataSourceManagerImpl summaryDataSourceManagerImpl = (SummaryDataSourceManagerImpl) querySource
				.getDataSourceManager();
		if (summaryDataSourceManagerImpl == null) {
			return null;
		}
		IDataSource[] summaryDataSourceImpl = summaryDataSourceManagerImpl
				.getDataSourceArray();
		int iCount = summaryDataSourceImpl.length;
		String sCurSourceID = null;
		for (int i = 0; i < iCount; i++) {
			sCurSourceID = summaryDataSourceImpl[i].getSourceID();
			if (!sSourceID.equals(sCurSourceID)) {
				continue;
			}
			return summaryDataSourceImpl[i];
		}
		return null;
	}

	/**
	 * 根据数据源ID得到数据源名称
	 * 
	 * @param summaryReport
	 * @param sSourceID
	 *            数据源ID
	 * @return 数据源名称
	 */
	public static String getDataSourceNameWithID(ReportQuerySource querySource,
			String sSourceID) {
		IDataSource dataSource = getDataSourceWithID(querySource, sSourceID);
		if (dataSource != null)
			return dataSource.getDataSourceName();
		return null;
	}

	/**
	 * 根据数据源列ID得到数据源列中文名称
	 * 
	 * @param querySource
	 * @param sSourceID
	 *            数据源ID
	 * @param sColID
	 *            列ID
	 * @return 数据源列中文名称
	 */
	public static String getDataSourceColNameWithID(
			ReportQuerySource querySource, String sSourceID, String sColID) {
		if (sSourceID == null || sColID == null)
			return null;
		// 得到选中的数据源
		SummaryDataSourceManagerImpl summaryDataSourceManagerImpl = (SummaryDataSourceManagerImpl) querySource
				.getDataSourceManager();
		if (summaryDataSourceManagerImpl == null) {
			return null;
		}
		IDataSource[] summaryDataSourceImpl = summaryDataSourceManagerImpl
				.getDataSourceArray();
		int iCount = summaryDataSourceImpl.length;
		String sCurSourceID = null;
		// 数据字典
		DefaultDictionaryImpl defaultDictionaryImpl = null;
		for (int i = 0; i < iCount; i++) {
			sCurSourceID = summaryDataSourceImpl[i].getSourceID();
			// 判断是否是当前数据源
			if (!sSourceID.equals(sCurSourceID)) {
				continue;
			}
			defaultDictionaryImpl = (DefaultDictionaryImpl) summaryDataSourceImpl[i]
					.getDictionary();
			return defaultDictionaryImpl.getDisplayValue(sColID);
		}
		return null;
	}

	/**
	 * 根据枚举源ID得到枚举源name
	 * 
	 * @param summaryReport
	 * 
	 * @param sEnumID枚举源ID
	 * @return 枚举源name
	 */
	private static String getEnumNameWithID(ReportQuerySource querySource,
			String sEnumID) {
		if (sEnumID == null)
			return null;

		// 枚举源
		SummaryEnumSourceManagerImpl summaryEnumSourceManagerImpl = (SummaryEnumSourceManagerImpl) querySource
				.getEnumSourceManager();
		// 枚举源数组
		IEnumSource[] summaryEnumSourceImpl = null;

		// 枚举源数组
		String sEnumIDCur;
		String sEnumName;
		if (summaryEnumSourceManagerImpl != null) {
			summaryEnumSourceImpl = summaryEnumSourceManagerImpl
					.getEnumSourceArray();
			for (int i = 0; i < summaryEnumSourceImpl.length; i++) {
				sEnumIDCur = summaryEnumSourceImpl[i].getEnumID();
				if (sEnumID.equals(sEnumIDCur)) {
					sEnumName = summaryEnumSourceImpl[i].getEnumName();
					return sEnumName;
				}
			}
		}

		return null;
	}

	/**
	 * 根据数据源定义的字段类型的值转换成报表对象字段类型相对应的值
	 * 
	 * @param sFieldTypeCh
	 *            数据源列字段类型
	 * @return
	 */
	public static String getFieldTypeWithCh(String sFieldTypeCh) {
		if (sFieldTypeCh == null)
			return null;

		if (sFieldTypeCh.equals(IDefineReport.INT_TYPE)
				|| sFieldTypeCh.equals(IDefineReport.INTT_TYPE)
				|| sFieldTypeCh.equals(IDefineReport.CURRENCY_TYPE)
				|| sFieldTypeCh.equals(IDefineReport.FLOAT_TYPE)) {
			return IDefineReport.NUMBER_VAL;
		}
		return IDefineReport.CHAR_Val;
	}

	/**
	 * 判断字段是整型还是浮点型
	 * 
	 * @param sFieldTypeCh
	 * @return
	 */
	public static int checkFieldFloat(String sFieldTypeCh) {
		if (sFieldTypeCh == null)
			return 1;

		if (sFieldTypeCh.equals(IDefineReport.CURRENCY_TYPE)
				|| sFieldTypeCh.equals(IDefineReport.FLOAT_TYPE)) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 显示数据源与枚举的对应关系
	 * 
	 * @param summaryReport
	 * @param refTypePri
	 *            是否只显示主键对应关系
	 * @return 枚举列表
	 */
	public static List showEnumWhereSetInfo(ReportQuerySource querySource,
			boolean refTypePri, List lstSourceID) {
		if (lstSourceID == null)
			return null;

		// 得到数据源信息
		SummaryDataSourceManagerImpl summaryDataSourceManagerImpl = (SummaryDataSourceManagerImpl) querySource
				.getDataSourceManager();

		// 得到数据源数据数组
		IDataSource[] summaryDataSourceImpl = null;
		if (summaryDataSourceManagerImpl != null) {
			summaryDataSourceImpl = summaryDataSourceManagerImpl
					.getDataSourceArray();
		}
		if (summaryDataSourceImpl == null)
			return null;
		// 数据源对应的枚举关系
		ISourceCol[] summarySourceCol;
		int iCount = summaryDataSourceImpl.length;
		int iColCount = 0;
		// 数据源ID
		String sSourceID = null;
		// 数据源别名
		String sSourceAlais = null;
		// 列ID
		String sColID = null;
		// 列名称
		String sFieldFname = null;
		// 数据源名称
		String sDataSourceName = null;
		// 联接方式，左联接还是右联接
		String sJoinType = null;
		// 枚举源ID
		String sEnumID = null;
		// 枚举源名称
		String sEnumName = null;
		// 定义数据源与枚举源关系列表
		ArrayList lstEnumRela = new ArrayList();
		Map mapEnumRela = null;
		for (int i = 0; i < iCount; i++) {
			// 数据源ID
			sSourceID = summaryDataSourceImpl[i].getSourceID();
			if (!lstSourceID.contains(sSourceID))
				continue;

			sSourceAlais = summaryDataSourceImpl[i].getSourceAlais();

			// 数据源名称
			sDataSourceName = summaryDataSourceImpl[i].getDataSourceName();
			// 得到数据源对应的枚举关系
			summarySourceCol = summaryDataSourceImpl[i].getColArray();
			iColCount = summarySourceCol.length;
			for (int j = 0; j < iColCount; j++) {
				// 列ID
				sColID = summarySourceCol[j].getSourceColID();
				// 列名称
				sFieldFname = getDataSourceColNameWithID(querySource,
						sSourceID, sColID);

				// 0:主键,1：非主键联接, 非主键联接不需要设置联接关系
				if (refTypePri) {
					if (summarySourceCol[j].getRefEnumSource().getRefType() == 1) {
						continue;
					}
				}
				sJoinType = summarySourceCol[j].getRefEnumSource()
						.getJoinType();
				// 将英文名称转换成中文名称
				if (IDefineReport.LEFT_JOIN.equals(sJoinType)) {
					sJoinType = IDefineReport.LEFT_JOIN_NAME;
				} else if (IDefineReport.RIGHT_JOIN.equals(sJoinType)) {
					sJoinType = IDefineReport.RIGHT_JOIN_NAME;
				}
				// 得到枚举ID
				sEnumID = summarySourceCol[j].getRefEnumSource().getEnumID();
				// 枚举源名称
				sEnumName = getEnumNameWithID(querySource, sEnumID);
				mapEnumRela = new HashMap();
				mapEnumRela.put(IDefineReport.SOURCE_ID, sSourceID);
				mapEnumRela.put(IDefineReport.SOURCE_ALAIS, sSourceAlais);
				mapEnumRela.put(IDefineReport.DATASOURCE_NAME, sDataSourceName);
				mapEnumRela.put(IDefineReport.COL_ID, sColID);
				mapEnumRela.put(IDefineReport.FIELD_FNAME, sFieldFname);
				mapEnumRela.put(IDefineReport.JOIN_TYPE, sJoinType);
				mapEnumRela.put(IDefineReport.ENUM_ID, sEnumID);
				mapEnumRela.put(IDefineReport.ENUM_NAME, sEnumName);
				lstEnumRela.add(mapEnumRela);
			}
		}
		return lstEnumRela;
	}

	public static String getRandomUUID() {
		// 加这个步进制的目的是控制同一时间内生成的格子不重复.
		uuidIndex++;
		String tmp = "ID_";
		String data = DateEx.getDateStringByFormat("yyMMddHHmmss");
		// int random = (int) (Math.random() * 10000);
		int random = uuidIndex;
		return new StringBuffer(tmp).append(data).append("_").append(random)
				.toString();
		// String randomID = UUID.randomUUID().toString();
		// randomID = randomID.substring(1);
		// randomID = randomID.substring(0, randomID.length() - 1);
		// randomID = StringEx.replace(randomID, "-", "");
		// return randomID;
	}

	/**
	 * 得到最大排序序号
	 * 
	 * @return 排序序号
	 */
	public static String getOrderIndex(GroupReport groupReport) {
		// 得到操作区行头区的开始行
		int rowIndex[] = CreateGroupReport.getRowIndexs(
				RowConstants.UIAREA_OPERATION, groupReport);
		int row = rowIndex[rowIndex.length - 1];

		// 得到总列数
		int colCount = groupReport.getColumnCount();
		int arrayNum = 0;
		Object object = null;
		IGroupAble[] iGroupAbleArray = null;
		int maxOrderIndex = 0;
		int curOrderIndex = 0;
		// 循环处理，得到排序值
		for (int i = 0; i < colCount; i++) {
			// 得到单元格值
			object = groupReport.getCellValue(i, row);
			// 判断是不是分组列,如是分组列判断是否排序，得到最大排序号
			if (object instanceof MyGroupValueImpl) {
				iGroupAbleArray = ((MyGroupValueImpl) object)
						.getGroupAbleArray();
				arrayNum = iGroupAbleArray.length;
				// 循环取得单元格数组中排序顺序号
				for (int j = 0; j < arrayNum; j++) {
					// 判断是否是数字
					if (!Common.isInteger(iGroupAbleArray[j].getOrderIndex()))
						continue;
					// 得到当前排序序号
					curOrderIndex = Integer.parseInt(iGroupAbleArray[j]
							.getOrderIndex());
					// 与当前最大排序号进行比较
					if (maxOrderIndex < curOrderIndex) {
						maxOrderIndex = curOrderIndex;
					}
				}
			}
			// 判断是不是计算列,如是计算列判断是否排序，得到最大排序号
			if (object instanceof MyCalculateValueImpl) {
				// 判断是否是数字
				if (((MyCalculateValueImpl) object).getOrderIndex() == null)
					continue;
				if (!Common.isInteger(((MyCalculateValueImpl) object)
						.getOrderIndex()))
					continue;
				curOrderIndex = Integer
						.parseInt(((MyCalculateValueImpl) object)
								.getOrderIndex());
				// 与当前最大排序号进行比较
				if (maxOrderIndex < curOrderIndex) {
					maxOrderIndex = curOrderIndex;
				}
			}
		}
		// 返回当前最大排序号加1
		return String.valueOf(maxOrderIndex + 1);
	}

	/**
	 * 得到临时表文件夹
	 * 
	 * @return
	 */
	public static String getTempDir() {

		GetPropertyAction getpropertyaction = new GetPropertyAction(
				"java.io.tmpdir");
		String tmpdir = (String) AccessController
				.doPrivileged(getpropertyaction);
		return tmpdir;
	}

	/**
	 * 判断是否枚举值根据数据源列ID
	 * 
	 * @param querySource
	 * @param SourceID数据源ID
	 * @param sourceColID数据源列ID
	 * @param bRefTypePri是否判断是不是主键
	 * @return 是枚举返回true,否则返回false
	 */
	public static boolean judgetEnumWithColID(ReportQuerySource querySource,
			String sourceID, String sourceColID, boolean bRefTypePri) {

		IDataSource dataSource[] = querySource.getDataSourceManager()
				.getDataSourceArray();
		ISourceCol[] sourceCol;
		int iCount = dataSource.length;
		for (int i = 0; i < iCount; i++) {
			if (!sourceID.equalsIgnoreCase(dataSource[i].getSourceID())) {
				continue;
			}
			sourceCol = dataSource[i].getColArray();
			int iCountJ = sourceCol.length;
			for (int j = 0; j < iCountJ; j++) {
				if (sourceColID.equalsIgnoreCase(sourceCol[j].getSourceColID())) {
					if (bRefTypePri) {
						// 0:主键，1：非主键
						if (sourceCol[j].getRefEnumSource().getRefType() == 0)
							return true;
						else
							return false;
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否枚举名称列根据数据源列ID
	 * 
	 * @param querySource
	 * @param SourceID数据源ID
	 * @param sourceColID数据源列ID
	 * @return 是返回true,否则返回false
	 */
	public static boolean judgetEnumNameWithColID(
			ReportQuerySource querySource, String sourceID, String sourceColID) {

		IDataSource dataSource[] = querySource.getDataSourceManager()
				.getDataSourceArray();
		ISourceCol[] sourceCol;
		int iCount = dataSource.length;
		for (int i = 0; i < iCount; i++) {
			if (!sourceID.equalsIgnoreCase(dataSource[i].getSourceID())) {
				continue;
			}
			sourceCol = dataSource[i].getColArray();
			int iCountJ = sourceCol.length;
			for (int j = 0; j < iCountJ; j++) {
				if (sourceColID.equalsIgnoreCase(sourceCol[j].getSourceColID())) {
					// 0:主键，1：非主键
					if (sourceCol[j].getRefEnumSource().getRefType() == 1)
						return true;

				}
			}
		}
		return false;
	}

	/**
	 * 得到编码根据编码名称
	 * 
	 * @param querySource
	 * @param SourceID数据源ID
	 * @param sourceColID数据源列ID
	 * @return 编码
	 */
	public static String getCodeWithName(ReportQuerySource querySource,
			String sourceID, String sourceColID) {

		IDataSource dataSource[] = querySource.getDataSourceManager()
				.getDataSourceArray();
		ISourceCol[] sourceCol;
		int iCount = dataSource.length;
		String sEnumID;
		for (int i = 0; i < iCount; i++) {
			if (!sourceID.equalsIgnoreCase(dataSource[i].getSourceID())) {
				continue;
			}
			sourceCol = dataSource[i].getColArray();
			int iCountJ = sourceCol.length;
			for (int j = 0; j < iCountJ; j++) {
				if (sourceColID.equalsIgnoreCase(sourceCol[j].getSourceColID())) {
					if (sourceCol[j].getRefEnumSource().getRefType() == 1) {
						sEnumID = sourceCol[j].getRefEnumSource().getEnumID();
						for (int k = 0; k < iCountJ; k++) {
							if (sEnumID.equals(sourceCol[k].getRefEnumSource()
									.getEnumID())
									&& sourceCol[k].getRefEnumSource()
											.getRefType() == 0) {
								return sourceCol[k].getSourceColID();
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 得到编码根据编码名称
	 * 
	 * @param querySource
	 * @param SourceID数据源ID
	 * @param sourceColID数据源列ID
	 * @return 编码
	 */
	public static String getNameWithCode(ReportQuerySource querySource,
			String sourceID, String sourceColID) {

		IDataSource dataSource[] = querySource.getDataSourceManager()
				.getDataSourceArray();
		ISourceCol[] sourceCol;
		int iCount = dataSource.length;
		String sEnumID;
		for (int i = 0; i < iCount; i++) {
			if (!sourceID.equalsIgnoreCase(dataSource[i].getSourceID())) {
				continue;
			}
			sourceCol = dataSource[i].getColArray();
			int iCountJ = sourceCol.length;
			for (int j = 0; j < iCountJ; j++) {
				if (sourceColID.equalsIgnoreCase(sourceCol[j].getSourceColID())) {
					if (sourceCol[j].getRefEnumSource().getRefType() == 0) {
						sEnumID = sourceCol[j].getRefEnumSource().getEnumID();
						for (int k = 0; k < iCountJ; k++) {
							if (sEnumID.equals(sourceCol[k].getRefEnumSource()
									.getEnumID())
									&& sourceCol[k].getRefEnumSource()
											.getRefType() == 1) {
								return sourceCol[k].getSourceColID();
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 根据列ID得到对应枚举ID
	 * 
	 * @param querySource
	 * @param sourceID数据源ID
	 * @param sourceColID数据源列ID
	 * @return 枚举ID
	 */
	public static String getEnumIDWithColID(ReportQuerySource querySource,
			String sourceID, String sourceColID) {
		IDataSource dataSource[] = querySource.getDataSourceManager()
				.getDataSourceArray();
		ISourceCol[] sourceCol;
		int iCount = dataSource.length;
		for (int i = 0; i < iCount; i++) {
			if (!sourceID.equalsIgnoreCase(dataSource[i].getSourceID())) {
				continue;
			}
			sourceCol = dataSource[i].getColArray();
			int iCountJ = sourceCol.length;
			for (int j = 0; j < iCountJ; j++) {
				if (sourceColID.equalsIgnoreCase(sourceCol[j].getSourceColID())) {
					return sourceCol[j].getRefEnumSource().getEnumID();
				}
			}
		}
		return null;
	}

	/**
	 * 根据列ID得到对应枚举信息
	 * 
	 * @param querySource
	 * @param sourceID数据源ID
	 * @param sourceColID数据源列ID
	 * @return 枚举ID
	 * @return
	 */
	public static RefEnumSource getEnumRefWithColID(
			ReportQuerySource querySource, String sourceID, String sourceColID) {
		IDataSource dataSource[] = querySource.getDataSourceManager()
				.getDataSourceArray();
		ISourceCol[] sourceCol;
		int iCount = dataSource.length;
		for (int i = 0; i < iCount; i++) {
			if (!sourceID.equalsIgnoreCase(dataSource[i].getSourceID())) {
				continue;
			}
			sourceCol = dataSource[i].getColArray();
			int iCountJ = sourceCol.length;
			for (int j = 0; j < iCountJ; j++) {
				if (sourceColID.equalsIgnoreCase(sourceCol[j].getSourceColID())) {
					return sourceCol[j].getRefEnumSource();
				}
			}
		}
		return null;
	}

	/**
	 * 根据数据源枚举id、编码，得到sourceID中相同枚举的数据的字段名称，此枚举不存在返回null
	 * 
	 * @param querySource
	 * @param refEnumSource枚举
	 * @param sourceID数据源ID
	 * @return 数据的字段名称
	 */
	public static String getSourceColID(ReportQuerySource querySource,
			RefEnumSource refEnumSource, String sourceID) {
		IDataSource dataSource[] = querySource.getDataSourceManager()
				.getDataSourceArray();
		ISourceCol[] sourceCol;
		String enumID;
		String enumColCode;

		String enumIDComp = refEnumSource.getEnumID();
		String enumColCodeComp = refEnumSource.getRefEnumColCode();
		int iCount = dataSource.length;
		for (int i = 0; i < iCount; i++) {
			if (!sourceID.equalsIgnoreCase(dataSource[i].getSourceID())) {
				continue;
			}
			sourceCol = dataSource[i].getColArray();
			int iCountJ = sourceCol.length;
			for (int j = 0; j < iCountJ; j++) {
				enumID = sourceCol[j].getRefEnumSource().getEnumID();
				enumColCode = sourceCol[j].getRefEnumSource()
						.getRefEnumColCode();
				if (enumID.equals(enumIDComp)
						&& enumColCode.equals(enumColCodeComp)) {
					return sourceCol[j].getSourceColID();
				}
			}
		}
		return null;

	}

	/**
	 * 得到枚举信息根据枚举ID
	 * 
	 * @param querySource
	 * @param enumID枚举ID
	 * @return 枚举信息
	 */
	public static IEnumSource getEnumInfoWithID(ReportQuerySource querySource,
			String enumID) {
		IEnumSourceManager enumSourceManager = querySource
				.getEnumSourceManager();
		IEnumSource[] enumSource = enumSourceManager.getEnumSourceArray();
		int iCount = enumSource.length;
		if (iCount == 0)
			return null;
		for (int i = 0; i < iCount; i++) {
			if (enumID.equals(enumSource[i].getEnumID())) {
				return enumSource[i];
			}
		}
		return null;
	}

	/**
	 * 组织条件区显示信息
	 * 
	 * @param querySource
	 * @param stacist
	 * @return
	 */
	public static String getFilter(ReportQuerySource querySource,
			IStatisticCaliber[] stacist) {
		if (stacist == null) {
			return "";
		}
		StringBuffer sFilter = new StringBuffer();
		for (int i = 0; i < stacist.length; i++) {
			if (!isPACaliber(stacist[i])) {
				sFilter.append(" "
						+ getJoinBeforeStr(stacist[i].getJoinBefore()));
				sFilter.append(" {"
						+ getDataSourceNameWithID(querySource, stacist[i]
								.getSourceID())
						+ "."
						+ getDataSourceColNameWithID(querySource, stacist[i]
								.getSourceID(), stacist[i].getSourceColID())
						+ "} ");
				sFilter.append(getCompareType(stacist[i].getCompareType()));
				sFilter.append(" " + stacist[i].getValue());
			} else {
				sFilter.append(stacist[i].toString());
			}
		}
		return sFilter.toString();
	}

	/**
	 * 联接类型
	 * 
	 * @param joinBeforeValue
	 * @return
	 */
	private static String getJoinBeforeStr(String joinBeforeValue) {
		return JoinBefore.getJoinBeforeName(joinBeforeValue);
	}

	/**
	 * 比较类型
	 * 
	 * @param compareTypeValue
	 * @return
	 */
	private static String getCompareType(String compareTypeValue) {
		return CompareType.getComparTypeName(compareTypeValue);
	}

	/**
	 * 得到操作区行号
	 * 
	 * @param groupReport
	 * @return
	 */
	public static int getOpeRow(GroupReport groupReport) {
		// 得到表头行区域
		int indexs[] = CreateGroupReport.getRowIndexs(
				RowConstants.UIAREA_OPERATION, groupReport);
		return indexs[0];
	}

	/**
	 * 得到操作区行号
	 * 
	 * @param groupReport
	 * @return
	 */
	public static int[] getHeaderRow(GroupReport groupReport) {
		// 得到表头行区域
		return CreateGroupReport.getRowIndexs(RowConstants.UIAREA_HEADER,
				groupReport);
	}

	/**
	 * 检查是否是单元格相加
	 * 
	 * @param calculateValueImpl
	 * @return
	 */
	public static boolean checkIsCellFormula(
			MyCalculateValueImpl calculateValueImpl) {
		if (calculateValueImpl.getMeasureArray() == null)
			return false;
		if (calculateValueImpl.getMeasureArray().length == 0)
			return true;
		else
			return false;
	}

	/**
	 * 判断是否是计算列
	 * 
	 * @param cell
	 * @return
	 */
	public static boolean isCalcCol(SummaryReportPane reportPane, int col,
			int row) {
		CellSelection cell = reportPane.getCellSelection();
		if (cell == null)
			return false;
		Object value = reportPane.getReport().getCellValue(col, row);
		if (value == null)
			return false;
		if (value instanceof MyCalculateValueImpl) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是分组列
	 * 
	 * @param cell
	 * @return
	 */
	public static boolean isGroupCol(SummaryReportPane reportPane, int col,
			int row) {
		CellSelection cell = reportPane.getCellSelection();
		if (cell == null)
			return false;
		Object value = reportPane.getReport().getCellValue(col, row);
		if (value == null)
			return false;
		if (value instanceof MyGroupValueImpl) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是字符值
	 */
	public static boolean checkCharVal(String sFieldType) {
		if (IDefineReport.CHAR_Val.equals(sFieldType))
			return true;
		return false;

	}

	/**
	 * 四舍五入信息
	 * 
	 * @param querySource
	 * @return
	 */
	public static int getMoneyOp(ReportQuerySource querySource) {
		return ((SummaryReportBasicAttr) querySource.getReportBasicAttr())
				.getMoneyOp();
	}

	public static String getIsShowTotalRow(ReportQuerySource querySource) {
		if (querySource.getReportBasicAttr() instanceof ICustomSummaryReportBasicAttr) {
			return ((ICustomSummaryReportBasicAttr) querySource
					.getReportBasicAttr()).getIsShowTotalRow();
		}
		return "1";
	}

	/**
	 * 判断是否是分栏设置的条件
	 * 
	 * @param caliber
	 * @return
	 */
	public static boolean isPACaliber(IStatisticCaliber caliber) {
		if (caliber.getCaliberID().indexOf(IDefineReport.PA_) != -1)
			return true;
		else
			return false;
	}

}
