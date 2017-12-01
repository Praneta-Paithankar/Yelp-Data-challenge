import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import structure.FilteringInputStructure;
import structure.Review;

/*
 * created by Praneta Paithankar(ppaithan)
 */
public class GetData {
	private List<FilteringInputStructure> trainingSet;
	private List<FilteringInputStructure> testingSet;
	private MongoClient mongoClient;
	private DB database;
	private Gson gson=new Gson();

	public void getData() {
		try {

			trainingSet = new ArrayList<FilteringInputStructure> (1000);
			testingSet=new ArrayList<FilteringInputStructure> (1000);
			
			mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
			database = mongoClient.getDB("yelp_db");
			DBCollection collection =   database.getCollection("reviewCharlotte");
			//BasicDBObject query=new BasicDBObject("date", new BasicDBObject("$lt",new BasicDBObject("$date","2017-01-01")));
			DBCursor dbcursor=collection.find();
			
			System.out.println("User cursor started.");
			while(dbcursor.hasNext()){
				String reviewString=dbcursor.next().toString();
				Review review=gson.fromJson(reviewString, Review.class);
				trainingSet.add(new FilteringInputStructure(review.getUser_id(),review.getBusiness_id(),review.getStars()));	
			}
			System.out.println("User finished.");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		writeToCsv();
	}
	

	public  void writeToCsv() {

		String recordAsCsv = trainingSet.stream()
				.map(FilteringInputStructure::toCsvRow)
				.collect(Collectors.joining(System.getProperty("line.separator")));
		PrintWriter pw;
		try {
			pw = new PrintWriter(new File("test.csv"));
			pw.write(recordAsCsv);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("done!");
	}
}