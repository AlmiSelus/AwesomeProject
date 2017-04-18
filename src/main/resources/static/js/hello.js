var app = angular.module('hello', []);
app.controller('home', function($scope, $http) {
    $http.get('/api/recipe-0').then(function (response) {
        $scope.recipes = response.data;
    });
});