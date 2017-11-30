import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class DatabaseOperations {
	public static void main(String[] args) {
		try {
			MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
			DB database = mongoClient.getDB("yelp_db");
			if(!database.collectionExists("business")) {
				insert("/Users/praneta/Downloads/dataset/business.json",database,"business");
			}
			if(!database.collectionExists("user")) {
				insert("/Users/praneta/Downloads/dataset/user.json",database,"user");

			}
			if(!database.collectionExists("review")) {
				insert("/Users/praneta/Downloads/dataset/review.json",database,"review");

			}
			if(!database.collectionExists("tip")) {
				insert("/Users/praneta/Downloads/dataset/tip.json",database,"tip");
			}


		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void insert(String path, DB db, String collectionName)
	{
		FileInputStream fstream = null;
		try
		{
			fstream = new FileInputStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String str1;
			DBCollection collection =   db.getCollection(collectionName);
			while ((str1 = br.readLine()) != null)
			{
				DBObject bson = (DBObject) JSON.parse(str1);
				try
				{
					collection.insert(bson);
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
		}

	}
}
