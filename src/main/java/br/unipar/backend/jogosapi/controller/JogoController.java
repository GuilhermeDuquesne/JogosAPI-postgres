package br.unipar.backend.jogosapi.controller;

import br.unipar.backend.jogosapi.model.Jogo;
import br.unipar.backend.jogosapi.repository.JogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/jogo")
public class JogoController {

    @Autowired
    private JogoRepository jogoRepository;

    // GET /jogo
    @GetMapping
    public ResponseEntity<List<Jogo>> listarJogos(@RequestParam(required = false) String nome,
                                                  @RequestParam(required = false) String genero,
                                                  @RequestParam(required = false) String plataforma,
                                                  @RequestParam(required = false) Integer anoLancamento,
                                                  @RequestParam(required = false) Double precoMaximo) {


        List<Jogo> resultado = jogoRepository.buscarComFiltros(nome, genero, plataforma, anoLancamento, precoMaximo);
        return ResponseEntity.ok(resultado);
    }

    // GET /jogo/1
    @GetMapping("/{id}")
    public ResponseEntity<Jogo> buscarJogo(@PathVariable Integer id) {
        Optional<Jogo> jogo = jogoRepository.findById(id);

        if (jogo.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(jogo.get());
        }
    }

    // POST /jogo
    @PostMapping
    public ResponseEntity<Jogo> cadastrarJogo(@RequestBody(required = false) Jogo jogo) {
        if (jogoInvalido(jogo)) {
            return ResponseEntity.badRequest().build();
        } else {
            jogo.setId(null);
            Jogo jogoSalvo = jogoRepository.save(jogo);
            return ResponseEntity.status(HttpStatus.CREATED).body(jogoSalvo); //201
        }
    }

    // PUT /jogo/1
    @PutMapping("/{id}")
    public ResponseEntity<Jogo> atualizarJogo(@PathVariable Integer id,
                                              @RequestBody(required = false) Jogo jogoAtualizado) {
        if (jogoInvalido(jogoAtualizado)) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Jogo> jogoExistente = jogoRepository.findById(id);

        if (jogoExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Jogo jogo = jogoExistente.get();
            jogo.setNome(jogoAtualizado.getNome());
            jogo.setGenero(jogoAtualizado.getGenero());
            jogo.setPlataforma(jogoAtualizado.getPlataforma());
            jogo.setAnoLancamento(jogoAtualizado.getAnoLancamento());
            jogo.setPreco(jogoAtualizado.getPreco());
            Jogo jogoSalvo = jogoRepository.save(jogo);
            return ResponseEntity.ok(jogoSalvo);
        }
    }

    // DELETE /jogo/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirJogo(@PathVariable Integer id) {
        if (!jogoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        } else {
            jogoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }

    // ---------- método auxiliar (não é rota) ----------

    private boolean jogoInvalido(Jogo jogo) {
        return jogo == null || jogo.getNome() == null || jogo.getNome().isBlank();
    }

}
