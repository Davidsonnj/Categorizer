package edu.br.ifes.categorizer.GenAI;

import edu.br.ifes.categorizer.EmailAPI.Model.Email;
import java.util.List;

public class Perguntar {

    // PROMPT AJUSTADO COM A LÓGICA DE PRIORIDADE CORRIGIDA
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
        
        2.  **Submissão de Dados Completos (Priorize verificar primeiro os DADOS_INICIAIS):**
            * **`DADOS_INICIAIS`**: Se as regras anteriores não se aplicarem, verifique se o corpo do e-mail DEVE conter um valor para CADA UM dos seguintes campos: Tipo de defesa (deve indicar se é Exame de Qualificação ou Defesa de Diseertação), Nome completo do aluno (deve conter nome e sobrenome), Título da dissertação, E-mail de contato do aluno, Data da defesa, Hora da defesa, Local da defesa, Nome do co-orientador (Opcional), Email do co-orientador (Opcional), e Lista da banca examinadora (Deve conter pelo menos um membro com NOME e E-MAIL). Se todas essas informações estiverem presentes, o status é **`DADOS_INICIAIS`**.
            * **`DADOS_INICIAIS_VINCULACAO**: Se as regras anteriores não se aplicarem, verifique se o corpo do e-mail DEVE conter um valor para CADA UM dos seguintes campos: Nome completo do aluno (deve conter nome e sobrenome), Mátrícula do aluno, E-mail de contato do aluno, período em que o aluno início o curso, Tema. Se todas essas informações estiverem presentes, o status é **`DADOS_INICIAIS_VINCULACAO`**.

        3.  **Recebimento de Dados de Avaliadores:**
            * **`DADOS_MEMBROS_BANCA`**: Se as regras anteriores não se aplicarem, verifique se o corpo do e-mail contém as informações necessárias para cadastrar um membro de banca no sistema. Os campos OBRIGATÓRIOS são:
                - Nome completo
                - Data de nascimento
                - CPF
                - Telefone
        
                - **Formação acadêmica obrigatória:**
                    - Graduação (curso, instituição, ano de início e conclusão)
                    - Mestrado (curso, instituição, ano de início e conclusão)
                    - Doutorado (curso, instituição, ano de início e conclusão)
                - Currículo (menção ao Lattes ou anexo em PDF, é opcional não obrigatório)
        
              Se TODOS esses dados estiverem presentes, o status deve ser **`DADOS_MEMBROS_BANCA`**.
            * **`DADOS_MEMBRO_BANCA_INCORRETO`**: Se houver uma **tentativa de envio de dados de avaliador**, mas **faltarem informações obrigatórias** (ex: não informar doutorado, faltar CPF, ou omitir datas de início/conclusão), classifique como **`DADOS_MEMBRO_BANCA_INCORRETO`**.
        
        4.  **Submissão de Documento:**
            * **`DOCUMENTO_DISSERTACAO`**: Se as regras anteriores não se aplicarem, verifique se há anexos (com extensão .pdf, .docx, .odt) E se o nome do arquivo ou o corpo do e-mail contiver palavras como "dissertação", "tese", "trabalho", "versão final" ou "documento", o status é **`DOCUMENTO_DISSERTACAO`**.

        5.  **Verificação de Assunto (Tentativa de Resgate):**
            * **`DADOS_INICIAIS_INCORRETOS`**: Se NENHUMA das regras acima (1, 2 e 3) for satisfeita, E o assunto do e-mail contiver a palavra 'Defesa' (ignorando maiúsculas/minúsculas), o status é **`DADOS_INICIAIS_INCORRETOS`**.
            * **`VINCULACAO_ORIENTACAO_INCORRETO`**: Se NENHUMA das regras acima (1, 2 e 3) for satisfeita, E o assunto do e-mail contiver a palavra ' Vinculação de orientação' (ignorando maiúsculas/minúsculas), o status é **`VINCULACAO_ORIENTACAO_INCORRETO`**.
        
        6.  **Fallback (Último Recurso):**
            * **`INDEFINIDO`**: Se, e somente se, NENHUMA das regras de 1 a 4 for satisfeita.
        
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
            return "ERRO";
        }
    }
}