package crusty;

import java.awt.*;
import javax.swing.*;
import se.datadosen.component.RiverLayout;

public class Main {

	public static void main(String[] args) {
		new Main();

	}
	public Main() {
		JFrame f = new JFrame("Crusty Cookies");
		JTabbedPane tabbedPane = new JTabbedPane();
		f.getContentPane().add(tabbedPane);
		tabbedPane.addTab("Production", new ProductionTab());

		f.pack();
		f.setVisible(true);
	}

}



