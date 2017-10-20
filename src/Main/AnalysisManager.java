package Main;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AnalysisManager {
	private LogManager lm;
	private double averageHeight;
	
	private List<Double> executedTotal;

	AnalysisManager(LogManager lm) {
		this.lm = lm;
		executedTotal = new ArrayList<Double>();
	}

	double getAverageHeight() {
		return averageHeight;
	}

	List<Map> analyze(ResultSet rs, List<Map> executionList) throws SQLException {
		List list = new ArrayList();
		Map map1 = new MyMap();
		Map map2 = new MyMap();
		list.add(map1);
		list.add(map2);		
		if (executionList.size() == 0) {
			return list;
		}

		Iterator<Map> itm;
		Iterator<Double> itd;
		Map tmp;
		double last = ((BigDecimal) (executionList.get(0)).get(Constant.PRICE)).doubleValue();
		// double last1 = rs.getDouble("last"), last2;
//		double samePrice = 0, samePriceVolume = 0;
		double totalSize = 0, sumSize = 0, averageSize = 0, tmpSize;
		int shaveCnt = 0;
		String side = "";
		rs.next();
		// last2 = rs.getDouble("last");

		averageHeight = 0;
		rs.first();
		while (rs.next()) {
			averageHeight += Math.abs(rs.getDouble("last") - rs.getDouble("first"));
		}
		averageHeight /= Constant.RANGE;

		// for(it = executionList.iterator(); it.hasNext(); ) {
		// tmp = it.next();
		// if(((BigDecimal)map.get(Constant.PRICE)).doubleValue() != samePrice)
		// {
		// samePrice = ((BigDecimal)map.get(Constant.PRICE)).doubleValue();
		// samePriceVolume = 0;
		// shaveCnt = 0;
		// } else {
		// shaveCnt++;
		// samePriceVolume +=
		// ((BigDecimal)map.get(Constant.SIZE)).doubleValue();
		// if(samePriceVolume > Constant.SHAEVD_WALL_SIZE && shaveCnt >
		// Constant.SHAEVD_WALL_NUM) {
		// if(((String) map.get(Constant.SIDE)).equals(Constant.SELL)) {
		// side = Constant.BUY;
		// } else {
		// side = Constant.SELL;
		// }
		// break;
		// }
		// }
		// }
		// last = samePrice;
		
		
		System.out.println("analyze");
		if(executedTotal.size() >= 9) {
			executedTotal.remove(0);
		}
		for(itm = executionList.iterator(); itm.hasNext(); ) {
			tmp = itm.next();
			if(tmp.get(Constant.SIDE).equals(Constant.SELL)) {
				totalSize += ((BigDecimal)tmp.get(Constant.SIZE)).doubleValue();				
			} else {
				totalSize -= ((BigDecimal)tmp.get(Constant.SIZE)).doubleValue();					
			}
			
		}
		System.out.println("totalSize: "+totalSize);
		executedTotal.add(totalSize);

		for(itd = executedTotal.iterator(); itd.hasNext(); ) {
			tmpSize = itd.next();
			sumSize += tmpSize;
			averageSize += Math.abs(tmpSize);
		}
		averageSize /= executedTotal.size();
		System.out.println("sumSize: " + sumSize);
		System.out.println("averageSize: " + averageSize);
		if (sumSize > averageSize*Constant.TREND_VOLUME) {
			map1.put(Constant.CHOICE, Constant.BUY);
			map1.put(Constant.ORDER_PRICE, (int)(last + averageHeight));
			map1.put(Constant.PROFIT_PRICE, (int)(last + averageHeight *  Constant.ORDER_PRICE_PROFIT_MUL));
			map1.put(Constant.LOSS_PRICE, (int)(last - averageHeight *  Constant.ORDER_PRICE_LOSS_MUL));			
		}else if(sumSize < -averageSize*Constant.TREND_VOLUME) {
			map2.put(Constant.CHOICE, Constant.SELL);
			map2.put(Constant.ORDER_PRICE, (int)(last - averageHeight));
			map2.put(Constant.PROFIT_PRICE, (int)(last - averageHeight * Constant.ORDER_PRICE_PROFIT_MUL));
			map2.put(Constant.LOSS_PRICE, (int)(last + averageHeight *  Constant.ORDER_PRICE_LOSS_MUL));			
		} 
		
//		map1.put(Constant.CHOICE, Constant.CHOICE_BUY);
//		map1.put(Constant.ORDER_PRICE, last - averageHeight * Constant.WHISKER);
//		map1.put(Constant.PROFIT_PRICE, last - averageHeight * (1 - Constant.RECOIL) * Constant.WHISKER);
//		map1.put(Constant.LOSS_PRICE, last - averageHeight * (1 + Constant.RECOIL) * Constant.WHISKER);
//		// lm.log("[Buy](" + TimeController.getDate() + "): " + map.toString());
//
//		map2.put(Constant.CHOICE, Constant.CHOICE_SELL);
//		map2.put(Constant.ORDER_PRICE, last + averageHeight * Constant.WHISKER);
//		map2.put(Constant.PROFIT_PRICE, last + averageHeight * (1 - Constant.RECOIL) * Constant.WHISKER);
//		map2.put(Constant.LOSS_PRICE, last + averageHeight * (1 + Constant.RECOIL) * Constant.WHISKER);
		// lm.log("[Sell)](" + TimeController.getDate() + "): " +
		// map.toString());
//		System.out.println(map2.get(Constant.CHOICE));
//		map1.put(Constant.SIM_STATE, Constant.SIM_STATE_0);
//		map2.put(Constant.SIM_STATE, Constant.SIM_STATE_0);
//
//		map1.put(Constant.EXPIRE, 3);	// 3 => 1minute
//		map2.put(Constant.EXPIRE, 3);
		return list;
	}

	class MyMap extends HashMap {
		MyMap() {
			this.put(Constant.CHOICE, Constant.CHOICE_NEITHER);
		}
	}
}
