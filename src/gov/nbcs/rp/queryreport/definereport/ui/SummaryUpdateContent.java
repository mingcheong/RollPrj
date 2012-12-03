/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.reportcy.common.script.DefaultUpdateContent;
import com.foundercy.pf.reportcy.common.util.StringEx;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>

 */
public class SummaryUpdateContent extends DefaultUpdateContent {

	private static final long serialVersionUID = 1L;

	public String getMovedLocationContent(String oldContent,
			boolean rowOrColumn, int rowColumnIndex, int step) {
		if (StringEx.isEmpty(oldContent))
			return StringEx.sNull;
		String tmp = oldContent;
		// 这里对 . 进行一个替换操作,
		tmp = StringEx.replace(tmp, ".", "^");
		tmp = StringEx.replace(tmp, IDefineReport.ASC_ARROW, "UP_UP");
		tmp = StringEx.replace(tmp, IDefineReport.DESC_ARROW, "DOWN_DOWN");

		tmp = StringEx.replace(tmp, "（", "ZW_ZW_L");
		tmp = StringEx.replace(tmp, "）", "ZW_ZW_R");
		tmp = StringEx.replace(tmp, "：", "COLON_COLON");
		tmp = StringEx.replace(tmp, ":", "COLON_COLON_DBC");

		String result = super.getMovedLocationContent(tmp, rowOrColumn,
				rowColumnIndex, step);
		result = StringEx.replace(result, "^", ".");
		result = StringEx.replace(result, "UP_UP", IDefineReport.ASC_ARROW);
		result = StringEx
				.replace(result, "DOWN_DOWN", IDefineReport.DESC_ARROW);

		result = StringEx.replace(result, "ZW_ZW_L", "（");

		result = StringEx.replace(result, "ZW_ZW_R", "）");
		result = StringEx.replace(result, "COLON_COLON", "：");
		result = StringEx.replace(result, "COLON_COLON_DBC", ":");

		return result;
	}

}
