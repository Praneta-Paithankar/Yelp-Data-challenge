package CollabFiltering;
/*
 * created by Aishwarya Dhage(adhage)and Praneta Paithankar(ppaithan)
 *Implements collaborative filtering algorithm.
 */

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
	public static HashMap<String,Long> RuserMap = new HashMap<String,Long>();
	public static HashMap<String,Long> RbusinessMap = new HashMap<String,Long>();
	public static Map<String, Collection<String>> RuseritemMap = new HashMap<String, Collection<String>>();
	public static Map<String, Collection<String>> RrecomsimMap = new HashMap<String, Collection<String>>();
	public static HashMap<String,Long> RnewuserMap=new HashMap<String,Long>();
	public static Map<String, Collection<String>> RgroundTruthMap = new HashMap<String, Collection<String>>();

	
	public static void main(String[] args) throws IOException, TasteException
	{
		ConvertData.main(args);
		RuserMap.putAll(ConvertData.userMap);
		RbusinessMap.putAll(ConvertData.businessMap);
		BufferedWriter bw = new BufferedWriter(new FileWriter("data/similarity.csv"));
		BufferedWriter bk = new BufferedWriter(new FileWriter("data/output.csv"));		
		BufferedReader br = new BufferedReader(new FileReader("groundTruth.csv"));			
		DataModel dm = new FileDataModel(new File("data/userbusinesscode.csv"));
		RecommenderBuilder recommenderBuilder=new RecommenderBuilder() 
		{
			public Recommender buildRecommender(DataModel dm) throws TasteException{
				//TanimotoCoefficientSimilarity sim = new TanimotoCoefficientSimilarity(dm);
				//ItemSimilarity sim = new LogLikelihoodSimilarity(dm);
				//ItemSimilarity sim = new EuclideanDistanceSimilarity(dm);
				//ItemSimilarity similarity = new GenericUserSimilarity(model);
				ItemSimilarity sim = new PearsonCorrelationSimilarity(dm);
				return  new GenericItemBasedRecommender(dm, sim);
			}
		};
		for(String key:RuserMap.keySet())
		{
			Recommender recommender=recommenderBuilder.buildRecommender(dm);
			List<RecommendedItem>recommendations = recommender.recommend(RuserMap.get(key), 25);
			for(RecommendedItem recommendedItem : recommendations) 
			{
				String newrecomm=null;
				for (Entry<String, Long> entry : RbusinessMap.entrySet()) 
				{
					if (Objects.equals(recommendedItem.getItemID(), entry.getValue())) 
					{
						newrecomm=entry.getKey();
						Collection<String> newrecomms=RrecomsimMap.get(key);
						if (newrecomms==null)
						{
							newrecomms=new ArrayList<String>();
							RrecomsimMap.put(key, newrecomms);
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
			if(RrecomsimMap.containsKey(items[0]))
			{
				bk.write(items[0]+","+RrecomsimMap.get(items[0])+"\n");
				System.out.println(items[0]+"\t"+RrecomsimMap.get(items[0]));
			}
	    	}		
		bw.close();	
		Evaluation.main(args);		
	}

	//evaluation using mahout
//	private static void evaluation(RecommenderBuilder recommenderBuilder, DataModel dm) throws TasteException 
//	{	
//		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();        
//	    double score = evaluator.evaluate(recommenderBuilder, null, dm, 0.7, 1.0);    
//	    System.out.println("RMSE: " + score);
//	    RecommenderIRStatsEvaluator statsEvaluator = new GenericRecommenderIRStatsEvaluator();        
//	    IRStatistics stats = statsEvaluator.evaluate(recommenderBuilder, null, dm, null, 10, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 0.7);
//	    // evaluate precision recall at 10		        
//	    System.out.println("Precision: " + stats.getPrecision());
//	    System.out.println("Recall: " + stats.getRecall());
//	    System.out.println("F1 Score: " + stats.getF1Measure());                
//	}
}
