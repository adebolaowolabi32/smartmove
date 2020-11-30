package com.interswitch.smartmoveserver.model;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;

/**
 * @author adebola.owolabi
 */
@Data
@Entity
@Table(name = "wallets")
@EntityListeners(AuditingEntityListener.class)
public class Wallet extends AuditEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private User owner;

    private double balance = 0;

    private boolean enabled;
}
