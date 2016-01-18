<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ include file="common.jsp" %>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1" />
<meta name="description" content="">
<title>Polycom® 汇视通微信绑定页面</title>
<link rel="shortcut icon" href="${context}/images/favicon.ico" type="image/x-icon">
<%-- <link rel="stylesheet" type="text/css" media="screen" href="${context}/lib/bootstrap/bootstrap-3.3.6-dist/css/bootstrap.css">
<link rel="stylesheet" type="text/css" media="screen" href="${context}/css/login.css"> --%>
<jwr:style src="/bundles/wechatbind.css" />
<%-- <script type="text/javascript" src="${context}/lib/jquery/jquery-2.1.4/jquery-2.1.4.min.js"></script> --%>
<script>
	window.contextPath = '${context}';
	function getLatestLoginImage() {
		return "${context}/images/login_pic.png?v=" + new Date().getTime();
	}
</script>
</head>
<body ng-app="wechatBindApp">
	<div class="container" id="page_login" ng-controller="wechatBindApp.bindCtrl">
        <div class="col-sm-5 left div_pic">
            <img src="${context}/images/login_pic.png" onload="this.onload=null; this.src=getLatestLoginImage();" style="width:445px;"/>
        </div>
		<div class="col-sm-6 right div_loginform">
			<br /> <br /> <br />
			<form id="loginForm" class="form-horizontal" role="form" ng-submit="processForm()" novalidate>
				<div class="form-group col-sm-9" ng-class="{'has-error': showAccountError}">
					<label for="account" class="control-label">User ID</label> 
					<input type="text" name="account" placeholder="domain\username" class="form-control" maxlength="128" autofocus="autofocus" autocomplete="off" ng-model="formData.account" ng-change="hideErrorMessage()">
				</div>
				<div class="form-group col-sm-9" ng-class="{'has-error': showPasswordError}">
					<label for="password" class="control-label">Password</label>
                    <input type="password" name="password" placeholder="Password" class="form-control" autocomplete="off" ng-model="formData.password" ng-change="hideErrorMessage()">
                    <input type="hidden" name="msurl" value="${requestScope.msurl}" ng-init="formData.msurl='${requestScope.msurl}'">
				</div>
				<div class="form-group col-sm-9">
				    <button type="submit" class="btn btn-danger" style="margin-bottom:20px;">Bind Wechat</button>
				</div>
				<div class="form-group col-sm-9">
				    <div class="error_info" ng-show="showErrorMessage" ng-bind="loginErrorMessage"></div>
				</div>
				<div class="clear"></div>
			</form>
		</div>
	</div>
</body>
<%-- <script type="text/javascript" src="${context}/lib/angularjs/angular-1.5.0-rc.0/angular.min.js"></script>
<script type="text/javascript" src="${context}/js/common.js"></script>
<script type="text/javascript" src="${context}/js/login.js"></script> --%>
<jwr:script src="/bundles/wechatbind.js"/> 
</html>
