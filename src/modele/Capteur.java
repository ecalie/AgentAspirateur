package modele;

public class Capteur {

    public Piece[][] observe(Piece[][] carteAgent,Piece[][] carteEnv) {

       // test pour verifier que les tableaux ont les memes dimensions
        if(carteEnv.length == 0 || carteEnv.length != carteAgent.length || carteEnv[1].length != carteAgent[1].length) return null;

        // fait une copie de la carte de l'environnement.
        boolean aBijoux;
        boolean aPoussiere;
        for (int i = 0 ; i < carteEnv.length ; i++) {
            for (int j = 0 ; j < carteEnv[i].length ; j++) {
                aBijoux = carteEnv[i][j].getBijou();
                aPoussiere = carteEnv[i][j].getPoussiere();

                carteAgent[i][j].setBijou(aBijoux);
                carteAgent[i][j].setPoussiere(aPoussiere);
            }
        }

        return carteAgent;
    }

}
