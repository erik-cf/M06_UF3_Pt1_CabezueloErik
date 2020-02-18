package application;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import mongoManager.ConnectMongo;
import mongoManager.DBManager;

public class Main {
	
	// Scanner p�blic y est�tico que compartir� todo el programa
	public static Scanner sc = new Scanner(System.in);

	// M�todo que imprime un men�:
	public static boolean menu() {
		System.out.println("1 - Insertar nueva consola");
		System.out.println("2 - Insertar nuevo juego");
		System.out.println("3 - Modificar consola");
		System.out.println("4 - Modificar juego");
		System.out.println("5 - Eliminar consola");
		System.out.println("6 - Eliminar juego");
		System.out.println("7 - Realizar busqueda simple (1 campo)");
		System.out.println("8 - Realizar busqueda compleja");
		System.out.println("9 - Salir");
		return chosenOption(checkIntInput());
	}
	
	// M�todo que realiza acciones seg�n la opci�n del men�:
	public static boolean chosenOption(int option) {
		switch(option) {
		case 1:
			// Insertar consola
			DBManager.insertarConsola();
			break;
		case 2:
			// Insertar juego
			DBManager.insertarJuego();
			break;
		case 3:
			// Actualizar consola
			DBManager.updatePlatform();
			break;
		case 4:
			// Actualizar juego
			DBManager.updateGame();
			break;
		case 5:
			// Eliminar consola
			DBManager.deleteItem(ConnectMongo.CONSOLAS);
			break;
		case 6:
			// Eliminar juego
			DBManager.deleteItem(ConnectMongo.JUEGOS);
			break;
		case 7:
			// B�squeda simple:
			DBManager.simpleSearch(); 
			break;
		case 8:
			// B�squeda avanzada (m�s de un filtro y operadores)
			DBManager.advancedSearch();
			break;
		case 9:
			// Salir
			System.out.println("Hasta pronto!");
			return true;
		}
		// Sigue el programa si no es la opci�n 9 (Salir)
		return false;
	}
	
	public static void main(String[] args) {
		// Deshabilitamos el log de conexi�n de mongo para tener limpia la consola:
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);

		// Inicializamos las conexiones de mongo:
		ConnectMongo.initialize();
		// Imprimimos el menu hasta que el usuario decida salir
		do {
			System.out.println("Introduce una opci�n: ");
		}while(!menu());
		// Cerramos la conexi�n
		ConnectMongo.closeClient();
		
		// Cerramos el Scanner:
		sc.close();
	}
	
	// M�todo que valida los inputs de un n�mero entero:
	public static int checkIntInput() {
		int num;
		while(true) {
			try {
				num = sc.nextInt();
				sc.nextLine();
				return num;
			}catch(InputMismatchException ime) {
				System.out.println("Introduce un numero entero!");
				sc.nextLine();
			}
		}
	}
	
	// M�todo que valida los inputs de un n�mero double:
	public static double checkDoubleInput() {
		double num;
		while(true) {
			try {
				num = sc.nextDouble();
				sc.nextLine();
				return num;
			}catch(InputMismatchException ime) {
				System.out.println("Introduce un numero!");
				sc.nextLine();
			}
		}
	}
	
}