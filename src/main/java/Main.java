
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

@SuppressWarnings("all")

public class Main extends javax.swing.JFrame {

	public Main() throws IOException {

		setTitle("");

		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Dialog", Font.PLAIN, 20));

		menuBar.setBackground(Color.decode("#03A9F4"));

		setJMenuBar(menuBar);

		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Abrir");
		mntmNewMenuItem_3.setHorizontalAlignment(SwingConstants.CENTER);
		mntmNewMenuItem_3.setForeground(Color.WHITE);
		mntmNewMenuItem_3.setFont(new Font("Dialog", Font.PLAIN, 20));

		mntmNewMenuItem_3.setBackground(Color.decode("#03A9F4"));

		menuBar.add(mntmNewMenuItem_3);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Proyecto");
		mntmNewMenuItem_2.setHorizontalAlignment(SwingConstants.CENTER);
		mntmNewMenuItem_2.setForeground(Color.WHITE);
		mntmNewMenuItem_2.setFont(new Font("Dialog", Font.PLAIN, 20));

		mntmNewMenuItem_2.setBackground(Color.decode("#03A9F4"));

		menuBar.add(mntmNewMenuItem_2);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Historia");
		mntmNewMenuItem_1.setHorizontalAlignment(SwingConstants.CENTER);
		mntmNewMenuItem_1.setForeground(Color.WHITE);
		mntmNewMenuItem_1.setFont(new Font("Dialog", Font.PLAIN, 20));

		mntmNewMenuItem_1.setBackground(Color.decode("#03A9F4"));

		menuBar.add(mntmNewMenuItem_1);

		JMenuItem mntmNewMenuItem = new JMenuItem("Equipo");
		mntmNewMenuItem.setHorizontalAlignment(SwingConstants.CENTER);
		mntmNewMenuItem.setForeground(Color.WHITE);
		mntmNewMenuItem.setFont(new Font("Dialog", Font.PLAIN, 20));

		mntmNewMenuItem.setBackground(Color.decode("#03A9F4"));

		menuBar.add(mntmNewMenuItem);

		JMenuItem mntmSobre = new JMenuItem("Exportar");
		mntmSobre.setHorizontalAlignment(SwingConstants.CENTER);
		mntmSobre.setForeground(Color.WHITE);
		mntmSobre.setFont(new Font("Dialog", Font.PLAIN, 20));
		mntmSobre.setBackground(new Color(3, 169, 244));
		menuBar.add(mntmSobre);

		JMenuItem mntmAcercaDe = new JMenuItem("Acerca de");
		mntmAcercaDe.setHorizontalAlignment(SwingConstants.CENTER);
		mntmAcercaDe.setForeground(Color.WHITE);
		mntmAcercaDe.setFont(new Font("Dialog", Font.PLAIN, 20));
		mntmAcercaDe.setBackground(new Color(3, 169, 244));
		menuBar.add(mntmAcercaDe);

		initComponents();

		setVisible(true);

	}

	public static void main(String[] args) {

		try {

			new Main().setVisible(true);

		}

		catch (Exception e) {

		}

	}

	public void initComponents() throws IOException {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

		KanbanBoardApp panel = new KanbanBoardApp();

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(panel, Alignment.TRAILING,
				GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(panel,
				GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE));
		panel.setLayout(new GridLayout(1, 0, 0, 0));

		getContentPane().setLayout(layout);

		setSize(new Dimension(532, 433));

		setLocationRelativeTo(null);

	}

	public void actionPerformed(ActionEvent arg0) {

	}

	public void stateChanged(ChangeEvent e) {

	}
}
