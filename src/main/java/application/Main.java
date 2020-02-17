package application;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import mongoManager.ConnectMongo;
import mongoManager.DBManager;

public class Main {
	
	public static Scanner sc = new Scanner(System.in);

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
	
	public static boolean chosenOption(int option) {
		switch(option) {
		case 1:
			DBManager.insertarConsola();
			break;
		case 2:
			DBManager.insertarJuego();
			break;
		case 3:
			DBManager.updatePlatform();
			break;
		case 4:
			DBManager.updateGame();
			break;
		case 5:
			DBManager.deleteItem(ConnectMongo.CONSOLAS);
			break;
		case 6:
			DBManager.deleteItem(ConnectMongo.JUEGOS);
			break;
		case 7:
			DBManager.simpleSearch(); 
			break;
		case 8:
			
			break;
		case 9:
			System.out.println("Hasta pronto!");
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		// Deshabilitamos el log de conexión de mongo para tener limpia la consola:
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);

		// Inicializamos las conexiones de mongo:
		ConnectMongo.initialize();
		// Imprimimos el menu hasta que el usuario decida salir
		do {
			System.out.println("Introduce una opción: ");
		}while(!menu());
	}
	
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