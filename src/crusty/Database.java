package crusty;

import java.util.ArrayList;
import java.sql.*;

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
			if (conn != null) {
				conn.close();
			}
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
			while (rs.next()) {
				cookies.add(rs.getString("recipeName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cookies;
	}

	public ArrayList<String> getIngredients() {
		// TODO Auto-generated method stub
		return null;
	}

	public void produce(String cookie, int amount) {
		String produceSQL = "UPDATE ingredient NATURAL JOIN recipeIngredient SET stock=stock-amount*?*54 WHERE recipeName = ?";
		String createPalletSQL = "INSERT INTO pallet " + "SET recipeName = ?, prodDate=NOW()+10*60";
		try {
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(produceSQL);
			ps.setInt(1, amount);
			ps.setString(2, cookie);
			ps.executeUpdate();
			/*ResultSet rs = ps.getGeneratedKeys();
			while (rs.next()) {
				if (rs.getInt(0) < 0) {
					throw new SQLException();
				}
			}*/
			PreparedStatement ps2 = conn.prepareStatement(createPalletSQL);
			ps2.setString(1, cookie);
			ps2.executeUpdate();
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

	public ArrayList<Integer> getPallets(String from, String to, String cookie, String ingredient) {
		String sql = "SELECT palletId FROM pallet " + "WHERE prodDate < ? and prodDate > ? and recipeName = ?";
		ArrayList<Integer> pallets = new ArrayList<Integer>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, to);
			ps.setString(2, from);
			ps.setString(3, cookie);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				pallets.add(rs.getInt("palletId"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pallets;
	}

	public void block(ArrayList<Integer> pallets) {

		String sql = "UPDATE pallet " + "SET blocked=true " + "WHERE palletId IN (?)";
		StringBuilder sb = new StringBuilder();
		for (int palNbr : pallets) {
			sb.append(palNbr);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, sb.toString());
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
			return "Pallet Number: " + palNbr + "\nCookie: " + cookieName + "\nProduction Date: " + prodDate
					+ "\nStatus: " + status + "\nBlocket: " + blocked;
		}

	}

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
