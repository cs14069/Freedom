package Main;

public class Constant {
	public static final int FIRST_ASSET = 200 * 10000; // 2 million 2,000,000 yen
	public static final int MAX_TOTAL_LOSS = 1 * 5000;	// 5,000 yen

	public static final int RANGE = 1 * 60;
	public static final int FACTOR = 5;
	public static final int BUYSELL_DIFF = 3;
	public static final double Y = 40000;
	public static final int GET_BOARD_SLEEP = 2000;
	public static final int MAIN_SLEEP = 20 * 1000;
	public static final double SHAEVD_WALL_SIZE = 30;
	public static final int SHAEVD_WALL_NUM = 5;
	
	public static final int WHISKER = 10;
	public static final double RECOIL = 0.3;

	public static final int MAX_EXECUTION_LIST = 300;

	public static final String DB_SERVER = "localhost";
	public static final String DB_INFO_NAME = "infogetter";
	public static final String DB_SCORE_NAME = "coffee";
	public static final String DB_USER = "tyokjava";
	public static final String DB_PASS = "yuma8003";
	public static final String DB_ANALYSIS_SUFFIX = "_DATA";
	public static final String DB_RESULT_SUFFIX = "_RESULT";
	

	/* Order */
	public static final int ORDER_TIMES = 8;
	public static final double ORDER_SIZE = 0.01;
	public static final double MAX_POSITION = 0.25;
	public static final int MINUTE_TO_EXPIRE = 30;
	public static final int ORDER_WIDTH = 191;
	
	public static final int GET_BOARD_TRIAL_TIMES = 3;
	
	public static final double ORDER_PRICE_PROFIT_MUL = 3;
	public static final double ORDER_PRICE_LOSS_MUL = 3;
	public static final double ORDER_PRICE_GIVEUP_MUL = 2.4;
	public static final double TREND_VOLUME = 3.5;
	

	public static final int PARENT_ORDER_COUNT = 30;
	public static final String BTC_JPY = "BTC_JPY";
	public static final String FX_BTC_JPY = "FX_BTC_JPY";
	public static final String ETH_BTC = "ETH_BTC";
	public static final String IFD_OCO = "IFDOCO";
	public static final String OCO = "OCO";
	public static final String GTC = "GTC";
	public static final String BUY = "BUY";
	public static final String SELL = "SELL";
	public static final String BUYSELL = "BUYSELL";
	public static final String SELLBUY = "SELLBUY";
	public static final String LIMIT = "LIMIT";
	public static final String STOP = "STOP";
	public static final String TRAIL = "TRAIL";
	public static final String MARKET = "MARKET";
	public static final String SIMPLE = "SIMPLE";
	public static final String ACTIVE = "ACTIVE";
	public static final String SIDE = "side";
	public static final String SIZE = "size";
	public static final String OUTSTANDING_SIZE = "outstanding_size";

	public static final String CHOICE_NEITHER = "neither";

	public static final String EXPIRE = "expire";

	/* keywords */
	public static final int SUCCESS = 200;
	public static final int FAIL = 400;

	public static final int NONE = 0;
	public static final int CORRECT = 1;
	public static final int WRONG = 2;

	/* simulator */
	public static final String SIM_STATE = "sim_state";
	public static final int SIM_STATE_0 = 0;
	public static final int SIM_STATE_1 = 100;
	public static final int SIM_STATE_2 = 200;
	public static final int SIM_REPORT_MINUTE = 1;

	public static final String COLLATERAL = "collateral";
	public static final String OPEN_POSITION_PNL = "open_position_pnl";
	public static final String REQUIRE_COLLATERAL = "require_collateral";
	public static final String KEEP_RATE = "keep_rate";
	public static final String OPEN_DATE = "open_date";
	public static final String PARENT_ORDER_ACCEPTANCE_ID = "parent_order_acceptance_id";
	public static final String PARENT_ORDER_STATE = "parent_order_state";
	public static final String PRICE = "price";

	public static final String ORDER_PRICE = "order_price";
	public static final String LOSS_PRICE = "loss_price";
	public static final String PROFIT_PRICE = "profit_price";

	public static final String CHOICE = "choice";

	/* HTTPConnector.java */
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final int DONE = 200;
	public static final int HTTP_ERROR = 400;

	/* Log */
	public static final String LOG_FILE_DIR = "log/";
	public static final String LOG_EXT = ".log";

	/* Command */
	public static final String COMMAND_FILE_PATH = "data/command.txt";


