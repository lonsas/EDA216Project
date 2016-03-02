package crusty;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class BlockedTab extends JPanel {
	private JTextField tfFrom;
	private JTextField tfTo;
	private JComboBox<String> cbCookie;
	private JButton btnClear;
	private JButton btnSearch;
	private JTextArea taFound;
	private JButton btnBlock;
	private ArrayList<Integer> pallets;

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

		btnBlock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (pallets != null && !pallets.isEmpty()) {
					int sel = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to block the selected pallets");
					if (sel == 0)
						db.block(pallets);
				} else
					error("Please search for pallets first");

			}
		});

		fillCookieList();
	}

	public void fillCookieList() {
		cbCookie.removeAllItems();
		ArrayList<String> cookies = db.getCookies();
		cbCookie.addItem("All");
		if (cookies != null)
			for (String cookie : cookies)
				cbCookie.addItem(cookie);
	}

	private JPanel topPane() {
		JPanel pane = new JPanel(new GridLayout(2, 4));
		tfFrom = new JTextField();
		tfTo = new JTextField();
		cbCookie = new JComboBox<String>();
		btnSearch = new JButton("Search");
		btnClear = new JButton("Clear");

		pane.add(new JLabel("From:", SwingConstants.CENTER));
		pane.add(tfFrom);
		pane.add(new JLabel("Cookie:", SwingConstants.CENTER));
		pane.add(cbCookie);
		pane.add(new JLabel("To:", SwingConstants.CENTER));
		pane.add(tfTo);
		pane.add(btnClear);
		pane.add(btnSearch);

		btnClear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tfFrom.setText("");
				tfTo.setText("");
				cbCookie.setSelectedIndex(0);

			}
		});

		btnSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String from = tfFrom.getText();
				String to = tfTo.getText();
				if (!from.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d")) {
					error("Wrong format on from date");
					return;
				}
				if (!to.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d")) {
					error("Wrong format on to date");
					return;
				}
				String cookie = (String) cbCookie.getSelectedItem();
				pallets = db.getPallets(from, to, cookie);
				taFound.setText("");
				if (pallets != null)
					for (Integer pallet : pallets)
						taFound.append(pallet + "\n");
			}
		});

		return pane;

	}

	public void error(String s) {
		JOptionPane.showMessageDialog(null, s, "Error", JOptionPane.ERROR_MESSAGE);
	}

}
