public class Carta {
    private String texto;
    private TipoEfeito tipo;
    private double valor;
    public enum TipoEfeito { DINHEIRO, AVANCA_INICIO, VAI_PRISAO, TAXA_TODOS, GANHA_POR_IMOVEL, NEUTRA }
    public Carta(String texto, TipoEfeito tipo, double valor) { this.texto = texto; this.tipo = tipo; this.valor = valor; }
    public String getTexto() { return texto; }
    public TipoEfeito getTipo() { return tipo; }
    public double getValor() { return valor; }
}