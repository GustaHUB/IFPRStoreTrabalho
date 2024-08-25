package controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Department;
import models.Seller;
import repositories.SellerRepository;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"", "/sellers"})
public class SellersController extends HttpServlet {

    private SellerRepository repository;

    public SellersController(){
        repository = new SellerRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Seller> sellers = repository.getSellers();

        RequestDispatcher dispatcher = req.getRequestDispatcher("/sellers.jsp");
        req.setAttribute("sellers", sellers);

        dispatcher.forward(req, resp);

    }
}
