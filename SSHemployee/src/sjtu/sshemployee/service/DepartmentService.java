package sjtu.sshemployee.service;

import java.util.List;

import sjtu.sshemployee.domain.Department;
import sjtu.sshemployee.domain.PageBean;

public interface DepartmentService {
	PageBean<Department> findByPage(Integer currPage);
	void save(Department department);
	Department findById(Integer id);
	void update(Department department);
	void delete(Department department);
	List<Department> findAll();
}
