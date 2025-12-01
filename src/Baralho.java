import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class Baralho {
    private final List<Carta> originais = new ArrayList<>();
    private final Deque<Carta> pilha = new ArrayDeque<>();
    public Baralho() {}
    public void carregarPadrao() {
        originais.clear();
        originais.add(new Carta("Ganhe R$ 500", Carta.TipoEfeito.DINHEIRO, 500));
        originais.add(new Carta("Perca R$ 300", Carta.TipoEfeito.DINHEIRO, -300));
        originais.add(new Carta("Avance para o Início", Carta.TipoEfeito.AVANCA_INICIO, 0));
        originais.add(new Carta("Vá para a prisão", Carta.TipoEfeito.VAI_PRISAO, 0));
        originais.add(new Carta("Pague R$ 200 a cada jogador", Carta.TipoEfeito.TAXA_TODOS, 200));
        originais.add(new Carta("Receba R$ 200 por imóvel", Carta.TipoEfeito.GANHA_POR_IMOVEL, 200));
        while (originais.size() < 16) originais.add(new Carta("Carta neutra", Carta.TipoEfeito.NEUTRA, 0));
        embaralhar();
    }
    public void embaralhar() {
        pilha.clear();
        List<Carta> temp = new ArrayList<>(originais);
        Collections.shuffle(temp);
        for (Carta c : temp) pilha.push(c);
    }
    public Carta puxar() {
        if (pilha.isEmpty()) embaralhar();
        return pilha.isEmpty() ? null : pilha.pop();
    }
}