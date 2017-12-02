package IR;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Evaluation {
	public static void main(String[]args) {
		Path csvFile = Paths.get(System.getProperty("user.dir"),"IR.csv");
		String line = "";
		String cvsSplitBy = ",";
		HashMap<String,List<String>> userBusinesssMap=new HashMap<String,List<String>>();

		try(BufferedReader br = new BufferedReader(new FileReader(csvFile.toString()))){
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] mapping = line.split(cvsSplitBy);
				List<String> res = userBusinesssMap.get(mapping[0]);
				if(res==null) {
					res=new ArrayList<String>();
					res.add(mapping[1]);
					userBusinesssMap.put(mapping[0], res);
				}
				else {
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

