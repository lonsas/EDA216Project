package crusty;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
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
		
		btnSearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					int id = Integer.parseInt(tfId.getText());
					ArrayList<String> pallets = db.getPallet(id);
					taResult.setText("");
					if(pallets!=null)
						for(String pallet:pallets)
							taResult.append(pallet+"\n");
				}catch(NumberFormatException err){
					JOptionPane.showMessageDialog(null, "Please enter a number as Id", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});

		return pane;
	}
}
