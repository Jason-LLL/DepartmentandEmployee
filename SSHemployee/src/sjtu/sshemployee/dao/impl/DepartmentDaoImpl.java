package sjtu.sshemployee.dao.impl;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;




/*
 * ���Ź����Dao��ʵ����
 */
import sjtu.sshemployee.dao.DepartmentDao;
import sjtu.sshemployee.domain.Department;

public class DepartmentDaoImpl extends HibernateDaoSupport implements DepartmentDao 
{
  
	public int findCount()
	{
		String hql = "select count(*) from Department";
		List<Long> list = this.getHibernateTemplate().find(hql);
		
		if(list.size()>0)
		{
			return list.get(0).intValue();
		}
		return 0;
	}
	/*
	 * ��ҳ��ѯ����
	 */
	public List<Department> findByPage( int begin,int pageSize)
	{
		DetachedCriteria  criteria = DetachedCriteria.forClass(Department.class);
		List<Department> list = this.getHibernateTemplate().findByCriteria(criteria,begin,pageSize);
		return list;
	}
	/*
	 * Dao���沿�Ž����ݿ�
	 */
	public void save(Department department)
	{
		this.getHibernateTemplate().save(department);
	}
	/*
	 * Dao�����id�Ҳ���(non-Javadoc)
	 * @see sjtu.sshemployee.dao.DepartmentDao#findById(java.lang.Integer)
	 */
	public Department findById(Integer did)
	{
		return this.getHibernateTemplate().get(Department.class,did);
	}
	/*
	 * Dao���޸Ĳ��ŷ���
	 */
	public void update(Department department)
	{
		this.getHibernateTemplate().update(department);
	}
	@Override
	public void delete(Department department) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().delete(department);
		
	}
	@Override
	public List<Department> findAll()
	{
		// TODO Auto-generated method stub
		List<Department> list = this.getHibernateTemplate().find("from Department");
	    return list;
	}
	
	
}
