package main;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

public class TestMongoDB {
	
	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("Syl");

		FindIterable<Document> iterable = database.getCollection("seleniumGoogleNews").find(new Document("Title", "ASIC to probe mortgage brokers"));
		if (iterable.first() != null){
			System.out.println("exitis");
		}
	}

}
