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

package org.eclipse.gef3.examples.text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventObject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2dl.FigureCanvas;
import org.eclipse.draw2dl.PositionConstants;
import org.eclipse.gef3.DefaultEditDomain;
import org.eclipse.gef3.EditDomain;
import org.eclipse.gef3.EditPart;
import org.eclipse.gef3.EditPartFactory;
import org.eclipse.gef3.EditPartViewer;
import org.eclipse.gef3.GraphicalViewer;
import org.eclipse.gef3.MouseWheelHandler;
import org.eclipse.gef3.MouseWheelZoomHandler;
import org.eclipse.gef3.commands.CommandStack;
import org.eclipse.gef3.commands.CommandStackEvent;
import org.eclipse.gef3.commands.CommandStackEventListener;
import org.eclipse.gef3.editparts.ScalableRootEditPart;
import org.eclipse.gef3.examples.text.actions.BooleanStyleAction;
import org.eclipse.gef3.examples.text.actions.MultiStyleAction;
import org.eclipse.gef3.examples.text.actions.StyleService;
import org.eclipse.gef3.examples.text.actions.TextActionConstants;
import org.eclipse.gef3.examples.text.edit.BlockTextPart;
import org.eclipse.gef3.examples.text.edit.ContainerTreePart;
import org.eclipse.gef3.examples.text.edit.DocumentPart;
import org.eclipse.gef3.examples.text.edit.ImportPart;
import org.eclipse.gef3.examples.text.edit.ImportsPart;
import org.eclipse.gef3.examples.text.edit.InlineTextPart;
import org.eclipse.gef3.examples.text.edit.TextFlowPart;
import org.eclipse.gef3.examples.text.edit.TextRunTreePart;
import org.eclipse.gef3.examples.text.model.Block;
import org.eclipse.gef3.examples.text.model.CanvasStyle;
import org.eclipse.gef3.examples.text.model.Container;
import org.eclipse.gef3.examples.text.model.Style;
import org.eclipse.gef3.examples.text.model.TextRun;
import org.eclipse.gef3.examples.text.tools.TextTool;
import org.eclipse.gef3.tools.SelectionTool;
import org.eclipse.gef3.ui.actions.ActionRegistry;
import org.eclipse.gef3.ui.actions.GEFActionConstants;
import org.eclipse.gef3.ui.parts.ContentOutlinePage;
import org.eclipse.gef3.ui.parts.GraphicalEditor;
import org.eclipse.gef3.ui.parts.TreeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * @since 3.1
 */
public class TextEditor extends GraphicalEditor {

	private Container doc;
	private StyleService styleService = new StyleService();

	class TextOutlinePage extends ContentOutlinePage {

		public TextOutlinePage(EditPartViewer viewer) {
			super(new TreeViewer());
			EditDomain domain = new DefaultEditDomain(TextEditor.this);
			domain.setDefaultTool(new SelectionTool());
			domain.loadDefaultTool();
			EditPartViewer treeViewer = getViewer();
			treeViewer.setEditDomain(domain);
			getSelectionSynchronizer().addViewer(treeViewer);
			treeViewer.setEditPartFactory(new EditPartFactory() {
				public EditPart createEditPart(EditPart context, Object model) {
					if (model instanceof Container)
						return new ContainerTreePart(model);
					return new TextRunTreePart(model);
				}
			});
		}

		public void createControl(Composite parent) {
			super.createControl(parent);
			getViewer().setContents(doc);
		}
	}

	public void commandStackChanged(EventObject event) {
		firePropertyChange(PROP_DIRTY);
		super.commandStackChanged(event);
	}

