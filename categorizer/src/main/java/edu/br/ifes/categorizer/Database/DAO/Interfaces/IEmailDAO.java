package edu.br.ifes.categorizer.Database.DAO.Interfaces;

import edu.br.ifes.categorizer.EmailAPI.Model.Email;

import java.util.List;

public interface IEmailDAO {
    void insert(Email email);
    List<Email> findAll();
}
