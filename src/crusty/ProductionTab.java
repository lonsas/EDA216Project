package crusty;

import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import se.datadosen.component.RiverLayout;

public class ProductionTab extends JPanel {
	private DefaultListModel<String> cookieListModel;
	private JList<String> cookieList;
	private Database db;
	public ProductionTab(Database db) {
		this.db = db;
		cookieListModel = new DefaultListModel<String>();
		cookieList = new JList<String>(cookieListModel);
		cookieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cookieList.setPrototypeCellValue("123456789012");
		JScrollPane cookiePane = new JScrollPane(cookieList);
		JTextField insertNumber = new JTextField(3);

		setLayout(new RiverLayout());
		add("vfill", cookiePane);
		add(new JLabel("Number to produce:"));
		add(insertNumber);
		add(new JButton("Produce"));
	}
	public void fillCookieList() {
		cookieListModel.removeAllElements();
		ArrayList<String> cookies = db.getCookies();
		for(String cookie : cookies) {
			cookieListModel.addElement(cookie);
		}
	}
	
}