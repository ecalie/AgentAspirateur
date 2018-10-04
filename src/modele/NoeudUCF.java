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
    public boolean estSolution(int desir) {
        return (this.calculerGain() - this.cout > desir);
    }


}
