package Main;
public class LogManager {
	private String fileName;
	private String logDirPath;
	private String logExt;
	LogManager(String logDirPath, String logExt) throws ExitException {
		this.logDirPath = logDirPath;
		this.logExt = logExt;
		if (!makeLogFile(TimeController.getToday())) {
			throw new ExitException();
		}
		String content = "        -----------------------------\r\n---- START AUTO TRADE AGENT " + TimeController.getCurrentTime()
				+ " ----\r\n        -----------------------------\r\n";
		log(content);
	}
	
	boolean makeLogFile(String today) {
		fileName = today.replace("/", "");
		if (Library.createFile(logDirPath + fileName + logExt)
				&& Library.fileWrite(logDirPath + fileName + logExt, "", true)) {
			return true;
		}
		return false;
	}

	boolean log(String content) {
		content += "\n";
		return Library.fileWrite(logDirPath + fileName + logExt, content, true);
	}
}

