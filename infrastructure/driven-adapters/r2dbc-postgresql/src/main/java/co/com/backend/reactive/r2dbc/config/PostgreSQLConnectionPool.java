package co.com.backend.reactive.r2dbc.config;


import org.springframework.context.annotation.Configuration;


@Configuration
public class PostgreSQLConnectionPool {
    /* Change these values for your project */
//     public static final int INITIAL_SIZE = 12;
//     public static final int MAX_SIZE = 15;
//     public static final int MAX_IDLE_TIME = 30;
//     public static final int DEFAULT_PORT = 5432;

// 	@Bean
// 	public ConnectionPool getConnectionConfig(PostgresqlConnectionProperties properties) {
// 		PostgresqlConnectionConfiguration dbConfiguration = PostgresqlConnectionConfiguration.builder()
//                 .host(properties.host())
//                 .port(properties.port())
//                 .database(properties.database())
//                 .schema(properties.schema())
//                 .username(properties.username())
//                 .password(properties.password())
//                 .build();

//         ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration.builder()
//                 .connectionFactory(new PostgresqlConnectionFactory(dbConfiguration))
//                 .name("api-postgres-connection-pool")
//                 .initialSize(INITIAL_SIZE)
//                 .maxSize(MAX_SIZE)
//                 .maxIdleTime(Duration.ofMinutes(MAX_IDLE_TIME))
//                 .validationQuery("SELECT 1")
//                 .build();

// 		return new ConnectionPool(poolConfiguration);
// 	}
}