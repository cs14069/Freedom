//package Main;
//
//import java.math.BigDecimal;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.TreeSet;
//
//import net.arnx.jsonic.JSON;
//
//public class FreedomSimulate extends Thread {
//	private String APIKEY = "4YCsZcwcQ4SW5Gxgkc5qzK";
//	private String APISECRET = "eRdpB56HLIy5mScxFPag0jjh2se4fbv+c57UgSo6EQs=";
//	private long lastExecutionId = 0;
//	private final boolean TRADE = false;
//
////	public static void main(String[] args) {
////		while (true) {
////			try {
////				Thread thread = new Freedom();
////				thread.start();
////				while (thread.isAlive()) {
////					Thread.sleep(30 * 1000);
////				}
////			} catch (Exception e) {
////				e.printStackTrace();
////			}
////			System.out.println("-------- restart [" + TimeController.getDate() + "] ---------");
////		}
////	}
////
//	public void run() {
//		LogManager lm;
//		CommandManager cm;
//		AssetManager asm;
//		AnalysisManager anm;
//		InformationManager im;
//		OrderManager om;
//		Simulator sim;
//		Map resultMap, tmp;
//		String productCode, newTime, oldTime = "";
//		List<Map> orderList = new ArrayList<Map>();
//		List<Map> executionList;
//		List<Map> resultMapList;
//		int minute = 0;
//		double profitLoss, restPosition;
//		Iterator it;
//		Result result = new Result();
//		try {
//			System.out.println("init");
//			lm = new LogManager("", Constant.LOG_EXT);
//			cm = new CommandManager(lm, Constant.COMMAND_FILE_PATH);
//			asm = new AssetManager(lm, APIKEY, APISECRET);
//			anm = new AnalysisManager(lm);
//			im = new InformationManager(lm, APIKEY, APISECRET, Constant.GET_BOARD_SLEEP);
//			om = new OrderManager(lm, APIKEY, APISECRET);
//			sim = new Simulator();
//
//			productCode = Constant.FX_BTC_JPY;
//			while (true) {
//				Thread.sleep(Constant.MAIN_SLEEP);
//				executionList = im.getExecution(productCode, lastExecutionId);
//				if (executionList != null && executionList.size() != 0) {
//					lastExecutionId = ((BigDecimal) ((Map) executionList.get(executionList.size() - 1)).get("id"))
//							.longValue();
//					if (TRADE) {
//						deleteCompletedOrder(productCode, om, orderList);
//					} else {
//						result = sim.checkMyOrder(productCode, orderList, im, executionList);
//					}
//					cancelOldOrder(im, om, orderList, productCode, executionList);
//				}
//				if (TRADE) {
//					restPosition = om.getRestPosition(productCode);
//					checkUnrealized(om, anm, asm, productCode, restPosition, orderList);
//				}
//				newTime = TimeController.getCurrentTime().substring(0, 5);
//				if (!oldTime.equals(newTime)) {
//					oldTime = newTime;
//					minute++;
//					resultMapList = analyze(anm, productCode, executionList);
//					for (it = resultMapList.iterator(); it.hasNext();) {
//						resultMap = (Map) it.next();
//						if (resultMap != null && (int) resultMap.get(Constant.CHOICE) != Constant.CHOICE_NEITHER) {
//							if (TRADE) {
//								if (restPosition + Constant.ORDER_SIZE * Constant.ORDER_TIMES > Constant.MAX_POSITION) {
//									lm.log("[Position Limit]@" + TimeController.getDate() + "\t" + resultMap);
//								} else {
//									orderList = om.order(resultMap, productCode, orderList);
//								}
//							} else {
//								if ((int) resultMap.get(Constant.CHOICE) != Constant.CHOICE_NEITHER) {
//									orderList.add(resultMap);
//								}
//							}
//						}
//					}
//					if (TRADE) {
//						asm.updateFxAsset();
//						profitLoss = asm.getProfit();
//						System.out.println("[Profit/Loss]: " + profitLoss);
//						lm.log("[Profit/Loss]@" + TimeController.getDate() + "\t" + profitLoss);
//						if (profitLoss <= -Constant.MAX_TOTAL_LOSS) {
//							om.cancelAllOrder(productCode, orderList);
//							om.closeOut(productCode, restPosition);
//							System.err.println("-------------- TOO MUCH LOSS!--------------");
//							System.err.println("-------------- " + TimeController.getDate() + "--------------");
//							lm.log("[Give Up]@" + TimeController.getDate() + "\tToo Much Loss!");
//							throw new ExitException();
//						}
//					} else {
//						if (minute % Constant.SIM_REPORT_MINUTE == 0) {
//							for (it = orderList.iterator(); it.hasNext();) {
//								tmp = (Map) it.next();
//								System.out.println("[" + tmp.get(Constant.CHOICE) + "] ["
//										+ tmp.get(Constant.ORDER_PRICE) + "] [" + tmp.get(Constant.PROFIT_PRICE) + "] ["
//										+ tmp.get(Constant.LOSS_PRICE) + "]");
//							}
//							sim.report();
//							minute = 0;
//						}
//					}
//				}
//			}
//		} catch (ExitException e) {
//			e.printStackTrace();
//			System.exit(1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	void checkUnrealized(OrderManager om, AnalysisManager anm, AssetManager asm, String productCode,
//			double restPosition, List<Map> orderList) {
//		if (restPosition == 0.0) {
//			return;
//		}
//		double pos = Math.abs(restPosition);
//		double size = Constant.ORDER_SIZE;
//		if (asm.getOpenPositionPnl() / pos < -anm.getAverageHeight() * Constant.ORDER_PRICE_LOSS_MUL) {
//			if (orderList.size() > 0) {
//				om.cancelOrder(productCode, (String) orderList.get(0).get(Constant.PARENT_ORDER_ACCEPTANCE_ID));
//			} else {
//				size = om.getRestPosition(productCode);
//			}
//			if (restPosition < 0) {
//				om.order(Constant.BUY, size, productCode);
//			} else {
//				om.order(Constant.SELL, size, productCode);
//			}
//			orderList.remove(0);
//		}
//	}
//
//	void deleteCompletedOrder(String productCode, OrderManager om, List<Map> orderList) {
//		List parentOrderList = om.getParentOrder(productCode);
//		List<String> removeList = new ArrayList<String>();
//		Map map;
//		Iterator it;
//		for (it = parentOrderList.iterator(); it.hasNext();) {
//			map = (Map) it.next();
//			if (!map.get(Constant.PARENT_ORDER_STATE).equals("ACTIVE")) {
//				removeList.add((String) map.get(Constant.PARENT_ORDER_ACCEPTANCE_ID));
//			}
//		}
//		for (it = orderList.iterator(); it.hasNext();) {
//			if (removeList.contains(((Map) it.next()).get(Constant.PARENT_ORDER_ACCEPTANCE_ID))) {
//				it.remove();
//			}
//		}
//	}
//
//	void cancelOldOrder(InformationManager im, OrderManager om, List<Map> orderList, String productCode,
//			List<Map> executionList) {
//		Iterator it;
//		Map executionMap, orderMap;
//		Set<Integer> removeList = new TreeSet<Integer>();
//		int i, j, tmp;
//		for (i = 0; i < orderList.size(); i++) {
//			orderMap = orderList.get(i);
//			tmp = (int) orderMap.get(Constant.EXPIRE);
//			if (tmp <= 0) {
//				removeList.add(i);
//				continue;
//			}
//			orderMap.put(Constant.EXPIRE, tmp - 1);
//			if ((int) orderMap.get(Constant.CHOICE) == Constant.CHOICE_SELL) {
//				for (it = executionList.iterator(); it.hasNext();) {
//					executionMap = (Map) it.next();
//					if ((double) orderMap.get(Constant.LOSS_PRICE) > ((BigDecimal) executionMap.get(Constant.PRICE))
//							.doubleValue()
//							|| (double) orderMap
//									.get(Constant.PROFIT_PRICE) < ((BigDecimal) executionMap.get(Constant.PRICE))
//											.doubleValue()) {
//						removeList.add(i);
//					}
//				}
//			} else if ((int) orderMap.get(Constant.CHOICE) == Constant.CHOICE_BUY) {
//				for (it = executionList.iterator(); it.hasNext();) {
//					executionMap = (Map) it.next();
//					if ((double) orderMap.get(Constant.LOSS_PRICE) < ((BigDecimal) executionMap.get(Constant.PRICE))
//							.doubleValue()
//							|| (double) orderMap
//									.get(Constant.PROFIT_PRICE) > ((BigDecimal) executionMap.get(Constant.PRICE))
//											.doubleValue()) {
//						removeList.add(i);
//					}
//				}
//			}
//		}
//		i = 0;
//		for (it = removeList.iterator(); it.hasNext();) {
//			j = (i--) + (int) it.next();
//			if (TRADE) {
//				om.cancelOrder(productCode, (String) orderList.get(j).get(Constant.PARENT_ORDER_ACCEPTANCE_ID));
//			}
//			orderList.remove(j);
//		}
//
//	}
//
//	List analyze(AnalysisManager anm, String table, List<Map> executionList) {
//		Connection con = null;
//		Statement st = null;
//		ResultSet rs = null;
//		List list = null;
//		try {
//			Class.forName("com.mysql.jdbc.Driver").newInstance();
//			con = DriverManager.getConnection("jdbc:mysql://" + Constant.DB_SERVER + "/" + Constant.DB_INFO_NAME,
//					Constant.DB_USER, Constant.DB_PASS);
//			st = con.createStatement();
//			String query = "SELECT * FROM " + table + " order by id desc limit " + Constant.RANGE;
//			rs = st.executeQuery(query);
//			if (rs.first()) {
//				list = anm.analyze(rs, executionList);
//			}
//			st.close();
//			con.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return list;
//	}
//
//	int getAveragePrice(List<Map> list) {
//		return (((BigDecimal) (list.get(0).get(Constant.PRICE))).intValue()
//				+ ((BigDecimal) (list.get(1).get(Constant.PRICE))).intValue()
//				+ ((BigDecimal) (list.get(2).get(Constant.PRICE))).intValue()
//				+ ((BigDecimal) (list.get(3).get(Constant.PRICE))).intValue()
//				+ ((BigDecimal) (list.get(4).get(Constant.PRICE))).intValue()
//				+ ((BigDecimal) (list.get(5).get(Constant.PRICE))).intValue()
//				+ ((BigDecimal) (list.get(6).get(Constant.PRICE))).intValue()
//				+ ((BigDecimal) (list.get(7).get(Constant.PRICE))).intValue()
//				+ ((BigDecimal) (list.get(8).get(Constant.PRICE))).intValue()
//				+ ((BigDecimal) (list.get(9).get(Constant.PRICE))).intValue()
//				+ ((BigDecimal) (list.get(10).get(Constant.PRICE))).intValue()
//				+ ((BigDecimal) (list.get(11).get(Constant.PRICE))).intValue()
//				+ ((BigDecimal) (list.get(12).get(Constant.PRICE))).intValue()) / 13;
//	}
//}
