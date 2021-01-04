package io.koreada.supportfactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import io.koreada.util.Install;

public class APIFactory {
	Install mInstall = null;
	
	public APIFactory(Install mInstall) {
		this.mInstall = mInstall;
	}
	
	@SuppressWarnings("deprecation")
	public boolean deposit2API(String aParam) throws Exception {                                                                                                    
    	boolean ret = false;
		URL url = null;
    	DataOutputStream dos = null;
    	HttpURLConnection mHttpConn = null;
    	try {
    		url = new URL(URLDecoder.decode(mInstall.getProperty(Install.SMART_BRIDGE_API)));
    		
    		mHttpConn = (HttpURLConnection) url.openConnection();
    		
    		mHttpConn.setConnectTimeout(5000);
    		mHttpConn.setReadTimeout(5000);
    		
//    		mHttpConn.addRequestProperty("key", "value"); 
    		
    		mHttpConn.setRequestMethod("POST");
			
    		mHttpConn.setRequestProperty("Accept", "application/json");
			mHttpConn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			mHttpConn.setDoInput(true);
			mHttpConn.setDoOutput(true);
			mHttpConn.setUseCaches(false);
			mHttpConn.setDefaultUseCaches(false);
//		System.out.println(mHttpConn.getContentLength());
			dos = new DataOutputStream(mHttpConn.getOutputStream());
			dos.write(aParam.getBytes("UTF-8"));
			dos.flush();
			
			StringBuilder sb = new StringBuilder();
			int iResponseCode = 0;
			if ((iResponseCode= mHttpConn.getResponseCode()) == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(mHttpConn.getInputStream(), "utf-8"));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
				br.close();
				System.out.println("" + sb.toString());
				ret = true;
			} else {
				System.out.println(iResponseCode+":"+mHttpConn.getResponseMessage());
			}
			
			dos.close();
			
    	}catch(Exception e) {
    		throw e;
    	}finally {
    		mHttpConn.disconnect();
    		if(dos != null) dos.close();
    	}
    	return ret;
    }
}
