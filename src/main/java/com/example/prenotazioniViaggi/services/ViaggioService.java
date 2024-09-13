package com.example.prenotazioniViaggi.services;

import com.cloudinary.Cloudinary;
import com.example.prenotazioniViaggi.entities.Dipendente;
import com.example.prenotazioniViaggi.entities.Viaggio;
import com.example.prenotazioniViaggi.enums.StatoViaggio;
import com.example.prenotazioniViaggi.exceptions.BadRequestException;
import com.example.prenotazioniViaggi.exceptions.NotFoundException;
import com.example.prenotazioniViaggi.payloads.ViaggioDTO;
import com.example.prenotazioniViaggi.repositories.DipendenteRepository;
import com.example.prenotazioniViaggi.repositories.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ViaggioService {
    @Autowired
    private ViaggioRepository viaggioRepository;
    @Autowired
    private Cloudinary cloudinaryUploader;

    public Page<Viaggio> findAll(int page, int size, String sortBy){
        if(page > 100) page = 100;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.viaggioRepository.findAll(pageable);
    }

    public Viaggio save(ViaggioDTO viaggioDTO){
        try {
            StatoViaggio statoViaggio = StatoViaggio.valueOf(viaggioDTO.stato().toUpperCase());
            Viaggio viaggio = new Viaggio(viaggioDTO.destinazione(),viaggioDTO.dataViaggio(), statoViaggio);

            return this.viaggioRepository.save(viaggio);
        } catch (Exception e) {
            // Gestisci l'errore, ad esempio lanciando un'eccezione personalizzata o restituendo un messaggio d'errore
            throw new BadRequestException("Errore: lo stato specificato non esiste.");
            // Puoi anche lanciare un'eccezione personalizzata, oppure restituire un valore di default.
        }

    }

    public Viaggio findById(UUID viaggioId){
        return this.viaggioRepository.findById(viaggioId).orElseThrow(() -> new NotFoundException(viaggioId));

    }
    public Viaggio findByIdAndUpdate(UUID  viaggioId, ViaggioDTO updatedViaggioDTO){
        Viaggio found = findById(viaggioId);
        try {
            StatoViaggio statoViaggio = StatoViaggio.valueOf(updatedViaggioDTO.stato().toUpperCase());
            found.setDestinazione(updatedViaggioDTO.destinazione());
            found.setDataViaggio(updatedViaggioDTO.dataViaggio());
            found.setStato(statoViaggio);

            return this.viaggioRepository.save(found);
        }catch (BadRequestException e) {
            // Gestisci l'errore, ad esempio lanciando un'eccezione personalizzata o restituendo un messaggio d'errore
            throw new BadRequestException("Errore: lo stato specificato non esiste.");
            // Puoi anche lanciare un'eccezione personalizzata, oppure restituire un valore di default.
        }

    }
    public void findByIdAndDelete(UUID  viaggioId){
        Viaggio found = findById(viaggioId);
        this.viaggioRepository.delete(found);
    }

}
