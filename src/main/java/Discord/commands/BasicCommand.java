package Discord.commands;

import Discord.Main;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;

public class BasicCommand {
	String gid, cid, guildLang;
	TextChannel channel;
	JSONObject guildLangObject;
	public BasicCommand(MessageReceivedEvent event) {
		gid = event.getGuild().getId();
		cid = event.getChannel().getId();
		channel = event.getChannel().asTextChannel();
		guildLang = "englisch";
		if (Main.bindsObject.has(gid) && Main.bindsObject.getJSONObject(gid).has("lang")) guildLang = Main.bindsObject.getJSONObject(gid).getString("lang");
		guildLangObject = Main.lang.getJSONObject(guildLang);
	}
	
	protected void sendMessage(String message) {
		channel.sendMessage(guildLangObject.getString(message)).queue();
	}
	
	protected void sendMessage(String message, String replacement) {
		channel.sendMessage(guildLangObject.getString(message).replace("{}", replacement)).queue();
	}
}
