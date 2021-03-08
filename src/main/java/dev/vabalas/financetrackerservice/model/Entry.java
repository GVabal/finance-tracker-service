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

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EntryType entryType;

    private LocalDateTime time;
    private String category;
    private BigDecimal amount;

    public Entry(EntryType entryType, LocalDateTime time, String category, BigDecimal amount) {
        this.entryType = entryType;
        this.time = time;
        this.category = category;
        this.amount = amount;
    }

    public void setId(String id) {
        this.id = id;
    }
}
