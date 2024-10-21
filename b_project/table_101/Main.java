package table_101;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Main extends JFrame {
	public static String loggedInUserId; //웨이팅 유저아이디 추가
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JList<Restaurant> restaurantList; // 식당 리스트
    private DefaultListModel<Restaurant> listModel; // 리스트 모델
    private List<Restaurant> allRestaurants; // 전체 식당 리스트
    private JComboBox<String> comboBox;

    // 사용자 정보를 저장할 변수 추가
    private String userName;
    private String userTelid;
    private String userId;
    private String userPhone;
    private int userPoint;

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

    // 사용자 정보를 받는 생성자 추가
    public Main(String userName, String loggedInUserId, String userPhone, String userTelid) {
        this.userName = userName;
        this.loggedInUserId = loggedInUserId;
        this.userPhone = userPhone;
        this.userPoint = userPoint;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 490, 650);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // 상단 네비게이션 바
        ImageIcon originalIcon = new ImageIcon(Main.class.getResource("/table_101/back.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JButton btnNewButton = new JButton("");
        btnNewButton.setBackground(new Color(255, 87, 132));
        btnNewButton.setIcon(scaledIcon);
        btnNewButton.setBounds(10, 8, 40, 40);
        btnNewButton.setBorder(null);
        contentPane.add(btnNewButton);

        btnNewButton.addActionListener(e -> {
            Login loginFrame = new Login();
            loginFrame.setLocation(getLocation());
            loginFrame.setVisible(true);
            dispose();  // 현재 창을 닫음
        });

        JLabel lblNewLabel = new JLabel("TABLE101");
        lblNewLabel.setOpaque(true);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Leelawadee", Font.BOLD, 23));
        lblNewLabel.setBackground(new Color(255, 87, 132));
        lblNewLabel.setBounds(0, 0, 476, 54);
        contentPane.add(lblNewLabel);

        // 정보 아이콘
        ImageIcon infoIcon = new ImageIcon(Main.class.getResource("/table_101/info.png"));
        Image scaledInfoImage = infoIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledInfoIcon = new ImageIcon(scaledInfoImage);
        JLabel lblInfoIcon = new JLabel(scaledInfoIcon);
        lblInfoIcon.setBounds(34, 73, 30, 30);
        contentPane.add(lblInfoIcon);

        lblInfoIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Notice myPageFrame = new Notice(userName, loggedInUserId, userPhone, userTelid);
                myPageFrame.setLocation(getLocation());
                myPageFrame.setVisible(true);
                dispose();  // 현재 창(Main)을 닫음
            }
        });

        // 마이페이지 아이콘
        ImageIcon myPageIcon = new ImageIcon(Main.class.getResource("/table_101/mypage.png"));
        Image scaledMyPageImage = myPageIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon scaledMyPageIcon = new ImageIcon(scaledMyPageImage);
        JLabel lblMyPageIcon = new JLabel(scaledMyPageIcon);
        lblMyPageIcon.setBounds(401, 70, 40, 40);
        contentPane.add(lblMyPageIcon);

        lblMyPageIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MyPage myPageFrame = new MyPage(userName, loggedInUserId, userPhone, userTelid); // 사용자 정보 전달
                myPageFrame.setLocation(getLocation());
                myPageFrame.setVisible(true);
                dispose();
            }
        });

        // 검색 텍스트 필드
        textField = new JTextField();
        textField.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 14));
        textField.setForeground(new Color(192, 192, 192));
        textField.setText("식당을 검색하세요.");
        textField.setBackground(new Color(255, 255, 255));
        textField.setBounds(89, 75, 300, 28);
        contentPane.add(textField);
        textField.setColumns(10);
        textField.setBorder(BorderFactory.createBevelBorder(1, new Color(192, 192, 192), new Color(192, 192, 192)));

        textField.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (textField.getText().equals("식당을 검색하세요.")) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
        });

        // DocumentListener를 추가해 입력값을 기반으로 리스트 필터링
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterRestaurantList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterRestaurantList();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterRestaurantList();
            }
        });

        // 지역 선택 콤보박스
        comboBox = new JComboBox<>();
        comboBox.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 13));
        comboBox.setBackground(new Color(255, 255, 255));
        comboBox.setModel(new DefaultComboBoxModel<>(new String[] {"전국", "서울", "부산", "대구", "인천", "광주", "대전", "울산"}));
        comboBox.setBounds(24, 115, 76, 28); // y좌표를 115로 설정
        comboBox.setBorder(null);
        contentPane.add(comboBox);

        // 콤보박스 리스너 추가 및 지역 선택에 따른 필터링 로직 추가
        comboBox.addActionListener(e -> {
            String selectedLocation = (String) comboBox.getSelectedItem();
            filterRestaurantByLocation(selectedLocation);  // 선택한 지역에 따라 식당 필터링
        });

        // 식당 리스트 모델 및 JList 생성
        listModel = new DefaultListModel<>();
        restaurantList = new JList<>(listModel);
        restaurantList.setCellRenderer(new RestaurantListCellRenderer()); // 커스텀 렌더러 설정

        // 항목 높이 설정 (예: 120픽셀로 설정)
        restaurantList.setFixedCellHeight(120); // 원하는 높이로 설정

        // JList를 JScrollPane에 추가하여 스크롤 가능하게 만듭니다.
        JScrollPane scrollPane = new JScrollPane(restaurantList);
        scrollPane.setBounds(0, 160, 476, 429);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3)); // 두께 3의 회색 테두리
        contentPane.add(scrollPane);

        // 전체 식당 리스트 초기화
        allRestaurants = new ArrayList<>();

        // 데이터베이스에서 식당 데이터 가져오기
        loadRestaurantData();
        
        restaurantList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // 더블 클릭했을 때
                    int index = restaurantList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        Restaurant selectedRestaurant = listModel.getElementAt(index);
                        Waiting waitlistFrame = new Waiting(selectedRestaurant, userId, userTelid);
                        waitlistFrame.setLocation(getLocation());
                        waitlistFrame.setVisible(true);
                    }
                }
            }
        });
    }

    private void loadRestaurantData() {
        String url = "jdbc:mysql://localhost:3306/project"; // 데이터베이스 URL
        String user = "root"; // 사용자 이름
        String password = "1234"; // 비밀번호

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT REST_NAME, REST_MENU, REST_TIME, REST_IMAGE, REST_LOCATION FROM REST")) {

            while (rs.next()) {
                String name = rs.getString("REST_NAME");
                String menu = rs.getString("REST_MENU");
                String time = rs.getString("REST_TIME");
                ImageIcon imageIcon = new ImageIcon(rs.getString("REST_IMAGE")); // 이미지 경로 가져오기
                String location = rs.getString("REST_LOCATION"); // 위치 정보 추가

                // 식당 정보를 리스트에 추가
                Restaurant restaurant = new Restaurant(name, menu, time, imageIcon, location);
                allRestaurants.add(restaurant); // 전체 리스트에 추가
                listModel.addElement(restaurant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 검색어를 기반으로 리스트 필터링
    private void filterRestaurantList() {
        String searchTerm = textField.getText().trim().toLowerCase(); // 검색어를 소문자로 변환

        // 검색어가 없을 경우, 전체 리스트를 보여줌
        if (searchTerm.isEmpty() || searchTerm.equals("식당을 검색하세요.")) {
            listModel.clear();
            for (Restaurant restaurant : allRestaurants) {
                listModel.addElement(restaurant);
            }
        } else {
            // 검색어가 있는 경우, 식당 이름에 검색어가 포함된 식당만 보여줌
            listModel.clear();
            for (Restaurant restaurant : allRestaurants) {
                if (restaurant.name.toLowerCase().contains(searchTerm)) { // 이름만 검색
                    listModel.addElement(restaurant);
                }
            }
        }
    }

    // 지역에 따라 식당을 필터링하는 메서드
    private void filterRestaurantByLocation(String location) {
        listModel.clear(); // 기존 목록 초기화
        allRestaurants.clear(); // 전체 목록 초기화

        String url = "jdbc:mysql://localhost:3306/project"; // 데이터베이스 URL
        String user = "root"; // 사용자 이름
        String password = "1234"; // 비밀번호

        String query = "SELECT REST_NAME, REST_MENU, REST_TIME, REST_IMAGE, REST_LOCATION FROM REST";

        // 지역 선택에 따른 조건 추가
        if (!location.equals("전국")) {
            if (location.equals("서울")) {
                query += " WHERE REST_LOCATION LIKE '서울%' OR REST_LOCATION LIKE '경기도%'";
            } else if (location.equals("부산")){
                query += " WHERE REST_LOCATION LIKE '부산%'";
            } else if (location.equals("대구")){
                query += " WHERE REST_LOCATION LIKE '대구%'";
            } else if (location.equals("인천")){
                query += " WHERE REST_LOCATION LIKE '인천%'";
            } else if (location.equals("광주")){
                query += " WHERE REST_LOCATION LIKE '광주%'";
            } else if (location.equals("대전")){
                query += " WHERE REST_LOCATION LIKE '대전%'";
            } else if (location.equals("울산")){
                query += " WHERE REST_LOCATION LIKE '울산%'";
            }
        }

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("REST_NAME");
                String menu = rs.getString("REST_MENU");
                String time = rs.getString("REST_TIME");
                ImageIcon imageIcon = new ImageIcon(rs.getString("REST_IMAGE")); // 이미지 경로 가져오기
                String restLocation = rs.getString("REST_LOCATION"); // 위치 정보 추가

                // 식당 정보를 리스트에 추가
                Restaurant restaurant = new Restaurant(name, menu, time, imageIcon, restLocation);
                allRestaurants.add(restaurant); // 전체 리스트에 추가
                listModel.addElement(restaurant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 식당 정보를 저장하는 클래스
    class Restaurant {
        String name;
        String menu;
        String time;
        ImageIcon image;
        String location;

        public Restaurant(String name, String menu, String time, ImageIcon image, String location) {
            this.name = name;
            this.menu = menu;
            this.time = time;
            this.image = image;
            this.location = location;
        }
    }

    class RestaurantListCellRenderer extends JPanel implements ListCellRenderer<Restaurant> {
        private static final int IMAGE_WIDTH = 100; // 이미지 너비
        private static final int IMAGE_HEIGHT = 100; // 이미지 높이

        @Override
        public Component getListCellRendererComponent(JList<? extends Restaurant> list, Restaurant restaurant, int index, boolean isSelected, boolean cellHasFocus) {
            setLayout(new GridBagLayout()); // GridBagLayout 사용
            removeAll(); // 이전 컴포넌트 제거

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 30, 0, 0); // 간격 조정

            // 이미지 레이블 추가
            JLabel imageLabel = new JLabel();
            ImageIcon scaledImageIcon = new ImageIcon(restaurant.image.getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH));
            imageLabel.setIcon(scaledImageIcon);

            add(imageLabel, gbc);

            // 텍스트 레이블 추가
            JLabel textLabel = new JLabel("<html><b>" + restaurant.name + "</b><br>" + restaurant.menu + "<br>" + restaurant.time + "</html>");
            textLabel.setVerticalAlignment(SwingConstants.TOP);
            textLabel.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15)); // 폰트 조정
            gbc.gridx = 1; // 두 번째 열
            gbc.anchor = GridBagConstraints.WEST; // 왼쪽 정렬
            gbc.weightx = 1; // 텍스트 레이블이 수평 공간을 차지하도록 설정
            gbc.weighty = 1; // 텍스트 레이블이 수직 공간을 차지하도록 설정
            add(textLabel, gbc);

            if (isSelected) {
                setBackground(new Color(255, 191, 211));
            } else {
                setBackground(Color.white);
            }

            return this;
        }
    }
}
