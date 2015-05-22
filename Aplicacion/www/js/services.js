angular.module('starter.services', [])
.factory('ServicioApi',['CredencialesService','$http',function(CredencialesService,$http){
  var getDoctores = function(){
    var req = {
     method: 'GET',
     url: 'http://localhost:8085/api/doctores',
     headers: {  
       'Content-Type': 'application/json',
       'autenticado': 'true'
     }
    }
    return $http(req);
  };

  var registrar = function(usuario){
    var req = {
     method: 'POST',
     url: 'http://localhost:8085/api/auth/registro',
     headers: {
       'Content-Type': 'application/json'
     },
     data:usuario
    }
    return $http(req);
  };

  var getPacientes = function(){
      var req = {
     method: 'GET',
     url: 'http://localhost:8085/api/doctores/'+CredencialesService.usuario.id+'/pacientes',
     headers: {  
       'Content-Type': 'application/json',
       'autenticado': 'true'
     }
    }
    return $http(req);
  }

  var login = function(usuario){
    var req = {
     method: 'POST',
     url: 'http://localhost:8085/api/auth/login',
     headers: {
       'Content-Type': 'application/json'
     },
     data:usuario
    }
    return $http(req);
  };

  var getEpisodios = function(idPaciente){
    var req = {
     method: 'GET',
     url: 'http://localhost:8085/api/paciente/'+idPaciente+'/episodio',
     headers: {  
       'Content-Type': 'application/json',
       'autenticado': 'true'
     }
    }
    return $http(req);
  };

  var getReporteCausas = function(idPaciente){
    var req = {
     method: 'GET',
     url: 'http://localhost:8085/api/paciente/'+idPaciente+'/reporteCausas',
     headers: {  
       'Content-Type': 'application/json',
       'autenticado': 'true'
     }
    }
    return $http(req);
  };

  var getEpisodio = function(idEpisodio){
    var req = {
     method: 'GET',
     url: 'http://localhost:8085/api/paciente/episodio/'+idEpisodio,
     headers: {  
       'Content-Type': 'application/json',
       'autenticado': 'true'
     }
    }
    return $http(req);
  };

  var buscarPaciente = function(cedulaPaciente){
    var req = {
     method: 'GET',
     url: 'http://localhost:8085/api/paciente/cedula/'+cedulaPaciente+'/episodio',
     headers: {  
       'Content-Type': 'application/json',
       'autenticado': 'true'
     }
    }
    return $http(req);
  }

  var getCausas = function(){
    var req = {
     method: 'GET',
     url: 'http://localhost:8085/api/paciente/'+CredencialesService.usuario.id+'/causa',
     headers: {  
       'Content-Type': 'application/json',
       'autenticado': 'true'
     }
    }
    return $http(req);
  }

  var getMedicamentos = function(){
    var req = {
     method: 'GET',
     url: 'http://localhost:8085/api/paciente/'+CredencialesService.usuario.id+'/medicamento',
     headers: {  
       'Content-Type': 'application/json',
       'autenticado': 'true'
     }
    }
    return $http(req);
  }

  return {
    darDoctores: getDoctores,
    nuevoUsuario: registrar,
    autenticar:login,
    pacientesDoctor:getPacientes,
    episodios:getEpisodios,
    reporteCausas:getReporteCausas,
    episodio:getEpisodio,
    buscarPaciente:buscarPaciente,
    causas:getCausas,
    medicamentos:getMedicamentos
  }
}])
.factory('EstadoService',['$state',function($state){

  function homeDoctor(){
    $state.go('doctor.home');
  }

  function homePaciente(){
    $state.go('paciente.home');
  }

  return{
    homeDoctor:homeDoctor,
    homePaciente:homePaciente
  }

}])
.factory('CredencialesService',[function(){
  var usuario ={};
  return{
    usuario:usuario
  }
}]);
