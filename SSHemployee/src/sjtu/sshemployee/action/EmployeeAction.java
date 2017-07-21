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
    //ģ������ʹ�õĶ���
	private Employee employee = new Employee();
	
    private EmployeeService employeeService;
    //ע��service��
	public void setEmployeeService(EmployeeService employeeService) {
	this.employeeService = employeeService;
}
    private DepartmentService departmentService;
    
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public String login()
	{
		System.out.println("login����ִ����.....");
		Employee existEmployee= employeeService.login(employee);
		if(existEmployee==null)
		{
			//��¼ʧ��
			this.addActionError("�û������������");
			return INPUT;
		}
		else
		{
			//��¼�ɹ�
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

	//�ṩһ����ҳ��Ա��ѯ�ķ���
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
	
	//������ӵĹ�Ա���ݵķ���
	public String save()
	{
		employeeService.save(employee);
		return "saveSuccess";
	}
	//�༭Ա����ִ�з���
	public String edit()
	{
		//����Ա��id��ѯԱ��
		employee = employeeService.findById(employee.getEid());
		//��ѯ���еĲ���
		List<Department> list = departmentService.findAll();
		ActionContext.getContext().getValueStack().set("list", list);
		return "editSuccess";
		
	}
	//�޸�Ա����ִ�еķ���
	public String update()
	{
		employeeService.update(employee);
		return "updateSuccess";
	}
	//ɾ��Ա����ִ�з���
	public String delete()
	{
		employee = employeeService.findById(employee.getEid());
		employeeService.delete(employee);
		return "deleteSuccess";
	}
}
