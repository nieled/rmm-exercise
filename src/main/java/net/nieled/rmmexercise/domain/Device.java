package net.nieled.rmmexercise.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String systemName;

    @ManyToOne
    private DeviceType deviceType;

    @ManyToMany
    @JoinTable
    private Set<Service> services = new HashSet<>();

    @ManyToOne
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;
        return id != null && id.equals(((Device) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
