package CollabFiltering;

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
		for(String user:EvalrecomsimMap.keySet())
		{
			if(EvalgroundTruthMap.containsKey(user))
			{
				for(String abusiness:EvalgroundTruthMap.get(user))
				{
					if(EvalrecomsimMap.get(user).contains(abusiness))
					{
						hitrate+=1;
					}
				}		
				totalbusinessR+=(EvalgroundTruthMap.get(user)).size();
				totalbusinessP+=(EvalrecomsimMap.get(user)).size();
			}
		}
		System.out.println("Recall: "+(float)hitrate/totalbusinessR);
		System.out.println("Precision: "+(float)hitrate/totalbusinessP);		
	}
}
