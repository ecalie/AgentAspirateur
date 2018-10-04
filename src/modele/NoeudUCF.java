package modele;


public class NoeudUCF extends Noeud{
    private Piece[][] carte;
    private NoeudUCF noeudParent;

    public NoeudUCF(Piece[][] carte, Piece position, Action action, NoeudUCF noeudParent, int cout) {
        super(position, action, cout);
        this.noeudParent = noeudParent;
        this.carte = carte;
    }

    @Override
    public NoeudUCF getNoeudParent() {
        return noeudParent;
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
