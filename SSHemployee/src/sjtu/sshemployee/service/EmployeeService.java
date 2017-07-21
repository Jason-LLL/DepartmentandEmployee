package sjtu.sshemployee.service;

import sjtu.sshemployee.domain.Employee;
import sjtu.sshemployee.domain.PageBean;

public interface EmployeeService {
Employee login(Employee employee);
PageBean<Employee> findByPage(Integer currPage);
void save(Employee employee);
Employee findById(Integer eid);
void delete(Employee employee);
void update(Employee employee);
}
