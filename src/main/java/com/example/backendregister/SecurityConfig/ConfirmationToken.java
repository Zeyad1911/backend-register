package com.example.backendregister.SecurityConfig;

import com.example.backendregister.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

        @SequenceGenerator(
                name = "user_seq", sequenceName = "user_token_sequence",
                initialValue = 1, allocationSize = 1)
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(nullable = false)
        private String token;
        @Column(nullable = false)
        private LocalDateTime createdAt;
        @Column(nullable = false)
        private LocalDateTime expiresAt;
        @Column
        private LocalDateTime confirmedAt;
        @ManyToOne
        @JoinColumn(
                nullable = false,
                name = "user_id"
        )
        private User user;

        public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt,
                                 User user
                                 ) {
                this.token = token;
                this.createdAt = createdAt;
                this.expiresAt = expiresAt;
                this.user = user;
        }


}