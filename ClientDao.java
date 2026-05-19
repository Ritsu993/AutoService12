package ru.kafpin.autoservice.dao.interfaces;

import ru.kafpin.autoservice.entity.core.Client;
import ru.kafpin.autoservice.enums.ClientType;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ClientDao extends BaseDao<Client, Integer> {
    List<Client> findByClientType(ClientType clientType) throws SQLException;
    Optional<Client> findByPhone(String phone) throws SQLException;
    Optional<Client> findByEmail(String email) throws SQLException;
    List<Client> findByCompanyName(String companyName) throws SQLException;
    List<Client> searchByName(String firstName, String lastName) throws SQLException;
}