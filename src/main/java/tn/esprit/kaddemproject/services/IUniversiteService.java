package tn.esprit.kaddemproject.services;


import tn.esprit.kaddemproject.entities.Departement;
import tn.esprit.kaddemproject.entities.Specialite;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IUniversiteService {

    void assignUniversiteToDepartement(Integer idUniversite, Integer idDepartement);

    List<Departement> retrieveDepartementsByUniversite(Integer idUniversite);

    Map<Specialite,Float> getMontantContartEntreDeuxDate(int idUniv, LocalDate startDate, LocalDate endDate);

}
