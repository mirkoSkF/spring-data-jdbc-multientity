package spring.crudJdbc.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Table("dipendenti")
public class Dipendente {

    @Id
    private Long id;
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private Genere genere;
    private LocalDate dataDiNascita;
    private String luogoNascita;

    private AggregateReference<RuoloAziendale, Long> ruoloId;

    @MappedCollection(idColumn = "dipendente_id")
    private Account account;

    @MappedCollection(idColumn = "dipendente_id")
    private Set<Contatto> contatti = new HashSet<>();

    @MappedCollection(idColumn = "dipendente_id")
    private Set<DipendenteTitolo> titoliStudio = new HashSet<>();

    public Dipendente() {}

    // GETTER e SETTER Standard e Puliti (Usa sempre e solo i tipi corretti)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public String getCodiceFiscale() { return codiceFiscale; }
    public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }
    public Genere getGenere() { return genere; }
    public void setGenere(Genere genere) { this.genere = genere; }
    public LocalDate getDataDiNascita() { return dataDiNascita; }
    public void setDataDiNascita(LocalDate dataDiNascita) { this.dataDiNascita = dataDiNascita; }
    public String getLuogoNascita() { return luogoNascita; }
    public void setLuogoNascita(String luogoNascita) { this.luogoNascita = luogoNascita; }
    
    public AggregateReference<RuoloAziendale, Long> getRuoloId() { return ruoloId; }
    public void setRuoloId(AggregateReference<RuoloAziendale, Long> ruoloId) { this.ruoloId = ruoloId; }
    
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    
    public Set<Contatto> getContatti() { return contatti; }
    public void setContatti(Set<Contatto> contatti) { this.contatti = contatti; }
    
    public Set<DipendenteTitolo> getTitoliStudio() { return titoliStudio; }
    public void setTitoliStudio(Set<DipendenteTitolo> titoliStudio) { this.titoliStudio = titoliStudio; }
}