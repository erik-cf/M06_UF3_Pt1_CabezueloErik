package mongoManager;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;

import application.Main;

public class DBManager {

	public static void insertarConsola() {
		System.out.println("Introduce el nombre de la consola: ");
		String nombreConsola = ConnectMongo.askForNonExistingPlatformName();
		System.out.println("Introduce el precio de la consola: ");
		double precioConsola = Main.checkDoubleInput();
		Document document = new Document();
		document.put("nombre", nombreConsola);
		document.put("precio", precioConsola);
		ConnectMongo.getMongoCollection(ConnectMongo.CONSOLAS).insertOne(document);
		System.out.println("\n\t�Consola insertada con �xito!\n");
	}

	public static void insertarJuego() {
		if (!ConnectMongo.collectionHasDocuments(ConnectMongo.CONSOLAS)) {
			System.out.println("Antes de crear un juego debes crear una consola!");
			return;
		}
		String nombrePlataforma = null;
		System.out.println("Introduce el nombre del juego: ");
		String nombreJuego = Main.sc.nextLine();
		System.out.println("Introduce el precio del juego: ");
		double precioJuego = Main.checkDoubleInput();
		nombrePlataforma = ConnectMongo.askForExistingPlatformName();

		Document plataforma = ConnectMongo.getConsolaByName(nombrePlataforma);
		Document document = new Document();
		document.put("nombre", nombreJuego);
		document.put("precio", precioJuego);
		document.put("plataforma", plataforma);
		ConnectMongo.getMongoCollection(ConnectMongo.JUEGOS).insertOne(document);
		System.out.println("\n\t�Juego insertado con �xito!\n");
	}

	public static void updatePlatform() {
		System.out.println("Introduce la consola a modificar: ");
		String nombreConsola = ConnectMongo.askForExistingPlatformName();
		System.out.println("Elige el campo a editar: ");
		System.out.println("\t1 - Nombre.");
		System.out.println("\t2 - Precio.");
		System.out.println("\t3 - Atr�s.");
		int option = Main.checkIntInput();
		switch (option) {
		case 1:
			System.out.println("Introduce el nuevo nombre:");
			String nuevoNombre = ConnectMongo.askForNonExistingPlatformName();
			ConnectMongo.updateItemName(ConnectMongo.CONSOLAS, nombreConsola, nuevoNombre);
			break;
		case 2:
			System.out.println("Introduce el nuevo precio:");
			double nuevoPrecio = Main.checkDoubleInput();
			ConnectMongo.updateItemPrice(ConnectMongo.CONSOLAS, nombreConsola, nuevoPrecio);
			break;
		case 3:
			System.out.println("Volviendo atr�s...");
			break;
		default:
			System.out.println("Opci�n incorrecta, volviendo atr�s...");
			break;
		}
	}

	public static void updateGame() {
		System.out.println("Introduce el juego a modificar: ");
		String nombreJuego = ConnectMongo.askForExistingGameName();
		System.out.println("Elige el campo a editar: ");
		System.out.println("\t1 - Nombre.");
		System.out.println("\t2 - Precio.");
		System.out.println("\t3 - Plataforma");
		System.out.println("\t4 - Atr�s.");
		int option = Main.checkIntInput();
		switch (option) {
		case 1:
			System.out.println("Introduce el nuevo nombre:");
			String nuevoNombre = ConnectMongo.askForNonExistingGameName();
			ConnectMongo.updateItemName(ConnectMongo.JUEGOS, nombreJuego, nuevoNombre);
			break;
		case 2:
			System.out.println("Introduce el nuevo precio:");
			double nuevoPrecio = Main.checkDoubleInput();
			ConnectMongo.updateItemPrice(ConnectMongo.JUEGOS, nombreJuego, nuevoPrecio);
			break;
		case 3:
			System.out.println("Introduce la plataforma: ");
			String nuevaPlataformaNombre = ConnectMongo.askForExistingPlatformName();
			Document nuevaPlataformaObject = ConnectMongo.getConsolaByName(nuevaPlataformaNombre);
			ConnectMongo.updateItemPlatform(ConnectMongo.JUEGOS, nombreJuego, nuevaPlataformaObject);
			break;
		case 4:
			System.out.println("Volviendo atr�s...");
			break;
		default:
			System.out.println("Opci�n incorrecta, volviendo atr�s...");
			break;
		}
	}

