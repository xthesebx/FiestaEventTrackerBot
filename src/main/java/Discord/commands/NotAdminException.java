package Discord.commands;

public class NotAdminException extends Exception {
	public NotAdminException () {
		super("User is not Admin");
	}
}
