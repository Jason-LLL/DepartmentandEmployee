package sjtu.sshemployee.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import sjtu.sshemployee.dao.EmployeeDao;
import sjtu.sshemployee.domain.Employee;

public class EmployeeDaoImpl extends HibernateDaoSupport implements EmployeeDao {

	/*
	 * Dao中根据用户名和密码查询用户的方法
	 * @see sjtu.sshemployee.dao.EmployeeDao#findByUsernameAndPassword(sjtu.sshemployee.domain.Employee)
	 */
	@Override
	public Employee findByUsernameAndPassword(Employee employee) {
		// TODO Auto-generated method stub
		String hql= "from Employee where username=? and password=? ";
		List<Employee>	list = this.getHibernateTemplate().find(hql,employee.getUsername(),employee.getPassword() );
		if(list.size()>0)
		{
				return list.get(0);
		}
		else
		{
			return null;
		}
	}

	public int findCount() {
		// TODO Auto-generated method stub
		String hql = "select count(*) from Employee";
		List<Long> list=this.getHibernateTemplate().find(hql);
		if(list.size()>0)
		{
			return list.get(0).intValue();
		}
		return 0;
	}

	public List<Employee> findByPage(int begin, int pageSize) {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(Employee.class);
		List<Employee> list= this.getHibernateTemplate().findByCriteria(criteria,begin,pageSize);
		return list;
	}

	@Override
	public void save(Employee employee) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().save(employee);
	}

	@Override
	public Employee findById(Integer eid) {
		// TODO Auto-generated method stub
		return this.getHibernateTemplate().get(Employee.class, eid);
	}

	@Override
	public void delete(Employee employee) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().delete(employee);
	}
     
	@Override
	public void update(Employee employee) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().update(employee);
	}

	

	

	
    
	
}
