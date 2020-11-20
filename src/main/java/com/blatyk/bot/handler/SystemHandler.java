package com.blatyk.bot.handler;

import com.blatyk.bot.data.Bot;
import com.blatyk.bot.tools.Command;
import com.blatyk.bot.tools.ParsedCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.blatyk.bot.tools.Command.KEK;

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
        return "Your telegramID: " + update.getMessage().getFrom().getId();
      case STICKER:
        return "StickerID: " + parsedCommand.getText();
      case KEK:
        return "Ще троха сміємося, чи йдемо деплоїти?";
    }
    return "";
  }

  private SendMessage getMessageHelp(String chatID) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatID);
    sendMessage.enableMarkdown(true);

    StringBuilder text = new StringBuilder();
    text.append("*This is help message*").append(END_LINE).append(END_LINE);
    text.append("[/start](/start) - show start message").append(END_LINE);
    text.append("[/help](/help) - show help message").append(END_LINE);
    text.append("[/id](/id) - know your ID in telegram ").append(END_LINE);
    text.append("/*notify* _time-in-sec_  - receive notification from me after the specified time").append(END_LINE);

    sendMessage.setText(text.toString());
    return sendMessage;
  }

  private SendMessage getMessageStart(String chatID) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatID);
    sendMessage.enableMarkdown(true);
    StringBuilder text = new StringBuilder();
    text.append("Hello. I'm  *").append(bot.getBotName()).append("*").append(END_LINE);
    text.append("All that I can do - you can see calling the command [/help](/help)");
    sendMessage.setText(text.toString());
    return sendMessage;
  }
}
