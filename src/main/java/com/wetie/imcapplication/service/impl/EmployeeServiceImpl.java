package com.wetie.imcapplication.service.impl;

import com.wetie.imcapplication.dto.EmployeeDto;
import com.wetie.imcapplication.entity.Employee;
import com.wetie.imcapplication.exception.EmployeeNotFoundException;
import com.wetie.imcapplication.mapper.EmployeeMapper;
import com.wetie.imcapplication.repository.EmployeeRepository;
import com.wetie.imcapplication.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        return EmployeeMapper.mapToEmployeeDto(employeeRepository
                .save(EmployeeMapper.mapToEmployee(employeeDto)));
    }

   /* @Override    //  1
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        return employeeRepository.findById(id)
                .map(employeeRecord -> {

                    //EmployeeMapper.mapToEmployee(employeeDto);
                    employeeRecord.setFirstName(employeeDto.getFirstName());
                    employeeRecord.setLastName(employeeDto.getLastName());
                    employeeRecord.setEmail(employeeDto.getEmail());

                    return EmployeeMapper.mapToEmployeeDto(employeeRepository.save(employeeRecord));
                }).orElseThrow(() -> new EmployeeNotFoundException("not found"));
    }*/

    @Override    //  2
    public EmployeeDto updateEmp(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        return EmployeeMapper.mapToEmployeeDto(employeeRepository.save(employee));
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.delete(employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee could not be deleted")));
    }

    /*
        @Override
    public StudentDto updateStudent(StudentDto studentDto, Long id) {

        Student student2 = studentRepository.findById(id)
                .map(student -> {

                    student.setFirstName(studentDto.firstName());
                    student.setLastName(studentDto.lastName());

                    return studentRepository.save(student);
                })
                .orElseThrow(() -> new NotFoundException("Student with id: " + id + " is not found"));

        StudentDto studentDto1 = StudentMapper.mapToStudentDto(student2);

        return studentDto1;
    }
     */

    @Override
    public List<EmployeeDto> searchAllEmployee() {

        List<Employee> employeeList = employeeRepository.findAll();

        List<EmployeeDto> employeeDtos = employeeList.stream()
                .map(employee -> EmployeeMapper.mapToEmployeeDto(employee)).collect(Collectors.toList());

        return employeeDtos;
    }

    @Override
    public Optional<EmployeeDto> getEmployeeById(Long id) {

        /*Employee employee = employeeRepository.findById(id).get();
        if (employee == null) {
            throw new EmployeeNotFoundException("");
        }
        return Optional.of(EmployeeMapper.mapToEmployeeDto(employee));*/

        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new EmployeeNotFoundException("Employee could not be found!"));

        return Optional.of(EmployeeMapper.mapToEmployeeDto(employee));
    }
}
