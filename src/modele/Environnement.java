package modele;

import java.util.ArrayList;
import java.util.List;

public class Environnement implements Runnable {

	private int longueur;
	private int largeur;
	private int mesurePerformance;

	private Piece[][] pieces;
	
	/** Coordonnées de la dernière pièce modifiée par l'environnement. */
	private int abscisse;
	private int ordonnee;
	
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
				pieces[i][j] = new Piece(i,j);
			}
		}
		
		this.mesurePerformance = 0;
	}
	
	public int getLongueur() {
		return longueur;
	}

	public int getLargeur() {
		return largeur;
	}

	public int getAbscisse() {
		return abscisse;
		
	}

	public int getOrdonnee() {
		return ordonnee;
	}

	public Piece[][] getPieces() {
		return pieces;
	}

	/**
	 * Générer de la pousière dans une pièce aléatoire.
	 */
	public void genererPoussiere() {
		abscisse = (int) (Math.random() * this.largeur);
		ordonnee = (int) (Math.random() * this.longueur);
		
		this.pieces[abscisse][ordonnee].setPoussiere(true);
		
		System.out.println(pieces[abscisse][ordonnee].getBijou());
		System.out.println(pieces[abscisse][ordonnee].getPoussiere());
	}
	
	/**
	 * Générer un bijou dans une pièce aléatoire. 
	 */
	public void genererBijou() {
		abscisse = (int) (Math.random() * this.largeur);
		ordonnee = (int) (Math.random() * this.longueur);
		
		this.pieces[abscisse][ordonnee].setBijou(true);		
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
	@Override
	public void run() {
		while (true) {
			double probabilité = Math.random();
			if (probabilité > 0.66)
				this.genererBijou();
			else 
				this.genererPoussiere();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
