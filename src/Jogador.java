import java.util.ArrayList;
import java.util.List;

public class Jogador {
    private String nome;
    private double saldo;
    private Casa posicao;
    private List<Imovel> propriedades = new ArrayList<>();
    private boolean preso = false;
    private int tentativasPrisao = 0;
    private boolean liberadoNaProxima = false;
    private boolean falido = false;
    public Jogador(String nome, double saldo) { this.nome = nome; this.saldo = saldo; }
    public String getNome() { return nome; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double s) { saldo = s; }
    public Casa getPosicao() { return posicao; }
    public void setPosicao(Casa p) { posicao = p; }
    public List<Imovel> getPropriedades() { return propriedades; }
    public boolean isPreso() { return preso && !liberadoNaProxima; }
    public void enviarPrisao() { preso = true; tentativasPrisao = 0; liberadoNaProxima = false; }
    public void tentarSairPrisao(int d1, int d2) {
        if (d1 == d2) { preso = false; tentativasPrisao = 0; liberadoNaProxima = false; }
        else { tentativasPrisao++; if (tentativasPrisao >= 3) { preso = false; liberadoNaProxima = true; } }
    }
    public void sairPrisaoDefinitivo() { preso = false; tentativasPrisao = 0; liberadoNaProxima = false; }
    public int getTentativasPrisao() { return tentativasPrisao; }
    public boolean isLiberadoNaProxima() { return liberadoNaProxima; }
    public void setLiberadoNaProxima(boolean v) { liberadoNaProxima = v; }
    public boolean isFalido() { return falido; }
    public void declararFalencia() {
        falido = true;
        for (Imovel im : new ArrayList<>(propriedades)) {
            im.setDono(null);
        }
        propriedades.clear();
    }
    public double getPatrimonio() {
        double soma = saldo;
        for (Imovel im : propriedades) soma += im.getPreco();
        return soma;
    }
    public void adicionarPropriedade(Imovel im) { propriedades.add(im); im.setDono(this); }
    public void removerPropriedade(Imovel im) { propriedades.remove(im); im.setDono(null); }
    public void avancar(int passos, double salarioPorVolta) {
        for (int i = 0; i < passos; i++) {
            posicao = posicao.getProximo();
            if (posicao.getTipo() == Casa.Tipo.INICIO) saldo += salarioPorVolta;
        }
    }
    public void imprimirStatus() {
        System.out.printf("%s | Saldo R$ %.2f | Patrimônio R$ %.2f | Posição: %s\n", nome, saldo, getPatrimonio(), posicao != null ? posicao.descricao() : "N/A");
        if (!propriedades.isEmpty()) {
            for (Imovel im : propriedades) {
                System.out.printf(" - %s (R$ %.2f)%s\n", im.getNome(), im.getPreco(), im.isHipotecado() ? " (Hipotecado)" : "");
            }
        }
        if (preso) System.out.printf("Status: PRESO (tentativas: %d)\n", tentativasPrisao);
        if (liberadoNaProxima) System.out.println("Liberado para jogar no próximo turno.");
    }
}