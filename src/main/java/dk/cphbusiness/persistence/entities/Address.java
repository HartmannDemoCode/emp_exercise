package dk.cphbusiness.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Purpose:
 *
 * @author: Thomas Hartmann
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;
    private int streetNumber;
    private int zip;
    private String city;
    @OneToMany(mappedBy="address")
    private Set<Employee> employees = new HashSet();

    public void addEmployee(Employee emp){
        if(employees == null)
            this.employees = new HashSet();
        this.employees.add(emp);
        emp.setAddress(this);
    }
    public void removeEmployee(Employee emp){
        this.employees.remove(emp);
        emp.setAddress(null);
    }

    @Override
    public String toString(){
        return "{"+
            this.street + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id) && Objects.equals(street, address.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, street);
    }
}
