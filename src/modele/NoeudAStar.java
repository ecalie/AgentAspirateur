package modele;


public class NoeudAStar extends Noeud{

    private int coutG;
    private int coutH;
    private NoeudAStar noeudAstarParent;

    public NoeudAStar(Piece position, Action action, NoeudAStar noeudParent, int coutF, int coutG, int coutH) {
        super(position,action,coutF);
        this.coutG = coutG;
        this.coutH = coutH;
        this.noeudAstarParent = noeudParent;
    }

    public int getCout() {
        return this.cout;
    }

    public int getCoutG() {
        return coutG;
    }

    public void setCoutG(int coutG) {
        this.coutG = coutG;
    }

    public int getCoutH() {
        return coutH;
    }

    public void setCoutH(int coutH) {
        this.coutH = coutH;
    }

    public void setCoutF(int coutF) {
        this.cout = coutF;
    }

    @Override
    public NoeudAStar getNoeudParent(){
        return noeudAstarParent;
    }


    /**
     * Comparer le noeud avec un noeud solution
     * @param solution Le noeud solution
     * @return Vrai si les deux ont la même carte.
     */

    public boolean estSolution(NoeudAStar but) {
        return positionRobot == but.getPositionRobot();
    }

}
