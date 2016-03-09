package crusty;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com.sun.org.apache.xml.internal.utils.PrefixResolverDefault;

public class BlockedTab extends JPanel {
	private JComboBox<String> cbCookie;
	private DefaultListModel<String> resultListModel;
	private JList<String> jlFound;

	private ArrayList<Integer> pallets;

	private Database db;
	private JTabbedPane tp;
	private SearchTab st;

	/**
	 * Creates the Search tab
	 * @param db The database containing information about the cookies
	 * @param tabbedPane The pane that the tabs are created in
	 * @param searchTab The search tab
	 */
	public BlockedTab(Database db, JTabbedPane tabbedPane, SearchTab searchTab) {
		this.db = db;
		this.tp = tabbedPane;
		this.st = searchTab;

		resultListModel = new DefaultListModel<String>();
		jlFound = new JList<String>(resultListModel);
		jlFound.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlFound.setPrototypeCellValue("123456789012");

		setLayout(new BorderLayout());

		add(topPane(), BorderLayout.NORTH);
		add(new JScrollPane(jlFound), BorderLayout.CENTER);
		add(botPane(), BorderLayout.SOUTH);

		fillCookieList();
	}

	private void fillCookieList() {
		cbCookie.removeAllItems();
		ArrayList<String> cookies = db.getCookies();
		cbCookie.addItem("All");
		if (cookies != null)
			for (String cookie : cookies)
				cbCookie.addItem(cookie);
	}

	private JPanel topPane() {
		JPanel pane = new JPanel(new GridLayout(2, 4));
		JTextField tfFrom = new JTextField();
		JTextField tfTo = new JTextField();
		cbCookie = new JComboBox<String>();
		JButton btnSearch = new JButton("Search");
		JButton btnClear = new JButton("Clear");

		pane.add(new JLabel("From:", SwingConstants.CENTER));
		pane.add(tfFrom);
		pane.add(new JLabel("Cookie:", SwingConstants.CENTER));
		pane.add(cbCookie);
		pane.add(new JLabel("To:", SwingConstants.CENTER));
		pane.add(tfTo);
		pane.add(btnClear);
		pane.add(btnSearch);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());

		tfTo.setText(date);
		tfFrom.setText(date);

		/**
		 * Clears all search fields.
		 */
		btnClear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tfFrom.setText("");
				tfTo.setText("");
				cbCookie.setSelectedIndex(0);

			}
		});

		btnSearch.addActionListener(new ActionListener() {

			/**
			 * Searches for the selected cookies produces between the selected dates.
			 */
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
				if (pallets != null) {
					resultListModel.removeAllElements();
					for (int pallet : pallets) {
						resultListModel.addElement(Integer.toString(pallet));
					}
				}
			}
		});

		return pane;

	}

	private JPanel botPane() {
		JPanel pane = new JPanel(new GridLayout(1, 2));

		JButton btnBlock = new JButton("Block found pallets");
		JButton btnInfo = new JButton("Information about selected pallet");

		pane.add(btnBlock, BorderLayout.SOUTH);
		pane.add(btnInfo);

		btnBlock.addActionListener(new ActionListener() {

			/**
			 * Blocks all found pallets.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				if (pallets != null && !pallets.isEmpty()) {
					int sel = JOptionPane.showConfirmDialog(null,
							"You are about to block "+pallets.size()+" pallets", "Warning", JOptionPane.WARNING_MESSAGE);
					if (sel == 0)
						db.block(pallets);
				} else
					error("Please search for pallets first");

			}
		});

		btnInfo.addActionListener(new ActionListener() {

			/**
			 * Searches the database for the selected pallet id.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!jlFound.isSelectionEmpty()){
					String selected = jlFound.getSelectedValue();
					tp.setSelectedIndex(2);
					st.search(Integer.parseInt(selected));
				}else{
					error("Please select a pallet");
				}
			}
		});

		return pane;
	}

	private void error(String s) {
		JOptionPane.showMessageDialog(null, s, "Error", JOptionPane.ERROR_MESSAGE);
	}

}
