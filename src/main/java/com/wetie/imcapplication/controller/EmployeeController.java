package com.wetie.imcapplication.controller;

import com.wetie.imcapplication.dto.EmployeeDto;
import com.wetie.imcapplication.exception.EmployeeNotFoundException;
import com.wetie.imcapplication.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")  // server.servlet.context-path=/api, ceci pour mettre /api/avant employees
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        EmployeeDto saveEmployee = employeeService.saveEmployee(employeeDto);
        return new ResponseEntity<>(saveEmployee, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> searchAllEmployee() {
        List<EmployeeDto> employeeDtoList = employeeService.searchAllEmployee();
        return ResponseEntity.ok(employeeDtoList);
    }

    /*@GetMapping("{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") Long employeeId) {
        EmployeeDto employeeById = employeeService.getEmployeeById(employeeId).get();
        return ResponseEntity.ok(employeeById);
    }*/

    @GetMapping("{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") Long employeeId) {
        return employeeService.getEmployeeById(employeeId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*@PutMapping("{id}")      //    1
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") Long employeeId, @RequestBody EmployeeDto employeeDto) {
        EmployeeDto updateEmployee = employeeService.updateEmployee(employeeId, employeeDto);
        return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
    }*/

    @PutMapping("{id}")      //    2
    public ResponseEntity<EmployeeDto> update(@PathVariable("id") Long employeeId, @RequestBody EmployeeDto employeeDto) {
        return employeeService.getEmployeeById(employeeId)
                .map(savedEmployee -> {
                    savedEmployee.setFirstName(employeeDto.getFirstName());
                    savedEmployee.setLastName(employeeDto.getLastName());
                    savedEmployee.setEmail(employeeDto.getEmail());

                    EmployeeDto updatedEmployee = employeeService.updateEmp(savedEmployee);

                    return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);

                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId) {
        employeeService.deleteEmployee(employeeId);

        return new ResponseEntity<String>("Employee deleted successfully", HttpStatus.OK);
    }

}
