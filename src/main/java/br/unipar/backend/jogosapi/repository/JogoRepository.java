package br.unipar.backend.jogosapi.repository;

import br.unipar.backend.jogosapi.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface JogoRepository extends JpaRepository<Jogo, Integer> {


    @Query("SELECT j FROM Jogo j WHERE " +
            "(:nome IS NULL OR LOWER(j.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:genero IS NULL OR LOWER(j.genero) = LOWER(:genero)) AND " +
            "(:plataforma IS NULL OR LOWER(j.plataforma) = LOWER(:plataforma)) AND " +
            "(:anoLancamento IS NULL OR j.anoLancamento = :anoLancamento) AND " +
            "(:precoMaximo IS NULL OR j.preco <= :precoMaximo)")
    List<Jogo> buscarComFiltros(@Param("nome") String nome,
                                 @Param("genero") String genero,
                                 @Param("plataforma") String plataforma,
                                 @Param("anoLancamento") Integer anoLancamento,
                                 @Param("precoMaximo") Double precoMaximo);

}
