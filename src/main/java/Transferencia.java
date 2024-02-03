import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSource;
import java.io.IOException;
import java.util.Objects;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.TransferHandler;

public class Transferencia extends TransferHandler {

	private static final long serialVersionUID = 1L;
	protected DataFlavor localObjectFlavor;
	protected int[] indices;
	protected int addIndex = -1;
	protected int addCount;
	private KanbanBoardApp kanbanBoardApp; // Agrega esta variable

	public Transferencia(KanbanBoardApp kanbanBoardApp) {
		super();
		this.kanbanBoardApp = kanbanBoardApp;
		localObjectFlavor = new DataFlavor(Object[].class, "Array of items");
	}

	public Transferencia() {
		super();
		localObjectFlavor = new DataFlavor(Object[].class, "Array of items");
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		JList<?> source = (JList<?>) c;
		c.getRootPane().getGlassPane().setVisible(true);

		indices = source.getSelectedIndices();
		Object[] transferredObjects = source.getSelectedValuesList().toArray(new Object[0]);
		return new Transferable() {
			@Override
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { localObjectFlavor };
			}

			@Override
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return Objects.equals(localObjectFlavor, flavor);
			}

			@Override
			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
				if (isDataFlavorSupported(flavor)) {
					return transferredObjects;
				} else {
					throw new UnsupportedFlavorException(flavor);
				}
			}
		};
	}

	@Override
	public boolean canImport(TransferSupport info) {
		return info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
	}

	@Override
	public int getSourceActions(JComponent c) {
		c.getRootPane().getGlassPane().setCursor(DragSource.DefaultMoveDrop);
		return MOVE;
	}

	@SuppressWarnings("unchecked")
	public boolean importData(TransferSupport info) {
		TransferHandler.DropLocation tdl = info.getDropLocation();
		if (!canImport(info) || !(tdl instanceof JList.DropLocation)) {
			return false;
		}

		JList.DropLocation dl = (JList.DropLocation) tdl;
		JList target = (JList) info.getComponent();
		DefaultListModel listModel = (DefaultListModel) target.getModel();
		int max = listModel.getSize();
		int index = dl.getIndex();
		index = index < 0 ? max : index;
		index = Math.min(index, max);

		addIndex = index;

		try {
			Object[] values = (Object[]) info.getTransferable().getTransferData(localObjectFlavor);
			for (int i = 0; i < values.length; i++) {
				int idx = index + i; // Fix the calculation of the index
				listModel.add(idx, values[i]);
				target.addSelectionInterval(idx, idx);
			}
			addCount = values.length;
			return true;
		} catch (UnsupportedFlavorException | IOException ex) {
			ex.printStackTrace();
		}

		return false;
	}

	@Override
	protected void exportDone(JComponent c, Transferable data, int action) {
		c.getRootPane().getGlassPane().setVisible(false);
		cleanup(c, action == MOVE);

		if (c instanceof JList) {
			JList<?> source = (JList<?>) c;
			ListModel<?> model = source.getModel();

			for (int i = 0; i < model.getSize(); i++) {
				Thumbnail thumbnail = (Thumbnail) model.getElementAt(i);

				// Pinta el fondo blanco para los ítems seleccionados
				if (source.isSelectedIndex(i)) {
					thumbnail.selectedColor = Color.WHITE;
				}
			}
			source.repaint();

			// Restaura el color de fondo a blanco para todas las tareas
			kanbanBoardApp.updateAllUnselectedItemsBackground();
		}

		// Después de la limpieza, verifica y redimensiona los paneles según sea
		// necesario
		kanbanBoardApp.checkAndResizePanels();
	}

	private void cleanup(JComponent c, boolean remove) {

		if (remove && Objects.nonNull(indices)) {

			if (c instanceof JList) {

				JList<?> source = (JList<?>) c;

				DefaultListModel<?> model = (DefaultListModel<?>) source.getModel();

				for (int i = indices.length - 1; i >= 0; i--) {

					if (indices[i] >= 0 && indices[i] < model.getSize()) {

						model.remove(indices[i]);

					}

				}

			}

			indices = null;

			addCount = 0;

			addIndex = -1;

		}

	}
}