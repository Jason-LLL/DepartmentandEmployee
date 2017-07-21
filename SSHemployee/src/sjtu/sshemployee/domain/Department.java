package sjtu.sshemployee.domain;

import java.util.HashSet;
import java.util.Set;
import sjtu.sshemployee.domain.Employee;
public class Department {
	private int did;
	   private String dname;
	   private String ddesc;
	   //一对多关系映射
	   private Set<Employee> employees = new HashSet<Employee>();
	public int getDid() {
		return did;
	}
	public void setDid(int did) {
		this.did = did;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getDdesc() {
		return ddesc;
	}
	public void setDdesc(String ddesc) {
		this.ddesc = ddesc;
	}
	public Set<Employee> getEmployees() {
		return employees;
	}
	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}
	
	   
	   
	}