	/**
	 * @see org.eclipse.gef3.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		doc.getStyle().setParentStyle(
				new CanvasStyle(getGraphicalViewer().getControl()));

		getEditDomain().setDefaultTool(new TextTool(styleService));
		getEditDomain().loadDefaultTool();

		getGraphicalViewer().setRootEditPart(new ScalableRootEditPart());
		((FigureCanvas) getGraphicalViewer().getControl()).getViewport()
				.setContentsTracksWidth(true);

		// Scroll-wheel Zoom
		getGraphicalViewer().setProperty(
				MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1),
				MouseWheelZoomHandler.SINGLETON);
	}

	/**
	 * @see org.eclipse.gef3.ui.parts.GraphicalEditor#createActions()
	 */
	protected void createActions() {
		super.createActions();
		IKeyBindingService service = getSite().getKeyBindingService();
		ActionRegistry registry = getActionRegistry();
		IAction action;

		action = new BooleanStyleAction(styleService,
				TextActionConstants.STYLE_BOLD, Style.PROPERTY_BOLD);
		registry.registerAction(action);
		service.registerAction(action);

		action = new BooleanStyleAction(styleService,
				TextActionConstants.STYLE_ITALIC, Style.PROPERTY_ITALIC);
		registry.registerAction(action);
		service.registerAction(action);

		action = new BooleanStyleAction(styleService,
				TextActionConstants.STYLE_UNDERLINE, Style.PROPERTY_UNDERLINE);
		registry.registerAction(action);
		service.registerAction(action);

		action = new MultiStyleAction(styleService,
				TextActionConstants.BLOCK_ALIGN_LEFT, Style.PROPERTY_ALIGNMENT,
				new Integer(PositionConstants.ALWAYS_LEFT));
		registry.registerAction(action);

		action = new MultiStyleAction(styleService,
				TextActionConstants.BLOCK_ALIGN_CENTER,
				Style.PROPERTY_ALIGNMENT, new Integer(PositionConstants.CENTER));
		registry.registerAction(action);

		action = new MultiStyleAction(styleService,
				TextActionConstants.BLOCK_ALIGN_RIGHT,
				Style.PROPERTY_ALIGNMENT, new Integer(
						PositionConstants.ALWAYS_RIGHT));
		registry.registerAction(action);

		action = new MultiStyleAction(styleService,
				TextActionConstants.BLOCK_LTR, Style.PROPERTY_ORIENTATION,
				new Integer(SWT.LEFT_TO_RIGHT));
		registry.registerAction(action);

		action = new MultiStyleAction(styleService,
				TextActionConstants.BLOCK_RTL, Style.PROPERTY_ORIENTATION,
				new Integer(SWT.RIGHT_TO_LEFT));
		registry.registerAction(action);
	}

	/**
	 * @see org.eclipse.gef3.ui.parts.GraphicalEditor#createGraphicalViewer(org.eclipse.swt.widgets.Composite)
	 */
	protected void createGraphicalViewer(Composite parent) {
		GraphicalViewer viewer = new GraphicalTextViewer();
		viewer.createControl(parent);
		setGraphicalViewer(viewer);
		configureGraphicalViewer();
		hookGraphicalViewer();
		initializeGraphicalViewer();
	}

	public Object getAdapter(Class type) {
		if (type == IContentOutlinePage.class)
			return createOutlinePage();
		if (type == StyleService.class)
			return styleService;
		return super.getAdapter(type);
	}

	private IContentOutlinePage createOutlinePage() {
		return new TextOutlinePage(null);
	}

	/**
	 * @see GraphicalEditor#initializeGraphicalViewer()
	 */
	protected void initializeGraphicalViewer() {
		getGraphicalViewer().setEditPartFactory(new EditPartFactory() {
			public EditPart createEditPart(EditPart context, Object model) {
				if (model instanceof Container) {
					switch (((Container) model).getType()) {
					case Container.TYPE_ROOT:
						return new DocumentPart(model);
					case Container.TYPE_IMPORT_DECLARATIONS:
						return new ImportsPart(model);

					case Container.TYPE_COMMENT:
					case Container.TYPE_PARAGRAPH:
						return new BlockTextPart(model);
					case Container.TYPE_INLINE:
						return new InlineTextPart(model);
					default:
						throw new RuntimeException("unknown model type");
					}
				} else if (model instanceof TextRun) {
					switch (((TextRun) model).getType()) {
					case TextRun.TYPE_IMPORT:
						return new ImportPart(model);
					default:
						return new TextFlowPart(model);
					}
				}
				throw new RuntimeException("unexpected model object");
			}
		});

		getGraphicalViewer().setContents(doc);
	}

