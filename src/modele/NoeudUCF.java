package modele;


public class NoeudUCF extends Noeud{
    private NoeudUCF noeudParent;

    public NoeudUCF(Manoir carte, Piece position, Action action, NoeudUCF noeudParent, int cout) {
        super(carte, position, action, cout);
        this.noeudParent = noeudParent;
        
        if (this.noeudParent == null) 
        	this.profondeur = 0;
        else
        	this.profondeur = this.noeudParent.profondeur + 1;
    }

    @Override
    public NoeudUCF getNoeudParent() {
        return noeudParent;
    }

}
