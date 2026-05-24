package spring.crudJdbc.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("ruoli_aziendali")
public record RuoloAziendale(
    @Id 
    Long id, 
    String denominazione
) {}