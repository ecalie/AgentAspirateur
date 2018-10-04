package modele;

public class Manoir {
	
	private Piece[][] lesPieces;
	private int largeur;
	private int longueur;
	
	public Manoir(Piece[][] pieces) {
		this.lesPieces = pieces;
		this.largeur = pieces.length;
		this.longueur = pieces[0].length;
	}
	
	public Piece[][] getLesPieces() {
		return lesPieces;
	}

	public void setLesPieces(Piece[][] lesPieces) {
		this.lesPieces = lesPieces;
	}

	public int getLargeur() {
		return largeur;
	}

	public int getLongueur() {
		return longueur;
	}

	public Piece piece(int abscisse, int ordonnee) {
		return lesPieces[abscisse][ordonnee];
	}
	
	public Piece piece(Piece piece) {
		return this.piece(piece.getAbscisse(), piece.getOrdonnee());
	}
	
	/**
	 * Récupérer la pièce voisine de la pièce courante selon une direction, si elle existe.
	 * @param d La direction de la pièce voisine. 
	 * @return La pièce voisine de la pièce courante selon la direction indiquée.
	 */
	public Piece voisin(Piece p, Direction d) {
		switch (d) {
		case haut:
			if (p.getOrdonnee() != 0)
				return lesPieces[p.getAbscisse()][p.getOrdonnee()-1];
			else 
				return null;
		case bas:
			if (p.getOrdonnee() != largeur - 1)
				return lesPieces[p.getAbscisse()][p.getOrdonnee()+1];
			else 
				return null;
		case droite: 
			if (p.getAbscisse()!= longueur - 1)
				return lesPieces[p.getAbscisse()+1][p.getOrdonnee()];
			else 
				return null;
		case gauche: 		
			if (p.getAbscisse() != 0)
				return lesPieces[p.getAbscisse()-1][p.getOrdonnee()];
			else
				return null;
		default: 
			return null;
		}
	}

	


}
