package repositories;

import connection.ConnectionFactory;
import exceptions.DatabaseException;
import models.Department;
import models.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerRepository {

    private Connection connection;

    public SellerRepository() {
        connection = ConnectionFactory.getConnection();
    }

    public List<Seller> getSellers() {
        List<Seller> sellers = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();

                ResultSet result = statement.executeQuery("SELECT * FROM seller");

                while (result.next()) {
                    Seller seller = this.instantiateSeller(result, null);
                    sellers.add(seller);
                }

                result.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionFactory.closeConnection();
        }

        return sellers;
    }

    public Seller insert(Seller seller) {

        String sql = "INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId)" +
                "VALUES (?, ?, ?, ?, ?)";

        try{
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, seller.getName());
            statement.setString(2, seller.getEmail());
            statement.setDate(3, Date.valueOf(seller.getBirthDate()));
            statement.setDouble(4, seller.getBaseSalary());
            statement.setInt(5, 1);

            Integer rowsInserted = statement.executeUpdate();

            if(rowsInserted > 0) {
                ResultSet id = statement.getGeneratedKeys();

                id.next();

                Integer sellerId = id.getInt(1);

                System.out.println("Rows inserted: "  + rowsInserted);
                System.out.println("Id: " + sellerId);

                seller.setId(sellerId);
            }

        }catch(Exception e){
            throw new DatabaseException(e.getMessage());
        }

        return seller;
    }

    public void updateSalary(Integer DepartmentId, Double bonus) {

        String sql = "UPDATE seller SET BaseSalary = BaseSalary + ? WHERE DepartmentId = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, bonus);
            statement.setInt(2, DepartmentId);

            Integer rowsUpdated = statement.executeUpdate();

            if(rowsUpdated > 0) {
                System.out.println("Rows updated: " + rowsUpdated);
            }

        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }

    }

    public void delete(Integer id) {
        String sql = "DELETE FROM seller WHERE Id = ?";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            Integer rowsDeleted = statement.executeUpdate();

            if(rowsDeleted > 0) {
                System.out.println("Rows deleted: " + rowsDeleted);
            }

        }catch (Exception e){
            throw new DatabaseException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }

    }

    public Seller getById(Integer id) {

        Seller seller;
        Department department;

        String sql = "SELECT seller.*,department.Name as DepName\n" +
                "FROM seller INNER JOIN department\n" +
                "ON seller.DepartmentId = department.Id\n" +
                "WHERE seller.id = ?";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if(result.next()) {
                department = this.instantiateDepartment(result);
                seller = this.instantiateSeller(result, department);

            } else {
                throw new DatabaseException("Vendedor não encontrado!");
            }

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

        return seller;
    }

    public List<Seller> findByDepartment(Integer id) {

        List<Seller> sellersList = new ArrayList<>();

        Seller seller;
        Department department;

        String sql = "SELECT seller.*,department.Name as DepName " +
                "FROM seller INNER JOIN department " +
                "ON seller.DepartmentId = department.Id " +
                "WHERE DepartmentId = ? " +
                "ORDER BY Name";
        try{
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            Map<Integer, Department> map = new HashMap<>();

            while(result.next()) {

                department = map.get(result.getInt("DepartmentId"));
                if(department == null) {
                    department = instantiateDepartment(result);
                    map.put(result.getInt("DepartmentId"), department);
                }

                seller = this.instantiateSeller(result, department);
                sellersList.add(seller);
            }

        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return sellersList;
    }

    public Seller instantiateSeller(ResultSet result, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(result.getInt("Id"));
        seller.setName(result.getString("Name"));
        seller.setEmail(result.getString("Email"));
        seller.setBirthDate(result.getDate("BirthDate").toLocalDate());
        seller.setBaseSalary(result.getDouble("BaseSalary"));
        seller.setDepartment(department);

        return seller;
    }

    public Department instantiateDepartment(ResultSet result) throws SQLException {
        Department department1 = new Department();
        department1.setId(result.getInt("DepartmentId"));
        department1.setName(result.getString("DepName"));

        return department1;
    }
}
