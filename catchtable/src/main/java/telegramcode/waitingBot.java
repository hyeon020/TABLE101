package telegramcode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class waitingBot extends TelegramLongPollingBot {
    private final String BOT_TOKEN;
    private RpsGame rpsGame;

    public waitingBot(String botToken) {
        this.BOT_TOKEN = botToken;
        this.rpsGame = new RpsGame(this);
    }

    @Override
    public String getBotUsername() {
        return "dita2414bot";
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
            	case "확인":
            		checkChatId(chatId);
            		break;
                case "/start":
                    sendTextMessage(chatId, "웨이팅 봇에 오신 것을 환영합니다!");
                    break;
                case "메뉴":
                    sendMenu(chatId);
                    break;
                case "실시간 순번 확인✔️":
                    sendWaitingNumber(chatId);
                    break;
                case "예약 취소❌":
                    cancelReservation(chatId);
                    break;
                case "대결 신청🥊":
                	handleGameRequest(chatId);             	
                    break;
                case "수락": case"거절":
                	rpsGame.handleGameResponse(chatId, messageText);
                	break;
                case "가위✌️": case "바위✊": case "보🖐️":
                	rpsGame.handleGameMove(chatId, messageText);
                	break;
                case "도움말":
                    sendHelp(chatId);
                    break;
                case "/hide":
                    hideKeyboard(chatId);
                    break;
                case "예":
                    processReservationCancellation(chatId);
                    break;
                case "아니오":
                    sendTextMessage(chatId, "예약 취소가 취소되었습니다.");
                    break;
                default:
                    sendTextMessage(chatId, "알 수 없는 명령입니다.");
            }
        }
    }
    
    private void checkChatId(long chatId) {
    	String message = "당신의 chat_id는 " + chatId + " 입니다.";
        sendTextMessage(chatId, message);
    }

    private void sendMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("원하시는 항목을 선택해주세요.");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        
        KeyboardRow row1 = new KeyboardRow();
        row1.add("실시간 순번 확인✔️");
        keyboard.add(row1);
        
        KeyboardRow row2 = new KeyboardRow();
        row2.add("예약 취소❌");
        keyboard.add(row2);
        
        KeyboardRow row3 = new KeyboardRow();
        row3.add("대결 신청🥊");
        keyboard.add(row3);

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendWaitingNumber(long chatId) {
        int waitingNumber = getWaitingNumber(chatId);
        String responseMessage;
        if (waitingNumber > 0) {
            responseMessage = "고객님의 웨이팅 순번은 " + waitingNumber + "번 입니다.";
        } else {
            responseMessage = "현재 웨이팅 목록에 등록되어 있지 않습니다.";
        }
        sendTextMessage(chatId, responseMessage);
    }

    public int getWaitingNumber(long chatId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int waitingNumber = -1;

        try {
            conn = DatabaseCon.getConnection();
            String sql = "SELECT WAIT_NUMBER "+
                         "FROM WAIT " +
                         "WHERE WAIT_CHATID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, chatId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                waitingNumber = rs.getInt("WAIT_NUMBER");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return waitingNumber;
    }

    private void cancelReservation(long chatId) {
        SendMessage confirmMessage = SendMessage.builder()
        		.chatId(String.valueOf(chatId))
        		.text("정말로 예약을 취소하시겠습니까?(예/아니오)")
        		.build();
        try {
			execute(confirmMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
    }
    
    private void processReservationCancellation(long chatId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseCon.getConnection();
            String sql = "DELETE FROM WAIT WHERE WAIT_CHATID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, chatId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                sendTextMessage(chatId, "예약이 취소되었습니다.");
            } else {
                sendTextMessage(chatId, "취소할 예약이 없습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendTextMessage(chatId, "예약 취소 중 오류가 발생했습니다. 나중에 다시 시도해주세요.");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }  

    private void sendHelp(long chatId) {
        String helpMessage = "1. 실시간 순번 확인: 현재 나의 대기순번이 몇 번째 인지 확인합니다.\n\n" +
                             "2. 예약 취소: 현재 등록되어 있는 웨이팅을 취소합니다.\n\n" +
                             "3. 대결 신청: 2000point를 지불하여 내 앞 순번인 팀에게 가위바위보 대결을 신청합니다. " +
                             "대결에서 승리 시 순번이 바뀌며, 패배 시 유지됩니다." +
                             "신청을 받은 팀은 승낙 혹은 거절할 수 있고, 승리 시 2000p를 획득하며, 패배 시 순번이 바뀝니다.";
        sendTextMessage(chatId, helpMessage);
    }

    private void hideKeyboard(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("키보드가 숨겨졌습니다.");
        message.setReplyMarkup(new ReplyKeyboardRemove(true));
        
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendTextMessage(long chatId, String text) {
        SendMessage message = SendMessage.builder()
            .chatId(String.valueOf(chatId))
            .text(text)
            .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    
    public int getUserPoints(long chatId) {
    	Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int points = 0;

        try {
            conn = DatabaseCon.getConnection();
            String sql = "SELECT USER_POINT FROM USER WHERE USER_CHATID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, chatId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                points = rs.getInt("USER_POINT");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	try {
        		if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
            
        }
        return points;
    }
    
    public long getOpponentChatId(int waitNumber) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        long opponentChatId = -1;

        try {
            conn = DatabaseCon.getConnection();
            String sql = "SELECT WAIT_CHATID FROM WAIT WHERE WAIT_NUMBER = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, waitNumber);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                opponentChatId = rs.getLong("WAIT_CHATID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	try {
        		if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        return opponentChatId;
    }
    
    public void deductPoints(long chatId, int points) {
        updateUserPoints(chatId, -points);
    }

    public void refundPoints(long chatId, int points) {
        updateUserPoints(chatId, points);
    }

    public void addPoints(long chatId, int points) {
        updateUserPoints(chatId, points);
    }
    
    public void handleGameRequest(long chatId) {
    	String category = getWaitCategory(chatId);
    	if ("포장".equals(category)) {
            sendTextMessage(chatId, "포장 손님은 이용하실 수 없습니다.");
        } else if ("매장식사".equals(category)) {
            rpsGame.startGame(chatId);
        } else {
            sendTextMessage(chatId, "현재 이용할 수 없는 기능입니다.");
        }
    }
    
    public String getWaitCategory(long chatId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String category = null;

        try {
            conn = DatabaseCon.getConnection();
            String sql = "SELECT WAIT_CATEGORY FROM WAIT WHERE WAIT_CHATID = ? AND WAIT_STATE = '확정'";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, chatId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                category = rs.getString("WAIT_CATEGORY");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return category;
    }

    private void updateUserPoints(long chatId, int pointChange) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseCon.getConnection();
            String sql = "UPDATE USER SET USER_POINT = USER_POINT + ? WHERE USER_CHATID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pointChange);
            pstmt.setLong(2, chatId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
    
}