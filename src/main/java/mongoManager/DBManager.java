package mongoManager;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;

import application.Main;

public class DBManager {

	// M�todo que pide al usuario insertar una consola.
	public static void insertarConsola() {
		// Pedimos datos:
		System.out.println("Introduce el nombre de la consola: ");
		String nombreConsola = ConnectMongo.askForNonExistingPlatformName();
		System.out.println("Introduce el precio de la consola: ");
		double precioConsola = Main.checkDoubleInput();
		// Los a�adimos a un documento:
		Document document = new Document();
		document.put("nombre", nombreConsola);
		document.put("precio", precioConsola);
		// Insertamos el documento:
		ConnectMongo.getMongoCollection(ConnectMongo.CONSOLAS).insertOne(document);
		System.out.println("\n\t�Consola insertada con �xito!\n");
	}

	public static void insertarJuego() {
		// Si no existen consolas no se puede crear un juego:
		if (!ConnectMongo.collectionHasDocuments(ConnectMongo.CONSOLAS)) {
			System.out.println("Antes de crear un juego debes crear una plataforma (consola)!");
			return;
		}
		String nombrePlataforma = null;
		// Pedimos datos:
		System.out.println("Introduce el nombre del juego: ");
		String nombreJuego = Main.sc.nextLine();
		System.out.println("Introduce el precio del juego: ");
		double precioJuego = Main.checkDoubleInput();
		// Solicitamos la plataforma:
		nombrePlataforma = ConnectMongo.askForExistingPlatformName();

		// Obtenemos la plataforma:
		Document plataforma = ConnectMongo.getConsolaByName(nombrePlataforma);
		// Creamos el documento
		Document document = new Document();
		// Le a�adimos los datos:
		document.put("nombre", nombreJuego);
		document.put("precio", precioJuego);
		document.put("plataforma", plataforma);
		// Insertamos el documento (juego)
		ConnectMongo.getMongoCollection(ConnectMongo.JUEGOS).insertOne(document);
		System.out.println("\n\t�Juego insertado con �xito!\n");
	}

	// M�todo que pide los datos para actualizar una consola:
	public static void updatePlatform() {
		// Pedimos el nombre de la consola a modificar:
		System.out.println("Introduce la consola a modificar: ");
		String nombreConsola = ConnectMongo.askForExistingPlatformName();
		// Pedimos el campo a editar:
		System.out.println("Elige el campo a editar: ");
		System.out.println("\t1 - Nombre.");
		System.out.println("\t2 - Precio.");
		System.out.println("\t3 - Atr�s.");
		int option = Main.checkIntInput();
		switch (option) {
		case 1:
			// Cambio nombre
			System.out.println("Introduce el nuevo nombre:");
			String nuevoNombre = ConnectMongo.askForNonExistingPlatformName();
			// Actualizamos:
			ConnectMongo.updateItemName(ConnectMongo.CONSOLAS, nombreConsola, nuevoNombre);
			break;
		case 2:
			// Cambio precio
			System.out.println("Introduce el nuevo precio:");
			double nuevoPrecio = Main.checkDoubleInput();
			// Actualizamos
			ConnectMongo.updateItemPrice(ConnectMongo.CONSOLAS, nombreConsola, nuevoPrecio);
			break;
		case 3:
			// Volvemos al men�, no realizamos nada
			System.out.println("Volviendo atr�s...");
			break;
		default:
			// Opci�n incorrecta, volvemos al men� sin realizar nada:
			System.out.println("Opci�n incorrecta, volviendo atr�s...");
			break;
		}
	}

