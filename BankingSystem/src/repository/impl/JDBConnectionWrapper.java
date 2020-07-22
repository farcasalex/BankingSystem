package repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBConnectionWrapper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASS = "avion2020";
    private static final int TIMEOUT = 5;

    private Connection connection;

    public JDBConnectionWrapper(String schemaName) {
        try {
            connection = DriverManager.getConnection(DB_URL + schemaName + "?useSSL=false", USER, PASS);
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        Statement statement = connection.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS client (\n" +
                "\tid BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "\tfirstname varchar(255) NOT NULL,\n" +
                "\tname varchar(255) NOT NULL,\n" +
                "    identity_card_number varchar(255) NOT NULL,\n" +
                "    cnp varchar(255) NOT NULL,\n" +
                "    address varchar(255) NOT NULL,\n" +
                "    PRIMARY KEY (id),\n" +
                "    UNIQUE KEY id_UNIQUE (id)) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=UTF8MB4;";
        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS user (\n" +
                "\tid BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "\tusername varchar(255) NOT NULL,\n" +
                "\tpassword varchar(255) NOT NULL,\n" +
                "    admin BOOLEAN NOT NULL,\n" +
                "    PRIMARY KEY (id),\n" +
                "    UNIQUE KEY id_UNIQUE (id)) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=UTF8MB4;";
        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS transaction (\n" +
                "\tid BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "    account_out_id varchar(255) NOT NULL,\n" +
                "\taccount_in_id varchar(255) NOT NULL,\n" +
                "\tmoney_amount double NOT NULL,\n" +
                "    type varchar(255) NOT NULL,\n" +
                "    userid BIGINT NOT NULL,\n" +
                "    PRIMARY KEY (id),\n" +
                "    UNIQUE KEY id_UNIQUE (id)) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=UTF8MB4;";
        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS account (\n" +
                "\tid BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "    clientid BIGINT NOT NULL,\n" +
                "    account_id varchar(255) NOT NULL,\n" +
                "\ttype varchar(255) NOT NULL,\n" +
                "\tmoney_amount double NOT NULL,\n" +
                "    date_of_creation varchar(255) NOT NULL,\n" +
                "    PRIMARY KEY (id),\n" +
                "    UNIQUE KEY id_UNIQUE (id),\n" +
                "    CONSTRAINT owner\n" +
                "\tFOREIGN KEY (clientid)\n" +
                "\tREFERENCES assig1.client (id)\n" +
                "\tON DELETE NO ACTION\n" +
                "\tON UPDATE NO ACTION) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=UTF8MB4;";
        statement.execute(sql);
    }

    public boolean testConnection() throws SQLException {
        return connection.isValid(TIMEOUT);
    }

    public Connection getConnection() {
        return connection;
    }
}
