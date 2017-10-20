package Main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {
	static final String separator = "\\r\\n|[\\n\\r\\u2028\\u2029\\u0085]";

	public class IllegalException extends Exception {

	}
	public static String fileReadAll(String path) {
		StringBuilder builder = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String string = reader.readLine();
			while (string != null) {
				builder.append(string + System.getProperty("line.separator"));
				string = reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return builder.toString();
	}

	public static boolean fileWrite(String path, String content, boolean append) {
		try {
			File file = new File(path);

			if (file.isFile() && file.canWrite()) {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, append)));
				pw.print(content);
				pw.close();
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean createFile(String path) {
		try {
			File file = new File(path);
			file.createNewFile();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}

class Order {
	public String order_method;
	public int minute_to_expire;
	public String time_in_force;
	public Parameter[] parameters;

	Order(String om, int mte, String tif, Parameter[] p) {
		order_method = om;
		minute_to_expire = mte;
		time_in_force = tif;
		parameters = p;
	}
}

class Parameter {
	public String product_code;
	public String condition_type;
	public String side;
	public int price;
	public double size;
	public int offset;
	public int trigger_price;

	void setLimitParam(String pc, String sd, int p, double sz) {
		condition_type = "LIMIT";
		product_code = pc;
		side = sd;
		price = p;
		size = sz;
	}

	void setStopParam(String pc, String sd, int tp, double sz) {
		condition_type = "STOP";
		product_code = pc;
		side = sd;
		trigger_price = tp;
		size = sz;
	}

	void setMarketParam(String pc, String sd, double sz) {
		condition_type = "TRAIL";
		product_code = pc;
		side = sd;
		size = sz;
		offset = 1;
	}
}
class CancelOrder {
	public String product_code;
	public String parent_order_acceptance_id;

	CancelOrder(String pc, String poai) {
		product_code = pc;
		parent_order_acceptance_id = poai;
	}
}

class Info {
	double mid;
	List<Map> bid;
	List<Map> ask;
	List<Map> execution;
	Map<String, Map> lastMinuteExecution;

	Info() {
		mid = 0;
		bid = new ArrayList();
		ask = new ArrayList();
		Map tmp = new HashMap();
		execution = new ArrayList();
		lastMinuteExecution = new HashMap();
		for (int i = 0; i < Constant.MAX_EXECUTION_LIST; i++) {
			execution.add(tmp);
		}
	}

	void addExecution(List<Map> list) {
		execution.subList(0, list.size() - 1).clear();
		execution.addAll(list);
	}
}

class Choice {
	String direction = Constant.CHOICE_NEITHER;
	double price = 0;
	double profitPrice = 0;
	double giveupPrice = 0;
	double index = 0;
	Choice(String direction, double price, double profitPrice, double giveupPrice) {
		this.direction = direction;
		this.price = price;
		this.profitPrice = profitPrice;
		this.giveupPrice = giveupPrice;
	}
	Choice(String direction, double price, double profitPrice, double giveupPrice, double index) {
		this.direction = direction;
		this.price = price;
		this.profitPrice = profitPrice;
		this.giveupPrice = giveupPrice;
		this.index = index;
	}
}

class ExitException extends Exception {
	
}
class IllException extends Exception {
	
}
class AnalysisException extends Exception {
	
}