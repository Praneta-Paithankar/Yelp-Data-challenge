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


public class ItemBasedRecommendation  {
	public static HashMap<String,Long> userMap = new HashMap<String,Long>();
	public static HashMap<String,Long> businessMap = new HashMap<String,Long>();
	public static Map<String, Collection<String>> useritemMap = new HashMap<String, Collection<String>>();
	public static Map<String, Collection<String>> recomsimMap = new HashMap<String, Collection<String>>();
	//public static HashMap<String,Long> groundTruthMap = new HashMap<String,Long>();
	public static HashMap<String,Long> newuserMap=new HashMap<String,Long>();
	public static Map<String, Collection<String>> groundTruthMap = new HashMap<String, Collection<String>>();

	
	public static void main(String[] args) throws IOException, TasteException
	{
		convertdata();
		itembasedrecommendation();	
	}
	
	private static void itembasedrecommendation() throws IOException, TasteException {
			RandomUtils.useTestSeed();
			BufferedWriter bw = new BufferedWriter(new FileWriter("data/similarity.csv"));
			BufferedWriter bk = new BufferedWriter(new FileWriter("data/output.csv"));		
			BufferedReader br = new BufferedReader(new FileReader("data/groundTruth.csv"));			
			DataModel dm = new FileDataModel(new File("data/userbusinesscode.csv"));
			//ItemSimilarity sim = new LogLikelihoodSimilarity(dm);
			//ItemSimilarity sim = new PearsonCorrelationSimilarity(dm);
			RecommenderBuilder recommenderBuilder=new RecommenderBuilder() 
			{
				public Recommender buildRecommender(DataModel dm) throws TasteException{
					TanimotoCoefficientSimilarity sim = new TanimotoCoefficientSimilarity(dm);
					//ItemSimilarity sim = new LogLikelihoodSimilarity(dm);
					//ItemSimilarity sim = new EuclideanDistanceSimilarity(dm);
					//ItemSimilarity similarity = new GenericUserSimilarity(model);
					//ItemSimilarity sim = new PearsonCorrelationSimilarity(dm);
					return  new GenericItemBasedRecommender(dm, sim);
				}
			};
			for(String key:newuserMap.keySet())
			{
				//GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dm, sim);		
				Recommender recommender=recommenderBuilder.buildRecommender(dm);
				List<RecommendedItem>recommendations = recommender.recommend(newuserMap.get(key), 50);
				for(RecommendedItem recommendedItem : recommendations) 
				{
					String newrecomm=null;
					for (Entry<String, Long> entry : businessMap.entrySet()) 
					{
						if (Objects.equals(recommendedItem.getItemID(), entry.getValue())) 
						{
							newrecomm=entry.getKey();
							Collection<String> newrecomms=recomsimMap.get(key);
							if (newrecomms==null)
							{
								newrecomms=new ArrayList<String>();
								recomsimMap.put(key, newrecomms);
							}
							newrecomms.add(newrecomm);
						}
					}	
				}
			}
			String line = "";
			String cvsSplitBy = ",";
			while((line = br.readLine()) != null ) 
	    		{ 
				String[] items = line.split(cvsSplitBy);
				if(recomsimMap.containsKey(items[0]))
				{
					bk.write(items[0]+","+recomsimMap.get(items[0])+"\n");
					System.out.println(items[0]+"\t"+recomsimMap.get(items[0]));
				}
	    		}		
			evaluation(recommenderBuilder,dm);
			calculatehitratio();
			bw.close();	
	}

	private static void calculatehitratio() {
		int hitrate=0;
		int totalbusiness=0;
		for(String user:recomsimMap.keySet())
		{
			if(groundTruthMap.containsKey(user))
			{
				for(String abusiness:groundTruthMap.get(user))
				{
					if(recomsimMap.get(user).contains(abusiness))
					{
						hitrate+=1;
					}
				}
				
				totalbusiness+=(groundTruthMap.get(user)).size();
			}
		}
		System.out.println(((float)hitrate/totalbusiness)*100);
		
	}

	public static void convertdata() throws IOException
	{	
		BufferedReader br = new BufferedReader(new FileReader("data/input.csv"));
		BufferedReader readUsers = new BufferedReader(new FileReader("data/groundTruth.csv"));
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
		System.out.println("starts");
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
	}
	
	private static void evaluation(RecommenderBuilder recommenderBuilder, DataModel dm) throws TasteException 
	{
	
		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();        
	    double score = evaluator.evaluate(recommenderBuilder, null, dm, 0.7, 1.0);    
	    System.out.println("RMSE: " + score);
	    RecommenderIRStatsEvaluator statsEvaluator = new GenericRecommenderIRStatsEvaluator();        
	    IRStatistics stats = statsEvaluator.evaluate(recommenderBuilder, null, dm, null, 10, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 0.7);
	    // evaluate precision recall at 10		        
	    System.out.println("Precision: " + stats.getPrecision());
	    System.out.println("Recall: " + stats.getRecall());
	    System.out.println("F1 Score: " + stats.getF1Measure());                

	
	}
}
