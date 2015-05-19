angular.module('starter.controllers', [])

.controller('DashCtrl', function($scope) {})

.controller('ChatsCtrl', function($scope, Chats) {
  $scope.chats = Chats.all();
  $scope.remove = function(chat) {
    Chats.remove(chat);
  }
})

.controller('ChatDetailCtrl', function($scope, $stateParams, Chats) {
  $scope.chat = Chats.get($stateParams.chatId);
})

.controller('AccountCtrl', function($scope) {
  $scope.settings = {
    enableFriends: true
  };
})
.controller('RegistroCtrl',['$scope','doctores','ServicioApi',function($scope,doctores,ServicioApi){
	$scope.registro = {};
	$scope.preregistro = {};
	$scope.preregistro.doctores = doctores.data.doctotres;
	$scope.registro.grupo = 'doctores';
	$scope.registrar = function(){
		if($scope.registro.grupo == 'pacientes'){
			$scope.registro.doc = {
				id : $scope.preregistro.iddoctor
			};
		}
		ServicioApi.nuevoUsuario($scope.registro).then(function(data){
			console.log(data);
		});
	}
}]);
