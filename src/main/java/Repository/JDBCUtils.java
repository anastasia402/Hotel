package Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class JDBCUtils {
    private static final String urlDataBase = "jdbc:sqlite:/Users/anastasiacutulima/Desktop/siemens/siemensHotel/Untitled";

    private static final Logger logger= (Logger) LogManager.getLogger();

    public JDBCUtils() {}

    private Connection connection=null;

    private Connection getNewConnection() throws SQLException {
        logger.traceEntry();

        return DriverManager.getConnection(urlDataBase);
    }

    public Connection getConnection(){
        logger.traceEntry();
        try {
            if (connection==null || connection.isClosed())
                connection=getNewConnection();

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(connection);
        return connection;
    }
}
