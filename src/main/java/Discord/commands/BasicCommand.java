package Discord.commands;

import Discord.Main;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.json.JSONObject;

public class BasicCommand {
	String gid, cid, guildLang;
	TextChannel channel;
	JSONObject guildLangObject;
	SlashCommandInteractionEvent event;

	public BasicCommand(SlashCommandInteractionEvent event) {
		this.event = event;
		assert event.getGuild() != null : "Guild is null";
		gid = event.getGuild().getId();
		cid = event.getChannel().getId();
		channel = event.getChannel().asTextChannel();
		guildLang = "english";
		if (Main.bindsObject.has(gid) && Main.bindsObject.getJSONObject(gid).has("lang")) guildLang = Main.bindsObject.getJSONObject(gid).getString("lang");
		guildLangObject = Main.lang.getJSONObject(guildLang);
	}
	
	protected void sendMessage(String message) {
		event.reply(guildLangObject.getString(message)).queue();
	}
	
	protected void sendMessage(String message, String replacement) {
		event.reply(guildLangObject.getString(message).replace("{}", replacement)).queue();
	}
}
