package com.gui.estoqueAPI.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "estoques")
@Data
public class Estoque {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String quantidade;

}
