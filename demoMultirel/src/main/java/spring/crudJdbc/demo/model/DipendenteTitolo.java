package spring.crudJdbc.demo.model;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

@Table("dipendenti_titoli")
public class DipendenteTitolo {

    @Column("titolo_studio_id")
    private AggregateReference<TitoloStudio, Long> titoloStudioId;

    public DipendenteTitolo() {}

    public DipendenteTitolo(AggregateReference<TitoloStudio, Long> titoloStudioId) {
        this.titoloStudioId = titoloStudioId;
    }

    public AggregateReference<TitoloStudio, Long> getTitoloStudioId() { return titoloStudioId; }
    public void setTitoloStudioId(AggregateReference<TitoloStudio, Long> titoloStudioId) { this.titoloStudioId = titoloStudioId; }
}