	/* Param files */
	public static final String X_FILE_PATH1 = "data/score54.017_1,919,360_4,398,756_-72156.68735376722_-75280.0287413393_71163.59384362117_73043.44675805254_";
	public static final String X_FILE_PATH2 = "data/score54.023_1,964,092_4,677,874_-71709.63468528859_-75069.51309244274_73775.54435201928_76742.84671949368_";
	public static final String X_FILE_PATH3 = "data/score54.036_1,979,396_3,103,040_-61110.86005155175_-62850.969014410875_61748.998124559905_60341.525222555785_";
	public static final String X_FILE_PATH4 = "data/score54.074_1,948,476_3,036,723_-60455.60025472041_-62379.84907614388_62923.217857281335_62400.8727196419_";
	public static final String X_FILE_PATH5 = "data/score54.078_1,967,051_3,509,571_-63106.96909664369_-64188.040644189874_64738.77074132597_66373.94008152747_";
	public static final String X_FILE_PATH6 = "data/score54.123_1,973,691_3,692,540_-66001.43040551031_-68451.31990194245_65549.0982015462_66557.55330737552_";
	public static final String X_FILE_PATH7 = "data/score54.127_1,914,568_3,774,742_-66254.49502806208_-67854.39179561764_67082.77941597157_68384.17171092816_";
	public static final String X_FILE_PATH8 = "data/score54.154_1,935,488_4,269,759_-68741.65835803398_-70298.68504224013_70630.33876321459_71852.09258526706_";
	public static final String X_FILE_PATH9 = "data/score54.272_1,923,057_3,658,165_-64039.372928746016_-66649.99289239185_63889.39465549379_66049.98871917922_";
	public static final String X_FILE_PATH10 = "data/score54.273_1,889,795_4,480,448_-72529.32219228405_-75258.68018184327_72019.83858969327_73434.63366495636_";
	public static final String X_FILE_PATH11 = "data/score54.344_1,704,437_4,337,781_-71942.33675700288_-75053.3225597445_73423.54583202493_74157.0816405195_";
	public static final String X_FILE_PATH12 = "data/score54.392_1,903,234_2,558,613_-61364.70625882667_-66829.99702165184_80125.60766236811_76812.73293564687_";
	public static final String X_FILE_PATH13 = "data/score54.429_1,904,220_3,320,685_-60216.89176138864_-63924.142744145494_60214.795684335135_62170.31954748005_";
	public static final String X_FILE_PATH14 = "data/score54.431_1,796,413_3,463,355_-62137.813931454984_-65689.74703098736_61850.75252768323_63275.47307887973_";
	public static final String X_FILE_PATH15 = "data/score54.475_1,892,975_4,401,230_-68956.20070492793_-72157.53941993494_70665.06131805107_72239.23332282311_";
	public static final String X_FILE_PATH16 = "data/score54.494_1,890,125_3,851,556_-64718.863540340906_-66579.57869155289_65115.49699812841_65965.45079590654_";
	public static final String X_FILE_PATH17 = "data/score54.504_1,980,921_3,910,327_-63599.759434223946_-67191.81245850997_64583.00756927361_67671.02636878943_";
	public static final String X_FILE_PATH18 = "data/score54.531_1,871,517_3,664,624_-63596.13079648348_-64720.64216218058_63455.83372557035_65344.57134729963_";
	public static final String X_FILE_PATH19 = "data/score54.531_1,993,747_4,028,105_-64385.60148680004_-66750.89350792501_65645.21821212224_67755.55970101418_";
	public static final String X_FILE_PATH20 = "data/score54.568_1,970,187_3,188,406_-58285.31573390838_-62393.25664074359_57365.4674422571_59395.95748184577_";
	public static final String X_FILE_PATH21 = "data/score54.586_1,981,603_4,187,528_-67569.96031386325_-70602.7806088557_66851.53226279425_69262.19953166292_";
	public static final String X_FILE_PATH22 = "data/score54.587_1,934,535_2,712,069_-56858.409770346945_-57082.0462711055_58471.85345778585_57036.26099272178_";
	public static final String X_FILE_PATH23 = "data/score54.794_1,903,449_3,089,608_-60339.557885296876_-61926.699769083934_61448.888351822556_60497.207062308284_";
	public static final String X_FILE_PATH24 = "data/score54.819_1,955,890_3,895,681_-66329.54987670551_-69650.60519603711_65842.5816869886_67002.6839574569_";
	public static final String X_FILE_PATH25 = "data/score54.923_1,804,985_3,411,636_-59704.24720891749_-63504.708938034084_60569.24206149203_62606.4817689117_";
	public static final String X_FILE_PATH26 = "data/score54.976_1,875,943_3,905,330_-62275.14025554264_-64848.86072246505_62681.8324157118_65229.236615689246_";
	public static final String X_FILE_PATH27 = "data/score55.033_1,920,627_3,445,360_-63167.39315404447_-62836.4123228741_60560.25345802675_62385.54060691714_";
	public static final String X_FILE_PATH28 = "data/score55.085_1,869,807_3,350,967_-60003.97226850244_-61463.801446386984_60748.7867704275_63704.26387109681_";
	public static final String X_FILE_PATH29 = "data/score55.149_1,924,886_3,210,334_-60244.6716326519_-59923.64350183806_62093.834845164216_61441.57368509665_";
	public static final String X_FILE_PATH30 = "data/score56.109_1,920,872_2,347,940_-57777.09147780862_-60334.72622294146_56169.65423296193_58621.307433989816_";
	public static final String X_FILE_PATH31 = "data/score56.765_1,893,595_3,907,219_-62541.01257209504_-64563.691802138914_61442.83618789507_63951.01076754234_";

}
