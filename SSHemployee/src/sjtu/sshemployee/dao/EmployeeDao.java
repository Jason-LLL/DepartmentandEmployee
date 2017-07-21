package sjtu.sshemployee.dao;

import java.util.List;

import sjtu.sshemployee.domain.Employee;

public interface EmployeeDao {
 Employee findByUsernameAndPassword(Employee employee);
 int findCount();
 List<Employee> findByPage(int begin,int pageSize);
 void save(Employee employee);
 Employee findById(Integer eid);
 void delete(Employee employee);
 void update(Employee employee);
}
