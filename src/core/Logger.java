package core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Log messages to a file.
 * 
 * @author Tanner Smith
 */
public class Logger {
	private static String file = "log.txt";
	public static Level currentLevel = Level.INFO;
	
	public static enum Level {
		NONE (0, "NONE"),
		INFO (1, "INFO"),
		DEBUG (2, "DEBUG"),
		WARNING (3, "WARNING"),
		ERROR (4, "ERROR");
		
		private int level;
		private String name = null;
		
		Level(int level) {
			this.level = level;
		}
		
		Level(int level, String name) {
			this(level);
			this.name = name;
		}
		
		public int getLevel() {
			return level;
		}
		
		public String getName() {
			return name;
		}
	}
	
	/**
	 * Logs a message to a file if the log level given matches the current log level.
	 * @param message Message to be logged
	 * @param level Level when this should be logged
	 */
	public static void log(String message, Level level) {
		// If you try to log at level none, we deny you
		if (level == Level.NONE) return;
		
		if (level == currentLevel || level.getLevel() < currentLevel.getLevel()) {
			try {
				FileWriter fstream = new FileWriter(file, true);
				BufferedWriter out = new BufferedWriter(fstream);
				
				Date now = new Date();
				String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSS z").format(now);
				
				String output = level.getName() + " - " + date + " - " + message+"\n";
				
				out.write(output);
				System.out.print(output);
				
				out.close();
				fstream.close();
			} catch (Exception e) {
				System.err.println("Logger could not log message: " + e.getMessage());
			}
		}
	}
}
