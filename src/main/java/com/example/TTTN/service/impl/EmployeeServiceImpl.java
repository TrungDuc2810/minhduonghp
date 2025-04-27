package com.example.TTTN.service.impl;

import com.example.TTTN.entity.Employee;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.EmployeeDto;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.repository.EmployeeRepository;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements GenericService<EmployeeDto> {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    private EmployeeDto mapToDto(Employee employee) {
        return modelMapper.map(employee, EmployeeDto.class);
    }

    private Employee mapToEntity(EmployeeDto employeeDto) {
        return modelMapper.map(employeeDto, Employee.class);
    }

    @Override
    public EmployeeDto getById(long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Employee", "id", String.valueOf(id)));
        return mapToDto(employee);
    }

    @Override
    public ListResponse<EmployeeDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<Employee> employees = employeeRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(employees, this::mapToDto);
    }

    @Override
    @Transactional
    public EmployeeDto create(EmployeeDto employeeDto) {
        Employee employee = mapToEntity(employeeDto);
        return mapToDto(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public EmployeeDto update(long id, EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", String.valueOf(id)));

        employee.setFullname(employeeDto.getFullname());
        employee.setPhoneNumber(employeeDto.getPhoneNumber());
        employee.setAddress(employeeDto.getAddress());

        return mapToDto(employeeRepository.save(employee));
    }

    @Override
    public void delete(long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", String.valueOf(id)));

        employeeRepository.delete(employee);
    }
}
