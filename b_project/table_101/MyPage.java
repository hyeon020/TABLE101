package table_101;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.border.MatteBorder;
import javax.swing.SwingConstants;
import java.awt.Component;
import javax.swing.ListCellRenderer;
import java.awt.BorderLayout;

// WaitingItem 클래스 정의
class WaitingItem {
	private ImageIcon imageIcon;
	private String text;

	public WaitingItem(ImageIcon imageIcon, String text) {
		this.imageIcon = imageIcon;
		this.text = text;
	}

	public ImageIcon getImageIcon() {
		return imageIcon;
	}

	public String getText() {
		return text;
	}
}

// ImageTextRenderer 클래스 정의
class ImageTextRenderer extends JPanel implements ListCellRenderer<WaitingItem> {
	private JLabel imageLabel;
	private JLabel textLabel;

	public ImageTextRenderer() {
		setLayout(new BorderLayout(40, 10));
		imageLabel = new JLabel();
		textLabel = new JLabel();

		textLabel.setFont(new Font("나눔고딕", Font.PLAIN, 16));
		textLabel.setText("<html><body style='width: 200px'>");
		add(imageLabel, BorderLayout.WEST);
		add(textLabel, BorderLayout.CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends WaitingItem> list, WaitingItem value, int index,
			boolean isSelected, boolean cellHasFocus) {
		imageLabel.setIcon(value.getImageIcon());
		textLabel.setText("<html>" + value.getText() + "</html>");

		if (isSelected) {
			setBackground(Color.white);
		} else {
			setBackground(list.getBackground());
		}

		return this;
	}
}

public class MyPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel userInfoLabel; // 사용자 정보를 표시할 라벨
	private JList<WaitingItem> waitingList; // 웨이팅 리스트
	private JList<WaitingItem> pastWaitingList; // 이전 웨이팅 리스트
	private DefaultListModel<WaitingItem> listModel; // 웨이팅 리스트 모델
	private DefaultListModel<WaitingItem> pastListModel; // 이전 웨이팅 리스트 모델

