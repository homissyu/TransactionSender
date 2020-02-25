package io.koreada.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

public class Install {
    private static Debug mDebug = null;
    private static Hashtable<String, String> mEnvironment = new Hashtable<String, String>();

    private static String mInstallDir = null;

    private static boolean printedConfigFileMissing = false;

    public static Install mInstall = null; // global per-process isntall object

    // these are key,value pairs which will override any existing key
    // with the new value for any Install object created AFTER they have
    // been "submitted" with the setOverride() function.
    static Hashtable<String, String> mPerProcessOverrides = new Hashtable<String, String>();

    static Hashtable<String, String> mAcceptedHash = null;

    //

    // CONSTANTS
    // System Configuration
    public static final String INSTALL_DIR = "INSTALL_DIR";
    public static final String INSTALL_DIR_DEFAULT = ".";
    public static final String CONFIG_FILE = "CONFIG_FILE";
    public static final String CONFIG_FILE_DEFAULT = "NWebChecker.conf";
    
    public static final String OPERATION_MODE = "OPERATION.MODE";
    public static final String OPERATION_MODE_DEFAULT = "0";
    
    // Output Configuration
    public static final String DEBUG_SUBSYSTEM = "LOG.SUBSYSTEM";
    public static final String DEBUG_SUBSYSTEM_DEFAULT = "ALL";
    public static final String DEBUG_VERBOSITY = "LOG.VERBOSITY";
    public static final String DEBUG_VERBOSITY_DEFAULT = "1";
    public static final String DEBUG_KIND = "LOG.KIND";
    public static final String DEBUG_KIND_DEFAULT = "SIZE";     // DAY, SIZE
    public static final String MAX_LOGFILE_SIZE = "LOG.MAX_SIZE";
    public static final String MAX_LOGFILE_SIZE_DEFAULT = "4096"; // 4MB (4096K)
    
    public static final String RESULT_TYPE = "RESULT.TYPE"; // 
    public static final String RESULT_TYPE_DEFAULT = "ALLWAYS"; // ALLWAYS OR CHANGE
    
    public static final String RESULT_LOG_TYPE = "RESULT.LOG_TYPE";
    public static final String RESULT_LOG_TYPE_DEFAULT = "TOTAL"; // TOTAL OR EVERY
    
    // Database variables
//    public static final String DB_TYPE = "DB.TYPE";
//    public static final String DB_TYPE_DEFAULT = "ORACLE";
//    public static final String DB_NAME = "DB.NAME";
//    public static final String DB_NAME_DEFAULT = "";
//    public static final String DB_HOSTNAME = "DB.HOSTNAME";
//    public static final String DB_HOSTNAME_DEFAULT = "";
//    public static final String DB_PORT = "DB.PORT";
//    public static final String DB_PORT_DEFAULT = "1521";
//    public static final String DB_USERNAME = "DB.USERNAME";
//    public static final String DB_USERNAME_DEFAULT = "";
//    public static final String DB_PASSWORD = "DB.PASSWORD";
//    public static final String DB_PASSWORD_DEFAULT = "";

    //  SMART BRIDGE
    public static final String SMART_BRIDGE_SEL_IP = "SB.SEL_IP";
    public static final String SMART_BRIDGE_SEL_IP_DEFAULT = "LOCALHOST";
    public static final String SMART_BRIDGE_POJO_IP = "SB.POJO_IP";
    public static final String SMART_BRIDGE_POJO_IP_DEFAULT = "LOCALHOST";
    public static final String SMART_BRIDGE_PARAM = "SB.PARAM";
    public static final String SMART_BRIDGE_PARAM_DEFAULT = "";
    public static final String SMART_BRIDGE_COOKIE = "SB.COOKIE";
    public static final String SMART_BRIDGE_COOKIE_DEFAULT = "";
    public static final String SMART_BRIDGE_ACC_NO = "SB.ACC_NO";
    public static final String SMART_BRIDGE_ACC_NO_DEFAULT = "";
    public static final String SMART_BRIDGE_BIZ_NO = "SB.BIZ_NO";
    public static final String SMART_BRIDGE_BIZ_NO_DEFAULT = "";
    public static final String SMART_BRIDGE_WEBDRIVER = "SB.WEBDRIVER";
    public static final String SMART_BRIDGE_WEBDRIVER_DEFAULT = "0";
    public static final String SMART_BRIDGE_API = "SB.API";
    public static final String SMART_BRIDGE_API_DEFAULT = "0";
    public static final String SMART_BRIDGE_BANK = "SB.BANK";
    public static final String SMART_BRIDGE_BANK_DEFAULT = "0";
    
