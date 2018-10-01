package modele;

public class Noeud {

	private Piece[][] carte;
	private Piece positionRobot;
	/** L'action qui permet d'arriver à l'état correspondant au noeud. */
	private Action action;
	/** Le noeud parent dans l'arbre d'exploration. */
	private Noeud noeudParent;
	/** Coût total pour arriver à ce noeud. */
	private int cout;
	
	
	public Noeud(Piece[][] carte, Piece position, Action action, Noeud noeudParent, int cout) {
		this.carte = carte;
		this.positionRobot = position;
		this.action = action;
		this.noeudParent = noeudParent;
		this.cout = cout;
	}
	
	public Piece getPositionRobot() {
		return positionRobot;
	}

	public Action getAction() {
		return this.action;
	}
	
	public int getCout() {
		return this.cout;
	}
	
	public Noeud getNoeudParent() {
		return this.noeudParent;
	}

	/**
	 * Comparer le noeud avec un noeud solution
	 * @param solution Le noeud solution
	 * @return Vrai si les deux ont la même carte. 
	 */
	public boolean estSolution(Piece[][] carteSolution) {
		int largeur = carte.length;
		int longueur = carte[0].length;
		
		// vérifier les dimensions des deux cartes
		if (carteSolution.length != largeur ||
				carteSolution[0].length != longueur)
			return false;
		
		// verifie les différences entre la carte désirée et la carte observée
		for(int i = 0 ; i < largeur ; i++) {
			for (int j = 0 ; j < longueur ; j++) {
				if(carte[i][j].getBijou() != carteSolution[i][j].getBijou() ||
				   carte[i][j].getPoussiere() != carteSolution[i][j].getPoussiere()) {
					return false;
				}
			}
		}
		return true;
	}
}
