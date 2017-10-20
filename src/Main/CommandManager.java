package Main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

public class CommandManager {
	private LogManager lm;
	private String cmdFilePath = "";
	CommandManager(LogManager lm, String cmdFilePath) {
		this.lm = lm;
		this.cmdFilePath = cmdFilePath;
	}
	Map checkCommand() {
		String content = Library.fileReadAll(cmdFilePath);
		String[] command = content.split(Library.separator);
		String[] line;
		Map map = new HashMap();
		for(int i = 0; i < command.length; i++) {
			System.err.printf("command[%d]: "+command[i]+"\n", i);
			line = command[i].split(" ");
			if(line[0].equals("")){ continue; }
			if(line[0].equals("stop")) {
				map.put(STOP, null);
				return map;
			}else if(line[0].equals("exit")) {
				map.put(EXIT, null);
				return map;
			}else if(line[0].equals("bm")) {
				map.put(BUY_MARKET, new Detail(0, Double.parseDouble(line[1])));
			}else if(line[0].equals("sm")) {
				map.put(SELL_MARKET, new Detail(0, Double.parseDouble(line[1])));
			}else if(line[0].equals("buy")) {
				map.put(BUY, new Detail(Double.parseDouble(line[1]), Double.parseDouble(line[2])));
			}else if(line[0].equals("sell")) {
				map.put(SELL, new Detail(Double.parseDouble(line[1]), Double.parseDouble(line[2])));
			}else if(line[0].equals("forcesave")) {
				map.put(FORCE_SAVE, null);
			}
		}
		return map;
	}
	public static int EXIT = -1;
	public static int STOP = -2;
	
	public static int BUY = 1;
	public static int SELL = 2;
	public static int BUY_MARKET = 3;
	public static int SELL_MARKET = 4;

	
	public static int FORCE_SAVE = 100;
	
	class Detail {
		double price = 0;
		double size = 0;
		Detail(double price, double size) {
			this.price = price;
			this.size = size;
		}
	}
}
