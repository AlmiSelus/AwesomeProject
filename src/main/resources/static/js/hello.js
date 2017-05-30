var app = angular.module('hello', [ 'ngRoute', 'vcRecaptcha', 'ngMessages' ]);
app.config(function ($routeProvider, $httpProvider, $locationProvider, vcRecaptchaServiceProvider) {
    vcRecaptchaServiceProvider.setSiteKey('6LcbwCIUAAAAAGjuEk3pzNbcnvS1Z289hcaMkx0N');

    $routeProvider.when('/', {
        templateUrl : '/partials/recipe/list.do',
        controller : 'RecipeController',
        controllerAs: 'controller'
    }).when('/ingredients', {
        templateUrl : '/partials/ingredients/main.do',
        controller : 'IngredientsController',
        controllerAs: 'controller'
    }).when('/recipeEditor', {
        templateUrl : '/partials/recipe/editor.do',
        controller : 'IngredientsController',
        controllerAs: 'controller'
    }).when('/login', {
        templateUrl : '/partials/user/login.do',
        controller : 'LoginController',
        controllerAs: 'controller'
    }).when('/confirm', {
        templateUrl : '/partials/user/confirm.do',
        controller : 'ConfirmationController',
        controllerAs: 'controller'
    }).when('/register', {
        templateUrl : '/partials/user/register.do',
        controller : 'RegisterController',
        controllerAs: 'controller'
    }).otherwise('/');

    $locationProvider.hashPrefix('');

    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
});

app.controller('RecipesController', function($scope, $http) {
    $http.get('/api/recipe-0').then(function (response) {
        console.log(response.data);
        $scope.recipes = response.data;
    });

    console.log($scope.username);
    console.log($scope.form);

});

app.controller('RecipeController', function($scope, $http) {
    $http.get('/partials/recipe/editor').then(function (response) {
        console.log(response.data);
        $scope.recipes = response.data;
    });

    console.log($scope.username);
    console.log($scope.form);

});

app.controller('IngredientsController', function($scope, $http) {
    $http.get('/partials/ingredients/main').then(function (response) {
        console.log(response.data);
        $scope.recipes = response.data;
    });

    console.log($scope.username);
    console.log($scope.form);

});

app.controller('RegisterController', ['$scope', 'vcRecaptchaService', '$http', '$location', function ($scope, recaptcha, $http, $location) {
    $scope.register = function() {
        if(recaptcha.getResponse() !== '') {
            var userData = {
                'email': $scope.user.email,
                'password': $scope.user.password,
                'name' : $scope.user.name,
                'surname' : $scope.user.surname,
                'g-recaptcha-response' : recaptcha.getResponse()
            };

            $http.post('/api/user/register', userData)
                .then(function (response) {
                    $location.url(response.message);
                });
        }
    };

    $scope.widgetId = null;

    $scope.setWidgetId = function (widgetId) {
        console.info('Created widget ID: %s', widgetId);
        $scope.widgetId = widgetId;
    };


}]).directive("passwordVerify", function passwordVerify() {
    return {
        restrict: 'A', // only activate on element attribute
        require: '?ngModel', // get a hold of NgModelController
        link: function(scope, elem, attrs, ngModel) {
            if (!ngModel) return; // do nothing if no ng-model

            // watch own value and re-validate on change
            scope.$watch(attrs.ngModel, function() {
                validate();
            });

            // observe the other value and re-validate on change
            attrs.$observe('passwordVerify', function(val) {
                validate();
            });

            var validate = function() {
                // values
                var val1 = ngModel.$viewValue;
                var val2 = attrs.passwordVerify;

                // set validity
                ngModel.$setValidity('passwordVerify', val1 === val2);
            };
        }
    }
}).directive('checkStrength', function () {

    return {
        replace: false,
        restrict: 'EACM',
        scope: { model: '=checkStrength' },
        link: function (scope, element, attrs) {
            var strength = {
                colors: ['#F00', '#F90', '#FF0', '#9F0', '#0F0'],
                mesureStrength: function (p) {
                    var _force = 0;
                    var _regex = /[$-/:-?{-~!"^_`\[\]]/g; //" (Commentaire juste là pour pas pourrir la coloration sous Sublime...)

                    var _lowerLetters = /[a-z]+/.test(p);
                    var _upperLetters = /[A-Z]+/.test(p);
                    var _numbers = /[0-9]+/.test(p);
                    var _symbols = _regex.test(p);

                    var _flags = [_lowerLetters, _upperLetters, _numbers, _symbols];
                    var _passedMatches = $.grep(_flags, function (el) { return el === true; }).length;

                    _force += 2 * p.length + ((p.length >= 10) ? 1 : 0);
                    _force += _passedMatches * 10;

                    // penality (short password)
                    _force = (p.length <= 6) ? Math.min(_force, 10) : _force;

                    // penality (poor variety of characters)
                    _force = (_passedMatches == 1) ? Math.min(_force, 10) : _force;
                    _force = (_passedMatches == 2) ? Math.min(_force, 20) : _force;
                    _force = (_passedMatches == 3) ? Math.min(_force, 40) : _force;

                    return _force;
                },
                getColor: function (s) {
                    var idx = 0;
                    if (s <= 10) { idx = 0; }
                    else if (s <= 20) { idx = 1; }
                    else if (s <= 30) { idx = 2; }
                    else if (s <= 40) { idx = 3; }
                    else { idx = 4; }

                    return { idx: idx + 1, col: this.colors[idx] };
                }
            };

            scope.$watch('model', function (newValue, oldValue) {
                console.log('model = ' + newValue);
                if (!newValue || newValue === '') {
                    element.css({ "display": "none"  });
                } else {
                    var c = strength.getColor(strength.mesureStrength(newValue));
                    element.css({ "display": "inline" });
                    $(element.children('li'))
                        .css({ "background": "#DDD" })
                        .slice(0, c.idx)
                        .css({ "background": c.col });
                }
            });

        },
        template: '<li class="point"></li><li class="point"></li><li class="point"></li><li class="point"></li><li class="point"></li>'
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
    $http.post('/api/user/confirm', hash).then(function (response) {
        console.log(response);
    });
});

app.controller('LoginController', function ($rootScope, $scope, $http, $location) {

    $scope.authenticate = function(credentials, callback) {

        var headers = credentials ? { Authorization : "Basic " + btoa(credentials.username + ":" + credentials.password) } : {};
        $http.get("/api/user/login", {headers: headers}).then(function (response) {
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
        $http.post('/api/user/logout', {}).finally(function() {
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