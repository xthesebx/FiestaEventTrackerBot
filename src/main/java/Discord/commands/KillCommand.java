package Discord.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import static java.lang.System.exit;

public class KillCommand extends BasicCommand {
	public KillCommand (SlashCommandInteractionEvent event) {
		super(event);
		event.reply("ok").queue();
		exit(0);
	}
}
