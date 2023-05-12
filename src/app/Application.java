package app;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.entities.ElementoCatalogo;
import app.entities.Genere;
import app.entities.Libro;
import app.entities.Periodicita;
import app.entities.Rivista;

public class Application {

	private static Logger logger = LoggerFactory.getLogger(app.Application.class);
	private final static String LIBRO = "B";

	private static File file = new File("catalogo_biblioteca.txt");

	public static void main(String[] args) {

		Map<String, ElementoCatalogo> allElements = new HashMap();

		Scanner scanner = new Scanner(System.in);

		int input;
		boolean runApplication = true;

		if (file.exists()) {
			logger.info("E' GIA' PRESENTE IL FILE CATALOGO.");
			logger.info("ELEMENTI PRESENTI:");
			leggiFile(allElements, file);
		}

		while (runApplication) {
			System.out.println("****CATALOGO BIBLIOTECA****");
			System.out.println("Quale operazione intendi eseguire?");
			System.out.println("1 - visualizza elementi nel file catalogo;");
			System.out.println("2 - per aggiungere un nuovo elemento nel catalogo;");
			System.out.println("3 - rimuovi un elemento dal catalogo;");
			System.out.println("4 - ricerca un elemento dal catalogo con codice;");
			System.out.println("5 - ricerca elementi dal catalogo per anno;");
			System.out.println("6 - ricerca elementi dal catalogo per autore;");
			System.out.println("7 - cancellare il file catalogo;");
			System.out.println("0 - per terminare");

			input = scanner.nextInt();

			switch (input) {
			case 1:
				if (!allElements.isEmpty()) {
					stampaCatalogoCompleto(allElements);
				} else {
					logger.info("Il catalogo è VUOTO! Aggiungi elementi al catalogo prima di generare il file!");
				}
				break;
			case 2:
				inserisciNuovoElemento(allElements, scanner);
				break;
			case 3:
				inputElementoRemove(allElements, scanner);
				break;
			case 4:
				inputElementoSearchById(allElements, scanner);
				break;
			case 5:
				inputElementoSearchByYear(allElements, scanner);
				break;
			case 6:
				inputElementoSearchByAutor(allElements, scanner);
				break;
			case 7:
				eliminaFile(allElements, file);
				break;
			case 0:
				runApplication = false;
				logger.info("Termino l'applicazione.");
				break;
			default:
				logger.info("Inserisci un input valido!");
				break;
			}
		}

		scanner.close();

	}

	private static void inserisciNuovoElemento(Map<String, ElementoCatalogo> allElements, Scanner scanner) {

		scanner.nextLine();

		System.out.println("Vuoi inserire un nuovo libro o una rivista?");
		System.out.println("B - per inserire un nuovo libro;");
		System.out.println("R - per inserire una nuova rivista;");

		String tipoElemento = scanner.nextLine();

		switch (tipoElemento) {
		case "B":
			aggiungiNuovoLibro(allElements, scanner);
			logger.info("ELEMENTO AGGIUNTO AL CATALOGO!");
			break;
		case "R":
			aggiungiNuovaRivista(allElements, scanner);
			logger.info("ELEMENTO AGGIUNTO AL CATALOGO!");
			break;
		default:
			logger.info("Inserisci un input valido!");
			break;
		}

	}

	private static void leggiFile(Map<String, ElementoCatalogo> allElementsReadFile, File file) {
		try {
			List<String> contents = FileUtils.readLines(file, "UTF-8");
			for (String line : contents) {
				if (line.startsWith(LIBRO)) {

					String[] elementoCatalogo = line.split("@|#");
					String codice = elementoCatalogo[0];
					String titolo = elementoCatalogo[1];
					Integer anno = Integer.valueOf(elementoCatalogo[2]);
					int numeroPagine = Integer.valueOf(elementoCatalogo[3]);
					String autore = elementoCatalogo[4];
					Genere genere = Genere.valueOf(elementoCatalogo[5]);

					aggiungiLibroAlCatalogo(allElementsReadFile, codice, titolo, anno, numeroPagine, autore, genere);

				} else {

					String[] elementoCatalogo = line.split("@|#");
					String codice = elementoCatalogo[0];
					String titolo = elementoCatalogo[1];
					Integer anno = Integer.valueOf(elementoCatalogo[2]);
					int numeroPagine = Integer.valueOf(elementoCatalogo[3]);
					Periodicita periodicita = Periodicita.valueOf(elementoCatalogo[4]);

					aggiungiRivistaAlCatalogo(allElementsReadFile, codice, titolo, anno, numeroPagine, periodicita);
				}
			}
		} catch (IOException e) { //
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error("FILE NON TROVATO!");
		}
	}

