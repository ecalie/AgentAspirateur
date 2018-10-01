package modele;

public class Piece {

	private boolean poussiere;
	private boolean bijou;

	private int abscisse;
	private int ordonnee;

	private Piece[][] pieces;

	public Piece(int a, int o, Piece[][] p) {
		this.ordonnee = o;
		this.abscisse = a;
		this.pieces = p;
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
	 *
	 */


	/**
	 * Récupérer la pièce voisine de la pièce courante selon une direction, si elle existe.
	 * @param d La direction de la pièce voisine. 
	 * @return La pièce voisine de la pièce courante selon la direction indiquée.
	 */
	public Piece voisin(Direction d) {
		switch (d) {
		case haut:
			if (this.abscisse != 0)
				return pieces[this.abscisse-1][this.ordonnee];
			else 
				return null;
		case bas:
			if (this.abscisse != pieces.length - 1)
				return pieces[this.abscisse+1][this.ordonnee];
			else 
				return null;
		case droite: 
			if (this.ordonnee!= this.pieces[0].length - 1)
				return pieces[this.abscisse][this.ordonnee+1];
			else 
				return null;
		case gauche: 		
			if (this.ordonnee != 0)
				return pieces[this.abscisse][this.ordonnee-1];
			else
				return null;
		default: 
			return null;
		}
	}

	/**
	 * Calculer le gain si le robot aspire dans la pièce
	 * @return Le gain si le robot aspirer dans la pièce.
	 */
	public int gainAspirer() {
		return ((this.getPoussiere() ? Constante.gainPoussiere : 0) +
				(this.getBijou() ? Constante.permetBijou : 0)); 
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
		// TODO Auto-generated method stub
		return "(" + this.abscisse + " ; " + this.ordonnee + ")";
	}


}

