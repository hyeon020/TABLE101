package telegramcode;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.vdurmont.emoji.EmojiParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RpsGame {
    private waitingBot bot;
    private Map<Long, GameState> gameStates = new HashMap<>();

    public RpsGame(waitingBot bot) {
        this.bot = bot;
    }

    public void startGame(long challengerChatId) {
        int currentWaitNumber = bot.getWaitingNumber(challengerChatId);
        if (currentWaitNumber <= 1) {
            bot.sendTextMessage(challengerChatId, "당신은 이미 1번째 순서입니다. 대결을 신청할 수 없습니다.");
            return;
        }

        int points = bot.getUserPoints(challengerChatId);
        if (points < 2000) {
            bot.sendTextMessage(challengerChatId, "포인트가 부족합니다. 최소 2000 포인트가 필요합니다.");
            return;
        }

        long opponentChatId = bot.getOpponentChatId(currentWaitNumber - 1);
        if (opponentChatId == -1) {
            bot.sendTextMessage(challengerChatId, "대결 상대를 찾을 수 없습니다.");
            return;
        }

        bot.deductPoints(challengerChatId, 2000);
        bot.sendTextMessage(challengerChatId, "2000 포인트를 사용하여 대결을 신청했습니다.");
        sendGameRequest(opponentChatId, challengerChatId);
    }

    private void sendGameRequest(long opponentChatId, long challengerChatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(opponentChatId));
        message.setText("가위바위보 대결 신청이 왔습니다. 수락하시겠습니까?");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("수락");
        row.add("거절");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
            gameStates.put(opponentChatId, new GameState(challengerChatId, GameState.State.WAITING_RESPONSE));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void handleGameResponse(long respondentChatId, String response) {
        GameState state = gameStates.get(respondentChatId);
        if (state == null || state.getState() != GameState.State.WAITING_RESPONSE) {
            bot.sendTextMessage(respondentChatId, "잘못된 응답입니다.");
            return;
        }

        long challengerChatId = state.getOpponentChatId();

        if ("수락".equals(response)) {
            startRpsGame(respondentChatId, challengerChatId);
        } else {
            bot.sendTextMessage(challengerChatId, "상대방이 대결을 거절했습니다. 2000 포인트가 환불됩니다.");
            bot.refundPoints(challengerChatId, 2000);
            gameStates.remove(respondentChatId);
        }
    }

    private void startRpsGame(long player1ChatId, long player2ChatId) {
        sendRpsKeyboard(player1ChatId);
        sendRpsKeyboard(player2ChatId);
        gameStates.put(player1ChatId, new GameState(player2ChatId, GameState.State.CHOOSING));
        gameStates.put(player2ChatId, new GameState(player1ChatId, GameState.State.CHOOSING));
    }

    private void sendRpsKeyboard(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("가위, 바위, 보 중 하나를 선택하세요.");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("가위✌️");
        row.add("바위✊");
        row.add("보🖐️");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void handleGameMove(long chatId, String move) {
        GameState state = gameStates.get(chatId);
        if (state == null || state.getState() != GameState.State.CHOOSING) {
            bot.sendTextMessage(chatId, "잘못된 게임 동작입니다.");
            return;
        }

        state.setMove(move);
        long opponentChatId = state.getOpponentChatId();
        GameState opponentState = gameStates.get(opponentChatId);

        if (opponentState.getMove() != null) {
            determineWinner(chatId, state.getMove(), opponentChatId, opponentState.getMove());
        }
    }

    private void determineWinner(long player1ChatId, String player1Move, long player2ChatId, String player2Move) {
        String result;
        if (player1Move.equals(player2Move)) {
            result = "무승부";
        } else if ((player1Move.equals("가위") && player2Move.equals("보")) ||
                   (player1Move.equals("바위") && player2Move.equals("가위")) ||
                   (player1Move.equals("보") && player2Move.equals("바위"))) {
            result = "player1 승리";
        } else {
            result = "player2 승리";
        }

        handleGameResult(player1ChatId, player2ChatId, result);
    }

    private void handleGameResult(long player1ChatId, long player2ChatId, String result) {
        if ("무승부".equals(result)) {
            bot.sendTextMessage(player1ChatId, "무승부입니다. 다시 선택해주세요.");
            bot.sendTextMessage(player2ChatId, "무승부입니다. 다시 선택해주세요.");
            startRpsGame(player1ChatId, player2ChatId);
        } else if ("player2 승리".equals(result)) {
            swapWaitNumbers(player1ChatId, player2ChatId);
            String messageEmoji = EmojiParser.parseToUnicode("패배했습니다. 대기 순서가 뒤로 밀립니다.\n🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪");
            String messageEmoji1 = EmojiParser.parseToUnicode("승리했습니다! 대기 순서가 앞당겨집니다.\n😆😆😆😆😆😆😆😆😆😆😆😆😆😆😆😆😆😆😆😆 ");
            bot.sendTextMessage(player2ChatId, messageEmoji1);
            bot.sendTextMessage(player1ChatId, messageEmoji);
            gameStates.remove(player2ChatId);
            gameStates.remove(player1ChatId);
        } else {
            bot.refundPoints(player1ChatId, 2000);
            String messageEmoji = EmojiParser.parseToUnicode("패배했습니다. 대기 순서는 변동 없습니다.\n🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪🤪");
            String messageEmoji1 = EmojiParser.parseToUnicode("승리했습니다! 2000 포인트를 획득했습니다.\n😆😆😆😆😆😆😆😆😆😆😆😆😆😆😆😆😆😆😆😆");
            bot.sendTextMessage(player2ChatId, messageEmoji);
            bot.sendTextMessage(player1ChatId, messageEmoji1);
            gameStates.remove(player2ChatId);
            gameStates.remove(player1ChatId);
        }
    }

    private void swapWaitNumbers(long player1ChatId, long player2ChatId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseCon.getConnection();
            String sql = "UPDATE WAIT SET WAIT_NUMBER = CASE " +
                "WHEN WAIT_CHATID = ? THEN WAIT_NUMBER + 1 " +  // player1의 번호를 1 증가
                "WHEN WAIT_CHATID = ? THEN WAIT_NUMBER - 1 " +  // player2의 번호를 1 감소
                "END " +
                "WHERE WAIT_CHATID IN (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, player1ChatId);
            pstmt.setLong(2, player2ChatId);
            pstmt.setLong(3, player1ChatId);
            pstmt.setLong(4, player2ChatId);
            int updatedRows = pstmt.executeUpdate();
            
            System.out.println("Updated rows: " + updatedRows); // 디버깅을 위한 로그
            
            if (updatedRows != 2) {
                System.out.println("예상치 못한 업데이트 결과: " + updatedRows + " 행이 업데이트됨");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static class GameState {
        enum State {
            WAITING_RESPONSE, CHOOSING
        }

        private long opponentChatId;
        private State state;
        private String move;

        GameState(long opponentChatId, State state) {
            this.opponentChatId = opponentChatId;
            this.state = state;
        }

        public long getOpponentChatId() {
            return opponentChatId;
        }

        public State getState() {
            return state;
        }

        public String getMove() {
            return move;
        }

        public void setMove(String move) {
            this.move = move;
        }
    }
}