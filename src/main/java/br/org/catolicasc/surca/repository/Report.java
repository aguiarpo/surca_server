package br.org.catolicasc.surca.repository;

public interface Report {
        int getMale();
        int getFemale();
        int getCastrated();
        int getNotCastrated();
        int getCat();
        int getDog();
        int getOther();
        String getState();
        String getCity();
        String getNeighborhood();
}
