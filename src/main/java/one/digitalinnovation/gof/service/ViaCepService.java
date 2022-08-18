package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.entity.Endereco;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente HTTP, criado via <b>OpenFeign</b>, para o consumo da Api do <b>ViaCep</b>.
 *
 * @see <a href="https://spring.io/projects/spring-cloud-openfeign">Spring Cloud OpenFeign</a>
 * @see  <a href="http://viacep.com.br">ViaCep</a>
 *
 * @author Alex Vieira
 */
@FeignClient(name = "viacep", url = "http://viacep.com.br/ws")
public interface ViaCepService {

    @GetMapping("/{cep}/json/")
    Endereco consultarCep(@PathVariable("cep") String cep);

}
