package dk.cphbusiness.persistence.daos;

import dk.cphbusiness.persistence.dtos.EmployeeDTO;
import dk.cphbusiness.persistence.entities.Address;
import dk.cphbusiness.persistence.entities.Employee;

import java.util.Set;

public interface IEmployeeDAO {
    Set<Employee> getEmployeesByName(String name);
    Set<Employee> getEmployeesByAddress(Address a);
    Set<Employee> getEmployeesByzip(int zip);
    Set<EmployeeDTO> getAllEmployeeDTOs();
    Set<Employee> getAllEmployeesWithSalaryBelow(double amount);
}
