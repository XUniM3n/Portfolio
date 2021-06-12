package com.infinibet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
public class EventSuggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "person_id", nullable = false)
    @ManyToOne
    private Person person;

    @Column
    @NotNull
    private String title;

    @Column(length = 3000)
    private String description;

    @Temporal(TemporalType.DATE)
    private Date submitDate;

    public EventSuggestion(Person person, String title, String description, Date submitDate) {
        this.person = person;
        this.title = title;
        this.description = description;
        this.submitDate = submitDate;
    }
}
