package br.com.chenrique.learning.todo.meutodo.infra.scheduler;

import br.com.chenrique.learning.todo.meutodo.application.port.out.NotificacaoPort;
import br.com.chenrique.learning.todo.meutodo.domain.Tarefa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * CAMADA: Infrastructure — Implementação de NotificacaoPort
 *
 * Esta classe implementa o contrato de notificação definido no Application.
 * Por ora, registra o lembrete via log (console).
 *
 * Para produção real, aqui você poderia trocar por:
 *   - Envio de e-mail (JavaMailSender)
 *   - Push Notification (Firebase FCM)
 *   - Webhook
 *   - WebSocket
 *
 * E o Use Case NÃO PRECISARIA MUDAR — apenas esta implementação.
 * Isso é o poder da inversão de dependência!
 */
@Component
public class NotificacaoLogAdapter implements NotificacaoPort {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoLogAdapter.class);

    @Override
    public void enviarLembrete(Tarefa tarefa) {
        log.warn("🔔 LEMBRETE | Tarefa: '{}' | Prazo: {} | ID: {}",
                tarefa.getTitulo(),
                tarefa.getPrazo(),
                tarefa.getId());
    }
}