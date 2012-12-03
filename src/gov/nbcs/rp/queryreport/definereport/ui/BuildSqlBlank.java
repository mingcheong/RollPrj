/**
 * 
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import gov.nbcs.rp.common.Common;
import com.foundercy.pf.reportcy.summary.iface.cell.IGroupAble;
import com.foundercy.pf.reportcy.summary.iface.enumer.IEnumSource;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;


public class BuildSqlBlank {
	/**
	 * 末级空格
	 * 
	 * @param groupValueImpl
	 * @param groupAble
	 * @return
	 */
	public static String getBlankIsTotal(MyGroupValueImpl groupValueImpl,
			IGroupAble groupAble, ReportQuerySource querySource) {
		StringBuffer sBlank = new StringBuffer();

		sBlank.append(getBlankNumRollup(groupValueImpl, groupAble));

		if (groupAble.getIsMbSummary() == 1) {
			sBlank.append("  ");
		}
		if (!Common.isNullStr(sBlank.toString())) {
			sBlank.insert(0, "'");
			sBlank.append("'||");
		}

		int iLev = 0;
		if (!Common.isNullStr(groupAble.getLevel())) {
			iLev = groupAble.getLevel().split(",").length;
		}

		String sLevStr = getLevStr(groupAble, querySource);
		sBlank.append("substr('              ',0,");
		sBlank.append(" case when (");
		sBlank.append(sLevStr);
		sBlank.append(")>" + iLev);
		sBlank.append(" then " + iLev + " else (" + sLevStr + ") end *2 )");
		sBlank.append("||");
		return sBlank.toString();
	}

	/**
	 * 层次空格
	 * 
	 * @param groupValueImpl
	 * @param groupAble
	 * @param curLev
	 * @return
	 */
	public static String getBlankLev(MyGroupValueImpl groupValueImpl,
			IGroupAble groupAble, String curLev) {
		String sBlank = getBlankNumRollup(groupValueImpl, groupAble);

		if (groupAble.getIsMbSummary() == 1) {
			sBlank = sBlank + "  ";
		}
		if (!Common.isNullStr(groupAble.getLevel())) {
			String[] levArray = groupAble.getLevel().split(",");
			int levLength = levArray.length;
			for (int j = 0; j < levLength; j++) {
				if (levArray[j].equals(curLev)) {
					break;
				}
				sBlank = sBlank + "  ";
			}
		}
		if (!Common.isNullStr(sBlank)) {
			sBlank = "'" + sBlank + "'||";
		}
		return sBlank;
	}

	/**
	 * 业务处室空格
	 * 
	 * @param groupValueImpl
	 * @param groupAble
	 * @return
	 */
	public static String getBlankMb(MyGroupValueImpl groupValueImpl,
			IGroupAble groupAble) {
		String sBlank = getBlankNumRollup(groupValueImpl, groupAble);
		if (!Common.isNullStr(sBlank)) {
			sBlank = "'" + sBlank + "'||";
		}
		return sBlank;
	}

	/**
	 * 分组汇总列
	 * 
	 * @param groupValueImpl
	 * @param groupAble
	 * @return
	 */
	public static String getBlankNumOther(MyGroupValueImpl groupValueImpl,
			IGroupAble groupAble) {
		String sBlank = getBlankNumRollup(groupValueImpl, groupAble);
		if (!Common.isNullStr(sBlank)) {
			sBlank = "'" + sBlank + "'||";
		}
		return sBlank;
	}

	private static String getBlankNumRollup(MyGroupValueImpl groupValueImpl,
			IGroupAble groupAble) {
		String sBlank = "";
		IGroupAble[] groupAbleArray = groupValueImpl.getGroupAbleArray();

		int length = groupAbleArray.length;
		for (int i = 0; i < length; i++) {
			if (groupAble == groupAbleArray[i]) {
				continue;
			}

			// 判断汇总顺序
			if (Integer.parseInt(groupAble.getSummaryIndex()) < Integer
					.parseInt(groupAbleArray[i].getSummaryIndex())) {
				continue;
			}
			if (DefinePubOther.isDetail(groupAbleArray[i])
					|| DefinePubOther.isGroup(groupAbleArray[i])) {// 明细或分组
				sBlank = sBlank + "  ";
			} else {// 分组向上汇总
				if (groupAbleArray[i].getIsMbSummary() == 1) {
					sBlank = sBlank + "  ";
				}
				if (!Common.isNullStr(groupAbleArray[i].getLevel())) {
					int levLength = groupAbleArray[i].getLevel().split(",").length;
					sBlank = sBlank
							+ "                ".substring(0, levLength * 2);
				}
				if (Common.estimate(groupAbleArray[i].getIsTotal())) {
					sBlank = sBlank + "  ";
				}
			}
		}
		return sBlank;
	}

	/**
	 * 根据引用列节次组织XH字段需要信息
	 * 
	 * @param groupAble
	 * @return
	 */
	private static String getLevStr(IGroupAble groupAble,
			ReportQuerySource querySource) {

		String sSourceID = groupAble.getSourceID();
		String sSourceColID = groupAble.getSourceColID();

		// 判断是否枚举名称
		if (DefinePub.judgetEnumNameWithColID(querySource, sSourceID,
				sSourceColID)) {
			// 根据名称得到编码
			sSourceColID = DefinePub.getCodeWithName(querySource, sSourceID,
					sSourceColID);
		}

		String sEnumId = DefinePub.getEnumIDWithColID(querySource, groupAble
				.getSourceID(), groupAble.getSourceColID());
		if (sEnumId == null)
			return "";
		IEnumSource enumSource = DefinePub.getEnumInfoWithID(querySource,
				sEnumId);
		String[] sLevArray = enumSource.getLevelInfo().split("-");
		if (sLevArray == null || sLevArray.length == 0)
			return "";
		int len = sLevArray.length;
		int iLev = 0;
		StringBuffer sSql = new StringBuffer();
		sSql.append("case");
		for (int i = 0; i < len; i++) {
			iLev = iLev + Integer.parseInt(sLevArray[i]);

			sSql.append(" when length(");
			sSql.append(sSourceColID);
			sSql.append(")=");
			sSql.append(iLev);
			sSql.append(" then ");
			sSql.append(i);
		}
		sSql.append(" end");
		return sSql.toString();
	}

}
