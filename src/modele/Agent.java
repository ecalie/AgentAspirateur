package modele;

import java.util.ArrayList;

public class Agent implements Runnable {

	/** position du robot aspirateur. */
	private Piece position;
	private Piece destination;

	private Environnement env;
	private Capteur capteur;
	private Effecteur effecteur;
	private Piece[][] croyance; // carte retenue lors de l'observation avec position aspi
	private Piece[][] desir; // carte des pièces vides : objectif de l'agent
	private ArrayList<Action> intentions;
	private int etatInterne; // score

	public Agent(Environnement env) {
		this.env = env;
		effecteur = new Effecteur(this);
		capteur = new Capteur();

		// creation des copies de la carte de l'environnement
		// /!\ faire attention a pas faire de references, il faut faire une copie de la carte
		Piece[][] carte = env.getPieces();

		croyance = new Piece[carte.length][carte[0].length];
		desir = new Piece[carte.length][carte[0].length];
		for (int i = 0 ; i < carte.length ; i++) {
			for (int j = 0 ; j < carte[i].length ; j++) {
				croyance[i][j] = new Piece(i,j, this.croyance);
				desir[i][j] = new Piece(i,j, this.croyance);
			}
		}

		etatInterne = 0;
		// todo placer l'agent dans la matrice
		position = croyance[1][1]; // initialise position à la 1ère pièce du manoir
		destination = null;
		intentions = new ArrayList<>();

	}
	public Piece getPosition() {
		return this.position;
	}

	public void setPosition(Piece position) {
		this.position = position;
	}

	public int getEtatInterne() {
		return etatInterne;
	}

	public void setEtatInterne(int etatInterne) {
		this.etatInterne = etatInterne;
	}

	public Environnement getEnv() {
		return env;
	}

	public void setEnv(Environnement env) {
		this.env = env;
	}

	public Piece[][] getCroyance() {
		return croyance;
	}

	public void setCroyance(Piece[][] croyance) {
		this.croyance = croyance;
	}

	public void majCroyances() {
		if(destination == null)
			croyance = capteur.observe(croyance, env.getPieces());
		// todo function pour placer l'agent dans la matrice ?
	}

	private boolean besoinAction() {
		// verifie les différences entre la carte désirée et la carte observée
		for(int i = 0 ; i < croyance.length ; i++) {
			for (int j = 0 ; j < croyance[i].length ; j++) {
				if(croyance[i][j].getBijou() != desir[i][j].getBijou() ||
						croyance[i][j].getPoussiere() != desir[i][j].getPoussiere()) {
					// si une différence est repérée on demande a effectuer une action
					return true;
				}
			}
		}
		return false;
	}	


	private void choisisAction(boolean agir) {
		// si pas besoin d action
		if(!agir) {
			intentions.add(Action.ATTENDRE);
		}
		// sinon
		else {
			//intentions[] = appel algo d'exploration a*
			//intentions = explorationInformee();

			intentions = explorationNonInformee();
		}
	}

	private void executerAction() {
		effecteur.executeAction(intentions);
		this.intentions.clear();

		// passer tableau d'intentions a l'effectueur qui fera :
		//for(Action intention : intentions)
		// ...
		// mets a jour la map de l'ihm

	}


	private ArrayList<Action> explorationInformee() {
		// todo utiliser algo A* + heuristique
		return null;
	}

	/**
	 * Récupérer la liste des actions à faire par un algorithme d'exploration non informé.
	 * @return La liste des actions.
	 */
	private ArrayList<Action> explorationNonInformee() {
		ArrayList<Noeud> frontiere = new ArrayList<>();

		frontiere.add(new Noeud(this.croyance, this.position, Action.ATTENDRE, null, 0));
		while (true) {
			if (frontiere.isEmpty())
				return null;

			// On récupère le noeud dans la frontière dont le cout est minimal
			int min = frontiere.get(0).getCout();
			int ind = 0;
			for (int i = 0 ; i < frontiere.size() ; i++) {
				if (frontiere.get(i).getCout() < min) {
					min = frontiere.get(i).getCout();
					ind = i;
				}
			}
			Noeud n = frontiere.remove(ind);

			// S'il est solution on récupère la liste des actions pour arriver à ce noeud
			if (n.estSolution(this.desir)) {
				ArrayList<Action> actions = new ArrayList<>();
				Noeud node = n;
				while (node != null) {
					actions.add(node.getAction());
					node = node.getNoeudParent();

				}
				return actions;
			}
			// expansion
			frontiere.addAll(this.expansionUniformCostSearch(n));
		}
	}

