package app.entities;

public class Libro extends ElementoCatalogo {

	private String autore;
	private Genere genere;

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public String getAutore() {
		return autore;
	}

	public void setGenere(Genere genere) {
		this.genere = genere;
	}

	public Genere getGenere() {
		return genere;
	}

	public Libro(String codiceIsbn, String titolo, Integer annoPubblicazione, int numeroPagine, String autore,
			Genere genere) {
		super(codiceIsbn, titolo, annoPubblicazione, numeroPagine);
		setAutore(autore);
		setGenere(genere);
	}

	@Override
	public String toString() {
		return this.getCodiceIsbn() + "@" + this.getTitolo() + "@" + this.getAnnoPubblicazione() + "@"
				+ this.getNumeroPagine() + "@" + this.getAutore() + "@" + this.getGenere() + "#";
	}
}
