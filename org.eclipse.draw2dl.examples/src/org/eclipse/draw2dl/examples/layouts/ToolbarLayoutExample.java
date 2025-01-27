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
package org.eclipse.draw2dl.examples.layouts;

import org.eclipse.draw2dl.*;
import org.eclipse.draw2dl.examples.AbstractExample;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;

/**
 * @author hudsonr
 * Created on Apr 30, 2003
 */
public class ToolbarLayoutExample extends AbstractExample {

org.eclipse.draw2dl.ToolbarLayout layout;

Shape ellipse, rect, roundRect, ellipse2, rect2;

public static void main(String[] args) {
	new ToolbarLayoutExample().run();
}

/**
 * @see AbstractExample#getContents()
 */
protected IFigure getContents() {
	org.eclipse.draw2dl.Figure container = new Figure();
	container.setBorder(new LineBorder());
	container.setLayoutManager(layout = new ToolbarLayout(true));
	
	ellipse = new org.eclipse.draw2dl.Ellipse();
	ellipse.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.blue);
	ellipse.setSize(60,40);
	container.add(ellipse);

	rect = new org.eclipse.draw2dl.RectangleFigure();
	rect.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.red);
	rect.setSize(30,70);
	container.add(rect);

	roundRect = new RoundedRectangle();
	roundRect.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.yellow);
	roundRect.setSize(90,30);
	container.add(roundRect);

	rect2 = new RectangleFigure();
	rect2.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.gray);
	rect2.setSize(50,80);
	container.add(rect2);

	ellipse2 = new Ellipse();
	ellipse2.setBackgroundColor(ColorConstants.green);
	ellipse2.setSize(50,50);
	container.add(ellipse2);
	
	return container;
}

/**
 * @see AbstractExample#hookShell()
 */
protected void hookShell() {
	Composite composite = new Composite(shell, 0);
	composite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
	
	composite.setLayout(new GridLayout());
	
	final Button horizontal = new Button(composite, SWT.CHECK);
	horizontal.setText("Horizontal");
	horizontal.setSelection(true);
	horizontal.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			layout.setVertical(layout.isHorizontal());
			if (layout.getStretchMinorAxis())
				resetShapes();
			contents.revalidate();
			shell.layout(true);
		}
	});

	final Button stretch = new Button(composite, SWT.CHECK);
		stretch.setText("Stretch Minor Axis");
		stretch.setSelection(false);
		stretch.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setStretchMinorAxis(!layout.getStretchMinorAxis());
				resetShapes();
				contents.revalidate();
				shell.layout(true);
			}
		});
	{
		Group major = new Group(composite, 0);
		major.setLayout(new FillLayout(SWT.VERTICAL));
		major.setText("Minor Axis");
		
		Button left = new Button(major, SWT.RADIO);
		left.setText("Top/Left");
		left.setSelection(true);
		left.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setMinorAlignment(org.eclipse.draw2dl.FlowLayout.ALIGN_LEFTTOP);
				contents.revalidate();
			}
		});
	
		Button center = new Button(major, SWT.RADIO);
		center.setText("Middle/Center");
		center.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setMinorAlignment(org.eclipse.draw2dl.FlowLayout.ALIGN_CENTER);
				contents.revalidate();
			}
		});
	
		Button right = new Button(major, SWT.RADIO);
		right.setText("Buttom/Right");
		right.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setMinorAlignment(FlowLayout.ALIGN_RIGHTBOTTOM);
				contents.revalidate();
			}
		});
		
		final Scale spacing = new Scale(major, 0);
		spacing.setMinimum(0);
		spacing.setMaximum(20);
		spacing.setSelection(5);
		spacing.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				layout.setSpacing(spacing.getSelection());
				contents.revalidate();
			}
		});
		Label spacingLabel = new Label(major, SWT.CENTER);
		spacingLabel.setText("Spacing");

	}
}

private void resetShapes() {
	rect.setSize(30,70);
	rect2.setSize(50,80);
	roundRect.setSize(90,30);
	ellipse.setSize(60,40);
	ellipse2.setSize(50,50);
}

}
