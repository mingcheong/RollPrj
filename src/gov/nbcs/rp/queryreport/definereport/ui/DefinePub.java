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
 * Copyright: Copyright (c) 2011 �㽭�������޹�˾
 * </p>
 * <p>
 * Company: �㽭�������޹�˾
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
	 * �õ�ѡ�е�����Դ
	 * 
	 * @param querySource
	 * @return ѡ�е�����Դ
	 */
	public static List getDataSource(ReportQuerySource querySource) {
		// �õ�ѡ�е�����Դ
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
	 * ��������ԴID�õ�����Դ��Ϣ
	 * 
	 * @param summaryReport
	 * @param sSourceID
	 *            ����ԴID
	 * @return ����Դ����
	 */
	public static IDataSource getDataSourceWithID(
			ReportQuerySource querySource, String sSourceID) {
		if (sSourceID == null)
			return null;
		// �õ�ѡ�е�����Դ
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
	 * ��������ԴID�õ�����Դ����
	 * 
	 * @param summaryReport
	 * @param sSourceID
	 *            ����ԴID
	 * @return ����Դ����
	 */
	public static String getDataSourceNameWithID(ReportQuerySource querySource,
			String sSourceID) {
		IDataSource dataSource = getDataSourceWithID(querySource, sSourceID);
		if (dataSource != null)
			return dataSource.getDataSourceName();
		return null;
	}

	/**
	 * ��������Դ��ID�õ�����Դ����������
	 * 
	 * @param querySource
	 * @param sSourceID
	 *            ����ԴID
	 * @param sColID
	 *            ��ID
	 * @return ����Դ����������
	 */
	public static String getDataSourceColNameWithID(
			ReportQuerySource querySource, String sSourceID, String sColID) {
		if (sSourceID == null || sColID == null)
			return null;
		// �õ�ѡ�е�����Դ
		SummaryDataSourceManagerImpl summaryDataSourceManagerImpl = (SummaryDataSourceManagerImpl) querySource
				.getDataSourceManager();
		if (summaryDataSourceManagerImpl == null) {
			return null;
		}
		IDataSource[] summaryDataSourceImpl = summaryDataSourceManagerImpl
				.getDataSourceArray();
		int iCount = summaryDataSourceImpl.length;
		String sCurSourceID = null;
		// �����ֵ�
		DefaultDictionaryImpl defaultDictionaryImpl = null;
		for (int i = 0; i < iCount; i++) {
			sCurSourceID = summaryDataSourceImpl[i].getSourceID();
			// �ж��Ƿ��ǵ�ǰ����Դ
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
	 * ����ö��ԴID�õ�ö��Դname
	 * 
	 * @param summaryReport
	 * 
	 * @param sEnumIDö��ԴID
	 * @return ö��Դname
	 */
	private static String getEnumNameWithID(ReportQuerySource querySource,
			String sEnumID) {
		if (sEnumID == null)
			return null;

		// ö��Դ
		SummaryEnumSourceManagerImpl summaryEnumSourceManagerImpl = (SummaryEnumSourceManagerImpl) querySource
				.getEnumSourceManager();
		// ö��Դ����
		IEnumSource[] summaryEnumSourceImpl = null;

		// ö��Դ����
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
	 * ��������Դ������ֶ����͵�ֵת���ɱ�������ֶ��������Ӧ��ֵ
	 * 
	 * @param sFieldTypeCh
	 *            ����Դ���ֶ�����
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
	 * �ж��ֶ������ͻ��Ǹ�����
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
	 * ��ʾ����Դ��ö�ٵĶ�Ӧ��ϵ
	 * 
	 * @param summaryReport
	 * @param refTypePri
	 *            �Ƿ�ֻ��ʾ������Ӧ��ϵ
	 * @return ö���б�
	 */
	public static List showEnumWhereSetInfo(ReportQuerySource querySource,
			boolean refTypePri, List lstSourceID) {
		if (lstSourceID == null)
			return null;

		// �õ�����Դ��Ϣ
		SummaryDataSourceManagerImpl summaryDataSourceManagerImpl = (SummaryDataSourceManagerImpl) querySource
				.getDataSourceManager();

		// �õ�����Դ��������
		IDataSource[] summaryDataSourceImpl = null;
		if (summaryDataSourceManagerImpl != null) {
			summaryDataSourceImpl = summaryDataSourceManagerImpl
					.getDataSourceArray();
		}
		if (summaryDataSourceImpl == null)
			return null;
		// ����Դ��Ӧ��ö�ٹ�ϵ
		ISourceCol[] summarySourceCol;
		int iCount = summaryDataSourceImpl.length;
		int iColCount = 0;
		// ����ԴID
		String sSourceID = null;
		// ����Դ����
		String sSourceAlais = null;
		// ��ID
		String sColID = null;
		// ������
		String sFieldFname = null;
		// ����Դ����
		String sDataSourceName = null;
		// ���ӷ�ʽ�������ӻ���������
		String sJoinType = null;
		// ö��ԴID
		String sEnumID = null;
		// ö��Դ����
		String sEnumName = null;
		// ��������Դ��ö��Դ��ϵ�б�
		ArrayList lstEnumRela = new ArrayList();
		Map mapEnumRela = null;
		for (int i = 0; i < iCount; i++) {
			// ����ԴID
			sSourceID = summaryDataSourceImpl[i].getSourceID();
			if (!lstSourceID.contains(sSourceID))
				continue;

			sSourceAlais = summaryDataSourceImpl[i].getSourceAlais();

			// ����Դ����
			sDataSourceName = summaryDataSourceImpl[i].getDataSourceName();
			// �õ�����Դ��Ӧ��ö�ٹ�ϵ
			summarySourceCol = summaryDataSourceImpl[i].getColArray();
			iColCount = summarySourceCol.length;
			for (int j = 0; j < iColCount; j++) {
				// ��ID
				sColID = summarySourceCol[j].getSourceColID();
				// ������
				sFieldFname = getDataSourceColNameWithID(querySource,
						sSourceID, sColID);

				// 0:����,1������������, ���������Ӳ���Ҫ�������ӹ�ϵ
				if (refTypePri) {
					if (summarySourceCol[j].getRefEnumSource().getRefType() == 1) {
						continue;
					}
				}
				sJoinType = summarySourceCol[j].getRefEnumSource()
						.getJoinType();
				// ��Ӣ������ת������������
				if (IDefineReport.LEFT_JOIN.equals(sJoinType)) {
					sJoinType = IDefineReport.LEFT_JOIN_NAME;
				} else if (IDefineReport.RIGHT_JOIN.equals(sJoinType)) {
					sJoinType = IDefineReport.RIGHT_JOIN_NAME;
				}
				// �õ�ö��ID
				sEnumID = summarySourceCol[j].getRefEnumSource().getEnumID();
				// ö��Դ����
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
		// ����������Ƶ�Ŀ���ǿ���ͬһʱ�������ɵĸ��Ӳ��ظ�.
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
	 * �õ�����������
	 * 
	 * @return �������
	 */
	public static String getOrderIndex(GroupReport groupReport) {
		// �õ���������ͷ���Ŀ�ʼ��
		int rowIndex[] = CreateGroupReport.getRowIndexs(
				RowConstants.UIAREA_OPERATION, groupReport);
		int row = rowIndex[rowIndex.length - 1];

		// �õ�������
		int colCount = groupReport.getColumnCount();
		int arrayNum = 0;
		Object object = null;
		IGroupAble[] iGroupAbleArray = null;
		int maxOrderIndex = 0;
		int curOrderIndex = 0;
		// ѭ�������õ�����ֵ
		for (int i = 0; i < colCount; i++) {
			// �õ���Ԫ��ֵ
			object = groupReport.getCellValue(i, row);
			// �ж��ǲ��Ƿ�����,���Ƿ������ж��Ƿ����򣬵õ���������
			if (object instanceof MyGroupValueImpl) {
				iGroupAbleArray = ((MyGroupValueImpl) object)
						.getGroupAbleArray();
				arrayNum = iGroupAbleArray.length;
				// ѭ��ȡ�õ�Ԫ������������˳���
				for (int j = 0; j < arrayNum; j++) {
					// �ж��Ƿ�������
					if (!Common.isInteger(iGroupAbleArray[j].getOrderIndex()))
						continue;
					// �õ���ǰ�������
					curOrderIndex = Integer.parseInt(iGroupAbleArray[j]
							.getOrderIndex());
					// �뵱ǰ�������Ž��бȽ�
					if (maxOrderIndex < curOrderIndex) {
						maxOrderIndex = curOrderIndex;
					}
				}
			}
			// �ж��ǲ��Ǽ�����,���Ǽ������ж��Ƿ����򣬵õ���������
			if (object instanceof MyCalculateValueImpl) {
				// �ж��Ƿ�������
				if (((MyCalculateValueImpl) object).getOrderIndex() == null)
					continue;
				if (!Common.isInteger(((MyCalculateValueImpl) object)
						.getOrderIndex()))
					continue;
				curOrderIndex = Integer
						.parseInt(((MyCalculateValueImpl) object)
								.getOrderIndex());
				// �뵱ǰ�������Ž��бȽ�
				if (maxOrderIndex < curOrderIndex) {
					maxOrderIndex = curOrderIndex;
				}
			}
		}
		// ���ص�ǰ�������ż�1
		return String.valueOf(maxOrderIndex + 1);
	}

	/**
	 * �õ���ʱ���ļ���
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
	 * �ж��Ƿ�ö��ֵ��������Դ��ID
	 * 
	 * @param querySource
	 * @param SourceID����ԴID
	 * @param sourceColID����Դ��ID
	 * @param bRefTypePri�Ƿ��ж��ǲ�������
	 * @return ��ö�ٷ���true,���򷵻�false
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
						// 0:������1��������
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
	 * �ж��Ƿ�ö�������и�������Դ��ID
	 * 
	 * @param querySource
	 * @param SourceID����ԴID
	 * @param sourceColID����Դ��ID
	 * @return �Ƿ���true,���򷵻�false
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
					// 0:������1��������
					if (sourceCol[j].getRefEnumSource().getRefType() == 1)
						return true;

				}
			}
		}
		return false;
	}

	/**
	 * �õ�������ݱ�������
	 * 
	 * @param querySource
	 * @param SourceID����ԴID
	 * @param sourceColID����Դ��ID
	 * @return ����
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
	 * �õ�������ݱ�������
	 * 
	 * @param querySource
	 * @param SourceID����ԴID
	 * @param sourceColID����Դ��ID
	 * @return ����
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
	 * ������ID�õ���Ӧö��ID
	 * 
	 * @param querySource
	 * @param sourceID����ԴID
	 * @param sourceColID����Դ��ID
	 * @return ö��ID
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
	 * ������ID�õ���Ӧö����Ϣ
	 * 
	 * @param querySource
	 * @param sourceID����ԴID
	 * @param sourceColID����Դ��ID
	 * @return ö��ID
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
	 * ��������Դö��id�����룬�õ�sourceID����ͬö�ٵ����ݵ��ֶ����ƣ���ö�ٲ����ڷ���null
	 * 
	 * @param querySource
	 * @param refEnumSourceö��
	 * @param sourceID����ԴID
	 * @return ���ݵ��ֶ�����
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
	 * �õ�ö����Ϣ����ö��ID
	 * 
	 * @param querySource
	 * @param enumIDö��ID
	 * @return ö����Ϣ
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
	 * ��֯��������ʾ��Ϣ
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
	 * ��������
	 * 
	 * @param joinBeforeValue
	 * @return
	 */
	private static String getJoinBeforeStr(String joinBeforeValue) {
		return JoinBefore.getJoinBeforeName(joinBeforeValue);
	}

	/**
	 * �Ƚ�����
	 * 
	 * @param compareTypeValue
	 * @return
	 */
	private static String getCompareType(String compareTypeValue) {
		return CompareType.getComparTypeName(compareTypeValue);
	}

	/**
	 * �õ��������к�
	 * 
	 * @param groupReport
	 * @return
	 */
	public static int getOpeRow(GroupReport groupReport) {
		// �õ���ͷ������
		int indexs[] = CreateGroupReport.getRowIndexs(
				RowConstants.UIAREA_OPERATION, groupReport);
		return indexs[0];
	}

	/**
	 * �õ��������к�
	 * 
	 * @param groupReport
	 * @return
	 */
	public static int[] getHeaderRow(GroupReport groupReport) {
		// �õ���ͷ������
		return CreateGroupReport.getRowIndexs(RowConstants.UIAREA_HEADER,
				groupReport);
	}

	/**
	 * ����Ƿ��ǵ�Ԫ�����
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
	 * �ж��Ƿ��Ǽ�����
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
	 * �ж��Ƿ��Ƿ�����
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
	 * �ж��Ƿ����ַ�ֵ
	 */
	public static boolean checkCharVal(String sFieldType) {
		if (IDefineReport.CHAR_Val.equals(sFieldType))
			return true;
		return false;

	}

	/**
	 * ����������Ϣ
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
	 * �ж��Ƿ��Ƿ������õ�����
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
