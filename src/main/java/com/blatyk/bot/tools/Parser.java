package com.blatyk.bot.tools;

import com.vdurmont.emoji.EmojiParser;

import org.glassfish.grizzly.utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Parser {

  private static final Logger log = LoggerFactory.getLogger(Parser.class);
  private final String PREFIX_FOR_COMMAND = "/";
  private final String DELIMITER_COMMAND_BOTNAME = "@";
  private String botName;

  public Parser(String botName) {
    this.botName = botName;
  }

  public ParsedCommand getParsedCommand(String text) {
    String trimText = "";
    if (text != null) trimText = text.trim();
    ParsedCommand result = new ParsedCommand(Command.NONE, trimText);

    if ("".equals(trimText)) return result;
    Pair<String, String> commandAndText = getDelimitedCommandFromText(trimText);
    if (isCommand(commandAndText.getFirst())) {
      if (isCommandForMe(commandAndText.getFirst())) {
        String commandForParse = cutCommandFromFullText(commandAndText.getFirst());
        Command commandFromText = getCommandFromText(commandForParse);
        result.setText(commandAndText.getSecond());
        result.setCommand(commandFromText);
      } else {
        result.setCommand(Command.NOTFORME);
        result.setText(commandAndText.getSecond());
      }

    }
    if (result.getCommand() == Command.NONE) {
      List<String> emojiContainsInText = EmojiParser.extractEmojis(result.getText());
      if (emojiContainsInText.size() > 0) result.setCommand(Command.TEXT_CONTAIN_EMOJI);
    }
    return result;
  }

  private String cutCommandFromFullText(String text) {
    return text.contains(DELIMITER_COMMAND_BOTNAME) ?
        text.substring(1, text.indexOf(DELIMITER_COMMAND_BOTNAME)) :
        text.substring(1);
  }

  private Command getCommandFromText(String text) {
    String upperCaseText = text.toUpperCase().trim();
    Command command = Command.NONE;
    try {
      command = Command.valueOf(upperCaseText);
    } catch (IllegalArgumentException e) {
      log.debug("Can't parse command: " + text);
    }
    return command;
  }

  private Pair<String, String> getDelimitedCommandFromText(String trimText) {
    Pair<String, String> commandText;

    if (trimText.contains(" ")) {
      int indexOfSpace = trimText.indexOf(" ");
      commandText = new Pair<>(trimText.substring(0, indexOfSpace), trimText.substring(indexOfSpace + 1));
    } else commandText = new Pair<>(trimText, "");
    return commandText;
  }

  private boolean isCommandForMe(String command) {
    if (command.contains(DELIMITER_COMMAND_BOTNAME)) {
      String botNameForEqual = command.substring(command.indexOf(DELIMITER_COMMAND_BOTNAME) + 1);
      return botName.equals(botNameForEqual);
    }
    return true;
  }

  private boolean isCommand(String text) {
    return text.startsWith(PREFIX_FOR_COMMAND);
  }
}
