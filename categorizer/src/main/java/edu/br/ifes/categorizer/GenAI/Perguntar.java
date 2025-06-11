package edu.br.ifes.categorizer.GenAI;

import edu.br.ifes.categorizer.EmailAPI.Model.Email;

public class Perguntar {
    public static String getStatusEmail(Email email){
        try {
            String resposta = GeminiAPI.perguntar(
                    "Com base no assunto: '" + email.getSubject() + "' e no corpo: '" + email.getBody() + "', diga qual o status do processo de defesa de mestrado o conteúdo representa. " +
                    "Os possíveis status são: "
                    + "DADOS_INICIAIS, "
                    + "DADOS_INICIAIS_INCORRETOS, "
                    + "CONFIRMACAO_DEFESA, "
                    + "DADOS_DETALHADOS_DEFESA, "
                    + "DADOS_DETALHADOS_DEFESA_INCORRETOS, "
                    + "ANUENCIA_COORDENACAO, "
                    + "DEFESA_CONCLUIDA, "
                    + "ou INDEFINIDO. "
                    + "Se o assunto for exatamente 'Defesa' (com qualquer capitalização), o status deve ser DADOS_INICIAIS ou DADOS_INICIAIS_INCORRETOS (Inclusive so nesse caso que sera preenchido com esse status). "
                    + "Retorne apenas e exatamente UM dos status listados acima, sem aspas, sem pontuação e sem explicações adicionais."
            );


            return resposta;
        }catch (Exception e){
            return "INDEFINIDO";
        }

    }
}
