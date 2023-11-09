package tn.esprit.kaddemproject.controllers;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.esprit.kaddemproject.entities.Specialite;
import tn.esprit.kaddemproject.services.IUniversiteService;

import java.time.LocalDate;
import java.util.Map;


@RestController
@RequestMapping("/universite")
@AllArgsConstructor
public class UniversiteRestController{

    private IUniversiteService universiteService;

    @GetMapping("/{idUniv}/{startDate}/{endDate}")
    public Map<Specialite,Float> getMontantContartEntreDeuxDate(@PathVariable int idUniv,
                                                                @PathVariable @DateTimeFormat(pattern ="yyyy-MM-dd") LocalDate startDate,
                                                                @PathVariable @DateTimeFormat(pattern ="yyyy-MM-dd") LocalDate endDate){
        return universiteService.getMontantContartEntreDeuxDate(idUniv,startDate,endDate);
    }

    @PutMapping("/{idUniversite}/{idDepartement}")
    public void assignUniversiteToDepartement(@PathVariable Integer idUniversite,
                                              @PathVariable Integer idDepartement){
         universiteService.assignUniversiteToDepartement(idUniversite,idDepartement);
    }



}


