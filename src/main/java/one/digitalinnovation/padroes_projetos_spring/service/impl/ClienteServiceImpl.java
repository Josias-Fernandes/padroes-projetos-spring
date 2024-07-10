package one.digitalinnovation.padroes_projetos_spring.service.impl;

import one.digitalinnovation.padroes_projetos_spring.model.Cliente;
import one.digitalinnovation.padroes_projetos_spring.model.ClienteRepository;
import one.digitalinnovation.padroes_projetos_spring.model.Endereco;
import one.digitalinnovation.padroes_projetos_spring.model.EnderecoRepository;
import one.digitalinnovation.padroes_projetos_spring.service.ClienteService;
import one.digitalinnovation.padroes_projetos_spring.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ClienteServiceImpl implements ClienteService {

    //Singleton: Injetar os componentes do Spring com @Autowired.
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    //Strategy: Implementar os métodos definidos na interface
    //Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

    @Override
    public Iterable<Cliente> buscarTodos() {

        return clienteRepository.findAll();
    }
    @Override
    public Cliente buscarPorId(Long id) {
       Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);

    }


    @Override
    public void atualizar(Long id, Cliente cliente) {
        //Buscar cliente por id, csao exista:
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()){
            //Verificar se o endereco do cliente já existe(pelo cep)
            //caso não exista, integrar com ViaCep e persistir o retorno
            //Alterar Cliente, vinculando o endereco (novo ou existente)
            salvarClienteComCep(cliente);
        }

    }

    @Override
    public void deletar(Long id) {
        //Deletar cliente por Id
        clienteRepository.deleteById(id);

    }

    private void salvarClienteComCep(Cliente cliente) {
        //verificar se o endereco do cliente já existe (pelocep)
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            //caso não exista, integrar com ViaCep e persistir o retorno
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        //inserir cliente, vinculando o endereco(novo ou existente)
        clienteRepository.save(cliente);
    }
}
