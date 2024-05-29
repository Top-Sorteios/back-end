| METODO | LINK | PERMISSAO |
|-----|------|-----------|
| PUT | https://grupo-top-sorteios.azurewebsites.net/usuarios/primeiro-acesso | X |
| POST| https://grupo-top-sorteios.azurewebsites.net/usuarios/login | X |
| GET | https://grupo-top-sorteios.azurewebsites.net/usuarios/obter | ADMIN |
| GET | https://grupo-top-sorteios.azurewebsites.net/usuarios/obter/{id} | USER |
| PUT | https://grupo-top-sorteios.azurewebsites.net/usuarios/editar/senha/{email} | USER |
| GET | https://grupo-top-sorteios.azurewebsites.net/usuarios/helloworld | X |

<b>Formato Requisições</b>

https://grupo-top-sorteios.azurewebsites.net/usuarios/primeiro-acesso

```console
{
	"email": string, 
	"cpf": string,  
	"datanascimento": string, //exemplo:"AAAA/MM/DD"
 	"senha": string,		
}
```

https://grupo-top-sorteios.azurewebsites.net/usuarios/login

```console
{
	"email": string, 
	"cpf": string,  
}
```

https://grupo-top-sorteios.azurewebsites.net/usuarios/senha/{email}

```console
{
	"senhaAtual": string, 
	"senha": string,  
}
```
