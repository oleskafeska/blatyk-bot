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

    if (Objects.nonNull(update.getMessage().getSticker())) {

     if (update.getMessage().getSticker().getFileId().equals(RIY_STICKER_FILE_ID)) {
        return "Та шо ви мені тут Рійчика шлете";
      }
      return "";
    }
    return "";
  }
}
