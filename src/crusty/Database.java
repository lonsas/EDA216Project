package crusty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database {
	private Connection conn;

	public Database() {
		conn = null;
	}

	/**
	 * Open a connection to the database, using the specified user name and
	 * password.
	 * 
	 * @param userName
	 *            The user name.
	 * @param password
	 *            The user's password.
	 * @return true if the connection succeeded, false if the supplied user name
	 *         and password were not recognized. Returns false also if the JDBC
	 *         driver isn't found.
	 */
	public boolean openConnection(String userName, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://puccini.cs.lth.se/" + userName, userName, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * Close the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
		}
		conn = null;
	}

	/**
	 * Check if the connection to the database has been established
	 * 
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	public ArrayList<String> getCookies() {
		String sql = "SELECT recipeName FROM recipe";
		ArrayList<String> cookies = new ArrayList<String>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				cookies.add(rs.getString("recipeName"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cookies;
	}

	/**
	 * Produces pallets of cookies
	 * @param cookie name of cookie to produces
	 * @param amount number of pallets to produce
	 */
	public void produce(String cookie, int amount) {
		String produceSQL = "UPDATE ingredient NATURAL JOIN recipeIngredient SET stock=stock-amount*?*54 WHERE recipeName = ?";
		String createPalletSQL = "INSERT INTO pallet " + "SET recipeName = ?, prodDate=NOW()+10*60";
		try {
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(produceSQL);
			ps.setInt(1, amount);
			ps.setString(2, cookie);
			ps.executeUpdate();
			/*
			 * ResultSet rs = ps.getGeneratedKeys(); while (rs.next()) { if
			 * (rs.getInt(0) < 0) { throw new SQLException(); } }
			 */
			PreparedStatement ps2 = conn.prepareStatement(createPalletSQL);
			for(int i = 0; i < amount; i++) {
				ps2.setString(1, cookie);
				ps2.addBatch();
			}
			ps2.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Searches for pallets
	 * @param from From date
	 * @param to To date
	 * @param cookie cookie type to search for
	 * @return List of palletIds
	 */
	public ArrayList<Integer> getPallets(String from, String to, String cookie) {
		if (cookie.equals("All"))
			cookie = "%";
		String sql = "SELECT palletId FROM pallet " + "WHERE prodDate < DATE_ADD(?,INTERVAL 1 DAY) and prodDate >= ? and recipeName LIKE ?";
		ArrayList<Integer> pallets = new ArrayList<Integer>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, to);
			ps.setString(2, from);
			ps.setString(3, cookie);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				pallets.add(rs.getInt("palletId"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pallets;
	}

	/**
	 * @param pallets Pallets to be blocked
	 */
	public void block(ArrayList<Integer> pallets) {

		StringBuilder sql = new StringBuilder("UPDATE pallet " + "SET blocked=true " + "WHERE palletId IN (");
		for (int i = 0; i < pallets.size(); i++) {
			sql.append('?');
			sql.append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(')');

		try {
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < pallets.size(); i++) {
				ps.setInt(i+1, pallets.get(i));
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public class Pallet {
		private int palNbr;
		private String cookieName;
		private String prodDate;
		private String status;
		private Boolean blocked;

		public int getPalNbr() {
			return palNbr;
		}

		public void setPalNbr(int palNbr) {
			this.palNbr = palNbr;
		}

		public String getCookieName() {
			return cookieName;
		}

		public void setCookieName(String cookieName) {
			this.cookieName = cookieName;
		}

		public String getProdDate() {
			return prodDate;
		}

		public void setProdDate(String prodDate) {
			this.prodDate = prodDate;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public Boolean getBlocked() {
			return blocked;
		}

		public void setBlocked(Boolean blocked) {
			this.blocked = blocked;
		}

		public String toString() {
			return "Pallet Number: " + palNbr + "\nCookie: " + cookieName + "\nProduction Time: " + prodDate
					+ "\nStatus: " + status + "\n"+ ((blocked)?"Blocked":"Not blocked");
		}

	}

	/**
	 * Get pallet by palletId
	 * @param palNbr palletId
	 * @return
	 */
	public Pallet getPallet(int palNbr) {
		String sql = "SELECT * FROM pallet WHERE palletId = ?";
		Pallet pallet = null;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, palNbr);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				pallet = new Pallet();
				pallet.setCookieName(rs.getString("recipeName"));
				pallet.setPalNbr(palNbr);
				pallet.setProdDate(rs.getString("prodDate"));
				pallet.setStatus(rs.getString("status"));
				pallet.setBlocked(rs.getBoolean("blocked"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pallet;
	}

}
