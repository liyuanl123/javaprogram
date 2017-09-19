import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContactsDAO {
	private Connection conn;

	public ContactsDAO() {
		conn = DBConnection.getInstance().getConnection();
	}

	public List<Contacts> queryAll() {
		return query("");
	}

	public List<Contacts> query(String sWhere) {
		List<Contacts> list = new ArrayList<Contacts>();
		String sql = "select * from contacts ";
		sql += sWhere;
		System.out.println(sql);
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Contacts tempc = Record2Entity(rs);
				list.add(tempc);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public int insert(Contacts c) {
		String sql = "insert into contacts(name, birthday,telephone,email,remark) values" + "(?,?,?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, c.getName());
			pstmt.setString(2, c.getBirthday());
			pstmt.setString(3, c.getTelephone());
			pstmt.setString(4, c.getEmail());
			pstmt.setString(5, c.getRemark());
			int n = pstmt.executeUpdate();
			System.out.println("insert executeUpdate: " + n);

			if (n > 0) {
				ResultSet rsKey = pstmt.getGeneratedKeys();
				if (rsKey.next()) {
					int autoId = rsKey.getInt(1);
					c.setId(autoId);
					return autoId;
				}
			}
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		return 0;
	}

	public boolean update(Contacts c) {
		return updateById(c, c.getId());
	}

	public boolean updateById(Contacts newContacts, int id) {
		return update(newContacts, " where id=" + id);
	}

	public boolean update(Contacts c, String sWhere) {
		String sql = "update contacts set name = ?,birthday=?, telephone=?,email=?,remark=?";
		sql += sWhere;

		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, c.getName());
			pstmt.setString(2, c.getBirthday());
			pstmt.setString(3, c.getTelephone());
			pstmt.setString(4, c.getEmail());
			pstmt.setString(5, c.getRemark());
			int n = pstmt.executeUpdate();
			pstmt.close();
			return n > 0;
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}

		return false;
	}

	public boolean delete(Contacts c) {
		return deleteById(c.getId());
	}

	public boolean deleteById(int id) {
		return delete(" where id=" + id);
	}

	public boolean delete(String sWhere) {
		String sql = "delete from contacts ";
		sql += sWhere;
		System.out.println(sql);
		try {
			Statement stmt = conn.createStatement();
			int n = stmt.executeUpdate(sql);
			stmt.close();
			return n > 0;
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		return false;
	}

	private Contacts Record2Entity(ResultSet rs) throws SQLException {
		Contacts tempc = new Contacts();
		tempc.setId(rs.getInt("id"));
		tempc.setName(rs.getString("name"));
		tempc.setBirthday(rs.getString("birthday"));
		tempc.setTelephone(rs.getString("telephone"));
		tempc.setEmail(rs.getString("email"));
		tempc.setRemark(rs.getString("remark"));
		return tempc;
	}

	// a Contacts object to carry sql where
	public List<Contacts> queryByFilter(Contacts filter) {
		return query(filterToSqlWhereClause(filter));
	}

	public boolean deleteByFilter(Contacts filter) {
		return delete(filterToSqlWhereClause(filter));
	}

	private String filterToSqlWhereClause(Contacts c) {
		String sql = "";
		String temp;
		temp = c.getName();
		if (temp != null && temp.length() > 0) {
			sql += "and name = '" + temp + "' ";
		}

		temp = c.getBirthday();
		if (temp != null && temp.length() > 0) {
			sql += "and birthday = '" + temp + "' ";
		}

		temp = c.getEmail();
		if (temp != null && temp.length() > 0) {
			sql += "and email = '" + temp + "' ";
		}

		temp = c.getTelephone();
		if (temp != null && temp.length() > 0) {
			sql += "and telephone = '" + temp + "' ";
		}

		temp = c.getRemark();
		if (temp != null && temp.length() > 0) {
			sql += "and remark = '" + temp + "' ";
		}

		if (sql.startsWith("and ")) {
			sql = " where " + sql.substring("and ".length());
		}

		return sql;
	}
}
