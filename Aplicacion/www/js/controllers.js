angular.module('starter.controllers', [])
.controller('RegistroCtrl',['$scope','doctores','ServicioApi','EstadoService','CredencialesService',function($scope,doctores,ServicioApi,EstadoService,CredencialesService){
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
			if(data.status == 200){
				CredencialesService.usuario = data.data;
				if(data.data.grupo == 'pacientes'){
					EstadoService.homePaciente();
				}else{
					EstadoService.homeDoctor();
				}
			}else{
				$scope.registro = {};
				$scope.registro.grupo = 'doctores';
			}
		});
	}
}])
.controller('LoginCtrl',['$scope','ServicioApi','EstadoService','CredencialesService',function($scope,ServicioApi,EstadoService,CredencialesService){
	$scope.login = {};
	$scope.autenticar = function(){
		ServicioApi.autenticar($scope.login).then(function(data){
			if(data.status == 200 ){
				CredencialesService.usuario =data.data;
				if(data.data.grupo == 'doctores'){
					EstadoService.homeDoctor();
				}else{
					EstadoService.homePaciente();
				}
			}else{
				$scope.login={};
			}
		});
	}
}])
.controller('DoctorHomeCtrl',['$scope','pacientes',function($scope,pacientes){
	$scope.pacientes = pacientes.data.usuarios;

}])
.controller('DoctorPacienteCtrl',['$scope','episodios','reporteCausas',function($scope,episodios,reporteCausas){
	$scope.episodios = episodios.data.episodios;
	$scope.alarmas = reporteCausas.data.alarmas;
}])
.controller('DoctorEpisodioCtrl',['$scope','episodio',function($scope,episodio){
	$scope.episodio = episodio.data;
}])
.controller('DoctoresBusquedaCtrl',['$scope','ServicioApi',function($scope,ServicioApi){
	$scope.busqueda = {};
	$scope.buscar = function(){
		ServicioApi.buscarPaciente($scope.busqueda.cedula).then(function(data){
			var resultadoOriginal = data.data.episodios;
			if($scope.busqueda.inicio && $scope.busqueda.fin){
				var arregloInicio = $scope.busqueda.inicio.split("-"),
					inicio= new Date(arregloInicio[0],arregloInicio[1]-1,arregloInicio[2]),
					arregloFin = $scope.busqueda.fin.split("-"),
					fin=new Date(arregloFin[0],arregloFin[1]-1,arregloFin[2]),
					arregloProvisional = []
				;
				for(var i =0; i<resultadoOriginal.length;i++){
					var fechaArreglo = resultadoOriginal[i].fecha.substring(0,10).split("-");
					var actual = new Date(fechaArreglo[0],fechaArreglo[1]-1,fechaArreglo[2]);
					if(actual>=inicio && actual<=fin){
						arregloProvisional.push(resultadoOriginal[i]);
					}
				}
				$scope.busqueda.resultados = arregloProvisional;
			}else{
				$scope.busqueda.resultados = resultadoOriginal;
			}
		});
	}

}])
.controller('PacienteEpisodioCtrl',['$scope','causas','medicamentos','ServicioApi',function($scope,causas,medicamentos,ServicioApi){
	$scope.episodio = {}
	$scope.causas = causas.data.causas;
	$scope.medicamentos = medicamentos.data.medicamentos;
	$scope.causasSeleccionadas = {};

	$scope.crearCausa = function(){
		ServicioApi.crearCausa($scope.nuevo.causa).then(function(data){
			$scope.nuevo.causa.id = data.id;
			$scope.causas[Object.keys($scope.causas).length] = $scope.nuevo.causa;
			$scope.causasSeleccionadas[Object.keys($scope.causasSeleccionadas).length] = {
				id:data.id
			} 
		});
	}

	$scope.crearMedicamento = function(){
		ServicioApi.crearMedicamento($scope.nuevo.medicamento).then(function(data){
			$scope.nuevo.medicamento.id = data.id;
			$scope.medicamentos[Object.keys($scope.causasSeleccionadas).length] = $scope.nuevo.medicamento;
			$scope.medicamento = $scope.nuevo.medicamento;
		});
	}

	$scope.crearEpisodio = function(){
		console.log($scope.episodio);
	}

}])
.controller('PacienteCausasCtrl',['$scope','causas',function($scope,causas){
	$scope.alarmas = causas.data.alarmas;
}])
.controller('PacienteHistorialCtrl',['$scope','episodios',function($scope,episodios){
	$scope.episodios = episodios.data.episodios;
}]);
