import java.net.UnknownHostException;
import structure.Business;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class BusinessData {
	public static void main(String[]args) {
		//		MongoClient mongoClient;
		//		try {
		//			mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		//			DB database = mongoClient.getDB("yelp_db");
		//			DBCollection collection =   database.getCollection("business");
		//			DBCursor dbcursor=collection.find(new BasicDBObject("business_id","WKPnq7NYw4IV5IG2smpd1Q"));
		//			String data=null;
		//			Business[]trainingBusiness;
		//			Gson gson=new Gson();
		//			while(dbcursor.hasNext()){
		//				data=dbcursor.next().toString();
		//				Business business=gson.fromJson(data, Business.class);
		//				System.out.println(business.getName());
		//				
		//			}

		Data.getData();
		Data.writeToCsv();
	} 
}
