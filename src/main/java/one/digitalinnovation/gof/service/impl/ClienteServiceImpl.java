package one.digitalinnovation.gof.service.impl;

import one.digitalinnovation.gof.entity.Cliente;
import one.digitalinnovation.gof.entity.Endereco;
import one.digitalinnovation.gof.repository.ClienteRepository;
import one.digitalinnovation.gof.repository.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementação da <b>Strategy</b> {@link ClienteService}, a qual pode
 * injetada pelo Spirng (via {@link Autowired}). Com isso, essa classe é um
 * {@link Service}, ela séra tratada com um Singleton.
 *
 * @author Alex Vieira
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    // Singleton: Injetar os componentes do Spring com @Autowired.
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    // Strategy: Implementa os métodos definidos na interface.
    // Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

    @Override
    public Iterable<Cliente> buscarTodos(){
        // Buscar todos os Clientes.
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id){
        // Buscar Cliente por ID.
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente){
        // Verificar se o endereço do Cliente já existir (pelo cep).
        salvarClienteComCep(cliente);
    }

    @Override
    public void atualizar(Long id, Cliente cliente) {
        // Buscar Cliente por ID, caso exista.
        Optional<Cliente> clienteDb = clienteRepository.findById(id);

        if(clienteDb.isPresent()){
            salvarClienteComCep(cliente);
        }

    }

    @Override
    public void deletar(Long id) {
        //FIXME Deletar Cliente por ID.

        Cliente cliente = clienteRepository.findById(id).get();
        clienteRepository.deleteById(cliente.getId());
    }

    private void salvarClienteComCep(Cliente cliente){
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            //Caso não exista, integrar com ViaCep é persistir o retorno.
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            return enderecoRepository.save(novoEndereco);
        });

        cliente.setEndereco(endereco);

        // Inserir Cliente, vinculando o Endereço (novo ou existente).

        clienteRepository.save(cliente);
    }
}
