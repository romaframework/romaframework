var romaChangedFields;

romaChangedFields = new Array();

var lastFocusName;

var janiculumFrontendLocked = false;
var janiculumFrontendLockedCount = 0;

var romaRemoveArray = new Object();

function romaAddRemove(id,func) {
	romaRemoveArray[id]=func;
}

function romaOnRemove(id){
	for(var index in romaRemoveArray){
		if(index && index.indexOf(id) === 0 && romaRemoveArray[index] instanceof Function){
			romaRemoveArray[index]();
			delete romaRemoveArray[index];
		}
	}
}

function isPageLocked(){
	return janiculumFrontendLocked;
}

function lockPage(iLock){
	var zIndex = -1000;
	var left = "-10000px";
	var top = "-10000px";
	if(iLock){
		zIndex=2000;
		left = "0px";
		var topOffset = 0;
		if(window.pageYOffset){
			topOffset = window.pageYOffset;
		}
		top = ""+topOffset+"px";
	}
	try{
		var divId = "#janiculumWaitDiv";
		var div = null;
		if(jQuery(divId).length > 0){
			jQuery(divId).css("z-index", ""+zIndex);
			jQuery(divId).css("left", ""+left);
			jQuery(divId).css("top", ""+top);
		}
	}catch(exc){
	}
	
	janiculumFrontendLocked = iLock;
}


function contains(a, obj) {
	  var i = a.length;
	  while (i--) {
	    if (a[i] == obj) {
	      return true;
	    }
	  }
	  return false;
	}


function romaFieldChanged(elementName){
	lastFocusName=elementName;
	romaAddChange(elementName);
}

function romaMultiSelectChanged(elementName){
	lastFocusName=elementName;
	var oneSelected = false;
	var checkPrefix = elementName.substring(0,elementName.lastIndexOf("_"));
	for(var i=0; i<1000000; i++){
		var checkName = checkPrefix+"_"+i;
		var elems = document.getElementsByName(checkName);
		if(elems.length==0){
			break;
		}
		var check = elems[0];
		if(check.checked){
			oneSelected = true;
			romaAddChange(checkName);
		}
	}
	if(!oneSelected){
		romaAddChange(checkPrefix+"_-1");
	}
}

function romaAction(actionName){
	lastFocusName=actionName;
	romaAddChange(actionName);
	romaSendAjaxRequest();
}

function romaEvent(elementName, eventName, params){
	var parName="(PojoEvent)_"+elementName+"_"+eventName;
	var request =romaBuildAjaxRequestData();
	if(params == undefined){
		request[parName]="event";// :P :P :P
	}else if (params instanceof Array){
		for(var i =0;i<params.length;i++){
			params[i] = i+"_"+params[i];
		}
		request[parName] = params;
	}
	
	romaSendAjaxRequestData(request, true,true);
}

function romaAddChange(name){
	if(!(contains(romaChangedFields, name))){
		romaChangedFields[romaChangedFields.length]=name;
	}
}


function romaBuildAjaxRequestData(){
	var request = new Object();
	for(var i=0; i<romaChangedFields.length; i++){
		var name=romaChangedFields[i];
		var elemList = document.getElementsByName(name);
		request["ajax"]="true";// needed for portlets...
		if((elemList==null|| elemList.length==0) && (name.match("PojoEvent")!=null || name.match("PojoAction")!=null)){
			request[name] = "event";
		}else if(elemList.length==1){
			var element = elemList[0];
			if(element.type=="checkbox"){
				if(element.checked){
					request[name] = "1";
				}
			}else{
				value = elemList[0].value;	
				request[name] = value;
			}
		}else{
			for(var j=0; j<elemList.length; j++){
				var elem = elemList[j];
				if(elem.checked){
					value = elem.value;	
					request[name] = value;
				}
			}
		}
	}	
	return request;
}

function handlePushCommands(data){
	var requestPath = "";
	if(typeof(requestPathPrefix)!='undefined'){
		requestPath = requestPathPrefix; 
	}
	if(typeof(requestContextPath)!='undefined'){
		requestPath = requestContextPath;
	}
	if(data["pushDownloadStream"]!=null){
		var url = requestPath+"downloadstream/"+data["pushDownloadStream"];
		window.location.href =url;
	}
	if(data["pushDownloadReader"]!=null){
		var url = requestPath+"downloadstream/"+data["pushDownloadReader"];
		window.location.href =url;
	}
	if(data["pushDownloadReport"]!=null){
		var url = requestPath+"downloadstream/"+data["pushDownloadReport"];
		window.location.href =url;
	}
	if(data["pushOpenWindow"]!=null){
		var pushData = data["pushOpenWindow"];
		if(pushData["name"]!=null && pushData["options"]!=null){
			window.open(pushData["location"], pushData["name"], pushData["options"]);
		}else if(pushData["name"]!=null){
			window.open(pushData["location"], pushData["name"], '');
		}else{
			window.open(pushData["location"], '_blank', '');
		}
	}
	if(data["pushRedirectView"]!=null){
		var pushData = data["pushRedirectView"];
		window.open(pushData["location"], "_top", '');
	}
}



