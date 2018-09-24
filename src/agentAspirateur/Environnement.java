package agentAspirateur;

import java.util.ArrayList;
import java.util.List;

public class Environnement {

	private int longueur;
	private int largeur;
	private int mesurePerformance; 
	private List<List<Piece>> pieces;	
	
	/**
	 * Créer l'environnement.
	 * @param longueur Nombre de pièces sur une ligne.
	 * @param largeur Nombre de lignes de pièces.
	 */
	public Environnement(int longueur, int largeur) {
		this.longueur = longueur;
		this.largeur = largeur;
		
		this.pieces = new ArrayList<>();
		for (int i = 0 ; i < this.largeur ; i++) {
			List<Piece> lignePieces = new ArrayList<>();
			for (int j = 0 ; j < this.longueur ; j++) {
				lignePieces.add(new Piece(i,j));
			}
			this.pieces.add(lignePieces);
		}
		
		this.mesurePerformance = 0;
	}
	
	/**
	 * Générer de la pousière dans une pièce aléatoire.
	 */
	public void genererPoussiere() {
		int abscisse = (int) Math.random() * this.largeur;
		int ordonnee = (int) Math.random() * this.longueur;
		
		this.pieces.get(ordonnee).get(abscisse).setPoussiere(true);
	}
	
	/**
	 * Générer un bijou dans une pièce aléatoire. 
	 */
	public void genererBijou() {
		int abscisse = (int) Math.random() * this.largeur;
		int ordonnee = (int) Math.random() * this.longueur;
		
		this.pieces.get(ordonnee).get(abscisse).setBijou(true);		
	}
	
	/**
	 * Renoyer la mesure de performance
	 */
	public int getMesurePerformance() {
		return this.mesurePerformance;
	}
	
	/**
	 * 
	 */
	public void demarrer() {
		while (true) {
			double probabilité = Math.random();
			if (probabilité > 0.66)
				this.genererBijou();
			else 
				genererPoussiere();
		}
	}
}
