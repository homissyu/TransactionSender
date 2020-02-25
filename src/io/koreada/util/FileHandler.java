package io.koreada.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * 
 * @author Karl
 * @date 2017. 6. 19.
 */

public class FileHandler {
	
	String mSubSystem = (this.getClass()).getCanonicalName();
	
	public FileHandler(){
	}
    
	/**
	 * @param path
	 * @param c
	 */
	public void recursiveFind(Path path, Consumer<Path> c) {
		try (DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(path)) {
			StreamSupport.stream(newDirectoryStream.spliterator(), false).peek(p -> {
				c.accept(p);
	            if (p.toFile().isDirectory()) recursiveFind(p, c);
			}).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /**
     * @param raf
     * @param Os
     * @param numBytes
     * @throws IOException
     */
    public void readWrite(RandomAccessFile raf, OutputStream Os, long numBytes) throws IOException {
        byte[] buf = new byte[(int) numBytes];
        int val = raf.read(buf);
        if(val != -1) {
        	Os.write(buf);
        }   	
    }
    
    /**
     * @param raf
     * @param Os
     * @throws IOException
     */
    public void readWrite(RandomAccessFile raf, OutputStream Os) throws IOException {
    	readWrite(raf, Os, raf.length());
    }
    
    /**
     * @param sFileName
     * @param content
     * @param sPath
     * @throws JayException
     */
    public void writeFile(String sFileName, byte[] contentBuf, String sPath){
    	    String sFilePath = sPath + sFileName;
        try{
            if(checkPath(sPath)) writeFile(sFilePath, contentBuf);
            else throw new FileNotFoundException();
        }catch(Exception ex){
        		ex.printStackTrace();
        }
    }
    
    /**
     * @param sSourcePath
     * @param sTargetPath
     * @param saFileList
     * @throws IOException
     * @throws JayException
     */
    public static void copyFile(String sSourcePath, String sTargetPath, String[] saFileList) throws Exception {
        for(int i=0;i<saFileList.length;i++) {
            File fInFile = new File(sSourcePath+File.separator+saFileList[i]);
            File fOutFile = new File(sTargetPath+File.separator+saFileList[i]);
            FileInputStream in = null;
            FileOutputStream out = null;
            byte[] buffer;
            int bytes_read;

            try {
                if(!fInFile.exists() || !fInFile.isFile())
                    throw new Exception("FileCopy:no such file or directory:"+"fInFile:"+sSourcePath+File.separator+saFileList[i]);
                if(!fInFile.canRead())
                    throw new Exception("FileCopy:source file is unreadable:"+"fInFile:"+sSourcePath+File.separator+saFileList[i]);
                in = new FileInputStream(fInFile);
                out = new FileOutputStream(fOutFile);

                buffer = new byte[(int)fInFile.length()];
                while(true) {
                    bytes_read = in.read(buffer);
                    if(bytes_read == -1)
                        break;
                }

                out.write(buffer);
                out.flush();
            }finally {
                if(in != null) in.close();
                if(out != null) out.close();
            }
        }        
    }
    
    /**
     * @param sSourcePath
     * @param sTargetPath
     * @throws IOException
     * @throws JayException
     */
    public static void copyFile(String sSourcePath, String sTargetPath) throws Exception {
        File fInFile = new File(sSourcePath);
        File fOutFile = new File(sTargetPath);
        FileInputStream in = null;
        FileOutputStream out = null;
        byte[] buffer;
        int bytes_read;

        try {
            if(!fInFile.exists() || !fInFile.isFile())
                throw new Exception("FileCopy:no such file or directory:"+sSourcePath);
            if(!fInFile.canRead())
                throw new Exception("FileCopy:source file is unreadable:"+sSourcePath);
            long iLength = fInFile.length();
            if(iLength >0 ) {
            	in = new FileInputStream(fInFile);
                out = new FileOutputStream(fOutFile);
                buffer = new byte[(int)iLength];
                while(true) {
                    bytes_read = in.read(buffer);
                    if(bytes_read == -1)
                        break;
                }
                out.write(buffer);
                out.flush();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
        	if(in != null) in.close();
            if(out != null) out.close();
        }
    }
    
    public static boolean checkFilePath(String aFilePath) throws IOException {
    	boolean ret = false;
    	File file = new File(aFilePath);
        if(!file.exists()) file.createNewFile(); 
        ret = true;
    	return ret;
    }
    
    public static boolean isEmptyFile(String aFilePath) {
    	boolean ret = true;
    	File file = new File(aFilePath);
    	if(file.length()>0) ret = false;
        System.out.println(aFilePath+":"+file.length()+":"+ret);
    	return ret;
    }
    
    public static boolean checkPath(String aPath) throws IOException {
    	boolean ret = false;
    	File file = new File(aPath);
        if(!file.exists())
            file.mkdirs();
        ret = true;
    	return ret;
    }
    
    /**
     * @param sFilePath
     * @param contentBuf
     */
    public void writeFile(String sFilePath, byte[] contentBuf){
        FileOutputStream fos = null;
        try{
            if(checkFilePath(sFilePath)) {
            	fos = new FileOutputStream(sFilePath);
                fos.write(contentBuf);
                fos.close();
            } else throw new FileNotFoundException();
        }catch(Exception ex){
        	ex.printStackTrace();
        }finally {
        	try {
        		if(fos!=null)fos.close();
        	}catch(Exception e) {
        		e.printStackTrace();
        	}
        }
    }
    
    /**
     * 
     * @param sFileName
     * @param obj
     * @param sPath
     * @throws JayException
     */
    public static void writeSerFile(Object obj, String sPath, String sFileName){
        ObjectOutputStream oos = null;
        try{
        	if(checkPath(sPath) && checkFilePath(sPath + File.separator + sFileName)) {
    			oos = new ObjectOutputStream(new FileOutputStream(sPath+File.separator+sFileName));
                oos.writeObject(obj);
                oos.close();        		
        	}else throw new FileNotFoundException();
        }catch(Exception ex){
        		ex.printStackTrace();
        }finally {
        	try {
        		if(oos!=null)oos.close();
        	}catch(Exception e) {
        		e.printStackTrace();
        	}
        }
    }
    
    /**
     * 
     * @param sFileName
     * @return oRet
     * @throws JayException
     */
    public static Object readSerFile(String sFilePath){
        FileInputStream fileIn = null;
        Object oRet = null;
        ObjectInputStream ois = null;
        try {
            if(checkFilePath(sFilePath)) {
        	    fileIn = new FileInputStream(sFilePath);
                ois = new ObjectInputStream(fileIn);
                oRet = ois.readObject();
            }else throw new FileNotFoundException();
        } catch (Exception ex) {
        		ex.printStackTrace();
        } finally {
            try {
                if(fileIn!=null)fileIn.close();
                if(ois!=null)ois.close();
            } catch (IOException ex) {
            		ex.printStackTrace();
            }
        }
        return oRet;
    }
    
    /**
     * 
     * @param sFileName
     * @param obj
     * @param sPath
     * @throws JayException
     */
    public void writeSerEncFile(Object obj, String sFilePath){
    	ObjectOutputStream oos = null;
        try{
        	if(checkFilePath(sFilePath)) {
	            Debug.trace(mSubSystem, CommonConst.DEVELOPING_MODE, "sFilePath:"+sFilePath, Thread.currentThread().getStackTrace()[1].getLineNumber());
	            oos = new ObjectOutputStream(new FileOutputStream(sFilePath));
	            CryptoUtils.encryptObj((Serializable) obj, oos);
        	}else throw new FileNotFoundException();
        }catch(Exception ex){
        		ex.printStackTrace();
        }
    }
    
    /**
     * 
     * @param sFileName
     * @return oRet
     * @throws JayException
     */
    public static Object readSerEncFile(String sFilePath){
        FileInputStream fileIn = null;
        Object oRet = null;
        ObjectInputStream ois = null;
        try {
            if(checkFilePath(sFilePath)){
                fileIn = new FileInputStream(sFilePath);
                ois = new ObjectInputStream(fileIn);
                oRet = CryptoUtils.decryptObj(ois);
            }else throw new FileNotFoundException();
        } catch (Exception ex) {
        		ex.printStackTrace();
        } finally {
            try {
                if(fileIn!=null)fileIn.close();
                if(ois!=null)ois.close();
            } catch (IOException ex) {
            		ex.printStackTrace();
            }
        }
        return oRet;
    }
    
    /**
     * 
     * @param filename
     * @return boolean bResult 
     */
    public boolean isASCII(String filename) {
		boolean bResult = false;
        FileReader inputStream = null;
        try {
            inputStream = new FileReader(filename);
            int c;
            while ((c = inputStream.read()) != -1) {
                Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
                if (block == Character.UnicodeBlock.BASIC_LATIN || block == Character.UnicodeBlock.GREEK) {
//                         (9)Horizontal Tab (10)Line feed  (11)Vertical tab (13)Carriage return (32)Space (126)tilde
                	if (c==9 || c == 10 || c == 11 || c == 13 || (c >= 32 && c <= 126)) {
                		bResult = true;
//                                (153)Superscript two (160)��  (255) No break space                     
                    } else if (c == 153 || c >= 160 && c <= 255) {
                		bResult = true;
//                                (884)姑 (885)孤 (890)故 (894)槁 (900)' (974)��     
                    } else if (c == 884 || c == 885 || c == 890 || c == 894 || c >= 900 && c <= 1019) {
                		bResult = true;
                    } else {                        
                		bResult = false;
                		break;
                    }
                }                
            }
        } catch (Exception ex) {
    		ex.printStackTrace();
        } finally {
        	try {
            	if (inputStream != null) inputStream.close();
            } catch (IOException ex) {
        		ex.printStackTrace();
            }
        }
        Debug.trace("com.jay.util.Debug", CommonConst.OPERATION_MODE, "Is this file ASCII ? : "+ bResult, Thread.currentThread().getStackTrace()[1].getLineNumber());
        return bResult;
    }
    
    /**
     * @param sFileName
     * @throws Exception
     */
    public static void remove(String sFileName) throws Exception{
        File fFile = new File(sFileName);
        fFile.delete();
    }
    
    /**
     * @param sFilePath
     * @param sFileName
     * @throws Exception
     */
    public void remove(String sFilePath, String sFileName) throws Exception{
        File fFile = new File(sFilePath+File.separator+sFileName);
        fFile.delete();
    }
    
    public static Properties readProperyXML(String aFileName){
    	Properties prop = new Properties();
    	try {
    		prop.loadFromXML(new FileInputStream(aFileName));
    	} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return prop;
    }

    /**
     * @param aMap
     * @param aFileName
     * @throws Exception
     */
    public static void makePropertyXML(HashMap<?, ?> aMap, String aFileName) throws Exception {
		remove(aFileName);
		Properties prop = new Properties();
		Iterator<?> it = aMap.keySet().iterator();
		String aKey;
		FileOutputStream fOut = new FileOutputStream(aFileName);
		
		while(it.hasNext()){
			aKey = (String)it.next();
			prop.setProperty(aKey,(String)aMap.get(aKey));
		}
		prop.storeToXML(fOut,(String)aMap.get(CommonConst.SID_STRING),"UTF-8");
		fOut.close();
	}

	public static void makePropertyXML(Hashtable<String, String> mEnvironment, String aFileName) throws Exception {
		// TODO Auto-generated method stub
		
		HashMap<Object, Object> aMap = new HashMap<Object, Object>();
		Iterator<String> it = mEnvironment.keySet().iterator();
		while(it.hasNext()){
			Object key = null;
			key = it.next();
			aMap.put(key, mEnvironment.get(key));
		}
		makePropertyXML(aMap, aFileName);
	}
}