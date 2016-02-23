package crusty;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SearchTab extends JPanel {
	private JTextField tfId;
	private JButton btnSearch;
	private JTextArea taResult;

	private Database db;

	public SearchTab(Database db) {
		this.db = db;

		setLayout(new BorderLayout());

		taResult = new JTextArea();
		taResult.setEditable(false);

		add(topPane(), BorderLayout.NORTH);
		add(new JScrollPane(taResult), BorderLayout.CENTER);
	}

	private JPanel topPane() {
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(1, 2));

		tfId = new JTextField();
		btnSearch = new JButton("Search");

		pane.add(tfId);
		pane.add(btnSearch);

		return pane;
	}
}
