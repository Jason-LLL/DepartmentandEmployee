package sjtu.sshemployee.action;

import sjtu.sshemployee.domain.Department;
import sjtu.sshemployee.domain.PageBean;
import sjtu.sshemployee.service.DepartmentService;
import sjtu.sshemployee.service.impl.DepartmentServiceImpl;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class DepartmentAction extends ActionSupport implements ModelDriven<Department>{
     private Department department =new Department();

	@Override
	public Department getModel() {
		// TODO Auto-generated method stub
		return department;
	}
	//��ҳ��ѯ �ṩһ����ʼ��ѯҳ
	 private Integer currPage =1;
	 
	 public void setCurrPage(Integer currPage) {
		this.currPage = currPage;
	}
    private DepartmentService departmentService;

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

   //�ṩ����һ����ѯ�취
	public String findAll()
	 {
		PageBean<Department> pageBean=departmentService.findByPage(currPage);
		//��pageBean����ֵջ��.
		ActionContext.getContext().getValueStack().push(pageBean);
		return "findAll";
	 }
	//��ת����Ӳ��ŵ�ҳ��İ취
	public String saveUI()
	{
		return "saveUI";
	}
    public String save()
    {
    	departmentService.save(department);
    	return "saveSuccess";
    }
    //�༭����ִ�з���
    public String edit()
    {
    	department = departmentService.findById(department.getDid());
    	return "editSuccess";
    }
    //�޸Ĳ��ŵ�ִ�з���
    public String update()
    {
    	departmentService.update(department);
    	return "updateSuccess";
    }
    public String delete()
    {
    	department = departmentService.findById(department.getDid());
    	departmentService.delete(department);
    	return "deleteSuccess";
    }
}
