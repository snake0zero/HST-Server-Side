/**
 * Created by wshi on 1/11/2016.
 */
'use strict';
var commonModule = angular.module('commonModule', []);
commonModule.factory('commonModule.updateQuerySrv', function() {
    var updateQuerySrv = function(url){
        var arr = url.split("#");
        var result = arr[0];
        var fragment = arr.length > 1 ? arr[1] : '';
        this.updateQuery = function(key, value){
            var re = new RegExp("([?&])" + key + "=.*?(&|$)", "i");
            var separator = result.indexOf('?') !== -1 ? "&" : "?";
            if (result.match(re)) {
                result =  result.replace(re, '$1' + key + "=" + value + '$2');
            }
            else {
                result =  result + separator + key + "=" + value;
            }
            return this;
        };
        this.getUrl = function(){
            return result + '#' + fragment;
        }
    };
    return updateQuerySrv;
});
