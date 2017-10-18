package Main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;

public class InformationManager {
	private LogManager lm;
	private String APIKEY = "";
	private String APISECRET = "";
	private String getBoardPath = "/v1/getboard";
	private String getExecutionPath = "/v1/getexecutions";
	private static int GET_BOARD_SLEEP;
	private long lastExecutionId = -1;
	private List lastExecution = null;

	InformationManager(LogManager lm, String APIKEY, String APISECRET, int GET_BOARD_SLEEP) {
		this.lm = lm;
		this.APIKEY = APIKEY;
		this.APISECRET = APISECRET;
		this.GET_BOARD_SLEEP = GET_BOARD_SLEEP;
	}

	List getExecution(String productCode, long lastExecutionId) {
		String response;
		try {
			response = HTTPConnector.access(Constant.GET, getExecutionPath + "?product_code=" + productCode + "&count="
					+ Constant.MAX_EXECUTION_LIST + "&after=" + lastExecutionId);
			lastExecution = (List) JSON.decode(response);
		} catch (Exception e) {
			return new ArrayList();
		}
		return lastExecution;
	}

	Info getBoard(String productCode, int getBoardTrialTimes) {
		String response;
		Info info = new Info();
		Map board = null;
		for (int i = 0; i < getBoardTrialTimes; i++) {
			response = HTTPConnector.access(Constant.GET, getBoardPath + "?product_code=" + productCode);
			if (response == null) {
				continue;
			}
			board = (Map) JSON.decode(response);
			if (board != null) {
				break;
			}
			try {
				Thread.sleep(GET_BOARD_SLEEP);
			} catch (Exception e) {
			}
		}

		if (board == null) {
			return null;
		}
		info.bid = (List<Map>) board.get("bids");
		info.ask = (List<Map>) board.get("asks");
		info.mid = ((BigDecimal) board.get("mid_price")).doubleValue();

		return info;
	}

}
