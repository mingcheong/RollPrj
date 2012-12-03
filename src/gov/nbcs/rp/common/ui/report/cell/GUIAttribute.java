/**
 * Copyright �㽭���� ��Ȩ����
 * 
 * ������Ŀ����ϵͳ
 * 
 * @title ��ѯtable
 * 
 * @author Ǯ�Գ�
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common.ui.report.cell;

import gov.nbcs.rp.common.ui.report.Report;
import com.fr.base.Constants;
import com.fr.report.cellElement.CellGUIAttr;

public class GUIAttribute extends CellGUIAttr {
    
    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private Report report;

    private Cell cell;

    public GUIAttribute(Report report) {
        this.report = report;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }
    
    /**
     * ��Ԫ���Ƿ���Ա༭�ж��Ե����û���ȫ������ã�ȫ��Ŀɱ༭���ú�DataSet״̬�й�
     */
    public int getEditableState() {
        if(!report.isTableEditable()) {
            return Constants.EDITABLE_STATE_FALSE;
        }
//        if (cell != null) {
//			PropertyProvider cellProperty = report.getCellProperty();
//			Object fieldId = cellProperty.getFieldId(cell.getFieldName());
//			return cellProperty.isEditable(cell.getBookmark(), fieldId) ? Constants.EDITABLE_STATE_TRUE
//					: Constants.EDITABLE_STATE_FALSE;
//		}
//        return super.getEditableState();
		return cell == null ? super.getEditableState() : cell
				.getEditableState();
    }

//qj ����������
//    public int getEnterMoveDirection() {
//        // TODO Auto-generated method stub
//        return report==null?ReportConstants.MOVE_DIRECTION_RIGHT:report.getEnterMoveDirection();
//    }

//    public CellEditorDef getCellEditorDef() {
//        if(report!=null && cell!=null) {
//           NumberCellEditorDef def = report.getNumCellEditorDef();
//           if(cell.getFractionDigits()>=0) {
//               def.setMaxDecimalLength(cell.getFractionDigits());
//           }
//           return def;
//        }
//        return super.getCellEditorDef();
//    }
}
