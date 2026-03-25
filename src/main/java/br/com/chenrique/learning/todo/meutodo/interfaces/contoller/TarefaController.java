package br.com.chenrique.learning.todo.meutodo.interfaces.contoller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.chenrique.learning.todo.meutodo.application.usecase.ConsultarTarefaService;
import br.com.chenrique.learning.todo.meutodo.application.usecase.GerenciarTarefaService;
import br.com.chenrique.learning.todo.meutodo.interfaces.dto.TarefaRequest;
import br.com.chenrique.learning.todo.meutodo.interfaces.dto.TarefaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Tarefas", description = "Gerenciamento de tarefas")
@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private final GerenciarTarefaService gerenciarTarefaService;
    private final ConsultarTarefaService consultarTarefaService;

    public TarefaController(GerenciarTarefaService gerenciarTarefaService,
            ConsultarTarefaService consultarTarefaService) {
        this.gerenciarTarefaService = gerenciarTarefaService;
        this.consultarTarefaService = consultarTarefaService;
    }

    @Operation(summary = "Lista todas as tarefas", description = "Retorna todas as tarefas cadastradas, incluindo concluídas")
    @GetMapping
    public ResponseEntity<List<TarefaResponse>> listarTodas() {
        List<TarefaResponse> tarefas = consultarTarefaService.listarTodas()
                .stream()
                .map(TarefaResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/destacadas")
    public ResponseEntity<List<TarefaResponse>> listarDestacadas() {
        List<TarefaResponse> tarefas = consultarTarefaService.listarDestacadas()
                .stream()
                .map(TarefaResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/atrasadas")
    public ResponseEntity<List<TarefaResponse>> listarAtrasadas() {
        List<TarefaResponse> tarefas = consultarTarefaService.listarAtrasadas()
                .stream()
                .map(TarefaResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponse> buscarPorId(@PathVariable Long id) {
        TarefaResponse tarefa = TarefaResponse.fromDomain(consultarTarefaService.buscarPorId(id));
        return ResponseEntity.ok(tarefa);
    }

    @Operation(summary = "Cria uma nova tarefa", responses = {
            @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<TarefaResponse> criarTarefa(@Valid @RequestBody TarefaRequest request) {
        var tarefa = gerenciarTarefaService.criarTarefa(request.titulo(), request.descricao(), request.prazo(),
                request.lembrete());

        return ResponseEntity.status(HttpStatus.CREATED).body(TarefaResponse.fromDomain(tarefa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponse> atualizarTarefa(@PathVariable Long id,
            @Valid @RequestBody TarefaRequest request) {
        var tarefa = gerenciarTarefaService.atualizarTarefa(id, request.titulo(), request.descricao(), request.prazo(),
                request.lembrete());

        return ResponseEntity.ok(TarefaResponse.fromDomain(tarefa));
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<TarefaResponse> concluir(@PathVariable Long id) {
        TarefaResponse tarefa = TarefaResponse.fromDomain(gerenciarTarefaService.concluirTarefa(id));
        return ResponseEntity.ok(tarefa);
    }

    @PatchMapping("/{id}/destacar")
    public ResponseEntity<TarefaResponse> destacar(@PathVariable Long id) {
        TarefaResponse tarefa = TarefaResponse.fromDomain(gerenciarTarefaService.destacarTarefa(id));
        return ResponseEntity.ok(tarefa);
    }

    @PatchMapping("/{id}/remover-destaque")
    public ResponseEntity<TarefaResponse> removerDestaque(@PathVariable Long id) {
        TarefaResponse tarefa = TarefaResponse.fromDomain(gerenciarTarefaService.removerDestaqueTarefa(id));
        return ResponseEntity.ok(tarefa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTarefa(@PathVariable Long id) {
        gerenciarTarefaService.excluirTarefa(id);
        return ResponseEntity.noContent().build();
    }
}
