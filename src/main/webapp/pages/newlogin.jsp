<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ include file="common.jsp"%>
<!doctype html>
<html ng-app="wechatBindApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=no">
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1" />
<meta name="description" content="">
<title>Polycom® {{'WECHAT_BIND.TITLE' | translate}}</title>
<link rel="shortcut icon" href="${context}/images/favicon.ico" type="image/x-icon">
<%--<link href="${context}/lib/bootstrap/bootstrap-3.3.6-dist/css/bootstrap.css" rel="stylesheet">--%>
<%--<link href="${context}/css/newlogin.css" rel="stylesheet">--%>
<%--<script type="text/javascript" src="${context}/lib/jquery/jquery-2.1.4/jquery-2.1.4.min.js"></script>--%>
<%--<script type="text/javascript" src="${context}/lib/bootstrap/bootstrap-3.3.6-dist/js/bootstrap.min.js"></script>--%>
	<jwr:style src="/bundles/wechatbind.css" />
<script>
	window.contextPath = '${context}';
	function getLatestLoginImage() {
	return "${context}/images/login_pic.png?v=" + new Date().getTime();
	}
</script>
</head>
<body>
	<div class="container hst-wechat-container container-responsive" ng-controller="wechatBindApp.bindCtrl">
	<div class="col-sm-5 left div_pic">
	<img src="${context}/images/login_pic.png" onload="this.onload=null; this.src=getLatestLoginImage();" style="width:445px;"/>
	</div>
		<div id="loginForm" style="margin-top: 50px;"
			class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
			<div>
				<div class="hst-wechat-panel-heading">
	               <div class="hst-wechat-logo"></div>
				</div>

				<div style="padding-top: 56px" class="panel-body">

					<div style="display: none" id="login-alert"
						class="alert alert-danger col-sm-12"></div>

					<form id="loginform" class="form-horizontal" role="form" ng-submit="processForm()" novalidate>
	                    <div class="input-set">
	                       <div class="input-group" ng-class="{'hasWechatError': showAccountError}">
	                          <span class="input-group-addon"><i class="glyphicon wechat-bind-user"></i></span>
	                          <input id="login-username" type="text" maxlength="25" class="form-control" name="account" autofocus="autofocus" autocomplete="off" placeholder="{{'WECHAT_BIND.PLACEHOLDER_ACCOUNT' | translate}}" ng-model="formData.account" ng-change="hideErrorMessage()">
	                          <span ng-show="!!formData.account && !!formData.account.trim()" ng-click="formData.account = '';focusOnTarget('login-username')" class="input-group-addon"><i class="glyphicon wechat-bind-del"></i></span>
	                       </div>
	                       <div class="input-border"></div>
	                       <div class="input-group" ng-class="{'hasWechatError': showPasswordError}">
	                         <span class="input-group-addon"><i class="glyphicon wechat-bind-lock"></i></span>
	                         <input id="login-password"type="password" maxlength="25" class="form-control" name="password" autocomplete="off" placeholder="{{'WECHAT_BIND.PLACEHOLDER_PASSWORD' | translate}}" ng-model="formData.password" ng-change="hideErrorMessage()">
	                         <input type="hidden" name="msurl" value="${requestScope.msurl}" ng-init="formData.msurl='${requestScope.msurl}'">
	                         <span ng-show="!!formData.password && !!formData.password.trim()" ng-click="formData.password = '';focusOnTarget('login-password')" class="input-group-addon"><i class="glyphicon wechat-bind-del"></i></span>
	                      </div>
	                    </div>
	                    <div class="hint-msg">
	                        <span translate="{{'WECHAT_BIND.HINT_MSG'}}"></span>
	                    </div>
						<div style="margin: 15px">
							<!-- Button -->
	                        <button type="submit" class="btn wechat-bind-btn" style="margin-bottom:20px;" translate="{{'WECHAT_BIND.BIND_BUTTON'}}" ng-disabled="disableBindBtn"></button>
						</div>
						<div class="form-group" style="width: 100%;">
							<div class="col-md-12 control">
								<div class="error_info" ng-show="showErrorMessage" ng-bind="loginErrorMessage"></div>
							</div>
						</div>
	                    <div class="clear"></div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<div id="toast" style="display: none; z-index: 9999;">

	<div class="weui_toast">
	<i class="weui_icon_toast"></i>
	<p class="weui_toast_content" translate="{{'WECHAT_BIND.SUCCESS_MSG'}}"></p>
	</div>
	</div>
</body>
	<jwr:script src="/bundles/wechatbind.js"/>
</html>