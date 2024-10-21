package telegramcode;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    private static final String BOT_TOKEN = "7385961763:AAHE7WiJkyWqZhovLFnc7R9kvGcPnCOY4pQ";

    public static void main(String[] args) {
        // 봇 등록 및 실행
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new waitingBot(BOT_TOKEN));
            System.out.println("WaitingBot successfully started!");
        } catch (TelegramApiException e) {
            System.err.println("Error occurred while starting the bot: " + e.getMessage());
            e.printStackTrace();
            return; // 봇 시작 실패 시 프로그램 종료
        }

        // 웨이팅 알림 처리를 위한 별도 스레드 시작
        Thread notificationThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                sendNoti.processNewWaitingNotifications();
                sendNoti.processWaitNumberOneNotifications();
                try {
                    // 10초마다 새로운 웨이팅 확인
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Notification thread was interrupted: " + e.getMessage());
                }
            }
        });
        notificationThread.start();

        // 메인 스레드는 계속 실행 상태 유지
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.err.println("Main thread was interrupted: " + e.getMessage());
            notificationThread.interrupt(); // 알림 스레드도 중단
        }
    }
}