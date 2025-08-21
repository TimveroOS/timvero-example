package com.timvero.example.portal.client;

import com.timvero.example.admin.client.entity.Client;
import com.timvero.example.admin.client.entity.ClientRepository;
import com.timvero.example.portal.client.form.ClientRequiestMapper;
import com.timvero.example.portal.client.form.CreateClientRequest;
import com.timvero.ground.filter.base.NotFoundException;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    @Autowired
    private ClientRequiestMapper mapper;

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public UUID createClient(@Valid CreateClientRequest form) {
        Client client = mapper.createEntity(form);
        Client savedClient = clientRepository.save(client);
        return savedClient.getId();
    }

    @Transactional(readOnly = true)
    public Client getById(UUID id) {
        return clientRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Client not found with ID: " + id));
    }
}
