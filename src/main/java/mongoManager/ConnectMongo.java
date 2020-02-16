package mongoManager;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class ConnectMongo {
	private static MongoClientURI uri = new MongoClientURI(
		    "mongodb+srv://erikcf:passw0rd@m06-cabezueloerik-e7cnn.mongodb.net/test?retryWrites=true&w=majority");
	
	private static MongoClient mongoClient = new MongoClient(uri);
	
	private static MongoDatabase database = mongoClient.getDatabase("Entretenimiento");

	
	public static MongoClientURI getMongoClientURI() {
		return uri;
	}
	
	public static MongoClient getMongoClient() {
		return mongoClient;
	}
	
	public static MongoDatabase getMongoDB() {
		return database;
	}
	
	public static void closeClient() {
		mongoClient.close();
	}
	
}
