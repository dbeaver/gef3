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
package org.eclipse.draw2dl.text;

import java.util.List;

import org.eclipse.draw2dl.IFigure;
import org.eclipse.swt.SWT;

import org.eclipse.draw2dl.Figure;
import org.eclipse.draw2dl.PositionConstants;
import org.eclipse.draw2dl.geometry.Insets;

/**
 * The layout for {@link org.eclipse.draw2dl.text.BlockFlow} figures.
 * 
 * <P>
 * WARNING: This class is not intended to be subclassed by clients.
 * 
 * @author hudsonr
 * @since 2.1
 */
public class BlockFlowLayout extends org.eclipse.draw2dl.text.FlowContainerLayout {

	BlockBox blockBox;
	boolean blockInvalid = false;
	private boolean continueOnSameLine = false;
	private org.eclipse.draw2dl.text.CompositeBox previousLine = null;

	/**
	 * Creates a new BlockFlowLayout with the given BlockFlow.
	 * 
	 * @param blockFlow
	 *            the BlockFlow
	 */
	public BlockFlowLayout(org.eclipse.draw2dl.text.BlockFlow blockFlow) {
		super(blockFlow);
	}

	private void addBelowPreviousLine(org.eclipse.draw2dl.text.CompositeBox line) {
		if (previousLine == null)
			line.setLineTop(line.getTopMargin());
		else
			line.setLineTop(previousLine.getBaseline()
					+ previousLine.getDescent()
					+ Math.max(previousLine.getBottomMargin(),
							line.getTopMargin()));

		int alignment = getBlockFlow().getHorizontalAligment();
		if (alignment == PositionConstants.LEFT
				|| alignment == PositionConstants.RIGHT) {
			int orientation = getBlockFlow().getOrientation();
			if (alignment == PositionConstants.LEFT)
				alignment = orientation == SWT.LEFT_TO_RIGHT ? PositionConstants.ALWAYS_LEFT
						: PositionConstants.ALWAYS_RIGHT;
			else
				alignment = orientation == SWT.LEFT_TO_RIGHT ? PositionConstants.ALWAYS_RIGHT
						: PositionConstants.ALWAYS_LEFT;
		}
		if (alignment != PositionConstants.CENTER
				&& getBlockFlow().isMirrored())
			alignment = (PositionConstants.ALWAYS_LEFT | PositionConstants.ALWAYS_RIGHT)
					& ~alignment;

		switch (alignment) {
		case PositionConstants.ALWAYS_RIGHT:
			line.setX(blockBox.getRecommendedWidth() - line.getWidth());
			break;
		case PositionConstants.CENTER:
			line.setX((blockBox.getRecommendedWidth() - line.getWidth()) / 2);
			break;
		case PositionConstants.ALWAYS_LEFT:
			line.setX(0);
			break;
		default:
			throw new RuntimeException("Unexpected state"); //$NON-NLS-1$
		}
		blockBox.add(line);
		previousLine = line;
	}

	/**
	 * Align the line horizontally and then commit it.
	 */
	protected void addCurrentLine() {
		addBelowPreviousLine(currentLine);
		((org.eclipse.draw2dl.text.LineRoot) currentLine).commit();
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowContext#addLine(org.eclipse.draw2dl.text.CompositeBox)
	 */
	public void addLine(CompositeBox box) {
		endLine();
		addBelowPreviousLine(box);
	}

	/**
	 * Marks the blocks contents as changed. This means that children will be
	 * invalidated during validation.
	 * 
	 * @since 3.1
	 */
	public void blockContentsChanged() {
		blockInvalid = true;
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowContainerLayout#cleanup()
	 */
	protected void cleanup() {
		super.cleanup();
		previousLine = null;
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowContainerLayout#createNewLine()
	 */
	protected void createNewLine() {
		currentLine = new LineRoot(getBlockFlow().isMirrored());
		currentLine.setRecommendedWidth(blockBox.getRecommendedWidth());
	}

	/**
	 * Called by flush(), adds the BlockBox associated with this BlockFlowLayout
	 * to the current line and then ends the line.
	 */
	protected void endBlock() {
		if (blockInvalid) {
			Insets insets = getBlockFlow().getInsets();
			blockBox.height += insets.getHeight();
			blockBox.width += insets.getWidth();
		}

		if (getContext() != null)
			getContext().addLine(blockBox);

		if (blockInvalid) {
			blockInvalid = false;
			List<IFigure> v = getFlowFigure().getChildren();
			for (IFigure child : v) ((FlowFigure) child).postValidate();
		}
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowContext#endLine()
	 */
	public void endLine() {
		if (currentLine == null || !currentLine.isOccupied())
			return;
		addCurrentLine();
		currentLine = null;
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowContainerLayout#flush()
	 */
	protected void flush() {
		endLine();
		endBlock();
	}

	boolean forceChildInvalidation(Figure f) {
		return blockInvalid;
	}

	/**
	 * Returns the BlockFlow associated with this BlockFlowLayout
	 * 
	 * @return the BlockFlow
	 */
	protected final org.eclipse.draw2dl.text.BlockFlow getBlockFlow() {
		return (org.eclipse.draw2dl.text.BlockFlow) getFlowFigure();
	}

	int getContextWidth() {
		return getContext().getRemainingLineWidth();
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowContext#getContinueOnSameLine()
	 */
	public boolean getContinueOnSameLine() {
		return continueOnSameLine;
	}

	/**
	 * @see FlowContext#getWidthLookahead(org.eclipse.draw2dl.text.FlowFigure, int[])
	 */
	public void getWidthLookahead(org.eclipse.draw2dl.text.FlowFigure child, int result[]) {
		List<IFigure> children = getFlowFigure().getChildren();
		int index = -1;
		if (child != null)
			index = children.indexOf(child);

		for (int i = index + 1; i < children.size(); i++)
			if (((FlowFigure) children.get(i))
					.addLeadingWordRequirements(result))
				return;
	}

	/**
	 * @see FlowContainerLayout#preLayout()
	 */
	protected void preLayout() {
		setContinueOnSameLine(false);
		blockBox = getBlockFlow().getBlockBox();
		setupBlock();
		// Probably could setup current and previous line here, or just previous
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowContext#setContinueOnSameLine(boolean)
	 */
	public void setContinueOnSameLine(boolean value) {
		continueOnSameLine = value;
	}

	/**
	 * sets up the single block that contains all of the lines.
	 */
	protected void setupBlock() {
		int recommended = getContextWidth();
		if (recommended == Integer.MAX_VALUE)
			recommended = -1;
		BlockFlow bf = getBlockFlow();
		if (recommended > 0) {
			int borderCorrection = bf.getInsets().getWidth()
					+ bf.getLeftMargin() + bf.getRightMargin();
			recommended = Math.max(0, recommended - borderCorrection);
		}

		if (recommended != blockBox.recommendedWidth) {
			blockInvalid = true;
			blockBox.setRecommendedWidth(recommended);
		}

		if (blockInvalid) {
			blockBox.height = 0;
			blockBox.setWidth(Math.max(0, recommended));
		}
	}

}