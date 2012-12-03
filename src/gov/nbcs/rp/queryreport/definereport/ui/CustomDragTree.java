/**
 * @# DragEx.java    <�ļ���>
 */
package gov.nbcs.rp.queryreport.definereport.ui;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import gov.nbcs.rp.common.BeanUtil;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.common.ui.tree.MyTreeNode;
import gov.nbcs.rp.queryreport.definereport.ibs.IDefineReport;
import com.foundercy.pf.reportcy.common.gui.dnd.TransferableObj;
import com.foundercy.pf.reportcy.common.gui.dnd.TransferableObjTransferable;

/**
 * <p>
 * Title:���϶�������ע�⣺setDragable()�������ã����������ԭʼ���еĲ�����
 * </p>
 * <p>
 * Description: ʵ�ֿ����ϳ����Ķ���Ҫʵ��DragGestureListener��DragSourceListener�����ӿ�
 * ��Ҫ���н�������Ҫʵ��DropTargetListener�ӿ�
 * <p>
 
 */

public class CustomDragTree extends CustomTree implements DragGestureListener,
		DragSourceListener, DropTargetListener {

	private static final long serialVersionUID = -6258496038380632610L;

	BufferedImage ghostImage;

	private Rectangle2D ghostRect = new Rectangle2D.Float();

	private Point ptOffset = new Point();

	private Point lastPoint = new Point();

	private boolean isCanDrag = true;

	public CustomDragTree() throws Exception {
		super();
		init();
	}

	public CustomDragTree(String rootName, DataSet ds, String idName,
			String textName, String parentIdName, SysCodeRule codeRule,
			String sortKey) throws Exception {
		super(rootName, ds, idName, textName, parentIdName, codeRule, sortKey);
		init();
	}

	public CustomDragTree(DataSet ds, String idName, String textName,
			String parentIdName) throws Exception {
		this(null, ds, idName, textName, parentIdName, null, null);
		init();
	}

	public CustomDragTree(DataSet ds, String idName, String textName,
			SysCodeRule codeRule) throws Exception {
		this(null, ds, idName, textName, codeRule);
		init();
	}

	public CustomDragTree(String rootName, DataSet ds, String idName,
			String textName, SysCodeRule codeRule) throws Exception {
		this(rootName, ds, idName, textName, null, codeRule, null);
		init();
	}

	public CustomDragTree(String rootName, DataSet ds, String idName,
			String textName, String parentIdName, SysCodeRule codeRule)
			throws Exception {
		this(rootName, ds, idName, textName, parentIdName, codeRule, null);
		init();
	}

	private void init() {

		DragSource dragSource = DragSource.getDefaultDragSource();

		dragSource.createDefaultDragGestureRecognizer(this, // component
				// where
				// drag originates
				DnDConstants.ACTION_COPY_OR_MOVE, // actions
				this); // drag gesture recognizer

		this.setCellRenderer(new DefaultTreeCellRenderer() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1694289706794112567L;

			public Component getTreeCellRendererComponent(JTree tree,
					Object value, boolean selected, boolean expanded,
					boolean leaf, int row, boolean hasFocus) {

				// setLeafIcon(null);
				// setClosedIcon(null);
				// setOpenIcon(null);
				return super.getTreeCellRendererComponent(tree, value,
						selected, expanded, leaf, row, hasFocus);
			}

		});

		super.setScrollsOnExpand(true);
		new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);

	}

	public void dragGestureRecognized(DragGestureEvent e) {
		// drag anything ...
		if (!isCanDrag)// �����Ҫ�϶��򷵻�
			return;
		TreePath path = getLeadSelectionPath();
		if (path == null)
			return;
		// Work out the offset of the drag point from the TreePath bounding
		// rectangle origin
		Rectangle raPath = getPathBounds(path);
		Point ptDragOrigin = e.getDragOrigin();
		// ȡ���϶�����ʼλ��
		ptOffset.setLocation(ptDragOrigin.x - raPath.x, ptDragOrigin.y
				- raPath.y);
		// Get the cell renderer (which is a JLabel) for the path being
		// dragged
		int row = this.getRowForLocation(ptDragOrigin.x, ptDragOrigin.y);
		JLabel lbl = (JLabel) getCellRenderer().getTreeCellRendererComponent(
				this, // tree
				path.getLastPathComponent(), // value
				false, // isSelected (dont want a colored background)
				isExpanded(path), // isExpanded
				getModel().isLeaf(path.getLastPathComponent()), // isLeaf
				row, // row (not important for rendering)
				false // hasFocus (dont want a focus rectangle)
				);
		lbl.setSize((int) raPath.getWidth(), (int) raPath.getHeight()); // <--
		// The
		// layout
		// manager
		// would
		// normally
		// do
		// this

		// Get a buffered image of the selection for dragging a ghost image
		this.ghostImage = new BufferedImage((int) raPath.getWidth(),
				(int) raPath.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g2 = ghostImage.createGraphics();

		// Ask the cell renderer to paint itself into the BufferedImage
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0.5f));
		// Make the image ghostlike
		lbl.paint(g2);

		g2.dispose();
		// this.getGraphics().drawImage(ghostImage, e.getDragOrigin().x,
		// e.getDragOrigin().y, this);
		// ��ʼ������Ķ���
		try {

			FieldObject fieldObject = new FieldObject();
			BeanUtil.mapping(fieldObject, this.getDataSet().getOriginData());
			TransferableObj transferableObj = new TransferableObj();
			transferableObj.setObject(fieldObject);
			transferableObj.setDataSourceName("ADATABASENAME:"
					+ this.getDataSet().fieldByName(IDefineReport.OBJECT_CNAME)
							.getString());
			transferableObj.setDataSourceDisplaName("���ݿ���:���ݱ���");

			e.startDrag(null, // cursor
					ghostImage, new Point(5, 5),
					new TransferableObjTransferable(transferableObj), // transferable
					this); // drag source listener
		} catch (Exception e1) {
			// TODO �Զ����� catch ��
			e1.printStackTrace();
		}
	}

	public void dragDropEnd(DragSourceDropEvent e) {
		ghostImage = null;
	}

	public void dragEnter(DragSourceDragEvent e) {
	}

	public void dragExit(DragSourceEvent e) {
		// ��������ʱ����ͼ��
		if (!DragSource.isDragImageSupported()) {
			repaint(ghostRect.getBounds());
		}
	}

	public void dragOver(DragSourceDragEvent e) {

	}

	public void dropActionChanged(DragSourceDragEvent e) {
	}

	public Object getFilename() {
		TreePath path = getLeadSelectionPath();
		MyTreeNode node = (MyTreeNode) path.getLastPathComponent();
		return node;
	}

	public void dragEnter(DropTargetDragEvent dtde) {

	}

	public void dragOver(DropTargetDragEvent dtde) {

		Point pt = dtde.getLocation();
		if (pt.equals(lastPoint)) {
			return;
		}
		if (ghostImage != null) {
			Graphics2D g2 = (Graphics2D) getGraphics();
			// If a drag image is not supported by the platform, then draw
			// my
			// own drag image
			if (!DragSource.isDragImageSupported()) {
				paintImmediately(ghostRect.getBounds()); // Rub out the
				// last
				// ghost image and cue
				// line
				// And remember where we are about to draw the new ghost
				// image
				ghostRect.setRect(pt.x - ptOffset.x, pt.y - ptOffset.y,
						ghostImage.getWidth(), ghostImage.getHeight());
				g2.drawImage((ghostImage), AffineTransform
						.getTranslateInstance(ghostRect.getX(), ghostRect
								.getY()), null);
			}
		}

	}

	public void dropActionChanged(DropTargetDragEvent dtde) {

	}

	public void drop(DropTargetDropEvent e) {
		try {
			e.rejectDrop();

		} finally {
			ghostImage = null;
			this.repaint();
		}
	}

	public void dragExit(DropTargetEvent dte) {

	}

	public boolean isCanDrag() {
		return isCanDrag;
	}

	public void setCanDrag(boolean isCanDrag) {
		this.isCanDrag = isCanDrag;
	}

}
