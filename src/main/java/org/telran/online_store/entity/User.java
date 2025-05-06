package org.telran.online_store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.telran.online_store.enums.UserRole;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")
    private String email;

    @NotBlank
    private String phone;
    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.CLIENT;
}
