var app = angular.module("delivery", ["checklist-model"],
    function($locationProvider){
        $locationProvider.html5Mode({
              enabled: true,
              requireBase: false
        });
});

app.controller('pedidoController', function($scope,$location,$http) {

	$scope.itens = [];
	$scope.subTotal = 0;
	$scope.pedidoItens=[];
	$scope.cep = "72874491";
	
	 var carregaOferta= function () {
	        $http.get("/oferta").success(function (data) {
	          $scope.oferta = data["mensagem"];
	          $scope.servidor = data["servidor"];
	          $scope.debug = data["debug"];
	        }).error(function (data, status) {
	          $scope.message = "Aconteceu um problema: " + data;
	        });
	 };
	      
	 var carregarItens= function () {
	        $http.get( "/api/itens/all").success(function (data) {
	        $scope.itens = data;
	        }).error(function (data, status) {
	          $scope.message = "Aconteceu um problema: " + data;
	        });
	 };
	      
     $scope.fazerPedido = function(pedidoItens) {
          if(pedidoItens.length !== 0){
              $scope.message ="";
              var pedidoStr="";
              var prefixo="";
              for (var i=0; i< $scope.pedidoItens.length; i++) {
                  pedidoStr+=prefixo+$scope.pedidoItens[i].id;
                  prefixo=",";
              }
              $scope.urlPedido="/rest/pedido/novo/2/"+pedidoStr;
              $http.get($scope.urlPedido).success(function (data) {
                  $scope.idPedido= data["pedido"];
                  $scope.mensagem= data["mensagem"];
                  $scope.valorTotal= data["valorTotal"];
              }).error(function (data, status) {
                $scope.message = "Aconteceu um problema: "
                    +"Status:"+ data.status+ " - error:"+data.error;
              });
	      } else {
	         $scope.message = "Favor selecione um Item!"
	      }
	 };

	 $scope.validaCep = function(cep) {
           if(cep.length !== 7){
                 $scope.message ="";
                 $scope.urlValidaCep="/rest/pedido/novo/cep/"+cep;
                 $http.get($scope.urlValidaCep).success(function (data) {
                    console.log(data);
                    $scope.erro = data["erro"];
                    if ($scope.erro !== null && $scope.erro === "CEP inválido!") {
                        $scope.message = $scope.erro;
                    } else if ($scope.erro !== null && $scope.erro.indexOf('Entrega indisponível para este CEP') === 0) {
                        $scope.cep = data["cep"];
                        $scope.message = $scope.erro;
                    } else {
                        $scope.cep = data["cep"];
                        $scope.message = "O seu pedido será entregue no CEP " +  $scope.cep + " em aproximadamente 55 minutos.";
                    }
                 }).error(function (data, status) {
                   $scope.message = "Aconteceu um problema: "
                       +"Status:"+ data.status+ " - error:"+data.error;
                 });
          } else {
             $scope.message = "Favor informe um CEP com 8 caracteres!"
          }
     };

     $scope.isItemSelecionado = function() { 
    	 
    	 if (this.checked)
   		  $scope.subTotal+=this.i.preco;
    	 else
   		  $scope.subTotal-=this.i.preco;    		 

     }

   carregarItens();
   carregaOferta();
	  
});
 