    public static final String SMART_BRIDGE_BANK_PASS = "SB.PASS";
    public static final String SMART_BRIDGE_BANK_PASS_DEFAULT = "0000";
    
    public static final String SMART_BRIDGE_CRAWLER_TYPE = "SB.TYPE";
    public static final String SMART_BRIDGE_CRAWLER_TYPE_DEFAULT = "0";
    
    // Daemon
    public static final String DAEMON_INTERVAL = "DAEMON.INTERVAL";
    public static final String DAEMON_INTERVAL_DEFAULT = "60"; //seconds

    private static final String UNDECODED_PASSWORD_STRING = "UNDECODED_PASSWORD_STRING";


    // Function to check if DB_PASSWORD is not decodable
    public static final boolean isDBPasswordEncoded(String aPassword) {
        return aPassword.equals(UNDECODED_PASSWORD_STRING);
    }

   // List of required "environment" variables which we must have
    // (along with their default values).
    static final String Required[] = {
        //system
        INSTALL_DIR, INSTALL_DIR_DEFAULT,
        CONFIG_FILE, CONFIG_FILE_DEFAULT,
        
        OPERATION_MODE, OPERATION_MODE_DEFAULT,

        // Debug
        DEBUG_SUBSYSTEM, DEBUG_SUBSYSTEM_DEFAULT,
        DEBUG_VERBOSITY, DEBUG_VERBOSITY_DEFAULT,
        DEBUG_KIND, DEBUG_KIND_DEFAULT,
        MAX_LOGFILE_SIZE, MAX_LOGFILE_SIZE_DEFAULT,

        // SMART BRIDGE
        SMART_BRIDGE_SEL_IP, SMART_BRIDGE_SEL_IP_DEFAULT,
        SMART_BRIDGE_POJO_IP, SMART_BRIDGE_POJO_IP_DEFAULT,
        SMART_BRIDGE_PARAM, SMART_BRIDGE_PARAM_DEFAULT,
        SMART_BRIDGE_COOKIE, SMART_BRIDGE_COOKIE_DEFAULT,
        SMART_BRIDGE_ACC_NO, SMART_BRIDGE_ACC_NO_DEFAULT,
        SMART_BRIDGE_BIZ_NO, SMART_BRIDGE_BIZ_NO_DEFAULT,
        SMART_BRIDGE_WEBDRIVER, SMART_BRIDGE_WEBDRIVER_DEFAULT,
        SMART_BRIDGE_API, SMART_BRIDGE_API_DEFAULT,
        SMART_BRIDGE_BANK, SMART_BRIDGE_BANK_DEFAULT,
        SMART_BRIDGE_BANK_PASS, SMART_BRIDGE_BANK_PASS_DEFAULT,
        
        RESULT_TYPE, RESULT_TYPE_DEFAULT,
        RESULT_LOG_TYPE, RESULT_LOG_TYPE_DEFAULT

        // DB
//        DB_TYPE, DB_TYPE_DEFAULT,
//        DB_NAME, DB_NAME_DEFAULT,
//        DB_HOSTNAME, DB_HOSTNAME_DEFAULT,
//        DB_PORT, DB_PORT_DEFAULT,
//        DB_USERNAME, DB_USERNAME_DEFAULT,
//        DB_PASSWORD, DB_PASSWORD_DEFAULT
    };

