package com.example.prenotazioniViaggi.services;


import com.cloudinary.Cloudinary;
import com.example.prenotazioniViaggi.entities.Dipendente;
import com.example.prenotazioniViaggi.entities.Prenotazione;
import com.example.prenotazioniViaggi.entities.Viaggio;
import com.example.prenotazioniViaggi.enums.StatoViaggio;
import com.example.prenotazioniViaggi.exceptions.BadRequestException;
import com.example.prenotazioniViaggi.exceptions.NotFoundException;
import com.example.prenotazioniViaggi.payloads.PrenotazioneDTO;
import com.example.prenotazioniViaggi.payloads.ViaggioDTO;
import com.example.prenotazioniViaggi.repositories.DipendenteRepository;
import com.example.prenotazioniViaggi.repositories.PrenotazioneRepository;
import com.example.prenotazioniViaggi.repositories.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PrenotazioneService {
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private  ViaggioRepository viaggioRepository;


    public Page<Prenotazione> findAll(int page, int size, String sortBy){
        if(page > 100) page = 100;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.prenotazioneRepository.findAll(pageable);
    }

    public Prenotazione save(PrenotazioneDTO prenotazioneDTO){

        Dipendente dipendente = dipendenteRepository.findById(prenotazioneDTO.dipendente()).orElseThrow(() ->  new NotFoundException(prenotazioneDTO.dipendente()));
        Viaggio viaggio = viaggioRepository.findById(prenotazioneDTO.viaggio()).orElseThrow(() ->  new NotFoundException(prenotazioneDTO.viaggio()));

        prenotazioneRepository.findByDipendenteAndViaggioDataViaggio(dipendente,viaggio.getDataViaggio()).ifPresent(
                // 1.1 Se lo è triggero un errore (400 Bad Request)
                prenotazione -> {
                    throw new BadRequestException("Hai già un viaggio prenotato per la data " + viaggio.getDataViaggio());
                }
        );



        Prenotazione prenotazione = new Prenotazione(prenotazioneDTO.info(),dipendente,viaggio);

        return this.prenotazioneRepository.save(prenotazione);

    }

    public Prenotazione findById(UUID prenotazioneId){
        return this.prenotazioneRepository.findById(prenotazioneId).orElseThrow(() -> new NotFoundException(prenotazioneId));
    }

    public Prenotazione findByIdAndUpdate(UUID  prenotazioneId, PrenotazioneDTO updatedPrenotazioneDTO){
        Prenotazione found = findById(prenotazioneId);
        Dipendente dipendente = dipendenteRepository.findById(updatedPrenotazioneDTO.dipendente()).orElseThrow(() ->  new NotFoundException(updatedPrenotazioneDTO.dipendente()));
        Viaggio viaggio = viaggioRepository.findById(updatedPrenotazioneDTO.viaggio()).orElseThrow(() ->  new NotFoundException(updatedPrenotazioneDTO.viaggio()));


        found.setInfo(updatedPrenotazioneDTO.info());
        found.setDipendente(dipendente);
        found.setViaggio(viaggio);

        return this.prenotazioneRepository.save(found);
    }
    public void findByIdAndDelete(UUID  prenotazioneId){
        Prenotazione found = findById(prenotazioneId);
        this.prenotazioneRepository.delete(found);
    }
}
