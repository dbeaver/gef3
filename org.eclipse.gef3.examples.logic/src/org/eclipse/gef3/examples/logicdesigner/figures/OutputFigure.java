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

import org.eclipse.draw2dl.StackLayout;

import org.eclipse.gef3.examples.logicdesigner.model.SimpleOutput;

public class OutputFigure extends NodeFigure {

	public OutputFigure() {
		FixedConnectionAnchor outputConnectionAnchor = new FixedConnectionAnchor(
				this);
		outputConnectionAnchor.topDown = false;
		outputConnectionAnchor.offsetH = 7;
		outputConnectionAnchors.addElement(outputConnectionAnchor);
		connectionAnchors
				.put(SimpleOutput.TERMINAL_OUT, outputConnectionAnchor);
		setLayoutManager(new StackLayout());
	}

}
