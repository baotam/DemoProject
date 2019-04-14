package com.demoapp.service;

import com.demoapp.domain.Employee;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    public boolean hasAccess(Employee employee) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String principalUsername = ((UserDetails) principal).getUsername();
            return employee.getFirstName().equals(principalUsername) || principalUsername.equals("admin");
        }
        return false;
    }

    public boolean isAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String principalUsername = ((UserDetails) principal).getUsername();
            return principalUsername.equals("admin");
        }
        return false;
    }

}
