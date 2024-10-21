package table_101;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.CardLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class SignUp extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel userSignUpPanel;
	private JPanel buisnessSignUpPanel;
	private JTextField buisnessNameField;
	private JTextField buisnessIdField;
	private JTextField buisnessPasswordField;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private boolean isUserIdChecked = false;
	private boolean isBuisnessIdChecked = false;
	UserMgr usermgr;
	BuisnessMgr buisnessmgr;
	private JTextField textField_6;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SignUp frame = new SignUp();
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
	public SignUp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 490, 650);
		usermgr = new UserMgr();
		buisnessmgr = new BuisnessMgr();
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		ImageIcon originalIcon = new ImageIcon(SignUp.class.getResource("/table_101/back.png"));
		Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImage);

		JButton btnNewButton = new JButton("");
		btnNewButton.setBackground(new Color(4, 140, 221));
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
		lblNewLabel.setBackground(new Color(4, 140, 221));
		lblNewLabel.setBounds(0, 0, 476, 54);
		contentPane.add(lblNewLabel);

		JButton btnUserSignUp = new JButton("사용자 회원가입");
		btnUserSignUp.setBackground(new Color(255, 255, 255));
		btnUserSignUp.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
		btnUserSignUp.setBounds(0, 54, 235, 46);
		btnUserSignUp.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(192, 192, 192), new Color(192, 192, 192),
				new Color(192, 192, 192), new Color(192, 192, 192)));
		contentPane.add(btnUserSignUp);

		JButton btnBuisnessSignUp = new JButton("사업자 회원가입");
		btnBuisnessSignUp.setBackground(new Color(255, 255, 255));
		btnBuisnessSignUp.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
		btnBuisnessSignUp.setBounds(234, 54, 242, 46);
		btnBuisnessSignUp.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(192, 192, 192),
				new Color(192, 192, 192), new Color(192, 192, 192), new Color(192, 192, 192)));
		contentPane.add(btnBuisnessSignUp);

		JPanel panelContainer = new JPanel();
		panelContainer.setBounds(0, 100, 476, 500);
		contentPane.add(panelContainer);
		panelContainer.setLayout(new CardLayout());

		userSignUpPanel = new JPanel();
		userSignUpPanel.setBackground(new Color(255, 255, 255));
		userSignUpPanel.setLayout(null);
		panelContainer.add(userSignUpPanel, "userSignUp");

		JLabel lblName = new JLabel("이름:");
		lblName.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
		lblName.setBounds(79, 42, 80, 30);
		userSignUpPanel.add(lblName);

		JLabel lblUserId = new JLabel("아이디:");
		lblUserId.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
		lblUserId.setBounds(79, 120, 80, 30);
		userSignUpPanel.add(lblUserId);

		JLabel lblPassword = new JLabel("비밀번호:");
		lblPassword.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
		lblPassword.setBounds(79, 208, 80, 30);
		userSignUpPanel.add(lblPassword);

		JLabel lblPhone = new JLabel("전화번호:");
		lblPhone.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
		lblPhone.setBounds(79, 297, 80, 30);
		userSignUpPanel.add(lblPhone);
		
		JLabel lblTelID = new JLabel("텔레그램:");
		lblTelID.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
		lblTelID.setBounds(79, 378, 80, 30);
		userSignUpPanel.add(lblTelID);
		
        JButton btnQRCode = new JButton("큐알");
        btnQRCode.setForeground(Color.WHITE);
        btnQRCode.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 14));
        btnQRCode.setBackground(new Color(4, 140, 221));
        btnQRCode.setBounds(324, 418, 91, 23);
        userSignUpPanel.add(btnQRCode);
        
        // 큐알 코드 버튼 클릭 시 큐알 코드 이미지가 크게 출력되도록 설정
        btnQRCode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame qrFrame = new JFrame("텔레그램 큐알 코드");
                qrFrame.setSize(600, 850);
                qrFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                
                ImageIcon qrIcon = new ImageIcon(SignUp.class.getResource("/table_101/텔레그램.jpg"));
                JLabel qrLabel = new JLabel(qrIcon);
                qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
                qrFrame.add(qrLabel);
                
                qrFrame.setVisible(true);
            }
        });
        
		textField_2 = new JTextField();
		textField_2.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(128, 128, 128)));
		textField_2.setBounds(79, 70, 233, 30);
		userSignUpPanel.add(textField_2);

		textField_3 = new JTextField();
		textField_3.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(128, 128, 128)));
		textField_3.setBounds(79, 150, 233, 30);
		userSignUpPanel.add(textField_3);

		textField_4 = new JTextField();
		textField_4.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(128, 128, 128)));
		textField_4.setBounds(79, 240, 233, 30);
		userSignUpPanel.add(textField_4);

		textField_5 = new JTextField();
		textField_5.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(128, 128, 128)));
		textField_5.setBounds(79, 327, 233, 30);
		userSignUpPanel.add(textField_5);

		textField_6 = new JTextField();
		textField_6.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(128, 128, 128)));
		textField_6.setBounds(79, 418, 233, 30);
		userSignUpPanel.add(textField_6);
		
		JButton btnNewButton_2 = new JButton("가입하기");
		btnNewButton_2.setForeground(Color.WHITE);
		btnNewButton_2.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 14));
		btnNewButton_2.setBackground(new Color(4, 140, 221));
		btnNewButton_2.setBounds(199, 467, 91, 23);
		userSignUpPanel.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("중복확인");
		btnNewButton_3.setForeground(Color.WHITE);
		btnNewButton_3.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 14));
		btnNewButton_3.setBackground(new Color(4, 140, 221));
		btnNewButton_3.setBounds(324, 139, 91, 23);
		userSignUpPanel.add(btnNewButton_3);
		
		// 사용자 중복확인
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user_id = textField_3.getText(); // 사용자가 입력한 ID를 가져옴

				if (user_id.isEmpty()) {
					JOptionPane.showMessageDialog(null, "아이디를 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (usermgr.isUserIdExists(user_id)) {
					// ID가 중복되는 경우
					JOptionPane.showMessageDialog(null, "이미 사용 중인 아이디입니다.", "중복 확인", JOptionPane.ERROR_MESSAGE);
					isUserIdChecked = false;
				} else {
					// ID가 중복되지 않는 경우
					JOptionPane.showMessageDialog(null, "사용 가능한 아이디입니다.", "중복 확인", JOptionPane.INFORMATION_MESSAGE);
					isUserIdChecked = true;
				}
			}
		});

		buisnessSignUpPanel = new JPanel();
		buisnessSignUpPanel.setBackground(new Color(255, 255, 255));
		buisnessSignUpPanel.setLayout(null);
		panelContainer.add(buisnessSignUpPanel, "buisnessSignUp");

		JLabel lblBuisnessName = new JLabel("이름");
		lblBuisnessName.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
		lblBuisnessName.setBounds(79, 42, 80, 30);
		buisnessSignUpPanel.add(lblBuisnessName);

		buisnessNameField = new JTextField();
		buisnessNameField.setBounds(79, 74, 233, 30);
		buisnessNameField.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(128, 128, 128)));
		buisnessSignUpPanel.add(buisnessNameField);

		JLabel lblBuisnessId = new JLabel("아이디:");
		lblBuisnessId.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
		lblBuisnessId.setBounds(79, 137, 80, 30);
		buisnessSignUpPanel.add(lblBuisnessId);
		/*
		 * businessIdField = new JTextField(); businessIdField.setBounds(141, 139, 169,
		 * 30); businessIdField.setBorder(null);
		 * businessSignUpPanel.add(businessIdField);
		 */
		JLabel lblBuisnessPassword = new JLabel("비밀번호:");
		lblBuisnessPassword.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 15));
		lblBuisnessPassword.setBounds(79, 238, 80, 30);
		buisnessSignUpPanel.add(lblBuisnessPassword);
		/*
		 * businessPasswordField = new JTextField();
		 * businessPasswordField.setBounds(200, 198, 150, 30);
		 * businessPasswordField.setBorder(null);
		 * businessSignUpPanel.add(businessPasswordField);
		 */
		textField = new JTextField();
		textField.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(128, 128, 128)));
		textField.setBounds(79, 166, 233, 30);
		buisnessSignUpPanel.add(textField);

		textField_1 = new JTextField();
		textField_1.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(128, 128, 128)));
		textField_1.setBounds(79, 266, 233, 30);
		buisnessSignUpPanel.add(textField_1);
		
		// 사업자 중복확인
		JButton btnNewButton1 = new JButton("중복확인");
		btnNewButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String buisness_id = textField.getText();

				if (buisness_id.isEmpty()) {
					JOptionPane.showMessageDialog(null, "아이디를 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (buisnessmgr.isBuisnessIdExists(buisness_id)) {
					// ID가 중복되는 경우
					JOptionPane.showMessageDialog(null, "이미 사용 중인 아이디입니다.", "중복 확인", JOptionPane.ERROR_MESSAGE);
					isBuisnessIdChecked = false;
				} else {
					// ID가 중복되지 않는 경우
					JOptionPane.showMessageDialog(null, "사용 가능한 아이디입니다.", "중복 확인", JOptionPane.INFORMATION_MESSAGE);
					isBuisnessIdChecked = true;
				}
			}
		});
		btnNewButton1.setForeground(new Color(255, 255, 255));
		btnNewButton1.setBackground(new Color(4, 140, 221));
		btnNewButton1.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 14));
		btnNewButton1.setBounds(322, 153, 91, 23);
		buisnessSignUpPanel.add(btnNewButton1);

		JButton btnNewButton2 = new JButton("가입하기");
		btnNewButton2.setForeground(Color.WHITE);
		btnNewButton2.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 14));
		btnNewButton2.setBackground(new Color(4, 140, 221));
		btnNewButton2.setBounds(200, 367, 91, 23);
		buisnessSignUpPanel.add(btnNewButton2);

		// 사용자 회원가입
		btnUserSignUp.addActionListener(e -> {
			CardLayout cl = (CardLayout) (panelContainer.getLayout());
			cl.show(panelContainer, "userSignUp");
		});

		// 사업자 회원가입
		btnBuisnessSignUp.addActionListener(e -> {
			CardLayout cl = (CardLayout) (panelContainer.getLayout());
			cl.show(panelContainer, "buisnessSignUp");
		});

		// 사용자 회원가입
		btnNewButton_2.addActionListener(e -> {

			if (!isUserIdChecked) {
				JOptionPane.showMessageDialog(null, "아이디 중복확인을 해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
				return;
			}

			UserBean bean = new UserBean();
			bean.setUser_name(textField_2.getText());
			bean.setUser_id(textField_3.getText());
			bean.setUser_pw(textField_4.getText());
			bean.setUser_phone(textField_5.getText());
			bean.setUser_telid(textField_6.getText());
			
			if (usermgr.insertUser(bean)) {
				JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다.", "가입 성공", JOptionPane.INFORMATION_MESSAGE);

				Login loginFrame = new Login();
				loginFrame.setLocation(getLocation());
				loginFrame.setVisible(true);
				this.dispose();
			} else {
				// 회원가입 실패 메시지
				JOptionPane.showMessageDialog(this, "회원가입에 실패하였습니다. 다시 시도해주세요.", "가입 실패", JOptionPane.ERROR_MESSAGE);
			}

		});
		
		// 사업자 회원가입
		btnNewButton2.addActionListener(e -> {
			if (!isBuisnessIdChecked) {
				JOptionPane.showMessageDialog(null, "아이디 중복확인을 해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
				return;
			}

			BuisnessBean bean = new BuisnessBean();
			bean.setBuisness_id(textField.getText());
			bean.setBuisness_name(buisnessNameField.getText());
			bean.setBuisness_pw(textField_1.getText());
			
			if (buisnessmgr.insertBuisness(bean)) {
				JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다.", "가입 성공", JOptionPane.INFORMATION_MESSAGE);

				Login loginFrame = new Login();
				loginFrame.setLocation(getLocation());
				loginFrame.setVisible(true);
				this.dispose();
			} else {
				// 회원가입 실패 메시지
				JOptionPane.showMessageDialog(this, "회원가입에 실패하였습니다. 다시 시도해주세요.", "가입 실패", JOptionPane.ERROR_MESSAGE);
			}
		});

	}
}
