package Discord;

import Discord.commands.*;
import com.hawolt.logger.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.SessionDisconnectEvent;
import net.dv8tion.jda.api.events.session.SessionRecreateEvent;
import net.dv8tion.jda.api.events.session.SessionResumeEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.URL;

public class Main extends ListenerAdapter {
	
	public static JDA jda;
	public static JSONObject bindsObject, pics, lang;
	public static CheckEvent eventCheck;
	public static boolean running = true, debug;
	
	public static void main (String[] args) throws AWTException, IOException, InterruptedException {
		new Main();
	}
	
	public Main() throws AWTException, IOException, InterruptedException {
		File env = new File("apikey.env");
		jda = JDABuilder.createDefault(read(env).toString().strip()).enableIntents(GatewayIntent.MESSAGE_CONTENT).enableIntents(GatewayIntent.GUILD_MESSAGES).setStatus(OnlineStatus.OFFLINE).setAutoReconnect(true).build();
		jda.addEventListener(this);
		jda.awaitReady();
		URL path = Main.class.getResource("Main.class");
		String text = "Tracking Fiesta Events on Khazul";
		OnlineStatus status = OnlineStatus.ONLINE;
		if (path != null && path.toString().startsWith("file")) {
			text = "Maintenance";
			status = OnlineStatus.DO_NOT_DISTURB;
			debug = true;
			running = false;
		}
		bindsObject = readBinds();
		readPics();
		lang = readLang();
		eventCheck = new CheckEvent(jda, bindsObject, pics, this, lang, debug);
		eventCheck.start();
		jda.getPresence().setPresence(status, Activity.customStatus(text));
	}

	@Override
	public void onSessionDisconnect(SessionDisconnectEvent event) {
		super.onSessionDisconnect(event);
		Logger.error("disconnected");
	}

	@Override
	public void onSessionRecreate(SessionRecreateEvent event) {
		super.onSessionRecreate(event);
		Logger.error("session recreated");
	}

	@Override
	public void onSessionResume(SessionResumeEvent event) {
		super.onSessionResume(event);
		Logger.error("session resumed");
	}

	@Override
	public void onSlashCommandInteraction (SlashCommandInteractionEvent event) {
		if (jda.getSelfUser().getName().equals(event.getUser().getName()) || event.getUser().isBot()) return;
		if (debug && !event.getUser().getId().equals("277064996264083456")) return;
		Logger.debug(event.getName());
		String s = event.getName();
		switch (s) {
			case "bind": {
				new BindCommand(event);
				break;
			}
			case "event": {
				new EventCommand(event);
				break;
			}
			case "unbind": {
				new UnbindCommand(event);
				break;
			}
			case "language": {
				new LanguageCommand(event);
				break;
			}
			case "credits": {
				new CreditsCommand(event);
				break;
			}
			case "help": {
				new HelpCommand(event);
				break;
			}
			case "stop": {
				new StopCommand(event);
				break;
			}
			case "start": {
				new StartCommand(event);
				break;
			}
			case "adminmessage": {
				new AdminMessagecommand(event);
				break;
			}
			case "kill": {
				new KillCommand(event);
				break;
			}
			case "restart": {
				new Thread(() -> {
					try {
						new RestartCommand(event);
					} catch (Exception e) {
						Logger.error(e);
					}
				}).start();
				break;
			}
		}
	}
	
	private JSONObject readBinds () {
		File file = new File("binds.json");
		StringBuilder text = read(file);
		return (new JSONObject(text.toString()));
	}
	
	public static void writeBinds() {
		File file = new File("binds.json");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			writer.write(bindsObject.toString());
			writer.close();
		} catch (IOException e) {
			Logger.error(e);
		}
	}
	
	public static void removeBind(String guild) {
		File file = new File("binds.json");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			bindsObject.getJSONObject(guild).remove("bind");
			writer.write(bindsObject.toString());
			writer.close();
		} catch (IOException e) {
			Logger.error(e);
		}
	}
	
	public static void readPics () {
		File file = new File("pics.json");
		StringBuilder text = read(file);
		pics = new JSONObject(text.toString());
	}
	
	public JSONObject readLang () {
		File file = new File("language.json");
		StringBuilder text = read(file);
		return new JSONObject(text.toString());
	}
	
	private static StringBuilder read (File file) {
		if (!file.exists()) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
				writer.write("{}");
				writer.close();
			} catch (IOException ignored) {
			}
		}
		StringBuilder text = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
			String temp;
			while (true) {
				temp = reader.readLine();
				if (temp == null) break;
				text.append(temp);
			}
			reader.close();
		} catch (IOException ignored) {
		}
		return text;
	}
	
	public static void sendToAll(String message) {
		for (String j : bindsObject.keySet()) {
			if (bindsObject.getJSONObject(j).has("bind")) {
				String s = bindsObject.getJSONObject(j).getString("bind");
				if (debug) s = "1178712493934252157";
				TextChannel channel = jda.getTextChannelById(s);
				if (channel == null) continue;
				channel.sendMessage(message).queue();
			}
		}
	}
	
	public static void sendToAllLangDependend(String message) {
		for (String j : bindsObject.keySet()) {
			if (bindsObject.getJSONObject(j).has("bind")) {
				String s = bindsObject.getJSONObject(j).getString("bind");
				if (debug) s = "1178712493934252157";
				TextChannel channel = jda.getTextChannelById(s);
				if (channel == null) continue;
				String output = "";
				String language = "english";
				if (bindsObject.getJSONObject(j).has("lang")) language = bindsObject.getJSONObject(j).getString("lang");
				output = lang.getJSONObject(language).getString(message);
				channel.sendMessage(output).queue();
			}
		}
	}
	
	public static void sendToAllLangDependend(String message, String replacement) {
		for (String j : bindsObject.keySet()) {
			if (bindsObject.getJSONObject(j).has("bind")) {
				String s = bindsObject.getJSONObject(j).getString("bind");
				if (debug) s = "1178712493934252157";
				TextChannel channel = jda.getTextChannelById(s);
				if (channel == null) continue;
				String latestMessageId = channel.getLatestMessageId();
				MessageHistory history = channel.getHistoryAround(latestMessageId, 1).complete();
				Message latestMessage = history.getMessageById(latestMessageId);
				if (!(latestMessage == null) && message.equals(latestMessage.getContentRaw())) return;
				String output = "";
				String language = "english";
				if (bindsObject.getJSONObject(j).has("lang")) language = bindsObject.getJSONObject(j).getString("lang");
				output = lang.getJSONObject(language).getString(message);
				channel.sendMessage(output.replace("{}", replacement)).queue();
			}
		}
	}
}
