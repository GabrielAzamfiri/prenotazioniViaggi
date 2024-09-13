package com.example.prenotazioniViaggi.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record PrenotazioneDTO(
        @NotEmpty(message = "Le info sono obbligatorie")
        @Size(min = 3, max = 40, message = "Il titolo deve essere compreso tra 3 e 40 caratteri")
        String info,

        @NotNull(message = "La data di nascita è obbligatoria")
        LocalDate dataRichiesta,

        @NotNull(message = "Il dipendente è obbligatorio")
        UUID dipendente,

        @NotNull(message = "Il viaggio è obbligatorio")
        UUID viaggio
) {
}
