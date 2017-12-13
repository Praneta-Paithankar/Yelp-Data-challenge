package Data;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
 * get data. from database
 */
public class GetData {

	public List<FilteringInputStructure> getData(String collectionName) {
		List<FilteringInputStructure> set = null;
		try {

			set = new ArrayList<FilteringInputStructure> (1000);
			Gson gson=new Gson();
			MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
			DB database = mongoClient.getDB("yelp_db");
			DBCollection collection =   database.getCollection(collectionName);
			DBCursor dbcursor=collection.find();
			while(dbcursor.hasNext()){
				String reviewString=dbcursor.next().toString();
				Review review=gson.fromJson(reviewString, Review.class);
				set.add(new FilteringInputStructure(review.getUser_id(),review.getBusiness_id(),review.getStars(),review.getText()));	
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return set;
	}
	// write data into csv file
	public  void writeToCsv(String fileName,List<FilteringInputStructure> set) {

		String recordAsCsv = set.stream()
				.map(FilteringInputStructure::toCsvRow)
				.collect(Collectors.joining(System.getProperty("line.separator")));
		PrintWriter pw;
		try {
			pw = new PrintWriter(new File(fileName));
			pw.write(recordAsCsv);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//get data required for IR
	public  HashMap<String,String>getIRData() {
		HashMap<String,String> businessReviewMap=new HashMap<String,String>();
		try {
			Gson gson=new Gson();
			MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
			DB database = mongoClient.getDB("yelp_db");
			DBCollection collection =   database.getCollection("reviewCharlotte");
			DBCursor dbcursor=collection.find();
			while(dbcursor.hasNext()){
				String reviewString=dbcursor.next().toString();
				Review review=gson.fromJson(reviewString, Review.class);
				String id=review.getBusiness_id();
				String res=businessReviewMap.get(id);
				if(res!=null) {
					businessReviewMap.put(id, res+review.getText());
				}
				else {
					businessReviewMap.put(id, review.getText());
				}
					
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return businessReviewMap;
	}
	//get user data required for IR
	public  HashMap<String,List<String>>getIRUserData() {
		HashMap<String,List<String>> userReviewMap=new HashMap<String,List<String>>();
		try {
			Gson gson=new Gson();
			MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
			DB database = mongoClient.getDB("yelp_db");
			DBCollection collection =   database.getCollection("reviewCharlotte");
			DBCursor dbcursor=collection.find();
			int count=0;
			while(dbcursor.hasNext()){
				String reviewString=dbcursor.next().toString();
				Review review=gson.fromJson(reviewString, Review.class);
				float rating=Float.parseFloat(review.getStars());
//				if(rating>=3.5) {
					String id=review.getUser_id();
					count++;
					if(userReviewMap.get(id)==null) {
						List<String> list=new ArrayList<String>();
						list.add(review.getText());
						userReviewMap.put(id, list);
					}
					else {
						userReviewMap.get(id).add(review.getText());
					}
//					if(count==100)
//						break;
//				}
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return userReviewMap;
	}
}
