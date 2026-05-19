package ru.kafpin.autoservice.dao.impl;

import lombok.RequiredArgsConstructor;
import ru.kafpin.autoservice.dao.interfaces.ClientDao;
import ru.kafpin.autoservice.entity.core.Client;
import ru.kafpin.autoservice.enums.ClientType;
import ru.kafpin.autoservice.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ClientDaoImpl implements ClientDao {

    private final DatabaseConnection dbConnection;

    private Connection getConnection() throws SQLException {
        return dbConnection.getConnection();
    }

    private Client mapResultSet(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        return Client.builder()
                .id(rs.getInt("id"))
                .clientType(ClientType.valueOf(rs.getString("client_type").toUpperCase()))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .middleName(rs.getString("middle_name"))
                .companyName(rs.getString("company_name"))
                .phone(rs.getString("phone"))
                .email(rs.getString("email"))
                .address(rs.getString("address"))
                .inn(rs.getString("inn"))
                .createdAt(createdAt != null ? createdAt.toLocalDateTime() : null)
                .build();
    }

    @Override
    public Optional<Client> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<Client> findAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients ORDER BY id";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(mapResultSet(rs));
            }
        }
        return clients;
    }

    @Override
    public Client save(Client client) throws SQLException {
        String sql = "INSERT INTO clients (client_type, first_name, last_name, middle_name, company_name, phone, email, address, inn) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, client.getClientType().getValue());
            stmt.setString(2, client.getFirstName());
            stmt.setString(3, client.getLastName());
            stmt.setString(4, client.getMiddleName());
            stmt.setString(5, client.getCompanyName());
            stmt.setString(6, client.getPhone());
            stmt.setString(7, client.getEmail());
            stmt.setString(8, client.getAddress());
            stmt.setString(9, client.getInn());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Timestamp createdAt = rs.getTimestamp("created_at");
                return client.toBuilder()
                        .id(rs.getInt("id"))
                        .createdAt(createdAt != null ? createdAt.toLocalDateTime() : null)
                        .build();
            }
        }
        return client;
    }

    @Override
    public void update(Client client) throws SQLException {
        String sql = "UPDATE clients SET client_type = ?, first_name = ?, last_name = ?, middle_name = ?, " +
                "company_name = ?, phone = ?, email = ?, address = ?, inn = ? WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, client.getClientType().getValue());
            stmt.setString(2, client.getFirstName());
            stmt.setString(3, client.getLastName());
            stmt.setString(4, client.getMiddleName());
            stmt.setString(5, client.getCompanyName());
            stmt.setString(6, client.getPhone());
            stmt.setString(7, client.getEmail());
            stmt.setString(8, client.getAddress());
            stmt.setString(9, client.getInn());
            stmt.setInt(10, client.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM clients WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Client> findByClientType(ClientType clientType) throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE client_type = ? ORDER BY id";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, clientType.getValue());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                clients.add(mapResultSet(rs));
            }
        }
        return clients;
    }

    @Override
    public Optional<Client> findByPhone(String phone) throws SQLException {
        String sql = "SELECT * FROM clients WHERE phone = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public Optional<Client> findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM clients WHERE email = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
        }
    }

    @Override
    public List<Client> findByCompanyName(String companyName) throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE company_name ILIKE ? ORDER BY company_name";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + companyName + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                clients.add(mapResultSet(rs));
            }
        }
        return clients;
    }

    @Override
    public List<Client> searchByName(String firstName, String lastName) throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE first_name ILIKE ? AND last_name ILIKE ? ORDER BY last_name, first_name";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + firstName + "%");
            stmt.setString(2, "%" + lastName + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                clients.add(mapResultSet(rs));
            }
        }
        return clients;
    }
}