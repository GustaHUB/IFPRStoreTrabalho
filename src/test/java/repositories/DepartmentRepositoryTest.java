package repositories;

import models.Department;
import models.Seller;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import java.util.List;

public class DepartmentRepositoryTest {

    @Test
    public void inserirUmDepartamento() {
        DepartmentRespository departmentRepository = new DepartmentRespository();
        Department newDepartment = new Department();
        newDepartment.setName("Inserindo departamento novo 22");

        departmentRepository.insert(newDepartment);
    }

    @Test
    public void atualizarDepartamento(){
        DepartmentRespository departRepo = new DepartmentRespository();

        departRepo.update(1,  "Tech TI");
    }

    @Test
    public void deletarDepartamento(){
        DepartmentRespository departRepo = new DepartmentRespository();
        departRepo.delete(1);
    }

    @Test
    public void exibirUmaListaDeDepartamento() {
       DepartmentRespository departRepo = new DepartmentRespository();

        List<Department> departments = departRepo.getDepartments();

        for (Department d : departments) {
            System.out.println(d);
        }
    }

    @Test
    public void departamentoPeloId(){
        DepartmentRespository departRepo = new DepartmentRespository();
        Department department = departRepo.getById(2);

        System.out.println("Departamento: " + department.getName());
    }

    @Test
    public void exibirDepartamentoDoTexto() {
        DepartmentRespository departmentRepository = new DepartmentRespository();
        String textoBusca = "Ti"; // Exemplo de texto para busca

        List<Department> departments = departmentRepository.findByNameContaining("ja");

        for (Department d : departments) {
            System.out.println(d);
        }
    }

    @Test
    public void exibirDepartamentosSemVendedores() {
        DepartmentRespository departmentRepository = new DepartmentRespository();
        List<Department> departmentsWithoutSellers = departmentRepository.findWithoutSellers();
        for (Department d : departmentsWithoutSellers) {
            System.out.println(d);
        }
    }
}
