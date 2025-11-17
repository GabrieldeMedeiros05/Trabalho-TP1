package AdministracaoGestao.ui;

import AdministracaoGestao.modelos.UsuariosTableModel;
import Seguranca.dominio.Usuario;
import Seguranca.servico.UsuarioService;
import utils.AppConfig;
import utils.Constantes;

import javax.swing.*;
import java.awt.*;

import java.util.List;

public class ListaUsuario extends JFrame {
    // Injeção do serviço de segurança
    private final UsuarioService usuarioService = AppConfig.usuarioService();

    private JTable tabela;


    public ListaUsuario() {
        initComponets();
        ListatodosUsuarios();
    }

    public void initComponets(){
        JPanel botoes = new JPanel();

        setTitle("Lista de Usuários Cadastrados");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        List<Usuario> usuarios = ListatodosUsuarios();
        UsuariosTableModel model = new UsuariosTableModel(usuarios);

        tabela = new JTable(model);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");

        botoes.add(btnFechar);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        footer.add(botoes, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);

        // Listeners
        btnFechar.addActionListener(e -> dispose());

        pack();
        setSize(Constantes.ALTURA_PADRAO_PAGINA, Constantes.LARGURA_PADRAO_PAGINA);
        setLocationRelativeTo(null);
    }

    private List<Usuario> ListatodosUsuarios() {

       return usuarioService.listarTodos();

    }

}
