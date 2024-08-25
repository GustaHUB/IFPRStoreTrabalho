package repositories;

import connection.ConnectionFactory;
import exceptions.DatabaseException;
import exceptions.DatabaseIntegrityException;
import models.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentRespository {
    Connection connection;

    public DepartmentRespository() {
        connection = ConnectionFactory.getConnection();
    }

    public List<Department> getDepartments() {
        List<Department> departments = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM department");

            while (result.next()) {
                Department department = this.instantiateDepartment(result);
                departments.add(department);
            }

            result.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionFactory.closeConnection();
        }

        return departments;
    }

    public Department insert(Department department) {
        String sql = "INSERT INTO department (Name) VALUES (?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, department.getName());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet id = statement.getGeneratedKeys();
                id.next();
                department.setId(id.getInt(1));
            }

        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }

        return department;
    }

    public void update(Integer id, String newName) {
        String sql = "UPDATE department SET Name = ? WHERE Id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newName);
            statement.setInt(2, id);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Rows updated: " + rowsUpdated);
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    public Department getById(Integer id) {
        Department department = null;
        String sql = "SELECT * FROM department WHERE Id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                department = this.instantiateDepartment(result);
            } else {
                throw new DatabaseException("Departamento não encontrado!");
            }

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }

        return department;
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM department WHERE Id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            Integer rowsDeleted = statement.executeUpdate();

            if(rowsDeleted > 0){
                System.out.println("Rows Deleted: " + rowsDeleted);
            }

        }catch (Exception e){
            throw new DatabaseIntegrityException(e.getMessage());
        }
    }

    public List<Department> findWithoutSellers() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT d.* FROM department d LEFT JOIN seller s ON d.Id = s.DepartmentId WHERE s.Id IS NULL";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Department department = instantiateDepartment(result);
                departments.add(department);
            }

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return departments;
    }

    public List<Department> findByNameContaining(String text) {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM department WHERE LOWER(Name) LIKE LOWER(?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + text + "%");
            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {
                    Department department = instantiateDepartment(result);
                    departments.add(department);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return departments;
    }

    private Department instantiateDepartment(ResultSet result) throws SQLException {
        Department department = new Department();
        department.setId(result.getInt("Id"));
        department.setName(result.getString("Name"));
        return department;
    }
}