    static final String Accepted[] = {
        INSTALL_DIR,
        CONFIG_FILE,

        // Output Configuration
        DEBUG_SUBSYSTEM,
        DEBUG_VERBOSITY,
        DEBUG_KIND,
        MAX_LOGFILE_SIZE

        // Database variables
//        DB_TYPE,
//        DB_NAME,
//        DB_HOSTNAME,
//        DB_PORT,
//        DB_USERNAME,
//        DB_PASSWORD
    };

    // BASIC ERROR MESSAGES

    public static final String CANT_CREATE_INSTALL = "Error: Improper installation.";
    public static final String CANT_CREATE_DEBUG = "Error: Can't start logging mechanism.";
    public static final String CANT_STARTUP = "Error: improper installation of logging and/or resource mechanism.";
    public static final String CANT_FIND_CONFIG_FILE = "Warning: Can't find config file: ";
    public static final String IO_ERROR_WHILE_READING_CONFIG_FILE = "Warning: IO Error while reading config file.";
    public static final String CANT_GET_LOCALHOST_NAME = "Error: Can't get name of localhost.";

    //
    // CONSTRUCTORS
    //

    // create an install object from using no command-line options
    //--------------------------------------------------
    public Install() throws Exception {
        ensureAcceptedHash();
        loadDefaults();
        finishInitialization();
    }

    //--------------------------------------------------
    public Install(String aArgs[], boolean aValidateArgs) throws Exception {
        ensureAcceptedHash();
        if (aValidateArgs) {
            confirmArguments(aArgs);
        }
        loadDefaults();
        
//        loadConfigFile(aArgs);
        loadConfigXMLFile(aArgs);
        loadEnvironment();
        parseCommandLine(aArgs);
        finishInitialization();
    }

    //--------------------------------------------------
    public Install(String aArgs[]) throws Exception {
        this(aArgs, true);
    }

    //--------------------------------------------------
    public void printEnvironment() {
        String key, value;
        System.out.println("\nInstall Environment .............");
        for (Enumeration<String> en = mEnvironment.keys(); en.hasMoreElements();) {
            key = (String)en.nextElement();
            value = (String)mEnvironment.get(key);
            System.out.println(key + "=" + value);
        }
        System.out.println(".............\n");
    }

    //--------------------------------------------------
    public void parseCommandLine(String aArgs[]) {
        int i;
        String arg, name, value;
        StringTokenizer st;

        // get command line arguments into the environment
        // NOTE: command line arguments override everything!
        for (i = 0; i < aArgs.length; i++) {
            arg = aArgs[i];
            st = new StringTokenizer(arg, "=", false);
            name = value = null;
            if (st.hasMoreTokens()) {
                name = st.nextToken();
            }
            if (st.hasMoreTokens()) {
                value = st.nextToken();
            } else {
                value = "";
            }
            if (name == null || value == null)
                ; // mDebug.println("unknown argument " + arg);
            else {
                mEnvironment.put(name, value);
            }
        }
    }

    //--------------------------------------------------
    public static Vector<String> removeUsedArguments(String aArgs[]) {
        int i;
        String arg;
        Vector<String> v = new Vector<String>();

        // get command line arguments into the environment
        // NOTE: command line arguments override everything!
        for (i = 0; i < aArgs.length; i++) {
            arg = aArgs[i];
            if (arg.indexOf("=") < 0) {
                v.addElement(arg);
            }
        }
        return v;
    }

    //--------------------------------------------------
    private void loadDefaults() {
        String name, def;

        // Load in all required arguments into the environment
        for (int i = 0; i < Required.length; i += 2) {
            name = Required[i];
            def = Required[i + 1];
            if (mEnvironment.get(name) == null) {
                mEnvironment.put(name, def);
            }
        }
    }

