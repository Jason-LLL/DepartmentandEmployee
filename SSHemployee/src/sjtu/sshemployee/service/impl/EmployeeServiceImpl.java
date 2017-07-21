package sjtu.sshemployee.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import sjtu.sshemployee.dao.EmployeeDao;
import sjtu.sshemployee.domain.Employee;
import sjtu.sshemployee.domain.PageBean;
import sjtu.sshemployee.service.EmployeeService;

@Transactional
public class EmployeeServiceImpl implements EmployeeService {
 private EmployeeDao employeeDao;

public void setEmployeeDao(EmployeeDao employeeDao) {
	this.employeeDao = employeeDao;
}
  public Employee login(Employee employee)
  {
	  Employee existEmployee = employeeDao.findByUsernameAndPassword(employee);
	  return existEmployee;
  }
@Override
/*
 * 业务层的分页查询员工的方法(non-Javadoc)
 * @see sjtu.sshemployee.service.EmployeeService#findByPage(java.lang.Integer)
 */
public PageBean<Employee> findByPage(Integer currPage) {
	// TODO Auto-generated method stub
	PageBean<Employee> pageBean= new PageBean<Employee>();
	//封装当前页数
	pageBean.setCurrPage(currPage);
	//封装每页显示的记录数
	int pageSize = 3;
	pageBean.setPageSize(pageSize);
	//封装总记录数
	int totalCount = employeeDao.findCount();
	pageBean.setTotalCount(totalCount);
	//封装总页数
	double tc = totalCount;//Math.ceil里面要求的是double类型
	Double tg=Math.ceil(tc/pageSize);
	pageBean.setTotalPage(tg.intValue());
	
	
	int begin =(currPage-1)*pageSize;
	List<Employee>list = employeeDao.findByPage(begin,pageSize);
	pageBean.setList(list);
	return pageBean;
	
}
@Override
   public void save(Employee employee) {
	// TODO Auto-generated method stub
	employeeDao.save(employee);
}
@Override
public Employee findById(Integer eid) {
	// TODO Auto-generated method stub
	return employeeDao.findById(eid);
}
@Override
public void delete(Employee employee) {
	// TODO Auto-generated method stub
	employeeDao.delete(employee);
}
@Override
public void update(Employee employee) {
	// TODO Auto-generated method stub
	employeeDao.update(employee);
}
}
