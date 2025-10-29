/**
package recrutamento.ui;

import javax.swing.SwingUtilities;

public class MainRecrutamento {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaMenuRecrutamento().setVisible(true));
    }
}
 */

package recrutamento.ui;

import recrutamento.dominio.*;
import recrutamento.servico.RecrutamentoService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe de teste independente para o módulo Recrutamento.
 * Permite validar o funcionamento do domínio, regras de negócio e persistência CSV.
 *
 * Este código simula o fluxo completo de um processo seletivo:
 * 1. Gestor cria uma vaga
 * 2. Gestor atribui um recrutador
 * 3. Recrutador cadastra candidatos
 * 4. Recrutador agenda entrevistas
 * 5. Recrutador aprova candidato
 * 6. Recrutador solicita contratação
 * 7. Gestor autoriza contratação
 * 8. Recrutador efetiva a contratação
 */
public class MainRecrutamento {

    public static void main(String[] args) {
        // Obter instância do serviço já configurado com persistência CSV
        RecrutamentoService svc = RecrutamentoModuleConfig.service();

        System.out.println("==== TESTE DO MÓDULO RECRUTAMENTO ====\n");

        // -------------------------------
        // 1. Gestor cria uma nova vaga
        // -------------------------------
        svc.logarComoGestor("11111111111"); // CPF do gestor
        Vaga vaga = svc.criarVaga(
                "Desenvolvedor Java",
                "Tecnologia da Informação",
                7500.00,
                "Experiência com POO, MVC e Git",
                RegimeContratacao.CLT
        );
        System.out.println("Vaga criada com ID: " + vaga.getId());

        // -------------------------------
        // 2. Gestor atribui um recrutador
        // -------------------------------
        svc.atribuirRecrutador(vaga.getId(), "22222222222");
        System.out.println("Recrutador atribuído à vaga.\n");

        // -------------------------------
        // 3. Recrutador registra candidaturas
        // -------------------------------
        svc.logarComoRecrutador("22222222222");
        var cand1 = svc.registrarCandidatura(vaga.getId(), "33333333333");
        var cand2 = svc.registrarCandidatura(vaga.getId(), "44444444444");

        System.out.println("Candidatos registrados: " + cand1.getCandidatoCpf() + ", " + cand2.getCandidatoCpf());

        // -------------------------------
        // 4. Recrutador agenda entrevistas
        // -------------------------------
        svc.agendarEntrevista(cand1.getId(), LocalDateTime.now().plusDays(1), "Entrevistador 1");
        svc.agendarEntrevista(cand2.getId(), LocalDateTime.now().plusDays(2), "Entrevistador 2");

        System.out.println("Entrevistas agendadas.\n");

        // -------------------------------
        // 5. Aprovar um candidato
        // -------------------------------
        svc.atualizarStatusCandidatura(cand1.getId(), StatusCandidatura.APROVADO);
        System.out.println("Candidato " + cand1.getCandidatoCpf() + " aprovado.");

        // -------------------------------
        // 6. Solicitar contratação
        // -------------------------------
        Contratacao cont = svc.solicitarContratacao(cand1.getId(), RegimeContratacao.CLT);
        System.out.println("Contratação solicitada com ID: " + cont.getId());

        // -------------------------------
        // 7. Gestor autoriza contratação
        // -------------------------------
        svc.logarComoGestor("11111111111");
        svc.autorizarContratacao(cont.getId());
        System.out.println("Contratação autorizada pelo gestor.");

        // -------------------------------
        // 8. Recrutador efetiva contratação
        // -------------------------------
        svc.logarComoRecrutador("22222222222");
        svc.efetivarContratacao(cont.getId());
        System.out.println("Contratação efetivada com sucesso!\n");

        // -------------------------------
        // 9. Exibir dados finais
        // -------------------------------
        System.out.println("Resumo do pipeline da vaga:");
        System.out.println(svc.pipelineResumo(vaga.getId()));

        System.out.println("\nListagem final de vagas:");
        List<Vaga> vagas = svc.listarVagas();
        vagas.forEach(v ->
                System.out.printf("- %s | Status: %s | Recrutador: %s%n",
                        v.getCargo(), v.getStatus(), v.getRecrutadorResponsavelCpf())
        );

        System.out.println("\n==== FIM DO TESTE ====");
    }
}

