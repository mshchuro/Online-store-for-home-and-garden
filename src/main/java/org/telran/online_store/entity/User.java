package org.telran.online_store.entity;

import jakarta.persistence.*;
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

    private String name;

    private String email;

    private String phone;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.CLIENT;
}
