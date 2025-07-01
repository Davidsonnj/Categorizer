package edu.br.ifes.categorizer.GenAI;

import edu.br.ifes.categorizer.EmailAPI.Model.Email;

public class MainTest {
    public static void main(String[] args) {
        Email email = new Email();

        // 0. DADOS_INICIAIS — número negativo
        email.setSubject("Defesa");
        email.setBody("-1");
        System.out.println("DADOS_INICIAIS_INCORRETOS: " + Perguntar.getStatusEmail(email));

        // 1. DADOS_INICIAIS — assunto correto + dados completos
        email.setSubject("Defesa");
        email.setBody("Nome: João da Silva\nTítulo: Sistemas Inteligentes\nEmail: joao.silva@ifes.br");
        System.out.println("DADOS_INICIAIS: " + Perguntar.getStatusEmail(email));

        // 2. DADOS_INICIAIS_INCORRETOS — assunto correto + dados faltando
        email.setSubject("defesa");
        email.setBody("Nome: João da Silva\nTítulo: Sistemas Inteligentes");
        System.out.println("DADOS_INICIAIS_INCORRETOS: " + Perguntar.getStatusEmail(email));

        // 3. CONFIRMACAO_DEFESA — confirmação textual
        email.setSubject("Confirmação da defesa");
        email.setBody("Confirmamos a defesa de João marcada para 15 de julho às 14h.");
        System.out.println("CONFIRMACAO_DEFESA: " + Perguntar.getStatusEmail(email));

        // 4. DADOS_DETALHADOS_DEFESA — inclui data, hora, local e banca
        email.setSubject("Detalhes da defesa");
        email.setBody("Data: 15/07/2024\nHora: 14h\nLocal: Auditório A\nBanca:\nProf. A (emailA@ifes.br)\nProf. B (emailB@ifes.br)");
        System.out.println("DADOS_DETALHADOS_DEFESA: " + Perguntar.getStatusEmail(email));

        // 5. DADOS_DETALHADOS_DEFESA_INCORRETOS — estrutura incompleta ou errada
        email.setSubject("Detalhes");
        email.setBody("Data: 15/07\nLocal: ???\nBanca: João");
        System.out.println("DADOS_DETALHADOS_DEFESA_INCORRETOS: " + Perguntar.getStatusEmail(email));

        // 6. ANUENCIA_COORDENACAO — aceite ou recusa justificada
        email.setSubject("Anuência Coordenação");
        email.setBody("Autorizamos a realização da defesa.\nJustificativa: Documentação OK.");
        System.out.println("ANUENCIA_COORDENACAO: " + Perguntar.getStatusEmail(email));

        // 7. DEFESA_CONCLUIDA — texto indicando finalização
        email.setSubject("Defesa finalizada");
        email.setBody("Informamos que a defesa foi concluída com êxito.");
        System.out.println("DEFESA_CONCLUIDA: " + Perguntar.getStatusEmail(email));

        // 8. INDEFINIDO — tudo inválido
        email.setSubject("Hello");
        email.setBody("Mensagem aleatória sem nenhuma informação útil.");
        System.out.println("INDEFINIDO: " + Perguntar.getStatusEmail(email));

        // 9. DADOS_INICIAIS_INCORRETOS — exceção (corpo nulo)
        email.setSubject("Defesa");
        email.setBody(null);
        System.out.println("DADOS_INICIAIS_INCORRETOS: " + Perguntar.getStatusEmail(email));

        // 10. DADOS_INICIAIS - dados corretos, so que com um texto enorme:
        email.setSubject("Defesa");

    }
}

