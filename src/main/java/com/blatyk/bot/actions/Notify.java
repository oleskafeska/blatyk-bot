package com.blatyk.bot.actions;

import com.blatyk.bot.data.Bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class Notify implements Runnable {

  private static final Logger log = LoggerFactory.getLogger(Notify.class);
  private static final int MILLISEC_IN_SEC = 1000;

  Bot bot;
  long delayInMillisec;
  String chatID;

  public Notify(Bot bot, String chatID, long delayInMillisec) {
    this.bot = bot;
    this.chatID = chatID;
    this.delayInMillisec = delayInMillisec;
    log.debug("CREATE. " + toString());
  }


  @Override
  public void run() {

    log.info("RUN. " + toString());
    bot.sendQueue.add(getFirstMessage());
    try {
      Thread.sleep(delayInMillisec);
      bot.sendQueue.add(Stickers.FUNNY_JIM_CARREY.getSendSticker(chatID));
    } catch (InterruptedException e) {
      log.error(e.getMessage(), e);
    }
    log.info("FIHISH. " + toString());
  }

  private SendMessage getFirstMessage() {
    return new SendMessage(chatID, "I will send you notify after " + delayInMillisec / MILLISEC_IN_SEC + "sec");
  }

  private SendMessage getSecondMessage() {
    return new SendMessage(chatID, "This is notify message. Thanks for using :)");
  }
}