/**
 * Created by wshi on 1/8/2016.
 */

'use strict';

    var wechatBindApp = angular.module('wechatBindApp',['commonModule', 'ngCookies', 'pascalprecht.translate']);

wechatBindApp.
    constant('wechatBindApp.constant',{
        "ACCOUNT": "ACCOUNT",
        "PASSWORD": "PASSWORD"
    }).
    config(["$translateProvider", function($translateProvider){
    $translateProvider.useStaticFilesLoader({
        prefix: window.contextPath + '/i18n/hst-wechat-',
        suffix: '.json?v=2.5&clear=' + new Date().getTime()
    });
    $translateProvider.preferredLanguage('zh_CN');
    $translateProvider.fallbackLanguage('en_US');
    //$translateProvider.useLocalStorage();
}]);

wechatBindApp.controller('wechatBindApp.bindCtrl',
    [
        '$scope',
        '$http',
        'commonModule.updateQuerySrv',
        '$filter',
        'wechatBindApp.constant',
    function($scope, $http, updateQuerySrv, $filter, bindConstant){
    $scope.formData = {
        account:"",
        password:""
    };
    $scope.showErrorMessage = false;
    $scope.showAccountError = false;
    $scope.showPasswordError = false;
    $scope.disableBindBtn = false;
    $scope.loginErrorMessage = '';
    $scope.callbacks = {};
    $scope.addCallbacks = function(key, callback){
        if(!$scope.callbacks.hasOwnProperty(key))
            $scope.callbacks[key] = callback;
    };
    $scope.processForm = function(){
        $scope.showErrorMessage = false;
        $scope.showAccountError = false;
        $scope.showPasswordError = false;
        $scope.disableBindBtn = true;
        if (!$scope.formData.account || !$scope.formData.account.trim()){
            $scope.showAccountError = true;
            $scope.showErrorMessage = true;
            $scope.loginErrorMessage = $filter('translate')('WECHAT_BIND.USER_ID_EMPTY');
            $scope.showErrorMessage = !!$scope.loginErrorMessage;
            $scope.disableBindBtn = false;
            typeof $scope.callbacks[bindConstant.ACCOUNT] === 'function' && $scope.callbacks[bindConstant.ACCOUNT]();
            return;
        }

        if (!$scope.formData.password || !$scope.formData.password.trim()){
            $scope.showPasswordError = true;
            $scope.showErrorMessage = true;
            $scope.loginErrorMessage = $filter('translate')('WECHAT_BIND.PASSWORD_EMPTY');
            $scope.showErrorMessage = !!$scope.loginErrorMessage;
            $scope.disableBindBtn = false;
            typeof $scope.callbacks[bindConstant.PASSWORD] === 'function' && $scope.callbacks[bindConstant.PASSWORD]();
            return;
        }

        $http({
            method  : 'POST',
            url     : window.contextPath + '/wechatbind',
            data    : $.param($scope.formData),  // pass in data as strings
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
        }).then(function(Obj){
            var data = Obj.data;
            console.log("Redirect Url is " + data.msUrl);
            if(data.msUrl){
                var token = data.token;
                var userId = '';
                if(data.userType && data.userType.toLowerCase() !== 'local'){
                    if(data.loginName && data.domainName)
                        userId = data.domainName + '\\' + data.loginName;
                } else {
                    userId = !data.loginName ? '' : data.loginName;
                }
                document.cookie = ("token=" + token + "; path=" + data.contextPath + "; domain=" + data.cookieDomain);
                var url = (new updateQuerySrv(data.msUrl)).updateQuery('token', token).updateQuery('userId', userId).getUrl();
                console.log(url);
                var toast = $('#toast');
                toast.show();
                setTimeout(function(){
                    toast.hide();
                    top.window.location.replace(url);
                },2000);
            }

        },function(data){
            switch(data.status){
                case 403:
                    $scope.loginErrorMessage = $filter('translate')('WECHAT_BIND.NOT_ENOUGH_PERMISSION');
                    break;
                case 803:
                    $scope.loginErrorMessage = $filter('translate')('WECHAT_BIND.USER_LOCKED');
                    break;
                case 804:
                    $scope.loginErrorMessage = $filter('translate')('WECHAT_BIND.USER_ID_OR_PASSWORD_ERROR');
                    break;
                default:
                    $scope.loginErrorMessage = 'Server Error';
                    break;
            }
            console.log("Server Error!");
            $scope.showErrorMessage = !!$scope.loginErrorMessage;
            $scope.disableBindBtn = false;
        });
    };
    $scope.hideErrorMessage = function(){
        $scope.showErrorMessage = false;
    };
    $scope.focusOnTarget = function(targetId){
        $('#' + targetId).focus();
    }
}]);

wechatBindApp.directive('loginInput', ['wechatBindApp.constant', function(bindConstant){
    return {
        restrict: 'E',
        replace: true,
        templateUrl: window.contextPath + '/pages/sections/common/loginInput.html',
        scope: {
            icon: '@',
            placeHolder: '@',
            bind: '=',
            type: '@',
            autoFocus: '@',
            errorFlag: '=',
            contentChange: '&',
            addCallback: '&'
        },
        controller: ['$scope', function($scope) {
            //TODO
        }],
        compile: function (element, attributes) {
            return {
                pre: function preLink(scope, element, attributes) {
                },
                post: function postLink(scope, element, attributes) {
                    scope.addCallback({"focus" : function(){
                        element.children().eq(1).focus();
                    }});
                    if(scope.autoFocus)
                        element.children().eq(1).focus();
                    scope.delProcess = function(){
                        scope.bind = '';
                        //set focus
                        element.children().eq(1).focus();
                    }
                }
            }
        }
    }
}]);
