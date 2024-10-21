package table_101;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class Notice extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private String userName;
	private String userTelid;
	private String userId;
	private String userPhone;
	private int userPoint;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Notice frame = new Notice(null, null, null, null);
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
	public Notice(String userName, String userId, String userPhone, String userTelid) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 490, 650);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		ImageIcon originalIcon = new ImageIcon(MyPage.class.getResource("/table_101/back.png"));
		Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImage);

		JButton btnNewButton = new JButton("");
		btnNewButton.setBackground(new Color(255, 87, 132));
		btnNewButton.setIcon(scaledIcon);
		btnNewButton.setBounds(10, 8, 40, 40);
		btnNewButton.setBorder(null);
		contentPane.add(btnNewButton);

		btnNewButton.addActionListener(e -> {
			Main mainFrame = new Main(userName, userId, userPhone, userTelid);
			mainFrame.setLocation(getLocation());
			mainFrame.setVisible(true);
			dispose();
		});

		JLabel lblNewLabel = new JLabel("TABLE101");
		lblNewLabel.setOpaque(true);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Leelawadee", Font.BOLD, 23));
		lblNewLabel.setBackground(new Color(255, 87, 132));
		lblNewLabel.setBounds(0, 0, 476, 54);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("앱 사용법");
		lblNewLabel_1.setFont(new Font("한컴 말랑말랑 Bold", Font.BOLD, 16));
		lblNewLabel_1.setBounds(25, 120, 84, 24);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("앞당기기란?");
		lblNewLabel_1_1.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 16));
		lblNewLabel_1_1.setBounds(25, 332, 113, 24);
		contentPane.add(lblNewLabel_1_1);

		JTextPane textPane = new JTextPane();
		textPane.setFont(new Font("한컴 말랑말랑", Font.PLAIN, 13));
		textPane.setText("\r\n웨이팅 등록시, 내 순번 바로 앞순번과 대결(가위바위보)을 통해 이기면 교체되는방식이다. \r\r\n단, 지면 앞순번에게 포인트 지급");
		textPane.setBounds(25, 356, 419, 220);
		contentPane.add(textPane);

		JTextPane textPane_1 = new JTextPane();
		textPane_1.setFont(new Font("한컴 말랑말랑", Font.PLAIN, 13));
		textPane_1.setText(
				"\r\n\r\n웨이팅 등록 확인 방법\r\n웨이팅 등록을 하면 텔레그램에 메시지 알림이 간다. \r\n앱과 메시지를 통해 자신의 대기 순번에 따라 입장순서가 되면 매장을 이용하면 된다. \r\n이때 등록 취소가 가능하며 앞당기기 기능을 통해 순번을 바꿀수 있다.");
		textPane_1.setBounds(25, 138, 419, 220);
		contentPane.add(textPane_1);
	}
}
