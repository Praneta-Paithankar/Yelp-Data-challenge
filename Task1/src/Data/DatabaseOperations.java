package Data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
/*
 * created by Praneta Paithankar(ppaithan)
 */
public class DatabaseOperations {

	private  Set<String> business_id= new HashSet<String>();
	private  Set<String>user_id=new HashSet<String>(1000);
	private  Set<String>user_id_for_review=new HashSet<String>(1000);
	public static void main(String[] args) {
		try {
			MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
			DB database = mongoClient.getDB("yelp_db");
			DatabaseOperations databaseOperations=new DatabaseOperations();
			databaseOperations.insert("/Users/praneta/Downloads/dataset/business.json",database,"businessCharlotte");
			databaseOperations.insert("/Users/praneta/Downloads/dataset/review.json",database,"review1");
			databaseOperations.insert("/Users/praneta/Downloads/dataset/user.json",database,"userCharlotte");
			databaseOperations.insert("/Users/praneta/Downloads/dataset/tip.json",database,"tipCharlotte");
			databaseOperations.insert("/Users/praneta/Downloads/dataset/review.json",database,"reviewCharlotte");

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public  void insert(String path, DB db,String collectionName)
	{
		FileInputStream fstream = null;
		try
		{
			fstream = new FileInputStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String str1;
			DBCollection collection = db.getCollection(collectionName);
			DBCollection testCollection=db.getCollection("testCollection");
			String inputString = "2016-01-01";
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date inputDate = dateFormat.parse(inputString);
			while ((str1 = br.readLine()) != null)
			{
				DBObject bson = (DBObject) JSON.parse(str1);
				try
				{	
					if(collectionName == "businessCharlotte") {
						String res=(String) bson.get("city");
						if(res.equals("Charlotte"))
						{	
							int count=(int)bson.get("review_count");
							if(count>=100)
							{
								BasicDBList category=(BasicDBList) bson.get("categories");
								for(Object obj:category) {
									if(String.valueOf(obj).equals("Restaurants"))
									{	
											business_id.add((String) bson.get("business_id"));
											collection.insert(bson);
											break;
									}
								}
								
							}	
						}
					}
					if(collectionName=="review1") {
						String key=(String) bson.get("business_id");
						if(business_id.contains(key)){
							user_id.add((String) bson.get("user_id"));
							collection.insert(bson);
						}
					}
					if(collectionName=="reviewCharlotte") {
						Date dbDate=dateFormat.parse((String) bson.get("date"));
						String key=(String) bson.get("business_id");
						if(user_id.contains((String) bson.get("user_id"))&&business_id.contains(key)){
							if(dbDate.compareTo(inputDate)<0)
								collection.insert(bson);
							else
								testCollection.insert(bson);
						}
					}
					if(collectionName=="userCharlotte") {
						int count=(int)bson.get("review_count");
						if(user_id.contains((String) bson.get("user_id"))&& count >=20){
							user_id_for_review.add((String) bson.get("user_id"));
							collection.insert(bson);
						}
					}
					if(collectionName=="tipCharlotte") {
						if(user_id_for_review.contains((String) bson.get("user_id"))){
							collection.insert(bson);
						}
					}
				}
				catch (MongoException e)
				{
					e.printStackTrace();
				}
			}
			br.close();
			System.out.println("Inserted "+Integer.toString(collection.find().count()) + " rows into the table " + collection.toString());
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("file not exist, exiting");
			return;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
