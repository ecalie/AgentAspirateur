package modele;

public class Piece {

	private boolean poussiere;
	private boolean bijou;

	private int abscisse;
	private int ordonnee;

	public Piece(int a, int o) {
		this.ordonnee = o;
		this.abscisse = a;
		this.poussiere = false;
		this.bijou = false;
	}

	public void setPoussiere(boolean p) {
		this.poussiere = p;
	}

	public void setBijou(boolean b) {
		this.bijou = b;
	}

	public boolean getPoussiere() {
		return this.poussiere;
	}

	public boolean getBijou() {
		return this.bijou;
	}

	public int getAbscisse() {
		return abscisse;
	}

	public int getOrdonnee() {
		return ordonnee;
	}

	/**
	 * Calculer le gain si le robot aspire dans la pièce
	 * @return Le gain si le robot aspirer dans la pièce.
	 */
	public int gainAspirer() {
		return ((this.getPoussiere() ? Constante.gainPoussiere : 0) +
				(this.getBijou() ? Constante.perteBijou : 0)); 
	}
	
	/**
	 * Calculer le gain si le robot ramasse dans la pièce
	 * @return Le gain si le robot ramasse dans la pièce.
	 */
	public int gainRamasser() {
		return ((this.getBijou() ? Constante.gainBijou : 0)); 
	}

	@Override
	public String toString() {
		return "(" + this.abscisse + " ; " + this.ordonnee + ")";
	}
}

