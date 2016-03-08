package crusty;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class Main {

	public static void main(String[] args) {
		new Main();

	}

	public Main() {
		Database db = new Database();
		db.openConnection("db87", "troM54k#");
		JFrame f = new JFrame("Crusty Cookies");
		JTabbedPane tabbedPane = new JTabbedPane();
		f.getContentPane().add(tabbedPane);
		SearchTab searchTab =  new SearchTab(db);
		tabbedPane.addTab("Production", new ProductionTab(db));
		tabbedPane.addTab("Search", new BlockedTab(db, tabbedPane, searchTab));
		tabbedPane.addTab("Show pallet",searchTab);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
	}

}
