package crusty;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import crusty.Database.Pallet;

public class SearchTab extends JPanel {
	private JTextArea taResult;

	private Database db;

	/**
	 * Creates the info tab
	 * @param db The database containing information about the cookies
	 */
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

		JTextField tfId = new JTextField();
		JButton btnSearch = new JButton("Search pallet number");

		pane.add(tfId);
		pane.add(btnSearch);
		
		btnSearch.addActionListener(new ActionListener() {
			
			/**
			 * Searches the database for the selected pallet id
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					int id = Integer.parseInt(tfId.getText());
					search(id);
					
				}catch(NumberFormatException err){
					error("Please enter a number as Id");
				}
				
			}
		});

		return pane;
	}
	
	/**
	 * Searches for an id and shows it in the info tab
	 * @param id The id to search for
	 */
	public void search(int id){
		Pallet pallet = db.getPallet(id);
		taResult.setText("");
		if(pallet!=null)
			taResult.setText(pallet.toString());
		else
			JOptionPane.showMessageDialog(null, "Pallet " + id + " not found");
	}
	
	private void error(String s){
		JOptionPane.showMessageDialog(null, s, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
