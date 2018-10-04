package modele;

import java.util.ArrayList;
import java.util.Collection;

public class NoeudAStar extends Noeud{

	private int coutH;
	private NoeudAStar noeudAstarParent;

	public NoeudAStar(Manoir carte, Piece position, Action action, NoeudAStar noeudParent) {
		super(carte, position,action);

		if (noeudParent == null) {
			this.profondeur = 0;
			this.cout = 0;
		} else {
			this.profondeur = noeudParent.profondeur + 1;
			this.cout = noeudParent.cout + 1;
			if (this.action == Action.RAMASSER )
				this.cout -= this.positionRobot.gainRamasser();
			else if (this.action == Action.ASPIRER)
				this.cout -= this.positionRobot.gainAspirer();
		}

		this.coutH = heuristique();
		this.noeudAstarParent = noeudParent;
	}

	public int getCoutH() {
		return coutH;
	}

	public void setCoutH(int coutH) {
		this.coutH = coutH;
	}

	@Override
	public NoeudAStar getNoeudParent(){
		return noeudAstarParent;
	}

	/**
	 * Calculer une estimation du coût restant jusqu'à un noeud solution.
	 * @param n Le noeud courant.
	 * @return Une estimation pour atteindre un noeud solution depuis le noeud n.
	 */
	private int heuristique() {
		// Cherche un noeud peut être solution proche du noeud courant
		int rayon = 1;
		while (true) {
			Collection<Piece> pieces = spirale(rayon, this.getPositionRobot(), this.getCarte());
			// Si aucune pièce, on parcouru toutes les pièce, on retourne +infini
			if (pieces.size() == 0)
				return 100000;

			// est-ce que le noeud est lui-même solution
			if (this.positionRobot.getPoussiere() || this.positionRobot.getBijou())
				return 0;
			
			// sinon on cherche parmi les autres cases en parcourant des cercles de plus en plus grands
			for (Piece p : pieces) {
				if (p.getPoussiere() || p.getBijou())
					return (Math.abs(this.getPositionRobot().getAbscisse() - p.getAbscisse()) + 
							Math.abs(this.getPositionRobot().getOrdonnee() - p.getOrdonnee()));
			}
			rayon++;
		}
	}

	private Collection<Piece> spirale(int rayon, Piece depart, Manoir manoir) {
		Collection<Piece> pieces = new ArrayList<>();
		Piece[][] map = manoir.getLesPieces();

		int abs = depart.getAbscisse();
		int ord = depart.getOrdonnee();

		// on parle de l'angle en haut à droite
		int i = abs + rayon;
		int j = ord + rayon;

		// le côté droit
		while (j != ord - rayon) {
			try {
				pieces.add(map[i][j]);
			} catch (ArrayIndexOutOfBoundsException ex) {
				// la pièce n'existe pas, rien à faire
			}
			j--;
		}

		// le coté bas
		while (i != abs - rayon) {
			try {
				pieces.add(map[i][j]);
			} catch (ArrayIndexOutOfBoundsException ex) {
				// la pièce n'existe pas, rien à faire
			}
			i--;
		}

		// le côté gauche
		while (j != ord+ rayon) {
			try {
				pieces.add(map[i][j]);
			} catch (ArrayIndexOutOfBoundsException ex) {
				// la pièce n'existe pas, rien à faire
			}
			j++;
		}

		// le côté haut
		while (i != abs + rayon) {
			try {
				pieces.add(map[i][j]);
			} catch (ArrayIndexOutOfBoundsException ex) {
				// la pièce n'existe pas, rien à faire
			}
			i++;
		}


		return pieces;
	}

	@Override
	public int evaluation() {
		return cout + coutH;
	}

}
