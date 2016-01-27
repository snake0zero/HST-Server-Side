<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ include file="common.jsp" %>
<!doctype html>
<html ng-app="errorApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1" />
<meta name="description" content="">
<title>Polycom® {{'WECHAT_ERROR.TITLE' | translate}}</title>
<link rel="shortcut icon" href="${context}/images/favicon.ico" type="image/x-icon">
<jwr:style src="/bundles/error.css" />
<script>window.contextPath = '${context}';</script>
</head>
<body>
    <div class="container" style="margin-top : 20%;">
       <img src="${context}/images/wechaterror.png" class="img-responsive center-block"/>
       <h2 class="text-center" translate="{{'${requestScope.error_msg}'}}"></h2>
    </div>
</body>
<jwr:script src="/bundles/error.js"/>
</html>
