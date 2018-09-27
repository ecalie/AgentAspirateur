package agentAspirateur;

import java.util.ArrayList;

public class Agent {

	/** position du robot aspirateur. */
	private Piece position;
	private Piece destination;
	private ArrayList<Piece> chemin;

	// todo Verifier si on peut y mettre l'environnement
	// todo la carte de l'agent est different de celle de l'env ?
	private Environnement env;
	private Capteur capteur;
	private Effecteur effecteur;
	private Croyance croyance;
	private Desir desir;
	private int etatInterne; // score

	public Agent(Environnement env) {
		this.env = env;
		effecteur = new Effecteur(this);
		capteur = new Capteur();
		// todo comment initialiser ?
		desir = Desir.aspirerTouteLaPoussiere;
		croyance = Croyance.seDeplacer;
		etatInterne = 0;
		// todo placer l'agent dans la matrice
		position = env.getCarte().get(1).get(1); // initialise position à la 1ère pièce du manoir
		destination = null;
		chemin = null;
	}

	public void observer() {
		if(destination == null)
			destination = capteur.observe(position, env);
	}

	public void evaluerCroyance(){
		if(position == destination && position.getBijou()) {
			croyance = Croyance.ramasser;
			destination = null; // on est arrivé a destination
		}

		else if(position == destination && position.getPoussiere()) {
			croyance = Croyance.aspirer;
			destination = null; // on est arrivé a destination
		}

		else
			croyance = Croyance.seDeplacer;
	}

	private void evaluerDesir() {
		if(croyance == Croyance.ramasser)
			desir = Desir.ramasserToutLesBijoux;
		else
			desir = Desir.aspirerTouteLaPoussiere;

	}

	private void executerAction() {
		if(croyance == Croyance.seDeplacer)
			this.calculChemin();
		effecteur.executeAction(desir);
	}

	private void calculChemin () {
		// todo utiliser algo A* + heuristique
	}
	
	public void demarrer() {
		while(true){
			observer();
			evaluerCroyance();
			evaluerDesir();
			executerAction();
		}
	}

	public Piece getPosition() {
		return position;
	}

	public Piece getDestination() {
		return destination;
	}

	public Environnement getEnv() {
		return env;
	}

	public int getEtatInterne() {
		return etatInterne;
	}

	public void setEtatInterne(int etatInterne) {
		this.etatInterne = etatInterne;
	}
}
