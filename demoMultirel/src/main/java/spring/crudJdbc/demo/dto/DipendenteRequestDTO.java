package spring.crudJdbc.demo.dto;

import spring.crudJdbc.demo.model.Account;
import spring.crudJdbc.demo.model.Genere;
import java.time.LocalDate;
import java.util.List;

public record DipendenteRequestDTO(
    String nome,
    String cognome,
    String codiceFiscale,
    Genere genere,
    LocalDate dataDiNascita,
    String luogoNascita,
    Long ruoloId,
    Account account, // Se account non ha AggregateReference interni, va bene così
    List<ContattoDTO> contatti,
    List<Long> titoliStudio
) {}