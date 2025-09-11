package edu.br.ifes.categorizer.GenAI;

import edu.br.ifes.categorizer.EmailAPI.Model.Email;
import java.util.List;

public class Perguntar {

    private static final String PROMPT_TEMPLATE = """
        Você é um sistema de triagem automatizado para a secretaria de um programa de pós-graduação. Sua função é analisar o conteúdo de e-mails e aplicar um conjunto de regras de negócio para retornar UM ÚNICO status.
        
        Siga a ordem de prioridade das regras sem exceção. Verifique a Regra 1 completamente antes de considerar a Regra 2, e assim por diante.
        
        <DADOS_PARA_ANALISE>
            <ASSUNTO>%s</ASSUNTO>
            <CORPO>%s</CORPO>
            <ANEXOS>%s</ANEXOS>
        </DADOS_PARA_ANALISE>
        
        **STATUS POSSÍVEIS:**
        - DADOS_INICIAIS
        - DOCUMENTO_DISSERTACAO
        - DADOS_INICIAIS_INCORRETOS
        - CONFIRMACAO_DEFESA
        - ANUENCIA_COORDENACAO
        - DEFESA_CONCLUIDA
        - INDEFINIDO
        
        **REGRAS DE CLASSIFICAÇÃO (SIGA ESTRITAMENTE ESTA ORDEM DE PRIORIDADE):**
        
        1.  **Ações Específicas (Prioridade Máxima):**
            * **`CONFIRMACAO_DEFESA`**: O corpo do e-mail indica uma confirmação (ex: "confirmo presença", "estarei lá") ou negação (ex: "não poderei participar", "Não comparecerei", "Não participarei") sobre uma defesa já agendada.
            * **`ANUENCIA_COORDENACAO`**: O corpo do e-mail contém uma aprovação explícita da coordenação ("autorizo", "aprovado", "de acordo", "ciência da coordenação").
            * **`DEFESA_CONCLUIDA`**: O corpo do e-mail anexa ou menciona o resultado final, como "ata de defesa", "resultado da defesa", "aluno aprovado" ou "aluno reprovado".
        
        2.  **Submissão de Dados:**
            * **`DADOS_INICIAIS`**: O corpo do e-mail DEVE conter um valor para CADA UM dos seguintes campos: Nome completo do aluno (deve conter nome e sobrenome), Título da dissertação, E-mail de contato do aluno, Data da defesa, Hora da defesa, Local da defesa, Nome do co-orientador (Opcional), Email do co-orientador (Opcional), e Lista da banca examinadora (Deve conter pelo menos um membro com NOME e E-MAIL). Se todas essas informações estiverem presentes, o status é **`DADOS_INICIAIS`**.
            * **`DADOS_INICIAIS_INCORRETOS`**: Se a regra `DADOS_INICIAIS` falhou E o assunto do e-mail contém a palavra 'Defesa' (ignorando maiúsculas/minúsculas), o status é **`DADOS_INICIAIS_INCORRETOS`**.
        
        3.  **Submissão de Documento:**
            * **`DOCUMENTO_DISSERTACAO`**: Se houver anexos (com extensão .pdf, .docx, .odt) E o nome do arquivo ou o corpo do e-mail contiver palavras como "dissertação", "tese", "trabalho", "versão final" ou "documento", o status é **`DOCUMENTO_DISSERTACAO`**.
        
        4.  **Fallback (Prioridade Mínima):**
            * **`INDEFINIDO`**: Se o conteúdo não se encaixar claramente em nenhuma categoria acima.
        
        **INSTRUÇÕES DE SAÍDA:**
        Sua resposta deve ser APENAS e EXATAMENTE um dos status listados. Não inclua aspas, pontuação ou explicações.
        
        **STATUS:**
        """;


    public static String getStatusEmail(Email email) {
        try {
            List<String> attachmentPaths = email.getAttachmentPaths();
            String attachmentsAsString = (attachmentPaths == null || attachmentPaths.isEmpty())
                    ? "Nenhum"
                    : String.join(", ", attachmentPaths);

            String promptFinal = String.format(PROMPT_TEMPLATE,
                    email.getSubject(),
                    email.getBody(),
                    attachmentsAsString);

            String resposta = GeminiAPI.perguntar(promptFinal);

            return resposta.trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "INDEFINIDO";
        }
    }
}