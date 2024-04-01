package com.wetie.imcapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wetie.imcapplication.dto.EmployeeDto;

import com.wetie.imcapplication.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTests {

    @MockBean // create an register it in appl context
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc; // to call REST API

    @Autowired  //to serialize and deserialize java object
    private ObjectMapper objectMapper;

    EmployeeDto employeeDto;

    @BeforeEach
    public void setup() {
        employeeDto = EmployeeDto.builder()
                .firstName("Kiki").lastName("Mendji")
                .email("kiki@yahoo.fr").build();
    }


    // JUnit test for saving employee method
    @DisplayName("JUnit test for saving employee method")
    @Test
    public void givenEmployeeObject_whenSavedEmployee_thenReturnEmployee() throws Exception {

        // given -precondition or setup
        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(EmployeeDto.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

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
       EmployeeDto employeeDto1 = EmployeeDto.builder()
               .firstName("Nepkiah").lastName("Wetie")
               .email("nepkiah@gmx.de").build();

       List<EmployeeDto> employeeDtos = List.of(employeeDto, employeeDto1);

        BDDMockito.given(employeeService.searchAllEmployee()).willReturn(employeeDtos);//willReturn(List.of(employeeDto, employeeDto1));

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(employeeDtos.size())));
    }

    // JUnit test for get employee by id method positive scenario
    @DisplayName("JUnit test for get employee by id method positive scenario")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() throws Exception {

        // given -precondition or setup
        // Define the employee ID for the test
        Long employeeId = 1L;

        //EmployeeDto mockEmployee = BDDMockito.mock(EmployeeDto.class);

        // Mocking the service behavior to return an Optional containing a specific Employee instance
        //when(employeeService.findById(employeeId)).thenReturn(Optional.of(employee));
        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employeeDto));  // -> mockEmployee

        // when - action or behaviour that we are going to test
        // Performing an HTTP GET request to retrieve an employee by ID
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto))); // -> mockEmployee

        // then - verify the output
        // Asserting the response expectations
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employeeDto.getLastName())))  // -> mockEmployee
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employeeDto.getFirstName())));  // -> mockEmployee
    }

    // JUnit test for get employee by id method negative scenario
    @DisplayName("JUnit test for get employee by id method negative scenario")
    @Test
    public void givenInvalidEmployeeId_whenFindById_thenReturnEmpty() throws Exception {

        // given -precondition or setup
        // Define the employee ID for the test
        Long employeeId = 1L;

        // Mocking the service behavior to return an Optional containing a specific Employee instance
        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());  // -> mockEmployee

        // when - action or behaviour that we are going to test
        // Performing an HTTP GET request to retrieve an employee by ID
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto))); // -> mockEmployee

        // then - verify the output
        // Asserting the response expectations
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    // JUnit test for update employee REST API positive scenario
   /* @DisplayName("JUnit test for update employee REST API positive scenario")    //   1
    @Test
    public void givenUpdateEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {

        // given -precondition or setup
        Long employeeId = 1L;

        EmployeeDto updateEmployee = EmployeeDto.builder()
                .firstName("Kiki").lastName("Mendji")
                .email("kiki@yahoo.fr").build();

        Employee savedEmployee = Employee.builder()
                .firstName("Simon").lastName("Mbietie")
                .email("simon@hotmail.com").build();

        BDDMockito.given(employeeService.updateEmployee(employeeId, employeeDto)).willReturn(employeeDto);
        *//*BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));*//*

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employeeDto.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employeeDto.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$email",
                        CoreMatchers.is(updateEmployee.getEmail())));
    }*/

    // JUnit test for update employee REST API positive scenario
    @DisplayName("JUnit test for update employee REST API positive scenario")
    @Test      //  2
    public void EmployeeController_UpdateEmployee_ReturnEmployeeDto() throws Exception {
        // given -precondition or setup
        Long employeeId = 1L;

        EmployeeDto savedEmployee = EmployeeDto.builder()
                .firstName("nana").lastName("toto")
                .email("nana@yahoo.fr").build();

        EmployeeDto updatedEmployee = EmployeeDto.builder()
                .firstName("nono").lastName("nana")
                .email("nono@yahoo.fr").build();

        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        BDDMockito.given(employeeService.updateEmp(ArgumentMatchers.any(EmployeeDto.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or behaviour that we are going to test
        // Performing an HTTP PUT request to update an employee
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employeeId)
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

        EmployeeDto savedEmployee = EmployeeDto.builder()
                .firstName("nana").lastName("toto")
                .email("nana@yahoo.fr").build();

        EmployeeDto updatedEmployee = EmployeeDto.builder()
                .firstName("nono").lastName("nana")
                .email("nono@yahoo.fr").build();

        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        BDDMockito.given(employeeService.updateEmp(ArgumentMatchers.any(EmployeeDto.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

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
        Long employeeId = 1L;
        BDDMockito.willDoNothing().given(employeeService).deleteEmployee(employeeId);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

}
