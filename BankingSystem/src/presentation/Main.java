package presentation;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import model.entity.Account;
import model.entity.Client;
import model.entity.Transaction;
import model.entity.User;
import repository.AccountRepository;
import repository.ClientRepository;
import repository.TransactionRepository;
import repository.UserRepository;
import repository.impl.*;
import service.*;
import service.impl.*;
import util.TableViewReflection;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class Main extends Application {

    private Stage primaryStage;
    private JDBConnectionWrapper connectionWrapper;
    private UserRepository userRepository;
    private ClientRepository clientRepository;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private UserService userService;
    private ClientService clientService;
    private AccountService accountService;
    private TransactionService transactionService;
    private ContextHolder contextHolder;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // connector
            connectionWrapper = new JDBConnectionWrapper("assig1");
            try {
                System.out.println("Successfully connected : " + connectionWrapper.testConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // repository
            userRepository = new UserRepositoryImpl(connectionWrapper);
            clientRepository = new ClientRepositoryImpl(connectionWrapper);
            accountRepository = new AccountRepositoryImpl(connectionWrapper);
            transactionRepository = new TransactionRepositoryImpl(connectionWrapper);

            // service
            userService = new UserServiceImpl(userRepository);
            clientService = new ClientServiceImpl(clientRepository);
            accountService = new AccountServiceImpl(accountRepository);
            transactionService = new TransactionServiceImpl(transactionRepository);

            // gui
            this.primaryStage = primaryStage;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./view/LoginView.fxml"));
            loader.setController(new LoginViewController());
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setFullScreenExitHint("");
            primaryStage.setFullScreen(true);
            URL imageUrl = this.getClass().getResource("./media/icon.png");
            primaryStage.getIcons().add(new Image(String.valueOf(imageUrl)));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class LoginViewController {

        @FXML
        private TextField username;

        @FXML
        private PasswordField password;

        @FXML
        void login(ActionEvent event) {
            User user = userService.login(username.getText(), password.getText());
            if (user != null){
                if (user.isAdmin()){
                    try {
                        contextHolder = new ContextHolderImpl();
                        contextHolder.setCurrentUser(userService.login(username.getText(), password.getText()));
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/AdminView.fxml"));
                        loader.setController(new AdminViewController());
                        Parent root = loader.load();
                        primaryStage.getScene().setRoot(root);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        contextHolder = new ContextHolderImpl();
                        contextHolder.setCurrentUser(userService.login(username.getText(), password.getText()));
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/RegularView.fxml"));
                        loader.setController(new RegularViewController());
                        Parent root = loader.load();
                        primaryStage.getScene().setRoot(root);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                // TODO: show error message
            }
        }

    }

    private class AdminViewController implements Initializable {
        @FXML
        private AnchorPane sidebar;

        @FXML
        private Label username;

        @FXML
        private Button logout;

        @FXML
        private ComboBox<Long> userIdReport;

        @FXML
        private ComboBox<Long> userIdEdit;

        @FXML
        private TextField usernameEdit;

        @FXML
        private TextField passwordEdit;

        @FXML
        private Button delete;

        @FXML
        private TextField usernameAdd;

        @FXML
        private TextField passwordAdd;

        @FXML
        private TableView<?> userInformation;

        @FXML
        private TableView<?> userReport;

        @FXML
        private Label addError;

        @FXML
        private Label editError;

        @FXML
        private CheckBox isAdminAdd;

        @FXML
        private CheckBox isAdminEdit;

        @FXML
        void addUser(ActionEvent event) {
            if(userService.create(usernameAdd.getText(), passwordAdd.getText(), isAdminAdd.isSelected()) != null){
                ErrorMessage message = new ErrorMessage();
                message.display(addError, "User successfully added!");
                usernameAdd.setText("");
                passwordAdd.setText("");
                isAdminAdd.setSelected(false);
                initialize();
            }
        }

        @FXML
        void deleteUser(ActionEvent event) {
            if(userService.delete(userService.viewById(userIdEdit.getValue()).getId())){
                ErrorMessage message = new ErrorMessage();
                message.display(editError, "User successfully deleted!");
                initialize();
            }
        }

        @FXML
        void updateUser(ActionEvent event) {
            User user = new User();
            user.setId(userIdEdit.getValue());
            user.setUsername(usernameEdit.getText());
            user.setPassword(passwordEdit.getText());
            user.setAdmin(isAdminEdit.isSelected());
            if(userService.update(user) != null){
                ErrorMessage message = new ErrorMessage();
                message.display(editError, "User successfully updated!");
                initialize();
            }
        }

        @FXML
        void userIdEdit(ActionEvent event) {
            try{
                usernameEdit.setText(userService.viewById(userIdEdit.getValue()).getUsername());
                passwordEdit.setText(userService.viewById(userIdEdit.getValue()).getPassword());
                isAdminEdit.setSelected(userService.viewById(userIdEdit.getValue()).isAdmin());
            }catch (NullPointerException e){}
        }

        @FXML
        void userIdReport(ActionEvent event) {
            updateTables();
        }

        @FXML
        void logout(ActionEvent event) {
            try {
                contextHolder = new ContextHolderImpl();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("view/LoginView.fxml"));
                loader.setController(new LoginViewController());
                Parent root = loader.load();
                primaryStage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void emptyTable(TableView table){
            table.getItems().clear();
        }

        public void initialize(){

            try{
                // comboBoxes
                List<Long> list = new ArrayList<Long>();

                for (User user : userService.viewAll()){
                    list.add(user.getId());
                }

                ObservableList userIds = FXCollections.observableList(list);

                userIdReport.setItems(userIds);
                userIdEdit.setItems(userIds);

                userIdReport.getSelectionModel().selectFirst();
                userIdEdit.getSelectionModel().selectFirst();

                // tables
                emptyTable(userInformation);
                emptyTable(userReport);

                for (User user : userService.viewAll()){
                    TableViewReflection.dataToTable(user, userInformation);
                }

                for (Transaction transaction : transactionService.userReport(userIdReport.getValue())){
                    TableViewReflection.dataToTable(transaction, userReport);
                }

                // fields
                usernameEdit.setText(userService.viewById(userIdEdit.getValue()).getUsername());
                passwordEdit.setText(userService.viewById(userIdEdit.getValue()).getPassword());
                isAdminEdit.setSelected(userService.viewById(userIdEdit.getValue()).isAdmin());
            }catch (NullPointerException e) {}
        }

        public void updateTables(){
            try{
                emptyTable(userInformation);
                emptyTable(userReport);

                for (User user : userService.viewAll()){
                    TableViewReflection.dataToTable(user, userInformation);
                }

                for (Transaction transaction : transactionService.userReport(userIdReport.getValue())){
                    TableViewReflection.dataToTable(transaction, userReport);
                }
            }catch (NullPointerException e) {}
        }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            username.setText("Logged in:   " + contextHolder.getCurrentUser().getUsername());
            initialize();
        }
    }

    private class RegularViewController implements Initializable{
        @FXML
        private AnchorPane sidebar;

        @FXML
        private Label username;

        @FXML
        private Button logout;

        @FXML
        private TableView<?> clientInformation;

        @FXML
        private ComboBox<Long> clientIdUpdate;

        @FXML
        private TextField clientNameUpdate;

        @FXML
        private TextField clientFirstNameUpdate;

        @FXML
        private TextField clientIdentityCardNumberUpdate;

        @FXML
        private TextField clientCnpUpdate;

        @FXML
        private TextField clientAddressUpdate;

        @FXML
        private TextField clientNameAdd;

        @FXML
        private TextField clientFirstNameAdd;

        @FXML
        private TextField clientIdentityCardNumberAdd;

        @FXML
        private TextField clientCnpAdd;

        @FXML
        private TextField clientAddressAdd;

        @FXML
        private Label addClientError;

        @FXML
        private Label updateClientError;

        @FXML
        private TableView<?> accountInformation;

        @FXML
        private ComboBox<Long> localAccountIdUpdate;

        @FXML
        private TextField clientIdupdate;

        @FXML
        private TextField accountIdUpdate;

        @FXML
        private TextField accountTypeUpdate;

        @FXML
        private TextField accountMoneyUpdate;

        @FXML
        private TextField accountDateUpdate;

        @FXML
        private TextField accountIdAdd;

        @FXML
        private TextField accountTypeAdd;

        @FXML
        private TextField accountMoneyAdd;

        @FXML
        private TextField accountDateAdd;

        @FXML
        private Button delete;

        @FXML
        private Label addAccountError;

        @FXML
        private Label updateAccountError;

        @FXML
        private Button accountUpdate;

        @FXML
        private ComboBox<Long> userIdAccountAdd;

        @FXML
        private ComboBox<String> senderAccountTransaction;

        @FXML
        private ComboBox<String> recieverAccountTransaction;

        @FXML
        private Label transactionError;

        @FXML
        private Label transferError;

        @FXML
        private ComboBox<String> senderAccountTransfer;

        @FXML
        private TextField destinationTransfer;

        @FXML
        private TextField transactionAmount;

        @FXML
        private TextField transferAmount;

        @FXML
        void accountAdd(ActionEvent event) {
            if(accountService.create(userIdAccountAdd.getValue(), accountTypeAdd.getText()) != null){
                ErrorMessage message = new ErrorMessage();
                message.display(addAccountError, "Account successfully added!");
                accountTypeAdd.setText("");
                initializeData();
            }
        }

        @FXML
        void accountDelete(ActionEvent event) {
            if(accountService.delete(accountService.viewById(localAccountIdUpdate.getValue()).getId())){
                ErrorMessage message = new ErrorMessage();
                message.display(updateAccountError, "Account successfully deleted!");
                initializeData();
            }
        }

        @FXML
        void accountUpdate(ActionEvent event) {
            Account account = accountService.viewById(localAccountIdUpdate.getValue());
            account.setMoneyAmount(Double.valueOf(accountMoneyUpdate.getText()));
            account.setType(accountTypeUpdate.getText());
            if(accountService.update(account) != null){
                ErrorMessage message = new ErrorMessage();
                message.display(updateAccountError, "Account successfully updated!");
                initializeData();
            }
        }

        @FXML
        void clientAdd(ActionEvent event) {
            if(clientService.add(clientNameAdd.getText(), clientFirstNameAdd.getText(), clientIdentityCardNumberAdd.getText(), clientCnpAdd.getText(), clientAddressAdd.getText()) != null){
                ErrorMessage message = new ErrorMessage();
                message.display(addClientError, "Client successfully added!");
                clientNameAdd.setText("");
                clientFirstNameAdd.setText("");
                clientIdentityCardNumberAdd.setText("");
                clientCnpAdd.setText("");
                clientAddressAdd.setText("");
                initializeData();
                updateTables();
            }
        }

        @FXML
        void clientIdUpdate(ActionEvent event) {
            try{
                clientNameUpdate.setText(clientService.viewById(clientIdUpdate.getValue()).getName());
                clientFirstNameUpdate.setText(clientService.viewById(clientIdUpdate.getValue()).getFirstName());
                clientIdentityCardNumberUpdate.setText(clientService.viewById(clientIdUpdate.getValue()).getIdentityCardNumber());
                clientCnpUpdate.setText(clientService.viewById(clientIdUpdate.getValue()).getCnp());
                clientAddressUpdate.setText(clientService.viewById(clientIdUpdate.getValue()).getAddress());
            }catch (NullPointerException e){}
        }

        @FXML
        void clientUpdate(ActionEvent event) {
            Client client = new Client();
            client.setId(clientIdUpdate.getValue());
            client.setName(clientNameUpdate.getText());
            client.setFirstName(clientFirstNameUpdate.getText());
            client.setIdentityCardNumber(clientIdentityCardNumberUpdate.getText());
            client.setCnp(clientCnpUpdate.getText());
            client.setAddress(clientAddressUpdate.getText());
            if(clientService.update(client) != null){
                ErrorMessage message = new ErrorMessage();
                message.display(updateClientError, "Client successfully updated!");
                initializeData();
                updateTables();
            }
        }

        @FXML
        void localAccountIdUpdate(ActionEvent event) {
            try {
                clientIdupdate.setText(accountService.viewById(localAccountIdUpdate.getValue()).getClientId().toString());
                accountIdUpdate.setText(accountService.viewById(localAccountIdUpdate.getValue()).getAccountId());
                accountTypeUpdate.setText(accountService.viewById(localAccountIdUpdate.getValue()).getType());
                accountMoneyUpdate.setText(accountService.viewById(localAccountIdUpdate.getValue()).getMoneyAmount().toString());
                accountDateUpdate.setText(accountService.viewById(localAccountIdUpdate.getValue()).getDateOfCreation());
            } catch (NullPointerException e) {}
        }

        @FXML
        void logout(ActionEvent event) {
            try {
                contextHolder = new ContextHolderImpl();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("view/LoginView.fxml"));
                loader.setController(new LoginViewController());
                Parent root = loader.load();
                primaryStage.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @FXML
        void sendTransaction(ActionEvent event) {
            try {
                Double amount = Double.valueOf(transactionAmount.getText());
                Long idSender = accountService.viewByAccountId(senderAccountTransaction.getValue()).getId();
                Long idReciever = accountService.viewByAccountId(recieverAccountTransaction.getValue()).getId();

                accountService.takeMoney(idSender, amount);
                accountService.addMoney(idReciever, amount);

                transactionService.create(accountService.viewByAccountId(senderAccountTransaction.getValue()).getAccountId(), accountService.viewByAccountId(recieverAccountTransaction.getValue()).getAccountId(), amount, "Transaction",contextHolder.getCurrentUser().getId());
                ErrorMessage message = new ErrorMessage();
                message.display(transactionError, "Money successfully transferred!");
                initializeData();
            } catch (NullPointerException e) {}
        }

        @FXML
        void sendTransfer(ActionEvent event) {
            try {
                Double amount = Double.valueOf(transferAmount.getText());
                Long idSender = accountService.viewByAccountId(senderAccountTransfer.getValue()).getId();

                accountService.takeMoney(idSender, amount);

                transactionService.create(accountService.viewByAccountId(senderAccountTransfer.getValue()).getAccountId(), destinationTransfer.getText(), amount, "Transfer",contextHolder.getCurrentUser().getId());
                ErrorMessage message = new ErrorMessage();
                message.display(transferError, "Money successfully transferred!");
                initializeData();
            } catch (NullPointerException e) {}
        }

        public void emptyTable(TableView table){
            table.getItems().clear();
        }

        public void updateTables(){
            try{
                emptyTable(clientInformation);
                emptyTable(accountInformation);

                for (Client client : clientService.viewAll()){
                    TableViewReflection.dataToTable(client, clientInformation);
                }
                for (Account account : accountService.viewAll()){
                    TableViewReflection.dataToTable(account, accountInformation);
                }
            }catch (NullPointerException e) {}
        }

        public void initializeData(){

            try{
                // comboBoxes
                List<Long> clientList = new ArrayList<Long>();

                for (Client client : clientService.viewAll()){
                    clientList.add(client.getId());
                }

                ObservableList clientIds = FXCollections.observableList(clientList);

                clientIdUpdate.setItems(clientIds);
                clientIdUpdate.getSelectionModel().selectFirst();

                userIdAccountAdd.setItems(clientIds);
                userIdAccountAdd.getSelectionModel().selectFirst();

                List<Long> accountList = new ArrayList<Long>();
                List<String> accountIdList = new ArrayList<String>();

                for (Account account : accountService.viewAll()){
                    accountList.add(account.getId());
                    accountIdList.add(account.getAccountId());
                }

                ObservableList accountsLocalId = FXCollections.observableList(accountList);

                localAccountIdUpdate.setItems(accountsLocalId);
                localAccountIdUpdate.getSelectionModel().selectFirst();

                ObservableList accountIds = FXCollections.observableList(accountIdList);

                senderAccountTransaction.setItems(accountIds);
                senderAccountTransaction.getSelectionModel().selectFirst();

                senderAccountTransfer.setItems(accountIds);
                senderAccountTransfer.getSelectionModel().selectFirst();

                recieverAccountTransaction.setItems(accountIds);
                recieverAccountTransaction.getSelectionModel().selectFirst();

                // fields
                clientNameUpdate.setText(clientService.viewById(clientIdUpdate.getValue()).getName());
                clientFirstNameUpdate.setText(clientService.viewById(clientIdUpdate.getValue()).getFirstName());
                clientIdentityCardNumberUpdate.setText(clientService.viewById(clientIdUpdate.getValue()).getIdentityCardNumber());
                clientCnpUpdate.setText(clientService.viewById(clientIdUpdate.getValue()).getCnp());
                clientAddressUpdate.setText(clientService.viewById(clientIdUpdate.getValue()).getAddress());

                clientIdupdate.setText(accountService.viewById(localAccountIdUpdate.getValue()).getClientId().toString());
                accountIdUpdate.setText(accountService.viewById(localAccountIdUpdate.getValue()).getAccountId());
                accountTypeUpdate.setText(accountService.viewById(localAccountIdUpdate.getValue()).getType());
                accountMoneyUpdate.setText(accountService.viewById(localAccountIdUpdate.getValue()).getMoneyAmount().toString());
                accountDateUpdate.setText(accountService.viewById(localAccountIdUpdate.getValue()).getDateOfCreation());

                accountMoneyAdd.setText("0");
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                accountDateAdd.setText(formatter.format(date));

                // tables
                emptyTable(clientInformation);
                emptyTable(accountInformation);

                for (Client client : clientService.viewAll()){
                    TableViewReflection.dataToTable(client, clientInformation);
                }
                for (Account account : accountService.viewAll()){
                    TableViewReflection.dataToTable(account, accountInformation);
                }

            }catch (NullPointerException e) {}
        }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            username.setText("Logged in:     " + contextHolder.getCurrentUser().getUsername());
            initializeData();
            updateTables();
            accountMoneyAdd.setText("0");
            accountMoneyAdd.setEditable(false);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            accountDateAdd.setText(formatter.format(date));
        }
    }

    private class ErrorMessage {
        public void display(Label error, String message) {

            final Runnable show = new Runnable() {
                @Override
                public void run() {
                    try {
                        error.setText(message);
                        error.setVisible(true);
                    } catch(final Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            final Runnable hide = new Runnable() {

                @Override
                public void run() {
                    try {
                        error.setVisible(false);
                    } catch(final Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            final Thread thread = new Thread(() -> {
                try {
                    Platform.runLater(show);
                    Thread.sleep(5000);
                    Platform.runLater(hide);
                } catch(final Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }

}