	private static void stampaCatalogoCompleto(Map<String, ElementoCatalogo> allElements) {
		logger.info("CATALOGO COMPLETO:");
		for (ElementoCatalogo e : allElements.values()) {
			logger.info(e.toString());
		}
	}

	private static void aggiungiNuovoLibro(Map<String, ElementoCatalogo> allElements, Scanner scanner) {

		String codiceLibro;
		String titoloLibro;
		Integer annoPubblicazione;
		int numeroPagine;
		String autore;
		String g;

		do {
			System.out.println("Inserisci il codice ISBN:");
			codiceLibro = scanner.nextLine();
			if (codiceLibro.equals("")) {
				logger.error("DATO OBBLIGATORIO!");
				logger.error("Inserisci il codice UNIVOCO del libro!");
			}
		} while (codiceLibro.equals(""));

		do {
			System.out.println("Inserisci il titolo del libro:");
			titoloLibro = scanner.nextLine();
			if (titoloLibro.equals("")) {
				logger.error("DATO OBBLIGATORIO!");
				logger.error("Inserisci il TITOLO del libro!");
			}
		} while (titoloLibro.equals(""));

		do {
			System.out.println("Inserisci l'anno di pubblicazione del libro:");
			annoPubblicazione = scanner.nextInt();
			if (annoPubblicazione == 0 || annoPubblicazione < 0) {
				logger.error("Non puoi inserire VALORI NEGATIVI!");
			}
		} while (annoPubblicazione == 0 || annoPubblicazione < 0);

		do {
			System.out.println("Inserisci il numero di pagine del libro:");
			numeroPagine = scanner.nextInt();
			if (numeroPagine == 0 || numeroPagine < 0) {
				logger.error("Non puoi inserire VALORI NEGATIVI!");
			}
		} while (numeroPagine == 0 || numeroPagine < 0);

		scanner.nextLine();

		do {
			System.out.println("Inserisci l'autore del libro:");
			autore = scanner.nextLine();
			if (autore.equals("")) {
				logger.error("DATO OBBLIGATORIO!");
				logger.error("Inserisci il AUTORE del libro!");
			}
		} while (autore.equals(""));

		do {
			System.out.println("Inserisci il genere del libro:");
			g = scanner.nextLine();
			if (g.equals("")) {
				logger.error("DATO OBBLIGATORIO!");
				logger.error("Inserisci il GENERE del libro!");
			}

		} while (g.equals(""));

		// DEVO INSERIRE NECESSARIAMENTE VALORI PRESENTI NELL'ENUM GENERE!
		Genere genere = Genere.valueOf(g);

		aggiungiLibroAlCatalogo(allElements, codiceLibro, titoloLibro, annoPubblicazione, numeroPagine, autore, genere);
		scriviFileCatalogo(file, allElements);
	}

