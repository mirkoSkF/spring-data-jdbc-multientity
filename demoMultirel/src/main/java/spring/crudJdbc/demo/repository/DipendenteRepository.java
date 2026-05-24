package spring.crudJdbc.demo.repository;

import spring.crudJdbc.demo.model.Dipendente;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DipendenteRepository extends ListCrudRepository<Dipendente, Long> {
    // Spring Data JDBC genererà automaticamente tutte le query di insert, update, select e delete
    // gestendo a cascata le tabelle accounts, contatti e dipendenti_titoli.
}