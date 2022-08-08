package ru.alishev.springcourse.dao;

import org.springframework.stereotype.Component;
import ru.alishev.springcourse.model.User;
import ru.alishev.springcourse.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDao {
    Util util = new Util();
    private final Connection connection;
    private List<User> userList = new ArrayList<>();

    public UserDao() {
        this.connection = util.getMySQLConnection();
    }

    public List<User> showUser() {
        try (Statement statement = connection.createStatement()) {
            String SQL = "SELECT * FROM User";
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setSurName(resultSet.getString("surName"));
                user.setEmail(resultSet.getString("email"));

                userList.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public User showUserWhereId(long id) throws SQLException {
        User user = new User();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id=?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            user.setId(resultSet.getLong("id"));
            user.setName(resultSet.getString("name"));
            user.setSurName(resultSet.getString("surName"));
            user.setEmail(resultSet.getString("email"));
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        return user;
    }

    public void save(User user) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user (name, surName, email) values (?, ?, ?)")) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.executeUpdate();
            System.out.println(user.getName() + " добавлен(а) в базу данных");
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
    }

    public void update(long id, User updateUser) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user SET name =?, surName=?, email =? WHERE id=?")) {
            preparedStatement.setString(1, updateUser.getName());
            preparedStatement.setString(2, updateUser.getSurName());
            preparedStatement.setString(3, updateUser.getEmail());
            preparedStatement.setLong(4,id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
    }

    public void delete(long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user WHERE id=?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
    }
}
