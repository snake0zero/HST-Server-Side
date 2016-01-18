/**
 * Created by wshi on 1/8/2016.
 */

'use strict';

var wechatBindApp = angular.module('wechatBindApp',['commonModule']);

wechatBindApp.controller('wechatBindApp.bindCtrl', ['$scope', '$http', 'commonModule.updateQuerySrv',
    function($scope, $http, updateQuerySrv){
    $scope.formData = {};
    $scope.showErrorMessage = false;
    $scope.showAccountError = false;
    $scope.showPasswordError = false;
    $scope.loginErrorMessage = '';

    $scope.processForm = function(){
        if (!$scope.formData.account || !$scope.formData.account.trim()){
            $scope.showAccountError = true;
            $scope.showErrorMessage = true;
            $scope.loginErrorMessage = 'User ID cannot be empty';
            $scope.showErrorMessage = !!$scope.loginErrorMessage;
            return;
        }

        if (!$scope.formData.password || !$scope.formData.password.trim()){
            $scope.showPasswordError = true;
            $scope.showErrorMessage = true;
            $scope.loginErrorMessage = 'Password cannot be empty';
            $scope.showErrorMessage = !!$scope.loginErrorMessage;
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
                top.window.location.replace((new updateQuerySrv(data.msUrl)).updateQuery('token', token).updateQuery('userId', userId).getUrl());
            }

        },function(){
            console.log("Server Error!");
            $scope.loginErrorMessage = 'Server Error';
            $scope.showErrorMessage = !!$scope.loginErrorMessage;
        });
    };
    $scope.hideErrorMessage = function(){
        $scope.showErrorMessage = false;
    };
}]);
