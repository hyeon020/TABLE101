package table_101;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;


public class BuisnessMgr {
	private DBConnectionMgr pool;
	
    public BuisnessMgr() {
        pool = DBConnectionMgr.getInstance();
    }
	
    // 로그인
	public BuisnessBean getBuisness(String buisness_id, String buisness_pw) {
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = null;
	    BuisnessBean bean = null;
	    try {
	        con = pool.getConnection();
	        sql = "SELECT * FROM buisness WHERE buisness_id = ? AND buisness_pw = ?";
	        pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, buisness_id);
	        pstmt.setString(2, buisness_pw);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	        	bean = new BuisnessBean();
	           bean.setBuisness_id(rs.getString(1));
	           bean.setBuisness_name(rs.getString(2));
	           bean.setBuisness_pw(rs.getString(3));
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        pool.freeConnection(con, pstmt, rs);
	    }
	    return bean; 
	}

	// 회원가입
		public boolean insertBuisness(BuisnessBean bean) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = null;
			boolean flag = false;
			try {
				con = pool.getConnection();
				sql = "insert into buisness values(?,?,?)";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, bean.getBuisness_id());
				pstmt.setString(2, bean.getBuisness_name());
				pstmt.setString(3, bean.getBuisness_pw());
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
		public boolean isBuisnessIdExists(String buisness_id) {
		    Connection con = null;
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;
		    String sql = null;
		    try {
		        con = pool.getConnection();
		        sql = "SELECT COUNT(*) FROM buisness WHERE buisness_id = ?";
		        pstmt = con.prepareStatement(sql);
		        pstmt.setString(1, buisness_id);
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