	private static void aggiungiNuovaRivista(Map<String, ElementoCatalogo> allElements, Scanner scanner) {

		String codiceRivista;
		String titoloRivista;
		Integer annoPubblicazione;
		int numeroPagine;
		String p;

		do {
			System.out.println("Inserisci il codice ISBN:");
			codiceRivista = scanner.nextLine();
			if (codiceRivista.equals("")) {
				logger.error("DATO OBBLIGATORIO!");
				logger.error("Inserisci il codice UNIVOCO della rivista!");
			}
		} while (codiceRivista.equals(""));

		do {
			System.out.println("Inserisci il titolo della rivista:");
			titoloRivista = scanner.nextLine();
			if (titoloRivista.equals("")) {
				logger.error("DATO OBBLIGATORIO!");
				logger.error("Inserisci il TITOLO della rivista!");
			}
		} while (titoloRivista.equals(""));

		do {
			System.out.println("Inserisci l'anno di pubblicazione della rivista:");
			annoPubblicazione = scanner.nextInt();
			if (annoPubblicazione == 0 || annoPubblicazione < 0) {
				logger.error("Non puoi inserire VALORI NEGATIVI!");
			}
		} while (annoPubblicazione == 0 || annoPubblicazione < 0);

		do {
			System.out.println("Inserisci il numero di pagine della rivista:");
			numeroPagine = scanner.nextInt();
			if (numeroPagine == 0 || numeroPagine < 0) {
				logger.error("Non puoi inserire VALORI NEGATIVI!");
			}
		} while (numeroPagine == 0 || numeroPagine < 0);

		scanner.nextLine();

		do {
			System.out.println("Inserisci la periodicità della rivista:");
			p = scanner.nextLine();
			if (p.equals("")) {
				logger.error("DATO OBBLIGATORIO!");
				logger.error("Inserisci PERIODICITA'!");
			}

		} while (p.equals(""));

		// DEVO INSERIRE NECESSARIAMENTE VALORI PRESENTI NELL'ENUM PERIODICITA!
		Periodicita periodicita = Periodicita.valueOf(p);

		aggiungiRivistaAlCatalogo(allElements, codiceRivista, titoloRivista, annoPubblicazione, numeroPagine,
				periodicita);
		scriviFileCatalogo(file, allElements);

	}

	private static void scriviFileCatalogo(File file, Map<String, ElementoCatalogo> allElements) {
		// SE IL FILE ESISTE GIA', SVUOTO IL CONTENUTO PER RISCRIVERLO CON GLI ELEMENTI
		// AGGIUNTI SUCCESSIVAMENTE
		if (file.exists()) {
			try {
				FileUtils.writeStringToFile(file, "", "UTF-8", false);
			} catch (IOException e) {
				logger.error(e.getMessage());
				logger.error("Errore in sovrascrittura file catalogo.");
			}
		}

		for (ElementoCatalogo elemento : allElements.values()) {
			try {
				FileUtils.writeStringToFile(file, elemento.toString() + System.lineSeparator(), "UTF-8", true);
			} catch (IOException e) {
				// e.printStackTrace();
				logger.error(e.getMessage());
				logger.error("Errore in scrittura file catalogo.");
			}
		}
	}

	private static void eliminaFile(Map<String, ElementoCatalogo> allElements, File file) {
		allElements.clear();
		if (file.exists()) {
			file.delete();
			logger.info("File catalogo eliminato correttamente!");
		} else {
			logger.info("File non presente. Genera un nuovo file catalogo.");
		}
	}

	private static void aggiungiLibroAlCatalogo(Map<String, ElementoCatalogo> allElements, String codice, String titolo,
			Integer anno, int numeroPagine, String autore, Genere genere) {

		Libro l = new Libro(codice, titolo, anno, numeroPagine, autore, genere);
		allElements.put(codice, l);

		logger.info(l.toString());

	}

	private static void aggiungiRivistaAlCatalogo(Map<String, ElementoCatalogo> allElements, String codice,
			String titolo, Integer anno, int numeroPagine, Periodicita periodicita) {

		Rivista r = new Rivista(codice, titolo, anno, numeroPagine, periodicita);
		allElements.put(codice, r);

		logger.info(r.toString());

	}

	private static void inputElementoRemove(Map<String, ElementoCatalogo> allElements, Scanner scanner) {
		scanner.nextLine();

		String codiceRemove;

		do {
			System.out.println("Inserisci il codice ISBN dell'elemento da rimuovere:");
			codiceRemove = scanner.nextLine();
			if (codiceRemove.equals("")) {
				logger.error("DATO OBBLIGATORIO!");
				logger.error("Inserisci il codice UNIVOCO dell'elemento!");
			}
		} while (codiceRemove.equals(""));

		rimuoviElementoCatalogo(allElements, codiceRemove);
	}

