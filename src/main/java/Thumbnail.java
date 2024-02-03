import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Thumbnail extends JPanel implements Serializable {
	private static final long serialVersionUID = 1L;
	public final String name;
	public final Icon icon;
	public Color selectedColor;
	private JButton button;

	public Thumbnail(String name) {

		this(name, null);

	}

	public Thumbnail(String name, Icon icon) {
		this.name = name;
		this.icon = icon;
		this.selectedColor = Color.WHITE;

		// Configurar el botón
		button = new JButton("Click Me");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Button clicked for: " + name);
			}
		});

		// Establecer un layout para organizar los componentes
		setLayout(new BorderLayout());
		add(new JLabel(icon, JLabel.CENTER), BorderLayout.CENTER);
		add(new JLabel(name, JLabel.CENTER), BorderLayout.SOUTH);
		add(button, BorderLayout.NORTH);

		// Establecer un tamaño preferido para el Thumbnail
		setPreferredSize(new Dimension(150, 150));
	}
}
