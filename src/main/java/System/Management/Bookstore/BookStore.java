package System.Management.Bookstore;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class BookStore {

    private final Scanner scanner = new Scanner(System.in);
    // Perform operations in Bookstore
    public void management(){

        String address = "jdbc:postgresql://localhost:5432/bookstoredb";
        String username = "postgres";
        String password = "postgres";
        try(Connection connection = DriverManager.getConnection(address, username, password))
        {
            while (true) {
                System.out.println("Choose the operation you want to perform");
                System.out.println("1. Update book details");
                System.out.println("2. List books by genre");
                System.out.println("3. List books by author");
                System.out.println("4. Update customer information");
                System.out.println("5. View customer's purchase history");
                System.out.println("6. Perform new sale");
                System.out.println("7. Calculate total revenue by genre");
                System.out.println("8. Generate book sales report");
                System.out.println("9. Generate revenue by genre report");
                System.out.println("10. View all books");
                System.out.println("11. View all customers");
                System.out.println("0. Exit");

                System.out.print("Enter your choice: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        System.out.println("Do you want to view the list of books?");
                        System.out.println("1. Yes");
                        System.out.println("2. No");
                        while(true) {
                            System.out.print("Enter your choice: ");
                            choice = scanner.nextLine();
                            if(choice.equals("1")) {
                                viewAllBooks(connection);
                                break;
                            }
                            if(choice.equals("2")) {
                                break;
                            }
                            System.out.println("Not valid choice. Try again");
                        }
                        updateBookDetails(connection);
                        break;

                    case "2":
                        listBooksByGenre(connection);
                        break;

                    case "3":
                        listBooksByAuthor(connection);
                        break;

                    case "4":
                        System.out.println("Do you want to view the list of customers?");
                        System.out.println("1. Yes");
                        System.out.println("2. No");
                        while(true) {
                            System.out.print("Enter your choice: ");
                            choice = scanner.nextLine();
                            if(choice.equals("1")) {
                                viewAllCustomers(connection);
                                break;
                            }
                            if(choice.equals("2")) {
                                break;
                            }
                            System.out.println("Not valid choice. Try again");
                        }
                        updateCustomerInfo(connection);
                        break;

                    case "5":
                        System.out.println("Do you want to view the list of customers?");
                        System.out.println("1. Yes");
                        System.out.println("2. No");
                        while(true) {
                            System.out.print("Enter your choice: ");
                            choice = scanner.nextLine();
                            if(choice.equals("1")) {
                                viewAllCustomers(connection);
                                break;
                            }
                            if(choice.equals("2")) {
                                break;
                            }
                            System.out.println("Not valid choice. Try again");
                        }
                        viewPurchaseHistory(connection);
                        break;

                    case "6":
                        performNewSale(connection);
                        break;

                    case "7":
                        totalRevenueByGenre(connection);
                        break;

                    case "8":
                        bookSalesReport(connection);
                        break;

                    case "9":
                        totalRevenueReportByGenres(connection);
                        break;

                    case "10":
                        viewAllBooks(connection);
                        break;

                    case "11":
                        viewAllCustomers(connection);
                        break;

                    case "0":
                        System.out.println("Exiting the app...");
                        return;
                    default:
                        System.out.println("Not valid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // Change value of selected column for row (specified by book ID)
    private void updateBookDetails(Connection connection){
        try {
            System.out.print("Enter BookID to update: ");
            int bookId = Integer.parseInt(scanner.nextLine());
            if(!isValidBookID(connection, bookId)) {
                System.out.println("Not valid book ID.");
                return;
            }
            String columnToChange = "";
            String change = "";
            System.out.println("Which column do you want to update?");
            System.out.println("1. Title");
            System.out.println("2. Author");
            System.out.println("3. Genre");
            System.out.println("4. Price");
            System.out.println("5. Quantity in stock");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    columnToChange = "Title";
                    System.out.print("Enter the new title: ");
                    change = scanner.nextLine();
                    break;
                case "2":
                    columnToChange = "Author";
                    System.out.print("Enter the new author: ");
                    change = scanner.nextLine();
                    break;
                case "3":
                    columnToChange = "Genre";
                    System.out.print("Enter the new genre: ");
                    change = scanner.nextLine();
                    break;
                case "4":
                    columnToChange = "Price";
                    System.out.print("Enter the new price: ");
                    change = scanner.nextLine();
                    break;
                case "5":
                    columnToChange = "QuantityInStock";
                    System.out.print("Enter the new quantity in stock: ");
                    change = scanner.nextLine();
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
            String updateQuery = "UPDATE Books SET " +
                    columnToChange + "=? WHERE BookID=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                switch (columnToChange) {
                    case "Title":
                    case "Author":
                    case "Genre":
                        preparedStatement.setString(1, change);
                        break;
                    case "Price":
                        preparedStatement.setDouble(1, Double.parseDouble(change));
                        break;
                    case "QuantityInStock":
                        preparedStatement.setInt(1, Integer.parseInt(change));
                        break;
                }
                preparedStatement.setInt(2, bookId);
                preparedStatement.execute();
                System.out.println("Book details are updated");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input type.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Change value of selected column for row (specified by customer ID)
    private void updateCustomerInfo(Connection connection) {
        try {
            System.out.print("Enter CustomerID to update: ");
            int customerID = Integer.parseInt(scanner.nextLine());
            if(!isValidCustomerID(connection, customerID)) {
                System.out.println("Not valid customer ID");
                return;
            }
            String columnToChange = "";
            String change = "";
            System.out.println("Which column do you want to update?");
            System.out.println("1. Name");
            System.out.println("2. Email");
            System.out.println("3. Phone");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    columnToChange = "Name";
                    System.out.print("Enter the new name: ");
                    change = scanner.nextLine();
                    break;
                case "2":
                    columnToChange = "Email";
                    System.out.print("Enter the new email: ");
                    change = scanner.nextLine();
                    break;
                case "3":
                    columnToChange = "Phone";
                    System.out.print("Enter the new phone: ");
                    change = scanner.nextLine();
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
            String updateQuery = "UPDATE Customers SET " +
                    columnToChange + "=? WHERE CustomerID=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, change);
                preparedStatement.setInt(2, customerID);
                preparedStatement.execute();
                System.out.println("Customer information is updated");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input type.");
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                System.out.println("Error: The new email already exists. Please choose a unique email.");
            } else {
                e.printStackTrace();
            }
        }
    }

    // Show all books with given genre
    private void listBooksByGenre(Connection connection) {
        String selectQuery = "SELECT * FROM Books WHERE Genre=?";
        System.out.print("Enter genre to list books: ");
        String genre = scanner.nextLine();
        listBooks(connection, selectQuery, genre);
    }

    // Show all books with given author
    private void listBooksByAuthor(Connection connection) {
        String selectQuery = "SELECT * FROM Books WHERE Author=?";
        System.out.print("Enter author to list books: ");
        String author = scanner.nextLine();
        listBooks(connection, selectQuery, author);
    }

    // Show all books with given criteria
    private void listBooks(Connection connection, String selectQuery, String criteria) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, criteria);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int bookId = resultSet.getInt("BookID");
                    String title = resultSet.getString("Title");
                    String resultGenre = resultSet.getString("Genre");
                    String resultAuthor = resultSet.getString("Author");
                    double price = resultSet.getDouble("Price");
                    int quantityInStock = resultSet.getInt("QuantityInStock");
                    System.out.printf("%-8d %-30s %-20s %-20s %-8.2f %-16d\n", bookId, title, resultAuthor, resultGenre, price, quantityInStock);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // View purchase history for customer
    private void viewPurchaseHistory(Connection connection) {
        System.out.print("Enter CustomerID to view his/her purchase history: ");
        int customerID = 0;
        try {
            customerID = Integer.parseInt(scanner.nextLine());
            if (!isValidCustomerID(connection, customerID)) {
                System.out.println("Not Valid customer id");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Not valid input");
            return;
        }
        String selectQuery = "SELECT B.BookID, B.Title, B.Author, B.Genre, B.Price, S.DateOfSale, S.QuantitySold, S.TotalPrice " +
                "FROM Sales S " +
                "JOIN Books B ON S.BookID = B.BookID " +
                "WHERE S.CustomerID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, customerID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.printf("%-8s %-30s %-20s %-20s %-8s %-10s %-12s %-12s\n",
                        "BookID", "Title", "Author", "Genre", "Price", "DateOfSale", "QuantitySold", "TotalPrice");
                System.out.println("-----------------------------------------------------------------------------------------------------------------------------");

                while (resultSet.next()) {
                    int bookId = resultSet.getInt("BookID");
                    String title = resultSet.getString("Title");
                    String author = resultSet.getString("Author");
                    String genre = resultSet.getString("Genre");
                    double price = resultSet.getDouble("Price");
                    java.sql.Date dateOfSale = resultSet.getDate("DateOfSale");
                    int quantitySold = resultSet.getInt("QuantitySold");
                    double totalPrice = resultSet.getDouble("TotalPrice");
                    System.out.printf("%-8d %-30s %-20s %-20s %-8.2f %-10s %-12d %-12.2f\n",
                            bookId, title, author, genre, price, dateOfSale, quantitySold, totalPrice);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // Add new row to Sales table
    private void performNewSale(Connection connection) {
        int bookID = 0;
        int customerID = 0;
        int quantitySold = 0;
        try {
            System.out.print("Enter BookID: ");
            bookID = Integer.parseInt(scanner.nextLine());
            if (!isValidBookID(connection, bookID)) {
                System.out.println("Not valid book id");
                return;
            }
            System.out.print("Enter CustomerID: ");
            customerID = Integer.parseInt(scanner.nextLine());
            if (!isValidCustomerID(connection, customerID)) {
                System.out.println("Not valid customer id");
                return;
            }
            System.out.print("Enter quantity sold: ");
            quantitySold = Integer.parseInt(scanner.nextLine());
            if (!hasEnoughQuantity(connection, bookID, quantitySold)) {
                System.out.println("Not enough books to sale");
                return;
            }
        } catch(NumberFormatException e) {
            System.out.println("Not valid input");
            return;
        }

        String query = "INSERT INTO Sales (BookID, CustomerID, DateOfSale, QuantitySold, TotalPrice)" +
                " VALUES (?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);
            try (PreparedStatement saleStatement = connection.prepareStatement(query)) {
                saleStatement.setInt(1, bookID);
                saleStatement.setInt(2, customerID);
                saleStatement.setDate(3, Date.valueOf(LocalDate.now()));
                saleStatement.setInt(4, quantitySold);

                double totalPrice = 0.0;
                String priceQuery = "SELECT Price FROM Books WHERE BookID = ?";
                try (PreparedStatement priceStatement = connection.prepareStatement(priceQuery)) {
                    priceStatement.setInt(1, bookID);
                    try (ResultSet resultSet = priceStatement.executeQuery()) {
                        if (resultSet.next()) {
                            totalPrice = quantitySold * resultSet.getDouble("Price");
                        }
                    }
                }
                saleStatement.setDouble(5, totalPrice);
                saleStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // Total revenue for given genre of book
    private void totalRevenueByGenre(Connection connection) {
        String query = "SELECT COALESCE(SUM(Sales.TotalPrice), 0) AS TotalRevenue " +
                "FROM Books " +
                "LEFT JOIN Sales ON Books.BookID = Sales.BookID AND Books.Genre = ?";

        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, genre);

            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.printf("%-20s %-10s\n", "Genre", "TotalRevenue");
                System.out.println("----------------------------------------");

                if (resultSet.next()) {
                    double totalRevenue = resultSet.getDouble("TotalRevenue");
                    System.out.printf("%-20s %-10.2f\n", genre, totalRevenue);
                } else {
                    System.out.println("No data found for the specified genre.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // View Sale report of book (specified by book ID)
    private void bookSalesReport(Connection connection) {
        String query = "SELECT Sales.SaleID, Books.Title AS BookTitle, Customers.Name AS CustomerName, " +
                "Sales.DateOfSale, Sales.QuantitySold, Sales.TotalPrice FROM Sales " +
                "JOIN Books " +
                "ON Sales.BookID = Books.BookID " +
                "JOIN Customers " +
                "ON Sales.CustomerID = Customers.CustomerID";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.printf("%-10s %-30s %-25s %-15s %-15s %-15s\n",
                    "SaleID", "Book Title", "Customer Name", "Date of Sale", "Quantity Sold", "Total Price");
            System.out.println("-------------------------------------------------------------------------------------------------------------");
            while (resultSet.next()) {
                int saleID = resultSet.getInt("SaleID");
                String bookTitle = resultSet.getString("BookTitle");
                String customerName = resultSet.getString("CustomerName");
                Date dateOfSale = resultSet.getDate("DateOfSale");
                int quantitySold = resultSet.getInt("QuantitySold");
                double totalPrice = resultSet.getDouble("TotalPrice");

                System.out.printf("%-10d %-30s %-25s %-15s %-15d %-15.2f\n",
                        saleID, bookTitle, customerName, dateOfSale, quantitySold, totalPrice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // total revenue for all genres in book store
    private void totalRevenueReportByGenres(Connection connection) {
        String query = "SELECT Books.Genre, COALESCE(SUM(Sales.TotalPrice), 0) AS TotalRevenue " +
                "FROM Books LEFT JOIN Sales ON Books.BookID = Sales.BookID " +
                "GROUP BY Books.Genre";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.printf("%-20s %-10s\n", "Genre", "TotalRevenue");
            System.out.println("----------------------------------------");

            while (resultSet.next()) {
                String genre = resultSet.getString("Genre");
                double totalRevenue = resultSet.getDouble("TotalRevenue");
                System.out.printf("%-20s %-10.2f\n", genre, totalRevenue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // View Books table
    private void viewAllBooks(Connection connection) {
        String selectQuery = "SELECT * FROM Books";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.printf("%-8s %-30s %-20s %-20s %-8s %-16s\n", "BookID", "Title", "Author", "Genre", "Price", "QuantityInStock");
            System.out.println("-------------------------------------------------------------------------");

            while (resultSet.next()) {
                int bookId = resultSet.getInt("BookID");
                String title = resultSet.getString("Title");
                String author = resultSet.getString("Author");
                String genre = resultSet.getString("Genre");
                double price = resultSet.getDouble("Price");
                int quantityInStock = resultSet.getInt("QuantityInStock");

                System.out.printf("%-8d %-30s %-20s %-20s %-8.2f %-16d\n", bookId, title, author, genre, price, quantityInStock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // View Customers table
    private void viewAllCustomers(Connection connection) {
        String selectQuery = "SELECT * FROM Customers";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            System.out.printf("%-12s %-35s %-100s %-20s\n", "CustomerID", "Name", "Email", "Phone");
            System.out.println("------------------------------------------------------------------------------------------------------------------");
            while (resultSet.next()) {
                int customerId = resultSet.getInt("CustomerID");
                String name = resultSet.getString("Name");
                String email = resultSet.getString("Email");
                String phone = resultSet.getString("Phone");
                System.out.printf("%-12d %-35s %-100s %-20s\n", customerId, name, email, phone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // check whether there is a book with given book ID in Books table
    private boolean isValidBookID(Connection connection, int bookID) {
        String bookIDQuery = "SELECT COUNT(*) FROM Books WHERE BookID=?";
        try (PreparedStatement stat = connection.prepareStatement(bookIDQuery)) {
            stat.setInt(1, bookID);
            try (ResultSet resultSet = stat.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // check whether there is a customer with given customer ID in Customers table
    private boolean isValidCustomerID(Connection connection, int customerID) {
        String customerIDQuery = "SELECT COUNT(*) FROM Customers WHERE CustomerID=?";
        try (PreparedStatement stat = connection.prepareStatement(customerIDQuery)) {
            stat.setInt(1, customerID);
            try (ResultSet resultSet = stat.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check whether there is enough quantity of books to buy
    private boolean hasEnoughQuantity(Connection connection, int bookID, int quantity) {
        String stockQuery = "SELECT QuantityInStock FROM Books " +
                " WHERE BookID = ?";
        try (PreparedStatement stat = connection.prepareStatement(stockQuery)) {
            stat.setInt(1, bookID);
            try (ResultSet resultSet = stat.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("QuantityInStock") >= quantity;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
