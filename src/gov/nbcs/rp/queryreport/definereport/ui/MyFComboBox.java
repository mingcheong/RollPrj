/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.util.List;
import java.util.Map;

import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.reportcy.summary.object.ReportQuerySource;

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
 * CreateData 2011-3-29
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class MyFComboBox extends FComboBox {

	private static final long serialVersionUID = 1L;

	private ReportQuerySource querySource = null;

	public MyFComboBox(ReportQuerySource querySource) {
		super();
		this.querySource = querySource;
		init();
	}

	public MyFComboBox(String arg0, ReportQuerySource querySource) {
		super(arg0);
		this.querySource = querySource;
		init();
	}

	private void init() {

		// 得到数据源列表
		List lstDataSource = DefinePub.getDataSource(querySource);

		// 获得值
		Map dataSourceMap = null;
		String sDataSourceRef = "";
		if (lstDataSource != null) {
			for (int i = 0; i < lstDataSource.size(); i++) {
				dataSourceMap = (Map) lstDataSource.get(i);
				if (!"".equals(sDataSourceRef)) {
					sDataSourceRef = sDataSourceRef + "+";
				}
				sDataSourceRef = sDataSourceRef
						+ dataSourceMap.get(IDefineReport.SOURCE_ID) + "#"
						+ dataSourceMap.get(IDefineReport.DATASOURCE_NAME);
			}
		}
		this.setRefModel(sDataSourceRef);
		this.setSelectedIndex(-1);
	}

}
