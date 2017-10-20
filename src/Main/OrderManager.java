package Main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;

public class OrderManager {
	private final String sendParentOrderPath = "/v1/me/sendparentorder";
	private final String cancelParentOrderPath = "/v1/me/cancelparentorder";
	private final String getPositionsPath = "/v1/me/getpositions";
	private final String getParentOrdersPath = "/v1/me/getparentorders";
	private LogManager lm;
	private String APIKEY = "";
	private String APISECRET = "";
	private int lastParentOrderId = 0;
	private List parentOrderList;

	OrderManager(LogManager lm, String APIKEY, String APISECRET) {
		this.lm = lm;
		this.APIKEY = APIKEY;
		this.APISECRET = APISECRET;
		parentOrderList = new ArrayList<Map>();
	}

	double getRestPosition(String productCode) {
		Map map;
		double size = 0;
		String response = HTTPConnector.access(Constant.GET, getPositionsPath + "?product_code=" + productCode, APIKEY,
				APISECRET);
		List list = (List) JSON.decode(response);
		if (list.size() == 0) {
			return 0.0;
		}
		for (int i = 0; i < list.size(); i++) {
			map = (Map) list.get(i);
			if (map.get("side").equals(Constant.BUY)) {
				size += ((BigDecimal) map.get("size")).doubleValue();
			} else {
				size -= ((BigDecimal) map.get("size")).doubleValue();
			}
		}
		size = (double) Math.round(size * 100000000) / 100000000;
		return size;
	}

	List getParentOrder(String productCode) {
		String response = HTTPConnector.access(Constant.GET, getParentOrdersPath + "?product_code=" + productCode
				+ "&after=" + lastParentOrderId + "&count=" + Constant.PARENT_ORDER_COUNT, APIKEY, APISECRET);
		List list = (List) JSON.decode(response);
		parentOrderList = list;
		return list;
	}

	void cancelOrder(String productCode, String id) {
		String json = JSON.encode(new CancelOrder(productCode, id));
		String response = HTTPConnector.access(Constant.POST, cancelParentOrderPath, json, APIKEY, APISECRET);
		System.out.println("[Cancel Order]: " + response);
	}

	void order(String side, double size, String productCode) {
		String json, response;
		Parameter[] param = new Parameter[1];
		for (int i = 0; i < param.length; i++) {
			param[i] = new Parameter();
		}
		param[0].setMarketParam(productCode, side, getProperSize(size));
		Order order = new Order(Constant.SIMPLE, Constant.MINUTE_TO_EXPIRE, Constant.GTC, param);
		json = JSON.encode(order);
		System.out.println("[Order Json]: " + json);
		response = HTTPConnector.access(Constant.POST, sendParentOrderPath, json, APIKEY, APISECRET);
		System.out.println("[Order response]" + response);
		if (response != null) {
			lm.log("[Order Json(order())]: " + json);
		}
	}

	List<Map> order(Map result, String productCode, List<Map> orderList) {
		String response = "";
		String[][] side = new String[Constant.ORDER_TIMES][3];
		int[][] price = new int[Constant.ORDER_TIMES][3];
		Parameter[][] param = new Parameter[Constant.ORDER_TIMES][3];
		String json;
		int i, j;
		Iterator<Map> it, it2;
		Map tmp;
		if (result.get(Constant.CHOICE).equals(Constant.SELL)) {
			System.out.print("  SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS  ");
			for (i = 0; i < Constant.ORDER_TIMES; i++) {
				side[i][0] = Constant.SELL;
				side[i][1] = Constant.BUY;
				side[i][2] = Constant.BUY;
			}
			for (i = 0; i < Constant.ORDER_TIMES; i++) {
				price[i][0] = (Integer) result.get(Constant.ORDER_PRICE) + Constant.ORDER_WIDTH * i;
				price[i][1] = (Integer) result.get(Constant.PROFIT_PRICE) + Constant.ORDER_WIDTH * i;
				price[i][2] = (Integer) result.get(Constant.LOSS_PRICE) + Constant.ORDER_WIDTH * i;
			}
			i--;
			if (price[i][0] < price[i][1] || price[i][0] > price[i][2]) {
				return orderList;
			}
			revoke(orderList, Constant.BUYSELL, Constant.SELL);
		} else if (result.get(Constant.CHOICE).equals(Constant.BUY)) {
			System.out.print("  BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB   ");
			for (i = 0; i < Constant.ORDER_TIMES; i++) {
				side[i][0] = Constant.BUY;
				side[i][1] = Constant.SELL;
				side[i][2] = Constant.SELL;
			}
			for (i = 0; i < Constant.ORDER_TIMES; i++) {
				price[i][0] = (Integer) result.get(Constant.ORDER_PRICE) - Constant.ORDER_WIDTH * i;
				price[i][1] = (Integer) result.get(Constant.PROFIT_PRICE) - Constant.ORDER_WIDTH * i;
				price[i][2] = (Integer) result.get(Constant.LOSS_PRICE) - Constant.ORDER_WIDTH * i;
			}
			i--;
			if (price[i][0] > price[i][1] || price[i][0] < price[i][2]) {
				return orderList;
			}
			revoke(orderList, Constant.SELLBUY, Constant.BUY);
		} else {
			System.out.println("order neither");
			return orderList;
		}
		System.out.println("\n----  order!  ----");

		for (i = 0; i < Constant.ORDER_TIMES; i++) {
			for (j = 0; j < 3; j++) {
				param[i][j] = new Parameter();
			}
			param[i][0].setLimitParam(productCode, side[i][0], price[i][0], Constant.ORDER_SIZE);
			param[i][1].setLimitParam(productCode, side[i][1], price[i][1], Constant.ORDER_SIZE);
			param[i][2].setStopParam(productCode, side[i][2], price[i][2], Constant.ORDER_SIZE);
		}

		lm.log("[Order Json(order())]: " + TimeController.getDate() + "\t START");
		for (i = 0; i < Constant.ORDER_TIMES; i++) {
			Order order = new Order(Constant.IFD_OCO, Constant.MINUTE_TO_EXPIRE, Constant.GTC, param[i]);
			json = JSON.encode(order);
			System.out.println("[Order Json " + i + "]: " + json);
			response = HTTPConnector.access(Constant.POST, sendParentOrderPath, json, APIKEY, APISECRET);
			System.out.println("[Order response]" + response);
			if (response != null) {
				result.put(Constant.PARENT_ORDER_ACCEPTANCE_ID,
						(String) ((Map) JSON.decode(response)).get(Constant.PARENT_ORDER_ACCEPTANCE_ID));
				orderList.add(result);
				lm.log("[Order Json(order())" + i + "]: " + json);
			}
		}
		lm.log("[Order Json(order())]: " + TimeController.getDate() + "\t END");

		return orderList;
	}

