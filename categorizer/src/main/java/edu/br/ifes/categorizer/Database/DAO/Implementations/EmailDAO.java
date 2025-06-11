package edu.br.ifes.categorizer.Database.DAO.Implementations;

import edu.br.ifes.categorizer.Database.DAO.Interfaces.IEmailDAO;
import edu.br.ifes.categorizer.Database.DatabaseConnection;
import edu.br.ifes.categorizer.EmailAPI.Model.Email;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmailDAO implements IEmailDAO {
    @Override
    public void insert(Email email){
        String sql = "INSERT INTO emails (uid, subject, sender, date, body, attachment_paths, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance();
             PreparedStatement stmt = connection.prepareStatement(sql)){

            Timestamp date = new Timestamp(email.getDate().getTime());

            stmt.setLong(1, email.getUid());
            stmt.setString(2, email.getSubject());
            stmt.setString(3, email.getSender());
            stmt.setTimestamp(4, date);
            stmt.setString(5, email.getBody());
            stmt.setArray(6, connection.createArrayOf("text", email.getAttachmentPaths().toArray()));
            stmt.setString(7, email.getStatus());
            stmt.executeUpdate();

        }catch (SQLException e){
            System.out.println("Erro ao inserir dados do email com UID: " + email.getUid());
        }
    }
    @Override
    public List<Email> findAll() {
        String sql = "SELECT uid, subject, sender, date, body, attachment_paths, status FROM emails";
        List<Email> emails = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Email email = new Email();
                email.setUid(rs.getLong("uid"));
                email.setSubject(rs.getString("subject"));
                email.setSender(rs.getString("sender"));
                email.setDate(rs.getTimestamp("date"));
                email.setBody(rs.getString("body"));
                email.setStatus(rs.getString("status"));


                Array sqlArray = rs.getArray("attachment_paths");
                if (sqlArray != null) {
                    String[] paths = (String[]) sqlArray.getArray();
                    email.setAttachmentPaths(Arrays.asList(paths));
                }

                emails.add(email);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar emails.");
        }

        return emails;
    }


}
