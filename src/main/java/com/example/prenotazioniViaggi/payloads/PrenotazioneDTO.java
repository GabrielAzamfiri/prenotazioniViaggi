package com.example.prenotazioniViaggi.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record PrenotazioneDTO(
        @NotEmpty(message = "Le info sono obbligatorie")
        @Size(min = 3, max = 100, message = "Le info  devono essere comprese tra 3 e 100 caratteri")
        String info,

        @NotNull(message = "Il dipendente è obbligatorio")
        String dipendente,

        @NotNull(message = "Il viaggio è obbligatorio")
        String viaggio
) {
}
