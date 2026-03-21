package br.com.chenrique.learning.todo.meutodo.application.port.out;

import br.com.chenrique.learning.todo.meutodo.domain.Tarefa;

/**
 * CAMADA: Application — Porta de Saída (Port OUT)
 *
 * Contrato para envio de notificações/lembretes.
 * A implementação concreta fica na infraestrutura (ex: log, e-mail, push, etc.)
 *
 * Isso permite que o Use Case dispare lembretes sem conhecer
 * COMO eles são enviados — apenas QUE devem ser enviados.
 */
public interface NotificacaoPort {

    void enviarLembrete(Tarefa tarefa);
}