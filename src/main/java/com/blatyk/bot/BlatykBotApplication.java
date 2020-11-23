package com.blatyk.bot;

import com.blatyk.bot.entity.Bot;
import com.blatyk.bot.service.MessageReceiver;
import com.blatyk.bot.service.MessageSender;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class BlatykBotApplication {

  private static final int PRIORITY_FOR_SENDER = 1;
  private static final int PRIORITY_FOR_RECEIVER = 3;
  private static final String BOT_ADMIN = "-360246826";
  private static final String TOKEN = "1349433672:AAFKe_6-jnAIw0ym5a511M_sbfCQ-3fTP8M";
  private static final String BOT_NAME = "blatyk_bot";

  public static void main(String[] args) {
    ApiContextInitializer.init();
    Bot bot = new Bot(BOT_NAME, TOKEN);

    MessageReceiver messageReceiver = new MessageReceiver(bot);
    MessageSender messageSender = new MessageSender(bot);

    bot.botConnect();

    Thread receiver = new Thread(messageReceiver);
    receiver.setDaemon(true);
    receiver.setName("MsgReceiver");
    receiver.setPriority(PRIORITY_FOR_RECEIVER);
    receiver.start();

    Thread sender = new Thread(messageSender);
    sender.setDaemon(true);
    sender.setName("MsgSender");
    sender.setPriority(PRIORITY_FOR_SENDER);
    sender.start();

    sendStartReport(bot);
  }

  private static void sendStartReport(Bot bot) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(BOT_ADMIN);
    sendMessage.setText("Borys bot has born. Or redeployed. Whatever...");
    bot.sendQueue.add(sendMessage);
  }
}
