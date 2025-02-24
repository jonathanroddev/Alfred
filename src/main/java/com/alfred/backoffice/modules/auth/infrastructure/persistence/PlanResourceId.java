package com.alfred.backoffice.modules.auth.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanResourceId implements Serializable {

    @Column(name = "plan", nullable = false)
    private String planName;

    @Column(name = "resource", nullable = false)
    private String resourceName;
}
