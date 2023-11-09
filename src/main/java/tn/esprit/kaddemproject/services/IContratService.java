package tn.esprit.kaddemproject.services;

import tn.esprit.kaddemproject.entities.Contrat;
import tn.esprit.kaddemproject.generic.IGenericService;

import java.time.LocalDate;

public interface IContratService extends IGenericService<Contrat,Integer> {

    Contrat affectContratToEtudiant (Integer idContrat, String nomE, String prenomE);
    Integer nbContratsValides(LocalDate startDate, LocalDate endDate);
    void retrieveAndUpdateStatusContrat();
    void archiveExpiredContracts();

}