	public static void deleteItem(int collection) {
		String nombre = null;
		switch (collection) {
		case ConnectMongo.CONSOLAS:
			System.out.println("Introduce la consola a eliminar (Nombre): ");
			nombre = ConnectMongo.askForExistingPlatformName();
			break;
		case ConnectMongo.JUEGOS:
			System.out.println("Introduce el juego a eliminar (Nombre): ");
			nombre = ConnectMongo.askForExistingGameName();
			break;
		}
		ConnectMongo.removeItem(collection, nombre);
	}

	public static void simpleSearch() {
		System.out.println("Introduce una opcion: ");
		System.out.println("\t1 - Buscar una consola");
		System.out.println("\t2 - Buscar un juego");
		int option = Main.checkIntInput();
		switch (option) {
		case 1:
			option = ConnectMongo.CONSOLAS;
			break;
		case 2:
			option = ConnectMongo.JUEGOS;
			break;
		default:
			System.out.println("La opci�n no est� en la lista, volviendo al men�...");
			return;
		}
		String field = askForField();
		switch (field) {
		case "nombre":
			String stringValue = askForStringValue();
			ConnectMongo.findObject(option, field, stringValue);
			break;
		case "precio":
			double doubleValue = askForDoubleValue();
			ConnectMongo.findObject(option, field, doubleValue);
			break;
		case "plataforma":
			if (option == ConnectMongo.CONSOLAS) {
				System.out.println("�No se puede buscar por plataforma en las plataformas! Volviendo al men�...");
				return;
			} else {
				System.out.println("Introduce la plataforma a buscar: ");
				Document documentValue = askForPlatformObject();
				ConnectMongo.findObject(option, field, documentValue);
			}
			break;
		default:
			System.out.println("�No existe ese campo en los objetos! Volviendo al men�...");
		}
	}
	
	public static void advancedSearch() {
		boolean exit = false;
		System.out.println("Introduce una opcion: ");
		System.out.println("\t1 - Buscar una consola");
		System.out.println("\t2 - Buscar un juego");
		int option = Main.checkIntInput();
		switch (option) {
		case 1:
			option = ConnectMongo.CONSOLAS;
			break;
		case 2:
			option = ConnectMongo.JUEGOS;
			break;
		default:
			System.out.println("La opci�n no est� en la lista, volviendo al men�...");
			return;
		}
		Bson filter;
		ArrayList<Bson> filters = new ArrayList<Bson>();
		Object value = null;
		String field;
		int operador;
		String moreFilters;
		do {
			field = askForField();
			switch (field) {
			case "nombre":
				value = askForStringValue();
				break;
			case "precio":
				value = askForDoubleValue();
				break;
			case "plataforma":
				System.out.println("Introduce la plataforma a buscar: ");
				value = askForPlatformObject();
				break;
			}
			operador = askForAction(value);
			filter = ConnectMongo.returnFilter(operador, field, value);
			filters.add(filter);
			System.out.println("Aplicar m�s filtros? Escribe 'S' en caso afirmativo, cualquier otro caracter en caso negativo:");
			moreFilters = Main.sc.nextLine();
			if(!moreFilters.equalsIgnoreCase("S")) {
				exit = true;
			}
		}while(!exit);
		
		ConnectMongo.applyFiltersAndPrint(option, filters);
	}

	public static String askForField() {
		System.out.println("Introduce el campo a buscar: ");
		return Main.sc.nextLine();
	}

	public static double askForDoubleValue() {
		System.out.println("Introduce el valor: ");
		return Main.checkDoubleInput();
	}

	public static String askForStringValue() {
		System.out.println("Introduce el valor: ");
		return Main.sc.nextLine();
	}

	public static <T> int askForAction(T t) {
		System.out.println("Selecciona operador: ");
		System.out.println("1 - Igual (=)");
		System.out.println("2 - Diferente (!=)");
		if (t instanceof Double) {
			System.out.println("3 - Mayor que (>)");
			System.out.println("4 - Mayor o igual que (>=)");
			System.out.println("5 - Menos que (<)");
			System.out.println("6 - Menor o igual que (<=)");
		}
		int option = Main.checkIntInput();
		switch (option) {
		case 1:
			return ConnectMongo.IGUAL;
		case 2:
			return ConnectMongo.DIFERENTE;
		}
		if (t instanceof Double) {
			switch (option) {
			case 3:
				return ConnectMongo.MAYOR;
			case 4:
				return ConnectMongo.MAYORIGUAL;
			case 5:
				return ConnectMongo.MENOR;
			case 6:
				return ConnectMongo.MENORIGUAL;
			}
		}
		System.out.println("La opci�n no est� en la lista, volviendo al men�...");
		return 0;
	}

	public static Document askForPlatformObject() {
		return ConnectMongo.getConsolaByName(ConnectMongo.askForExistingPlatformName());
	}

}
