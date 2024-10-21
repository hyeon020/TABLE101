package table_101;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.JOptionPane;

public class Buisness extends JFrame {
    private List<WaitingInfo> waitingList = new ArrayList<>(); // 웨이팅 대기자 정보를 저장할 리스트
    private List<WaitingInfo> confirmedList = new ArrayList<>(); // 웨이팅 확정자 정보를 저장할 리스트
    private JPanel waitingCustomerPanel; // 웨이팅 대기자 패널
    private JPanel confirmedCustomerPanel; // 웨이팅 확정자 패널

    // WaitingInfo 클래스 정의
    private static class WaitingInfo {
        String userName;
        String userPhone;
        String restaurantName;
        String waitingType;
        int numberOfPeople;
        int WAIT_ID;; // 추가된 필드: 웨이팅 ID

        public WaitingInfo(String userName, String userPhone, String restaurantName, String waitingType, int numberOfPeople, int WAIT_ID) {
            this.userName = userName;
            this.userPhone = userPhone;
            this.restaurantName = restaurantName;
            this.waitingType = waitingType;
            this.numberOfPeople = numberOfPeople;
            this.WAIT_ID = WAIT_ID; // 추가된 필드
        }
    }

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTextField textField_4;
    private JTextField textField_5;
    private JTextField textField_6;
    private String imagePath;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Buisness frame = new Buisness();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */

    
    public Buisness() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 490, 650);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));


        setContentPane(contentPane);
        contentPane.setLayout(null);

        ImageIcon originalIcon = new ImageIcon(Buisness.class.getResource("/table_101/back.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JButton btnNewButton = new JButton("");
        btnNewButton.setBackground(new Color(128, 128, 128));
        btnNewButton.setIcon(scaledIcon);
        btnNewButton.setBounds(10, 8, 40, 40);
        btnNewButton.setBorder(null);
        contentPane.add(btnNewButton);

        btnNewButton.addActionListener(e -> {
            Login loginFrame = new Login();
            loginFrame.setLocation(getLocation());
            loginFrame.setVisible(true);
            dispose();
        });

        JLabel lblNewLabel = new JLabel("TABLE101");
        lblNewLabel.setOpaque(true);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Leelawadee", Font.BOLD, 23));
        lblNewLabel.setBackground(new Color(128, 128, 128));
        lblNewLabel.setBounds(0, 0, 476, 54);
        contentPane.add(lblNewLabel);

        JButton btnNewButton1 = new JButton("매장 등록");
        btnNewButton1.setBackground(new Color(255, 255, 255));
        btnNewButton1.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
        btnNewButton1.setBounds(0, 54, 157, 46);
        btnNewButton1.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(192, 192, 192), new Color(192, 192, 192), new Color(192, 192, 192), new Color(192, 192, 192)));
        contentPane.add(btnNewButton1);

        JButton btnNewButton2 = new JButton("웨이팅 대기자");
        btnNewButton2.setBackground(new Color(255, 255, 255));
        btnNewButton2.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
        btnNewButton2.setBounds(156, 54, 160, 46);
        btnNewButton2.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(192, 192, 192), new Color(192, 192, 192), new Color(192, 192, 192), new Color(192, 192, 192)));
        contentPane.add(btnNewButton2);

        JButton btnNewButton3 = new JButton("웨이팅 확정자");
        btnNewButton3.setBackground(new Color(255, 255, 255));
        btnNewButton3.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
        btnNewButton3.setBounds(315, 54, 161, 46);
        btnNewButton3.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(192, 192, 192), new Color(192, 192, 192), new Color(192, 192, 192), new Color(192, 192, 192)));
        contentPane.add(btnNewButton3);

        JPanel panelContainer = new JPanel();
        panelContainer.setBounds(0, 97, 476, 506);
        contentPane.add(panelContainer);
        panelContainer.setLayout(new CardLayout());

        // 매장 등록 화면
        JPanel storeRegistrationPanel = new JPanel();
        storeRegistrationPanel.setBackground(new Color(255, 255, 255));
        storeRegistrationPanel.setLayout(null);
        storeRegistrationPanel.setPreferredSize(new java.awt.Dimension(460, 800)); 
        JScrollPane scrollPane = new JScrollPane(storeRegistrationPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(0, 0, 476, 506);
        panelContainer.add(scrollPane, "storeRegistration");

        JLabel lblStoreName = new JLabel("매장이름:");
        lblStoreName.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
        lblStoreName.setBounds(79, 42, 100, 30);
        storeRegistrationPanel.add(lblStoreName);

        JLabel lblStorePhone = new JLabel("매장 전화번호:");
        lblStorePhone.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
        lblStorePhone.setBounds(79, 117, 120, 30);
        storeRegistrationPanel.add(lblStorePhone);

        JLabel lblStoreAddress = new JLabel("매장 주소:");
        lblStoreAddress.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
        lblStoreAddress.setBounds(79, 199, 100, 30);
        storeRegistrationPanel.add(lblStoreAddress);

        JLabel lblStoreNotice = new JLabel("매장 공지사항:");
        lblStoreNotice.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
        lblStoreNotice.setBounds(79, 280, 120, 30);
        storeRegistrationPanel.add(lblStoreNotice);

        JLabel lblStoreMenu = new JLabel("메뉴:");
        lblStoreMenu.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
        lblStoreMenu.setBounds(79, 372, 100, 30);
        storeRegistrationPanel.add(lblStoreMenu);
        
        JLabel lblStoreHours = new JLabel("매장 시간:");
        lblStoreHours.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
        lblStoreHours.setBounds(79, 470, 100, 30);
        storeRegistrationPanel.add(lblStoreHours);
        
        JLabel lblStoreImage = new JLabel("매장 이미지:");
        lblStoreImage.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
        lblStoreImage.setBounds(79, 560, 100, 30);
        storeRegistrationPanel.add(lblStoreImage);
        
        textField = new JTextField();
        textField.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(128, 128, 128)));
        textField.setBounds(79, 70, 233, 30);
        storeRegistrationPanel.add(textField);

        textField_1 = new JTextField();
        textField_1.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(128, 128, 128)));
        textField_1.setBounds(79, 148, 233, 30);
        storeRegistrationPanel.add(textField_1);

        textField_2 = new JTextField();
        textField_2.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(128, 128, 128)));
        textField_2.setBounds(79, 229, 233, 30);
        storeRegistrationPanel.add(textField_2);

        textField_3 = new JTextField();
        textField_3.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(128, 128, 128)));
        textField_3.setBounds(79, 319, 233, 30);
        storeRegistrationPanel.add(textField_3);

        textField_4 = new JTextField();
        textField_4.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(128, 128, 128)));
        textField_4.setBounds(79, 413, 233, 30);
        storeRegistrationPanel.add(textField_4);
        
        textField_5 = new JTextField();
        textField_5.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(128, 128, 128)));
        textField_5.setBounds(79, 507, 233, 30);
        storeRegistrationPanel.add(textField_5);
        
        textField_6 = new JTextField();
        textField_6.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(128, 128, 128)));
        textField_6.setBounds(79, 590, 233, 30);
        storeRegistrationPanel.add(textField_6);
        
        JButton btnImageButton = new JButton("이미지 찾기");
		btnImageButton.setForeground(Color.WHITE);
		btnImageButton.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 14));
		btnImageButton.setBackground(new Color(128, 128, 128));
		btnImageButton.setBounds(320, 590, 109, 23);
		storeRegistrationPanel.add(btnImageButton);

		btnImageButton.addActionListener(e -> {

			JFileChooser fileChooser = new JFileChooser();
			int result = fileChooser.showOpenDialog(storeRegistrationPanel);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				imagePath = selectedFile.getAbsolutePath();
				textField_6.setText(imagePath);
			}

		});
        JButton btnNewButton_2 = new JButton("등록하기");
        btnNewButton_2.setForeground(Color.WHITE);
        btnNewButton_2.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 14));
        btnNewButton_2.setBackground(new Color(128, 128, 128));
        btnNewButton_2.setBounds(199, 670, 91, 23);
        storeRegistrationPanel.add(btnNewButton_2);

        // 웨이팅 대기자 패널
        waitingCustomerPanel = new JPanel();
        waitingCustomerPanel.setBackground(new Color(255, 255, 255));
        waitingCustomerPanel.setLayout(new GridBagLayout());
        JScrollPane waitingScrollPane = new JScrollPane(waitingCustomerPanel);
        waitingScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelContainer.add(waitingScrollPane, "waitingCustomer");

        // 웨이팅 확정자 패널
        confirmedCustomerPanel = new JPanel();
        confirmedCustomerPanel.setBackground(new Color(255, 255, 255));
        confirmedCustomerPanel.setLayout(new GridBagLayout());
        JScrollPane confirmedScrollPane = new JScrollPane(confirmedCustomerPanel);
        confirmedScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelContainer.add(confirmedScrollPane, "confirmedCustomer");

        btnNewButton1.addActionListener(e -> {
            CardLayout cl = (CardLayout) (panelContainer.getLayout());
            cl.show(panelContainer, "storeRegistration");
        });

        btnNewButton2.addActionListener(e -> {
            CardLayout cl = (CardLayout) (panelContainer.getLayout());
            cl.show(panelContainer, "waitingCustomer");
            displayWaitingCustomers(); // 대기자 목록 표시
        });

        btnNewButton3.addActionListener(e -> {
            CardLayout cl = (CardLayout) (panelContainer.getLayout());
            cl.show(panelContainer, "confirmedCustomer");
            displayConfirmedCustomers(); // 확정자 목록 표시
        });

        btnNewButton_2.addActionListener(e -> {
            // 등록하기 버튼 클릭 시
            String rest_name = textField.getText();
            String rest_callnumber = textField_1.getText();
            String rest_location = textField_2.getText();
            String rest_inform = textField_3.getText();
            String rest_time = textField_4.getText();
            String rest_menu = textField_5.getText();
            String rest_image = imagePath; // 매장 이미지

            if (rest_name.isEmpty() || rest_callnumber.isEmpty() || rest_callnumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "매장 이름, 전화번호, 주소를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 데이터베이스에 매장 정보를 저장하는 로직 추가
            // 예시로 DB에 저장하는 코드 추가
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "1234");
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO rest VALUES(0, ?, ?, ?, ?, ?, ?, ?, 0, 0, 0, ?);")) {
                stmt.setString(1, rest_name);
                stmt.setString(2, rest_callnumber);
                stmt.setString(3, rest_location);
                stmt.setString(4, rest_inform);
                stmt.setString(5, rest_menu);
                stmt.setString(6, Login.loggedInUserId);
                stmt.setString(7, rest_time);
                stmt.setString(8, imagePath);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "매장이 등록되었습니다.", "등록 성공", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "매장 등록 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }

            // 필드 초기화
            textField.setText("");
            textField_1.setText("");
            textField_2.setText("");
            textField_3.setText("");
            textField_4.setText("");
            textField_5.setText("");
            textField_6.setText("");

            // 대기자 목록 업데이트
            displayWaitingCustomers();
        });
    }

    private void displayWaitingCustomers() {
        waitingCustomerPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        
        // 데이터베이스에서 대기자 목록을 불러옵니다
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT w.WAIT_ID, u.USER_NAME AS userName, u.USER_PHONE AS userPhone, w.WAIT_RESTNAME AS restaurantName,"
                		+ "w.WAIT_CATEGORY AS waitingType, w.WAIT_COUNT AS numberOfPeople, w.WAIT_STATE AS waitingState"
                				+ " FROM WAIT w JOIN USER u ON w.WAIT_USERID = u.USER_ID JOIN REST r ON w.WAIT_RESTNAME = r.REST_NAME"
                				+ " WHERE w.WAIT_STATE = '대기중' AND r.REST_ID = ?")) {
        	stmt.setString(1, Login.loggedInUserId);
        	ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String userName = rs.getString("userName");
                String userPhone = rs.getString("userPhone");
                String restaurantName = rs.getString("restaurantName");
                String waitingType = rs.getString("waitingType");
                int numberOfPeople = rs.getInt("numberOfPeople");
                int WAIT_ID = rs.getInt("WAIT_ID");
                

                WaitingInfo info = new WaitingInfo(userName, userPhone, restaurantName, waitingType, numberOfPeople, WAIT_ID);

                JPanel panel = new JPanel();
                panel.setLayout(new GridBagLayout());
                panel.setBackground(new Color(224, 224, 224));
                panel.setBorder(new MatteBorder(1, 1, 1, 1, Color.WHITE));
                GridBagConstraints gbcPanel = new GridBagConstraints();
                gbcPanel.insets = new Insets(5, 5, 5, 5);
                gbcPanel.anchor = GridBagConstraints.WEST;
                gbcPanel.gridx = 0;
                gbcPanel.gridy = 0;
                gbcPanel.weightx = 1.0;
                gbcPanel.fill = GridBagConstraints.HORIZONTAL;

                // 식당 이름
                JLabel lblRestaurantName = new JLabel(info.restaurantName);
                lblRestaurantName.setFont(new Font("한컴 말랑말랑 Bold", Font.BOLD, 16));
                gbcPanel.gridy++;
                panel.add(lblRestaurantName, gbcPanel);

                // 대기자 정보
                gbcPanel.gridy++;
                panel.add(new JLabel("이름: " + info.userName), gbcPanel);
                gbcPanel.gridy++;
                panel.add(new JLabel("전화번호: " + info.userPhone), gbcPanel);
                gbcPanel.gridy++;
                panel.add(new JLabel("웨이팅 유형: " + info.waitingType), gbcPanel);
                gbcPanel.gridy++;
                panel.add(new JLabel("인원 수: " + info.numberOfPeople), gbcPanel);

                // 버튼 패널
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new GridLayout(2, 1, 3, 5));
                buttonPanel.setBackground(new Color(224, 224, 224));
                JButton btnAccept = new JButton("확정");
                btnAccept.setBackground(new Color(128, 128, 128));
                btnAccept.setForeground(Color.WHITE);
                
                JButton btnReject = new JButton("거절");
                btnReject.setBackground(new Color(128, 128, 128));
                btnReject.setForeground(Color.WHITE);

                btnAccept.addActionListener(e -> {
                    // Move to confirmed list and update status in database
                    confirmWaiting(info.WAIT_ID);
                    displayWaitingCustomers();
                });

                btnReject.addActionListener(e -> {
                    // Remove from waiting list and update status in database
                    rejectWaiting(info.WAIT_ID);
                    displayWaitingCustomers();
                });

                buttonPanel.add(btnAccept);
                buttonPanel.add(btnReject);

                // 버튼 패널을 오른쪽 정렬
                GridBagConstraints gbcButtonPanel = new GridBagConstraints();
                gbcButtonPanel.insets = new Insets(5, 5, 5, 5);
                gbcButtonPanel.gridx = 1; // 오른쪽 열
                gbcButtonPanel.gridy = 3; // 첫 번째 행
                gbcButtonPanel.weightx = 0;
                gbcButtonPanel.weighty = 1.0;
                gbcButtonPanel.fill = GridBagConstraints.VERTICAL;
                gbcButtonPanel.anchor = GridBagConstraints.CENTER;

                panel.add(buttonPanel, gbcButtonPanel);

                waitingCustomerPanel.add(panel, gbc);
                gbc.gridy++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "대기자 목록을 불러오는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }

        waitingCustomerPanel.revalidate();
        waitingCustomerPanel.repaint();
    }



    private void confirmWaiting(int WAIT_ID) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement("UPDATE Wait SET WAIT_STATE = '확정' WHERE WAIT_ID = ?")) {
            stmt.setInt(1, WAIT_ID);
            stmt.executeUpdate();

            // Move to confirmed list
            WaitingInfo info = getWaitingInfo(WAIT_ID);
            if (info != null) {
                waitingList.remove(info);
                confirmedList.add(info);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "대기자 확정 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private WaitingInfo getWaitingInfo(int WAIT_ID) {
		return null;
	}

	private void rejectWaiting(int WAIT_ID) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Wait WHERE WAIT_ID = ?")) {
            stmt.setInt(1, WAIT_ID);
            stmt.executeUpdate();

            // Remove from waiting list
            WaitingInfo info = getWaitingInfo(WAIT_ID);
            if (info != null) {
                waitingList.remove(info);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "대기자 거절 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private WaitingInfo getWaitingInfo1(int WAIT_ID) {
        // Retrieve waiting info from database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Wait WHERE WAIT_ID = ?")) {
            stmt.setInt(1, WAIT_ID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String userName = rs.getString("userName");
                String userPhone = rs.getString("userPhone");
                String restaurantName = rs.getString("restaurantName");
                String waitingType = rs.getString("waitingType");
                int numberOfPeople = rs.getInt("numberOfPeople");
                return new WaitingInfo(userName, userPhone, restaurantName, waitingType, numberOfPeople, WAIT_ID);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void displayConfirmedCustomers() {
        confirmedCustomerPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        
        // 데이터베이스에서 확정자 목록을 불러옵니다
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT w.WAIT_ID, u.USER_NAME AS userName, u.USER_PHONE AS userPhone, w.WAIT_RESTNAME AS restaurantName, " +
                "w.WAIT_CATEGORY AS waitingType, w.WAIT_COUNT AS numberOfPeople, w.WAIT_STATE AS waitingState " +
                "FROM WAIT w JOIN USER u ON w.WAIT_USERID = u.USER_ID JOIN REST r ON w.WAIT_RESTNAME = r.REST_NAME " +
                "WHERE w.WAIT_STATE = '확정'AND r.REST_ID = ?")) {
        	stmt.setString(1, Login.loggedInUserId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String userName = rs.getString("userName");
                String userPhone = rs.getString("userPhone");
                String restaurantName = rs.getString("restaurantName");
                String waitingType = rs.getString("waitingType");
                int numberOfPeople = rs.getInt("numberOfPeople");
                int WAIT_ID = rs.getInt("WAIT_ID");
                
                WaitingInfo info = new WaitingInfo(userName, userPhone, restaurantName, waitingType, numberOfPeople,
						WAIT_ID);
                
                JPanel panel = new JPanel();
                panel.setLayout(new GridBagLayout());
                panel.setBackground(new Color(224, 224, 224));
                panel.setBorder(new MatteBorder(1, 1, 1, 1, Color.WHITE));
                GridBagConstraints gbcPanel = new GridBagConstraints();
                gbcPanel.insets = new Insets(5, 5, 5, 5);
                gbcPanel.anchor = GridBagConstraints.WEST;
                gbcPanel.gridx = 0;
                gbcPanel.gridy = 0;
                gbcPanel.weightx = 1.0;
                gbcPanel.fill = GridBagConstraints.HORIZONTAL;
                
             // 식당 이름
				JLabel lblRestaurantName = new JLabel(restaurantName);
				lblRestaurantName.setFont(new Font("한컴 말랑말랑 Bold", Font.BOLD, 16));
				panel.add(lblRestaurantName, gbcPanel);

			
				// 대기자 정보
				gbcPanel.gridy++;
				panel.add(new JLabel("이름: " + userName), gbcPanel);
				gbcPanel.gridy++;
				panel.add(new JLabel("전화번호: " + userPhone), gbcPanel);
				gbcPanel.gridy++;
				panel.add(new JLabel("웨이팅 유형: " + waitingType), gbcPanel);
				gbcPanel.gridy++;
				panel.add(new JLabel("인원 수: " + numberOfPeople), gbcPanel);
                
             // 종료 버튼 추가
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new GridLayout(2, 1, 3, 5));
				buttonPanel.setBackground(new Color(224, 224, 224));
				JButton btnEnd = new JButton("종료");
				btnEnd.setBackground(new Color(128, 128, 128));
				btnEnd.setForeground(Color.WHITE);
				btnEnd.addActionListener(e -> {
					// Move to confirmed list and update status in database
					endWaiting(info.WAIT_ID, panel);
					displayWaitingCustomers();
				});
				
				buttonPanel.add(btnEnd);

				// 버튼 패널을 오른쪽 정렬
				GridBagConstraints gbcButtonPanel = new GridBagConstraints();
				gbcButtonPanel.insets = new Insets(5, 5, 5, 5);
				gbcButtonPanel.gridx = 1; // 오른쪽 열
				gbcButtonPanel.gridy = 3; // 첫 번째 행
				gbcButtonPanel.weightx = 0;
				gbcButtonPanel.weighty = 1.0;
				gbcButtonPanel.fill = GridBagConstraints.VERTICAL;
				gbcButtonPanel.anchor = GridBagConstraints.CENTER;

				panel.add(buttonPanel, gbcButtonPanel);

				waitingCustomerPanel.add(panel, gbc);
				gbc.gridy++;
				// 패널을 프레임에 추가
				confirmedCustomerPanel.add(panel, gbc);
				gbc.gridy++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "확정자 목록을 불러오는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }

        confirmedCustomerPanel.revalidate();
        confirmedCustomerPanel.repaint();
    }
    private void endWaiting(int WAIT_ID, JPanel panelToRemove) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "1234");
				PreparedStatement stmt = conn.prepareStatement("UPDATE Wait SET WAIT_STATE = '종료' WHERE WAIT_ID = ?")) {
			stmt.setInt(1, WAIT_ID);
			stmt.executeUpdate();
			JOptionPane.showMessageDialog(this, "대기 상태가 종료로 변경되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
			confirmedCustomerPanel.remove(panelToRemove); // 해당 패널 제거
			confirmedCustomerPanel.revalidate(); // 레이아웃 갱신
			confirmedCustomerPanel.repaint(); // 화면 다시 그리기
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "종료 처리 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
		}
	}

}

