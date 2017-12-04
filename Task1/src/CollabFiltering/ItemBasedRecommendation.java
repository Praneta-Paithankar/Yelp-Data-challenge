package CollabFiltering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.common.RandomUtils;
/*
 * created by Aishwarya Dhage(adhage) and Praneta Paithankar(ppaithan)
 */

public class ItemBasedRecommendation  {
	public static HashMap<String,Long> user = new HashMap<String,Long>();
	public static HashMap<String,Long> business = new HashMap<String,Long>();
	public static Map<String, Collection<String>> useritem = new HashMap<String, Collection<String>>();
	public static Map<String, Collection<String>> recomsim = new HashMap<String, Collection<String>>();
	public static Map<String, Collection<String>> finalrecom = new HashMap<String, Collection<String>>();

	public static void main(String[] args) throws IOException
	{
		try {
			convertdata();
			RandomUtils.useTestSeed();
			BufferedWriter bw = new BufferedWriter(new FileWriter("data/similarity.csv"));
			BufferedWriter bk = new BufferedWriter(new FileWriter("data/output.csv"));		
			BufferedReader br = new BufferedReader(new FileReader("data/groundTruth.csv"));			
			DataModel dm = new FileDataModel(new File("data/userbusinesscode.csv"));
			//ItemSimilarity sim = new LogLikelihoodSimilarity(dm);
			//ItemSimilarity sim = new PearsonCorrelationSimilarity(dm);
		
			RecommenderBuilder recommenderBuilder=new RecommenderBuilder() {
				public Recommender buildRecommender(DataModel dm) throws TasteException{
					//TanimotoCoefficientSimilarity sim = new TanimotoCoefficientSimilarity(dm);
					//ItemSimilarity sim = new LogLikelihoodSimilarity(dm);
					//ItemSimilarity sim = new EuclideanDistanceSimilarity(dm);
					//ItemSimilarity similarity = new GenericUserSimilarity(model);
					ItemSimilarity sim = new PearsonCorrelationSimilarity(dm);
					return  new GenericItemBasedRecommender(dm, sim);
				}
			};
			for(String key:user.keySet())
{
			//GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dm, sim);		
			Recommender recommender=recommenderBuilder.buildRecommender(dm);
			List<RecommendedItem>recommendations = recommender.recommend(user.get(key), 2);
			for(RecommendedItem recommendedItem : recommendations) {
				String newrecomm=null;
				for (Entry<String, Long> entry : business.entrySet()) {
			        if (Objects.equals(recommendedItem.getItemID(), entry.getValue())) {
			            newrecomm=entry.getKey();
			            //System.out.println(key+"\t"+newrecomm);
						Collection<String> newrecomms=recomsim.get(key);
							if (newrecomms==null)
							{
								newrecomms=new ArrayList<String>();
								recomsim.put(key, newrecomms);
							}
							newrecomms.add(newrecomm);
			        }
			    }
				
			}
}
//			BufferedReader br = new BufferedReader(new FileReader("data/groundTruth.csv"));
			String line = "";
			String cvsSplitBy = ",";
//			while((line = br.readLine()) != null ) 
//	    		{
//			String[] items = line.split(cvsSplitBy);
//			if(finalrecom.containsKey(items[0]))
//			{
//				System.out.println(items[0]+"\t"+finalrecom.get(items[0]));
//			}
			while((line = br.readLine()) != null ) 
	    		{ 
				String[] items = line.split(cvsSplitBy);
				if(recomsim.containsKey(items[0]))
				{
					bk.write(items[0]+","+recomsim.get(items[0])+"\n");
					System.out.println(items[0]+"\t"+recomsim.get(items[0]));
				}
	    		}
//			for(String auser:recomsim.keySet())
//			{
//				if()
//				bk.write(auser+","+recomsim.get(auser)+"\n");
//				System.out.println(auser+"\t"+recomsim.get(auser));
//			}
//			int x=1;
//			for(LongPrimitiveIterator items =(dm.getItemIDs()); items.hasNext();) 
//			{
//				long itemId =(long)items.next();
//			
//				List<RecommendedItem>recommendations = recommender.recommend(itemId, 5);
//				for(RecommendedItem recommendation : recommendations) 
//				{
//					String pastbusiness = null;
//					String recommendedbusiness=null;
//					 for (Entry<String, Long> entry : business.entrySet()) {
//					        if (Objects.equals(itemId, entry.getValue())) {
//					            pastbusiness=entry.getKey();
//					        }
//					    }
//					 Long recommendedlongkey=recommendation.getItemID();
//					 for (Entry<String, Long> entry : business.entrySet()) {
//					        if (Objects.equals(recommendedlongkey, entry.getValue())) {
//					            recommendedbusiness=entry.getKey();
//					        }
//					    }
//					 Collection<String> newbussids=recomsim.get(pastbusiness);
//						if (newbussids==null)
//						{
//							newbussids=new ArrayList<String>();
//							recomsim.put(pastbusiness, newbussids);
//						}
//						newbussids.add(recommendedbusiness);
//					bw.write(pastbusiness+ "," +recommendedbusiness + "," + recommendation.getValue()+"\n");
//				}
//				x++;
//			}
			RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();        
		    double score = evaluator.evaluate(recommenderBuilder, null, dm, 0.7, 1.0);    
		    System.out.println("RMSE: " + score);
		        
		        RecommenderIRStatsEvaluator statsEvaluator = new GenericRecommenderIRStatsEvaluator();        
		        IRStatistics stats = statsEvaluator.evaluate(recommenderBuilder, null, dm, null, 10, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 0.7); // evaluate precision recall at 10
		        
		    System.out.println("Precision: " + stats.getPrecision());
		    System.out.println("Recall: " + stats.getRecall());
		    System.out.println("F1 Score: " + stats.getF1Measure());                
		    

			bw.close();
			} catch (IOException e) 
			{
				System.out.println("There was an error.");
				e.printStackTrace();
			} catch (TasteException e)
			{
				System.out.println("There was a Taste Exception");
				e.printStackTrace();
			}
//			recommenditems();
//			BufferedReader br = new BufferedReader(new FileReader("data/groundTruth.csv"));
			String line = "";
			String cvsSplitBy = ",";
//			while((line = br.readLine()) != null ) 
//	    		{
//			String[] items = line.split(cvsSplitBy);
//			if(finalrecom.containsKey(items[0]))
//			{
//				System.out.println(items[0]+"\t"+finalrecom.get(items[0]));
//			}
			  
	    		
			
			
	}
	
