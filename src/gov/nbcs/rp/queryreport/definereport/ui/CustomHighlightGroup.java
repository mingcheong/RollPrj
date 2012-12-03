/*
 * Copyright 2011 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import com.fr.data.condition.JoinCondition;
import com.fr.data.condition.ListCondition;
import com.fr.data.condition.ObjectCondition;
import com.fr.data.core.Compare;
import com.fr.report.highlight.DefaultHighlight;
import com.fr.report.highlight.HighlightGroup;
import com.fr.report.highlight.ValueHighlightAction;

/**
 * <p>
 * Title:定义单元格高亮条件
 * </p>
 * <p>
 * Description:定义单元格高亮条件
 * </p>
 * <p>

 */
public class CustomHighlightGroup extends HighlightGroup {

	private static final long serialVersionUID = 1L;

	public CustomHighlightGroup() {

		ObjectCondition condition = new ObjectCondition();
		condition.setCompare(new Compare(0, "0"));
		JoinCondition joinCondition = new JoinCondition();
		joinCondition.setLiteCondition(condition);

		ObjectCondition conditionA = new ObjectCondition();
		conditionA.setCompare(new Compare(0, "0.00"));
		JoinCondition joinConditionA = new JoinCondition();
		joinConditionA.setLiteCondition(conditionA);

		ListCondition listCondition = new ListCondition();
		listCondition.addJoinCondition(joinCondition);
		listCondition.addJoinCondition(joinConditionA);

		ValueHighlightAction valueHighlightAction = new ValueHighlightAction();
		valueHighlightAction.setValue("");

		DefaultHighlight highlight = new DefaultHighlight();
		highlight.setCondition(listCondition);
		highlight.addHighlightAction(valueHighlightAction);

		this.addHighlight(highlight);
	}

}
