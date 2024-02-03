import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

public class KanbanBoardApp extends JPanel {

	JPanel panel;
	JPanel panel_1;
	JPanel panel_2;
	JPanel panel_3;

	JScrollPane scroll1;
	JScrollPane scroll2;
	JScrollPane scroll3;
	JScrollPane scroll4;

	private JList<Thumbnail> list_1;

	public KanbanBoardApp() {
		setBackground(Color.RED);
		setLayout(new GridLayout(1, 4));

		panel = createSectionPanel("TO DO", 1);
		panel_1 = createSectionPanel("IN PROGRESS", 2);
		panel_2 = createSectionPanel("DONE", 3);
		panel_3 = createSectionPanel("NEW SECTION", 4);

		addScrollablePanel(panel);
		addScrollablePanel(panel_1);
		addScrollablePanel(panel_2);
		addScrollablePanel(panel_3);
	}

	private JPanel createSectionPanel(String title, int numeroPanel) {
		DefaultListModel<Thumbnail> model = new DefaultListModel<>();
		JList<Thumbnail> list = createList(model, title);
		addSampleData(model);

		JPanel sectionPanel = new JPanel();

		sectionPanel.setBackground(Color.BLUE);
		sectionPanel.setLayout(new GridLayout(0, 1, 0, 0));
		sectionPanel.add(new JScrollPane(list));
		sectionPanel.setBorder(null);

		switch (numeroPanel) {
		case 2:
			panel_1 = sectionPanel;
			break;
		case 3:
			panel_2 = sectionPanel;
			break;
		case 4:
			panel_3 = sectionPanel;
			break;
		default:
			panel = sectionPanel;
			break;
		}

		return sectionPanel;

	}

	private void addScrollablePanel(JPanel sectionPanel) {
		JScrollPane scrollPane = new JScrollPane(sectionPanel);
		add(scrollPane);
	}

	private Border createTitledBorder(String title) {
		Border blackLine = BorderFactory.createLineBorder(Color.BLACK);
		Border titledBorder = BorderFactory.createTitledBorder(blackLine, title);
		return new CompoundBorder(titledBorder, BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}

	private JList<Thumbnail> createList(DefaultListModel<Thumbnail> model, String title) {

		JList<Thumbnail> list = new JList<>(model);
		list.setBackground(Color.ORANGE); // Establece el fondo naranja por defecto
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setTransferHandler(new Transferencia(this));
		list.setDropMode(DropMode.INSERT);
		list.setDragEnabled(true);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(0);
		list.setFixedCellWidth(150);
		list.setFixedCellHeight(100);
		list.add(new JLabel("ffff"));

		list.add(new JPanel());
		list.setBorder(createTitledBorder(title));

		list.setCellRenderer(new ListCellRenderer<Thumbnail>() {
			private final JPanel p = new JPanel(new BorderLayout());
			private final JLabel icon = new JLabel((Icon) null, JLabel.CENTER);
			private final JLabel label = new JLabel("", JLabel.CENTER);

			@Override
			public Component getListCellRendererComponent(JList<? extends Thumbnail> list, Thumbnail value, int index,
					boolean isSelected, boolean cellHasFocus) {
				icon.setIcon(value.icon);
				label.setText(value.name);

				// Pinta el fondo blanco para los ítems seleccionados

				// Pinta el fondo naranja para los ítems no seleccionados

				p.setBackground(isSelected ? Color.WHITE : Color.ORANGE);

				label.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

				p.add(add(new Thumbnail("www")));
				p.add(label, BorderLayout.SOUTH);
				return p;
			}
		});

		return list;
	}

	void addSampleData(DefaultListModel<Thumbnail> model) {
		model.addElement(new Thumbnail("Task 1", UIManager.getIcon("OptionPane.errorIcon")));
		model.addElement(new Thumbnail("Task 2", UIManager.getIcon("OptionPane.informationIcon")));
		model.addElement(new Thumbnail("Task 3", UIManager.getIcon("OptionPane.warningIcon")));
		model.addElement(new Thumbnail("Task 4", UIManager.getIcon("OptionPane.questionIcon")));
	}

	public void checkAndResizePanels() {
		checkAndResizePanel(panel);
		checkAndResizePanel(panel_1);
		checkAndResizePanel(panel_2);
		checkAndResizePanel(panel_3);
	}

	void checkAndResizePanel(JPanel sectionPanel) {

		if (sectionPanel.getComponentCount() > 0) {

			JScrollPane scrollPane = (JScrollPane) sectionPanel.getComponent(0);

			JViewport viewport = scrollPane.getViewport();

			if (viewport != null && viewport.getView() instanceof JList) {

				JList<?> list = (JList<?>) viewport.getView();

				if (list.getModel().getSize() == 0) {
					// La lista está vacía, ajustar el tamaño del panel y el JScrollPane
					Dimension panelSize = new Dimension(400, 400);
					sectionPanel.setPreferredSize(panelSize);
					sectionPanel.setMinimumSize(panelSize);
					scrollPane.setPreferredSize(panelSize);
					scrollPane.setMinimumSize(panelSize);
					sectionPanel.revalidate();
					sectionPanel.repaint();
				}
			} else {
				// Si el componente dentro del JScrollPane no es un JList, realiza acciones
				// alternativas si es necesario.
				// Puedes imprimir un mensaje de depuración para identificar qué tipo de
				// componente es.
				System.out.println("El componente dentro del JScrollPane no es un JList.");
			}
		}
	}

	public void updateAllUnselectedItemsBackground() {
		updateUnselectedItemsBackground(panel);
		updateUnselectedItemsBackground(panel_1);
		updateUnselectedItemsBackground(panel_2);
		updateUnselectedItemsBackground(panel_3);
	}

	private void updateUnselectedItemsBackground(JPanel sectionPanel) {
		if (sectionPanel.getComponentCount() > 0) {
			JScrollPane scrollPane = (JScrollPane) sectionPanel.getComponent(0);
			JViewport viewport = scrollPane.getViewport();

			if (viewport != null && viewport.getView() instanceof JList) {
				JList<?> list = (JList<?>) viewport.getView();
				DefaultListModel<?> model = (DefaultListModel<?>) list.getModel();

				for (int i = 0; i < model.getSize(); i++) {
					Thumbnail thumbnail = (Thumbnail) model.getElementAt(i);
					if (!list.isSelectedIndex(i)) {
						thumbnail.selectedColor = Color.ORANGE;
					}
				}

				list.repaint();
			}
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);

		KanbanBoardApp kanbanBoardApp = new KanbanBoardApp();
		frame.getContentPane().add(kanbanBoardApp);
		frame.setVisible(true);
	}
}