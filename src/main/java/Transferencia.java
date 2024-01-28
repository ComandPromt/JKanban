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
	private final DataFlavor localObjectFlavor;
	private int[] indices;
	private int addIndex = -1;
	private int addCount;
	private final KanbanBoardApp kanbanBoardApp;

	public Transferencia(KanbanBoardApp kanbanBoardApp) {
		super();
		this.kanbanBoardApp = kanbanBoardApp;
		localObjectFlavor = new DataFlavor(Object[].class, "Array of items");
	}

	public Transferencia() {
		super();
		localObjectFlavor = new DataFlavor(Object[].class, "Array of items");
		kanbanBoardApp = null; // Opcional si no se utiliza en el constructor sin argumentos
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
	@Override
	public boolean importData(TransferSupport info) {
		TransferHandler.DropLocation tdl = info.getDropLocation();
		if (!canImport(info) || !(tdl instanceof JList.DropLocation)) {
			return false;
		}

		JList.DropLocation dl = (JList.DropLocation) tdl;
		JList<?> target = (JList<?>) info.getComponent();
		DefaultListModel<Thumbnail> listModel = (DefaultListModel<Thumbnail>) target.getModel();

		int index = dl.getIndex();
		int max = listModel.getSize();
		index = index < 0 ? max : index;
		index = Math.min(index, max);

		// Obtener la lista de origen comparando los modelos de datos
		JList<?> source = null;
		Transferable transferable = info.getTransferable();
		if (transferable.isDataFlavorSupported(localObjectFlavor)) {
			try {
				Object[] transferredObjects = (Object[]) transferable.getTransferData(localObjectFlavor);
				if (transferredObjects.length > 0 && transferredObjects[0] instanceof Thumbnail) {
					JList<?> sourceList = (JList<?>) info.getComponent();
					ListModel<?> sourceModel = sourceList.getModel();
					ListModel<?> targetModel = target.getModel();
					if (sourceModel == targetModel) {
						source = sourceList;
					}
				}
			} catch (UnsupportedFlavorException | IOException e) {
				e.printStackTrace();
			}
		}

		// Verificar si el origen y el destino son la misma lista
		if (source == null) {
			// Importar elementos desde una lista diferente
			try {
				Object[] values = (Object[]) info.getTransferable().getTransferData(localObjectFlavor);
				for (int i = 0; i < values.length; i++) {
					listModel.add(index + i, (Thumbnail) values[i]);
					target.addSelectionInterval(index + i, index + i);
				}
				addCount = values.length;
				return true;
			} catch (UnsupportedFlavorException | IOException ex) {
				ex.printStackTrace();
			}

		}

		else {

			try {

				int[] selectedIndices = source.getSelectedIndices();

				int insertIndex = index;

				int de = selectedIndices[0];

				Thumbnail origen = listModel.get(de);

				Thumbnail dest = listModel.get(insertIndex);

				if (selectedIndices.length == 1) {

					if (de - insertIndex == 1) {

						listModel.add(insertIndex, origen);

						listModel.add(++de, dest);

						listModel.remove(++de);

					}

					else {

						if (insertIndex - de == 1) {

							dest = listModel.get(insertIndex);

							listModel.add(insertIndex, dest);

							listModel.add(++insertIndex, origen);

							listModel.remove(++insertIndex);

						}

						else {

							if (de == insertIndex) {

								listModel.add(de, origen);

							}

							else {

								int diferencia = 0;

								System.out.println("de " + de);

								System.out.println(" a " + insertIndex);

								if (de > insertIndex) {

									diferencia = de - insertIndex;

									listModel.add(insertIndex, origen);

									int sumar = de;

									sumar -= 1;

									insertIndex += sumar;

									listModel.add(insertIndex, listModel.get(insertIndex));

									listModel.remove(de += 2);

								}

								else {

									listModel.add(de, origen);

								}

							}

						}

					}

				}

				else {

					for (int i = selectedIndices.length - 1; i >= 0; i--) {

						int selectedIndex = selectedIndices[i];

						System.out.println("de : " + selectedIndex);

						origen = listModel.get(selectedIndex);

						dest = listModel.get(insertIndex);

						System.out.println("A:" + insertIndex);

						// listModel.add(1, origen);
						// listModel.add(0, dest);
						if (selectedIndex < insertIndex) {

							insertIndex--;

						}

					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Restablecer la selección de la lista de origen
			source.clearSelection();
			return true;
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
				if (source.isSelectedIndex(i)) {
					thumbnail.selectedColor = Color.WHITE;
				}
			}
			source.repaint();

			if (kanbanBoardApp != null) {
				kanbanBoardApp.updateAllUnselectedItemsBackground();
				kanbanBoardApp.checkAndResizePanels();
			}
		}
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