	/**
	 * Récupérer les noeuds voisins d'un noeud.
	 * @param actions La liste des actions permettant d'arriver au noeud courant.
	 * @param noeud Le noeud courant dont on cherche les voisins.
	 * @return La liste des noeuds voisins du noeud courant.
	 */
	private ArrayList<Noeud> expansionUniformCostSearch(Noeud noeud) {
		ArrayList<Noeud> noeudsVoisins = new ArrayList<>();

		Piece position = noeud.getPositionRobot();
		// pour chaque action possible
		// 		- monter
		Piece p = position.voisin(Direction.haut);
		if (p != null) {
			Noeud s = new Noeud(this.croyance, p, Action.MONTER, noeud, noeud.getCout() + 1);
			noeudsVoisins.add(s);
		}

		//		- descendre
		p = position.voisin(Direction.bas);
		if (p != null) {
			Noeud s = new Noeud(this.croyance, p, Action.DESCENDRE, noeud, noeud.getCout() + 1);
			noeudsVoisins.add(s);
		}

		//	- droite
		p = position.voisin(Direction.droite);
		if (p != null) {
			Noeud s = new Noeud(this.croyance, p, Action.DROITE, noeud, noeud.getCout() + 1);
			noeudsVoisins.add(s);
		}
		//		- gauche
		p = position.voisin(Direction.gauche);
		if (p != null) {
			Noeud s = new Noeud(this.croyance, p, Action.GAUCHE, noeud, noeud.getCout() + 1);
			noeudsVoisins.add(s);
		}

		//		- aspirer
		if (position.getBijou() || position.getPoussiere()) {
			Noeud s = new Noeud(this.getCarteApresApiration(), position, Action.ASPIRER, noeud, noeud.getCout() + 1 - position.gainAspirer());
			noeudsVoisins.add(s);
		}

		//		- ramasser
		if (position.getBijou()) {
			Noeud s = new Noeud(this.getCarteApresRamassage(), position, Action.RAMASSER, noeud, noeud.getCout() + 1 - position.gainRamasser());
			noeudsVoisins.add(s);
		}

		return noeudsVoisins;
	}

	/**
	 * Récupérer l'état de l'environnement après aspiration.
	 * @return  l'état de l'environnement après aspiration.
	 */
	private Piece[][] getCarteApresApiration() {
		int largeur = croyance.length;
		int longueur = croyance[0].length;

		Piece[][] carteApresAspiration = new Piece[largeur][longueur];
		for (int i = 0 ; i < largeur ; i++) {
			for (int j = 0 ; j < longueur ; j++) {
				carteApresAspiration[i][j] = new Piece(i,j, this.croyance);
			}
		}
		return carteApresAspiration;
	}

	/**
	 * Récupérer l'état de l'environnement après ramassage.
	 * @return l'état de l'environnement après ramassage.
	 */
	private Piece[][] getCarteApresRamassage() {
		int largeur = croyance.length;
		int longueur = croyance[0].length;

		Piece[][] carteApresAspiration = new Piece[largeur][longueur];
		for (int i = 0 ; i < largeur ; i++) {
			for (int j = 0 ; j < longueur ; j++) {
				carteApresAspiration[i][j] = new Piece(i,j, this.croyance);
				if (croyance[i][j].getPoussiere())
					carteApresAspiration[i][j].setPoussiere(true);
			}
		}
		return carteApresAspiration;
	}

	@Override
	public void run() {
		while(true){
			majCroyances();
			boolean agir = besoinAction();
			choisisAction(agir);
			executerAction();
		}
	}
}
