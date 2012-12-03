/**
 * @# FilterFactory.java <文件名>
 */
package gov.nbcs.rp.queryreport.reportsh.ui.FilterPro;

/**
 * 功能说明:
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>

 */
public class FilterFactory {

	public static IFilterProvider createFilter(String type) {

		if (IFilterProvider.TYPE_ACCT.equals(type)) {
			return new AcctFilterProvider();
		} else if (IFilterProvider.TYPE_ACCT_JJ.equals(type)) {
			return new AcctJJFilterProvider();

		} else if (IFilterProvider.TYPE_GENERAL.equals(type)) {
			return new GeneralFilter();
		} else if (IFilterProvider.TYPE_PROJECT.equals(type)) {
			return new ProjectFilter();
		} else if (IFilterProvider.TYPE_ACCT_TYPE.equals(type)) {
			return new AcctTypeFilterProvider();
		} 
		return null;

	}

}
