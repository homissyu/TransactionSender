package io.koreada.util;

import java.io.File;

public class CommonConst{
	public static final String getOSName() {
		String ret = null;
		if(System.getProperty("os.name").startsWith("Mac")) {
			ret = "Mac";
		}else if(System.getProperty("os.name").startsWith("Linux")) {
			ret = "Linux";
		}else if(System.getProperty("os.name").startsWith("Windows")) {
			ret = "Windows";
		}
		return ret;
	}
	                                     
	public static final String CRWALER = "Crawler";
    public static final String CURRENT_DIR = ".";
    public static final String COMMA = ",";
    public static final String WHITE_SPACE = " ";
    public static final String ENTRY_STR="entry";
    public static final String LIB_DIR = "lib";
    public static final String DAT_DIR = "dat";
    public static final String DEBUG_SYSTEM = "DEBUG_SYSTEM";
    public static final String ENCODING = "UTF-8";
    public static final String DEBUG_KIND_SIZE = "SIZE";
    public static final String DEBUG_KIND_DATE = "DATE";
    public static final long Max_DEBUG_FILE_LENGTH = 1024*1024*1024;
    
    
    public static final String T_DES = "TrippleDES";
    public static final String AES = "AES";
    public static final String AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";
    public static final String DES = "DES";
    public static final String DESede = "DESede";
    public static final String RSA = "RSA";
    public static final boolean ENCRYPT = true;
    public static final boolean DECRYPT = false;
    public static final boolean DO_RAID = true;
    public static final boolean NOT_RAID = false;
    
    public static final String SECRET_KEY_FILE = "jayk1.dat";
    public static final String PBE_KEY_FILE = "jayk2.dat";
    public static final String PKI_KEY_FILE = "jayk3.dat";

    public static final String USER_DIR_PROP_KEY = "user.dir";
	
    public static final String TIME_ZONE = "TimeZone";
    public static final String TIME_ZONE_DEFAULT = "GMT";
    public static final String TIME_ZONE_KR = "GMT+9";
    
    public static final String DATE_FORMAT = "yyyyMMdd";
    public static final String TIME_FORMAT = "HHmm";
    public static final String SEC_FORMAT = "HHmmss";
    public static final String DATETIME_FORMAT = "yyyyMMddHHmmss";
    
    public static final String JAVA_SPEC_VERSION = "java.specification.version";

    public static final char[] sAlphabetDecimalChar = {'A','a','B','b','C','c','D','d','E','e','F','f',
									            'G','g','H','h','I','i','J','j','K','k','L','l',
									            'M','m','N','n','O','o','P','p','Q','q','R','r',
									            'S','s','T','t','U','u','V','v','W','w','X','x',
									            'Y','y','Z','z','1','2','3','4','5','6','7','8','9','0'};
    
    public static final String SPLIT_STRING = "split";
	public static final int ASCII = 0;
	public static final int BINARY = 1;
	public static final String FILE_TYPE = "FILE_TYPE";
	public static final String FILE_PATH = "FILE_PATH";
	public static final String ID = "ID";
	public static final String ORIGIN_STRING = "ORIGIN";
	public static final String ORIGIN_FILE_PATH = ORIGIN_STRING +"_"+ FILE_PATH;
	public static final String SPLIT_FILE_NAMES = "SPLIT_FILE_NAMES";
	public static final String MERGE_STR = "merge_";
	public static final String ORIGIN_FILE_NAME = "ORIGIN_FILE_NAME";
	public static final Object SPLIT_FILE_NAME = "SPLIT_FILE_NAME";
	public static final int SYMMETRIC_KEY = 0;
	public static final String SYMMETRIC_KEY_STR = "SYMMETRIC_KEY";
	public static final int PUBLIC_KEY = 1;
	public static final String DECRYPTED = "DECRYPTED";
	public static final String ENCRYPTED = "ENCRYPTED";
	public static final String SPLIT_RATIO = "SPLIT_RATIO";
	public static final String META_FILE_NAME = "Meta.craid";
	
	public static final String[] CSP_ARRAY = {"AmazonDrive","box","DropBox","FlipDrive","GoogleDrive","HiDrive","Hubic","JumpShare","MEGA","pCloud","Sync.com","Syncplicity","Yandex","MediaFire"};
	public static final int [] CSP_FREE_AMOUNT = {5,10,2,10,15,5,25,2,50,20,5,10,10,10};
	public static final int JSON_TYPE = 0;
	public static final int STREAM_TYPE = 1;
	public static final int SECRET_KEY_TYPE = 0;
	public static final int PKI_KEY_TYPE = 1;
	public static final int PBE_KEY_TYPE = 2;
	public static final int ENC_BYTE_32 = 256;
	public static final int ENC_BYTE_64 = 512;
	public static final String PROVIDER_BC = "BC";
	public static final String RSA_NOPADDING_STR = "RSA/None/NoPadding";
	public static final String PBKD_WITH_SHA256 = "PBKDF2WithHmacSHA256";
	public static final int ITERATION_CNT = 65536;
	public static final int ENC_BYTE_16 = 128;
	public static final String PARITY_FILE_NAME = "PARITY_FILE_NAME";
	public static final String REMAINING_BYTES_LENGTH = "REMAINING_BYTES_LENGTH";
	public static final String IS_RAID = "IS_RAID";
	public static final String LOG_TYPE_SIZE = "SIZE";
	public static final String LOG_FILE_NAME = "CrawlerIBK.log";
	public static final int OPERATION_MODE = 0;
	public static final int TEST_MODE = 1;
	public static final int QA_MODE = 2;
	public static final int DEBUG_MODE = 3;
	public static final int DEVELOPING_MODE = 4;
	public static final String EXCEPTION_FILE_NAME = "Exception.log";
	public static final String LOGS_DIR = "logs";
	public static final String SOURCE_DIR = "source";
	public static final String PARITY_DIR = "parity";
	
