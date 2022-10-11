package net.nieled.rmmexercise.domain;

import lombok.*;
import net.nieled.rmmexercise.domain.enums.OS;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "os"})
})
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private BigDecimal cost;

    @Enumerated(EnumType.STRING)
    private OS os;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Service)) return false;
        return id != null && id.equals(((Service) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cost, os);
    }
}
