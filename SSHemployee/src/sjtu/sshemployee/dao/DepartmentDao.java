package sjtu.sshemployee.dao;

import java.util.List;

import sjtu.sshemployee.domain.Department;
import sjtu.sshemployee.domain.Employee;

public interface DepartmentDao {
	 int findCount();
	 List<Department> findByPage(int begin,int pageSize);
	 void save(Department department);
	 Department findById(Integer did);
	 void update(Department department);
	 void delete(Department department);
	 List<Department> findAll();
}
