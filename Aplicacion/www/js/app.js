// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.services' is found in services.js
// 'starter.controllers' is found in controllers.js
angular.module('starter', ['ionic', 'starter.controllers', 'starter.services'])

.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
    }
    if (window.StatusBar) {
      // org.apache.cordova.statusbar required
      StatusBar.styleLightContent();
    }
  });
})

.config(function($stateProvider, $urlRouterProvider) {

  // Ionic uses AngularUI Router which uses the concept of states
  // Learn more here: https://github.com/angular-ui/ui-router
  // Set up the various states which the app can be in.
  // Each state's controller can be found in controllers.js
  $stateProvider
  .state('autenticacion',{
    url:'/autenticacion',
    templateUrl:'templates/autenticacion.html',
    controller:'LoginCtrl'
  })
  .state('registro',{
    url:'/registro',
    templateUrl:'templates/registro.html',
    resolve:{
      doctores : function(ServicioApi){
        return ServicioApi.darDoctores();
      }
    },
    controller:'RegistroCtrl'
  })

  // setup an abstract state for the tabs directive
    .state('doctor', {
    url: "/doctor",
    abstract: true,
    templateUrl: "templates/tabs.html"
  })

  // Each tab has its own nav history stack:

  .state('doctor.home', {
    url: '/home',
    views: {
      'doctor-home': {
        templateUrl: 'templates/doctores-home.html',
        controller:'DoctorHomeCtrl'
      }
    },
    resolve:{
      pacientes: function(ServicioApi){
        return ServicioApi.pacientesDoctor();
      }
    }
  })
  .state('doctor.paciente',{
    url:'/paciente/:idPaciente',
    views:{
      'doctor-home':{
        templateUrl:'templates/doctores-paciente.html',
        controller:'DoctorPacienteCtrl'
      }
    },
    resolve:{
      episodios: function(ServicioApi, $stateParams){
        return ServicioApi.episodios($stateParams.idPaciente);
      },
      reporteCausas: function(ServicioApi,$stateParams){
        return ServicioApi.reporteCausas($stateParams.idPaciente);
      }
    }
  })
  .state('doctor.episodio',{
    url:'/paciente/episodio/:idEpisodio',
    views:{
      'doctor-home':{
        templateUrl:'templates/doctores-episodio.html',
        controller:'DoctorEpisodioCtrl'
      }
    },
    resolve:{
      episodio:function(ServicioApi, $stateParams){
        return ServicioApi.episodio($stateParams.idEpisodio);
      }
    }
  })
  .state('doctor.buscar',{
    url:'/buscar',
    views:{
      'doctor-buscar':{
        templateUrl:'templates/doctores-buscar.html',
        controller:'DoctoresBusquedaCtrl'
      }
    }
  })
  .state('paciente', {
    url: "/paciente",
    abstract: true,
    templateUrl: "templates/tabs-paciente.html"
  })

  // Each tab has its own nav history stack:

  .state('paciente.home', {
    url: '/home',
    views: {
      'paciente-home': {
        templateUrl: 'templates/pacientes-home.html',
        controller: 'PacienteEpisodioCtrl'
      }
    },
    resolve:{
      causas:function(ServicioApi){
        return ServicioApi.causas();
      },
      medicamentos:function(ServicioApi){
        return ServicioApi.medicamentos();
      }

    }
  })
  .state('paciente.causas',{
    url:'/causas',
    views:{
      'paciente-causas':{
        templateUrl:'templates/pacientes-causa.html',
        controller:'PacienteCausasCtrl'
      }
    },
    resolve:{
      causas: function(ServicioApi, CredencialesService){
        return ServicioApi.reporteCausas(CredencialesService.usuario.id);
      }
    }
  })
  .state('paciente.historial',{
    url:'/historial',
    views:{
      'paciente-historial':{
        templateUrl:'templates/pacientes-historial.html',
        controller:'PacienteHistorialCtrl'
      }
    },
    resolve:{
      episodios: function(ServicioApi, CredencialesService){
        return ServicioApi.episodios(CredencialesService.usuario.id);
      }
    }
  }).state('paciente.episodio',{
    url:'/episodio/:idEpisodio',
    views:{
      'paciente-historial':{
        templateUrl:'templates/doctores-episodio.html',
        controller:'DoctorEpisodioCtrl'
      }
    },
    resolve:{
      episodio:function(ServicioApi, $stateParams){
        return ServicioApi.episodio($stateParams.idEpisodio);
      }
    }
  })

  // if none of the above states are matched, use this as the fallback
  $urlRouterProvider.otherwise('/autenticacion');

});
