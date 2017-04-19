var app = angular.module('hello', []);
app.controller('home', function($scope, $http) {
    $http.get('http://localhost:8080/api/recipe-0').then(function (response) {
        console.log(response.data);
        $scope.recipes = response.data;
    });
});