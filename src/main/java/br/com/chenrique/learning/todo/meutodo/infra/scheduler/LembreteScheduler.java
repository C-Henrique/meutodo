package br.com.chenrique.learning.todo.meutodo.infra.scheduler;

import br.com.chenrique.learning.todo.meutodo.application.port.out.NotificacaoPort;
import br.com.chenrique.learning.todo.meutodo.domain.Tarefa;
import br.com.chenrique.learning.todo.meutodo.domain.TarefaRepositoryPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CAMADA: Infrastructure — Scheduler de Lembretes
 *
 * Roda automaticamente a cada 60 segundos verificando tarefas com lembrete
 * programado para os próximos minutos.
 *
 * Perceba que este Scheduler usa apenas as PORTAS (interfaces),
 * não as implementações concretas. O Spring injeta as implementações.
 *
 * Habilitar na classe principal: @EnableScheduling
 */
@Component
public class LembreteScheduler {

    private static final Logger log = LoggerFactory.getLogger(LembreteScheduler.class);

    private final TarefaRepositoryPort repositoryPort;
    private final NotificacaoPort notificacaoPort;

    public LembreteScheduler(TarefaRepositoryPort repositoryPort, NotificacaoPort notificacaoPort) {
        this.repositoryPort = repositoryPort;
        this.notificacaoPort = notificacaoPort;
    }

    /**
     * Executa a cada 60 segundos.
     * Busca tarefas cujo lembrete está dentro da janela [agora, agora + 1 minuto].
     */
    @Scheduled(fixedDelay = 60_000)
    public void verificarLembretes() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime proximoMinuto = agora.plusMinutes(1);

        List<Tarefa> tarefas = repositoryPort.buscarComLembreteEntre(agora, proximoMinuto);

        if (tarefas.isEmpty()) {
            log.debug("Nenhum lembrete pendente para os próximos 60 segundos.");
            return;
        }

        log.info("⏰ {} lembrete(s) encontrado(s) para disparar.", tarefas.size());
        tarefas.forEach(notificacaoPort::enviarLembrete);
    }
}