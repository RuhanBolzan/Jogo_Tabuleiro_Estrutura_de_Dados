import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private Scanner sc = new Scanner(System.in);
    private List<Jogador> jogadores = new ArrayList<>();
    private List<Imovel> catalogo = new ArrayList<>();
    private Tabuleiro tabuleiro = new Tabuleiro();
    private Baralho baralho = new Baralho();
    private double saldoInicial = 30000;
    private double salarioPorVolta = 2000;
    private int maxRodadas = 20;
    public void iniciar() {
        carregarSugestao();
        baralho.carregarPadrao();
        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1 Gerenciar Jogadores");
            System.out.println("2 Gerenciar Imóveis");
            System.out.println("3 Configurar Partida");
            System.out.println("4 Iniciar Jogo");
            System.out.println("0 Sair");
            String op = sc.nextLine().trim();
            if ("1".equals(op)) menuJogadores();
            else if ("2".equals(op)) menuImoveis();
            else if ("3".equals(op)) menuConfig();
            else if ("4".equals(op)) {
                if (catalogo.size() < 10) { System.out.println("Erro: mínimo 10 imóveis."); continue; }
                if (jogadores.size() < 2) { System.out.println("Erro: mínimo 2 jogadores."); continue; }
                montarTabuleiro();
                Jogo jogo = new Jogo(tabuleiro, jogadores, baralho, salarioPorVolta, maxRodadas);
                jogo.iniciar();
            }
            else if ("0".equals(op)) break;
        }
    }
    private void menuJogadores() {
        while (true) {
            System.out.println("\n1 Adicionar 2 Listar 3 Remover 4 Voltar");
            String op = sc.nextLine().trim();
            if ("1".equals(op)) {
                if (jogadores.size() >= 6) { System.out.println("Máx 6 jogadores."); continue; }
                System.out.print("Nome: ");
                String nome = sc.nextLine().trim();
                Jogador j = new Jogador(nome, saldoInicial);
                jogadores.add(j);
                System.out.println("Adicionado.");
            } else if ("2".equals(op)) {
                if (jogadores.isEmpty()) System.out.println("Nenhum jogador.");
                else for (Jogador j : jogadores) System.out.printf("%s - Saldo R$ %.2f\n", j.getNome(), j.getSaldo());
            } else if ("3".equals(op)) {
                System.out.print("Nome para remover: ");
                String nome = sc.nextLine().trim();
                jogadores.removeIf(j -> j.getNome().equalsIgnoreCase(nome));
            } else if ("4".equals(op)) return;
        }
    }
    private void menuImoveis() {
        while (true) {
            System.out.println("\n1 Adicionar 2 Listar 3 Remover 4 Voltar");
            String op = sc.nextLine().trim();
            if ("1".equals(op)) {
                if (catalogo.size() >= 40) { System.out.println("Máx 40."); continue; }
                System.out.print("Nome imóvel: "); String nome = sc.nextLine().trim();
                System.out.print("Preço: "); double preco = Double.parseDouble(sc.nextLine().trim().replace(",","."));
                System.out.print("Aluguel: "); double aluguel = Double.parseDouble(sc.nextLine().trim().replace(",","."));
                catalogo.add(new Imovel(nome, preco, aluguel));
                System.out.println("Cadastrado.");
            } else if ("2".equals(op)) {
                if (catalogo.isEmpty()) System.out.println("Nenhum imóvel.");
                else for (Imovel im : catalogo) System.out.printf("%s - R$ %.2f - Aluguel R$ %.2f\n", im.getNome(), im.getPreco(), im.getAluguel());
            } else if ("3".equals(op)) {
                System.out.print("Nome para remover: ");
                String nome = sc.nextLine().trim();
                catalogo.removeIf(im -> im.getNome().equalsIgnoreCase(nome));
            } else if ("4".equals(op)) return;
        }
    }
    private void menuConfig() {
        while (true) {
            System.out.println("\n1 Saldo inicial 2 Salário por volta 3 Máx rodadas 4 Voltar");
            String op = sc.nextLine().trim();
            if ("1".equals(op)) { System.out.print("Novo saldo: "); saldoInicial = Double.parseDouble(sc.nextLine().trim().replace(",",".")); }
            else if ("2".equals(op)) { System.out.print("Novo salário: "); salarioPorVolta = Double.parseDouble(sc.nextLine().trim().replace(",",".")); }
            else if ("3".equals(op)) { System.out.print("Novo max rodadas: "); maxRodadas = Integer.parseInt(sc.nextLine().trim()); }
            else if ("4".equals(op)) return;
        }
    }
    private void montarTabuleiro() {
        tabuleiro.limpar();
        Casa inicio = new Casa("INICIO", Casa.Tipo.INICIO);
        tabuleiro.adicionarCasa(inicio);
        for (int i = 0; i < catalogo.size(); i++) {
            Imovel im = catalogo.get(i);
            Casa c = new Casa(im.getNome(), Casa.Tipo.IMOVEL);
            c.setImovel(im);
            tabuleiro.adicionarCasa(c);
            if ((i+1) % 5 == 0) tabuleiro.adicionarCasa(new Casa("IMPOSTO", Casa.Tipo.IMPOSTO));
            if ((i+2) % 7 == 0) tabuleiro.adicionarCasa(new Casa("RESTITUICAO", Casa.Tipo.RESTITUICAO));
            if ((i+3) % 9 == 0) tabuleiro.adicionarCasa(new Casa("PRISAO", Casa.Tipo.PRISAO));
            if ((i+4) % 6 == 0) tabuleiro.adicionarCasa(new Casa("SORTE/REVES", Casa.Tipo.SORTE_REVES));
        }
        while (tabuleiro.getTamanho() < 20) {
            Imovel extra = new Imovel("Extra"+tabuleiro.getTamanho(), 100000, 1000);
            Casa c = new Casa(extra.getNome(), Casa.Tipo.IMOVEL);
            c.setImovel(extra);
            tabuleiro.adicionarCasa(c);
        }
        for (Jogador j : jogadores) j.setPosicao(tabuleiro.getInicio());
    }
    private void carregarSugestao() {
        catalogo.add(new Imovel("Casa do Bosque",200000,1100));
        catalogo.add(new Imovel("Apartamento Central",350000,1800));
        catalogo.add(new Imovel("Vila das Flores",400000,2200));
        catalogo.add(new Imovel("Pousada da Praia",500000,2700));
        catalogo.add(new Imovel("Mansão da Colina",600000,3300));
        catalogo.add(new Imovel("Residência do Lago",450000,2500));
        catalogo.add(new Imovel("Cobertura Diamante",700000,3700));
        catalogo.add(new Imovel("Edifício Horizonte",550000,2900));
        catalogo.add(new Imovel("Chácara do Sol",300000,1600));
        catalogo.add(new Imovel("Fazenda Boa Vista",250000,1300));
    }
}