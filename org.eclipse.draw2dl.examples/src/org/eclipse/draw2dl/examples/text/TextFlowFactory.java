/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl.examples.text;

import org.eclipse.draw2dl.text.BlockFlow;
import org.eclipse.draw2dl.text.FlowFigure;
import org.eclipse.draw2dl.text.TextFlow;
import org.eclipse.draw2dl.Figure;
import org.eclipse.draw2dl.IFigure;

/**
 * Used by TextFlowLargeExample to build paragraphs.
 * @author hudsonr
 */
public class TextFlowFactory {

static void addFontSizes(Figure parent, int min, int max){
	for (int i=min; i<max; i++){
		TextFlow tf = new TextFlow(Integer.toString(i) + " pt. Font ");
//		tf.setBackgroundColor(ColorConstants.lightGray);
//		tf.setForegroundColor(ColorConstants.yellow);
		tf.setFont(new org.eclipse.swt.graphics.Font(org.eclipse.swt.widgets.Display.getDefault(), "Helvetica", i, org.eclipse.swt.SWT.NORMAL));
		parent.add(tf);
	}
}

static void addSentences(org.eclipse.draw2dl.IFigure parent, int count){
	for (int i=0; i<count;i++)
		parent.add(new TextFlow("one two three four five six seven eight nine ten eleven twelve thirteen fourteen fifteen sixteen seventeen eighteen nineteen twenty twenty-one twenty-two twenty-three twenty-four twenty-five twenty-six twenty-seven twenty-eight twenty-nine thirty thirty-one thirty-two thirty-three thirty-four thirty-five thirty-six thirty-seven thirty-eight thirty-nine forty forty-one forty-two forty-three forty-four forty-five forty-six forty-seven forty-eight forty-nine fifty fifty-one fifty-two fifty-three fifty-four fifty-five fifty-six fifty-seven fifty-eight fifty-nine sixty>>>"));
}

static org.eclipse.draw2dl.IFigure block(IFigure child){
	FlowFigure block = new BlockFlow();
	block.add(child);
	return block;
}

}