package com.wetie.imcapplication.service;

import com.wetie.imcapplication.dto.EmployeeDto;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    EmployeeDto saveEmployee(EmployeeDto employeeDto);

    List<EmployeeDto> searchAllEmployee();

    Optional<EmployeeDto> getEmployeeById(Long id);

    EmployeeDto createEmployee(EmployeeDto employeeDto);

    //EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto);

    EmployeeDto updateEmp(EmployeeDto employeeDto);

    void deleteEmployee(Long id);
}
