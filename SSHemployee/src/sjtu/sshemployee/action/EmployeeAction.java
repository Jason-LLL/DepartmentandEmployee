package sjtu.sshemployee.action;

import java.util.List;

import sjtu.sshemployee.domain.Department;
import sjtu.sshemployee.domain.Employee;
import sjtu.sshemployee.domain.PageBean;
import sjtu.sshemployee.service.DepartmentService;
import sjtu.sshemployee.service.EmployeeService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class EmployeeAction extends ActionSupport implements ModelDriven<Employee> {
    //模型驱动使用的对象
	private Employee employee = new Employee();
	
    private EmployeeService employeeService;
    //注入service类
	public void setEmployeeService(EmployeeService employeeService) {
	this.employeeService = employeeService;
}
    private DepartmentService departmentService;
    
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public String login()
	{
		System.out.println("login方法执行了.....");
		Employee existEmployee= employeeService.login(employee);
		if(existEmployee==null)
		{
			//登录失败
			this.addActionError("用户名或密码错误");
			return INPUT;
		}
		else
		{
			//登录成功
			ActionContext.getContext().getSession().put("existEmployee", existEmployee);
			return SUCCESS;
		}
	}

	@Override
	public Employee getModel() {
		// TODO Auto-generated method stub
		return employee;
	}
	private Integer currPage=1;
	
   public void setCurrPage(Integer currPage) {
		this.currPage = currPage;
	}

	//提供一个分页雇员查询的方法
	public String findAll()
	{
		PageBean<Employee> pageBean= employeeService.findByPage(currPage);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "findAll";
	}
	public String saveUI()
	{
		List<Department> list = departmentService.findAll();
		ActionContext.getContext().getValueStack().set("list", list);
		return "saveUI";
	}
	
	//保存添加的雇员数据的方法
	public String save()
	{
		employeeService.save(employee);
		return "saveSuccess";
	}
	//编辑员工的执行方法
	public String edit()
	{
		//根据员工id查询员工
		employee = employeeService.findById(employee.getEid());
		//查询所有的部门
		List<Department> list = departmentService.findAll();
		ActionContext.getContext().getValueStack().set("list", list);
		return "editSuccess";
		
	}
	//修改员工的执行的方法
	public String update()
	{
		employeeService.update(employee);
		return "updateSuccess";
	}
	//删除员工的执行方法
	public String delete()
	{
		employee = employeeService.findById(employee.getEid());
		employeeService.delete(employee);
		return "deleteSuccess";
	}
}
