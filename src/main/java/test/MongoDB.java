package test;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDB {

	public static void main(String[] args) {

		MongoClient mongoClient = new MongoClient("localhost", 27017);

		MongoDatabase database = mongoClient.getDatabase("saud");
		
		

	}

}
