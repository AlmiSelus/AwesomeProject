var app = angular.module('hello', [ 'ngRoute', 'vcRecaptcha', 'ngMessages', 'ui.select', 'ngSanitize' ]);
app.config(function ($routeProvider, $httpProvider, $locationProvider, vcRecaptchaServiceProvider) {
    vcRecaptchaServiceProvider.setSiteKey('6LcbwCIUAAAAAGjuEk3pzNbcnvS1Z289hcaMkx0N');

    var partialsEndpoint = window.location.protocol + "//" + window.location.hostname + (window.location.port == 8080 ? ':8080' : '') + '/partials';
    console.log(partialsEndpoint);

    $routeProvider.when('/', {
        templateUrl : partialsEndpoint+'/recipe/list.do',
        controller : 'RecipesController',
        controllerAs: 'controller'
    }).when('/login', {
        templateUrl : partialsEndpoint+'/user/login.do',
        controller : 'LoginController',
        controllerAs: 'controller'
    }).when('/confirm', {
        templateUrl : partialsEndpoint+'/user/confirm.do',
        controller : 'ConfirmationController',
        controllerAs: 'controller'
    }).when('/register', {
        templateUrl : partialsEndpoint+'/user/register.do',
        controller : 'RegisterController',
        controllerAs: 'controller'
    }).when('/fridge', {
        templateUrl : partialsEndpoint+'/fridge/fridge.do',
        controller : 'FridgeController',
        controllerAs: 'controller'
    }).when('/ingredients', {
        templateUrl : partialsEndpoint+'/ingredients/main.do',
        controller : 'IngredientsController',
        controllerAs: 'controller'
    }).when('/recipeEditor', {
        templateUrl : partialsEndpoint+'/recipe/editor.do',
        controller : 'IngredientsController',
        controllerAs: 'controller'
    }).otherwise('/');

    $locationProvider.hashPrefix('');

    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
});

app.run(function($rootScope, $location) {
    $rootScope.apiEndpoint = $location.protocol() + '://' + $location.host() + ($location.port() == 8080 ? ':8080' : '') + '/api';
});

app.controller('RecipesController', function($rootScope, $scope, $http) {
    $http.get($rootScope.apiEndpoint+'/recipe-0').then(function (response) {
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

app.controller('RecipeController', function($scope, $http) {
    $http.get('/partials/recipe/editor').then(function (response) {
        console.log(response.data);
        $scope.recipes = response.data;
    });

    console.log($scope.username);
    console.log($scope.form);

});

app.controller('RegisterController', ['$scope', 'vcRecaptchaService', '$http', '$location', '$rootScope', function ($scope, recaptcha, $http, $location, $rootScope) {
    $scope.register = function() {
        if(recaptcha.getResponse() !== '') {
            var userData = {
                'email': $scope.user.email,
                'password': $scope.user.user_password,
                'name' : $scope.user.name,
                'surname' : $scope.user.surname,
                'g-recaptcha-response' : recaptcha.getResponse()
            };

            $http.post($rootScope.apiEndpoint+'/user/register', userData)
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

app.controller('ConfirmationController', function ($rootScope, $scope, $http, $routeParams) {
    var hash = $routeParams.uh;
    if(typeof hash !== 'undefined') {
        $scope.message = 'Zaktualizowano profil! Możesz się zalogować!';
    } else {
        $scope.message = 'Aktywuj konto linkiem podanym w mailu!';
    }

    console.log($routeParams.uh);
    $http.post($rootScope.apiEndpoint+'/user/confirm', hash).then(function (response) {
        console.log(response);
    });
});

app.controller('LoginController', function ($rootScope, $scope, $http, $location) {

    $scope.authenticate = function(credentials, callback) {

        console.log('credentials = ');
        console.log(credentials);

        // var headers = credentials ? { Authorization : "Basic " + btoa(credentials.username + ":" + credentials.password) } : {};
        $http.post($rootScope.apiEndpoint+'/user', credentials).then(function (response) {
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
        $http.post($rootScope.apiEndpoint+'/user/logout', {}).finally(function() {
            $rootScope.authenticated = false;
            $location.url("/");
        });
    };

    $scope.credentials = {};

    $scope.login = function () {
        $scope.authenticate($scope.credentials, function(authenticated) {
            if (authenticated) {
                console.log("Login succeeded");
                $location.url("/fridge");
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

app.controller('FridgeController', function ($rootScope, $scope, $http, $location) {
    if($rootScope.authenticated) {
        $scope.ingredients = [];
        $scope.selected = { selectedIngredients : [] };

        $http.get($rootScope.apiEndpoint + '/fridge/ingredients').then(function (response) {
            $scope.selected.selectedIngredients = response.data;
        }, function() {
            $location.url('/login');
        });

        $http.get($rootScope.apiEndpoint+'/ingredients/all').then(function(response){
            console.log(response);
            $scope.ingredients = response.data;
        }, function () {
            $location.url('/login');
        });

        $scope.clicked = false;
        $scope.doneOk  = false;

        $scope.addIngredients = function() {
            $scope.clicked = true;
            $scope.doneOk = false;

            $http.post($rootScope.apiEndpoint + "/fridge/addIngredients", $scope.selected.selectedIngredients).then(function(response){
                $scope.clicked = false;
                $scope.doneOk = true;
            }, function() {
                $location.url('/login');
            });

        };

    } else {
        $location.url("/login");
    }
}).filter('propsFilter', function() {
    return function(items, props) {
        var out = [];

        if (angular.isArray(items)) {
            items.forEach(function(item) {
                var itemMatches = false;

                var keys = Object.keys(props);
                for (var i = 0; i < keys.length; i++) {
                    var prop = keys[i];
                    var text = props[prop].toLowerCase();
                    if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                        itemMatches = true;
                        break;
                    }
                }

                if (itemMatches) {
                    out.push(item);
                }
            });
        } else {
            // Let the output be the input untouched
            out = items;
        }

        return out;
    };
});