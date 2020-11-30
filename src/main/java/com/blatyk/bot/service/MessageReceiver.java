package com.blatyk.bot.service;

import com.blatyk.bot.entity.Bot;
import com.blatyk.bot.handler.AbstractHandler;
import com.blatyk.bot.handler.DefaultHandler;
import com.blatyk.bot.handler.EmojiHandler;
import com.blatyk.bot.handler.StickerHandler;
import com.blatyk.bot.handler.SystemHandler;
import com.blatyk.bot.tools.Command;
import com.blatyk.bot.tools.ParsedCommand;
import com.blatyk.bot.tools.Parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;

import lombok.Data;

@Data
public class MessageReceiver implements Runnable {

  private static final Logger log = LoggerFactory.getLogger(MessageReceiver.class);

  private final int WAIT_FOR_NEW_MESSAGE_DELAY = 1000;

  private Bot bot;
  private Parser parser;

  public MessageReceiver(Bot bot) {
    this.bot = bot;
    parser = new Parser(bot.getBotUsername());
  }

  @Override
  public void run() {
    log.info("Message receiver has started. Bot : " + bot);
    while (true) {

      processReceiveQueue();

      try {
        Thread.sleep(WAIT_FOR_NEW_MESSAGE_DELAY);
      } catch (InterruptedException e) {
        log.error("Catch interrupt. Exit", e);
        return;
      }
    }
  }

  private void processReceiveQueue() {
    Object object;

    while ((object = bot.receiveQueue.poll()) != null) {
      log.debug("New object for analyze in queue " + object.toString());
      analyzeReceiveMessage(object);
    }
  }

  private void analyzeReceiveMessage(Object object) {

    if (object instanceof Update) {
      Update update = (Update) object;
      log.debug("Update received: " + update.toString());
      checkUpdateType(update);
    } else {
      log.warn("Cant operate type of object: " + object.toString());
    }
  }

  private void checkUpdateType(Update update) {

    Message message = update.getMessage();

    Long chatId = message.getChatId();

    ParsedCommand parsedCommand = new ParsedCommand(Command.NONE, "");

    if (message.hasText()) {
      parsedCommand = parser.getParsedCommand(message.getText());
    } else {
      Sticker sticker = message.getSticker();
      if (sticker != null) {
        parsedCommand = new ParsedCommand(Command.STICKER, sticker.getFileId());
      }
    }

    AbstractHandler handlerForCommand = getHandlerForCommand(parsedCommand.getCommand());

    String operationResult = handlerForCommand.operate(chatId.toString(), parsedCommand, update);

    if (!operationResult.equals("")) {
      SendMessage messageOut = new SendMessage();
      messageOut.setChatId(chatId);
      messageOut.setText(operationResult);
      bot.sendQueue.add(messageOut);
    }
  }

  private AbstractHandler getHandlerForCommand(Command command) {
    if (command == null) {
      log.warn("Null command accepted. This is not good scenario.");
      return new DefaultHandler(bot);
    }
    switch (command) {
      case START:
      case HELP:
      case QUOTE: {
        SystemHandler systemHandler = new SystemHandler(bot);
        log.info("Handler for command [" + command.toString() + "] is: " + systemHandler);
        return systemHandler;
      }
      case STICKER: {
        StickerHandler stickerHandler = new StickerHandler(bot);
        log.info("Sticker handler triggered");
        return stickerHandler;
      }
      case TEXT_CONTAINS_EMOJI: {
        EmojiHandler emojiHandler = new EmojiHandler(bot);
        log.info("Handler for command [" + command.toString() + "] is: " + emojiHandler);
        return emojiHandler;
      }
      default: {
        log.info("Handler for command [" + command.toString() + "] not Set. Return DefaultHandler");
        return new DefaultHandler(bot);
      }
    }
  }
}
