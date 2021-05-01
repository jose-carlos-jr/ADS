import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class FilaDeRequisicoes {

	private BlockingQueue<Requisicao> fila;
	private SummaryStatistics tempoDeRespostaStats;
	private SummaryStatistics tamanhoDaFilaStats;
	private SummaryStatistics utilizacaoStats;
	private AtomicLong adicionadas;
	private AtomicLong retiradas;

	public FilaDeRequisicoes() {
		this.fila = new LinkedBlockingQueue<Requisicao>();
		this.adicionadas = new AtomicLong();
		this.retiradas = new AtomicLong();
		this.tempoDeRespostaStats = new SummaryStatistics();
		this.tamanhoDaFilaStats = new SummaryStatistics();
		this.utilizacaoStats = new SummaryStatistics();
	}

	public void adicionaRequisicao(Requisicao requisicao) {
		requisicao.setTempoDeChegada(System.currentTimeMillis());
		adicionadas.incrementAndGet();
		fila.add(requisicao);
	}

	public Requisicao retiraRequisicao() throws InterruptedException {
		tamanhoDaFilaStats.addValue(fila.size());
		retiradas.incrementAndGet();
		Requisicao requisicao = fila.take();
		double tempoDeEspera = (System.currentTimeMillis() - requisicao
				.getTempoDeChegada()) / 1000;
		double tempoDeResposta = tempoDeEspera + requisicao.getTempoDeServico();
		tempoDeRespostaStats.addValue(tempoDeResposta);
		return requisicao;
	}

	public AtomicLong getAdicionadas() {
		return adicionadas;
	}

	public AtomicLong getRetiradas() {
		return retiradas;
	}

	public SummaryStatistics getTempoDeRespostaStats() {
		return tempoDeRespostaStats;
	}

	public SummaryStatistics getTamanhoDaFilaStats() {
		return tamanhoDaFilaStats;
	}

	public SummaryStatistics getUtilizacaoStats() {
		return utilizacaoStats;
	}
}
