package org.example.librarygui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.ArrayList;

public class HelloController {

    Library library = new Library();
    ObservableList<Book> booklist;
    public TableView bookTable;
    public TableColumn isbnColumn;
    public TableColumn titleColumn;
    public TableColumn authorColumn;
    public TableColumn genreColumn;
    public TableColumn actionsColumn;
    public TextField isbnInput;
    public TextField titleInput;
    public TextField authorInput;
    public TextField genreInput;
    public TextArea displayArea;

    public void initialize() {
        booklist = FXCollections.observableArrayList(library.listBooks());
        bookTable.setItems(booklist);

        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        isbnColumn.prefWidthProperty().bind(bookTable.widthProperty().multiply(0.20));
        titleColumn.prefWidthProperty().bind(bookTable.widthProperty().multiply(0.40));
        authorColumn.prefWidthProperty().bind(bookTable.widthProperty().multiply(0.20));
        genreColumn.prefWidthProperty().bind(bookTable.widthProperty().multiply(0.20));
        actionsColumn.prefWidthProperty().bind(bookTable.widthProperty().multiply(0.20));

        bookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        addButtonsToTable();
    }

    private void clearFields() {
        isbnInput.clear();
        titleInput.clear();
        authorInput.clear();
        genreInput.clear();
    }

    public void handleAddBook(ActionEvent actionEvent) {
        String isbn = isbnInput.getText();
        String title = titleInput.getText();
        String author = authorInput.getText();
        String genre = genreInput.getText();
        Book newBook = new Book(title, author, genre, isbn);
        library.addBook(newBook);
        clearFields();
        booklist.add(newBook);
    }

    public void handleUpdateBook(ActionEvent actionEvent) {
        String isbn = isbnInput.getText();
        String title = titleInput.getText();
        String author = authorInput.getText();
        String genre = genreInput.getText();
        library.updateBook(new Book(title, author, genre, isbn));
        Book updatedBook = library.findBookByIsbn(isbn);
        if(updatedBook != null) {
            int index = booklist.indexOf(updatedBook);
            booklist.set(index, updatedBook);
        }
        clearFields();
    }

    public void handleListAllBooks(ActionEvent actionEvent) {
        booklist.setAll(library.listBooks());
    }

    public void handleFindBookByISBN(ActionEvent actionEvent) {
        String isbn = isbnInput.getText();
        Book book = library.findBookByIsbn(isbn);
        if(book != null) {
            booklist.setAll(book);
        }
        else {
            booklist.clear();
        }
    }

    private void addButtonsToTable() {
        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellFactory = new Callback<TableColumn<Book, Void>, TableCell<Book, Void>>() {
            @Override
            public TableCell<Book, Void> call(TableColumn<Book, Void> param) {
                TableCell<Book, Void> cell = new TableCell<>() {
                    private Button btnEdit = new Button("Edit");
                    private Button btnDelete = new Button("Delete");
                    {
                        btnEdit.setOnAction(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            isbnInput.setText(book.getIsbn());
                            titleInput.setText(book.getTitle());
                            authorInput.setText(book.getAuthor());
                            genreInput.setText(book.getGenre());
                        });

                        btnDelete.setOnAction(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            library.removeBook(book.getIsbn());
                            booklist.remove(book);
                        });
                    }

                    private HBox btnPane = new HBox(btnEdit, btnDelete);

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty) {
                            setGraphic(null);
                        }
                        else {
                            setGraphic(btnPane);
                        }
                    }
                };
                return cell;
            }
        };
        actionsColumn.setCellFactory(cellFactory);
    }
}