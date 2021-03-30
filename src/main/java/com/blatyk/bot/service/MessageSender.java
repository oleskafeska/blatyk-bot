package com.blatyk.bot.service;

import com.blatyk.bot.entity.Bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.Data;

@Data
public class MessageSender implements Runnable {

  private static final Logger log = LoggerFactory.getLogger(MessageReceiver.class);

  private final int SENDER_SLEEP_TIME = 1000;
  private Bot bot;

  public MessageSender(Bot bot) {
    this.bot = bot;
  }
  @Override
  public void run() {

    log.info("[STARTED] message Sender.  Bot class: " + bot);
    try {
      while (true) {
        for (Object object = bot.sendQueue.poll(); object != null; object = bot.sendQueue.poll()) {
          log.debug("Get new msg to send " + object);
          send(object);
        }
        try {
          Thread.sleep(SENDER_SLEEP_TIME);
        } catch (InterruptedException e) {
          log.error("Take interrupt while operate msg list", e);
        }
      }
    } catch (Exception e) {
      System.out.println(e);
    }

  }

  private void send(Object object) {
    try {
      MessageType messageType = messageType(object);
      switch (messageType) {
        case EXECUTE:
          BotApiMethod<Message> message = (BotApiMethod<Message>) object;
          log.debug("Use Execute for " + object);
          bot.execute(message);
          break;
        default:
          log.warn("Cant detect type of object. " + object);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private MessageType messageType(Object object) {
    if (object instanceof SendSticker) return MessageType.STICKER;
    if (object instanceof BotApiMethod) return MessageType.EXECUTE;
    return MessageType.NOT_DETECTED;
  }

  enum MessageType {
    EXECUTE, STICKER, NOT_DETECTED,
  }
}
