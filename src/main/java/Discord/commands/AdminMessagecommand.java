package Discord.commands;

import Discord.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class AdminMessagecommand extends BasicCommand {
	public AdminMessagecommand (SlashCommandInteractionEvent event) {
		super(event);
		OptionMapping optionMapping = event.getOption("text");
		assert optionMapping != null;
		Main.sendToAll(optionMapping.getAsString());
		event.reply("ok").queue();
	}
}
