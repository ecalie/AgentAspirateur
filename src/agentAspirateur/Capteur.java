package agentAspirateur;

import java.util.ArrayList;
import java.util.List;

public class Capteur {

    public Piece observe(Piece position,Environnement env) {
        // possible d'opti avec un parcours en spirale

        // chercher salle avec poussiere autour de soit
        ArrayList<Piece> piecesAVisiter = new ArrayList<>();
        List<List<Piece>> cartePiece = env.getCarte();

        for(List<Piece> ligneP : cartePiece) {
            for(Piece p : ligneP) {
                if(p.getPoussiere() || p.getBijou())
                    piecesAVisiter.add(p);
            }
        }

        // Choix de la pièce la plus proche
        Piece pieceProche = null;
        for(Piece p : piecesAVisiter) {
            if(pieceProche == null || distance(position,p) < distance(position,pieceProche))
                pieceProche = p;
        }

        return pieceProche;
    }

    private int distance(Piece p1, Piece p2) {
        return (Math.abs(p1.getOrdonnée() - p2.getOrdonnée()) + Math.abs(p1.getAbscisse() - p2.getAbscisse()));
    }
}
