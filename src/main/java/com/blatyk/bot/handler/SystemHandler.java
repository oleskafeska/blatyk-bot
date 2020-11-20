package com.blatyk.bot.handler;

import com.blatyk.bot.entity.Bot;
import com.blatyk.bot.tools.Command;
import com.blatyk.bot.tools.ParsedCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SystemHandler extends AbstractHandler {

  private static final Logger log = LoggerFactory.getLogger(SystemHandler.class);

  private final String END_LINE = "\n";

  public SystemHandler(Bot bot) {
    super(bot);
  }

  @Override
  public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
    Command command = parsedCommand.getCommand();

    switch (command) {
      case START:
        bot.sendQueue.add(getMessageStart(chatId));
        break;
      case HELP:
        bot.sendQueue.add(getMessageHelp(chatId));
        break;
      case ID:
        return "Your telegram id: " + update.getMessage().getFrom().getId();
      case STICKER:
        return "Sticker id: " + parsedCommand.getText();
      case QUOTE:
        return "Ще троха сміємося, чи йдемо деплоїти?";
    }
    return "";
  }

  private SendMessage getMessageHelp(String chatId) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);
    sendMessage.enableMarkdown(true);

    String text =
        "*This is help message*"
            + END_LINE
            + END_LINE
            + "[/start](/start) - show start message"
            + END_LINE
            + "[/help](/help) - show help message"
            + END_LINE
            + "[/id](/id) - know your ID in telegram "
            + END_LINE
            + "/*notify* _time-in-sec_  - receive notification from me after the specified time"
            + END_LINE;
    sendMessage.setText(text);
    return sendMessage;
  }

  private SendMessage getMessageStart(String chatID) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatID);
    sendMessage.enableMarkdown(true);
    String text =
        "Hello. I'm  *"
            + bot.getBotName()
            + "*"
            + END_LINE
            + "All that I can do - you can see calling the command [/help](/help)";
    sendMessage.setText(text);
    return sendMessage;
  }
}
