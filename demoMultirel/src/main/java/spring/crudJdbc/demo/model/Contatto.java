package spring.crudJdbc.demo.model;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

@Table("contatti")
public class Contatto {

    @Column("tipo_contatto_id")
    private AggregateReference<TipoContatto, Long> tipoContattoId;
    private String valore;

    public Contatto() {}

    public Contatto(AggregateReference<TipoContatto, Long> tipoContattoId, String valore) {
        this.tipoContattoId = tipoContattoId;
        this.valore = valore;
    }

    public AggregateReference<TipoContatto, Long> getTipoContattoId() { return tipoContattoId; }
    public void setTipoContattoId(AggregateReference<TipoContatto, Long> tipoContattoId) { this.tipoContattoId = tipoContattoId; }
    public String getValore() { return valore; }
    public void setValore(String valore) { this.valore = valore; }
}