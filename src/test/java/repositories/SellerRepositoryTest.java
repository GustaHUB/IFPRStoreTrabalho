package repositories;

import models.Seller;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import java.time.LocalDate;
import java.util.List;

public class SellerRepositoryTest {
    @Test
    public void deveExibirUmaListaDeSellers(){
        SellerRepository sellerRepository = new SellerRepository();

        List<Seller> sellers = sellerRepository.getSellers();

        for (Seller s : sellers) {
            System.out.println(s);
        }
    }

    @Test
    public void deveExibirUmaListaDeSellersPorNome(){
        SellerRepository sellerRepository = new SellerRepository();
        Seller sellerFake = new Seller();
        sellerFake.setName("Teste");
        sellerFake.setEmail("teste@gmail.com");
        sellerFake.setBirthDate(LocalDate.of(2023, 8, 5));
        sellerFake.setBaseSalary(1000.00);

        Seller seller = sellerRepository.insert(sellerFake);
    }

    @Test
    public void deveAtualizarSalarioDeUmSellerDeUmDepartamento(){
        SellerRepository sellerRepository = new SellerRepository();

        sellerRepository.updateSalary(1,  1500.00);
    }

    @Test
    public void deveDeletarUmSeller(){
        SellerRepository sellerRepository = new SellerRepository();

        sellerRepository.delete(4);
    }

    @Test
    public void deveRetornarUmSellerPeloId(){
        SellerRepository sellerRepository = new SellerRepository();
        Seller seller = sellerRepository.getById(1);

        System.out.println(seller);
        System.out.println("Departamento: ----------- ");
        System.out.println(seller.getDepartment());
    }

    @Test
    public void buscarDepartamento() {
        SellerRepository sellerRepository = new SellerRepository();
        List<Seller> sellers = sellerRepository.findByDepartment(2);
        for (Seller s : sellers) {
            System.out.println(s);
            System.out.println("Departamento: " + s.getDepartment().getName());
            System.out.println("---------------");
        }
    }
}
