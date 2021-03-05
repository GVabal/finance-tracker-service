package dev.vabalas.financetrackerservice.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "entry")
public class Entry {
    @Id
    @GeneratedValue(generator = "simple")
    @GenericGenerator(name = "simple", strategy = "uuid2")
    private String id;

    @Enumerated(EnumType.STRING)
    private EntryType type;

    private LocalDateTime time;
    private String category;
    private BigDecimal amount;

    public Entry(EntryType type, LocalDateTime time, String category, BigDecimal amount) {
        this.type = type;
        this.time = time;
        this.category = category;
        this.amount = amount;
    }

    public void setId(String id) {
        this.id = id;
    }
}