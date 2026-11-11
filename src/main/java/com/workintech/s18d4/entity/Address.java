package com.workintech.s18d4.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;

    @Column(name = "no")
    private String no;

    private String city;

    private String country;

    @Column(nullable = true)
    private String description;

    @OneToOne(mappedBy = "address")
    private Customer customer;
}
