package com.example.prenotazioniViaggi.repositories;

import com.example.prenotazioniViaggi.entities.Dipendente;
import com.example.prenotazioniViaggi.entities.Prenotazione;
import com.example.prenotazioniViaggi.entities.Viaggio;
import com.example.prenotazioniViaggi.payloads.PrenotazioneDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, UUID> {

    Optional<Prenotazione> findByDipendenteAndViaggioDataViaggio( Dipendente dipendente, LocalDate viaggioData);
}
