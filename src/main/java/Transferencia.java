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

	private final KanbanBoardApp kanbanBoardApp;

	public Transferencia(KanbanBoardApp kanbanBoardApp) {

		super();

		this.kanbanBoardApp = kanbanBoardApp;

		localObjectFlavor = new DataFlavor(Object[].class, "Array of items");

	}

	public Transferencia() {

		super();

		localObjectFlavor = new DataFlavor(Object[].class, "Array of items");

		kanbanBoardApp = null;

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

				}

				else {

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

			}

			catch (UnsupportedFlavorException | IOException e) {

				e.printStackTrace();

			}

		}

		if (source == null) {

			try {

				Object[] values = (Object[]) info.getTransferable().getTransferData(localObjectFlavor);

				for (int i = 0; i < values.length; i++) {

					listModel.add(index + i, (Thumbnail) values[i]);

					target.addSelectionInterval(index + i, index + i);

				}

				return true;

			}

			catch (UnsupportedFlavorException | IOException ex) {

				ex.printStackTrace();

			}

		}

		else {

			try {

				int[] selectedIndices = source.getSelectedIndices();

				int insertIndex = index;

				if (insertIndex == listModel.size()) {

					insertIndex = listModel.size() - 1;

				}

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

								int hasta = 0;

								if (de > insertIndex) {

									diferencia = de - insertIndex;

									listModel.add(insertIndex, origen);

									int sumar = de;

									sumar -= 1;

									insertIndex += sumar;

									listModel.add(insertIndex, listModel.get(insertIndex));

									hasta = de += diferencia;

									if (hasta >= listModel.size()) {

										hasta = listModel.size() - 1;

									}

									listModel.remove(hasta);

								}

								else {

									diferencia = insertIndex - de;

									int inicio = de;

									int hasta2 = insertIndex;

									int borrar = insertIndex + diferencia;

									listModel.add(insertIndex, origen);

									int sumar = de;

									sumar -= 1;

									insertIndex += sumar;

									listModel.add(insertIndex, listModel.get(insertIndex));

									hasta = de += diferencia;

									if (hasta >= listModel.size()) {

										hasta = listModel.size() - 1;

									}

									if (inicio % 2 == 0 && hasta2 % 2 == 0) {

										listModel.add(hasta, dest);

										listModel.remove(inicio);

										if (borrar >= listModel.size()) {

											borrar = listModel.size() - 1;

										}

										listModel.remove(borrar);

									}

									else {

										listModel.remove(de);

										listModel.add(hasta, dest);

										if (borrar >= listModel.size()) {

											borrar = listModel.size() - 1;

										}

										listModel.remove(borrar);

									}

								}

							}

						}

					}

				}

				else {

					// Selecciono mas de 1 elemento

					for (int i = selectedIndices.length - 1; i >= 0; i--) {

						int selectedIndex = selectedIndices[i];

						origen = listModel.get(selectedIndex);

						dest = listModel.get(insertIndex);

						if (selectedIndex < insertIndex) {

							insertIndex--;

						}

					}

				}

			}

			catch (Exception e) {

				e.printStackTrace();

			}

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

		}

	}

}
