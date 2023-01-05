package DAL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;

public class MyDatabaseConnector {
    private SQLServerDataSource dataSource; //variable for creating a connection to the DB

    public MyDatabaseConnector() {
        dataSource = new SQLServerDataSource();
        dataSource.setServerName("10.176.111.31");
        dataSource.setDatabaseName("Netflicks");
        dataSource.setUser("CSe22A_27");
        dataSource.setPassword("CSe22A_27");
        dataSource.setTrustServerCertificate(true);
    }

    public Connection getConnection() throws SQLServerException {
        System.out.println("Connection to DB is successful");
        return dataSource.getConnection();
    }
}


