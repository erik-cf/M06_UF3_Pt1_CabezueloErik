package mongoManager;

import java.util.Iterator;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import application.Main;

public class ConnectMongo {

	public static final int CONSOLAS = 0x00001f;
	public static final int JUEGOS = 0x00002f;

	private static MongoClientURI uri;

	private static MongoClient mongoClient;

	private static MongoDatabase database;

	public static void initialize() {
		uri = new MongoClientURI(
				"mongodb+srv://erikcf:passw0rd@m06-cabezueloerik-e7cnn.mongodb.net/test?retryWrites=true&w=majority");
		mongoClient = new MongoClient(uri);
		database = mongoClient.getDatabase("Tienda");
	}

	public static MongoClientURI getMongoClientURI() {
		return uri;
	}

	public static MongoClient getMongoClient() {
		return mongoClient;
	}

	public static MongoDatabase getMongoDB() {
		return database;
	}

	public static MongoCollection<Document> getMongoCollection(int collection) {
		switch (collection) {
		case CONSOLAS:
			return database.getCollection("Consolas");
		case JUEGOS:
			return database.getCollection("Juegos");
		default:
			return null;
		}
	}

	public static FindIterable<Document> getAllDocuments(int collection) {
		switch (collection) {
		case CONSOLAS:
			return getMongoCollection(CONSOLAS).find();
		case JUEGOS:
			return getMongoCollection(JUEGOS).find();
		default:
			return null;
		}
	}

	public static boolean collectionHasDocuments(int collection) {
		switch (collection) {
		case CONSOLAS:
			return getMongoCollection(CONSOLAS).find().first() != null;
		case JUEGOS:
			return getMongoCollection(JUEGOS).find().first() != null;
		default:
			return false;
		}
	}

	public static void printConsolas() {
		Iterator<Document> it = getAllDocuments(CONSOLAS).iterator();
		Document d;
		while (it.hasNext()) {
			System.out.println("\n--------------");
			d = it.next();
			System.out.println("Nombre consola: " + d.getString("nombre"));
			System.out.println("Precio consola: " + d.getDouble("precio"));
			System.out.println("--------------");
		}
	}

	public static void printJuegos() {
		Iterator<Document> it = getAllDocuments(JUEGOS).iterator();
		Document d;
		while (it.hasNext()) {
			System.out.println("\n--------------");
			d = it.next();
			System.out.println("Nombre juego: " + d.getString("nombre"));
			System.out.println("Precio juego: " + d.getDouble("precio"));
			System.out.println("Plataforma: " + ((Document) d.get("plataforma")).getString("nombre"));
			System.out.println("--------------");
		}
	}

	public static String askForNonExistingPlatformName() {
		String nombreConsola = "";
		while (true) {
			nombreConsola = Main.sc.nextLine();
			if (getMongoCollection(CONSOLAS).countDocuments(Filters.eq("nombre", nombreConsola)) > 0) {
				System.out.println("¡Ya existe una consola con este nombre en la colección!");
				System.out.println("Introduce otro nombre:");
			} else {
				return nombreConsola;
			}
		}
	}

	public static void updateItemName(int collection, String nameFilter, String newValue) {
		getMongoCollection(collection).findOneAndUpdate(Filters.eq("nombre", nameFilter),
				Updates.set("nombre", newValue));
	}

	public static void updateItemPrice(int collection, String nameFilter, double newValue) {
		getMongoCollection(collection).findOneAndUpdate(Filters.eq("nombre", nameFilter),
				Updates.set("precio", newValue));
	}

	public static void updateItemPlatform(int collection, String nameFilter, Document newValue) {
		getMongoCollection(collection).findOneAndUpdate(Filters.eq("nombre", nameFilter),
				Updates.set("plataforma", newValue));
	}

	public static boolean checkIfPlatformExists(String platformName) {
		return getConsolaByName(platformName) != null;
	}

	public static Document getConsolaByName(String platformName) {
		return getMongoCollection(CONSOLAS).find(Filters.eq("nombre", platformName)).first();
	}

	public static String askForNonExistingGameName() {
		String nombreJuego = "";
		while (true) {
			nombreJuego = Main.sc.nextLine();
			if (getMongoCollection(JUEGOS).countDocuments(Filters.eq("nombre", nombreJuego)) > 0) {
				System.out.println("¡Ya existe un juego con este nombre en la colección!");
				System.out.println("Introduce otro nombre:");
			} else {
				return nombreJuego;
			}
		}
	}

	public static boolean checkIfGameExists(String gameName) {
		return getJuegoByName(gameName) != null;
	}

	public static Document getJuegoByName(String gameName) {
		return getMongoCollection(JUEGOS).find(Filters.eq("nombre", gameName)).first();
	}

	public static String askForExistingPlatformName() {
		String nombreConsola;
		while (true) {
			printConsolas();
			System.out.println("Introduce el nombre de la consola: ");
			nombreConsola = Main.sc.nextLine();
			if (checkIfPlatformExists(nombreConsola)) {
				return nombreConsola;
			} else {
				System.out.println("¡No existe la consola! Prueba otra vez:");
			}
		}
	}

	public static String askForExistingGameName() {
		String nombreJuego;
		while (true) {
			printJuegos();
			System.out.println("Introduce el nombre del juego: ");
			nombreJuego = Main.sc.nextLine();
			if (checkIfGameExists(nombreJuego)) {
				return nombreJuego;
			} else {
				System.out.println("¡No existe el juego! Prueba otra vez:");
			}
		}
	}

	public static void removeItem(int collection, String name) {
		switch (collection) {
		case CONSOLAS:
			getMongoCollection(CONSOLAS).deleteOne(Filters.eq("nombre", name));
			break;
		case JUEGOS:
			getMongoCollection(JUEGOS).deleteOne(Filters.eq("nombre", name));
			break;
		}
	}

	public static void findObject(int collection, String field, String value) {
		FindIterable<Document> iterable = getMongoCollection(collection).find(Filters.eq(field, value));
		for(Document d : iterable) {
			printDocument(collection, d);
		}
	}

	public static void findObject(int collection, String field, double value) {

	}

	public static void findObject(int collection, String field, Document value) {

	}
	
	public static void printDocument(int collection, Document d) {
		System.out.println("\n-------------------");
		System.out.println("Nombre: " + d.getString("nombre"));
		System.out.println("Precio: " + d.getDouble("precio"));
		if(collection == JUEGOS) {
			System.out.println("Plataforma: " + ((Document) d.get("plataforma")).getString("nombre"));
		}
		System.out.println("-------------------");
	}

	public static void closeClient() {
		mongoClient.close();
	}

}
