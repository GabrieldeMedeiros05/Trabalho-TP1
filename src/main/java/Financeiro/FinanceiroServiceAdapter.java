package Financeiro;


import recrutamento.interfaces.IFinanceiroService;
import recrutamento.dominio.RegimeContratacao;
import recrutamento.dto.FuncionarioDTO;
/**
 * Adapter que integra o módulo Recrutamento com o módulo Financeiro.
 *-
 * O Recrutamento só conhece IFinanceiroService e FuncionarioDTO.
 * Aqui fazemos a ponte para as classes do módulo Financeiro:
 *  - Financeiro
 *  - Funcionario
 *  - Vinculo
 *  - Cargo
 *  - Departamento
 */
public class FinanceiroServiceAdapter implements IFinanceiroService {

    @Override
    public void cadastrarFuncionario(FuncionarioDTO dto) {
        // 1) Monta o objeto Financeiro com as informações básicas
        Financeiro fin = new Financeiro();
        fin.vinculo = mapearVinculo(dto.getRegime());
        fin.salarioBase = dto.getSalarioBase();

        // Você pode ajustar estas flags conforme regras futuras do módulo Financeiro
        fin.temValeTransporte = false;
        fin.temValeAlimentacao = false;
        fin.temIsencaoIr = false;
        fin.adicionalSalario = 0.0;

        // Calcula o salário total com base nas regras do módulo Financeiro
        fin.calculaSalario();

        // 2) Mapeia o cargo vindo do DTO para o enum Cargo do módulo Financeiro
        Cargo cargoEnum = mapearCargo(dto.getCargo());

        // 3) Define um departamento padrão
        //    Se no futuro o DTO trouxer o departamento, basta adaptar aqui.
        Departamento departamentoEnum = Departamento.RH;

        // 4) Cria o Funcionario do módulo Financeiro
        //    Pessoa é deixada como null por enquanto, pois vem do módulo Seguranca.
        Funcionario funcionario = new Funcionario(
                null,            // Pessoa (poderá ser buscada pelo CPF futuramente)
                fin,
                cargoEnum,
                departamentoEnum,
                true             // ativo
        );

        // 5) (Opcional) Adiciona o funcionário em uma folha em memória
        //    Aqui apenas demonstramos o uso da classe FolhaPagamento.
        FolhaPagamento folha = new FolhaPagamento();
        folha.listaFuncionario.add(funcionario);

        // 6) Log para demonstrar a integração
        System.out.println("=== INTEGRAÇÃO RECRUTAMENTO -> FINANCEIRO ===");
        System.out.println("Novo funcionário cadastrado pelo FinanceiroServiceAdapter:");
        System.out.println("  CPF           : " + dto.getCpf());
        System.out.println("  Cargo         : " + cargoEnum);
        System.out.println("  Departamento  : " + departamentoEnum);
        System.out.println("  Vínculo       : " + fin.vinculo);
        System.out.println("  Salário base  : " + fin.salarioBase);
        System.out.println("  Salário final : " + fin.salarioCalculado);
        System.out.println("=============================================");
    }

    /**
     * Converte o RegimeContratacao (módulo Recrutamento)
     * para o enum Vinculo (módulo Financeiro).
     */
    private Vinculo mapearVinculo(RegimeContratacao regime) {
        if (regime == null) {
            return Vinculo.CLT;
        }
        return switch (regime) {
            case CLT -> Vinculo.CLT;
            case PJ -> Vinculo.PJ;
            case ESTAGIO -> Vinculo.ESTAGIO;
            default -> Vinculo.CLT;
        };
    }

    /**
     * Converte o texto do cargo vindo do DTO para o enum Cargo.
     * Se não bater com nenhum valor, assume ANALISTA como padrão.
     */
    private Cargo mapearCargo(String cargoTexto) {
        if (cargoTexto == null || cargoTexto.isBlank()) {
            return Cargo.ANALISTA;
        }
        String normalizado = cargoTexto.trim().toUpperCase();

        try {
            return Cargo.valueOf(normalizado);
        } catch (IllegalArgumentException e) {
            // Caso o texto não corresponda exatamente ao enum, escolhe um padrão.
            return Cargo.ANALISTA;
        }
    }
}
