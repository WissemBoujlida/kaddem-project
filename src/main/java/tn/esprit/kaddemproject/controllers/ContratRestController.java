package tn.esprit.kaddemproject.controllers;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.esprit.kaddemproject.entities.Contrat;
import tn.esprit.kaddemproject.services.IContratService;

import java.time.LocalDate;


@RestController
@RequestMapping("/contract")
@AllArgsConstructor
public class ContratRestController {

    private IContratService contratService;

    @PostMapping("/{idContrat}/{nomE}/{prenomE}")
    public Contrat affectContratToEtudiant (@PathVariable Integer idContrat,
                                                @PathVariable String nomE,
                                                @PathVariable String prenomE){
        return contratService.affectContratToEtudiant(idContrat,nomE,prenomE);
    }

    //Spring also gives us the option to set global date-time formats via the application properties file.
    @GetMapping("/{startDate}/{endDate}")
    public Integer nbContratsValides(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                     @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate endDate){
        return contratService.nbContratsValides(startDate,endDate);
    }

}


