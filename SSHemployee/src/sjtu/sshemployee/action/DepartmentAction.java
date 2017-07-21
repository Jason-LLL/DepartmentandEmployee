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
	//分页查询 提供一个初始查询页
	 private Integer currPage =1;
	 
	 public void setCurrPage(Integer currPage) {
		this.currPage = currPage;
	}
    private DepartmentService departmentService;

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

   //提供部门一个查询办法
	public String findAll()
	 {
		PageBean<Department> pageBean=departmentService.findByPage(currPage);
		//将pageBean存入值栈里.
		ActionContext.getContext().getValueStack().push(pageBean);
		return "findAll";
	 }
	//跳转到添加部门的页面的办法
	public String saveUI()
	{
		return "saveUI";
	}
    public String save()
    {
    	departmentService.save(department);
    	return "saveSuccess";
    }
    //编辑部门执行方法
    public String edit()
    {
    	department = departmentService.findById(department.getDid());
    	return "editSuccess";
    }
    //修改部门的执行方法
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
