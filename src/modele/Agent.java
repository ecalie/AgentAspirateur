package modele;

import java.util.*;

public class Agent implements Runnable {

	/** position du robot aspirateur. */
	private Piece position;
	private Piece destination;

	private Environnement env;
	private Capteur capteur;
	private Effecteur effecteur;
	private Piece[][] croyance; // carte retenue lors de l'observation avec position aspi
	private int desir; // points - coût > 0
	private ArrayList<Action> intentions;
	private int etatInterne; // score
	// choix de l'utilisateur, si à VRAI on utilise l'exploration INFORME , si a FAUX alors NON INFORME
	private boolean informe;

	public Agent(Environnement env,boolean informe) {
		this.env = env;
		this.informe = informe;

		effecteur = new Effecteur(this);
		capteur = new Capteur();

		// creation des copies de la carte de l'environnement
		// /!\ faire attention a pas faire de references, il faut faire une copie de la carte
		Piece[][] carte = env.getPieces();

		croyance = new Piece[carte.length][carte[0].length];
		desir = 0;
		synchronized (carte) {
			for (int i = 0; i < carte.length; i++) {
				for (int j = 0; j < carte[i].length; j++) {
					croyance[i][j] = new Piece(i, j, this.croyance);
				}
			}
		}

		etatInterne = 0;
		// todo placer l'agent dans la matrice
		position = croyance[0][0]; // initialise position à la 1ère pièce du manoir
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

	public Piece[][] getCroyance() {
		return croyance;
	}

	public void setCroyance(Piece[][] croyance) {
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
				croyance = capteur.observe(croyance, env.getPieces());
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
		for(int i = 0 ; i < croyance.length ; i++) {
			for (int j = 0 ; j < croyance[i].length ; j++) {
				if(croyance[i][j].getBijou() || croyance[i][j].getPoussiere()) {
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
			if(informe)		// utilise l'exploration informée
				intentions =explorationInformee();
			else			// utilise l'exploration NON informée
				intentions = explorationNonInformee();
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
	 * Récupérer la liste des actions à faire par un algorithme d'exploration informé.
	 * @return La liste des actions
	 */
	public ArrayList<Action> explorationInformee() {
		NoeudAStar noeudDepart = new NoeudAStar(position,Action.ATTENDRE,null,0,0,0);


		Integer dist, mindist;
		mindist = null;
		Piece but = null;
		for (int i = 0; i < croyance.length; i++) {
			for (int j = 0; j < croyance[i].length; j++) {
				if (croyance[i][j] != position && (croyance[i][j].getBijou() || croyance[i][j].getPoussiere())) {
					dist = heuristique(position, croyance[i][j]);
					if (mindist == null) {
						mindist = dist;
						but = croyance[i][j];
					} else if (dist < mindist) {
						mindist = dist;
						but = croyance[i][j];
					}
				}
			}
		}

		if(but != null) {
			NoeudAStar noeudArrivee = new NoeudAStar(but,Action.ATTENDRE,null,0,0,0);
			noeudDepart.setCoutH(heuristique(position,but));
			noeudDepart.setCoutF(noeudDepart.getCoutH());
			List<NoeudAStar> chemin = AstarSearch(noeudDepart,noeudArrivee);
			ArrayList<Action> listeActions = new ArrayList<>();
			this.destination = noeudArrivee.getPositionRobot();
			for(NoeudAStar noeud : chemin) {
				listeActions.add(noeud.getAction());
			}
			return listeActions;
		}
		return null;
	}

	/**
	 * Algorithme d'exploration A* permettant de trouver un chemin vers le but
	 * @param source noeud de départ, il sera le noeud racine de l'arbre
	 * @param but    le noeud à atteint
	 * @return La liste noeuds permettant d'aller du noeud source au but
	 */
	public List<NoeudAStar> AstarSearch(NoeudAStar source, NoeudAStar but){

		Set<NoeudAStar> explored = new HashSet<NoeudAStar>();

		PriorityQueue<NoeudAStar> queue = new PriorityQueue<>(100,
			new Comparator<NoeudAStar>(){
				@Override
				public int compare(NoeudAStar i, NoeudAStar j){
					if(i.getCout() > j.getCout()){
						return 1;
					}
					else if (i.getCout() < j.getCout()){
						return -1;
					}
					else{
						return 0;
					}
				}
			}
		);

		//initialisation du coût au départ
		source.setCoutG(0);
		queue.add(source);

		boolean trouve = false;
		NoeudAStar current = null;
		while((!queue.isEmpty())&&(!trouve)){
			// selection du noeud avec le score F le plus bas
			current = queue.poll();

			explored.add(current);
			//on regarde si on est au noeud solution
			if(current.estSolution(but)){

				NoeudAStar n = null;
				if(current.getPositionRobot().getBijou()) {
					n = new NoeudAStar(current.getPositionRobot(),Action.ASPIRER,current,0,0,0);
					n = new NoeudAStar(current.getPositionRobot(),Action.RAMASSER,n,0,0,0);
				}
				else if(current.getPositionRobot().getBijou())
					n = new NoeudAStar(current.getPositionRobot(),Action.RAMASSER,current,0,0,0);
				else if(current.getPositionRobot().getPoussiere())
					n = new NoeudAStar(current.getPositionRobot(),Action.ASPIRER,current,0,0,0);
				current = n;
				trouve = true;
			}
			else {
				//on visite tout les noeuds voisin
				for (NoeudAStar voisin : getVoisins(current)) {
					voisin.setCoutH(heuristique(voisin.getPositionRobot(), but.getPositionRobot()));
					int cost = 1;
					int temp_g_scores = current.getCoutG() + cost;
					int temp_f_scores = temp_g_scores + voisin.getCoutH();

					if ((explored.contains(voisin)) && (temp_f_scores >= voisin.getCout())) {
						continue;
					}
					else if ((!queue.contains(voisin)) ||
							(temp_f_scores < voisin.getCout())) {
						voisin.setCoutG(temp_g_scores);
						voisin.setCoutF(temp_f_scores);
						if (queue.contains(voisin)) {
							queue.remove(voisin);
						}
						queue.add(voisin);
					}
				}
			}
		}

		List<NoeudAStar> chemin = new ArrayList<>();
		while (current != null) {
			chemin.add(current);
			current = current.getNoeudParent();
		}
		// on veut la parocurir dans l'autre sens
		Collections.reverse(chemin);
		for (NoeudAStar n : chemin){
			System.out.println(n.getPositionRobot() + " : " + n.getAction());
		}
		return chemin;
	}

	/**
	 * Récupérer les noeuds voisins d'un noeud.
	 * @param p : le noeud dont on veut les voisons
	 * @return La liste des noeuds voisins du noeud en parametre.
	 */
	private ArrayList<NoeudAStar> getVoisins(NoeudAStar p){

		ArrayList<NoeudAStar> voisins = new ArrayList<>();

		Piece haut = p.getPositionRobot().voisin(Direction.haut);
		Piece bas = p.getPositionRobot().voisin(Direction.bas);
		Piece gauche = p.getPositionRobot().voisin(Direction.gauche);
		Piece droite = p.getPositionRobot().voisin(Direction.droite);

		// haut
		if(haut != null) {
			voisins.add(new NoeudAStar(haut,Action.MONTER,p,0,0,0));
		}

		// bas
		if(bas != null) {
			voisins.add(new NoeudAStar(bas,Action.DESCENDRE,p,0,0,0));
		}

		// gauche
		if(gauche != null) {
			voisins.add(new NoeudAStar(gauche,Action.GAUCHE,p,0,0,0));
		}

		// droite
		if(droite != null) {
			voisins.add(new NoeudAStar(droite,Action.DROITE,p,0,0,0));
		}

		return voisins;
	}

	private int heuristique(Piece depart, Piece arrivee) {
		// distance de manhattan
		return (Math.abs(depart.getAbscisse() - arrivee.getAbscisse()) + Math.abs(depart.getOrdonnee() - arrivee.getOrdonnee()));
	}


	/**
	 * Récupérer la liste des actions à faire par un algorithme d'exploration non informé.
	 * @return La liste des actions.
	 */
	private ArrayList<Action> explorationNonInformee() {
		ArrayList<NoeudUCF> frontiere = new ArrayList<>();

		frontiere.add(new NoeudUCF(this.croyance, this.position, Action.ATTENDRE, null, 0));
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
			NoeudUCF n = frontiere.remove(ind);

			// S'il est solution on récupère la liste des actions pour arriver à ce noeud
			if (n.estSolution(this.desir)) {
				ArrayList<Action> actions = new ArrayList<>();
				NoeudUCF node = n;
				this.destination = node.getPositionRobot();
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
	 * @param noeud Le noeud courant dont on cherche les voisins.
	 * @return La liste des noeuds voisins du noeud courant.
	 */
	private ArrayList<NoeudUCF> expansionUniformCostSearch(NoeudUCF noeud) {
		ArrayList<NoeudUCF> noeudsVoisins = new ArrayList<>();

		Piece position = noeud.getPositionRobot();
		// pour chaque action possible
		// 		- monter
		Piece p = position.voisin(Direction.haut);
		if (p != null) {
			NoeudUCF s = new NoeudUCF(this.croyance, p, Action.MONTER, noeud, noeud.getCout() + 1);
			noeudsVoisins.add(s);
		}

		//		- descendre
		p = position.voisin(Direction.bas);
		if (p != null) {
			NoeudUCF s = new NoeudUCF(this.croyance, p, Action.DESCENDRE, noeud, noeud.getCout() + 1);
			noeudsVoisins.add(s);
		}

		//	- droite
		p = position.voisin(Direction.droite);
		if (p != null) {
			NoeudUCF s = new NoeudUCF(this.croyance, p, Action.DROITE, noeud, noeud.getCout() + 1);
			noeudsVoisins.add(s);
		}
		//		- gauche
		p = position.voisin(Direction.gauche);
		if (p != null) {
			NoeudUCF s = new NoeudUCF(this.croyance, p, Action.GAUCHE, noeud, noeud.getCout() + 1);
			noeudsVoisins.add(s);
		}

		//		- aspirer
		if (position.getBijou() || position.getPoussiere()) {
			NoeudUCF s = new NoeudUCF(this.getCarteApresApiration(), position, Action.ASPIRER, noeud, noeud.getCout() + 1 - position.gainAspirer());
			noeudsVoisins.add(s);
		}

		//		- ramasser
		if (position.getBijou()) {
			NoeudUCF s = new NoeudUCF(this.getCarteApresRamassage(), position, Action.RAMASSER, noeud, noeud.getCout() + 1 - position.gainRamasser());
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
