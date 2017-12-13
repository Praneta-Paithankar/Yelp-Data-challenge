package CollabFiltering;
/*
 * created by Aishwarya Dhage(adhage)and Praneta Paithankar(ppaithan)
 *Calculate precision, recall and MAPE of recommendations.
 */

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Evaluation {
	public static Map<String, Collection<String>> EvalgroundTruthMap = new HashMap<String, Collection<String>>();
	public static Map<String, Collection<String>> EvalrecomsimMap = new HashMap<String, Collection<String>>();

	public static void main(String args[])
	{
		EvalgroundTruthMap.putAll(ConvertData.groundTruthMap);
		EvalrecomsimMap.putAll(ItemBasedRecommendation.RrecomsimMap);
		int hitrate=0;
		int totalbusinessR=0;
		int totalbusinessP=0;	
		float mape= 0;
		int allrecommendedbusiness=0;
		int allactualbusiness=0;
		int i=0;
		for(String user:EvalrecomsimMap.keySet())
		{
			int hits=0;
			if(EvalgroundTruthMap.containsKey(user))
			{
				for(String abusiness:EvalgroundTruthMap.get(user))
				{
					if(EvalrecomsimMap.get(user).contains(abusiness))
					{
						hitrate+=1;
						hits+=1;
					}
				}
				i+=1;
				allrecommendedbusiness=(EvalrecomsimMap.get(user)).size();
				allactualbusiness=(EvalgroundTruthMap.get(user)).size();
				mape+=((float)(allactualbusiness-hits)/allactualbusiness);
				totalbusinessR+=allactualbusiness;
				totalbusinessP+=allrecommendedbusiness;
			}
		}
		int n=EvalgroundTruthMap.size();
		System.out.println("Recall: "+(float)hitrate/totalbusinessR);
		System.out.println("Precision: "+(float)hitrate/totalbusinessP);
		System.out.println("MAPE: "+(float)(mape*100)/i);		
	}
}
