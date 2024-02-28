package Discord;

import com.hawolt.logger.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.FileUpload;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.util.concurrent.CompletableFuture;

public class Main extends ListenerAdapter {
	
	JDA jda;
	JSONObject bindsObject, pics, lang;
	CheckEvent eventCheck;
	boolean muted, debug;
	
	public static void main (String[] args) throws AWTException, IOException, InterruptedException {
		new Main();
	}
	
	public Main() throws AWTException, IOException, InterruptedException {
		File env = new File("apikey.env");
		InputStream inputStream = new FileInputStream(env);
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br
				     = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		}
		jda = JDABuilder.createDefault(resultStringBuilder.toString().strip()).enableIntents(GatewayIntent.MESSAGE_CONTENT).enableIntents(GatewayIntent.GUILD_MESSAGES).build();
		jda.addEventListener(this);
		jda.awaitReady();
		URL path = Main.class.getResource("Main.class");
		String text = "Tracking Fiesta Events on Khazul";
		if (path != null && path.toString().startsWith("file")) {
			text = "Wartungsarbeiten";
			debug = true;
		}
		jda.getPresence().setActivity(Activity.customStatus(text));
		bindsObject = readBinds();
		readPics();
		lang = readLang();
		eventCheck = new CheckEvent(jda, bindsObject, pics, this, lang, debug);
		eventCheck.start();
	}
	
	@Override
	public void onMessageReceived (MessageReceivedEvent event) {
		if (jda.getSelfUser().getName().equals(event.getAuthor().getName()) || event.getAuthor().isBot()) return;
		Logger.debug(event.getMessage().getContentRaw());
		String s = event.getMessage().getContentRaw();
		String gid = event.getGuild().getId();
		String cid = event.getChannel().getId();
		TextChannel channel = event.getChannel().asTextChannel();
		if (s.equals("?bind")) {
			Logger.info("binding");
			if (!bindsObject.has(gid)) bindsObject.put(gid, new JSONObject());
			bindsObject.getJSONObject(gid).put("bind", cid);
			writeBinds();
			channel.sendMessage("binding Channel").queue();
		} else if(s.equals("?event")) {
			Logger.info("printing current event");
			channel.sendMessage("Current Event: " + eventCheck.currentEvent).queue();
		} else if(s.equals("?unbind")) {
			Logger.info("unbinding");
			removeBind(event.getGuild().getId());
			channel.sendMessage("unbinding Channel").queue();
		} else if (s.contains("?language")) {
			String sprache = s.substring(s.indexOf(" ") + 1).toLowerCase();
			if (!sprache.equals("deutsch") && !sprache.equals("englisch") && !sprache.equals("debug")) {
				channel.sendMessage("Sprache unbekannt, nutze englisch oder deutsch als sprache").queue();
				return;
			}
			if (!bindsObject.has(gid)) bindsObject.put(gid, new JSONObject());
			bindsObject.getJSONObject(gid).put("lang", sprache);
			writeBinds();
		} else if (s.equals("?help") || s.equals("?commands")) {
			Logger.info("command list");
			channel.sendMessage("Commands:\n?bind bind Bot to this channel\n?event print current event\n?unbind unbind bot from channel").queue();
		}
		if (event.getAuthor().getId().equals("277064996264083456")) {
			if (s.contains("?save")) {
				String messageid = s.substring(s.indexOf(" ") + 1);
				String pic = messageid.substring(messageid.indexOf(" ") + 1);
				String message = pic;
				
				messageid = messageid.substring(0, messageid.indexOf(" "));
				pic = pic.substring(0, pic.indexOf(" "));
				message = message.substring(message.indexOf(" ") + 1);
				
				MessageHistory history = channel.getHistoryAround(messageid, 10).complete();
				Message message1 = history.getMessageById(messageid);
				File file = new File("eventimages\\" + pic);
				try {
					CompletableFuture<File> future = message1.getAttachments().get(0).getProxy().downloadToFile(file);
					while (!future.isDone()) try {
						Thread.sleep(10);
					} catch (InterruptedException ignored) {
					}
				} catch (NullPointerException e) {
					channel.sendMessage("No Image found in messageid, maybe wrong id or wrong order? Syntax: ?send [messageid] [filename] [message]").queue();
				}
				try {
					writePics(pic, message);
					channel.sendMessage("Picture successfully saved as " + pic + " sending the message " + message).queue();
				} catch (IOException e) {
					if (e instanceof FileAlreadyExistsException) {
						channel.sendMessage("Picture already exists with that name").queue();
					}
					Logger.error(e);
				}
			} else if (s.equals("?list")) {
				StringBuilder message = new StringBuilder();
				for (String key : pics.keySet()) {
					message.append(key);
					message.append(" : ");
					message.append(pics.getString(key));
					message.append("\n");
				}
				channel.sendMessage(message.toString()).queue();
			} else if (s.contains("?edit")) {
				String png = s.substring(s.indexOf(" ") + 1);
				String message = png.substring(png.indexOf(" ") + 1);
				png = png.substring(0, png.indexOf(message) - 1);
				try {
					if (pics.has(png)) {
						BufferedWriter writer = new BufferedWriter(new FileWriter("pics.json"));
						pics.put(png, message);
						writer.write(pics.toString());
						writer.close();
					}
				} catch (IOException e) {
					Logger.error(e);
				}
			} else if (s.contains("?remove")) {
				String png = s.substring(s.indexOf(" ") + 1);
				File f = new File(png);
				if (f.exists()) f.delete();
				if (pics.has(png)) pics.remove(png);
			} else if (s.equals("?stop")) {
				eventCheck.running = false;
			} else if (s.contains("?show")) {
				String image = s.substring(s.indexOf(" ")+1);
				channel.sendMessage("Here is " + image).addFiles(FileUpload.fromData(new File("eventimages\\" + image))).queue();
			} else if (s.equals("?start")) {
				eventCheck.running = true;
			} else if (s.equals("?mute")) {
				muted = !muted;
			}
		}
	}
	
	
	private JSONObject readBinds () {
		File file = new File("binds.json");
		System.out.println(file.getAbsolutePath());
		StringBuilder text = read(file);
		return (new JSONObject(text.toString()));
	}
	
	private void writeBinds() {
		File file = new File("binds.json");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			writer.write(bindsObject.toString());
			writer.close();
		} catch (IOException e) {
			Logger.error(e);
		}
	}
	
	private void removeBind(String guild) {
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
	
	private void writePics(String pic, String message) throws IOException {
	File file = new File("pics.json");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
		if (pics.has(pic)) {
			throw new FileAlreadyExistsException(pic);
		}
		pics.put(pic, message);
		writer.write(pics.toString());
		writer.close();
	}
	
	public void readPics () {
		File file = new File("pics.json");
		StringBuilder text = read(file);
		pics = new JSONObject(text.toString());
	}
	
	public JSONObject readLang () {
		File file = new File("src/main/resources/language.json");
		StringBuilder text = read(file);
		return new JSONObject(text.toString());
	}
	
	private StringBuilder read (File file) {
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
}
