package com.blatyk.bot.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Bot extends TelegramLongPollingBot {

  private static final Logger log = LoggerFactory.getLogger(Bot.class);
  private static final int RECONNECT_PAUSE = 10000;
  public final Queue<Object> sendQueue = new ConcurrentLinkedQueue<>();
  public final Queue<Object> receiveQueue = new ConcurrentLinkedQueue<>();

  private final String botName;

  private final String token;

  public Bot(String botName, String token) {
    this.botName = botName;
    this.token = token;
  }

  @Override
  public String getBotUsername() {
    return botName;
  }

  @Override
  public String getBotToken() {
    return token;
  }

  @Override
  public void onUpdateReceived(Update update) {

    log.debug("Receive new Update. updateID: " + update.getUpdateId());
    receiveQueue.add(update);
  }

  public void botConnect() {
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

    try {
      telegramBotsApi.registerBot(this);
      log.info("[STARTED] Telegram API. Bot Connected. Bot class: " + this);
    } catch (TelegramApiRequestException e) {
      log.error("Cant Connect. Pause " + RECONNECT_PAUSE / 1000 + "sec and try again. Error: " + e.getMessage());
      try {
        Thread.sleep(RECONNECT_PAUSE);
      } catch (InterruptedException e1) {
        e1.printStackTrace();
        return;
      }
      botConnect();
    }
  }
}
