# green-dog-delivery
Sistema de pedidos de uma lanchonete 

## Projeto pedido de uma lanchonete com Spring Boot Java e o Spring Security para a autenticação e autorização (3 perfis de autorização, ADMIN, MANAGER e COMMON_USER), usando docker para o servidor de banco de dados MSSQL.

##Abaixo segue o link do arquivo do docker-compose.yml para você subir após ter instalado e configurado o docker em seu computador. O Arquivo pode ser colocado em qualquer lugar do seu computador e para você rodar deve executar o seguinte comando:

Arquivo docker-compose para usar no comando abaixo:

[https://drive.google.com/file/d/14wpIbVxDhyRhFiOFW1qfA6Iym6rWNT08/view?usp=share_link]

### `docker-compose -f c:\pasta-do-arquivo\docker-compose.yaml up`

##Assim que você executar o docker irá baixar a imagem do SQL Server 2019 e irá subir um servior do MSSQL no seu docker na porta 1433

## E para baixar o SQL Server Management Studio 18.12.1 para você acessar o banco de dados que subiu usando o docker acima clique abaixo:

[https://catalog.s.download.windowsupdate.com/d/msdownload/update/software/updt/2022/06/ssms-setup-enu_2d6b129259b4870fb12d735f8bcd34403950075c.exe]

Para se conectar usando o SQL Server Management Studio ao abrí-lo coloque as seguintes opções:

Server Type: Database Engine <br/>
Server Name: Localhost <br/>
Authentication: SQL Server Authentication <br/>
Login: sa <br/>
Password: A_Str0ng_Required_Password <br/>

E crie uma database chamada greendog e rode o script abaixo dentro do SQL Server Management Studio para criar e popular as tabelas do banco de dados:

[https://drive.google.com/file/d/132l0sZuDyhNR0KOQE88Sex9rD0eM6YYw/view?usp=share_link]

## Após abrir o projeto com o a sua IDEA de preferência (Eclipse, Intellij, Spring Tools, Netbeans) basta rodar a aplicação que ficará disponível na porta 8080.

O Projeto foi feito seguindo o livro Spring Boot Acelere o desenvolvimento de microsserviços do Fernando Boaglio:

<p align="center">
<img src="https://cdn.shopify.com/s/files/1/0155/7645/products/SpringBoot_ebook_large.jpg" />
</p> 

Segue meu LinkedIn: [https://www.linkedin.com/in/bruno-araujo-oficial/]
