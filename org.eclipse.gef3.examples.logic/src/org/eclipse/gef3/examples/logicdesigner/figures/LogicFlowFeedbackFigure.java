/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef3.examples.logicdesigner.figures;

import org.eclipse.draw2dl.RectangleFigure;

public class LogicFlowFeedbackFigure extends RectangleFigure {

	public LogicFlowFeedbackFigure() {
		this.setFill(false);
		this.setXOR(true);
		setBorder(new LogicFlowFeedbackBorder());
	}

}
