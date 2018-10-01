package modele;

import java.util.ArrayList;

public class Effecteur {
    private Agent agent;

    public Effecteur(Agent agent) {
        this.agent = agent;
    }

    public void executeAction(ArrayList<Action> intentions) {
        // redirige vers une action suivant le désir de l'agent
    	
    	for (int i = intentions.size()-1 ; i >= 0 ; i--) {
    		Action a = intentions.get(i);
    		switch (a) {
    		case MONTER:
    			seDeplacer(Direction.haut);
    			break;
    		case DESCENDRE:
    			seDeplacer(Direction.bas);
    			break;
    		case DROITE:
    			seDeplacer(Direction.droite);
    			break;
    		case GAUCHE:
    			seDeplacer(Direction.gauche);
    			break;
    		case ASPIRER:
    			aspirerTout();
    			break;
    		case RAMASSER:
    			ramasserBijou();
    			break;
    		}
    		
    		try {
    			Thread.sleep(300);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    }

    private void seDeplacer(Direction d) {
        // appel de plus court chemin
        // ajouter unité de deplacement au score
    	
    	agent.setPosition(agent.getPosition().voisin(d));
    	agent.setEtatInterne(agent.getEtatInterne() - 1);
    }

    private void aspirerTout() {
        // modif etat piece
        // modif score
    	
    	agent.setEnv(agent.getEnv().aspirer(agent.getPosition().getAbscisse(), agent.getPosition().getOrdonnee()));
    	agent.setEtatInterne(agent.getEtatInterne() - 1 + agent.getPosition().gainAspirer());
    }

    private void ramasserBijou() {
        // modif etat piece
        // modif score
    	
    	agent.setEnv(agent.getEnv().ramasser(agent.getPosition().getAbscisse(), agent.getPosition().getOrdonnee()));
    	agent.setEtatInterne(agent.getEtatInterne() - 1 + agent.getPosition().gainRamasser());
    }
}
