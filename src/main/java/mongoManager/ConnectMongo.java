package mongoManager;

import java.util.ArrayList;
import java.util.Iterator;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import application.Main;

public class ConnectMongo {
	// Constantes que definen la colecci�n 
	public static final int CONSOLAS = 0x00001f;
	public static final int JUEGOS = 0x00002f;
	
	// Constantes que definen las operaciones de b�squeda avanzada
	public static final int IGUAL = 0x00011f;
	public static final int DIFERENTE = 0x00012f;
	public static final int MAYOR = 0x00013f;
	public static final int MAYORIGUAL = 0x00014f;
	public static final int MENOR = 0x00015f;
	public static final int MENORIGUAL = 0x00016f;
	
	// Objetos de la conexi�n con mongo
	private static MongoClientURI uri;
	private static MongoClient mongoClient;
	private static MongoDatabase database;

	// M�todo que inicializa la conexi�n
	public static void initialize() {
		// Inicializamos la URI
		uri = new MongoClientURI(
				"mongodb+srv://erikcf:passw0rd@m06-cabezueloerik-e7cnn.mongodb.net/test?retryWrites=true&w=majority");
		// Inicializamos el cliente de mongo
		mongoClient = new MongoClient(uri);
		// Inicializamos la DataBase
		database = mongoClient.getDatabase("Tienda");
	}
	
	// M�todo que retorna la URI de mongo
	public static MongoClientURI getMongoClientURI() {
		return uri;
	}

	// M�todo que retorna el cliente de mongo
	public static MongoClient getMongoClient() {
		return mongoClient;
	}

	// M�todo que retorna la DataBase de mongo
	public static MongoDatabase getMongoDB() {
		return database;
	}

	// M�todo que retorna una colecci�n seg�n el identificador que se le pasa por par�metro
	public static MongoCollection<Document> getMongoCollection(int collection) {
		
		switch (collection) {
		// En el caso de consolas:
		case CONSOLAS:
			// Retornamos consolas
			return database.getCollection("Consolas");
		// En el caso de juegos
		case JUEGOS:
			// Retornamos juegos
			return database.getCollection("Juegos");
		// Si se pasa un identificador no creado, se retornar� null
		default:
			return null;
		}
	}

	// M�todo que retorna todos los documentos de una colecci�n seg�n el identificador que se le pase por par�metro:
	public static FindIterable<Document> getAllDocuments(int collection) {
		return getMongoCollection(collection).find();
	}

	// M�todo que comprueba si una colecci�n contiene documentos:
	public static boolean collectionHasDocuments(int collection) {
		return getMongoCollection(collection).find().first() != null;
	}

	// M�todo que imprime todas las consolas
	public static void printConsolas() {
		// Recogemos todas las consolas y creamos un iterador
		Iterator<Document> it = getAllDocuments(CONSOLAS).iterator();
		Document d;
		// Recorremos el iterador para imprimir cada una:
		while (it.hasNext()) {
			System.out.println("\n--------------");
			d = it.next();
			System.out.println("Nombre consola: " + d.getString("nombre"));
			System.out.println("Precio consola: " + d.getDouble("precio"));
			System.out.println("--------------");
		}
	}

	//M�todo que imprime todos los juegos
	public static void printJuegos() {
		// Recogemos todos los juegos y creamos un iterador
		Iterator<Document> it = getAllDocuments(JUEGOS).iterator();
		Document d;
		// Recorremos el iterador para imprimir cada uno:
		while (it.hasNext()) {
			System.out.println("\n--------------");
			d = it.next();
			System.out.println("Nombre juego: " + d.getString("nombre"));
			System.out.println("Precio juego: " + d.getDouble("precio"));
			System.out.println("Plataforma: " + ((Document) d.get("plataforma")).getString("nombre"));
			System.out.println("--------------");
		}
	}

	// M�todo que pide al usuario una consola que no haya sido registrada:
	public static String askForNonExistingPlatformName() {
		String nombreConsola = "";
		// Mientras no se introduzca un input valido, seguimos pidiendo un nombre al usuario
		while (true) {
			nombreConsola = Main.sc.nextLine();
			// Miramos si hay resultados con el nombre introducido
			if (checkIfPlatformExists(nombreConsola)) {
				System.out.println("�Ya existe una consola con este nombre en la colecci�n!");
				System.out.println("Introduce otro nombre:");
			} else {
				return nombreConsola;
			}
		}
	}

	// M�todo que actualiza el campo nombre de un objeto
	public static void updateItemName(int collection, String nameFilter, String newValue) {
		getMongoCollection(collection).findOneAndUpdate(Filters.eq("nombre", nameFilter),
				Updates.set("nombre", newValue));
	}

	// M�todo que actualiza el campo precio de un objeto
	public static void updateItemPrice(int collection, String nameFilter, double newValue) {
		getMongoCollection(collection).findOneAndUpdate(Filters.eq("nombre", nameFilter),
				Updates.set("precio", newValue));
	}

	// M�todo que actualiza el campo plataforma de los juegos
	public static void updateItemPlatform(int collection, String nameFilter, Document newValue) {
		getMongoCollection(collection).findOneAndUpdate(Filters.eq("nombre", nameFilter),
				Updates.set("plataforma", newValue));
	}

