package com.blatyk.bot.handler;

import com.blatyk.bot.entity.Bot;
import com.blatyk.bot.tools.ParsedCommand;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

public class StickerHandler extends AbstractHandler {

  private static final String RIY_STICKER_FILE_ID = "CAACAgIAAxkBAAPjX7zj_FB5nm9_4xOz3PtUbiOvrqgAAh0AAyS5Jxi91Ycd3kDpGR4E";

  public StickerHandler(Bot bot) {
    super(bot);
  }

  @Override
  public String operate(String chatId, ParsedCommand parsedCommand, Update update) {

    return "Стікери якісь тут шлете...Йдіть працюйте";
  }
}
