package tn.esprit.kaddemproject.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.kaddemproject.entities.Contrat;
import tn.esprit.kaddemproject.entities.Departement;
import tn.esprit.kaddemproject.entities.Specialite;
import tn.esprit.kaddemproject.entities.Universite;
import tn.esprit.kaddemproject.repositories.ContratRepository;
import tn.esprit.kaddemproject.repositories.UniversiteRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class IUniversiteServiceImpl implements IUniversiteService{

    private final UniversiteRepository universiteRepository;
    private final ContratRepository contratRepository;
    private final IDepartementServiceImpl departementService;

    @Override
    public void assignUniversiteToDepartement(Integer idUniversite, Integer idDepartement) {
        Universite universite = universiteRepository.findById(idUniversite).orElse(null);
        Departement departement = departementService.retrieveById(idDepartement);
        if (universite!= null && departement !=null){
            universite.getDepartements().add(departement);
        }
    }

    @Override
    public List<Departement> retrieveDepartementsByUniversite(Integer idUniversite) {
        Universite universite = universiteRepository.findById(idUniversite).orElse(null);
        return universite!=null ? universite.getDepartements(): null;
    }

    public List<Contrat> getContractsByDepartement(Departement departement) {
       return contratRepository.findByArchiveIsFalseAndEtudiantDepartement(departement);
    }


    @Override
    public Map<Specialite, Float> getMontantContartEntreDeuxDate(int idUniv, LocalDate startDate, LocalDate endDate) {

        // get all active contracts between the start and end date
        List<Departement> departements = this.retrieveDepartementsByUniversite(idUniv);
        List<Contrat> contrats = departements.stream()
                .map(departement -> this.getContractsByDepartement(departement))
                .flatMap(List::stream)
                .filter(contrat -> contrat.getArchive().equals(false) && isContractBetween(contrat,startDate,endDate))
                .collect(Collectors.toList());

        // get price per sepcialite
        Map<Specialite, Float> map = new HashMap<Specialite, Float>();
        if (contrats!= null){
            for (Specialite specialite : Specialite.values()) {
                float price = contrats.stream()
                        .filter(contrat -> contrat.getSpecialite().equals(specialite))
                        .map(contrat -> getMontantContract(contrat,startDate,endDate))
                        .collect(Collectors.summarizingLong(Long::longValue))
                        .getSum();

                map.put(specialite,price);
            }
        }

        return map;
    }

    public Boolean isContractBetween(Contrat contrat, LocalDate startDate, LocalDate endDate) {

        // java.util.Date before&after
        if(contrat.getDateDebutContrat().isAfter(startDate) && contrat.getDateFinContrat().isBefore(endDate)
                || contrat.getDateFinContrat().isAfter(startDate) && contrat.getDateFinContrat().isBefore(endDate)
                || contrat.getDateDebutContrat().isBefore(startDate) && contrat.getDateFinContrat().isAfter(endDate) ){
            return true;
        }
        return false;
    }



    public long getMontantContract(Contrat contrat, LocalDate startDate, LocalDate endDate){

        long nbJoursContrat = ChronoUnit.DAYS.between(contrat.getDateDebutContrat(), contrat.getDateFinContrat());
        long nbJoursBetweenDebutContratAndStartDate = ChronoUnit.DAYS.between(startDate, contrat.getDateDebutContrat());
        long nbJoursBetweenFinContratAndEndDate = ChronoUnit.DAYS.between(contrat.getDateFinContrat(), endDate);

        if(nbJoursBetweenDebutContratAndStartDate<0) nbJoursContrat -= Math.abs(nbJoursBetweenDebutContratAndStartDate);
        if(nbJoursBetweenFinContratAndEndDate<0) nbJoursContrat -= Math.abs(nbJoursBetweenFinContratAndEndDate);

        return nbJoursContrat>0 ? contrat.getMontantContrat() * nbJoursContrat/30 : 0;

    }

    public Map<Specialite, Float> getMontantContartEntreDeuxDate2(int idUniv, LocalDate startDate, LocalDate endDate) {

        // 1- Get all active contracts between the start and end date

        List<Departement> departements = this.retrieveDepartementsByUniversite(idUniv);

        // 1.1- get all active contracts by id university <<idUniv>>
        List<Contrat> contrats = new ArrayList<Contrat>();
        for (Departement departement : departements) {
                contrats.addAll(contratRepository.findByArchiveIsFalseAndEtudiantDepartement(departement));
        }

        // 1.2- keep only contracts between the start and end Date
        for (Contrat contrat: contrats) {
            if(isContractBetween(contrat,startDate,endDate) != true){
                contrats.remove(contrat);
            }
        }

     /*   // using java stream: this bloc replaces the bloc from ligne 98 to 109
        List<Contrat> contrats1 = departements.stream()
                .map(departement -> this.getContractsByDepartement(departement))
                .flatMap(List::stream)
                .filter(contrat -> contrat.getArchive().equals(false) && isContractBetween(contrat,startDate,endDate))
                .collect(Collectors.toList());
        */


        // 2- Get price per sepcialite
        Map<Specialite, Float> map = new HashMap<Specialite, Float>();
        if (contrats!= null){

            for (Specialite specialite : Specialite.values()) {
                float pricePerSpecialite = 0;

                for (Contrat contrat: contrats) {
                    if(contrat.getSpecialite().equals(specialite)){
                        pricePerSpecialite += getMontantContract(contrat,startDate,endDate);
                    }
                }



                // using java stream: replaces from ligne 127 to 131
                 pricePerSpecialite = contrats.stream()
                        .filter(contrat -> contrat.getSpecialite().equals(specialite))
                        .map(contrat -> getMontantContract(contrat,startDate,endDate))
                        .collect(Collectors.summarizingLong(Long::longValue))
                        .getSum();

                map.put(specialite,pricePerSpecialite);
            }
        }

        return map;
    }



}
