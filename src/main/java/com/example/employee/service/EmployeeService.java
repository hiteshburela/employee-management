package com.example.employee.service;
 
import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service
public class EmployeeService {
 
    private final EmployeeRepository repository;
 
    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }
 
    public Employee addEmployee(Employee employee) {
 
        // Lalithaa's validation
        if (employee.getName() == null || employee.getName().isBlank()) {
            throw new IllegalArgumentException("Employee name is required");
        }
 
        if (employee.getEmail() == null || employee.getEmail().isBlank()) {
            throw new IllegalArgumentException("Employee email is required");
        }
 
        return repository.save(employee);
    }
 
    public List<Employee> getEmployees() {
        return repository.findAll();
    }
 
    // Hitesh's Employee API functionality
    public Employee getEmployeeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }
}
