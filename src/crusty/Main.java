package crusty;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class Main {

	public static void main(String[] args) {
		new Main();

	}

	public Main() {
		Database db = new Database();
		JFrame f = new JFrame("Crusty Cookies");
		JTabbedPane tabbedPane = new JTabbedPane();
		f.getContentPane().add(tabbedPane);
		tabbedPane.addTab("Production", new ProductionTab(db));
		tabbedPane.addTab("Block", new BlockedTab(db));
		tabbedPane.addTab("Search", new SearchTab(db));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
	}

}
