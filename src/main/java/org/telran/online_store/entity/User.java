package org.telran.online_store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telran.online_store.enums.UserRole;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String phone;

    private String password;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "user_role", nullable = false)
//    private UserRole userRole = UserRole.CLIENT;
}
