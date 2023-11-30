package cs.unicam.ids.synk.view;

import java.util.Scanner;

public class App {
	
	public static void main(String[] args) {
		Municipath app = new Municipath();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input, output;
		while(true) {
			input = scanner.nextLine();
			if(help(input))
				output = getHelp();
			else output = app.apply(input);
			System.out.println("\n" + output + "\n");
		}
		
	}

	private static String getHelp() {
		return "Benvenuto in questo prototipo di backend.\n" +
				"Questa è la lista di tutti i comandi eseguibili.\n" +
				"------------------------------------------------\n" +
				"help: visualizzi questo messaggio.\n" +
				"back: torni indietro.\n" +
				"login [username] [password]: accedi al tuo account.\n" +
				"logout: esci dal tuo account.\n" +
				"signin [username] [password]: crei un nuovo account.\n" +
				"create post [titolo] [lat] [lon] [text]: crei un nuovo contenuto da vedere.\n" +
				"create city [nome] [CAP] [lat] [lon] [curatore]: crei un nuovo comune da visitare.\n" +
				"Inoltre, in base alla tua posizione all'interno di Municipath," +
				" avrai a disposizione varie opzioni di navigazione.\n" +
				"Buon divertimento!.";
	}

	private static boolean help(String input) {
		String s = input.replace("-", "").toLowerCase();
		return s.equals("?") || s.equals("h") || s.equals("help");
	}
	
}
