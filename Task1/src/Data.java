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
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import structure.FilteringInputStructure;
import structure.Review;
import structure.User;


public class Data implements Runnable
{

	public Data(List<FilteringInputStructure> trainingSet, List<FilteringInputStructure> testingSet, DBCursor cursor,
			DB db) {
		super();
		this.trainingSet = trainingSet;
		this.testingSet = testingSet;
		this.cursor = cursor;
		this.db = db;
	}
	public Data() {
		trainingSet = new ArrayList<FilteringInputStructure> (20);
		testingSet=new ArrayList<FilteringInputStructure> (20);
	}
	private List<FilteringInputStructure> trainingSet;

	private List<FilteringInputStructure> testingSet;
	private static MongoClient mongoClient;
	private static DB database;
	private static Gson gson=new Gson();
	DBCursor cursor;
	DB db;


	public void getData() {
		try {
			mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
			database = mongoClient.getDB("yelp_db");
			DBCollection collection =   database.getCollection("reviewCharlotte");
			BasicDBObject query=new BasicDBObject("review_count",new BasicDBObject("$gte",20));
			int totalRecords=collection.find().count();
			totalRecords=100;
			int numOfProcessors = Runtime.getRuntime().availableProcessors();
			int window = (int) (totalRecords/ numOfProcessors);
			System.out.println("window "+ window);
			DBCursor[] cursors = new DBCursor[numOfProcessors];
			Thread[] threads = new Thread[numOfProcessors];
			for(int p=0; p < numOfProcessors; p++)
			{
				cursors[p] =collection.find(query).limit(window).skip(window * p);
				Data objClass = new Data(trainingSet,testingSet,cursors[p], database );
				threads[p] = new Thread(objClass, p + "");
				threads[p].start();
			}

			for(int p=0; p < numOfProcessors; p++)
			{
				try
				{
					threads[p].join();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			writeToCsv();
			System.out.println("User finished.");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public void getReview(String userId)
	{
		String inputString = "2017-01-01";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date inputDate = dateFormat.parse(inputString);
			DBCollection reviewCollection =database.getCollection("review");
			DBCursor reviewCursor;
			BasicDBObject reviewQuery;
			reviewQuery=new BasicDBObject("user_id",userId);//.append("date", new BasicDBObject("$lt",new BasicDBObject("$date","2017-01-01"))); 
			reviewCursor=reviewCollection.find(reviewQuery).limit(1);
			while(reviewCursor.hasNext()) {
				String reviewString=reviewCursor.next().toString();
				Review review=gson.fromJson(reviewString, Review.class);
				if(review.getDate().compareTo(inputDate)<0){
					
						trainingSet.add(new FilteringInputStructure(userId,review.getBusiness_id(),review.getStars()));
				
						
				}
				else
				{
					//synchronized (testingSet) {
						testingSet.add(new FilteringInputStructure(userId, review.getBusiness_id(), review.getStars()));
					//}
				}
			}	
			System.out.println("Review of "+userId+"finished");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeToCsv() {

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
	@Override
	public void run() {
		int i=1;
		System.out.println("User cursor started.");
		while(cursor.hasNext()){
			String data = cursor.next().toString();
			User user = gson.fromJson(data, User.class);
			System.out.println(i+" Review of "+user.getUser_id()+" started");
			getReview(user.getUser_id());
			i++;
		}

	}



}
