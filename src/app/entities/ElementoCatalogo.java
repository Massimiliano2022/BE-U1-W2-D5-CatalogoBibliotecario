package app.entities;

public abstract class ElementoCatalogo {

	private String codiceIsbn;
	private String titolo;
	private Integer annoPubblicazione;
	private int numeroPagine;

	public void setCodiceIsbn(String codiceIsbn) {
		this.codiceIsbn = codiceIsbn;
	}

	public String getCodiceIsbn() {
		return codiceIsbn;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setAnnoPubblicazione(Integer annoPubblicazione) {
		this.annoPubblicazione = annoPubblicazione;
	}

	public int getAnnoPubblicazione() {
		return annoPubblicazione;
	}

	public void setNumeroPagine(int numeroPagine) {
		this.numeroPagine = numeroPagine;
	}

	public int getNumeroPagine() {
		return numeroPagine;
	}

	public ElementoCatalogo(String codiceIsbn, String titolo, int annoPubblicazione, int numeroPagine) {
		setCodiceIsbn(codiceIsbn);
		setTitolo(titolo);
		setAnnoPubblicazione(annoPubblicazione);
		setNumeroPagine(numeroPagine);
	}
}
