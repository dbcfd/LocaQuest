findPos = function(obj) {
   var posX = obj.offsetLeft;var posY = obj.offsetTop;
   while(obj && obj.offsetParent){
      obj=obj.offsetParent
      posX=posX+obj.offsetLeft;
      posY=posY+obj.offsetTop;
   }
   var posArray=[posX,posY]
   return posArray;
}

getRelativeCoords = function(evt) {
   var canvas = event.target
   
   // Calculate the current mouse X, Y coordinates with canvas offset
   var posArray=findPos(canvas)
   var x = event.pageX - posArray[0]
   var y = event.pageY - posArray[1]
   
   var coordArray=[x,y]
   return coordArray
}

getJsonClickMessage = function(evt) {
	var canvas = event.target
	var coordArray = getRelativeCoords(evt)
   
	var jsonMsg = "{\"canvas\" : {" +
		"\"height\" : " + canvas.height + ", \"width\" : " + canvas.width +
		"}, \"click\" : {" + 
		"\"x\" : " + coordArray[0] + ", \"y\" : " + coordArray[1] +
		"} }";
   
   return jsonMsg
}
