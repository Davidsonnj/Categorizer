package edu.br.ifes.categorizer.GenAI;

import edu.br.ifes.categorizer.EmailAPI.Model.Email;

public class Perguntar {
    public static String getStatusEmail(Email email){
        try {
            String resposta = GeminiAPI.perguntar(
                    "Com base no assunto: '" + email.getSubject() + "' e no corpo: '" + email.getBody() + "', diga qual o status do processo de defesa de mestrado o conteúdo representa. " +
                    "Os possíveis status são: "
                    + "DADOS_INICIAIS (quando houver nome do aluno, título da dissertação e email do aluno), , "
                    + "DADOS_INICIAIS_INCORRETOS, "
                    + "CONFIRMACAO_DEFESA (quando o email confirma ou não a continuidade da defesa), "
                    + "DADOS_DETALHADOS_DEFESA (quando há data, hora, local e banca - incluindo nome, email e opcionalmente mini currículo), "
                    + "DADOS_DETALHADOS_DEFESA_INCORRETOS, "
                    + "ANUENCIA_COORDENACAO (quando há autorização ou recusa com justificativa), "
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
