package com.sm.orm.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable=false)
    private String name;

    @NotNull
    @Column(nullable=false)
    private Integer age;

    @NotNull
    @Column(nullable=false)
    private String department;

}
