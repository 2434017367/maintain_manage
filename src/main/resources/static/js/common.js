//jqGrid的配置信息

// var baseURL = "../../";

var baseURL = "../"

//工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost:8080/index.html?id=123
// T.p('id') --> 123;
var url = function(name) {
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null)return  unescape(r[2]); return null;
};
T.p = url;


//重写alert
window.alert = function(msg, callback){
	parent.layer.alert(msg, function(index){
		parent.layer.close(index);
		if(typeof(callback) === "function"){
			callback("ok");
		}
	});
}

//重写confirm式样框
window.confirm = function(msg, callback){
	parent.layer.confirm(msg, {btn: ['确定','取消']},
	function(){//确定事件
		if(typeof(callback) === "function"){
			callback("ok");
		}
	});
}

//选择一条记录
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
    var selectedIDs = grid.getGridParam("selarrrow");
    if(selectedIDs.length > 1){
    	alert("只能选择一条记录");
    	return ;
    }
    
    return selectedIDs[0];
}

//选择多条记录
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
    return grid.getGridParam("selarrrow");
}

//判断是否为空
function isBlank(value) {
	if(value == "undefined" || value == null || value == ""){
		return true;
	}else{
		return false;
	}
}

function isNotBlank(value) {
	return !isBlank(value);
}

// 获取url中的参数
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = decodeURI(window.location.search).substr(1).match(reg);
	if(r != null) return (r[2]);
	return null;
}



// 校验

// 用户名校验
function isUsername(value) {
	if(isBlank(value)){
		return "用户名不能为空";
	}else{
		// 6到12位 数字 字母 下划线组成
		var reg = /^(\w){6,12}$/;
		if(!reg.test(value)){
			return "用户名格式为6到12位字母、数字、下划线组成";
		}else{
			return "";
		}
	}
}

// 密码校验
function isPassword(value) {
	if(isBlank(value)){
		return "密码不能为空";
	}else{
		// 6到12位 数字 字母 下划线组成
		var reg = /^[a-zA-Z0-9\.]{6,12}$/;
		if(!reg.test(value)){
			return "密码格式为6到12位字母、数字、小数点组成";
		}else{
			return "";
		}
	}
}

function isEmail(value) {
	if(isBlank(value)){
		return "邮箱不能为空";
	}else{
		var reg = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
		if(!reg.test(value)){
			return "邮箱格式为错误";
		}else{
			return "";
		}
	}
}

function isVCode(value) {
	if(isBlank(value)){
		return "验证码不能为空";
	}else{
		var reg = /^[0-9]{6}$/;
		if(!reg.test(value)){
			return "验证码格式为错误";
		}else{
			return "";
		}
	}
}


function isWorkNumber(value) {
	if(isBlank(value)){
		return "工号不能为空";
	}else{
		var reg = /^1[0-9]{5}$/;
		if(!reg.test(value)){
			return "工号格式为错误";
		}else{
			return "";
		}
	}
}