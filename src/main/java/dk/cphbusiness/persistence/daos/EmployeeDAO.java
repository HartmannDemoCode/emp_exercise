package dk.cphbusiness.persistence.daos;

import dk.cphbusiness.persistence.HibernateConfig;
import dk.cphbusiness.persistence.entities.Address;
import dk.cphbusiness.persistence.entities.Employee;
import jakarta.persistence.*;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Purpose:
 *
 * @author: Thomas Hartmann
 */
public class EmployeeDAO implements IDAO<Employee> {
    EntityManagerFactory emf;
    public EmployeeDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    @Override
    public Employee create(Employee emp) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Address address = emp.getAddress();
            if(address != null){
                String street = address.getStreet();
                TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a WHERE a.street = :street", Address.class);
                query.setParameter("street", street);
                try {
                    Address found = query.getSingleResult();
                    emp.setAddress(found);
                } catch(NoResultException e){
                    em.persist(address);
                }
            }
            em.persist(emp);
            em.getTransaction().commit();
        }
        return emp;
    }

    @Override
    public Employee getById(Long id) {
        try(EntityManager em = emf.createEntityManager()){
            return em.find(Employee.class, id);
        }
    }

    @Override
    public Set<Employee> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            return em.createQuery("SELECT e FROM Employee e", Employee.class)
                    .getResultStream()
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public Employee update(Employee employee) {
        try(EntityManager em = emf.createEntityManager()){
            Employee found = em.find(Employee.class, employee.getId());
            if(found == null)
                throw new EntityNotFoundException("No entity with that id");

            em.getTransaction().begin();
            if(employee.getName()!=null)
                found.setName(employee.getName());
            if(employee.getAddress()!=null) {
                if (employee.getAddress().getId() != null) {
                    Address merged = em.merge(employee.getAddress());
                    found.setAddress(merged);
                } else {
                    em.persist(employee.getAddress());
                    found.setAddress(employee.getAddress());
                }
            }
            em.getTransaction().commit();
        }
        return employee;
    }

    @Override
    public void delete(Employee emp) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.remove(emp);
            em.getTransaction().commit();
        }
    }


    public static void main(String[] args) {
        EmployeeDAO dao = new EmployeeDAO(HibernateConfig.getEntityManagerFactory());
//        Employee e1 = Employee.builder()
//                .name("Henriette")
//                .build();
//        Address a1 = Address.builder()
//                .street("Rolighedsvej 25")
//                .build();
//
//        a1.addEmployee(e1);
//        dao.create(e1);
//        Employee foundEmp = dao.getById(1l);
//        foundEmp.setName("Margrethe");
//        dao.update(foundEmp);
    }
}
