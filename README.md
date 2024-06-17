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
**MARCAS**

| MÉTODO | LINK | PERMISSÃO |
|-----|------|-----------|
| POST | https://modulo-sorteios.azurewebsites.net/marcas/registrar | ADMIN |
| GET | https://modulo-sorteios.azurewebsites.net/marcas/obter | ADMIN |
| GET | https://modulo-sorteios.azurewebsites.net/marcas/obter/{id} | ADMIN |
| PUT | https://modulo-sorteios.azurewebsites.net/marcas/editar/{id} | ADMIN |
| DELETE | https://modulo-sorteios.azurewebsites.net/marcas/{id} | ADMIN |
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
**PRÊMIOS**

| MÉTODO | LINK | PERMISSÃO |
|-----|------|-----------|
| POST | https://modulo-sorteios.azurewebsites.net/premios/registrar | ADMIN |
| GET | https://modulo-sorteios.azurewebsites.net/premios/obter | ADMIN |
| GET| https://modulo-sorteios.azurewebsites.net/premios/obter/{id} | ADMIN |
| PUT| https://modulo-sorteios.azurewebsites.net/premios/editar/{id} | ADMIN |
| DELETE | https://modulo-sorteios.azurewebsites.net/premios/{id} | ADMIN |
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
**SORTEIOS**

| MÉTODO | LINK | PERMISSÃO |
|-----|------|-----------|
| POST | https://modulo-sorteios.azurewebsites.net/premios/sortear | ADMIN |
| POST | https://modulo-sorteios.azurewebsites.net/sorteios/participantes-do-sorteio | ADMIN |
| GET| https://modulo-sorteios.azurewebsites.net/sorteios/sorteios-da-semana | ADMIN |

https://modulo-sorteios.azurewebsites.net/sorteios/sortear

```console
{
    "sorteio_surpresa": Boolean || 0/1,
    "email_administrador": String,
    "codigo_sku": String //"SKU1230"
}
```

https://modulo-sorteios.azurewebsites.net/sorteios/participantes-do-sorteio

```console
{
    "sorteio_surpresa": Boolean || 0/1,
}
```




