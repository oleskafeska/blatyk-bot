package com.blatyk.bot.tools;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParsedCommand {
  private Command command;
  private String text;
}
