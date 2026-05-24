package spring.crudJdbc.demo.controller;

import spring.crudJdbc.demo.dto.ContattoDTO;
import spring.crudJdbc.demo.dto.DipendenteRequestDTO;
import spring.crudJdbc.demo.model.Contatto;
import spring.crudJdbc.demo.model.Dipendente;
import spring.crudJdbc.demo.model.DipendenteTitolo;
import spring.crudJdbc.demo.repository.DipendenteRepository;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dipendenti")
public class DipendenteController {

    private final DipendenteRepository repository;

    // Injection tramite costruttore
    public DipendenteController(DipendenteRepository repository) {
        this.repository = repository;
    }

    // READ ALL
    @GetMapping
    public List<Dipendente> getAll() {
        return repository.findAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Dipendente> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE (Riscritto per lavorare con il DTO in ingresso)
    @PostMapping
    public ResponseEntity<Dipendente> create(@RequestBody DipendenteRequestDTO dto) {
        Dipendente dipendente = new Dipendente();
        
        // 1. Mappatura campi nativi e primitivi
        dipendente.setNome(dto.nome());
        dipendente.setCognome(dto.cognome());
        dipendente.setCodiceFiscale(dto.codiceFiscale());
        dipendente.setGenere(dto.genere());
        dipendente.setDataDiNascita(dto.dataDiNascita());
        dipendente.setLuogoNascita(dto.luogoNascita());
        
        // 2. Mappatura della relazione 1-M (Ruolo)
        if (dto.ruoloId() != null) {
            dipendente.setRuoloId(AggregateReference.to(dto.ruoloId()));
        }
        
        // 3. Mappatura della relazione 1-1 (Account)
        dipendente.setAccount(dto.account());
        
        // 4. Mappatura della relazione M-M con dati extra (Contatti)
        if (dto.contatti() != null) {
            for (ContattoDTO contattoDto : dto.contatti()) {
                dipendente.getContatti().add(new Contatto(
                    AggregateReference.to(contattoDto.tipoContattoId()), 
                    contattoDto.valore()
                ));
            }
        }
        
        // 5. Mappatura della relazione M-M pura (Titoli di Studio tramite entità di giunzione)
        if (dto.titoliStudio() != null) {
            for (Long titoloId : dto.titoliStudio()) {
                dipendente.getTitoliStudio().add(new DipendenteTitolo(
                    AggregateReference.to(titoloId)
                ));
            }
        }
        
        // Persistenza tramite Spring Data JDBC
        Dipendente salvato = repository.save(dipendente);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvato);
    }
    
 // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Dipendente> update(@PathVariable Long id, @RequestBody DipendenteRequestDTO dto) {
        // 1. Verifica se il dipendente esiste a sistema
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // 2. Istanzia l'entità impostando l'ID della risorsa da aggiornare
        Dipendente dipendente = new Dipendente();
        dipendente.setId(id); // Questo ID dice a Spring Data JDBC di fare UPDATE e non INSERT

        // 3. Mappatura campi nativi e primitivi
        dipendente.setNome(dto.nome());
        dipendente.setCognome(dto.cognome());
        dipendente.setCodiceFiscale(dto.codiceFiscale());
        dipendente.setGenere(dto.genere());
        dipendente.setDataDiNascita(dto.dataDiNascita());
        dipendente.setLuogoNascita(dto.luogoNascita());

        // 4. Mappatura della relazione 1-M (Ruolo)
        if (dto.ruoloId() != null) {
            dipendente.setRuoloId(AggregateReference.to(dto.ruoloId()));
        }

        // 5. Mappatura della relazione 1-1 (Account)
        dipendente.setAccount(dto.account());

        // 6. Mappatura della relazione M-M con dati extra (Contatti)
        if (dto.contatti() != null) {
            for (ContattoDTO contattoDto : dto.contatti()) {
                dipendente.getContatti().add(new Contatto(
                    AggregateReference.to(contattoDto.tipoContattoId()), 
                    contattoDto.valore()
                ));
            }
        }

        // 7. Mappatura della relazione M-M pura (Titoli di Studio)
        if (dto.titoliStudio() != null) {
            for (Long titoloId : dto.titoliStudio()) {
                dipendente.getTitoliStudio().add(new DipendenteTitolo(
                    AggregateReference.to(titoloId)
                ));
            }
        }

        // 8. Esecuzione dell'aggiornamento (orchestrazione automatica dell'aggregato)
        Dipendente aggiornato = repository.save(dipendente);
        return ResponseEntity.ok(aggiornato);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}