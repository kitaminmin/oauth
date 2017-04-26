/**
 *
 */
package com.internousdev.oauth.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.internousdev.oauth.dto.UsersDTO;
import com.internousdev.util.DBConnector;

/**
 * @author internous
 *
 */
public class LoginOauthDAO {

	private UsersDTO dto = new UsersDTO();

	public boolean select(String userUniqueId, int oauthName) {
		System.out.println("dao");
		DBConnector db = new DBConnector("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/", "openconnect", "root","mysql");
		Connection con = null;

		System.out.println("接続OK");
		boolean result = false;
		con = db.getConnection();

		try {
			String sql = "SELECT * FROM users WHERE oauth_id = ? AND oauth_name = ?";
			System.out.println(sql);
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, userUniqueId);
			System.out.println(userUniqueId);
			stmt.setInt(2, oauthName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				dto.setUserId(rs.getInt("user_id"));
				dto.setLoginFlg(rs.getBoolean("login_flg"));
				dto.setOauthId(rs.getString("oauth_id"));
				dto.setOauthAccount(rs.getString("oauth_account"));

				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public boolean insert(String uniqueId, String userName, int oauthName) {
		DBConnector db = new DBConnector("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/", "openconnect", "root",
				"mysql");
		Connection con = null;
		boolean result = false;
		con = db.getConnection();
		String sql = "INSERT INTO users"
				+ "(oauth_account, oauth_id, oauth_name, family_name, given_name, family_name_kanji,"
				+ "family_name_kana,given_name_kanji, given_name_kana, sex)" + " values" + " (?,?,?,?,?,?,?,?,?,?)";

		System.out.println("sql22222");
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userName);
			ps.setString(2, uniqueId);
			ps.setInt(3, oauthName);
			ps.setString(4, "");
			ps.setString(5, "");
			ps.setString(6, "");
			ps.setString(7, "");
			ps.setString(8, "");
			ps.setString(9, "");
			ps.setString(10, "");

			int insertCount = ps.executeUpdate();
			if (insertCount > 0) {
				result = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// 多重ログインを防ぐため、ログインフラグをTrueにUPDATEをおこなう。
	public int update(String uniqueId) {
		int count = 0;
		DBConnector db = new DBConnector("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/", "openconnect", "root",
				"mysql");
		Connection con = db.getConnection();
		String sql = "UPDATE users SET login_flg = true WHERE oauth_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, uniqueId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	public UsersDTO getUsersDTO() {
		return dto;
	}

	public void setLoginOauthDTO(UsersDTO dto) {
		this.dto = dto;
	}
}
