package sjtu.sshemployee.service.impl;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import sjtu.sshemployee.dao.DepartmentDao;
import sjtu.sshemployee.domain.Department;
import sjtu.sshemployee.domain.PageBean;
/*
 * ���Ź����serviceʵ����
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
    	//��װ��ǰҳ��
    	pageBean.setCurrPage(currPage);
    	//��װÿҳ��¼��
    	int pageSize =3;
    	pageBean.setPageSize(pageSize);
    	//��װ�ܼ�¼��
    	int totalCount = departmentDao.findCount();
    	pageBean.setTotalCount(totalCount);
    	//��װ��ҳ��
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
	 * ҵ������id���Ҳ���
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
