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

            long uid = email.getUid();
            String subject = email.getSubject();
            String sender = email.getSender();
            String body = email.getBody();
            String status = email.getStatus().replace("\\", "").replaceAll("\\s+", "");

            Timestamp date = new Timestamp(email.getDate().getTime());

            stmt.setLong(1, uid);
            stmt.setString(2, subject);
            stmt.setString(3, sender);
            stmt.setTimestamp(4, date);
            stmt.setString(5, body);
            stmt.setArray(6, connection.createArrayOf("text", email.getAttachmentPaths().toArray()));
            stmt.setString(7, status);
            stmt.executeUpdate();

        }catch (SQLException e){
            System.out.println("Erro ao inserir dados do email com UID: " + email.getUid());
            e.printStackTrace();
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
                long uid = rs.getLong("uid");
                String subject = rs.getString("subject");
                String sender = rs.getString("sender");
                Timestamp date = rs.getTimestamp("date");
                String body = rs.getString("body");
                String status = rs.getString("status");

                Email email = new Email();
                email.setUid(uid);
                email.setSubject(subject);
                email.setSender(sender);
                email.setDate(date);
                email.setBody(body);
                email.setStatus(status);

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
