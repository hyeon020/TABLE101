package table_101;

import java.awt.Checkbox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.JButton;

public class AdminMgr {
    private DBConnectionMgr pool;

    public AdminMgr() {
        pool = DBConnectionMgr.getInstance();
    }

    // 사용자 목록 가져오기
    public Vector<Vector<Object>> getUserList() {
        Vector<Vector<Object>> userList = new Vector<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT user_id, user_chatid, user_name, user_point, user_phone FROM user";

        try {
            con = pool.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Vector<Object> user = new Vector<>();
                user.add(rs.getString("user_id"));
                user.add(rs.getString("user_chatid"));
                user.add(rs.getString("user_name"));
                user.add(rs.getInt("user_point"));
                user.add(rs.getString("user_phone"));
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(con, pstmt, rs);
        }
        return userList;
    }

    // 사업자 목록 가져오기
    public Vector<Vector<Object>> getBusinessList() {
        Vector<Vector<Object>> businessList = new Vector<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT buisness_id, buisness_name, buisness_pw FROM buisness";

        try {
            con = pool.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Vector<Object> business = new Vector<>();
                business.add(rs.getString("buisness_id"));
                business.add(rs.getString("buisness_name"));
                business.add(rs.getString("buisness_pw"));
                businessList.add(business);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(con, pstmt, rs);
        }
        return businessList;
    }

    // 매장 등록 대기 목록 가져오기
    public Vector<Vector<Object>> getStoreApprovalList() {
        Vector<Vector<Object>> storeList = new Vector<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT REST_NAME, REST_CALLNUMBER, REST_LOCATION, REST_TIME FROM REST";

        try {
            con = pool.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Vector<Object> store = new Vector<>();
                store.add(rs.getString("REST_NAME"));
                store.add(rs.getString("REST_LOCATION"));
                store.add(rs.getString("REST_CALLNUMBER"));
                //store.add();
                storeList.add(store);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(con, pstmt, rs);
        }
        return storeList;
    }
}
