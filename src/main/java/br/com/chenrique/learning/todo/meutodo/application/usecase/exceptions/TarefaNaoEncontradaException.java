package br.com.chenrique.learning.todo.meutodo.application.usecase.exceptions;

/**
 * CAMADA: Application
 *
 * Exceção de domínio — representa a situação em que uma tarefa
 * não foi encontrada. Fica na camada de aplicação pois é usada
 * pelos Use Cases para sinalizar falha de busca.
 *
 * A camada de interfaces (Controller) pode capturá-la e transformar
 * em um HTTP 404, mas a exceção em si não conhece HTTP.
 */
public class TarefaNaoEncontradaException extends RuntimeException {

    public TarefaNaoEncontradaException(Long id) {
        super("Tarefa não encontrada com id: " + id);
    }
}