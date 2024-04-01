package com.wetie.imcapplication.service;

import com.wetie.imcapplication.dto.EmployeeDto;
import com.wetie.imcapplication.entity.Employee;
import com.wetie.imcapplication.exception.EmployeeNotFoundException;
import com.wetie.imcapplication.mapper.EmployeeMapper;
import com.wetie.imcapplication.repository.EmployeeRepository;
import com.wetie.imcapplication.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    EmployeeDto employeeDto;

    @BeforeEach
    public void setup() {
        employeeDto = EmployeeDto.builder()
                .firstName("Francis")
                .lastName("Wetie")
                .email("francis@yahoo.de").build();
    }

    // JUnit test for savedEmployee method
    // A test method to test the create method of the EmployeeService class.
    @DisplayName("JUnit test for saved employee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {

        // given -precondition or setup
        Employee employee = new Employee();    // (cas 1)
        // Using BeanUtils.copyProperties to copy properties from EmployeeDto to Employee for the test.
        BeanUtils.copyProperties(employeeDto, employee);

        //Employee employee = EmployeeMapper.mapToEmployee(employeeDto);  // (cas 2)

        // Mocking the employeeRepository.save method to return this instance of Employee whenever any instance of Employee is passed.
        BDDMockito.given(employeeRepository.save(Mockito.any(Employee.class))).willReturn(employee);
        //Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);

        // when - action or behaviour that we are going to test
        EmployeeDto response = employeeService.saveEmployee(this.employeeDto);

        // then - verify the output
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getFirstName()).isEqualTo(employeeDto.getFirstName());
    }

    // JUnit test for find all Employees method
    @DisplayName("JUnit test for find all Employees method")
    @Test
    public void givenEmployeeList_whenFindAll_thenReturnEmployees() {

        // given -precondition or setup
        EmployeeDto employeeDto1 = EmployeeDto.builder()
                .firstName("Mensih").lastName("Wetie")
                .email("mensih@gmail.de").build();

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee employee1 = EmployeeMapper.mapToEmployee(employeeDto1);
        List<Employee> employeeList = Arrays.asList(employee, employee1);

        BDDMockito.given(employeeRepository.findAll()).willReturn(employeeList);

        // when - action or behaviour that we are going to test
        List<EmployeeDto> dtoList = employeeService.searchAllEmployee();

        // then - verify the output
        Assertions.assertThat(dtoList).isNotNull();
        Assertions.assertThat(dtoList.size()).isEqualTo(2);
    }

    // JUnit test for find all employee method negative
    @DisplayName("JUnit test for find all Employees method negative scenario")
    @Test
    public void givenEmptyEmployeeList_whenFindAll_thenReturnEmptyEmployees() {

        // given -precondition or setup
        EmployeeDto employeeDto1 = EmployeeDto.builder()
                .firstName("Mensih").lastName("Wetie")
                .email("mensih@gmail.de").build();

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee employee1 = EmployeeMapper.mapToEmployee(employeeDto1);
        List<Employee> employeeList = Arrays.asList(employee, employee1);

        BDDMockito.given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when - action or behaviour that we are going to test
        List<EmployeeDto> dtoList = employeeService.searchAllEmployee();

        // then - verify the output
        Assertions.assertThat(dtoList).isEmpty();
        Assertions.assertThat(dtoList.size()).isEqualTo(0);
    }


    // JUnit test for update employee method
   /* @DisplayName("JUnit test for update employee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnEmployee() {

        // given -precondition or setup
        Long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Rebecca").lastName("Nana").email("rebecca@gmail.de").build();

        EmployeeDto employeeDto1 = EmployeeMapper.mapToEmployeeDto(employee);

        BDDMockito.given(employeeRepository.findById(employeeId)).willReturn(Optional.of(employee));
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        // when - action or behaviour that we are going to test
        EmployeeDto updateEmployee = employeeService.updateEmployee(employeeId, employeeDto1);

        // then - verify the output
        Assertions.assertThat(updateEmployee).isNotNull();
    }*/

    @DisplayName("JUnit test for update employee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnEmployee() {

        // given -precondition or setup
        Long employeeId = 1L;

        Employee employee = new Employee();
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setEmail(employeeDto.getEmail());

        BDDMockito.given(employeeRepository.save(Mockito.any(Employee.class))).willReturn(employee);

        // when - action or behaviour that we are going to test
        EmployeeDto updateEmployee = employeeService.updateEmp(employeeDto);

        // then - verify the output
        Assertions.assertThat(updateEmployee).isNotNull();
        Assertions.assertThat(updateEmployee.getFirstName()).isEqualTo("Francis");
    }


    // JUnit test for delete employee method
    @DisplayName("JUnit test for delete employee method")
    @Test
    public void givenEmployeeId_whenDelete_thenNothing() {

        // given -precondition or setup
        Long employeeId = 1L;
        Employee employee = Employee.builder().id(employeeId)
                .firstName("mensih").lastName("Wetie").email("mensih@yahoo.de").build();

        BDDMockito.given(employeeRepository.findById(employeeId)).willReturn(Optional.of(employee));
        BDDMockito.willDoNothing().given(employeeRepository).delete(employee);

        // when - action or behaviour that we are going to test
        employeeService.deleteEmployee(employeeId);

        // then - verify the output
        Mockito.verify(employeeRepository, Mockito.times(1)).delete(employee);
    }

    // JUnit test for delete employee by id method
    @DisplayName("JUnit test for delete employee by id method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnVoid() {

        // given -precondition or setup
        Long employeeId = 1L;
        Employee employee = Employee.builder()
                .id(employeeId).lastName("Aziz KAle")
                .lastName("Nana")
                .email("aziz@yahoo.de").build();

        // when - action or behaviour that we are going to test
        // When findById is invoked with the specified employeeId, it returns an Optional containing the employee.
        BDDMockito.given(employeeRepository.findById(employeeId)).willReturn(Optional.ofNullable(employee));

        // Configure the delete method to perform no action when called with an Employee object.
        BDDMockito.willDoNothing().given(employeeRepository).delete(employee);

        // Invoke the delete method of the employeeService with the created employeeId.
        employeeService.deleteEmployee(employeeId);

        // then - verify the output
        // Use assertAll to ensure that no exceptions are thrown during the execution of the delete method.
        Mockito.verify(employeeRepository, Mockito.times(1)).delete(employee);
    }

    // JUnit test for delete employee method
  /*  @DisplayName("JUnit test for delete employee method")
    @Test
    public void givenExistingEmployeeId_whenDelete_thenThrowsException() {

        // given -precondition or setup
        Long employeeId = 1L;
        Employee employee = Employee.builder().id(employeeId)
                .firstName("mensih").lastName("Wetie").email("mensih@yahoo.de").build();

        BDDMockito.given(employeeRepository.findById(employeeId)).willReturn(Optional.of(employee));
        BDDMockito.willDoNothing().given(employeeRepository).delete(employee);

        // when - action or behaviour that we are going to test
        org.junit.jupiter.api.Assertions.assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.deleteEmployee(employeeId);
        });

        // then - verify the output
        Mockito.verify(employeeRepository, Mockito.never()).delete(employee);
    }*/

    // JUnit test for get employee by id method
    @DisplayName("JUnit test for get employee by id method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {

        // given -precondition or setup
        Long employeeId = 1L;
        Employee mockEmployee = Mockito.mock(Employee.class);

        BDDMockito.given(employeeRepository.findById(employeeId)).willReturn(Optional.of(mockEmployee));

        // when - action or behaviour that we are going to test
        //Optional<EmployeeDto> optional = employeeService.getEmployeeById(employeeId);
        EmployeeDto savedEmployee = employeeService.getEmployeeById(employeeId).get();

        // then - verify the output
        //Assertions.assertThat(optional.isPresent()); // assert that the result is present assertSame(mockEmployee, result.get());
        Assertions.assertThat(savedEmployee).isNotNull();
    }
}



/*
    @Mock creates a mock. @InjectMocks creates an instance of the class and injects
    the mocks that are created with the @Mock (or @Spy) annotations into this instance.
    MockMvc is used for Spring MVC testing.
 */