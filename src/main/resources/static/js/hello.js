var app = angular.module('hello', [ 'ngRoute' ]);
app.config(function ($routeProvider, $httpProvider, $locationProvider) {

    $routeProvider.when('/', {
        templateUrl : 'http://localhost:8080/partials/recipe/list.do',
        controller : 'RecipesController',
        controllerAs: 'controller'
    }).when('/login', {
        templateUrl : 'login.html',
        controller : 'navigation',
        controllerAs: 'controller'
    }).when('/confirm', {
        templateUrl : 'http://localhost:8080/partials/user/confirm.do',
        controller : 'ConfirmationController',
        controllerAs: 'controller'
    }).when('/register', {
        templateUrl : 'http://localhost:8080/partials/user/register.do',
        controller : 'RegisterController',
        controllerAs: 'controller'
    }).otherwise('/');

    $locationProvider.hashPrefix('');

    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
});

app.controller('RecipesController', function($scope, $http) {
    $http.get('http://localhost:8080/api/recipe-0').then(function (response) {
        console.log(response.data);
        $scope.recipes = response.data;
    });

    console.log($scope.username);
    console.log($scope.form);

});

app.controller('RegisterController', function ($scope, $http, $location) {
    $scope.register = function() {
        var userData = {
            'email': $scope.user.email,
            'password': $scope.user.password
        };

        $http.post('http://localhost:8080/api/user/register', userData)
            .then(function (response) {
                $location.url('/confirm');
            });
    };
});

app.controller('ConfirmationController', function ($scope, $http, $routeParams) {
    var hash = $routeParams.uh;
    if(typeof hash !== 'undefined') {
        $scope.message = 'Zaktualizowano profil! Możesz się zalogować!';
    } else {
        $scope.message = 'Aktywuj konto linkiem podanym w mailu!';
    }

    console.log($routeParams.uh);
    $http.post('http://localhost:8080/api/user/confirm', hash).then(function (response) {
        console.log(response);
    });
});