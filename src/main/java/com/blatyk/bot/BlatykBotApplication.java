package com.blatyk.bot;

import com.blatyk.bot.data.Bot;
import com.blatyk.bot.service.MessageReceiver;
import com.blatyk.bot.service.MessageSender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootApplication
public class BlatykBotApplication {

  private static final int PRIORITY_FOR_SENDER = 1;
  private static final int PRIORITY_FOR_RECEIVER = 3;
  private static final String BOT_ADMIN = "321644283";

  public static void main(String[] args) {
    SpringApplication.run(BlatykBotApplication.class, args);

    ApiContextInitializer.init();
    Bot blatyk_bot = new Bot("blatyk_bot", "1349433672:AAFKe_6-jnAIw0ym5a511M_sbfCQ-3fTP8M");

    MessageReceiver messageReciever = new MessageReceiver(blatyk_bot);
    MessageSender messageSender = new MessageSender(blatyk_bot);

    try {
      blatyk_bot.botConnect();
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }

    Thread receiver = new Thread(messageReciever);
    receiver.setDaemon(true);
    receiver.setName("MsgReciever");
    receiver.setPriority(PRIORITY_FOR_RECEIVER);
    receiver.start();

    Thread sender = new Thread(messageSender);
    sender.setDaemon(true);
    sender.setName("MsgSender");
    sender.setPriority(PRIORITY_FOR_SENDER);
    sender.start();

    sendStartReport(blatyk_bot);
  }

  private static void sendStartReport(Bot bot) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(BOT_ADMIN);
    sendMessage.setText("Borys bot has born");
    bot.sendQueue.add(sendMessage);
  }

}
