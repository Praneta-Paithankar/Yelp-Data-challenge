package CollabFiltering;
/*
 * created by Aishwarya Dhage(adhage)and Praneta Paithankar(ppaithan)
 *Converts string user and business ids into long datatype.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ConvertData {
	public static HashMap<String,Long> userMap = new HashMap<String,Long>();
	public static HashMap<String,Long> businessMap = new HashMap<String,Long>();
	public static Map<String, Collection<String>> useritemMap = new HashMap<String, Collection<String>>();
	public static HashMap<String,Long> newuserMap=new HashMap<String,Long>();
	public static Map<String, Collection<String>> groundTruthMap = new HashMap<String, Collection<String>>();

	public static void main(String args[]) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("input.csv"));
		BufferedReader readUsers = new BufferedReader(new FileReader("groundTruth.csv"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("data/userbusinesscode.csv"));
		String line;
		Long longuserid=(long) 1;
		Long longbussid=(long) 1000000;
		while((line = br.readLine()) != null) 
		{
			String[] values = line.split(",", -1);
			int rating=Integer.parseInt(values[2]);
			//check if this business id is present in hashmap
			//if true den get the long value from hash table and then put in csv file
			if(businessMap.containsKey(values[1]) && !userMap.containsKey(values[0])) 
			{
				userMap.put( values[0], longuserid);
				bw.write(longuserid+","+businessMap.get(values[1])+","+values[2]+"\n");
				longuserid++;
			}
			//check if this user id  is in hashmap
			else if(!businessMap.containsKey(values[1]) && userMap.containsKey(values[0]))
			{
				businessMap.put(values[1], longbussid);
				bw.write(userMap.get(values[0])+","+longbussid+","+values[2]+"\n");
				longbussid++;
			}
			//check if both buss. id and user id are not in hashmap
			else if(!businessMap.containsKey(values[1]) && !userMap.containsKey(values[0]))
			{
				businessMap.put(values[1], longbussid);
				userMap.put( values[0], longuserid);
				bw.write(longuserid+","+longbussid+","+values[2]+"\n");
				longuserid++;
				longbussid++;
			}
			else if(businessMap.containsKey(values[1]) && userMap.containsKey(values[0]))
			{
				bw.write(userMap.get(values[0])+","+businessMap.get(values[1])+","+values[2]+"\n");
			}
			//map user ids with bussiness ids
			String bussid=values[1];
			String userid=values[0];
			Collection<String> bussids=useritemMap.get(userid);
			if (bussids==null)
			{
				bussids=new ArrayList<String>();
				useritemMap.put(userid,bussids);
			}
			bussids.add(bussid);
			
		}
		br.close();
		bw.close();	
		while((line = readUsers.readLine()) != null) 
		{
			String[] values = line.split(",", -1);
			//groundTruthMap.put(values[0], values[1]);
			Collection<String> bussids=groundTruthMap.get(values[0]);
			if (bussids==null)
			{
				bussids=new ArrayList<String>();
				groundTruthMap.put(values[0],bussids);
			}
			bussids.add(values[1]);
		}
		readUsers.close();
		newuserMap.putAll(userMap);
		for(String a:userMap.keySet())
		{
			if(!groundTruthMap.containsKey(a))
			{
				newuserMap.remove(a);
			}
		}
		System.out.println("Conversion finished");
	}

}
