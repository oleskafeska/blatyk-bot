package com.blatyk.bot.handler;

import com.blatyk.bot.data.Bot;
import com.blatyk.bot.tools.ParsedCommand;

import org.telegram.telegrambots.meta.api.objects.Update;


public class DefaultHandler extends AbstractHandler {

  public DefaultHandler(Bot bot) {
    super(bot);
  }

  @Override
  public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
    return "";
  }
}
