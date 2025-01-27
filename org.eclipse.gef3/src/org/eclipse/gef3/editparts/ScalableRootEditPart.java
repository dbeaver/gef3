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
package org.eclipse.gef3.editparts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2dl.*;
import org.eclipse.draw2dl.StackLayout;
import org.eclipse.draw2dl.Viewport;
import org.eclipse.draw2dl.geometry.Dimension;
import org.eclipse.draw2dl.geometry.Point;
import org.eclipse.draw2dl.geometry.Rectangle;

import org.eclipse.gef3.AutoexposeHelper;
import org.eclipse.gef3.DragTracker;
import org.eclipse.gef3.LayerConstants;
import org.eclipse.gef3.Request;
import org.eclipse.gef3.SnapToGrid;
import org.eclipse.gef3.tools.MarqueeDragTracker;

/**
 * A graphical root composed of regular {@link Layer Layers}.
 * The layers are added to {@link LayeredPane} or
 * {@link ScalableLayeredPane}. All layers are positioned by
 * {@link StackLayout}s, which means that the diagrams
 * preferred size is the union of the preferred size of each layer, and all
 * layers will be positioned to fill the entire diagram.
 * <P>
 * <EM>IMPORTANT</EM>ScalableRootEditPart uses a <code>Viewport</code> as its
 * primary figure. It must be used with a
 * {@link org.eclipse.gef3.ui.parts.ScrollingGraphicalViewer}. The viewport gets
 * installed into that viewer's {@link FigureCanvas}, which
 * provides native scrollbars for scrolling the viewport.
 * <P>
 * The layer structure (top-to-bottom) for this root is:
 * <table cellspacing="0" cellpadding="0">
 * <tr>
 * <td colspan="4">Root Layered Pane</td>
 * </tr>
 * <tr>
 * <td>&#9500;</td>
 * <td colspan="3">&nbsp;Guide Layer</td>
 * </tr>
 * <tr>
 * <td>&#9500;</td>
 * <td colspan="3">&nbsp;Feedback Layer</td>
 * </tr>
 * <tr>
 * <td>&#9500;</td>
 * <td colspan="3">&nbsp;Handle Layer</td>
 * </tr>
 * <tr>
 * <td>&#9492;</td>
 * <td colspan="2">&nbsp;<b>Scalable Layers</b></td>
 * <td>({@link ScalableLayeredPane})</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>&#9500;</td>
 * <td colspan="2">&nbsp;Scaled Feedback Layer</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>&#9500;</td>
 * <td colspan="2">&nbsp;Printable Layers</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&#9500; Connection Layer</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>&nbsp;</td>
 * <td>&#9492;&nbsp;Primary Layer</td>
 * <td>&nbsp;</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * <td>&#9492;</td>
 * <td colspan="2">&nbsp;Grid Layer</td>
 * </tr>
 * </table>
 * 
 * @author Eric Bordeau
 * @since 2.1.1
 */
