package br.com.topsorteio.dtos;

import br.com.topsorteio.entities.PremioModel;

public record IsSorteioSurpresaResquestDTO(Boolean sorteio_surpresa, String email_administrador, String codigo_sku) {
}
