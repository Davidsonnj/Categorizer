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

    1.  **Verificação de DADOS_INICIAIS (Prioridade Máxima):**
        * Para o status `DADOS_INICIAIS`, o corpo do e-mail DEVE conter um valor para CADA UM dos seguintes campos:
            - Nome completo do aluno (deve conter nome e sobrenome)
            - Título da dissertação
            - E-mail de contato do aluno
            - Data da defesa
            - Hora da defesa
            - Local da defesa
            - Nome do co-orientador (Opcional)
            - Email do co-orientador (Opcional)
            - Lista da banca examinadora: Deve conter pelo menos um membro com NOME (um primeiro nome é suficiente) e E-MAIL.
        * Se TODAS essas informações estiverem presentes (exceto as informações do co-orientador, pois ẽ opcional), o status é **`DADOS_INICIAIS`**. Ignore o assunto completamente neste caso.

    2.  **Verificação de DOCUMENTO_DISSERTACAO:**
        * Se a Regra 1 não for satisfeita, analise os anexos.
        * Se houver anexos (com extensão .pdf, .docx, .odt) E o nome do arquivo ou o corpo do e-mail contiver palavras como "dissertação", "tese", "trabalho", "versão final" ou "documento", o status é **`DOCUMENTO_DISSERTACAO`**.

    3.  **Verificação de DADOS_INICIAIS_INCORRETOS:**
        * Se as Regras 1 e 2 não forem satisfeitas, verifique se o assunto contém a palavra 'Defesa' (ignorando maiúsculas/minúsculas).
        * Se 'Defesa' estiver no assunto E a verificação da Regra 1 falhou, o status é **`DADOS_INICIAIS_INCORRETOS`**.

    4.  **Outras Regras (se nenhuma das anteriores se aplicar):**
        * **`CONFIRMACAO_DEFESA`**: O corpo do e-mail indica uma confirmação (ex: "confirmo presença", "estarei lá") ou negação (ex: "não poderei participar", "Não comparecerei", "Não participarei") sobre uma defesa já agendada.
        * **`ANUENCIA_COORDENACAO`**: O corpo do e-mail contém uma aprovação explícita da coordenação ("autorizo", "aprovado", "de acordo", "ciência da coordenação").
        * **`DEFESA_CONCLUIDA`**: O corpo do e-mail anexa ou menciona o resultado final, como "ata de defesa", "resultado da defesa", "aluno aprovado" ou "aluno reprovado".
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