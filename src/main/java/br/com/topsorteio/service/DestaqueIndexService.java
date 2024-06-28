package br.com.topsorteio.service;

import br.com.topsorteio.repositories.IDestaqueIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DestaqueIndexService {

    @Autowired
    private IDestaqueIndexRepository destaqueIndexRepository;

    public ResponseEntity obterDestaqueIndex(){
        try{

        }
    }
}