	/**
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream objStream = new ObjectOutputStream(outputStream);
			objStream.writeObject(doc);
			objStream.close();
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.setContents(
					new ByteArrayInputStream(outputStream.toByteArray()), true,
					false, monitor);
			outputStream.close();
			getCommandStack().markSaveLocation();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	public void doSaveAs() {
	}

	/**
	 * @see GraphicalEditor#init(IEditorSite, IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setEditDomain(new DefaultEditDomain(this));
		getCommandStack().addCommandStackEventListener(
				new CommandStackEventListener() {
					public void stackChanged(CommandStackEvent event) {
						TextCommand command = (TextCommand) event.getCommand();
						if (command != null) {
							GraphicalTextViewer textViewer = (GraphicalTextViewer) getGraphicalViewer();
							if (event.getDetail() == CommandStack.POST_EXECUTE)
								textViewer.setSelectionRange(command
										.getExecuteSelectionRange(textViewer));
							else if (event.getDetail() == CommandStack.POST_REDO)
								textViewer.setSelectionRange(command
										.getRedoSelectionRange(textViewer));
							else if (event.getDetail() == CommandStack.POST_UNDO)
								textViewer.setSelectionRange(command
										.getUndoSelectionRange(textViewer));
						}
					}
				});

		super.init(site, input);

		site.getKeyBindingService().setScopes(
				new String[] { GEFActionConstants.CONTEXT_TEXT });
		site.getActionBars().setGlobalActionHandler(ActionFactory.UNDO.getId(),
				getActionRegistry().getAction(ActionFactory.UNDO.getId()));
		site.getActionBars().setGlobalActionHandler(ActionFactory.REDO.getId(),
				getActionRegistry().getAction(ActionFactory.REDO.getId()));
	}

	/**
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return false;
	}

	protected void setInput(IEditorInput input) {
		super.setInput(input);

		IFile file = ((IFileEditorInput) input).getFile();
		try {
			InputStream is = file.getContents(false);
			ObjectInputStream ois = new ObjectInputStream(is);
			doc = (Container) ois.readObject();
			ois.close();
		} catch (EOFException eofe) {
			// file was empty (as in the case of a new file); do nothing
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (doc == null) {
			doc = new Block(Container.TYPE_ROOT);
			Container preface = new Block(Container.TYPE_PARAGRAPH);
			preface.add(new TextRun("package org.eclipse.gef3.examples.text"));
			doc.add(preface);
			Container imports = new Block(Container.TYPE_IMPORT_DECLARATIONS);
			doc.add(imports);
			imports.add(new TextRun("org.eclipse.draw2dl", TextRun.TYPE_IMPORT));
			imports.add(new TextRun("org.eclipse.gef3", TextRun.TYPE_IMPORT));
			// for (int i = 0; i < 400; i++) {
			Container block = new Block(Container.TYPE_COMMENT);
			block.add(new TextRun(
					"Copyright (c) 2005 IBM Corporation and others. All rights reserved. This program and "
							+ "the accompanying materials are made available under the terms of the Eclipse Public "
							+ "License v1.0 which accompanies this distribution, and is available at "
							+ "http://www.eclipse.org/legal/epl-v10.html (\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329\u7325\u7334\u7329)\r\n"
							+ "Contributors:\n    IBM Corporation - initial API and implementation\n"
							+ "\u0630\u0628\u063a \u0634\u0635\u062c\u062d (Saeed Anwar) - \u0634\u0635\u062c\u062d "
							+ "\u0638\u0635\u0634\u0637\u0635\u0639\u0633 \u0635\u0639\u0633\u0640 \u0630\u0628\u063a (Bug 113700)"));
			doc.add(block);

			Container code = new Block(Container.TYPE_PARAGRAPH);
			code.getStyle().setFontFamily("Courier New");
			doc.add(code);
			code.add(new TextRun(
					"public void countToANumber(int limit) {\n"
							+ "    for (int i = 0; i < limit; i++)\n"
							+ "        System.out.println(\"Counting: \" + i); //$NON-NLS-1$\n\n"
							+ "}", TextRun.TYPE_CODE));
			// }
		}

		setPartName(file.getName());
	}

}