    //--------------------------------------------------
    private void loadEnvironment() {
        Properties props = System.getProperties();
        String name, value;

        for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements();) {
            name = (String)en.nextElement();
            value = props.getProperty(name);
            mEnvironment.put(name, value);
        }
    }

    //--------------------------------------------------
    @SuppressWarnings({ "deprecation", "unused" })
	private void loadConfigFile(String aArgs[]) throws Exception {
        int i, l;
        String arg, name, value;
        Properties props = System.getProperties();
        Vector<Object> buff = new Vector<Object>();

        // look for INSTALL_DIR and CONFIG_FILE in environment options
        for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements(); ) {
            name = (String)en.nextElement();
            value = props.getProperty(name);
            if ((name.equals(INSTALL_DIR) || name.equals(CONFIG_FILE)) && !(value == null)) {
                mEnvironment.put(name, value);
            }
        }

        // look for INSTALL_DIR and CONFIG_FILE in command line options
        StringTokenizer st;
        for (i = 0; i < aArgs.length; i++) {
            arg = aArgs[i];
            st = new StringTokenizer(arg, "=", false);
            name = value = null;
            if (st.hasMoreTokens()) {
                name = st.nextToken();
            }
            if (st.hasMoreTokens()) {
                value = st.nextToken();
            }
            if ((name.equals(INSTALL_DIR) || name.equals(CONFIG_FILE)) && !(value == null))  {
                mEnvironment.put(name, value);
            }
        }
        // find config file and parse it...
        String config_file = getConfigFile();
        FileReader fr = null;

        try {
            fr = new FileReader(config_file);
        } catch (FileNotFoundException e) {
            if (!printedConfigFileMissing) {
                System.out.println(CANT_FIND_CONFIG_FILE + config_file);
                printedConfigFileMissing = true;
            }
        }

        // actually read file...
        String line;
        int line_no = 0;
        int index;
        if (fr != null) {
            BufferedReader br = new BufferedReader(fr);
            try {
                while ((line = br.readLine()) != null) {
                    line_no++;

                    // strip out comments
                    index = line.indexOf((char)'#');
                    if (index == 0) {
                        continue;
                    }
                    if (index > 0) {
                        line = line.substring(0, index);
                    }

                    // skip blank lines
                    if (line.trim().equals("")) {
                        continue;
                    }

                    // get data!
                    st = new StringTokenizer(line, "=", false);
                    name = value = null;
                    if (st.hasMoreTokens()) {
                        name = st.nextToken().trim();
                    }
                    if (st.hasMoreTokens()) {
                        value = st.nextToken().trim();
                    }
                    if (name == null || name.equals("") || value == null || value.equals("")) {
                        buff.addElement("Error at line " + 
                                        new Integer(line_no).toString() +
                                        " in " + config_file +
                                        ": syntax error");
                    } else {
                        name = name.trim();
                        value = value.trim();

                        if (name.equals(INSTALL_DIR)) {
                            buff.addElement("Error at line " +
                                            new Integer(line_no).toString() +
                                            " in " + config_file +
                                            ": can't set INSTALL_DIR from within config file.");
                            continue;
                        }
                        if (name.equals(CONFIG_FILE)) {
                            buff.addElement("Error at line " +
                                              new Integer(line_no).toString() +
                                                " in " + config_file +
                                                ": can't set CONFIG_FILE from within config file.");
                            continue;
                        }

                        // System.out.println("setting:  " + name + " = " + value + ".");

                        mEnvironment.put(name, value);
                    }
                } // while
                
//                FileHandler.makePropertyXML(mEnvironment, CommonConst.LOGS_DIR+File.separator+this.CONFIG_FILE_DEFAULT);
            } catch (Exception e) {
                System.out.println(IO_ERROR_WHILE_READING_CONFIG_FILE);
                e.printStackTrace();
                try {
                    fr.close();
                } catch (IOException ee) { ; }
                return;
            }

            try {
                fr.close();
            } catch (IOException ee) { ; }

        } // if

        if (buff.size() != 0) {
            StringBuffer sb = new StringBuffer();
            for (i = 0, l = buff.size(); i < l; i++) {
                sb.append((String)buff.elementAt(i));
            }
            throw new Exception(sb.toString());
        }

    }
    
    private void loadConfigXMLFile(String aArgs[]) {
        int i;
        String arg, name, value;
        Properties props = System.getProperties();
        // look for INSTALL_DIR and CONFIG_FILE in environment options
        for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements(); ) {
            name = (String)en.nextElement();
            value = props.getProperty(name);
            if ((name.equals(INSTALL_DIR) || name.equals(CONFIG_FILE)) && !(value == null)) {
                mEnvironment.put(name, value);
            }
        }

        // look for INSTALL_DIR and CONFIG_FILE in command line options
        StringTokenizer st;
        for (i = 0; i < aArgs.length; i++) {
            arg = aArgs[i];
            st = new StringTokenizer(arg, "=", false);
            name = value = null;
            if (st.hasMoreTokens()) {
                name = st.nextToken();
            }
            if (st.hasMoreTokens()) {
                value = st.nextToken();
            }
            if ((name.equals(INSTALL_DIR) || name.equals(CONFIG_FILE)) && !(value == null))  {
                mEnvironment.put(name, value);
            }
        }
        // find config file and parse it...
        String config_file = getConfigFile();
        
        // actually read file...
        Properties conFigProp = FileHandler.readProperyXML(config_file);
        
        for (Enumeration<?> en = conFigProp.propertyNames(); en.hasMoreElements(); ) {
            name = (String)en.nextElement();
            value = conFigProp.getProperty(name);
            mEnvironment.put(name, value);
        }
    }

    //--------------------------------------------------
    private void finishInitialization() throws Exception {
        // put some commonly used environments into class variables for
        // ease of use...
        String id = (String)mEnvironment.get(INSTALL_DIR);
        if (mInstallDir == null || !mInstallDir.equals(id)) {
            mInstallDir = id;
        }

        // Create mDebug.object
        if (mDebug == null) {
            try {
                mDebug = new Debug(this);
            } catch (Exception e) {
                throw e;
            }
        }

        // Include all per-process overrides
        String key;
        for (Enumeration<String> en = mPerProcessOverrides.keys() ; en.hasMoreElements() ; ) {
            key = (String)en.nextElement();
            mEnvironment.put(key, mPerProcessOverrides.get(key));
        }
    }

    //
    // ENVIRONMENT STUFF
    //

    //--------------------------------------------------
    public String getProperty(String a) {
        String s = (String)mPerProcessOverrides.get(a);
        if (s == null) {
            s = (String)mEnvironment.get(a);
        }

//        if (a.equals(DB_PASSWORD)) {
//            try {
//                s = new String(Base64Encrypt.decode(s));
//            } catch (Exception e) {
//                s = UNDECODED_PASSWORD_STRING;
//            }
//        }

        return s;
    }

    //--------------------------------------------------
    public static String setOverride(String key, String value) {
        return (String)mPerProcessOverrides.put(key, value);
    }

    //---------------------------------------------------
    // Return a copy of the Install data (used by the GUI to
    // display configuration info for other components).
    public Hashtable<String, String> getData() {
        return(mEnvironment);
    }

    // Places the relationship into the environment,  If it was already
    // there, the old value is returned, otherwise null will be
    // returned.
    //--------------------------------------------------
    public String putProperty(String key, String value) {
        return (String)mEnvironment.put(key, value);
    }


    //--------------------------------------------------
    //
    // GET DIRECTORY STUFF
    //
    //--------------------------------------------------
    public String getLogDir() {
        return mInstallDir + File.separatorChar + "logs";
    }
    public String getConfigDir() {
        return mInstallDir + File.separatorChar + "conf";
    }

    public String getLibDir() {
        return mInstallDir + File.separatorChar + "lib";
    }

    public String getBinDir() {
        return mInstallDir + File.separatorChar + "bin";
    }

    public String getDataDir() {
        return mInstallDir + File.separatorChar + "data";
    }

    public String getRootDir() {
        return mInstallDir;
    }

    public String getClassesDir() {
        return mInstallDir + File.separatorChar + "classes";
    }

    // Note this is NOT the same as getProperty(Install.INSTALL_DIR). This method
    // returns the dir named "install" in the dir specified by INSTALL_DIR.
    public String getInstallDir() {
        return mInstallDir + File.separatorChar + "install";
    }

    //
    // CONFIG FILE STUFF
    //
    //--------------------------------------------------
    public String getConfigDirectory() {
        return getProperty(INSTALL_DIR) + File.separatorChar + "conf" +
                    File.separatorChar;
    }

    public String getImagesDir()   {
        return mInstallDir + File.separatorChar + "images";
    }

    // returns null if the config file has not been specificlaly set...
    //--------------------------------------------------
    public String getConfigFile() {
        String config = getProperty(CONFIG_FILE);
        if (config.indexOf((int)File.separatorChar) == -1) {
            // look for file in config directory
            return getConfigDirectory() + config;
        } else {
            return config;
        }
    }

    //--------------------------------------------------
    public static void setGlobalInstall() throws Exception {
        if (mInstall == null) {
            mInstall = new Install();
        }
    }

    //--------------------------------------------------
    public static Install getGlobalInstall() throws Exception {
        if (mInstall == null) {
            mInstall = new Install();
        }
        return mInstall;
    }

    //--------------------------------------------------
    void ensureAcceptedHash() {
        if (mAcceptedHash == null) {
            mAcceptedHash = new Hashtable<String, String>();
            for (int i = 0; i < Accepted.length; i++)
                mAcceptedHash.put(Accepted[i], Accepted[i]);
        }
    }

    //--------------------------------------------------
    void confirmArguments(String args[]) throws Exception {
        Vector<Object> buff = new Vector<Object>();
        String arg, name, value;
        StringTokenizer st;

        for (int i = 0; i < args.length; i++) {
            arg = args[i];
            if (arg.indexOf("=") != (-1)) {
                st = new StringTokenizer(arg, "=", false);
                name = value = null;
                if (st.hasMoreTokens())
                    name = st.nextToken();
                if (st.hasMoreTokens())
                    value = st.nextToken();

                if (name == null || value == null) {
                    buff.addElement("Arguments must be in the form 'name=value'.  The following is invalid: " + arg);
                }
            }
        }

        if (buff.size() != 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0, l = buff.size(); i < l; i++) {
                sb.append((String)buff.elementAt(i));
            }
            throw new Exception(sb.toString());
        }

    }

    public static final char LPAREN_CHAR = '(';
    public static final char RPAREN_CHAR = ')';
    public static final char LBRACE_CHAR = '[';
    public static final char RBRACE_CHAR = ']';
	


    //--------------------------------------------------
    public String interpolateWithEnvironment(String source) {
        StringBuffer target = new StringBuffer();
        String value;
        int l = source.length();
        char c;
        String others = "_-";
        int i = 0;
        int index, index2;
        StringBuffer name;
        String ret;

        while ((index = source.indexOf("$")) >= 0) {
            name = new StringBuffer();
            target.append(source.substring(0, index));
            if (source.charAt(index + 1) == LPAREN_CHAR) {
                index2 = source.indexOf(RPAREN_CHAR, index + 2);
                if (index2 >= 0) {
                    name.append(source.substring(index + 2, index2));
                    i = index2 + 1;
                } else {
                    target.append("$");
                    source = source.substring(index + 1);
                    continue;
                }
            } else {
                for (i = index + 1; i < l; i++) {
                    c = source.charAt(i);
                    if (Character.isLetterOrDigit(c) || others.indexOf(c) >= 0)
                        name.append(c);
                    else
                        break;
                }
            }
            value = getProperty(name.toString());
            if (value != null) {
                target.append(value);
            }
            source = source.substring(i);
        }
        target.append(source);
        ret = target.toString();

        if (ret.indexOf(LBRACE_CHAR) >= 0 &&
                        ret.indexOf(RBRACE_CHAR) >= 0) {
            // modify file separator characters to the current architecture
            // for strings enclosed in braces ([])

            StringBuffer target2 = new StringBuffer(target.length());
            boolean inbrace = false;
            for (i = 0, l = target.length(); i < l; i++) {
                c = target.charAt(i);
                if (c == LBRACE_CHAR)
                    inbrace = true;
                else if (c == RBRACE_CHAR)
                    inbrace = false;
                else {
                    if (inbrace && (c == '/' || c == '\\'))
                        target2.append(File.separator);
                    else
                        target2.append(c);
                }
            }
            ret = target2.toString();
        }

        return ret;
    }
    
    

}
