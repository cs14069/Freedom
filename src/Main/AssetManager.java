package Main;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;

public class AssetManager {
	private LogManager lm;
	private String APIKEY = "";
	private String APISECRET = "";
	private final String getBalancePath = "/v1/me/getbalance";
	private final String getCollateralPath = "/v1/me/getcollateral";
	private Asset asset;
	private static double firstCollateral = 0;
	
	AssetManager(LogManager lm, String APIKEY, String APISECRET) {
		this.lm = lm;
		this.APIKEY = APIKEY;
		this.APISECRET = APISECRET;
		asset = new Asset();
		updateActualAsset();
		updateFxAsset();
		if(firstCollateral == 0) {
			firstCollateral = (double) ((Map) asset.fx).get(Constant.COLLATERAL);
		}
	}
	double getProfit() {
		return asset.fx.get(Constant.COLLATERAL) - firstCollateral;
	}
	double getOpenPositionPnl() {
		return asset.fx.get(Constant.OPEN_POSITION_PNL);
	}
	void updateActualAsset() {
		String response;
		response = HTTPConnector.access(Constant.GET, getBalancePath, APIKEY, APISECRET);
		
		List<Map> list = (List) JSON.decode(response);
		for(Iterator<Map> it = list.iterator(); it.hasNext(); ) {
			Map map = it.next();
			asset.amount.put((String) map.get(CURRENCY_CODE), ((BigDecimal)map.get(AMOUNT)).doubleValue());
			asset.available.put((String) map.get(CURRENCY_CODE), ((BigDecimal)map.get(AVAILABLE)).doubleValue());
		}
		
		System.err.println("asset: "+asset.amount.toString());
		System.err.println("available: "+asset.available.toString());		
		lm.log("[Update Asset]: "+asset.amount.toString() + "(Available: "+ asset.available.toString() + ")");
	}
	
	void updateFxAsset() {
		String response;
		response = HTTPConnector.access(Constant.GET, getCollateralPath, APIKEY, APISECRET);
		
		Map list = (Map)JSON.decode(response);
		for(Iterator it = list.entrySet().iterator(); it.hasNext(); ) {
		    Map.Entry entry = (Map.Entry)it.next();
		    asset.fx.put((String) entry.getKey(), ((BigDecimal) entry.getValue()).doubleValue());
		}
		
		System.err.println(asset.fx.toString());
		lm.log("[Update Asset(FX)]: "+asset.fx.toString());
	}
	Asset getAsset() {
		return asset;
	}
	
	public static final String CURRENCY_CODE = "currency_code";
	public static final String AMOUNT = "amount";
	public static final String AVAILABLE = "available";

	class Asset {
		Map<String, Double> amount;
		Map<String, Double> available;
		Map<String, Double> fx;
		Asset() {
			amount = new HashMap<String, Double>();
			available = new HashMap<String, Double>();
			fx = new HashMap<String, Double>();
		}
	}

}
