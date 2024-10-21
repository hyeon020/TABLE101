package table_101;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import table_101.Main.Restaurant;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Waiting extends JFrame {

    private static final long serialVersionUID = 1L;
    private String userId;
    private String userTelid;
    private Restaurant restaurant;
    
    public Waiting(Restaurant restaurant, String userId, String userTelid) {
    	this.restaurant = restaurant;
    	this.userId = userId;
    	this.userTelid = userTelid;
    	
        setTitle("웨이팅 등록");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 600, 450);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setBackground(Color.WHITE); 
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 10));

        // 이미지 패널
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(Color.WHITE);
        contentPane.add(imagePanel, BorderLayout.NORTH);
        imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // 이미지 추가
        JLabel imageLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon(); 
        ImageIcon scaledImageIcon = new ImageIcon(restaurant.image.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH));
        imageLabel.setIcon(scaledImageIcon);
        imagePanel.add(imageLabel);

        // 텍스트 패널
        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.WHITE);
        contentPane.add(textPanel, BorderLayout.CENTER);
        textPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;

        // 텍스트 추가
        JLabel nameLabel = new JLabel("<html><div style='text-align: center;'><b>" + restaurant.name + "</b></div></html>");
        nameLabel.setFont(new Font("한컴 말랑말랑 Bold", Font.BOLD, 16));
        textPanel.add(nameLabel, gbc);

        JLabel menuLabel = new JLabel("<html><div style='text-align: center;'>" + restaurant.menu + "</div></html>");
        menuLabel.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 14));
        textPanel.add(menuLabel, gbc);

        JLabel timeLabel = new JLabel("<html><div style='text-align: center;'>" + restaurant.time + "</div></html>");
        timeLabel.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 14));
        textPanel.add(timeLabel, gbc);

        JLabel locationLabel = new JLabel("<html><div style='text-align: center;'>위치: " + restaurant.location + "</div></html>");
        locationLabel.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 14));
        textPanel.add(locationLabel, gbc);

        // 웨이팅 등록하기 버튼 추가
        JButton waitlistButton = new JButton("웨이팅 등록하기");
        waitlistButton.setBackground(new Color(255, 87, 132));
        waitlistButton.setForeground(Color.WHITE);
        contentPane.add(waitlistButton, BorderLayout.SOUTH);

        waitlistButton.addActionListener(e -> showWaitingDialog(restaurant.name));
    }

    private void showWaitingDialog(String restaurantName) {
        // 다이얼로그 생성
        JDialog dialog = new JDialog(this, "웨이팅 등록", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 300);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 웨이팅 유형 선택
        JLabel typeLabel = new JLabel("웨이팅 유형:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(typeLabel, gbc);

        String[] waitingTypes = {"매장식사", "포장"};
        JComboBox<String> typeComboBox = new JComboBox<>(waitingTypes);
        gbc.gridx = 1;
        gbc.gridy = 0;
        dialog.add(typeComboBox, gbc);

        // 인원수 선택
        JLabel peopleLabel = new JLabel("인원수:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(peopleLabel, gbc);

        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 20, 1); // 최소 1명, 최대 20명
        JSpinner peopleSpinner = new JSpinner(spinnerModel);
        gbc.gridx = 1;
        gbc.gridy = 1;
        dialog.add(peopleSpinner, gbc);

        // 등록하기 버튼
        JButton submitButton = new JButton("등록하기");
        submitButton.setBackground(new Color(255, 87, 132));
        submitButton.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            String waitingType = (String) typeComboBox.getSelectedItem();
            int numberOfPeople = (Integer) peopleSpinner.getValue();
            addWaitingToDatabase(restaurant.name, waitingType, numberOfPeople, userTelid);
            dialog.dispose();
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void addWaitingToDatabase(String restaurantName, String waitingType, int numberOfPeople, String userTelid) {
        String url = "jdbc:mysql://localhost:3306/project"; // 데이터베이스 URL
        String username = "root"; // 데이터베이스 사용자 이름
        String password = "1234"; // 데이터베이스 비밀번호
        
        String sql = "INSERT INTO WAIT (WAIT_USERID, WAIT_RESTNAME, WAIT_NUMBER, WAIT_COUNT, WAIT_CATEGORY, WAIT_INTIME, WAIT_STATE, WAIT_CHATID) VALUES (?, ?, ?, ?, ?, ?, '대기중', ?)";
        
        
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

        	pstmt.setString(1, Login.loggedInUserId); // 사용자 ID 설정
            pstmt.setString(2, restaurantName); // 식당 이름 설정
            pstmt.setInt(3, 0); // 웨이팅 번호
            pstmt.setInt(4, numberOfPeople); // 인원수 설정
            pstmt.setString(5, waitingType); // 웨이팅 유형 설정
            pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now())); // 현재 시간 설정
            pstmt.setString(7, userTelid);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "웨이팅이 등록되었습니다!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "웨이팅 등록 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
