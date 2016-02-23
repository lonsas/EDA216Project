package crusty;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class BlockedTab extends JPanel {
	private JTextField tfFrom;
	private JTextField tfTo;
	private JComboBox<String> cbIngredient;
	private JComboBox<String> cbCookie;
	private JButton btnClear;
	private JButton btnSearch;
	private JTextArea taFound;
	private JButton btnBlock;

	private Database db;

	public BlockedTab(Database db) {
		this.db = db;

		taFound = new JTextArea();
		btnBlock = new JButton("Block selected pallets");

		taFound.setEditable(false);

		setLayout(new BorderLayout());

		add(topPane(), BorderLayout.NORTH);
		add(new JScrollPane(taFound), BorderLayout.CENTER);
		add(btnBlock, BorderLayout.SOUTH);

	}

	public void fillCookieList() {
		cbCookie.removeAllItems();
		ArrayList<String> cookies = db.getCookies();
		for (String cookie : cookies)
			cbCookie.addItem(cookie);
	}

	public void fillIngredientList() {
		cbIngredient.removeAllItems();
		ArrayList<String> cookies = db.getIngredients();
		for (String cookie : cookies)
			cbIngredient.addItem(cookie);
	}

	private JPanel topPane() {
		JPanel pane = new JPanel(new GridLayout(2, 5));
		tfFrom = new JTextField();
		tfTo = new JTextField();
		cbIngredient = new JComboBox<String>();
		cbCookie = new JComboBox<String>();
		btnSearch = new JButton("Search");
		btnClear = new JButton("Clear");

		pane.add(new JLabel("From:", SwingConstants.CENTER));
		pane.add(tfFrom);
		pane.add(new JLabel("Ingredient:", SwingConstants.CENTER));
		pane.add(cbIngredient);
		pane.add(btnClear);
		pane.add(new JLabel("To:", SwingConstants.CENTER));
		pane.add(tfTo);
		pane.add(new JLabel("Cookie:", SwingConstants.CENTER));
		pane.add(cbCookie);
		pane.add(btnSearch);

		return pane;

	}

}