/*
 * 
 * @param data the call result - {status: "ok"|"reload", pageId: "pageId",
 * changes:{id:"html snippet"}}
 * 
 */

function romaOnSuccess(data, textStatus) {
	romaRespStatus = data["status"];
	if(romaRespStatus=="stop"){
		alert("The session is expired. To reconnect refresh your browser.");
		window.location.href = window.location.href;		// reload page
	} if(romaRespStatus=="reload"){
		window.location.href = window.location.href;		// reload page
	}else if(romaRespStatus="ok"){
		var changes = data["changes"];
		for(var i in changes){
			romaOnRemove(i);
			jQuery('#'+i).after(changes[i]).remove();
		}
		if(data["bindingExecuted"]){				
			romaChangedFields = new Array();
		}else{
			var oldRomaChanged = romaChangedFields;
			romaChangedFields = new Array();
			var index = 0;
			for(var i=0; i<oldRomaChanged.length; i++){
				if(!(oldRomaChanged[i].substring(0, "(PojoAction)".length) == "(PojoAction)") && !(oldRomaChanged[i].substring(0, "(PojoEvent)".length) == "(PojoEvent)")){
					romaChangedFields[index] = oldRomaChanged[i];
				}
			}
		}
		eval(data["romajs"]);
	}
	handlePushCommands(data);
	if(janiculumFrontendLockedCount > 0)janiculumFrontendLockedCount--;
	else lockPage(false);
	document.body.style.cursor='default';
	if(lastFocusName!=null){
		var tmp = document.getElementsByName(lastFocusName);
		if(tmp!=null && tmp.length>0){
			tmp[0].focus();
		}
	}
}

function romaSendAjaxRequest(force){
	romaSendAjaxRequestData(romaBuildAjaxRequestData(),force);
}

function romaSendAjaxRequestData(data,force,wait,onResponseOk){
	if(isPageLocked()){
		if(force==true)
			janiculumFrontendLockedCount++;
		else
			return;
	}
	if(wait==undefined || wait == true){
		lockPage(true);
	}
	document.body.style.cursor='wait';
	var requestPath = "ajax";
	if(typeof(requestPathPrefix)!='undefined'){
		requestPath = requestPathPrefix; 
	}
	if(typeof(requestContextPath)!='undefined'){
		requestPath = requestContextPath +"ajax";
	}
	var succesFunction=romaOnSuccess;
	if(onResponseOk) {
		succesFunction =function(data, textStatus){romaOnSuccess(data, textStatus);onResponseOk(data, textStatus);};
	}
	jQuery.ajax({ type: 'POST', url: requestPath, data:data, contentType: "application/x-www-form-urlencoded; " + globalCharType, success: succesFunction, 
	dataType: "json"});
}

function reloadBaseJs(pageId){
	var context = "";
	if(typeof(requestContextPath)!='undefined'){
		context = requestContextPath; 
	}
	var newSrc = context+"roma.js?pageId="+pageId+"&timestamp="+(new Date()).getTime();
	// alert("cambio js "+pageId+ " "+newSrc);
	var scriptTag = document.createElement("script");
	scriptTag.setAttribute("type", "text/javascript");
	scriptTag.setAttribute("src", newSrc);
	scriptTag.setAttribute("id", "romajszz");
	// alert(jQuery('#romajs'));
	jQuery('#romajs').after(scriptTag);
}


function romaSendPollAjaxRequest(){
	romaSendAjaxRequestData(new Object(),false,false,function(data,status){if(data["status"]!="stop")startPushCommandPolling();});
}

function startPushCommandPolling(){
	window.setTimeout("romaSendPollAjaxRequest()",1000);
}

function changeTab(fieldName,id){
	document.getElementsByName(fieldName)[0].value = fieldName+"_"+id;
	romaFieldChanged(fieldName);
	romaSendAjaxRequest(true);
}

// POLLER THREAD FOR PUSH COMMANDS
//startPushCommandPolling();
