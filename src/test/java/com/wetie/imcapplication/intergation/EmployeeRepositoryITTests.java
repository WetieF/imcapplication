package com.wetie.imcapplication.intergation;

import com.wetie.imcapplication.dto.EmployeeDto;
import com.wetie.imcapplication.entity.Employee;
import com.wetie.imcapplication.mapper.EmployeeMapper;
import com.wetie.imcapplication.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// will load only spring data jpa component, to test the persistence layer component that will autoconfigure in-memory embedded DB.
// scans @Entity classes and configures spring data jpa repositories annotated with @Repository.
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryITTests {

    @Autowired   // on doit le declarer @Autowired ici au lieu de @AllArgsConstructor(retourne la faute).
    private EmployeeRepository employeeRepository;

    private EmployeeDto employeeDto;

    @BeforeEach
    public void setup() {
        employeeDto = EmployeeDto.builder()
                .firstName("Mensih")
                .lastName("Wetie")
                .email("mensih@yahoo.de").build();
    }

    // JUnit test for save employee operation
    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {

        // given -precondition or setup
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);

        // when - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        // then - verify the output
        Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo("Mensih");
    }

    // JUnit test for find all employees operation
    @DisplayName("JUnit test for find all employees operation")
    @Test
    public void givenListEmployees_whenFindAll_thenReturnListEmployees() {

        // given -precondition or setup
        EmployeeDto employeeDto1 = EmployeeDto.builder()
                .firstName("Nepkiah")
                .lastName("Wetie")
                .email("nepkiah@yahoo.de").build();

        Employee employee = EmployeeMapper.mapToEmployee(this.employeeDto);
        Employee employee1 = EmployeeMapper.mapToEmployee(employeeDto1);

        List<Employee> employees = Arrays.asList(employee, employee1);
        employeeRepository.saveAll(employees);

        // when - action or behaviour that we are going to test
        List<Employee> employeeList = employeeRepository.findAll();

        // then - verify the output
        Assertions.assertThat(employeeList).isNotNull();
    }

    // JUnit test for update employee operation
    @DisplayName("JUnit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        // given -precondition or setup
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee employeeSaved = employeeRepository.save(employee);

        // when - action or behaviour that we are going to test
        Employee saveEmployee = employeeRepository.findById(employeeSaved.getId()).get();
        saveEmployee.setFirstName("Mensih");
        saveEmployee.setLastName("Wetie");
        saveEmployee.setEmail("mensih@gmail.de");
        Employee updatedEmployee = employeeRepository.save(saveEmployee);

        // then - verify the output
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Mensih");
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("mensih@gmail.de");
    }

    // JUnit test for delete employee operation
    @DisplayName("JUnit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {

        // given -precondition or setup
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        employeeRepository.save(employee);

        // when - action or behaviour that we are going to test
        //employeeRepository.delete(employee);  // marche aussi
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOpt = employeeRepository.findById(employee.getId());

        // then - verify the output
        Assertions.assertThat(employeeOpt).isEmpty();
    }

    // JUnit test for get employee by id operation
    @DisplayName("JUnit test for get employee by id operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployee() {

        // given -precondition or setup
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);

        // when - action or behaviour that we are going to test
        Employee employeeFindById = employeeRepository.findById(savedEmployee.getId()).get();

        // then - verify the output
        Assertions.assertThat(employeeFindById).isNotNull();
    }

    // JUnit test for get employee by email operation
    @DisplayName("JUnit test for get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {

        // given -precondition or setup
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        employeeRepository.save(employee);

        // when - action or behaviour that we are going to test
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        // then - verify the output
        Assertions.assertThat(employeeDB).isNotNull();
        Assertions.assertThat(employeeDB.getEmail()).isEqualTo("mensih@yahoo.de");
    }
}
