package modele;

import java.util.*;

public class Agent implements Runnable {

	/** position du robot aspirateur. */
	private Piece position;
	private Piece destination;

	private Environnement env;
	private Capteur capteur;
	private Effecteur effecteur;
	private Manoir croyance; // carte retenue lors de l'observation avec position aspi
	private int desir; // points - coût > 0
	private ArrayList<Action> intentions;
	private int etatInterne; // score
	// choix de l'utilisateur, si à VRAI on utilise l'exploration INFORME , si a FAUX alors NON INFORME
	private boolean informe;

	public Agent(Environnement env, boolean informe) {
		this.env = env;
		effecteur = new Effecteur(this);
		capteur = new Capteur();
		this.informe = informe;
		
		// creation des copies de la carte de l'environnement
		// /!\ faire attention a pas faire de references, il faut faire une copie de la carte
		Manoir carte = env.getPieces();

		Piece[][] carteCroyance = new Piece[carte.getLargeur()][carte.getLongueur()];
		desir = 0;
		for (int i = 0 ; i < carte.getLargeur() ; i++) {
			for (int j = 0 ; j < carte.getLongueur() ; j++) {
				carteCroyance[i][j] = new Piece(i,j);
			}
		}
		this.croyance = new Manoir(carteCroyance);

		etatInterne = 0;
		// initialise la position au centre du manoir
		position = croyance.piece(env.getLargeur() / 2, env.getLongueur()/2); 
		destination = null;
		intentions = new ArrayList<>();
	}

