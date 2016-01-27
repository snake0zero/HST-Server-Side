/**
 * Created by wshi on 1/27/2016.
 */
'use strict';

var errorApp = angular.module('errorApp',['ngCookies', 'pascalprecht.translate']);

errorApp.config(["$translateProvider", function($translateProvider){
    $translateProvider.useStaticFilesLoader({
        prefix: window.contextPath + '/i18n/hst-wechat-',
        suffix: '.json?v=2.5'
    });
    $translateProvider.preferredLanguage('zh_CN');
    $translateProvider.fallbackLanguage('en_US');
    $translateProvider.useLocalStorage();
}]).run(function(){
    console.log("error process is run!");
});