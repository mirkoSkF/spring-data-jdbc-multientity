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
		// 1. Recupera il dipendente esistente con tutto il suo albero
		return repository.findById(id).map(dipendente -> {

			// 2. Aggiorna solo i campi nativi dell'anagrafica
			dipendente.setNome(dto.nome());
			dipendente.setCognome(dto.cognome());
			dipendente.setCodiceFiscale(dto.codiceFiscale());
			dipendente.setGenere(dto.genere());
			dipendente.setDataDiNascita(dto.dataDiNascita());
			dipendente.setLuogoNascita(dto.luogoNascita());

			// 3. Ruolo
			if (dto.ruoloId() != null) {
				dipendente.setRuoloId(AggregateReference.to(dto.ruoloId()));
			} else {
				dipendente.setRuoloId(null);
			}

			// 4. Gestione Account per evitare l'auto-incremento selvaggio
			if (dto.account() != null) {
				if (dipendente.getAccount() != null) {
					// Se l'account esisteva già, mantieni l'ID originale e aggiorna solo i dati interni
					dto.account().setId(dipendente.getAccount().getId());
				}
				dipendente.setAccount(dto.account());
			} else {
				dipendente.setAccount(null);
			}

			// 5. Aggiorna le collezioni (Svuota e ripopola quelle esistenti dell'aggregato persistito)
			dipendente.getContatti().clear();
			if (dto.contatti() != null) {
				for (ContattoDTO contattoDto : dto.contatti()) {
					dipendente.getContatti().add(new Contatto(
							AggregateReference.to(contattoDto.tipoContattoId()), 
							contattoDto.valore()
							));
				}
			}

			dipendente.getTitoliStudio().clear();
			if (dto.titoliStudio() != null) {
				for (Long titoloId : dto.titoliStudio()) {
					dipendente.getTitoliStudio().add(new DipendenteTitolo(
							AggregateReference.to(titoloId)
							));
				}
			}

			// 6. Salva l'aggregato modificato
			Dipendente aggiornato = repository.save(dipendente);
			return ResponseEntity.ok(aggiornato);

		}).orElse(ResponseEntity.notFound().build());
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
