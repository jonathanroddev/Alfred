package com.alfred.backoffice.modules.auth.infrastructure.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user_types")
public class UserTypeEntity {

    @Id
    @Column(name = "name", length = 50, nullable = false, updatable = false)
    @Size(max = 50, message = "Name must be at most 50 characters long.")
    @NotBlank(message = "Name cannot be blank.")
    @Pattern(regexp = "\\S.*\\S|\\S", message = "Name cannot contain only spaces.")
    private String name;

    @ManyToMany(mappedBy = "userTypes")
    private Set<UserEntity> users = new HashSet<>();

}