	public static void convertdata() throws IOException
	{	
		BufferedReader br = new BufferedReader(new FileReader("data/input.csv"));
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
			if(business.containsKey(values[1]) && !user.containsKey(values[0])) 
			{
				//long hashCode = values[0].hashCode();
				user.put( values[0], longuserid);
				bw.write(longuserid+","+business.get(values[1])+","+values[2]+"\n");
				longuserid++;
			}
			//check if this user id  is in hashmap
			else if(!business.containsKey(values[1]) && user.containsKey(values[0]))
			{
				//long hashCode1 = values[1].hashCode();
				business.put(values[1], longbussid);
				bw.write(user.get(values[0])+","+longbussid+","+values[2]+"\n");
				longbussid++;
			}
			//
			else if(!business.containsKey(values[1]) && !user.containsKey(values[0]))
			{
				business.put(values[1], longbussid);
				user.put( values[0], longuserid);
				bw.write(longuserid+","+longbussid+","+values[2]+"\n");
				longuserid++;
				longbussid++;
			}
			else if(business.containsKey(values[1]) && user.containsKey(values[0]))
			{
				bw.write(user.get(values[0])+","+business.get(values[1])+","+values[2]+"\n");
			}
			String bussid=values[1];
			String userid=values[0];
			Collection<String> bussids=useritem.get(userid);
			if (bussids==null)
			{
				bussids=new ArrayList<String>();
				useritem.put(userid,bussids);
			}
			bussids.add(bussid);
		}
		System.out.println("starts");
//		for(String key:user.keySet())
//		{
//			System.out.println(key+"\t"+user.get(key));
//		}
		br.close();
		bw.close();	
		
		
	}
	
	public static void recommenditems() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("data/similarity.csv"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("data/output.csv"));
		
		
		
		for(String auser:user.keySet())
		{
		for(String abusiness:useritem.get(auser))
		{
			if(recomsim.containsKey(abusiness))
			{
				Collection<String> bussids=finalrecom.get(auser);
				if(finalrecom.keySet().contains(auser))
				{
					//Collection<String> bussids=useritem.get(userid);
					
					bussids.addAll(recomsim.get(abusiness));
				}
				else
				{
					bussids=new ArrayList<String>();
					finalrecom.put(auser,bussids);
					bussids.addAll(recomsim.get(abusiness));
				}
			}
		}
		
		}

		bw.close();	
	}
}
