package agentAspirateur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agent {

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
		Piece[][] carte = env.getCarte();
		for (int i = 0 ; i < carte.length ; i++) {
			for (int j = 0 ; j < carte[i].length ; j++) {
				croyance[i][j] = new Piece(i,j);
				desir[i][j] = new Piece(i,j);
			}
		}

		etatInterne = 0;
		// todo placer l'agent dans la matrice
		position = croyance[1][1]; // initialise position à la 1ère pièce du manoir
		destination = null;
		intentions = new ArrayList<>();

	}

	public void majCroyances() {
		if(destination == null)
			croyance = capteur.observe(croyance, env.getCarte());
		// todo function pour placer l'agent dans la matrice ?
	}

	private boolean besoinAction() {
		// verifie les différences entre la carte désirée et la carte observée
		for(int i = 0 ; i < croyance.length ; i++) {
			for (int j = 0 ; j < croyance[i].length ; j++) {
				if(croyance[i][j].getBijou() != desir[i][j].getBijou() &&
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
			intentions = explorationInformee();
		}
	}

	private void executerAction() {
			// passer tableau d'intentions a l'effectueur qui fera :
						//for(Action intention : intentions)
							// ...
							// mets a jour la map de l'ihm

	}


	private ArrayList<Action> explorationInformee() {
		// todo utiliser algo A* + heuristique
		return null;
	}
	
	public void demarrer() {
		while(true){
			majCroyances();
			boolean agir = besoinAction();
			choisisAction(agir);
			executerAction();
		}
	}

}
