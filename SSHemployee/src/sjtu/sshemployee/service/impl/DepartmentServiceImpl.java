package sjtu.sshemployee.service.impl;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import sjtu.sshemployee.dao.DepartmentDao;
import sjtu.sshemployee.domain.Department;
import sjtu.sshemployee.domain.PageBean;
/*
 * 部门管理的service实现类
 */
import sjtu.sshemployee.service.DepartmentService;

@Transactional
public class DepartmentServiceImpl implements DepartmentService {
   private DepartmentDao departmentDao;
   
    public void setDepartmentDao(DepartmentDao departmentDao) {
	this.departmentDao = departmentDao;
}

	public PageBean<Department> findByPage(Integer currPage)
    {
    	PageBean<Department> pageBean = new PageBean<Department>();
    	//封装当前页数
    	pageBean.setCurrPage(currPage);
    	//封装每页记录数
    	int pageSize =3;
    	pageBean.setPageSize(pageSize);
    	//封装总记录数
    	int totalCount = departmentDao.findCount();
    	pageBean.setTotalCount(totalCount);
    	//封装总页数
    	double tc = totalCount;
    	Double tg=Math.ceil(tc/pageSize);
    	pageBean.setTotalPage(tg.intValue());
    	
    	int begin = (currPage-1)*pageSize;
    	List<Department> list = departmentDao.findByPage(begin,pageSize);
    	pageBean.setList(list);
    	return pageBean;
    }
	public void save(Department department)
	{
		departmentDao.save(department);
	}
	/*
	 * 业务层根据id查找部门
	 * @see sjtu.sshemployee.service.DepartmentService#findById(java.lang.Integer)
	 */
	public Department findById(Integer did)
	{
		return  departmentDao.findById(did);
	}

	@Override
	public void update(Department department) {
		// TODO Auto-generated method stub
		departmentDao.update(department);
	}

	@Override
	public void delete(Department department) {
		// TODO Auto-generated method stub
		departmentDao.delete(department);
	}

	@Override
	public List<Department> findAll() {
		// TODO Auto-generated method stub
		return  departmentDao.findAll();
	}

	

	
}
