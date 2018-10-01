package modele;

public class Environnement implements Runnable {

	private int longueur;
	private int largeur;

	private Piece[][] pieces;
	
	/**
	 * Créer l'environnement.
	 * @param longueur Nombre de pièces sur une ligne.
	 * @param largeur Nombre de lignes de pièces.
	 */
	public Environnement(int longueur, int largeur) {
		this.longueur = longueur;
		this.largeur = largeur;
		
		this.pieces = new Piece[largeur][longueur];
		for (int i = 0 ; i < this.largeur ; i++) {
			for (int j = 0 ; j < this.longueur ; j++) {
				pieces[i][j] = new Piece(i,j, this.pieces);
			}
		}
	}
	
	public int getLongueur() {
		return longueur;
	}

	public int getLargeur() {
		return largeur;
	}

	public Piece[][] getPieces() {
		return pieces;
	}

	/**
	 * Générer de la pousière dans une pièce aléatoire.
	 */
	public void genererPoussiere() {
		int abscisse = (int) (Math.random() * this.largeur);
		int ordonnee = (int) (Math.random() * this.longueur);
		
		this.pieces[abscisse][ordonnee].setPoussiere(true);
	}
	
	/**
	 * Générer un bijou dans une pièce aléatoire. 
	 */
	public void genererBijou() {
		int abscisse = (int) (Math.random() * this.largeur);
		int ordonnee = (int) (Math.random() * this.longueur);
		
		this.pieces[abscisse][ordonnee].setBijou(true);		
	}
	
	public Environnement ramasser(int abscisse, int ordonnee) {
		this.pieces[abscisse][ordonnee].setBijou(false);
		return this;
	}
	
	public Environnement aspirer(int abscisse, int ordonnee) {
		this.pieces[abscisse][ordonnee].setBijou(false);
		this.pieces[abscisse][ordonnee].setPoussiere(false);
		return this;
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		while (true) {
			double probabilité = Math.random();
			if (probabilité > 0.66)
				this.genererBijou();
			else 
				this.genererPoussiere();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
