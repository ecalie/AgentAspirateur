package modele;


public class NoeudUCF extends Noeud{
    private NoeudUCF noeudParent;

    public NoeudUCF(Manoir carte, Piece position, Action action, NoeudUCF noeudParent) {
        super(carte, position, action);
        this.noeudParent = noeudParent;


		if (this.noeudParent == null) {
			this.profondeur = 0;
			this.cout = 0;
		} else {
			this.profondeur = this.noeudParent.profondeur + 1;
			this.cout = noeudParent.getCout() + 1;
			if (this.action == Action.RAMASSER )
				this.cout -= this.positionRobot.gainRamasser();
			else if (this.action == Action.ASPIRER)
				this.cout -= this.positionRobot.gainAspirer();
		}
    }

    @Override
    public NoeudUCF getNoeudParent() {
        return noeudParent;
    }

	@Override
	public int evaluation() {
		return cout;
	}

}
