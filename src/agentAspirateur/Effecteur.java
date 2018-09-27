package agentAspirateur;

import java.util.ArrayList;

public class Effecteur {
    private Agent agent;

    public Effecteur(Agent agent) {
        this.agent = agent;
    }

    public void executeAction(Desir desir) {
        // redirige vers une action suivant le désir de l'agent
    }

    private void seDeplacer(Direction d) {
        // appel de plus court chemin
        // ajouter unité de deplacement au score
    }

    private void aspirerPoussiere() {
        // modif etat piece
        // modif score
    }

    private void ramasserBijou() {
        // modif etat piece
        // modif score
    }



}
