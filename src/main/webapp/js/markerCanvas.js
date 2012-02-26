// Adapted from TinyDoodle to perform selections, http://tinydoodle.com/
// Sets an attack type based on user selection of a canvas

markerCanvasInit = function() {
   // Collect elements from the DOM and set-up the canvas
   var canvas = $('#interact_canvas')[0];
   var context = canvas.getContext('2d');
   
   // Mouse based interface
   $(canvas).bind('mouseup', markerCanvasPlace);
    
   // Touch screen based interface
   $(canvas).bind('touchend', markerCanvasPlace);
   
   markerCanvasClear(canvas, context);
}

markerCanvasClear = function(canvas, ctx) {
   var width = canvas.width;
   var height = canvas.height;
   var centerX = width / 2;
   var centerY = height / 2;
   var innerRadius = width / 10;
   var outerRadius = width / 2;
   {
      var grad = ctx.createLinearGradient(centerX, 0, centerX, height);
      grad.addColorStop(0, '#0000FF');
      grad.addColorStop(3/10, '#0000AA');
      grad.addColorStop(1, '#F5CFFF');
      ctx.fillStyle=grad;
      ctx.fillRect(centerX, 0, width, height)
   }
   {
      var grad = ctx.createLinearGradient(0, 0, 0, height);
      grad.addColorStop(0, '#FF0000');
      grad.addColorStop(3/10, '#AA0000');
      grad.addColorStop(1, '#FFBFBF');
      ctx.fillStyle=grad;
      ctx.fillRect(0,0,centerX,height)
   }
   var imgObj = new Image();
   imgObj.src = '/images/combat_overlay.png';
	imgObj.onload = function () {			
		// Draw the image on the canvas
		ctx.drawImage(imgObj, 0, 0, width, height);
	}
}
   
markerCanvasPlace = function(event) {
   var canvas = event.target
   var context = canvas.getContext('2d');
   var radius = canvas.width;
   
   markerCanvasClear(canvas, context);
   
   // Calculate the current mouse X, Y coordinates with canvas offset
   var posArray=findPos(canvas)
   var x = event.pageX - posArray[0]
   var y = event.pageY - posArray[1]
   
   context.fillStyle="#000000"
   context.fillRect(x,y,10,10)
}


greyOutCanvas = function(canvas) {
   var context = canvas.getContext('2d');
   
   var width = canvas.width;
   var height = canvas.height;
   
   context.fillStyle = "#888888";
   context.fillRect(0,0,width,height);
}

enableMarkerCanvas = function() {
   var canvas = $('#interact_canvas')[0];
   var context = canvas.getContext('2d');
   
   markerCanvasClear(canvas, context);
}

disableMarkerCanvas = function(evt, timerDuration) {
   var canvas = event.target
   
   greyOutCanvas(canvas)
   
   var fireEnableCanvas = setTimeout(enableMarkerCanvas,timerDuration);

   return getJsonClickMessage(evt)
}
