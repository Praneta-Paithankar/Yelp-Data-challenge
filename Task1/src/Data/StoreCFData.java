package Data;
import java.util.List;

import structure.FilteringInputStructure;

/*
 * created by Praneta Paithankar(ppaithan)
 */
public class StoreCFData {
	//Store data into database
	public static void main(String[]args) {
		GetData data=new GetData();
		List<FilteringInputStructure> trainingSet = data.getData("reviewCharlotte");
		data.writeToCsv("input.csv",trainingSet);
		List<FilteringInputStructure> testingSet = data.getData("testCollection");
		data.writeToCsv("groundTruth.csv",testingSet);
		System.out.println("done");
	} 
}
