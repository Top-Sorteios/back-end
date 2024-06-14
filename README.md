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

**MARCAS**

| MÉTODO | LINK | PERMISSÃO |
|-----|------|-----------|
| POST | https://modulo-sorteios.azurewebsites.net/marcas/registrar | ADMIN |
| GET | https://modulo-sorteios.azurewebsites.net/marcas/obter | ADMIN |
| GET | https://modulo-sorteios.azurewebsites.net/marcas/obter/{id} | ADMIN |
| PUT | https://modulo-sorteios.azurewebsites.net/marcas/editar/{id} | ADMIN |
| DELETE | https://modulo-sorteios.azurewebsites.net/marcas/{id} | ADMIN |

**PRÊMIOS**

| MÉTODO | LINK | PERMISSÃO |
|-----|------|-----------|
| POST | https://modulo-sorteios.azurewebsites.net/premios/registrar | ADMIN |
| GET | https://modulo-sorteios.azurewebsites.net/premios/obter | ADMIN |
| GET| https://modulo-sorteios.azurewebsites.net/premios/obter/{id} | ADMIN |
| PUT| https://modulo-sorteios.azurewebsites.net/premios/editar/{id} | ADMIN |
| DELETE | https://modulo-sorteios.azurewebsites.net/premios/{id} | ADMIN |


<b>Formato Requisições</b>

| POST | https://modulo-sorteios.azurewebsites.net/usuarios/importar-usuario/{email} | ADMIN |

**Tipo de dado:**
*form-data*

**Obs:** *Email do caminho é o administrador que está cadastrando os usuarios*

```console
	file: {arquivo.xlsx}
```

https://modulo-sorteios.azurewebsites.net/usuarios/primeiro-acesso

```console
{
	"email": string, 
	"cpf": string,  
	"datanascimento": string, //exemplo:"AAAA/MM/DD"
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

https://modulo-sorteios.azurewebsites.net/usuarios/senha/{email}

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
| POST | https://modulo-sorteios.azurewebsites.net/marcas/registrar

```console
{
    "nome": string,
    "titulo": string, 
    "logo": null, // byte array
    "banner": null, // byte array
    "ordemExibicao": int,
    "criadoPor": Integer
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
| POST | https://modulo-sorteios.azurewebsites.net/premios/registrar

``` console
{
  "nome": string,
  "codigoSku": string,
  "imagem": null, //byte array
  "quantidade": int,
  "descricao": string,
  "marcaId": int,
  "criadoPor": int
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
