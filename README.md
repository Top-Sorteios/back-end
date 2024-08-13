**USUARIOS**

| METODO | LINK | PERMISSAO |
|-----|------|-----------|
| POST | https://modulo-sorteios.azurewebsites.net/usuarios/importar-usuario | ADMIN |
| PUT | https://modulo-sorteios.azurewebsites.net/usuarios/primeiro-acesso | X |
| POST| https://modulo-sorteios.azurewebsites.net/usuarios/login | X |
| POST| https://modulo-sorteios.azurewebsites.net/usuarios/esqueci-senha | X |
| GET | https://modulo-sorteios.azurewebsites.net/usuarios/obter | ADMIN |
| GET | https://modulo-sorteios.azurewebsites.net/usuarios/obter/{email} | USER |
| PUT | https://modulo-sorteios.azurewebsites.net/usuarios/editar/senha/{email} | USER |
| GET | https://modulo-sorteios.azurewebsites.net/usuarios/helloworld | X |
| POST | https://modulo-sorteios.azurewebsites.net/usuarios/sorteio/participar | USER |
| PUT | https://modulo-sorteios.azurewebsites.net/usuarios/editar/tipo/{email} | ADMIN |


<b>Formato Requisições</b>

| POST | https://modulo-sorteios.azurewebsites.net/usuarios/importar-usuario | ADMIN |

**Tipo de dado:**
*form-data*

**Obs:** *Email do "email_autenticado" tem que ser a do ADMINISTRADOR que está cadastrando os usuarios*

```console
	file: {arquivo.xlsx}
	email_autenticado: String
```

https://modulo-sorteios.azurewebsites.net/usuarios/primeiro-acesso

```console
{
	"email": string, 
	"cpf": string,  
	"datanascimento": string, //exemplo:"AAAA-MM-DD"
 	"senha": string,		
}
```

https://modulo-sorteios.azurewebsites.net/usuarios/login

```console
{
	"email": string, 
	"senha": string,  
}
```

https://modulo-sorteios.azurewebsites.net/editar/senha/{email}

```console
{
	"senhaAtual": string, 
	"senha": string,  
}
```

https://modulo-sorteios.azurewebsites.net/usuarios/esqueci-senha

```console
{
	"email": string 
}
```

https://modulo-sorteios.azurewebsites.net/usuarios/editar/tipo/{email}

```console
{
    "adm": boolean
}
```
**MARCAS**

| MÉTODO | LINK | PERMISSÃO |
|-----|------|-----------|
| POST | https://modulo-sorteios.azurewebsites.net/marcas/registrar | ADMIN     |
| GET | https://modulo-sorteios.azurewebsites.net/marcas/obter | X         |
| GET | https://modulo-sorteios.azurewebsites.net/marcas/obter/{id} | ADMIN     |
| PUT | https://modulo-sorteios.azurewebsites.net/marcas/editar/{id} | ADMIN     |
| DELETE | https://modulo-sorteios.azurewebsites.net/marcas/{id} | ADMIN     |
| GET | https://modulo-sorteios.azurewebsites.net/marcas/vitrine | USER      |

```console
{
    "nome": string,
    "titulo": string, 
    "logo": null, // byte array
    "banner": null, // byte array
    "ordemExibicao": int
}
```

| PUT | https://modulo-sorteios.azurewebsites.net/marcas/editar/{id}

``` console
{
    "nome": string,
    "titulo": string, 
    "logo": null,
    "banner": null,
    "ordemExibicao": int
}

```
**PRÊMIOS**

| MÉTODO | LINK                                                           | PERMISSÃO |
|-----|----------------------------------------------------------------|-----------|
| POST | https://modulo-sorteios.azurewebsites.net/premios/registrar    | ADMIN |
| GET | https://modulo-sorteios.azurewebsites.net/premios/obter        | ADMIN |
| GET| https://modulo-sorteios.azurewebsites.net/premios/obter/{id}   | ADMIN |
| PUT| https://modulo-sorteios.azurewebsites.net/premios/editar/{id}  | ADMIN |
| DELETE | https://modulo-sorteios.azurewebsites.net/premios/{id}         | ADMIN |