	public Piece getDestination() {
		return this.destination;
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

	public Manoir getCroyance() {
		return croyance;
	}

	public void setCroyance(Manoir croyance) {
		this.croyance = croyance;
	}

	/**
	 * Récupérer l'action que le robot est en train de faire.
	 * @return L'action en cours.
	 */
	public Action action() {
		return this.effecteur.getAction();		
	}

	/**
	 * Demande au capteur d'observer son environnement pour mettre à jour ses croyances
	 */
	public void majCroyances() {
		if(destination == null) {
			synchronized (env.getPieces()) {
				croyance.setLesPieces(capteur.observe(croyance.getLesPieces(), env.getPieces().getLesPieces()));
			}
		}
		// todo function pour placer l'agent dans la matrice ?
	}

	/**
	 * Compare la carte des croyances à celle des désir pour savoir si des actions sont nécessaires
	 * @return boolean à vrai il y a une difference entre les 2 carte
	 */
	private boolean besoinAction() {
		// verifie les différences entre la carte désirée et la carte observée
		for(int i = 0 ; i < croyance.getLargeur() ; i++) {
			for (int j = 0 ; j < croyance.getLongueur() ; j++) {
				if(croyance.piece(i,j).getBijou() || croyance.piece(i,j).getPoussiere()) {
					// si une différence est repérée on demande a effectuer une action
					return true;
				}
			}
		}
		return false;
	}	

	/**
	 * lance l'agorithme pour effectuer des actions si necessaire
	 * @param agir booleen indiquant si il est nécessaire d'agir
	 */
	private void choisisAction(boolean agir) {
		// si pas besoin d action
		if(!agir) {
			intentions.add(Action.ATTENDRE);
		}
		// sinon
		else {
			Noeud n;
			if(informe) {		// utilise l'exploration informée
				n = new NoeudAStar(this.croyance, this.position, Action.ATTENDRE, null);
			} else {			// utilise l'exploration NON informée
				n = new NoeudUCF(this.croyance, this.position, Action.ATTENDRE, null);
			}

			intentions = exploration(n);
		}
	}
	/**
	 * Demande a l'effectueur d'effectuer une suite d'action
	 */
	private void executerAction() {
		effecteur.executeAction(intentions);
		this.intentions.clear();
		this.destination = null;
	}

	/**
	 * Récupérer la liste des actions à faire par un algorithme d'exploration.
	 * @return La liste des actions.
	 */
	private ArrayList<Action> exploration(Noeud noeudDepart) {
		// initialisation de la liste des noeuds déjà explorés
		ArrayList<Noeud> explored = new ArrayList<>();
		// Initialisation de la frontière
		PriorityQueue<Noeud> frontiere = new PriorityQueue<>(100,
			new Comparator<Noeud>(){
				@Override
				public int compare(Noeud i, Noeud j){
					if(i.evaluation() > j.evaluation()){
						return 1;
					}
					else if (i.evaluation() < j.evaluation()){
						return -1;
					}
					else{
						return 0;
					}
				}
			}
		);
		frontiere.add(noeudDepart);


		while (true) {
			if (frontiere.isEmpty())
				return null;

			boolean continuer = true;
			Noeud n = null;
			while (continuer) {
				n = frontiere.poll();
				continuer = explored.contains(n);
			}
		
			explored.add(n);
			
			// S'il est solution on récupère la liste des actions pour arriver à ce noeud
			if (n.estSolution(this.desir)) {
				System.out.println(n.getCout());
				ArrayList<Action> actions = new ArrayList<>();
				Noeud node = n;
				this.destination = node.getPositionRobot();
				while (node != null) {
					actions.add(node.getAction());
					node = node.getNoeudParent();
				}
				return actions;
			}
			
			// expansion
			if (n.getProfondeur() < 7)
				if (informe)
					frontiere.addAll(this.AstarSearch(n));
				else
					frontiere.addAll(this.UCFSearch(n));
			else 
				return null;
		}
	}
	
	/**
	 * Récupérer les noeuds voisins d'un noeud pour a*.
	 * @param p : le noeud dont on veut les voisons
	 * @return La liste des noeuds voisins du noeud en parametre.
	 */
	private ArrayList<NoeudAStar> AstarSearch(Noeud n){
		NoeudAStar noeud = (NoeudAStar) n;

		ArrayList<NoeudAStar> voisins = new ArrayList<>();

		Piece haut = noeud.getCarte().voisin(noeud.getPositionRobot(), Direction.haut);
		Piece bas = noeud.getCarte().voisin(noeud.getPositionRobot(), Direction.bas);
		Piece gauche = noeud.getCarte().voisin(noeud.getPositionRobot(), Direction.gauche);
		Piece droite = noeud.getCarte().voisin(noeud.getPositionRobot(), Direction.droite);

		Piece position = noeud.getPositionRobot();
		// haut
		if(haut != null) {
			voisins.add(new NoeudAStar(noeud.getCarte(),haut,Action.MONTER,noeud));
		}

		// bas
		if(bas != null) {
			voisins.add(new NoeudAStar(noeud.getCarte(),bas,Action.DESCENDRE,noeud));
		}

		// gauche
		if(gauche != null) {
			voisins.add(new NoeudAStar(noeud.getCarte(),gauche,Action.GAUCHE,noeud));
		}

		// droite
		if(droite != null) {
			voisins.add(new NoeudAStar(noeud.getCarte(),droite,Action.DROITE,noeud));
		}

		// aspirer
		if (noeud.getCarte().piece(position).getBijou() || noeud.getCarte().piece(position).getPoussiere()) {
			voisins.add(new NoeudAStar(this.getCarteApresAspiration(position), noeud.getPositionRobot(),
					Action.ASPIRER, noeud));
		}
		
		// ramasser
		if (noeud.getCarte().piece(position).getBijou()) {
			voisins.add(new NoeudAStar(this.getCarteApresRamassage(position), noeud.getPositionRobot(), 
					Action.RAMASSER, noeud));
		}
		
		return voisins;
	}
	
	/**
	 * Récupérer les noeuds voisins d'un noeud.
	 * @param noeud Le noeud courant dont on cherche les voisins.
	 * @return La liste des noeuds voisins du noeud courant.
	 */
	private Collection<Noeud> UCFSearch(Noeud n) {
		NoeudUCF noeud = (NoeudUCF) n;
		ArrayList<Noeud> noeudsVoisins = new ArrayList<>();

		Piece position = noeud.getPositionRobot();
		// pour chaque action possible
		// 		- monter
		Piece p = croyance.voisin(position, Direction.haut);
		if (p != null) {
			Noeud s = new NoeudUCF(noeud.getCarte(), p, Action.MONTER, noeud);
			noeudsVoisins.add(s);
		}

		//		- descendre
		p = croyance.voisin(position, Direction.bas);
		if (p != null) {
			Noeud s = new NoeudUCF(noeud.getCarte(), p, Action.DESCENDRE, noeud);
			noeudsVoisins.add(s);
		}

		//	- droite
		p = croyance.voisin(position, Direction.droite);
		if (p != null) {
			Noeud s = new NoeudUCF(noeud.getCarte(), p, Action.DROITE, noeud);
			noeudsVoisins.add(s);
		}
		//		- gauche
		p = croyance.voisin(position, Direction.gauche);
		if (p != null) {
			Noeud s = new NoeudUCF(noeud.getCarte(), p, Action.GAUCHE, noeud);
			noeudsVoisins.add(s);
		}

		//		- aspirer
		if (noeud.getCarte().piece(position).getBijou() || noeud.getCarte().piece(position).getPoussiere()) {
			Noeud s = new NoeudUCF(this.getCarteApresAspiration(position), position, Action.ASPIRER, noeud);
			noeudsVoisins.add(s);
		}

		//		- ramasser
		if (noeud.getCarte().piece(position).getBijou()) {
			Noeud s = new NoeudUCF(this.getCarteApresRamassage(position), position, Action.RAMASSER, noeud);
			noeudsVoisins.add(s);
		}

		return noeudsVoisins;
	}

	/**
	 * Récupérer l'état de l'environnement après aspiration.
	 * @return  l'état de l'environnement après aspiration.
	 */
	private Manoir getCarteApresAspiration(Piece positionRobot) {
		int largeur = croyance.getLargeur();
		int longueur = croyance.getLongueur();

		Piece[][] carteApresAspiration = new Piece[largeur][longueur];
		for (int i = 0 ; i < largeur ; i++) {
			for (int j = 0 ; j < longueur ; j++) {
				carteApresAspiration[i][j] = new Piece(i,j);
			}
		}

		Manoir manoir = new Manoir(carteApresAspiration);
		manoir.piece(positionRobot).setBijou(false);
		manoir.piece(positionRobot).setPoussiere(false);
		
		return manoir;
	}

	/**
	 * Récupérer l'état de l'environnement après ramassage.
	 * @return l'état de l'environnement après ramassage.
	 */
	private Manoir  getCarteApresRamassage(Piece positionRobot) {
		int largeur = croyance.getLargeur();
		int longueur = croyance.getLongueur();

		Piece[][] carteApresRamassage = new Piece[largeur][longueur];
		for (int i = 0 ; i < largeur ; i++) {
			for (int j = 0 ; j < longueur ; j++) {
				carteApresRamassage[i][j] = new Piece(i,j);
				if (croyance.piece(i,j).getPoussiere()) {
					carteApresRamassage[i][j].setPoussiere(true);
					carteApresRamassage[i][j].setBijou(true);
				}
			}
		}

		Manoir manoir = new Manoir(carteApresRamassage);
		manoir.piece(positionRobot).setBijou(false);
		
		return manoir;
		
	}
	/**
	 * Cycle de vie de l'agent
	 */
	@Override
	public void run() {
		while(true){
			majCroyances();
			boolean agir = besoinAction();
			choisisAction(agir);
			if(intentions != null)
				executerAction();
		}
	}

}
