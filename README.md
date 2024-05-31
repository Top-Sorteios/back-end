| METODO | LINK | PERMISSAO |
|-----|------|-----------|
| POST | https://grupo-top-sorteios.azurewebsites.net/usuarios/registrar | X |
| PUT | https://grupo-top-sorteios.azurewebsites.net/usuarios/primeiro-acesso | X |
| POST| https://grupo-top-sorteios.azurewebsites.net/usuarios/login | X |
| GET | https://grupo-top-sorteios.azurewebsites.net/usuarios/obter | ADMIN |
| GET | https://grupo-top-sorteios.azurewebsites.net/usuarios/obter/{id} | USER |
| PUT | https://grupo-top-sorteios.azurewebsites.net/usuarios/editar/senha/{email} | USER |
| GET | https://grupo-top-sorteios.azurewebsites.net/usuarios/helloworld | X |


<b>Formato Requisições</b>

| POST | https://grupo-top-sorteios.azurewebsites.net/usuarios/registrar | X |

```console
{
	"nome": "string",
   	 "cpf": "string",
   	 "email": "string",
   	 "dataNascimento": "string", //exemplo:"AAAA/MM/DD"
   	 "turma": "string",
   	 "status": "string",
   	 "administrador": "string", //USER ou ADMIN
	"criadoPor": 2, //aqui é passado o id de um admin, pode ultilizar o 2 que é um id de admmin
```

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
	"senha": string,  
}
```

https://grupo-top-sorteios.azurewebsites.net/usuarios/senha/{email}

```console
{
	"senhaAtual": string, 
	"senha": string,  
}
```


