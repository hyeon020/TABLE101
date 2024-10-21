package telegramcode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.vdurmont.emoji.EmojiParser;

public class sendNoti {
	private static final String BOT_TOKEN = "7385961763:AAHE7WiJkyWqZhovLFnc7R9kvGcPnCOY4pQ";

	public static void processNewWaitingNotifications() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseCon.getConnection();
			// WAIT 테이블에서 알림이 전송되지 않은 새로운 웨이팅 정보 조회
			String waitQuery = "SELECT WAIT_ID, WAIT_CHATID, WAIT_RESTNAME, WAIT_CATEGORY FROM WAIT WHERE NOTI_SENT = FALSE AND WAIT_STATE = '확정'";
			pstmt = conn.prepareStatement(waitQuery);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				long waitId = rs.getLong("WAIT_ID");
				String chatId = rs.getString("WAIT_CHATID");
				String restaurantName = rs.getString("WAIT_RESTNAME");
				String category = rs.getString("WAIT_CATEGORY");

				// 해당 식당의 공지사항 조회
				String notice = getRestaurantNotice(conn, restaurantName);

				// 확성기 이모지 [식당이름]
				String messageEmoji = EmojiParser.parseToUnicode(":loudspeaker: [" + restaurantName + "]\n" + notice);

				// 알림 전송
				sendTelegramMessage(chatId, messageEmoji);

				switch (category) {
				case "포장":
					sendTelegramEntire(chatId, "고객님은 포장 이용 고객입니다.");
					break;
				case "매장식사":
					sendTelegramEntire(chatId, "고객님은 매장 내 식사 이용 고객입니다.");
					break;
				default:
					sendTelegramEntire(chatId, "감사합니다 고객님.");
				}

				// 알림 전송 표시
				markNotificationAsSent(conn, waitId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void processWaitNumberOneNotifications() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseCon.getConnection();
			String waitQuery = "SELECT WAIT_ID, WAIT_CHATID,WAIT_CATEGORY FROM WAIT WHERE WAIT_NUMBER = TRUE AND WAIT_NUMBERONE = FALSE AND WAIT_STATE = '확정'";
			pstmt = conn.prepareStatement(waitQuery);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				long waitId = rs.getLong("WAIT_ID");
				String chatId = rs.getString("WAIT_CHATID");
				String category = rs.getString("WAIT_CATEGORY");

				switch (category) {
				case "포장":
					String messageEmoji = EmojiParser.parseToUnicode("현재 포장 대기 순위 1번입니다. 음식이 곧 준비 될 예정이니 매장 내에서 대기해 주세요!:smile: ");
					sendTelegramEntire(chatId, messageEmoji);
					break;
				case "매장식사":
					String messageEmoji2 = EmojiParser.parseToUnicode("현재 식사 대기 순위 1번입니다. 입장을 준비해 주세요!:smile: ");
					sendTelegramEntire(chatId, messageEmoji2);
					break;
				default:
					sendTelegramEntire(chatId, "현재 대기 순위 1번입니다. 준비해 주세요!");
				}

				// 전송 후 1로 변경
				markWaitNumberOneNotificationAsSent(conn, waitId);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static void markWaitNumberOneNotificationAsSent(Connection conn, long waitId) throws SQLException {
		String updateQuery = "UPDATE WAIT SET WAIT_NUMBERONE = TRUE WHERE WAIT_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
			pstmt.setLong(1, waitId);
			pstmt.executeUpdate();
		}
	}

	private static String getRestaurantNotice(Connection conn, String restaurantName) throws SQLException {
		String noticeQuery = "SELECT REST_INFORM FROM REST WHERE REST_NAME = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(noticeQuery)) {
			pstmt.setString(1, restaurantName);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getString("REST_INFORM");
				}
			}
		}
		return "공지사항이 없습니다.";
	}

	private static void markNotificationAsSent(Connection conn, long waitId) throws SQLException {
		String updateQuery = "UPDATE WAIT SET NOTI_SENT = TRUE WHERE WAIT_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
			pstmt.setLong(1, waitId);
			pstmt.executeUpdate();
		}
	}

	private static void sendTelegramMessage(String chatId, String text) {
		try {
			String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
			String encodedChatId = URLEncoder.encode(chatId, StandardCharsets.UTF_8.toString());
			String urlString = String.format(
					"https://api.telegram.org/bot%s/sendmessage?chat_id=%s&text=%s%%0A메뉴를 입력하여 원하는 항목을 선택할 수 있습니다. 각 항목에 대한 설명이 필요하면 도움말을 입력합니다.",
					BOT_TOKEN, encodedChatId, encodedText);
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
			}
		} catch (Exception e) {
			System.err.println("Error sending Telegram message: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void sendTelegramEntire(String chatId, String text) {
		try {
			String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
			String encodedChatId = URLEncoder.encode(chatId, StandardCharsets.UTF_8.toString());
			String urlString = String.format("https://api.telegram.org/bot%s/sendmessage?chat_id=%s&text=%s%%0A",
					BOT_TOKEN, encodedChatId, encodedText);
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
			}
		} catch (Exception e) {
			System.err.println("Error sending Telegram message: " + e.getMessage());
			e.printStackTrace();
		}
	}
}