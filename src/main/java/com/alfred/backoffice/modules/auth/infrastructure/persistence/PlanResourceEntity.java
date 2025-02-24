package com.alfred.backoffice.modules.auth.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "plans_resources")
public class PlanResourceEntity {

    @EmbeddedId
    private PlanResourceId id;

    @ManyToOne(optional = false)
    @MapsId("planName")
    @JoinColumn(name = "plan", referencedColumnName = "name", nullable = false)
    private PlanEntity plan;

    @ManyToOne(optional = false)
    @MapsId("resourceName")
    @JoinColumn(name = "resource", referencedColumnName = "name", nullable = false)
    private ResourceEntity resource;

}
