import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Jogo {
    private Tabuleiro tabuleiro;
    private List<Jogador> jogadores;
    private Baralho baralho;
    private double salarioPorVolta;
    private int maxRodadas;
    private Scanner sc = new Scanner(System.in);
    public Jogo(Tabuleiro tabuleiro, List<Jogador> jogadores, Baralho baralho, double salarioPorVolta, int maxRodadas) {
        this.tabuleiro = tabuleiro;
        this.jogadores = jogadores;
        this.baralho = baralho;
        this.salarioPorVolta = salarioPorVolta;
        this.maxRodadas = maxRodadas;
    }
    public void iniciar() {
        for (Jogador j : jogadores) {
            j.setPosicao(tabuleiro.getInicio());
            j.setSaldo(j.getSaldo());
        }
        int rodada = 1;
        Random rnd = new Random();
        while (rodada <= maxRodadas) {
            System.out.printf("\n=== RODADA %d/%d ===\n", rodada, maxRodadas);
            for (Jogador j : jogadores) {
                if (j.isFalido()) continue;
                System.out.printf("\nVez de: %s\n", j.getNome());
                j.imprimirStatus();
                if (j.isPreso()) {
                    System.out.println("1- Tentar sair (lançar dados)  2- Passar a vez");
                    String op = sc.nextLine().trim();
                    if ("1".equals(op)) {
                        int d1 = rnd.nextInt(6)+1, d2 = rnd.nextInt(6)+1;
                        System.out.printf("Dados: %d e %d\n", d1, d2);
                        j.tentarSairPrisao(d1,d2);
                        if (!j.isPreso()) {
                            int soma = d1 + d2;
                            j.avancar(soma, salarioPorVolta);
                            executarCasa(j);
                        }
                    } else System.out.println("Passou a vez preso.");
                    continue;
                }
                System.out.println("1- Lançar dados e mover  2- Ver status  3- Desistir (falir)");
                String op = sc.nextLine().trim();
                if ("1".equals(op)) {
                    int d1 = rnd.nextInt(6)+1, d2 = rnd.nextInt(6)+1;
                    System.out.printf("Dados: %d e %d (soma=%d)\n", d1, d2, d1+d2);
                    j.avancar(d1+d2, salarioPorVolta);
                    executarCasa(j);
                } else if ("2".equals(op)) { j.imprimirStatus(); }
                else if ("3".equals(op)) { j.declararFalencia(); }
            }
            int ativos = 0; Jogador ultimo = null;
            for (Jogador p : jogadores) if (!p.isFalido()) { ativos++; ultimo = p; }
            if (ativos <= 1) {
                if (ultimo != null) System.out.printf("\nVencedor por eliminação: %s (Patrimônio R$ %.2f)\n", ultimo.getNome(), ultimo.getPatrimonio());
                else System.out.println("\nTodos falidos.");
                return;
            }
            rodada++;
        }
        jogadores.sort(Comparator.comparingDouble(Jogador::getPatrimonio).reversed());
        Jogador v = jogadores.get(0);
        System.out.printf("\nFim por rodadas. Vencedor: %s (Patrimônio R$ %.2f)\n", v.getNome(), v.getPatrimonio());
    }
    private void executarCasa(Jogador j) {
        Casa c = j.getPosicao();
        System.out.println("Parou em: " + c.descricao());
        switch (c.getTipo()) {
            case INICIO -> System.out.println("Passou pelo início.");
            case IMOVEL -> {
                Imovel im = c.getImovel();
                if (im.getDono() == null) {
                    System.out.printf("Imóvel sem dono. Preço R$ %.2f. Comprar? 1-Sim\n", im.getPreco());
                    String op = sc.nextLine().trim();
                    if ("1".equals(op) && j.getSaldo() >= im.getPreco()) {
                        j.setSaldo(j.getSaldo() - im.getPreco());
                        j.adicionarPropriedade(im);
                        System.out.printf("Comprou %s.\n", im.getNome());
                    } else System.out.println("Não comprou.");
                } else if (im.getDono() != j && !im.isHipotecado()) {
                    double aluguel = im.getAluguel();
                    double pago = Math.min(j.getSaldo(), aluguel);
                    j.setSaldo(j.getSaldo() - pago);
                    im.getDono().setSaldo(im.getDono().getSaldo() + pago);
                    System.out.printf("Pagou R$ %.2f de aluguel para %s.\n", pago, im.getDono().getNome());
                } else if (im.getDono() != j && im.isHipotecado()) {
                    System.out.println("Imóvel hipotecado. Sem cobrança de aluguel.");
                }
            }
            case IMPOSTO -> {
                double taxa = j.getPatrimonio() * 0.05;
                j.setSaldo(j.getSaldo() - taxa);
                System.out.printf("Imposto cobrado: R$ %.2f\n", taxa);
            }
            case RESTITUICAO -> {
                double valor = salarioPorVolta * 0.10;
                j.setSaldo(j.getSaldo() + valor);
                System.out.printf("Recebeu restituição de R$ %.2f\n", valor);
            }
            case PRISAO -> {
                j.enviarPrisao();
                System.out.println("Enviado para a prisão.");
            }
            case SORTE_REVES -> {
                Carta carta = baralho.puxar();
                if (carta == null) { System.out.println("Sem cartas."); return; }
                System.out.println("Carta: " + carta.getTexto());
                aplicarCarta(j, carta);
            }
        }
        if (j.getSaldo() < 0) { System.out.println("Saldo negativo. Declarado falido."); j.declararFalencia(); }
    }
    private void aplicarCarta(Jogador j, Carta carta) {
        switch (carta.getTipo()) {
            case DINHEIRO -> j.setSaldo(j.getSaldo() + carta.getValor());
            case AVANCA_INICIO -> {
                Casa cur = j.getPosicao();
                for (int i = 0; i < 1000; i++) {
                    if (cur.getTipo() == Casa.Tipo.INICIO) { j.setPosicao(cur); return; }
                    cur = cur.getProximo();
                    if (cur == j.getPosicao()) break;
                }
            }
            case VAI_PRISAO -> j.enviarPrisao();
            case TAXA_TODOS -> {
                double valor = carta.getValor();
                for (Jogador o : jogadores) {
                    if (o != j && !o.isFalido()) {
                        double pago = Math.min(j.getSaldo(), valor);
                        j.setSaldo(j.getSaldo() - pago);
                        o.setSaldo(o.getSaldo() + pago);
                    }
                }
            }
            case GANHA_POR_IMOVEL -> {
                double ganho = 0;
                for (Imovel im : j.getPropriedades()) ganho += carta.getValor();
                j.setSaldo(j.getSaldo() + ganho);
            }
            case NEUTRA -> {}
        }
    }
}