	// M�todo que pide los datos para actualizar un juego:
	public static void updateGame() {
		// Pedimos el nombre del juego a actualizar:
		System.out.println("Introduce el juego a modificar: ");
		String nombreJuego = ConnectMongo.askForExistingGameName();
		// Pedimos el campo a editar:
		System.out.println("Elige el campo a editar: ");
		System.out.println("\t1 - Nombre.");
		System.out.println("\t2 - Precio.");
		System.out.println("\t3 - Plataforma");
		System.out.println("\t4 - Atr�s.");
		int option = Main.checkIntInput();
		switch (option) {
		case 1:
			// Pedimos nuevo nombre:
			System.out.println("Introduce el nuevo nombre:");
			String nuevoNombre = ConnectMongo.askForNonExistingGameName();
			// Actualizamos nombre
			ConnectMongo.updateItemName(ConnectMongo.JUEGOS, nombreJuego, nuevoNombre);
			break;
		case 2:
			// Pedimos nuevo precio
			System.out.println("Introduce el nuevo precio:");
			double nuevoPrecio = Main.checkDoubleInput();
			// Actualizamos precio
			ConnectMongo.updateItemPrice(ConnectMongo.JUEGOS, nombreJuego, nuevoPrecio);
			break;
		case 3:
			// Pedimos nueva plataforma (ya existente)
			System.out.println("Introduce la plataforma: ");
			String nuevaPlataformaNombre = ConnectMongo.askForExistingPlatformName();
			Document nuevaPlataformaObject = ConnectMongo.getConsolaByName(nuevaPlataformaNombre);
			// Actualizamos plataforma:
			ConnectMongo.updateItemPlatform(ConnectMongo.JUEGOS, nombreJuego, nuevaPlataformaObject);
			break;
		case 4:
			// Volvemos atr�s sin hacer nada
			System.out.println("Volviendo atr�s...");
			break;
		default:
			// Opci�n incorrecta, volvemos al men�:
			System.out.println("Opci�n incorrecta, volviendo atr�s...");
			break;
		}
	}

	// M�todo que pide los datos para eliminar una consola o juego:
	public static void deleteItem(int collection) {
		String nombre = null;
		switch (collection) {
		case ConnectMongo.CONSOLAS:
			// Caso de consola, pedimos nombre para eliminarla:
			System.out.println("Introduce la consola a eliminar (Nombre): ");
			nombre = ConnectMongo.askForExistingPlatformName();
			break;
		case ConnectMongo.JUEGOS:
			// Caso de juego, pedimos nombre para eliminarlo
			System.out.println("Introduce el juego a eliminar (Nombre): ");
			nombre = ConnectMongo.askForExistingGameName();
			break;
		}
		// Eliminamos el objeto:
		ConnectMongo.removeItem(collection, nombre);
	}

	// M�todo que pide los datos para la b�squeda sencilla
	public static void simpleSearch() {
		// Pedimos opcion:
		System.out.println("Introduce una opcion: ");
		System.out.println("\t1 - Buscar una consola");
		System.out.println("\t2 - Buscar un juego");
		int option = Main.checkIntInput();
		switch (option) {
		case 1:
			// En el caso de consola, guardamos identificador consolas en option
			option = ConnectMongo.CONSOLAS;
			break;
		case 2:
			// En el caso de juego, guardamos identificador juegos en option
			option = ConnectMongo.JUEGOS;
			break;
		default:
			// Opci�n incorrecta, volvemos al men�:
			System.out.println("La opci�n no est� en la lista, volviendo al men�...");
			return;
		}
		// Pedimos el campo por el que buscar:
		String field = askForField();
		switch (field) {
		case "nombre":
			// En el caso denombre, pedimos un String
			String stringValue = askForStringValue();
			// Buscamos por colecci�n, campo y valor String:
			ConnectMongo.findObject(option, field, stringValue);
			break;
		case "precio":
			// En el caso de precio, pedimos un Double:
			double doubleValue = askForDoubleValue();
			// Buscamos por colecci�n, campo y valor double:
			ConnectMongo.findObject(option, field, doubleValue);
			break;
		case "plataforma":
			// En el caso de plataforma
			if (option == ConnectMongo.CONSOLAS) {
				// Si la opcion es consolas, es un error, no contiene ese campo, volvemos al men�:
				System.out.println("�No se puede buscar por plataforma en las plataformas! Volviendo al men�...");
				return;
			} else {
				// Si la opci�n es juegos, pedimos la plataforma
				System.out.println("Introduce la plataforma a buscar: ");
				Document documentValue = askForPlatformObject();
				// Buscamos por colecci�n, campo y valor Document
				ConnectMongo.findObject(option, field, documentValue);
			}
			break;
		default:
			// En caso que se introduzca otro campo, no existe, volvemos al men�:
			System.out.println("�No existe ese campo en los objetos! Volviendo al men�...");
		}
	}
	
