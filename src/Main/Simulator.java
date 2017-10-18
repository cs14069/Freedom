package Main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

class Simulator {
	private long lastExecutionId = 0;
	private static double loss = 0, profit = 0;
	private static int wrongBuy = 0, wrongSell = 0, correctBuy = 0, correctSell = 0;

	void report() {
		int sellTime = wrongSell + correctSell;
		int buyTime = wrongBuy + correctBuy;
		System.out.println("-------------------Simulator report ----------------------");
		System.out.println("profit: " + profit + "\t loss: " + loss);
		System.out.println("total count: " + (sellTime + buyTime) + "\t correct: " + (correctSell + correctBuy)
				+ "\t wrong: " + (wrongSell + wrongBuy));
		System.out.println("correct: sell[ " + correctSell + " ]\t buy[ " + correctBuy + " ]");
		System.out.println("wrong:   sell[ " + wrongSell + " ]\t buy[ " + wrongBuy + " ]");
		try {
			System.out.printf("correct rate: sell[ %5.3f%% ]\t buy[ %5.3f%% ]\n",
					((double) correctSell * 100.0 / (double) sellTime),
					((double) correctBuy * 100.0 / (double) buyTime));
			System.out.printf("total rate: %6.4f%%\n",
					(double) (correctSell + correctBuy) / (double) (sellTime + buyTime) * 100);
		} catch (Exception e) {
		}
		System.out.println("-------------------------------------------------");
	}

	Result checkMyOrder(String productCode, List<Map> orderList, InformationManager im, List<Map> executionList) {
		Iterator it1, it2;
		lastExecutionId = ((BigDecimal) ((Map) executionList.get(executionList.size() - 1)).get("id")).longValue();
		Map executionMap, orderMap;
		Result result = new Result();
		if(orderList.size() == 0) {
			return result;
		}
		result.orderExists = true;
		for (it1 = executionList.iterator(); it1.hasNext();) {
			executionMap = (Map) it1.next();
			for (it2 = orderList.iterator(); it2.hasNext();) {
				orderMap = (Map) it2.next();
				if (orderMap.get(Constant.CHOICE).equals(Constant.SELL)) {
					if ((int) orderMap.get(Constant.SIM_STATE) == Constant.SIM_STATE_0) {
						if (((BigDecimal) executionMap.get(Constant.PRICE)).doubleValue() > (double) orderMap.get(Constant.ORDER_PRICE)) {
							orderMap.put(Constant.SIM_STATE, Constant.SIM_STATE_1);
						}
					} else if ((int) orderMap.get(Constant.SIM_STATE) == Constant.SIM_STATE_1) {
						if (((BigDecimal) executionMap.get(Constant.PRICE)).doubleValue() > (double) orderMap.get(Constant.LOSS_PRICE)) {
							loss += ((BigDecimal) executionMap.get(Constant.PRICE)).doubleValue()
									- (double) orderMap.get(Constant.ORDER_PRICE);
							wrongSell++;
							it2.remove();
							result.wrongSell++;
						} else if (((BigDecimal) executionMap.get(Constant.PRICE)).doubleValue() < (double) orderMap.get(Constant.PROFIT_PRICE)) {
							profit += (double) orderMap.get(Constant.ORDER_PRICE)
									- (double) orderMap.get(Constant.PROFIT_PRICE);
							correctSell++;
							it2.remove();
							result.correctSell++;
						}
					}

				} else if (orderMap.get(Constant.CHOICE).equals(Constant.BUY)) {
					if ((int) orderMap.get(Constant.SIM_STATE) == Constant.SIM_STATE_0) {
						if (((BigDecimal) executionMap.get(Constant.PRICE)).doubleValue() < (double) orderMap.get(Constant.ORDER_PRICE)) {
							orderMap.put(Constant.SIM_STATE, Constant.SIM_STATE_1);
						}
					} else if ((int) orderMap.get(Constant.SIM_STATE) == Constant.SIM_STATE_1) {
						if (((BigDecimal) executionMap.get(Constant.PRICE)).doubleValue() < (double) orderMap.get(Constant.LOSS_PRICE)) {
							loss += (double) orderMap.get(Constant.ORDER_PRICE)
									- ((BigDecimal) executionMap.get(Constant.PRICE)).doubleValue();
							wrongBuy++;
							it2.remove();
							result.wrongBuy++;
						} else if (((BigDecimal) executionMap.get(Constant.PRICE)).doubleValue() > (double) orderMap.get(Constant.PROFIT_PRICE)) {
							profit += (double) orderMap.get(Constant.PROFIT_PRICE)
									- (double) orderMap.get(Constant.ORDER_PRICE);
							correctBuy++;
							it2.remove();
							result.correctBuy++;
						}
					}
				}
			}
		}
		return result;
	}
}

class Result {
	boolean orderExists = false;
	int correctBuy = 0;
	int wrongBuy = 0;
	int correctSell = 0;
	int wrongSell = 0;
	int getSum() {
		return correctBuy+wrongBuy+correctSell+wrongSell;
	}
}
