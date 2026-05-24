package spring.crudJdbc.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("tipi_contatti")
public record TipoContatto(@Id Long id, String denominazione) {}