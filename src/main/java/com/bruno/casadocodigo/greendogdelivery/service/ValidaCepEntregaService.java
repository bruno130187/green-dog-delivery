package com.bruno.casadocodigo.greendogdelivery.service;

import com.bruno.casadocodigo.greendogdelivery.entity.Cep;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ValidaCepEntregaService {

    public static String LOCALIDADE_FIXA = "Valparaiso de Goias";

    public static String UF_FIXA = "GO";

    private static String URL_VIACEP1 = "https://viacep.com.br/ws/";

    private static String URL_VIACEP2 = "/json/";

    public Cep processa(String cep) {

        String urlBuscaCEP = URL_VIACEP1 + cep + URL_VIACEP2;

        System.out.println(" buscando CEP em [" + urlBuscaCEP + "]");

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Cep> responseCEP = restTemplate.getForEntity(urlBuscaCEP, Cep.class);

        Cep wsCEP = responseCEP.getBody();

        if (wsCEP.getErro() != null && wsCEP.getErro().equalsIgnoreCase("true")) {
            wsCEP.setErro("CEP inválido!");
        } else if (!wsCEP.getUf().equals(UF_FIXA) && !wsCEP.getLocalidade().equals(LOCALIDADE_FIXA)) {
            wsCEP.setErro("Entrega indisponível para este CEP " + wsCEP.getCep() + "!");
        }

        return wsCEP;
    }

}
