package com.wetie.imcapplication.testcontainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wetie.imcapplication.dto.EmployeeDto;
import com.wetie.imcapplication.entity.Employee;
import com.wetie.imcapplication.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationContainersTests extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        employeeRepository.deleteAll();
    }

    // JUnit test for saving employee method
    @DisplayName("JUnit test for saving employee method")
    @Test
    public void givenEmployeeObject_whenSavedEmployee_thenReturnEmployee() throws Exception {

        // given -precondition or setup
        EmployeeDto employeeDto = EmployeeDto.builder()
                .firstName("Kiki").lastName("Mendji")
                .email("kiki@yahoo.fr").build();

        // when - action or behaviour that we are going to test
        // Performing an HTTP POST request to create an employee
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employeeDto.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employeeDto.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employeeDto.getEmail())));
    }

    // JUnit test for find all employees method
    @DisplayName("JUnit test for find all employees method")
    @Test
    public void givenEmployeeList_whenFindAll_thenReturnEmployees() throws Exception {

        // given -precondition or setup
        List<Employee> employeeList = new ArrayList<>();

        Employee employee = Employee.builder()
                .firstName("Kiki").lastName("Mendji")
                .email("kiki@yahoo.fr").build();

        Employee employee1 = Employee.builder()
                .firstName("Nepkiah").lastName("Wetie")
                .email("nepkiah@gmx.de").build();

        employeeList.add(employee);
        employeeList.add(employee1);

        employeeRepository.saveAll(employeeList);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(employeeList.size())));
    }

    // JUnit test for get employee by id method positive scenario
    @DisplayName("JUnit test for get employee by id method positive scenario")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() throws Exception {

        // given -precondition or setup
        // Define the employee ID for the test
        Employee employee = Employee.builder()
                .firstName("Kiki").lastName("Mendji")
                .email("kiki@yahoo.fr").build();

        employeeRepository.save(employee);

        // when - action or behaviour that we are going to test
        // Performing an HTTP GET request to retrieve an employee by ID
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee))); // -> mockEmployee

        // then - verify the output
        // Asserting the response expectations
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))  // -> mockEmployee
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())));  // -> mockEmployee
    }

    // JUnit test for get employee by id method negative scenario
    @DisplayName("JUnit test for get employee by id method negative scenario")
    @Test
    public void givenInvalidEmployeeId_whenFindById_thenReturnEmpty() throws Exception {

        // given -precondition or setup
        // Define the employee ID for the test
        Long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Kiki").lastName("Mendji")
                .email("kiki@yahoo.fr").build();

        employeeRepository.save(employee);

        // when - action or behaviour that we are going to test
        // Performing an HTTP GET request to retrieve an employee by ID
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId));

        // then - verify the output
        // Asserting the response expectations
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    // JUnit test for update employee REST API positive scenario
    @DisplayName("JUnit test for update employee REST API positive scenario")
    @Test      //  2
    public void EmployeeController_UpdateEmployee_ReturnEmployeeDto() throws Exception {
        // given -precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("nana").lastName("toto")
                .email("nana@yahoo.fr").build();

        employeeRepository.save(savedEmployee);

        EmployeeDto updatedEmployee = EmployeeDto.builder()
                .firstName("nono").lastName("nana")
                .email("nono@yahoo.fr").build();

        // when - action or behaviour that we are going to test
        // Performing an HTTP PUT request to update an employee
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // Asserting the response expectations
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(updatedEmployee.getEmail())));
    }

    // JUnit test for update employee REST API negative scenario
    @DisplayName("JUnit test for update employee REST API negative scenario")
    @Test      //  2
    public void EmployeeController_UpdateEmployee_Return404() throws Exception {
        // given -precondition or setup
        Long employeeId = 1L;

        Employee savedEmployee = Employee.builder()
                .firstName("nana").lastName("toto")
                .email("nana@yahoo.fr").build();

        employeeRepository.save(savedEmployee);

        EmployeeDto updatedEmployee = EmployeeDto.builder()
                .firstName("nono").lastName("nana")
                .email("nono@yahoo.fr").build();

        // when - action or behaviour that we are going to test
        // Performing an HTTP PUT request to update an employee
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // Asserting the response expectations
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    // JUnit test for delete employee method
    @DisplayName("JUnit test for delete employee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {

        // given -precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("nana").lastName("toto")
                .email("nana@yahoo.fr").build();

        employeeRepository.save(savedEmployee);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", savedEmployee.getId()));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