	void revoke(List<Map> orderList, String oldSide, String newSide) {
		Iterator<Map> it, it2;
		Map tmp;
		for (it = parentOrderList.iterator(); it.hasNext();) {
			tmp = it.next();
			if (tmp.get(Constant.PARENT_ORDER_STATE).equals(Constant.ACTIVE)) {
				if (tmp.get(Constant.SIDE).equals(oldSide)
						&& tmp.get(Constant.SIZE) != tmp.get(Constant.OUTSTANDING_SIZE)) {
					System.out.println("revoke: " + oldSide + "->" + newSide);
					cancelOrder(Constant.FX_BTC_JPY, (String) tmp.get(Constant.PARENT_ORDER_ACCEPTANCE_ID));
					order(newSide,
							getProperSize(((BigDecimal) tmp.get(Constant.SIZE)).doubleValue()
									- ((BigDecimal) tmp.get(Constant.OUTSTANDING_SIZE)).doubleValue()),
							Constant.FX_BTC_JPY);
					for (it2 = orderList.iterator(); it2.hasNext();) {
						if (it2.next().get(Constant.PARENT_ORDER_ACCEPTANCE_ID)
								.equals(tmp.get(Constant.PARENT_ORDER_ACCEPTANCE_ID))) {
							it2.remove();
						}
					}
					break;
				}
			}
		}
	}

	void cancelAllOrder(String productCode) {
		String json, response;
		Iterator<Map> it, it2;
		Map tmp;
		List<Map> parentOrderList = getParentOrder(Constant.FX_BTC_JPY);
		for (it = parentOrderList.iterator(); it.hasNext();) {
			tmp = it.next();
			if (tmp.get(Constant.PARENT_ORDER_STATE).equals(Constant.ACTIVE)) {
				cancelOrder(Constant.FX_BTC_JPY, (String)tmp.get(Constant.PARENT_ORDER_ACCEPTANCE_ID));
			}
		}
	}

	void closeOut(String productCode, double size) {
		String response = "", json;
		String side;
		Parameter[] param = new Parameter[1];
		param[0] = new Parameter();
		Order order;
		size = getRestPosition(productCode);
		if (size == 0.0) {
			return;
		} else if (size < 0) {
			size *= -1.0;
			side = Constant.BUY;
		} else {
			side = Constant.SELL;
		}
		param[0].setMarketParam(productCode, side, size);
		order = new Order(Constant.SIMPLE, 1440, Constant.GTC, param); // 1440m
																		// -> a
																		// day
		json = JSON.encode(order);
		System.out.println("[closeout]: " + json);
		response = HTTPConnector.access(Constant.POST, sendParentOrderPath, json, APIKEY, APISECRET);
		System.out.println("[closeout]: " + response);
		if (response != null) {
			lm.log("[Order Json(closeOut())]: " + TimeController.getDate() + "\t" + json);
		}
	}

	double getProperSize(double size) {
		return (double) Math.round(size * 100000000) / 100000000;
	}

}
