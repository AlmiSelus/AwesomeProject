var app = angular.module('hello', [ 'ngRoute' ]);
app.config(function ($routeProvider, $httpProvider, $locationProvider) {

    $routeProvider.when('/', {
        templateUrl : 'http://localhost:8080/partials/recipe/list.do',
        controller : 'RecipesController',
        controllerAs: 'controller'
    }).when('/login', {
        templateUrl : 'http://localhost:8080/partials/user/login.do',
        controller : 'LoginController',
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
            'password': $scope.user.password,
            'name' : $scope.user.name,
            'surname' : $scope.user.surname
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

app.controller('LoginController', function ($rootScope, $scope, $http, $location) {

    $scope.authenticate = function(credentials, callback) {

        var headers = credentials ? {
                        authorization : "Basic " + btoa(credentials.username + ":" + credentials.password)
                      } : {};

        $http.get("http://localhost:8080/api/user", {headers: headers}).then(function () {
            if (response.data.name) {
                $rootScope.authenticated = true;
                $rootScope.name = response.data.name;
            } else {
                $rootScope.authenticated = false;
            }

            console.log(response.data);

            callback && callback($rootScope.authenticated);
        }, function() {
            $rootScope.authenticated = false;
            callback && callback(false);
        });
    };

    $scope.logout = function() {
        $http.post('http://localhost:8080/api/user/logout', {}).finally(function() {
            $rootScope.authenticated = false;
            $location.url("/");
        });
    };

    $scope.credentials = {};

    $scope.login = function () {
        $scope.authenticate($scope.credentials, function(authenticated) {
            if (authenticated) {
                console.log("Login succeeded");
                $location.url("/");
                $scope.error = false;
                $rootScope.authenticated = true;
            } else {
                console.log("Login failed");
                $location.url("/login");
                $scope.error = true;
                $rootScope.authenticated = false;
            }
        });
    };
});