public class ScalableRootEditPart extends SimpleRootEditPart implements
		LayerConstants, org.eclipse.gef3.editparts.LayerManager {

	class FeedbackLayer extends Layer {
		FeedbackLayer() {
			setEnabled(false);
		}

		/**
		 * @see Figure#getPreferredSize(int, int)
		 */
		public Dimension getPreferredSize(int wHint, int hHint) {
			Rectangle rect = new Rectangle();
			for (int i = 0; i < getChildren().size(); i++)
				rect.union(getChildren().get(i).getBounds());
			return rect.getSize();
		}

	}

	private LayeredPane innerLayers;
	private LayeredPane printableLayers;
	private ScalableLayeredPane scaledLayers;
	private PropertyChangeListener gridListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			String property = evt.getPropertyName();
			if (property.equals(SnapToGrid.PROPERTY_GRID_ORIGIN)
					|| property.equals(SnapToGrid.PROPERTY_GRID_SPACING)
					|| property.equals(SnapToGrid.PROPERTY_GRID_VISIBLE))
				refreshGridLayer();
		}
	};

	private org.eclipse.gef3.editparts.ZoomManager zoomManager;

	/**
	 * Constructor for ScalableFreeformRootEditPart
	 */
	public ScalableRootEditPart() {
		zoomManager = createZoomManager(
				(ScalableLayeredPane) getScaledLayers(),
				((Viewport) getFigure()));
	}

	/**
	 * Responsible of creating a {@link org.eclipse.gef3.editparts.ZoomManager} to be used by this
	 * {@link ScalableRootEditPart}.
	 * 
	 * @return A new {@link org.eclipse.gef3.editparts.ZoomManager} bound to the given
	 *         {@link ScalableFigure} and {@link Viewport}.
	 * @since 3.10
	 */
	protected org.eclipse.gef3.editparts.ZoomManager createZoomManager(ScalableFigure scalableFigure,
																	   Viewport viewport) {
		return new org.eclipse.gef3.editparts.ZoomManager(scalableFigure, viewport);
	}

	/**
	 * @see AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		Viewport viewport = createViewport();

		innerLayers = new LayeredPane();
		createLayers(innerLayers);

		viewport.setContents(innerLayers);
		return viewport;
	}

	/**
	 * Creates a {@link org.eclipse.gef3.editparts.GridLayer grid}. Sub-classes can override this method to
	 * customize the appearance of the grid. The grid layer should be the first
	 * layer (i.e., beneath the primary layer) if it is not to cover up parts on
	 * the primary layer. In that case, the primary layer should be transparent
	 * so that the grid is visible.
	 * 
	 * @return the newly created GridLayer
	 */
	protected org.eclipse.gef3.editparts.GridLayer createGridLayer() {
		return new org.eclipse.gef3.editparts.GridLayer();
	}

	/**
	 * Creates the top-most set of layers on the given layered pane
	 * 
	 * @param layeredPane
	 *            the parent for the created layers
	 */
	protected void createLayers(LayeredPane layeredPane) {
		layeredPane.add(getScaledLayers(), SCALABLE_LAYERS);
		layeredPane.add(new Layer() {
			public Dimension getPreferredSize(int wHint, int hHint) {
				return new Dimension();
			}
		}, HANDLE_LAYER);
		layeredPane.add(new FeedbackLayer(), FEEDBACK_LAYER);
		layeredPane.add(new GuideLayer(), GUIDE_LAYER);
	}

	/**
	 * Creates a layered pane and the layers that should be printed.
	 * 
	 * @see org.eclipse.gef3.print.PrintGraphicalViewerOperation
	 * @return a new LayeredPane containing the printable layers
	 */
	protected LayeredPane createPrintableLayers() {
		LayeredPane pane = new LayeredPane();

		Layer layer = new Layer();
		layer.setLayoutManager(new StackLayout());
		pane.add(layer, PRIMARY_LAYER);

		layer = new ConnectionLayer();
		layer.setPreferredSize(new Dimension(5, 5));
		pane.add(layer, CONNECTION_LAYER);

		return pane;
	}

	/**
	 * Creates a scalable layered pane and the layers that should be scaled.
	 * 
	 * @return a new <code>ScalableLayeredPane</code> containing the scalable
	 *         layers
	 */
	protected ScalableLayeredPane createScaledLayers() {
		ScalableLayeredPane layers = new ScalableLayeredPane();
		layers.add(createGridLayer(), GRID_LAYER);
		layers.add(getPrintableLayers(), PRINTABLE_LAYERS);
		layers.add(new FeedbackLayer(), SCALED_FEEDBACK_LAYER);
		return layers;
	}

	/**
	 * Constructs the viewport that will be used to contain all of the layers.
	 * 
	 * @return a new Viewport
	 */
	protected Viewport createViewport() {
		return new Viewport(true);
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class key) {
		if (key == AutoexposeHelper.class)
			return new ViewportAutoexposeHelper(this);
		return super.getAdapter(key);
	}

	/**
	 * The contents' Figure will be added to the PRIMARY_LAYER.
	 * 
	 * @see org.eclipse.gef3.GraphicalEditPart#getContentPane()
	 */
	public IFigure getContentPane() {
		return getLayer(PRIMARY_LAYER);
	}

	/**
	 * Should not be called, but returns a MarqeeDragTracker for good measure.
	 * 
	 * @see org.eclipse.gef3.EditPart#getDragTracker(org.eclipse.gef3.Request)
	 */
	public DragTracker getDragTracker(Request req) {
		/*
		 * The root will only be asked for a drag tracker if for some reason the
		 * contents EditPart is not selectable or has a non-opaque figure.
		 */
		return new MarqueeDragTracker();
	}

	/**
	 * Returns the layer indicated by the key. Searches all layered panes.
	 * 
	 * @see org.eclipse.gef3.editparts.LayerManager#getLayer(Object)
	 */
	public IFigure getLayer(Object key) {
		if (innerLayers == null)
			return null;
		IFigure layer = scaledLayers.getLayer(key);
		if (layer != null)
			return layer;
		layer = printableLayers.getLayer(key);
		if (layer != null)
			return layer;
		return innerLayers.getLayer(key);
	}

	/**
	 * The root editpart does not have a real model. The LayerManager ID is
	 * returned so that this editpart gets registered using that key.
	 * 
	 * @see org.eclipse.gef3.EditPart#getModel()
	 */
	public Object getModel() {
		return LayerManager.ID;
	}

	/**
	 * Returns the LayeredPane that should be used during printing. This layer
	 * will be identified using {@link LayerConstants#PRINTABLE_LAYERS}.
	 * 
	 * @return the layered pane containing all printable content
	 */
	protected LayeredPane getPrintableLayers() {
		if (printableLayers == null)
			printableLayers = createPrintableLayers();
		return printableLayers;
	}

	/**
	 * Returns the scalable layers of this EditPart
	 * 
	 * @return LayeredPane
	 */
	protected LayeredPane getScaledLayers() {
		if (scaledLayers == null)
			scaledLayers = createScaledLayers();
		return scaledLayers;
	}

	/**
	 * Returns the zoomManager.
	 * 
	 * @return ZoomManager
	 */
	public org.eclipse.gef3.editparts.ZoomManager getZoomManager() {
		return zoomManager;
	}

	/**
	 * Updates the {@link org.eclipse.gef3.editparts.GridLayer grid} based on properties set on the
	 * {@link #getViewer() graphical viewer}:
	 * {@link SnapToGrid#PROPERTY_GRID_VISIBLE},
	 * {@link SnapToGrid#PROPERTY_GRID_SPACING}, and
	 * {@link SnapToGrid#PROPERTY_GRID_ORIGIN}.
	 * <p>
	 * This method is invoked initially when the GridLayer is created, and when
	 * any of the above-mentioned properties are changed on the viewer.
	 */
	protected void refreshGridLayer() {
		boolean visible = false;
		org.eclipse.gef3.editparts.GridLayer grid = (GridLayer) getLayer(GRID_LAYER);
		Boolean val = (Boolean) getViewer().getProperty(
				SnapToGrid.PROPERTY_GRID_VISIBLE);
		if (val != null)
			visible = val.booleanValue();
		grid.setOrigin((Point) getViewer().getProperty(
				SnapToGrid.PROPERTY_GRID_ORIGIN));
		grid.setSpacing((Dimension) getViewer().getProperty(
				SnapToGrid.PROPERTY_GRID_SPACING));
		grid.setVisible(visible);
	}

	/**
	 * @see AbstractEditPart#register()
	 */
	protected void register() {
		super.register();
		getViewer().setProperty(org.eclipse.gef3.editparts.ZoomManager.class.toString(), getZoomManager());
		if (getLayer(GRID_LAYER) != null) {
			getViewer().addPropertyChangeListener(gridListener);
			refreshGridLayer();
		}
	}

	/**
	 * @see AbstractEditPart#unregister()
	 */
	protected void unregister() {
		getViewer().removePropertyChangeListener(gridListener);
		super.unregister();
		getViewer().setProperty(ZoomManager.class.toString(), null);
	}

}
