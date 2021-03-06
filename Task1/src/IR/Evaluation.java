package IR;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
 * created by Aishwarya Dhage(adhage) and  Praneta Paithankar(ppaithan)
 */
public class Evaluation {
	//Evaluate precision,relevance, and MAPE
	public static void main(String[]args) {
		Path csvFile = Paths.get(System.getProperty("user.dir"),"IR.csv");
		String line = "";
		String cvsSplitBy = ",";
		HashMap<String,List<String>> userBusinessMap=new HashMap<String,List<String>>();
		HashMap<String,List<String>> groundTruthMap=new HashMap<String,List<String>>();

		try(BufferedReader br = new BufferedReader(new FileReader(csvFile.toString()))){
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] mapping = line.split(cvsSplitBy);
				List<String> res = userBusinessMap.get(mapping[0]);
				if(res==null) {
					res=new ArrayList<String>();
					res.add(mapping[1]);
					userBusinessMap.put(mapping[0], res);
				}
				else {
					userBusinessMap.get(mapping[0]).add(mapping[1]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		csvFile=Paths.get(System.getProperty("user.dir"),"groundTruth.csv");
		try(BufferedReader br = new BufferedReader(new FileReader(csvFile.toString()))){
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] mapping = line.split(cvsSplitBy);
				if(userBusinessMap.containsKey(mapping[0])) {
					List<String> res = groundTruthMap.get(mapping[0]);
					if(res==null) {
						res=new ArrayList<String>();
						res.add(mapping[1]);
						groundTruthMap.put(mapping[0], res);
					}
					else {
						groundTruthMap.get(mapping[0]).add(mapping[1]);
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		int commonRestaurant=0;
		int totalRestaurant=0;
		int retrievedDocument=0;
		int relevantDocument=0;
		int totalRelevant=0;
		double sum=0.0;
		int n=0;
		for(String user:userBusinessMap.keySet())
		{
			relevantDocument=0;
			totalRelevant=0;
			if(groundTruthMap.get(user)!=null) {
				retrievedDocument+=userBusinessMap.get(user).size();
				for(String business_id:userBusinessMap.get(user))
				{
					if(groundTruthMap.get(user).contains(business_id))
					{
						commonRestaurant+=1;
						relevantDocument+=1;
					}
					
				}
				totalRestaurant+=groundTruthMap.get(user).size();
				totalRelevant+=groundTruthMap.get(user).size();
				sum+=((double)(totalRelevant-relevantDocument)/totalRelevant);
				n++;
			}
			
		}
		System.out.println("commonRestaurant "+commonRestaurant);
		System.out.println("totalRestaurant "+totalRestaurant);
		if(totalRestaurant!=0)
		{
			double res = (double) ((double)commonRestaurant/totalRestaurant)*100;
			
			System.out.println("HitRate "+ res);
		}
		System.out.println("Relevant"+relevantDocument);
		System.out.println("Retrieved"+retrievedDocument);
		System.out.println("totalRelevant"+totalRelevant);
		if(retrievedDocument!=0)
		{
			double res = (double) ((double)commonRestaurant/retrievedDocument);
			
			System.out.println("Precision"+ res);
		}
		if(totalRelevant!=0)
			System.out.println("Recall"+(double) ((double)commonRestaurant/totalRestaurant));
		double res=sum/n;
		System.out.println("MAPE"+res*100);
		
	}

}

