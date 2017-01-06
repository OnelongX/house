package com.sxdt.house.log;

import java.text.SimpleDateFormat;
import java.util.*;

public class LogQueue {
	
	public static Queue qlog= new LinkedList();
	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public synchronized static void addLog(String log)
	{
		String date =format.format(new Date());
        qlog.add(date+"  "+log);
	}

	public  static String outLog()
    {
        if (qlog.size() > 0)
        {
            return qlog.poll().toString();
        }
        else
        {
            return null;
        }
    }

	public synchronized static List<String> outLog50()
    {
        if (qlog.size() > 0)
        {
        	List list = new ArrayList();
        	for(int i=0;i<50;i++)
        	{
        	  String log =(String) LogQueue.outLog();
         	   if(log==null)
         	   {
         		   break;
         	   }
         	  list.add(log);
        	}        	
            return list;
        }
        else
        {
            return null;
        }
    }
}
