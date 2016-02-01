/**
 * Created by wshi on 1/8/2016.
 */

'use strict';

    var wechatBindApp = angular.module('wechatBindApp',['commonModule', 'ngCookies', 'pascalprecht.translate']);

wechatBindApp.config(["$translateProvider", function($translateProvider){
    $translateProvider.useStaticFilesLoader({
        prefix: window.contextPath + '/i18n/hst-wechat-',
        suffix: '.json?v=2.5'
    });
    $translateProvider.preferredLanguage('zh_CN');
    $translateProvider.fallbackLanguage('en_US');
    $translateProvider.useLocalStorage();
}]);

wechatBindApp.controller('wechatBindApp.bindCtrl',
    [
        '$scope',
        '$http',
        'commonModule.updateQuerySrv',
        '$filter',
    function($scope, $http, updateQuerySrv, $filter){
    $scope.formData = {};
    $scope.showErrorMessage = false;
    $scope.showAccountError = false;
    $scope.showPasswordError = false;
    $scope.disableBindBtn = false;
    $scope.loginErrorMessage = '';

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
            return;
        }

        if (!$scope.formData.password || !$scope.formData.password.trim()){
            $scope.showPasswordError = true;
            $scope.showErrorMessage = true;
            $scope.loginErrorMessage = $filter('translate')('WECHAT_BIND.PASSWORD_EMPTY');
            $scope.showErrorMessage = !!$scope.loginErrorMessage;
            $scope.disableBindBtn = false;
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
