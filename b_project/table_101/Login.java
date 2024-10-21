package table_101;

//Login 클래스에서 사용자 정보를 Main 화면으로 전달하는 방법

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Image;

import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Login extends JFrame {

	public static String loggedInUserId;
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
 
	// 사용자 정보를 저장할 변수 추가
	private String userName;
	private String userTelid;
	private String userId;
	private String userPhone;
	private int userPoint;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 490, 650);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("TABLE101");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
     	lblNewLabel.setFont(new Font("Leelawadee", Font.BOLD, 23));
     	lblNewLabel.setForeground(new Color(255, 255, 255));
     	lblNewLabel.setBackground(new Color(255, 87, 132));
	    lblNewLabel.setOpaque(true);
	    lblNewLabel.setBounds(0, 0, 476, 54);
	    contentPane.add(lblNewLabel);

	    textField = new JTextField();
	    textField.setBounds(209, 291, 174, 24);
	    contentPane.add(textField);
	    textField.setColumns(10);

	    textField_1 = new JTextField();
	    textField_1.setBounds(209, 345, 174, 24);
	    contentPane.add(textField_1);
	    textField_1.setColumns(10);

	    ImageIcon originalIcon = new ImageIcon(Login.class.getResource("/table_101/Group1.png"));
	    Image resizedImage = originalIcon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
	    ImageIcon resizedIcon = new ImageIcon(resizedImage);

	    JLabel lblNewLabel_1 = new JLabel();
	    lblNewLabel_1.setIcon(resizedIcon);
	    lblNewLabel_1.setBounds(155, 79, 165, 181);
	    contentPane.add(lblNewLabel_1);
	    
	    lblNewLabel_2 = new JLabel("아이디");
	    lblNewLabel_2.setForeground(new Color(255, 87, 132));
	    lblNewLabel_2.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 17));
	    lblNewLabel_2.setBounds(100, 293, 50, 20);
	    contentPane.add(lblNewLabel_2);

	    lblNewLabel_3 = new JLabel("비밀번호");
	    lblNewLabel_3.setForeground(new Color(255, 87, 132));
	    lblNewLabel_3.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 17));
	    lblNewLabel_3.setBounds(100, 345, 79, 20);
	    contentPane.add(lblNewLabel_3);

	    // Enter key action
	    KeyAdapter enterKeyListener = new KeyAdapter() {
	    	@Override
	    	public void keyPressed(KeyEvent e) {
	    		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	    			handleLogin();
	    		}
	    	}
	    };

	    textField.addKeyListener(enterKeyListener);
     	textField_1.addKeyListener(enterKeyListener);

     	JButton btnNewButton = new JButton("사용자");
     	btnNewButton.setForeground(new Color(255, 255, 255));
     	btnNewButton.setBackground(new Color(255, 87, 132));
     	btnNewButton.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
     	btnNewButton.setBounds(120, 400, 100, 26);
     	btnNewButton.setOpaque(true);
     	contentPane.add(btnNewButton);

     	btnNewButton.addActionListener(e -> {
     		String userId = textField.getText();
     		String password = textField_1.getText();
     		if (authenticateUser(userId, password)) {
     			// Main 프레임으로 사용자 정보 전달
     			Main mainFrame = new Main(userName, loggedInUserId, userPhone, userTelid);
     			mainFrame.setLocation(getLocation());
     			mainFrame.setVisible(true);
     			this.dispose();
     		} else {
     			JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 잘못되었습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
     		}
     	});

     	JButton btnNewButton_1 = new JButton("사업자");
     	btnNewButton_1.setForeground(new Color(255, 255, 255));
     	btnNewButton_1.setBackground(new Color(255, 87, 132));
     	btnNewButton_1.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
     	btnNewButton_1.setBounds(258, 400, 100, 26);
     	btnNewButton_1.setOpaque(true);
     	contentPane.add(btnNewButton_1);

     	btnNewButton_1.addActionListener(e -> {
            String userId = textField.getText();
            String password = textField_1.getText();

            if (authenticateBusiness(userId, password)) {
                Buisness businessFrame = new Buisness();
                businessFrame.setLocation(getLocation());
                businessFrame.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 잘못되었습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
            }
        });

     	JButton btnNewButton_2 = new JButton("회원가입");
     	btnNewButton_2.setForeground(new Color(120, 120, 120));
     	btnNewButton_2.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 14));
     	btnNewButton_2.setBackground(new Color(255, 255, 255));
     	btnNewButton_2.setBounds(200, 468, 91, 23);
     	btnNewButton_2.setBorder(null);
     	contentPane.add(btnNewButton_2);

     	btnNewButton_2.addActionListener(e -> {
     		SignUp signupFrame = new SignUp();
     		signupFrame.setLocation(getLocation());
     		signupFrame.setVisible(true);
     		dispose();
     	});
	}
 
	// 사용자 인증 및 정보 조회 메서드
	private boolean authenticateUser(String userId, String password) {
     	String url = "jdbc:mysql://localhost:3306/project"; // 데이터베이스 URL
     	String user = "root"; // 데이터베이스 사용자 이름
     	String password1 = "1234"; // 데이터베이스 비밀번호

     	String query = "SELECT * FROM USER WHERE USER_ID = ? AND USER_PW = ?";
     
     	try (Connection conn = DriverManager.getConnection(url, user, password1);
     			PreparedStatement pstmt = conn.prepareStatement(query)) {

     		pstmt.setString(1, userId);
     		pstmt.setString(2, password);

     		try (ResultSet rs = pstmt.executeQuery()) {
     			if (rs.next()) {
     				// 사용자 정보 저장
            	 
     				userName = rs.getString("USER_NAME");
     				this.userId = rs.getString("USER_ID");
                	userPhone = rs.getString("USER_PHONE");
                	userPoint = rs.getInt("USER_POINT");
                	userTelid = rs.getString("USER_CHATID");
                	Login.loggedInUserId = userId;
                	return true; // 일치하는 사용자가 있는 경우 로그인 성공
            	}
     		}
     	} catch (SQLException e) {
     		e.printStackTrace();
     	}
     	return false; // 로그인 실패
	}
 
	private boolean authenticateBusiness(String userId, String password) {
		String url = "jdbc:mysql://localhost:3306/project"; 
		String User = "root"; 
		String Password = "1234"; 

		String query = "SELECT * FROM BUISNESS WHERE BUISNESS_ID = ? AND BUISNESS_PW = ?";

		try (Connection conn = DriverManager.getConnection(url, User, Password);
				PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setString(1, userId);
			pstmt.setString(2, password);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					System.out.println("Business login successful for ID: " + userId);
					Login.loggedInUserId = userId; // 로그인된 아이디 정보 저장
					return true; // 일치하는 사업자가 있는 경우 로그인 성공
				} else {
					System.out.println("No matching business found for ID: " + userId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false; // 로그인 실패
	} 
		private void handleLogin() {
			String userId = textField.getText();
			String password = textField_1.getText();
			
			if (userId.equals("admin") && password.equals("1234")) {
				Admin adminFrame = new Admin();
				adminFrame.setLocation(getLocation());
				adminFrame.setVisible(true);
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 잘못되었습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
