package table_101;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;


public class UserMgr {
	private DBConnectionMgr pool;
	
    public UserMgr() {
        pool = DBConnectionMgr.getInstance();
    }
	
    // 로그인
	public UserBean getUser(String user_id, String user_pw) {
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = null;
	    UserBean bean = null;
	    try {
	        con = pool.getConnection();
	        sql = "SELECT * FROM user WHERE user_id = ? AND user_pw = ?";
	        pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, user_id);
	        pstmt.setString(2, user_pw);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	        	bean = new UserBean();
	           bean.setUser_id(rs.getString(1));
	           bean.setUser_pw(rs.getString(2));
	           bean.setUser_name(rs.getString(3));
	           bean.setUser_point(rs.getInt(4));
	           bean.setUser_phone(rs.getString(5));
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        pool.freeConnection(con, pstmt, rs);
	    }
	    return bean; 
	}
	
	// 회원가입
	public boolean insertUser(UserBean bean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "insert into user values(?,?,?,?,10000,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, bean.getUser_id());
			pstmt.setString(2, bean.getUser_telid());
			pstmt.setString(3, bean.getUser_pw());
			pstmt.setString(4, bean.getUser_name());
			pstmt.setString(5, bean.getUser_phone());
			int cnt = pstmt.executeUpdate();
			if(cnt==1) flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return flag;
	}
	
	// 중복 확인
	public boolean isUserIdExists(String user_id) {
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = null;
	    try {
	        con = pool.getConnection();
	        sql = "SELECT COUNT(*) FROM user WHERE user_id = ?";
	        pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, user_id);
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	            int count = rs.getInt(1);
	            return count > 0; // ID가 존재하면 true 반환
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        pool.freeConnection(con, pstmt, rs);
	    }
	    return false;
	}

}
