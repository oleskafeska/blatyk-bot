package com.blatyk.bot.handler;

import com.blatyk.bot.data.Bot;
import com.blatyk.bot.tools.ParsedCommand;

import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractHandler {

  Bot bot;

  AbstractHandler(Bot bot) {
    this.bot = bot;
  }

  public abstract String operate(String chatId, ParsedCommand parsedCommand, Update update);
}
