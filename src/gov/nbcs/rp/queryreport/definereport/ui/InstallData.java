/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

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
 * CreateData 2011-5-15
 * </p>
 * 
 * @author qzc
 * @version 6.2.40
 */
public class InstallData {

	protected String m_name;

	protected Object m_value;

	protected boolean m_selected;

	public InstallData(String name, Object value) {
		m_name = name;
		m_value = value;
		m_selected = false;
	}

	public String getName() {
		return m_name;
	}

	public Object getValue() {
		return m_value;
	}

	public void setSelected(boolean selected) {
		m_selected = selected;
	}

	public void invertSelected() {
		m_selected = !m_selected;
	}

	public boolean isSelected() {
		return m_selected;
	}

	public String toString() {
		return m_name;
	}

}
