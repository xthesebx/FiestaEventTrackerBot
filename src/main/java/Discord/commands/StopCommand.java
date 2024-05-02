package Discord.commands;

import Discord.Main;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class StopCommand extends BasicCommand {
	public StopCommand (SlashCommandInteractionEvent event) {
		super(event);
		Main.running = false;
		Main.debug = true;
		Main.jda.getPresence().setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.customStatus("Maintenance"));
		event.reply("ok").queue();
	}
}
