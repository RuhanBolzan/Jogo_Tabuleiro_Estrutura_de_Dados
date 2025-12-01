public class Tabuleiro {
    private Casa inicio;
    private int tamanho = 0;
    public void adicionarCasa(Casa casa) {
        if (inicio == null) {
            inicio = casa;
            inicio.setProximo(inicio);
        } else {
            Casa atual = inicio;
            while (atual.getProximo() != inicio) atual = atual.getProximo();
            atual.setProximo(casa);
            casa.setProximo(inicio);
        }
        tamanho++;
    }
    public Casa getInicio() { return inicio; }
    public int getTamanho() { return tamanho; }
    public void limpar() { inicio = null; tamanho = 0; }
}