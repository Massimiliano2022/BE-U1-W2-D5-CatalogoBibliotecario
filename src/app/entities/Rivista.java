package app.entities;

public class Rivista extends ElementoCatalogo {

	private Periodicita periodicita;

	public void setPeriodicita(Periodicita periodicita) {
		this.periodicita = periodicita;
	}

	public Periodicita getPeriodicita() {
		return periodicita;
	}

	public Rivista(String codiceIsbn, String titolo, Integer annoPubblicazione, int numeroPagine,
			Periodicita periodicita) {
		super(codiceIsbn, titolo, annoPubblicazione, numeroPagine);
		setPeriodicita(periodicita);
	}

	@Override
	public String toString() {
		return this.getCodiceIsbn() + "@" + this.getTitolo() + "@" + this.getAnnoPubblicazione() + "@"
				+ this.getNumeroPagine() + "@" + this.getPeriodicita() + "#";
	}

}