	// 사용자 정보를 저장할 변수 추가
	private String loggedInUserId;
	private String userName;
	private String userTelid;
	private String userId;
	private String userPhone;
	private int userPoint;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				MyPage frame = new MyPage(null, null, null, null);
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MyPage(String userName, String loggedInUserId, String userPhone, String userTelid) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 490, 650); // 창 크기를 늘림
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Back Button
		ImageIcon originalIcon = new ImageIcon(MyPage.class.getResource("/table_101/back.png"));
		Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		JButton btnBack = new JButton(new ImageIcon(scaledImage));
		btnBack.setBackground(new Color(255, 87, 132));
		btnBack.setBounds(10, 8, 40, 40);
		btnBack.setBorder(null);
		contentPane.add(btnBack);

		btnBack.addActionListener(e -> {
			Main mainFrame = new Main(userName, loggedInUserId, userPhone, userTelid);
			mainFrame.setLocation(getLocation());
			mainFrame.setVisible(true);
			dispose(); // 현재 창을 닫고 Main 화면으로 전환
		});

		// Title Label
		JLabel lblTitle = new JLabel("TABLE101");
		lblTitle.setOpaque(true);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(new Font("Leelawadee", Font.BOLD, 23));
		lblTitle.setBackground(new Color(255, 87, 132));
		lblTitle.setBounds(0, 0, 476, 54);
		contentPane.add(lblTitle);

		// 새로고침 버튼 추가
	       ImageIcon refreshIcon = new ImageIcon(MyPage.class.getResource("/table_101/RE.png"));
	       Image refreshImage = refreshIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
	       JButton btnRefresh = new JButton(new ImageIcon(refreshImage));
	       btnRefresh.setBackground(new Color(255, 87, 132));
	       btnRefresh.setBounds(430, 10, 30, 30); // 위치 조정 (라벨 오른쪽 끝)
	       btnRefresh.setBorder(null);
	       contentPane.add(btnRefresh);

	       // 새로고침 버튼 동작 정의
	       btnRefresh.addActionListener(e -> {
	           dispose(); // 현재 창 닫기
	           MyPage refreshedPage = new MyPage(userName, loggedInUserId, userPhone, userTelid); // 새 창 열기
	           refreshedPage.setLocation(getLocation());
	           refreshedPage.setVisible(true); // 새로고침 효과
	       });

	       
		// '마이페이지' 라벨 추가
		JLabel lblMyPage = new JLabel("마이페이지");
		lblMyPage.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 16));
		lblMyPage.setBounds(25, 74, 100, 24);
		contentPane.add(lblMyPage);

		// 사용자 이미지 추가
	      ImageIcon profileIcon = new ImageIcon(getClass().getResource("/table_101/user.png")); // 사용자 이미지 경로
	      Image profileImage = profileIcon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH); // 이미지 크기 조정
	      JLabel profileLabel = new JLabel(new ImageIcon(profileImage));
	      profileLabel.setBounds(25, 110, 90, 90); // 위치 설정
	      contentPane.add(profileLabel);
	      
		 // Fetch user point from database before showing
        int userPoint = fetchUserPointData(loggedInUserId); // userPoint를 데이터베이스에서 가져오기
		
		// User Info Label
		userInfoLabel = new JLabel("<html>" + loggedInUserId + "<br>이름: " + userName + "<br>전화번호: " + userPhone
				+ "<br>포인트: " + userPoint + "</html>");
		userInfoLabel.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 18));
		userInfoLabel.setBounds(140, 100, 400, 100);
		contentPane.add(userInfoLabel);

		// Waiting List Label
		JLabel lblWaitingStatus = new JLabel("웨이팅 등록 현황");
		lblWaitingStatus.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 16));
		lblWaitingStatus.setBounds(25, 220, 170, 24);
		contentPane.add(lblWaitingStatus);

		// Create JList with a DefaultListModel for current waiting list
		listModel = new DefaultListModel<>();
		waitingList = new JList<>(listModel);
		waitingList.setBorder(new MatteBorder(1, 0, 1, 0, new Color(128, 128, 128)));
		waitingList.setFixedCellHeight(130);
		waitingList.setCellRenderer(new ImageTextRenderer());

		JScrollPane waitingScrollPane = new JScrollPane(waitingList);
		waitingScrollPane.setBounds(0, 254, 476, 135);
		contentPane.add(waitingScrollPane);

		// 이전 웨이팅 내역 라벨 추가
		JLabel lblPastWaitingStatus = new JLabel("이전 웨이팅 내역");
		lblPastWaitingStatus.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 16));
		lblPastWaitingStatus.setBounds(25, 414, 170, 24);
		contentPane.add(lblPastWaitingStatus);

		// Create JList with a DefaultListModel for past waiting list
		pastListModel = new DefaultListModel<>();
		pastWaitingList = new JList<>(pastListModel);
		pastWaitingList.setBorder(new MatteBorder(1, 0, 1, 0, new Color(128, 128, 128)));
		pastWaitingList.setFixedCellHeight(130);
		pastWaitingList.setCellRenderer(new ImageTextRenderer());

		JScrollPane pastWaitingScrollPane = new JScrollPane(pastWaitingList);
		pastWaitingScrollPane.setBounds(0, 448, 476, 135);
		contentPane.add(pastWaitingScrollPane);

		// Fetch waiting data from database
		fetchWaitingData(loggedInUserId);
	}

	private void fetchWaitingData(String loggedInUserId) {
		String url = "jdbc:mysql://localhost:3306/project"; // 데이터베이스 URL
		String user = "root"; // DB 사용자명
		String password = "1234"; // DB 비밀번호

		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			String sql = "SELECT WAIT.WAIT_NUMBER, WAIT.WAIT_RESTNAME, WAIT.WAIT_CATEGORY, WAIT.WAIT_STATE, REST.REST_NAME, REST.REST_IMAGE "
					+ "FROM WAIT INNER JOIN REST ON WAIT.WAIT_RESTNAME = REST.REST_NAME "
					+ "WHERE WAIT.WAIT_USERID = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, loggedInUserId); // userId를 쿼리에 설정

			ResultSet rs = pstmt.executeQuery();

			// 데이터 표시
			while (rs.next()) {
				int waitNumber = rs.getInt("WAIT_NUMBER");
				String waitName = rs.getString("WAIT_RESTNAME");
				String waitState = rs.getString("WAIT_STATE");
				String waitCategory = rs.getString("WAIT_CATEGORY");
				String restName = rs.getString("REST_NAME");
				String restImagePath = rs.getString("REST_IMAGE");

				// 이미지 로드
				ImageIcon restImage = new ImageIcon(restImagePath);
				Image scaledImage = restImage.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
				restImage = new ImageIcon(scaledImage);

				// WaitingItem 생성하여 리스트 모델에 추가
				String itemText = "<strong><span style='font-size: 17px;'>" + waitName + "</span></strong><br>나의순서: "
						+ waitNumber + "<br>유형: " + waitCategory + "<br>상태: " + waitState;
				String itemText1 = "<strong><span style='font-size: 17px;'>" + waitName + "</span></strong><br>"
						+ "<br>유형: " + waitCategory;
				WaitingItem item = new WaitingItem(restImage, itemText);
				WaitingItem item1 = new WaitingItem(restImage, itemText1);

				// 상태에 따라 리스트에 추가
				if ("대기중".equals(waitState) || "확정".equals(waitState)) {
					listModel.addElement(item);
				} else {
					pastListModel.addElement(item1);
				}
			}
			// 연결 종료
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// 사용자 포인트를 다시 불러오는 메서드
    private int fetchUserPointData(String loggedInUserId) {
        int point = 0;
        String url = "jdbc:mysql://localhost:3306/project"; // 데이터베이스 URL
        String user = "root"; // DB 사용자명
        String password = "1234"; // DB 비밀번호

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT USER_POINT FROM USER WHERE USER_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loggedInUserId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                point = rs.getInt("USER_POINT");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return point;
    }
}
