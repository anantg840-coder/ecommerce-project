package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(mappedBy = "payment",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Order order;

    @NotBlank
    @Size(min = 2,message = "payment method must at least contain 2 characters")
    private String paymentMethod;

    private String pgPaymentId;
    private  String pgStatus;
    private String pgResponseMessage;
    private String pgName;

    public Payment(String pgName, String pgResponseMessage, String pgStatus, String pgPaymentId, String paymentMethod) {
        this.pgName = pgName;
        this.pgResponseMessage = pgResponseMessage;
        this.pgStatus = pgStatus;
        this.pgPaymentId = pgPaymentId;
        this.paymentMethod = paymentMethod;
    }
}
