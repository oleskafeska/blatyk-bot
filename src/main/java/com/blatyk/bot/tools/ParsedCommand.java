package com.blatyk.bot.tools;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParsedCommand {
  Command command = Command.NONE;
  String text="";
}