	private static final File ROOT =  new File(File.listRoots()[0], CommonConst.CRWALER);
	private static final String ROOT_PATH = ROOT.getAbsolutePath();
	public static final String SOURCE_PATH = ROOT_PATH + File.separator+CommonConst.SOURCE_DIR;
	public static final String SPLIT_PATH = ROOT_PATH + File.separator + CommonConst.SPLIT_STRING;
	public static final String META_PATH = SPLIT_PATH;
	public static final String LOG_PATH = ROOT_PATH + File.separator + CommonConst.LOGS_DIR;
	public static final String PARITY_PATH = SPLIT_PATH + File.separator + CommonConst.PARITY_DIR;
	
	//0:Chrome, 1:Firefox, 2:PhantomJS, 3:Safari, 4:Edge, 5:IE
    public static final String [] WEBDRIVER_STR_ARR = {CommonConst.CHROME_DRIVER,CommonConst.FIREFOX_DRIVER,CommonConst.PHANTOM_DRIVER,CommonConst.SAFARI_DRIVER,CommonConst.EDGE_DRIVER,CommonConst.IE_DRIVER};
    public static final String [] WEBDRIVER_ID_ARR = {CommonConst.CHROME_DRIVER_ID,CommonConst.FIREFOX_DRIVER_ID,CommonConst.PHANTOM_DRIVER_ID,CommonConst.SAFARI_DRIVER_ID,CommonConst.EDGE_DRIVER_ID,CommonConst.IE_DRIVER_ID};
    
	public static final String CHROME_DRIVER = "chromedriver";
	public static final String EDGE_DRIVER = "MicrosoftWebDriver";
	public static final String IE_DRIVER = "IEDRiverServer32";
	public static final String PHANTOM_DRIVER = "phantomjs";
	public static final String SAFARI_DRIVER = "safaridriver";
	public static final String FIREFOX_DRIVER = "geckodriver";
	
	public static final String CHROME_DRIVER_ID = "webdriver.chrome.driver";
	public static final String EDGE_DRIVER_ID = "webdriver.edge.driver";
	public static final String IE_DRIVER_ID = "webdriver.ie.driver";
	public static final String PHANTOM_DRIVER_ID = "phantomjs.binary.path";
	public static final String SAFARI_DRIVER_ID = "webdriver.safari.driver";
	public static final String FIREFOX_DRIVER_ID = "webdriver.gecko.driver";
	
	
	public static final String WEBDRIVER_PATH = CommonConst.WEB_DRIVER_DIR_STR + File.separator + CommonConst.getOSName();
//	public static final String EDGE_DRIVER_PATH = "E:\\sts_workspace\\selenium\\lib\\MicrosoftWebDriver.exe";
//	public static final String IE_DRIVER_PATH = "E:\\sts_workspace\\selenium\\lib\\IEDriverServer.exe"; 
	public static final Object SID_STRING = "SID";
	public static final String ACCOUNT_INFO_NAME = "accountInfo";
	public static final String JSON_EXTENSION = "json";
	public static final int RESULT_TOTAL = 0;
	public static final int RESULT_EVERY = 1;
	public static final String TOUCH_EN_CHROME_PATH = CommonConst.BROWSER_EXT_STR + File.separator + "TouchEn-PC_v1.0.1.15.crx";
	public static final String BROWSER_EXT_STR = "browserExt";
	public static final String WEB_DRIVER_DIR_STR = "webdriver";
	public static final String TOUCH_EN_FIREFOX_PATH = CommonConst.BROWSER_EXT_STR + File.separator + "touchenex@raon.co.kr.xpi";
	public static final String BACKUP_FILE_NAME = "backup.bak";
	public static final String PBKD_WITH_SHA1 = "PBKDF2WithHmacSHA1";
	public static final int OPERATION_TOTAL = 0;
	public static final int OPERATION_WITHDRAW = 2;
	public static final int OPERATION_DEPOSIT = 1;
	public static char[] getSalphabetdecimalchar() {
			return sAlphabetDecimalChar;
	}
	
	public static boolean App4Server = false;
}