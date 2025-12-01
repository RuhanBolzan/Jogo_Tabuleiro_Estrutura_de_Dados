public class Casa {
    public enum Tipo { INICIO, IMOVEL, IMPOSTO, RESTITUICAO, PRISAO, SORTE_REVES }
    private String nome;
    private Tipo tipo;
    private Imovel imovel;
    private Casa proximo;
    public Casa(String nome, Tipo tipo) { this.nome = nome; this.tipo = tipo; }
    public String getNome() { return nome; }
    public Tipo getTipo() { return tipo; }
    public Imovel getImovel() { return imovel; }
    public void setImovel(Imovel imovel) { this.imovel = imovel; }
    public Casa getProximo() { return proximo; }
    public void setProximo(Casa proximo) { this.proximo = proximo; }
    public String descricao() {
        if (tipo == Tipo.IMOVEL && imovel != null) return "Imóvel: " + imovel.getNome();
        return tipo.name();
    }
}