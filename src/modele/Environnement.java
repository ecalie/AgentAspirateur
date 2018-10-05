package modele;

public class Environnement implements Runnable {

	private int longueur;
	private int largeur;

	private Manoir pieces;
	
	/**
	 * Créer l'environnement.
	 * @param longueur Nombre de pièces sur une ligne.
	 * @param largeur Nombre de lignes de pièces.
	 */
	public Environnement(int longueur, int largeur) {
		this.longueur = longueur;
		this.largeur = largeur;
		
		Piece[][] pieces = new Piece[longueur][largeur];
		for (int i = 0 ; i < this.largeur ; i++) {
			for (int j = 0 ; j < this.longueur ; j++) {
				pieces[i][j] = new Piece(i,j);
			}
		}
		this.pieces = new Manoir(pieces);
	}
	
	public int getLongueur() {
		return longueur;
	}

	public int getLargeur() {
		return largeur;
	}

	public Manoir getPieces() {
		return pieces;
	}

	/**
	 * Générer de la pousière dans une pièce aléatoire.
	 */
	public void genererPoussiere() {
		int abscisse = (int) (Math.random() * this.largeur);
		int ordonnee = (int) (Math.random() * this.longueur);
		
		this.pieces.piece(abscisse,ordonnee).setPoussiere(true);
	}
	
	/**
	 * Générer un bijou dans une pièce aléatoire. 
	 */
	public void genererBijou() {
		int abscisse = (int) (Math.random() * this.largeur);
		int ordonnee = (int) (Math.random() * this.longueur);
		
		this.pieces.piece(abscisse,ordonnee).setBijou(true);		
	}
	
	public Environnement ramasser(int abscisse, int ordonnee) {
		this.pieces.piece(abscisse,ordonnee).setBijou(false);
		return this;
	}
	
	public Environnement aspirer(int abscisse, int ordonnee) {
		this.pieces.piece(abscisse,ordonnee).setBijou(false);
		this.pieces.piece(abscisse,ordonnee).setPoussiere(false);
		return this;
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		while (true) {
			double probabilité = Math.random();
			if (probabilité < Constante.probabiliteBijou)
				this.genererBijou();
			else 
				this.genererPoussiere();
			try {
				Thread.sleep(Constante.attenteEnvironnement);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
