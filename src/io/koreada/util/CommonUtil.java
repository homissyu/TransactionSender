package io.koreada.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import io.koreada.util.CommonConst;

public class CommonUtil {
	/**
	 * Quick Sort �˰���
	 *
	 * @param aArr
	 * @param aLo0
	 * @param aHi0
	 */
	public static void quickSort(int[] aArr, int aLo0, int aHi0){
		int lo = aLo0;
	    int hi = aHi0;
	    if (lo >= hi){
	    	return;
	    } else if (lo == hi - 1) {
	    	if (aArr[lo] > aArr[hi]){
	    		int T = aArr[lo];
	    		aArr[lo] = aArr[hi];
	    		aArr[hi] = T;
	    	}
	    	return;
	    }

	    int pivot = aArr[(lo + hi) / 2];
	    aArr[(lo + hi) / 2] = aArr[hi];
	    aArr[hi] = pivot;

	    while (lo < hi){
	    	while (aArr[lo] <= pivot && lo < hi){
	    		lo++;
	    	}
	    	while (pivot <= aArr[hi] && lo < hi){
	    		hi--;
	    	}
	    	if (lo < hi){
	    		int T = aArr[lo];
	    		aArr[lo] = aArr[hi];
	    		aArr[hi] = T;
	    	}
	    }

	    aArr[aHi0] = aArr[hi];
	    aArr[hi] = pivot;

	    quickSort(aArr, aLo0, lo - 1);
	    quickSort(aArr, hi + 1, aHi0);
	}

	public static String getCurrentTime(String dataFormat){
        Date date = new Date();
        SimpleDateFormat simpledateformat = new SimpleDateFormat(dataFormat);
        String s = simpledateformat.format(date);
        return s;
    }

	/**
     * 
     * @param iLength
     * @return String
     */
    public static String makeUniqueTimeID(int iLength){
        StringBuilder sUniqueID = new StringBuilder();
    	Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String strMillSec = sdf.format(dt);
        sUniqueID.append(strMillSec);
        int iTempIdx = 0;
        while(true){
            iTempIdx = (int)(Math.random() * 100);
            if(iTempIdx<CommonConst.getSalphabetdecimalchar().length){
                sUniqueID.append(CommonConst.getSalphabetdecimalchar()[iTempIdx]);
            }
            if(sUniqueID.length()==iLength) break;
        }
        sUniqueID.append(((int)(Math.random() * 100)));
        return sUniqueID.toString();
    }
    
    /**
     * @param arrList
     * @return
     */
    public static Vector<Vector<Object>> toVector(ArrayList<?> arrList){
    	Vector<Vector<Object>> data = new Vector<Vector<Object>>();
//    	System.out.println(arrList);
    	Iterator<?> dataIt = arrList.iterator();
    	HashMap<?, ?> currentRow = null;
    	Iterator<?> rowIt = null;
    	Object colValue = null;
    	while(dataIt.hasNext()){
            currentRow = (HashMap<?, ?>)dataIt.next();
//            System.out.println(currentRow);
            
            rowIt = currentRow.keySet().iterator();
            Vector<Object> row = new Vector<Object>();
            while(rowIt.hasNext()){
                colValue = (Object)currentRow.get(rowIt.next());
                row.addElement(colValue);
            }
            row.addElement(currentRow);
            data.addElement(row);
    	}
    	return data;
    }
}
