package utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Tian Qi Qing
 * @version 1.0
 * @date 2021/07/18/12:59
 **/
@Slf4j
public class DbUtils {

    private static  HikariDataSource ds;

    //从连接池中取用一个连接
    public static  HikariDataSource getDs(){
        if (ds == null) {
            try {
                HikariConfig config = new HikariConfig();
                config.setJdbcUrl("jdbc:mysql://localhost:3306/bws?rewriteBatchedStatements=true");
                config.setUsername("root");
                config.setPassword("root");
                config.addDataSourceProperty("cachePrepStmts", "true");
                config.addDataSourceProperty("prepStmtCacheSize", "250");
                config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                ds = new HikariDataSource(config);
            } catch(Exception ex) {
                ex.printStackTrace();
                log.info(ex.getLocalizedMessage());
            }
        }
        return ds;
    }


    //释放连接回连接池
    public static void close(Connection conn, PreparedStatement pst, ResultSet rs){
        if(rs!=null){
            try {
                 rs.close();
            } catch (SQLException e) {
                 log.error("Exception in DbUtils!", e);
            }
        }
        if(pst!=null){
            try {
                 pst.close();
            } catch (SQLException e) {
                log.error("Exception in DbUtils!", e);
            }
        }

        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("Exception in DbUtils!", e);
            }
        }
    }


     public static void executeBatchUpdate(Connection conn, String sql, List<List<?>> paramsList) {
        PreparedStatement stmt = null;
         try {
             conn.setAutoCommit(false);
             stmt = conn.prepareStatement(sql);
             if(paramsList!=null&&paramsList.size()!=0) {
                 for (List<?> params : paramsList) {
                     for (int i = 0; i < params.size(); i++) {
                         stmt.setObject(i + 1, params.get(i));
                     }
                     stmt.addBatch();
                 }
                 stmt.executeBatch();
                 conn.commit();
                 stmt.clearBatch();
             }
         }catch(Exception ex){
             log.error(ex.getMessage());
         }finally {
             close(conn, stmt,null );
         }

     }

}