https://modulo-sorteios.azurewebsites.net/premios/registrar
``` console
{
  "nome": string,
  "codigoSku": string,
  "imagem": null, //byte array
  "quantidade": int,
  "descricao": string,
  "marcaId": int
}
```

| PUT | https://modulo-sorteios.azurewebsites.net/premios/editar/{id}
``` console
{
  "nome": string,
  "codigoSku": "string,
  "imagem": null, //byte array
  "quantidade": int,
  "descricao": string,
  "marcaId": int
}


```
OBS:
https://modulo-sorteios.azurewebsites.net/premios/obter
Só aparece 1 ou mais premios

**SORTEIOS**

| MÉTODO | LINK                                                                        | PERMISSÃO |
|--------|-----------------------------------------------------------------------------|-----------|
| POST   | https://modulo-sorteios.azurewebsites.net/sorteios/sortear                   | ADMIN     |
| POST   | https://modulo-sorteios.azurewebsites.net/sorteios/participantes-do-sorteio | ADMIN     |
| GET    | https://modulo-sorteios.azurewebsites.net/sorteios/sorteios-da-semana       | ADMIN     |
| GET    | https://modulo-sorteios.azurewebsites.net/sorteios/historico-sorteio        | USER   |
| POST   | https://modulo-sorteios.azurewebsites.net/sorteios/historico-sorteio/turma  | USER   |

https://modulo-sorteios.azurewebsites.net/sorteios/sortear

```console
{
    "sorteio_surpresa": Boolean || true/false,
    "email_autenticado": String,
    "codigo_sku": String //"SKU1230"
}
```

https://modulo-sorteios.azurewebsites.net/sorteios/participantes-do-sorteio

```console
{
    "sorteio_surpresa": Boolean || 0/1,
}
```
https://modulo-sorteios.azurewebsites.net/sorteios/historico-sorteio/turma
```console
{
 "turmaNome": String
}
```
**TURMA**

| MÉTODO | LINK                                                                        | PERMISSÃO |
|--------|-----------------------------------------------------------------------------|-----------|
| GET    | https://modulo-sorteios.azurewebsites.net/turmas/obter                | USER     |



**DESTAQUE INDEX**

| MÉTODO | LINK                                                         | PERMISSÃO |
|--------|--------------------------------------------------------------|-----------|
| GET    | https://modulo-sorteios.azurewebsites.net/index/obter        | X         |
| GET    | https://modulo-sorteios.azurewebsites.net/index/obter/{id}   | ADMIN     |
| POST   | https://modulo-sorteios.azurewebsites.net/index/registrar    | ADMIN     |
| PUT    | https://modulo-sorteios.azurewebsites.net/index/editar/{id}  | ADMIN     |
| DELETE | https://modulo-sorteios.azurewebsites.net/index/deletar/{id} | ADMIN     |


https://modulo-sorteios.azurewebsites.net/index/registrar


| Key    |   Value  |   Type    |
|--------|----------|-----------|
| nome   |  string  |   Text    |
| titulo |  string  |   Text    |
| imagem |  arquivo |   file    |


https://modulo-sorteios.azurewebsites.net/index/editar/{id}

| Key    |   Value  |   Type    |
|--------|----------|-----------|
| nome   |  string  |   Text    |
| titulo |  string  |   Text    |
| imagem |  arquivo |   file    |

https://modulo-sorteios.azurewebsites.net/index/deletar/{id}

| Key    |   Value  |   Type    |
|--------|----------|-----------|
| nome   |  string  |   Text    |
| titulo |  string  |   Text    |
| imagem |  arquivo |   file    |
