package Discord.commands;

import Discord.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class AdminMessagecommand extends BasicCommand {
	public AdminMessagecommand (SlashCommandInteractionEvent event) {
		super(event);
		Main.sendToAll(event.getOption("text").getAsString());
		event.reply("ok").queue();
	}
}
