package spring.crudJdbc.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("titoli_studio")
public class TitoloStudio {

    @Id
    private Long id;
    private String descrizione;

    // Costruttore vuoto richiesto da Spring Data JDBC
    public TitoloStudio() {}

    public TitoloStudio(Long id, String descrizione) {
        this.id = id;
        this.descrizione = descrizione;
    }

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
}