	// M�todo que recoge una lista de filtros pudiendo elegir entre operadores para aplicarlos todos
	public static void advancedSearch() {
		boolean exit = false;
		// Pedimos qu� buscar
		System.out.println("Introduce una opcion: ");
		System.out.println("\t1 - Buscar una consola");
		System.out.println("\t2 - Buscar un juego");
		int option = Main.checkIntInput();
		// Guardamos el identificador de la colecci�n seg�n la option
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
		// Creamos objetos necesarios:
		Bson filter;
		ArrayList<Bson> filters = new ArrayList<Bson>();
		Object value = null;
		String field;
		int operador;
		String moreFilters;
		// Mientras no se solicite salir, pedimos filtros:
		do {
			// Pedimos campo a buscar:
			field = askForField();
			switch (field) {
			case "nombre":
				// Si es nombre, pedimos String
				value = askForStringValue();
				break;
			case "precio":
				// Si es precio, pedimos Double:
				value = askForDoubleValue();
				break;
			case "plataforma":
				System.out.println("Introduce la plataforma a buscar: ");
				// Si es plataforma, pedimos la plataforma, y recogemos un Document
				value = askForPlatformObject();
				break;
			}
			// Pedimos por el operador:
			operador = askForAction(value);
			// Recogemos el filtro:
			filter = ConnectMongo.returnFilter(operador, field, value);
			// Lo a�adimos a la lista de filtros:
			filters.add(filter);
			// Preguntamos si se desea aplicar m�s filtros:
			System.out.println("Aplicar m�s filtros? Escribe 'S' en caso afirmativo, cualquier otro caracter en caso negativo:");
			moreFilters = Main.sc.nextLine();
			if(!moreFilters.equalsIgnoreCase("S")) {
				exit = true;
			}
		}while(!exit);
		// Aplicamos filtros e imprimimos:
		ConnectMongo.applyFiltersAndPrint(option, filters);
	}

	// M�todo que pide un campo:
	public static String askForField() {
		System.out.println("Introduce el campo a buscar: ");
		return Main.sc.nextLine();
	}

	// M�todo que pide un valor double:
	public static double askForDoubleValue() {
		System.out.println("Introduce el valor: ");
		return Main.checkDoubleInput();
	}

	// M�todo que pide un valor String:
	public static String askForStringValue() {
		System.out.println("Introduce el valor: ");
		return Main.sc.nextLine();
	}

	// M�todo que pide el operador deseado para el filtro
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
		// Seg�n la opci�n, retornamos el identificador de cada opci�n:
		switch (option) {
		case 1:
			return ConnectMongo.IGUAL;
		case 2:
			return ConnectMongo.DIFERENTE;
		}
		/* En caso de que el valor sea double, se le pueden aplicar m�s filtros: 
		 *		- Mayor que
		 *		- Mayor o igual que
		 *		- Menor que
		 *		- Menor o igual que 
		 */
		
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
		// Si llegamos aqu�, no se ha introducido opci�n correcta, retornamos 0:
		return 0;
	}

	// M�todo que pide y retorna una plataforma:
	public static Document askForPlatformObject() {
		return ConnectMongo.getConsolaByName(ConnectMongo.askForExistingPlatformName());
	}

}
