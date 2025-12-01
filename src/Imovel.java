public class Imovel {
    private String nome;
    private double preco;
    private double aluguel;
    private Jogador dono;
    private boolean hipotecado = false;
    public Imovel(String nome, double preco, double aluguel) { this.nome = nome; this.preco = preco; this.aluguel = aluguel; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public double getAluguel() { return aluguel; }
    public Jogador getDono() { return dono; }
    public void setDono(Jogador dono) { this.dono = dono; }
    public boolean isHipotecado() { return hipotecado; }
    public void setHipotecado(boolean h) { hipotecado = h; }
}