package tn.esprit.kaddemproject.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.kaddemproject.entities.*;
import tn.esprit.kaddemproject.repositories.ContratRepository;
import tn.esprit.kaddemproject.repositories.UniversiteRepository;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IUniversiteServiceImplTest {
    @Mock
    private UniversiteRepository universiteBaseRepository;
    @Mock
    private ContratRepository contratRepository;
    @Mock
    private IDepartementServiceImpl departementService;


    private IUniversiteServiceImpl underTest;

    // This will run before each test
    @BeforeEach
    void setUp() {
        underTest = new IUniversiteServiceImpl(universiteBaseRepository, contratRepository, departementService);
    }

    // ensure that the method behaves as expected without modifying any data or throwing exceptions when universite and
    // departement exist
    @Test
    void assignUniversiteToDepartementWhenUniversiteAndDepartementExistTest() {
        //given
        Integer idUniversite = 1;
        Integer idDepartement = 1;
        Universite universite = new Universite(idUniversite, "Esprit", new ArrayList<>());
        Departement departement = new Departement(idDepartement, "DS", new ArrayList<>());

        when(universiteBaseRepository.findById(idUniversite)).thenReturn(Optional.of(universite));
        when(departementService.retrieveById(idDepartement)).thenReturn(departement);

        //when
        underTest.assignUniversiteToDepartement(idUniversite, idDepartement);

        //then
        assertTrue(universite.getDepartements().contains(departement));
    }

    // ensure that the method behaves as expected without modifying any data or throwing exceptions when universite exists and
    // departement does not exist
    @Test
    void assignUniversiteToDepartementWhenUniversiteExistsAndDepartementDoesNoExistTest() {
        //given
        Integer idUniversite = 1;
        Integer idDepartement = 1;
        Universite universite = new Universite(idUniversite, "Esprit", new ArrayList<>());

        when(universiteBaseRepository.findById(idUniversite)).thenReturn(Optional.of(universite));

        //when
        underTest.assignUniversiteToDepartement(idUniversite, idDepartement);

        //then
        assertTrue(universite.getDepartements().isEmpty());
    }

    // ensure that the method behaves as expected without modifying any data or throwing exceptions when universite does not exist and
    // departement exists
    @Test
    void assignUniversiteToDepartementWhenUniversiteDoesNotExistAndDepartementExistsTest() {
        //given
        Integer idUniversite = 1;
        Integer idDepartement = 1;

        Departement departement = new Departement(idDepartement, "DS", new ArrayList<>());

        when(departementService.retrieveById(idDepartement)).thenReturn(departement);

        //when
        underTest.assignUniversiteToDepartement(idUniversite, idDepartement);
    }

    // ensure that the method behaves as expected without modifying any data or throwing exceptions when universite and
    // departement do not exist
    @Test
    void assignUniversiteToDepartementWhenUniversiteAndDepartementDoNotExistTest() {
        //given
        Integer idUniversite = 1;
        Integer idDepartement = 1;

        //when
        underTest.assignUniversiteToDepartement(idUniversite, idDepartement);

    }


    // ensure that the method behaves as expected without modifying any data or throwing exceptions when universite exists
    @Test
    void retrieveDepartementsByUniversiteWhenIdUniversiteExistsTest() {
        //given
        Integer idUniversite = 1;
        List<Departement> departements = new ArrayList<>();
        departements.add(new Departement(1, "DS", new ArrayList<>()));
        departements.add(new Departement(2, "BI", new ArrayList<>()));
        Universite universite = new Universite(idUniversite, "ESPRIT", departements);

        when(universiteBaseRepository.findById(idUniversite)).thenReturn(Optional.of(universite));

        //when
        List<Departement> result = underTest.retrieveDepartementsByUniversite(idUniversite);

        //then
        assertEquals(result, departements);

    }

    // ensure that the method behaves as expected without modifying any data or throwing exceptions when universite does
    // not exist
    @Test
    void RetrieveDepartementsByUniversiteWhenIdUniversiteDoesNotExistTest() {
        //given
        Integer idUniversite = 1;

        //when
        List<Departement> departements = underTest.retrieveDepartementsByUniversite(idUniversite);
        //then
        assertNull(departements);
    }


    @Test
    void getContractsByDepartementTest() {

        // given
        Departement departement = new Departement(1, "informatique", new ArrayList<>());

        // when
        underTest.getContractsByDepartement(departement);

        // then
        // Check That mock was invoked with the same departement that we passed to underTest.
        ArgumentCaptor<Departement> departementArgumentCaptor = ArgumentCaptor.forClass(Departement.class);
        verify(contratRepository).findByArchiveIsFalseAndEtudiantDepartement(departementArgumentCaptor.capture());
        Departement capturedDepartement = departementArgumentCaptor.getValue();
        assertThat(capturedDepartement).isEqualTo(departement);

    }

    @Test
    void getMontantContartEntreDeuxDateWhenDepartementAndContractsNotNullTest() {
        //given
        int idUniv = 1;

        List<Etudiant> etudiants = new ArrayList<>();
        Etudiant etudiant = new Etudiant(1, "wissem", "boujlida", Option.NIDS, new ArrayList<>() , new Departement(), new ArrayList<>());

        List<Contrat> contrats = new ArrayList<>();
        contrats.add(new Contrat(1,
                LocalDate.of(2023, 5, 19),
                LocalDate.of(2024, 5, 19),
                Specialite.IA, false, 1, etudiant));
        contrats.add(new Contrat(2,
                LocalDate.of(2023, 5, 19),
                LocalDate.of(2024, 5, 19),
                Specialite.CLOUD, false, 1, etudiant));
        contrats.add(new Contrat(3,
                LocalDate.of(2023, 5, 19),
                LocalDate.of(2024, 5, 19),
                Specialite.SECURITE, false, 1, etudiant));
        etudiant.setContrats(contrats);

        etudiants.add(etudiant);

        List<Departement> departements = new ArrayList<>();
        Departement departement = new Departement(1, "informatique", new ArrayList<>());
        departement.setEtudiants(etudiants);
        departements.add(departement);

        Universite universite = new Universite(idUniv, "Esprit", departements);

        LocalDate startDate = LocalDate.of(2023, 5, 18);
        LocalDate endDate = LocalDate.of(2024, 5, 20);



        when(universiteBaseRepository.findById(idUniv)).thenReturn(Optional.of(universite));
        when(contratRepository.findByArchiveIsFalseAndEtudiantDepartement(departement)).thenReturn(contrats);

        //when
        Map<Specialite, Float> result = underTest.getMontantContartEntreDeuxDate(idUniv, startDate, endDate);

        Map<Specialite, Float> expected_result = new HashMap<>();
        for (Specialite specialite : Specialite.values()) {
            expected_result.put(specialite,0.0f);
        }
        expected_result.replace(Specialite.IA, 0.0f, 12.0f);
        expected_result.replace(Specialite.CLOUD, 0.0f, 12.0f);
        expected_result.replace(Specialite.SECURITE, 0.0f, 12.0f);
        //then
        assertEquals(result, expected_result);

    }

    @Test
    void getMontantContartEntreDeuxDateWhenDepartementNullTest() {

        //given
        int idUniv = 1;

        LocalDate startDate = LocalDate.of(2023, 5, 18);
        LocalDate endDate = LocalDate.of(2024, 5, 20);

        //when
        //then
        assertThatThrownBy(() -> underTest.getMontantContartEntreDeuxDate(idUniv, startDate, endDate)).isInstanceOf(NullPointerException.class);

    }

    @Test
    void getMontantContartEntreDeuxDate2WhenDepartementAndContractsNotNullTest() {
        //given
        int idUniv = 1;

        List<Etudiant> etudiants = new ArrayList<>();
        Etudiant etudiant = new Etudiant(1, "wissem", "boujlida", Option.NIDS, new ArrayList<>() , new Departement(), new ArrayList<>());

        List<Contrat> contrats = new ArrayList<>();
        contrats.add(new Contrat(1,
                LocalDate.of(2023, 5, 19),
                LocalDate.of(2024, 5, 19),
                Specialite.IA, false, 1, etudiant));
        contrats.add(new Contrat(2,
                LocalDate.of(2023, 5, 19),
                LocalDate.of(2024, 5, 19),
                Specialite.CLOUD, false, 1, etudiant));
        contrats.add(new Contrat(3,
                LocalDate.of(2023, 5, 19),
                LocalDate.of(2024, 5, 19),
                Specialite.SECURITE, false, 1, etudiant));
        etudiant.setContrats(contrats);

        etudiants.add(etudiant);

        List<Departement> departements = new ArrayList<>();
        Departement departement = new Departement(1, "informatique", new ArrayList<>());
        departement.setEtudiants(etudiants);
        departements.add(departement);

        Universite universite = new Universite(idUniv, "Esprit", departements);

        LocalDate startDate = LocalDate.of(2023, 5, 18);
        LocalDate endDate = LocalDate.of(2024, 5, 20);



        when(universiteBaseRepository.findById(idUniv)).thenReturn(Optional.of(universite));
        when(contratRepository.findByArchiveIsFalseAndEtudiantDepartement(departement)).thenReturn(contrats);

        //when
        Map<Specialite, Float> result = underTest.getMontantContartEntreDeuxDate2(idUniv, startDate, endDate);

        Map<Specialite, Float> expected_result = new HashMap<>();
        for (Specialite specialite : Specialite.values()) {
            expected_result.put(specialite,0.0f);
        }
        expected_result.replace(Specialite.IA, 0.0f, 12.0f);
        expected_result.replace(Specialite.CLOUD, 0.0f, 12.0f);
        expected_result.replace(Specialite.SECURITE, 0.0f, 12.0f);
        //then
        assertEquals(result, expected_result);

    }

    @Test
    void getMontantContartEntreDeuxDate2WhenDepartementNullTest() {

        //given
        int idUniv = 1;

        LocalDate startDate = LocalDate.of(2023, 5, 18);
        LocalDate endDate = LocalDate.of(2024, 5, 20);

        //when
        //then
        assertThatThrownBy(() -> underTest.getMontantContartEntreDeuxDate2(idUniv, startDate, endDate)).isInstanceOf(NullPointerException.class);

    }


    @Test
    void isContractBetweenWhenTrueTest1() {
        //  |**************************|
        //given
        Contrat contrat = new Contrat(1, LocalDate.of(2023, 5, 19),
                LocalDate.of(2024, 5, 19), Specialite.IA, false,
                200, new Etudiant());

        LocalDate startDate = LocalDate.of(2023, 5, 18);
        LocalDate endDate = LocalDate.of(2024, 5, 20);

        //when
        Boolean result = underTest.isContractBetween(contrat, startDate, endDate);

        //then
        assertTrue(result);
    }

    @Test
    void isContractBetweenWhenTrueTest2() {
        //  *****|**************************|*****
        //given
        Contrat contrat = new Contrat(1, LocalDate.of(2023, 5, 19),
                LocalDate.of(2024, 5, 19), Specialite.IA, false,
                200, new Etudiant());


        LocalDate startDate = LocalDate.of(2023, 5, 25);
        LocalDate endDate = LocalDate.of(2024, 5, 18);

        //when
        Boolean result = underTest.isContractBetween(contrat, startDate, endDate);

        //then
        assertTrue(result);
    }

    @Test
    void isContractBetweenWhenTrueTest3() {
        // *****|**************************|
        //given
        Contrat contrat = new Contrat(1, LocalDate.of(2023, 5, 19),
                LocalDate.of(2024, 5, 19), Specialite.IA, false,
                200, new Etudiant());

        LocalDate startDate = LocalDate.of(2023, 5, 20);
        LocalDate endDate = LocalDate.of(2024, 5, 15);

        //when
        Boolean result = underTest.isContractBetween(contrat, startDate, endDate);

        //then
        assertTrue(result);
    }

    @Test
    void isContractBetweenWhenFalseTest1() {
        //given
        Contrat contrat = new Contrat(1, LocalDate.of(2023, 5, 19),
                LocalDate.of(2024, 5, 19), Specialite.IA, false,
                200, new Etudiant());

        LocalDate startDate = LocalDate.of(2023, 5, 17);
        LocalDate endDate = LocalDate.of(2024, 5, 15);

        //when
        Boolean result = underTest.isContractBetween(contrat, startDate, endDate);

        //then
        assertFalse(result);
    }

    @Test
    void isContractBetweenWhenFalseTest2() {
        //given
        Contrat contrat = new Contrat(1, LocalDate.of(2023, 5, 19),
                LocalDate.of(2024, 5, 19), Specialite.IA, false,
                200, new Etudiant());

        LocalDate startDate = LocalDate.of(2022, 5, 17);
        LocalDate endDate = LocalDate.of(2023, 5, 15);

        //when
        Boolean result = underTest.isContractBetween(contrat, startDate, endDate);

        //then
        assertFalse(result);
    }

    @Test
    void isContractBetweenWhenFalseTest3() {
        //given
        Contrat contrat = new Contrat(1, LocalDate.of(2023, 5, 19),
                LocalDate.of(2024, 5, 19), Specialite.IA, false,
                200, new Etudiant());

        LocalDate startDate = LocalDate.of(2024, 5, 20);
        LocalDate endDate = LocalDate.of(2025, 5, 15);

        //when
        Boolean result = underTest.isContractBetween(contrat, startDate, endDate);

        //then
        assertFalse(result);
    }

    @Test
    void getMontantContractWhenNbJourBiggerThan0Test() {
        //given
        Contrat contrat = new Contrat(1, LocalDate.of(2023, 5, 19),
                LocalDate.of(2024, 4, 19), Specialite.IA, false,
                1, new Etudiant());
        LocalDate startDate = LocalDate.of(2023, 5, 24);
        LocalDate endDate = LocalDate.of(2024, 3, 20);

        //when
        long result = underTest.getMontantContract(contrat, startDate, endDate);

        //then
        assertEquals(10, result);
    }

    @Test
    void getMontantContractWhenNbJourLessThan0Test() {
        //given
        Contrat contrat = new Contrat(1, LocalDate.of(2023, 4, 19),
                LocalDate.of(2024, 8, 19), Specialite.IA, false,
                1, new Etudiant());
        LocalDate startDate = LocalDate.of(2022, 4, 19);
        LocalDate endDate = LocalDate.of(2023, 5, 18);

        //when
        long result = underTest.getMontantContract(contrat, startDate, endDate);

        //then
        assertEquals(0, result);
    }

}