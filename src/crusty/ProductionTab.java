package crusty;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
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

		fillCookieList();

		JButton btnProduce = new JButton("Produce");
		add(btnProduce);

		btnProduce.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!cookieList.isSelectionEmpty()) {
					String cookie = cookieList.getSelectedValue();
					try {
						int amount = Integer.parseInt(insertNumber.getText());
						db.produce(cookie, amount);
						String m = amount + " " + cookie + (cookie.length()>6 && cookie.substring(cookie.length()-6).equals("cookie")?"":" cookie") + (amount==1?"":"s") + " will be baked";
						JOptionPane.showMessageDialog(null, m);
					} catch (NumberFormatException err) {
						error("Please enter a number");
					}
				}
				else{
					error("Please select a cookie to produce");
				}
			}
		});
	}

	public void fillCookieList() {
		cookieListModel.removeAllElements();
		ArrayList<String> cookies = db.getCookies();
		for (String cookie : cookies) {
			cookieListModel.addElement(cookie);
		}
	}

	public void error(String s) {
		JOptionPane.showMessageDialog(null, s, "Error", JOptionPane.ERROR_MESSAGE);
	}

}