	private static void rimuoviElementoCatalogo(Map<String, ElementoCatalogo> allElements, String codiceIsbn) {
		ElementoCatalogo e = allElements.get(codiceIsbn);
		if (e != null) {
			allElements.remove(codiceIsbn);
			logger.info("Elemento rimosso dal catalogo: " + e.getCodiceIsbn() + " " + e.getTitolo());
			scriviFileCatalogo(file, allElements);
		} else {
			logger.info("L'elemento che desideri rimuovere non è presente nel catalogo!");
		}
	}

	private static void inputElementoSearchById(Map<String, ElementoCatalogo> allElements, Scanner scanner) {

		scanner.nextLine();

		String codiceSearch;

		do {
			System.out.println("Inserisci il codice ISBN dell'elemento da cercare:");
			codiceSearch = scanner.nextLine();
			if (codiceSearch.equals("")) {
				logger.error("DATO OBBLIGATORIO!");
				logger.error("Inserisci il codice UNIVOCO dell'elemento!");
			}
		} while (codiceSearch.equals(""));

		ElementoCatalogo e = ricercaElementoConCodice(allElements, codiceSearch);
		stampaRisultatoRicercaConCodice(e);
	}

	private static ElementoCatalogo ricercaElementoConCodice(Map<String, ElementoCatalogo> allElements,
			String codiceIsbn) {
		ElementoCatalogo e = allElements.get(codiceIsbn);
		return e;
	}

	private static void stampaRisultatoRicercaConCodice(ElementoCatalogo elementoCercatoCodice) {
		logger.info("Elemento cercato con codice :");
		logger.info(elementoCercatoCodice.toString());
	}

	private static void inputElementoSearchByYear(Map<String, ElementoCatalogo> allElements, Scanner scanner) {

		scanner.nextLine();

		int annoSearch;
		do {
			System.out.println("Inserisci l'anno di pubblicazione della rivista:");
			annoSearch = scanner.nextInt();
			if (annoSearch == 0 || annoSearch < 0) {
				logger.error("Non puoi inserire VALORI NEGATIVI!");
			}
		} while (annoSearch == 0 || annoSearch < 0);

		List<ElementoCatalogo> listByYear = ricercaElementiAnno(allElements, annoSearch);
		stampaRisultatoRicercaConAnno(listByYear);

	}

	private static List<ElementoCatalogo> ricercaElementiAnno(Map<String, ElementoCatalogo> allElements,
			Integer annoPubblicazione) {
		return allElements.values().stream().filter(e -> annoPubblicazione.equals(e.getAnnoPubblicazione())).toList();
	}

	private static void stampaRisultatoRicercaConAnno(List<ElementoCatalogo> listaElementiCercatiAnno) {
		logger.info("Elementi cercati per anno: ");
		for (ElementoCatalogo e : listaElementiCercatiAnno) {
			logger.info(e.toString());
		}
	}

	private static void inputElementoSearchByAutor(Map<String, ElementoCatalogo> allElements, Scanner scanner) {

		scanner.nextLine();

		String autore;

		do {
			System.out.println("Inserisci l'autore da cercare:");
			autore = scanner.nextLine();
			if (autore.equals("")) {
				logger.error("DATO OBBLIGATORIO!");
				logger.error("Inserisci l'AUTORE!");
			}
		} while (autore.equals(""));

		List<ElementoCatalogo> listByAutor = ricercaElementiAutore(allElements, autore);
		stampaRisultatoRicercaConAutore(listByAutor);

	}

	private static List<ElementoCatalogo> ricercaElementiAutore(Map<String, ElementoCatalogo> allElements,
			String autore) {
		return allElements.values().stream().filter(elemento -> elemento instanceof Libro)
				.map(elemento -> (Libro) elemento).filter(libro -> libro.getAutore().equals(autore))
				.collect(Collectors.toList());
	}

	private static void stampaRisultatoRicercaConAutore(List<ElementoCatalogo> listaElementiCercatiAutore) {
		logger.info("Elementi cercati per autore: ");
		for (ElementoCatalogo e : listaElementiCercatiAutore) {
			logger.info(e.toString());
		}
	}
}