	// M�todo que comprueba si existe una plataforma
	public static boolean checkIfPlatformExists(String platformName) {
		return getConsolaByName(platformName) != null;
	}

	// M�todo que retorna un documento de consola si existe por el nombre 
	public static Document getConsolaByName(String platformName) {
		return getMongoCollection(CONSOLAS).find(Filters.eq("nombre", platformName)).first();
	}

	// M�todo que pide por teclado un juego que no exista:
	public static String askForNonExistingGameName() {
		String nombreJuego = "";
		// Hasta que no haya un Input v�lido, seguimos pidiendo el nombre
		while (true) {
			nombreJuego = Main.sc.nextLine();
			if (checkIfGameExists(nombreJuego)) {
				System.out.println("�Ya existe un juego con este nombre en la colecci�n!");
				System.out.println("Introduce otro nombre:");
			} else {
				return nombreJuego;
			}
		}
	}

	// M�todo que comprueba si un juego existe
	public static boolean checkIfGameExists(String gameName) {
		return getJuegoByName(gameName) != null;
	}

	// M�todo que retorna un documento de la colecci�n de juegos busc�ndolo por nombre:
	public static Document getJuegoByName(String gameName) {
		return getMongoCollection(JUEGOS).find(Filters.eq("nombre", gameName)).first();
	}

	// M�todo que pide al usuario introducir una consola existente:
	public static String askForExistingPlatformName() {
		String nombreConsola;
		// Hasta que no se introduzca una consola existente, pedimos input al usuario
		while (true) {
			printConsolas();
			System.out.println("Introduce el nombre de la consola: ");
			nombreConsola = Main.sc.nextLine();
			if (checkIfPlatformExists(nombreConsola)) {
				return nombreConsola;
			} else {
				System.out.println("�No existe la consola! Prueba otra vez:");
			}
		}
	}

	// M�todo que pide al usuario introducir un juego existente:
	public static String askForExistingGameName() {
		String nombreJuego;
		// Hasta que no se introduzca un juego existente, pedimos input al usuario
		while (true) {
			printJuegos();
			System.out.println("Introduce el nombre del juego: ");
			nombreJuego = Main.sc.nextLine();
			if (checkIfGameExists(nombreJuego)) {
				return nombreJuego;
			} else {
				System.out.println("�No existe el juego! Prueba otra vez:");
			}
		}
	}

	// M�todo que elimina un elemento de una colecci�n por el nombre
	public static void removeItem(int collection, String name) {
		getMongoCollection(collection).deleteOne(Filters.eq("nombre", name));
	}

	// M�todo que busca un objeto por el campo que se le pase y lo imprime
	public static <T> void findObject(int collection, String field, T value) {
		FindIterable<Document> iterable = getMongoCollection(collection).find(Filters.eq(field, value));
		boolean elementFind = false;
		for(Document d : iterable) {
			elementFind = true;
			printDocument(collection, d);
		}
		if(!elementFind) {
			System.out.println("No hay resultados...");
		}
	}
	
	// M�todo que imprime un documento
	public static void printDocument(int collection, Document d) {
		System.out.println("\n-------------------");
		System.out.println("Nombre: " + d.getString("nombre"));
		System.out.println("Precio: " + d.getDouble("precio"));
		if(collection == JUEGOS) {
			System.out.println("Plataforma: " + ((Document) d.get("plataforma")).getString("nombre"));
		}
		System.out.println("-------------------");
	}
	
	// M�todo que retorna un fitro dependiendo del operador que se le pase:
	public static Bson returnFilter(int operador, String field, Object value) {
		switch(operador) {
		// En caso de igual, retornamos un Bson de Filter equal
		case IGUAL:
			return Filters.eq(field, value);
		// En caso de diferente, retornamos un Bson de Filter NotEqual
		case DIFERENTE:
			return Filters.ne(field, value);
		// En caso de mayor, retornamos un Bson de Filter Greater Than
		case MAYOR:
			return Filters.gt(field, value);
		// En caso de mayor o igual, retornamos un Bson de Filter Greater Than or Equal
		case MAYORIGUAL:
			return Filters.gte(field, value);
		// En caso de menor, retornamos un Bson de Filter Less Than
		case MENOR:
			return Filters.lt(field, value);
		// En caso de menor o igual, retornamos un Bson de Filter Less Than or Equal
		case MENORIGUAL:
			return Filters.lte(field, value);
		}
		// En cualquier otro caso, retornamos nulo
		return null;
	}
	
	// M�todo que aplica todos los filtros que se le pasen e imprime los resultados
	public static void applyFiltersAndPrint(int collection, ArrayList<Bson> filters) {
		Bson allFilters = Filters.and(filters);
		FindIterable<Document> docsToFilter = getMongoCollection(collection).find(allFilters);
		
		for(Document d : docsToFilter) {
			printDocument(collection, d);
		}
	}

	// M�todo que cierra el cliente de mongo
	public static void closeClient() {
		mongoClient.close();
	}

}
