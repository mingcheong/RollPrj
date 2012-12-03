/**
 * @# ISelectionChangeListener.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.szzbset.ui;

import com.fr.cell.CellSelection;
import com.fr.report.CellElement;

/**
 * ����˵��:
 * <P>
 * Copyright
 * <P>
 * All rights reserved.
 * <P>
 * ��Ȩ���У��㽭����
 * <P>
 * δ������˾��ɣ��������κη�ʽ���ƻ�ʹ�ñ������κβ��֣�
 * <P>
 * ��Ȩ�߽��ܵ�����׷����
 * <P>
 * DERIVED FROM: NONE
 * <P>
 * PURPOSE:
 * <P>
 * DESCRIPTION:
 * <P>
 * CALLED BY:
 * <P>
 * UPDATE:
 * <P>
 * DATE: 2011-3-19
 * <P>
 * HISTORY: 1.0
 * 
 * @version 1.0
 * @author qzc
 * @since java 1.4.2
 */
public interface ISelectionListener {

	public void selectionChangeed(CellSelection cs, CellElement cell,
			CellFieldInfo cf);

}
