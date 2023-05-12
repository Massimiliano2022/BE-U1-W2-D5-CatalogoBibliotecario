package app;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	public static void main(String[] args) {

		File file = new File("catalogo_biblioteca.txt");

		Map<String, ElementoCatalogo> allElements = new HashMap();

		if (file.exists()) {

			leggiElementiDalCatalogo(allElements, file);

			// rimuoviElementoCatalogo(allElements, "ISBN-00007");

			ElementoCatalogo elementoCercatoCodice = ricercaElementoConCodice(allElements, "B-00002");
			stampaRisultatoRicercaConCodice(elementoCercatoCodice);

			List<ElementoCatalogo> listaElementiCercatiAnno = ricercaElementiAnno(allElements, 2022);
			stampaRisultatoRicercaConAnno(listaElementiCercatiAnno);

			List<ElementoCatalogo> listaElementiCercatiAutore = ricercaElementiAutore(allElements, "autore1");
			stampaRisultatoRicercaConAutore(listaElementiCercatiAutore);

			salvaFileCatalogo(file, allElements);

		} else {

			Libro l1 = new Libro("B-00001", "libro1", 1994, 300, "autore1", Genere.FANTASY);
			Libro l2 = new Libro("B-00002", "libro2", 1920, 150, "autore2", Genere.ROMANZI);

			Rivista r1 = new Rivista("R-00006", "rivista1", 2022, 50, Periodicita.MENSILE);
			Rivista r2 = new Rivista("R-00007", "rivista2", 2023, 50, Periodicita.SEMESTRALE);

			aggiungiElementoAlCatalogo(allElements, l1);
			aggiungiElementoAlCatalogo(allElements, l2);
			aggiungiElementoAlCatalogo(allElements, r1);
			aggiungiElementoAlCatalogo(allElements, r2);

			rimuoviElementoCatalogo(allElements, "R-00007");

			ElementoCatalogo elementoCercatoCodice = ricercaElementoConCodice(allElements, "B-00002");
			stampaRisultatoRicercaConCodice(elementoCercatoCodice);

			List<ElementoCatalogo> listaElementiCercatiAnno = ricercaElementiAnno(allElements, 2022);
			stampaRisultatoRicercaConAnno(listaElementiCercatiAnno);

			List<ElementoCatalogo> listaElementiCercatiAutore = ricercaElementiAutore(allElements, "autore1");
			stampaRisultatoRicercaConAutore(listaElementiCercatiAutore);

			salvaFileCatalogo(file, allElements);
			stampaCatalogoCompleto(allElements);

		}
	}

	private static void aggiungiElementoAlCatalogo(Map<String, ElementoCatalogo> allElements, ElementoCatalogo e) {

		allElements.put(e.getCodiceIsbn(), e);

	}

	private static void leggiElementiDalCatalogo(Map<String, ElementoCatalogo> allElements, File file) {
		try {
			List<String> contents = FileUtils.readLines(file, "UTF-8");
			// String lineElemento = null;

			for (String line : contents) {
				if (line.startsWith(LIBRO)) {

					String[] elementoCatalogo = line.split("@|#");
					String codice = elementoCatalogo[0];
					String titolo = elementoCatalogo[1];
					Integer anno = Integer.valueOf(elementoCatalogo[2]);
					int numeroPagine = Integer.valueOf(elementoCatalogo[3]);
					String autore = elementoCatalogo[4];
					Genere genere = Genere.valueOf(elementoCatalogo[5]);

					aggiungiLibroDaCatalogo(allElements, codice, titolo, anno, numeroPagine, autore, genere);

				} else {

					String[] elementoCatalogo = line.split("@|#");
					String codice = elementoCatalogo[0];
					String titolo = elementoCatalogo[1];
					Integer anno = Integer.valueOf(elementoCatalogo[2]);
					int numeroPagine = Integer.valueOf(elementoCatalogo[3]);
					Periodicita periodicita = Periodicita.valueOf(elementoCatalogo[4]);

					aggiungiRivistaDaCatalogo(allElements, codice, titolo, anno, numeroPagine, periodicita);
				}
			}
		} catch (IOException e) { //
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error("FILE NON TROVATO!");
		}
	}

	private static void aggiungiLibroDaCatalogo(Map<String, ElementoCatalogo> allElements, String codice, String titolo,
			Integer anno, int numeroPagine, String autore, Genere genere) {

		Libro l = new Libro(codice, titolo, anno, numeroPagine, autore, genere);
		allElements.put(codice, l);
	}

	private static void aggiungiRivistaDaCatalogo(Map<String, ElementoCatalogo> allElements, String codice,
			String titolo, Integer anno, int numeroPagine, Periodicita periodicita) {

		Rivista r = new Rivista(codice, titolo, anno, numeroPagine, periodicita);
		allElements.put(codice, r);
	}

	private static void rimuoviElementoCatalogo(Map<String, ElementoCatalogo> allElements, String codiceIsbn) {
		ElementoCatalogo e = allElements.remove(codiceIsbn);
		logger.info("Elemento rimosso dal catalogo: " + e.getCodiceIsbn() + " " + e.getTitolo());
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

	private static void stampaCatalogoCompleto(Map<String, ElementoCatalogo> allElements) {
		logger.info("CATALOGO COMPLETO:");
		for (ElementoCatalogo e : allElements.values()) {
			logger.info(e.toString());
		}
	}

	private static void salvaFileCatalogo(File file, Map<String, ElementoCatalogo> allElements) {

		if (!file.exists()) {
			for (ElementoCatalogo elemento : allElements.values()) {
				try {
					FileUtils.writeStringToFile(file, elemento.toString() + System.lineSeparator(), "UTF-8", true);
				} catch (IOException e) {
					// e.printStackTrace();
					logger.error(e.getMessage());
					logger.error("Errore in scrittura file catalogo.");
				}
			}
		} else {
			stampaCatalogoCompleto(allElements);
		}
	}
}
