package com.mattoi.frambo_mock.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class ClientRepository {

        private final JdbcClient jdbcClient;
        private static final Logger log = LoggerFactory.getLogger(ClientRepository.class);

        public ClientRepository(JdbcClient jdbcClient) {
                this.jdbcClient = jdbcClient;
        }

        public boolean create(Client client) {
                var updated = jdbcClient
                                .sql("INSERT INTO Clients(client_name, email_address, phone_number) values(?,?,?)")
                                .params(List.of(client.name(), client.email(), client.phoneNumber())).update();
                return updated == 1;
        }

        public boolean update(Client updatedClient) {
                var updated = jdbcClient.sql(
                                "UPDATE Clients SET client_id = ?, client_name = ?, email_address = ?, phone_number = ? WHERE client_id = ?")
                                .params(List.of(updatedClient.id(), updatedClient.name(), updatedClient.email(),
                                                updatedClient.phoneNumber(), updatedClient.id()))
                                .update();
                return updated == 1;
        }

        public List<Client> findAll() {
                return jdbcClient.sql("SELECT * from clients")
                                .query((rows, rowNum) -> new Client(
                                                rows.getInt("client_id"),
                                                rows.getString("client_name"),
                                                rows.getString("email_address"),
                                                rows.getString("phone_number")))
                                .list();
        }

        public Client findById(Integer id) {
                return jdbcClient.sql("SELECT * from Clients WHERE client_id = :id")
                                .param("id", id).query((rows, rowNum) -> new Client(
                                                rows.getInt("client_id"),
                                                rows.getString("client_name"),
                                                rows.getString("email_address"),
                                                rows.getString("phone_number")))
                                .list().get(0);
        }

        public Client findByPhoneNumber(String phoneNumber) {
                return jdbcClient.sql("SELECT * from clients WHERE phone_number = :phone_number")
                                .param("phone_number", phoneNumber).query((rows, rowNum) -> new Client(
                                                rows.getInt("client_id"),
                                                rows.getString("client_name"),
                                                rows.getString("email_address"),
                                                rows.getString("phone_number")))
                                .list().get(0);
        }

        public boolean delete(Integer id) {
                var updated = jdbcClient.sql("DELETE FROM Clients WHERE client_id = :id")
                                .param("id", id).update();
                return updated == 1;
        }
}
