package kz.symtech.antifraud.coreservice.entities.summary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kz.symtech.antifraud.models.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Product extends BaseEntity {

    private Long id;

    private String name;

    private Long price;

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
