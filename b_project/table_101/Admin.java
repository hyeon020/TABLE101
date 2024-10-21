package table_101;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.Vector;

public class Admin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnUserList;
    private JButton btnBusinessList;
    private JButton btnStoreApproval;
    private AdminMgr adminMgr; // AdminMgr 객체

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Admin frame = new Admin();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Admin() {
        adminMgr = new AdminMgr(); // AdminMgr 초기화

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 650);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        ImageIcon originalIcon = new ImageIcon(Admin.class.getResource("/table_101/back.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        contentPane.setLayout(null);

        JButton btnBack = new JButton("");
        btnBack.setBounds(10, 8, 40, 40);
        btnBack.setBackground(new Color(128, 128, 128));
        btnBack.setIcon(scaledIcon);
        btnBack.setBorder(null);
        contentPane.add(btnBack);

        btnBack.addActionListener(e -> {
            Login loginFrame = new Login();
            loginFrame.setLocation(getLocation());
            loginFrame.setVisible(true);
            dispose();
        });

        JLabel lblTitle = new JLabel("TABLE101");
        lblTitle.setBounds(0, 0, 586, 54);
        lblTitle.setOpaque(true);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Leelawadee", Font.BOLD, 23));
        lblTitle.setBackground(new Color(128, 128, 128));
        contentPane.add(lblTitle);

        btnUserList = new JButton("사용자 목록");
        btnUserList.setBounds(0, 54, 193, 46);
        btnUserList.setBackground(Color.WHITE);
        btnUserList.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
        btnUserList.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(192, 192, 192), new Color(192, 192, 192), new Color(192, 192, 192), new Color(192, 192, 192)));
        contentPane.add(btnUserList);

        btnBusinessList = new JButton("사업자 목록");
        btnBusinessList.setBounds(192, 54, 193, 46);
        btnBusinessList.setBackground(Color.WHITE);
        btnBusinessList.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
        btnBusinessList.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(192, 192, 192), new Color(192, 192, 192), new Color(192, 192, 192), new Color(192, 192, 192)));
        contentPane.add(btnBusinessList);

        btnStoreApproval = new JButton("매장 현황");
        btnStoreApproval.setBounds(384, 54, 193, 46);
        btnStoreApproval.setBackground(Color.WHITE);
        btnStoreApproval.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
        btnStoreApproval.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(192, 192, 192), new Color(192, 192, 192), new Color(192, 192, 192), new Color(192, 192, 192)));
        contentPane.add(btnStoreApproval);

        // JTable 초기화
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 110, 564, 490);
        contentPane.add(scrollPane);

        table = new JTable();
        model = new DefaultTableModel();
        table.setModel(model);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(128, 128, 128));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("한컴 말랑말랑 Bold", Font.BOLD, 15));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 30));
        table.setRowHeight(30);

        scrollPane.setViewportView(table);

        // 초기 데이터 로드 (사용자 목록)
        loadUserData();

        // 버튼 클릭 시 데이터 로드
        btnUserList.addActionListener(e -> loadUserData());
        btnBusinessList.addActionListener(e -> loadBusinessData());
        btnStoreApproval.addActionListener(e -> loadStoreApprovalData());
    }

    private void loadUserData() {
        model.setRowCount(0); // 기존 데이터를 삭제
        model.setColumnIdentifiers(new String[]{"사용자 ID", "사용자 TELID", "사용자 이름", "사용자 포인트", "사용자 폰번호"});

        Vector<Vector<Object>> userList = adminMgr.getUserList();
        for (Vector<Object> user : userList) {
            model.addRow(user);
        }
    }

    private void loadBusinessData() {
        model.setRowCount(0); // 기존 데이터를 삭제
        model.setColumnIdentifiers(new String[]{"사업자 ID", "사업자 이름", "사업자 PW"});

        Vector<Vector<Object>> businessList = adminMgr.getBusinessList();
        if (businessList != null) {
            for (Vector<Object> business : businessList) {
                model.addRow(business);
            }
        } else {
            System.out.println("Business data could not be loaded.");
        }
    }

    private void loadStoreApprovalData() {
        model.setRowCount(0); // 기존 데이터를 삭제
        model.setColumnIdentifiers(new String[]{"식당 이름", "주소", "식당 전화번호"});

        Vector<Vector<Object>> storeList = adminMgr.getStoreApprovalList();
        if (storeList != null) {
            for (Vector<Object> store : storeList) {
                model.addRow(store);
            }
        } else {
            System.out.println("Store approval data could not be loaded.");
        }
    }

    
}
