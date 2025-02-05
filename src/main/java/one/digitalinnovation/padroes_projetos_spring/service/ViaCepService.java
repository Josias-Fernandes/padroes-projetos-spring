package one.digitalinnovation.padroes_projetos_spring.service;

import one.digitalinnovation.padroes_projetos_spring.model.Endereco;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "viacep", url = "https://viacep.com.br/ws/")
public interface ViaCepService {
    @RequestMapping(method = RequestMethod.GET, value = "/{cep}/jason/")
    Endereco consultarCep(@PathVariable("cep") String cep);

}
