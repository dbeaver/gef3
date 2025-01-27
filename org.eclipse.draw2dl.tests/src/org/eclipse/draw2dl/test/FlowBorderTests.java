/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2dl.test;

import org.eclipse.draw2dl.FigureUtilities;
import org.eclipse.draw2dl.geometry.Insets;
import org.eclipse.draw2dl.text.TextFlow;
import org.eclipse.draw2dl.text.TextFragmentBox;
import org.eclipse.swt.graphics.FontMetrics;

/**
 * @since 3.1
 */
public class FlowBorderTests extends SimpleTextTest {

	public void testBorderedTextFlow() {
		sentence.setBorder(new TestBorder(new Insets(5)));
		makePageWidth("The quick", 5);
		expected.x = 0;
		expected.y = 0;
		expected.width = 5;
		FontMetrics metrics = FigureUtilities.getFontMetrics(font);
		expected.height = metrics.getHeight();
		assertFragmentLocation(getTextFragment(sentence, 0));
		expected.x = 5;
		expected.width = FigureUtilities.getStringExtents("The quick", font).width;
		assertFragmentLocation(getTextFragment(sentence, 1));
	}

	protected TextFragmentBox getTextFragment(TextFlow flow, int index) {
		return (TextFragmentBox) flow.getFragments().get(index);
	}

}
