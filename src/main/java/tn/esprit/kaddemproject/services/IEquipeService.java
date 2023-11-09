package tn.esprit.kaddemproject.services;


import tn.esprit.kaddemproject.entities.Equipe;
import tn.esprit.kaddemproject.generic.IGenericService;

public interface IEquipeService extends IGenericService<Equipe,Integer> {

    void faireEvoluerEquipes();

}
