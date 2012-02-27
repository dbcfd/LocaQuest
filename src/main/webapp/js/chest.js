// Adapted from TinyDoodle to perform selections, http://tinydoodle.com/
// Sets an attack type based on user selection of a canvas

chestDisplayInit = function() {
    // Collect elements from the DOM and set-up the canvas
    var canvas = $ ( '#chest_canvas' ) [0];
    var ctx = canvas.getContext ( '2d' );
    
    chestDisplayDraw ( canvas, ctx, '#888888' );
}

chestDisplayUpdate = function ( color ) {
    var canvas = $ ( '#chest_canvas' ) [0];
    var ctx = canvas.getContext ( '2d' );
    
    chestDisplayDraw ( canvas, ctx, color );
}

chestDisplayDraw = function ( canvas, ctx, color ) {
    var width = canvas.width;
    var height = canvas.height;
    var radius = 0.25 * ( width + height );
    
    //draw our canvas
    var grad = ctx.createLinearGradient ( 0, 0, width, height );
    grad.addColorStop ( 0, '#444444' );
    grad.addColorStop ( 1 / 2, color );
    grad.addColorStop ( 1, color );
    ctx.fillStyle = grad;
    ctx.arc ( width / 2, height / 2, radius, 0, 2 * Math.PI );
    ctx.fill();
    ctx.fillStyle = "#FFFFFF"
                    ctx.font = "24pt Calibri";
    ctx.fillText ( "Unlock Chest", width / 10, height / 2 );
}

chestLockInit = function ( domElem, color ) {
    // Collect elements from the DOM and set-up the canvas
    var canvas = $ ( domElem ) [0];
    var ctx = canvas.getContext ( '2d' );
    
    // Mouse based interface
    $ ( canvas ).bind ( 'mouseup', placeMarkerOnLock );
    
    // Touch screen based interface
    $ ( canvas ).bind ( 'touchend', placeMarkerOnLock );
    
    chestLockDraw ( canvas, ctx, color );
}

chestLockUpdate = function ( domElem, color ) {
    var canvas = $ ( domElem ) [0];
    var ctx = canvas.getContext ( '2d' );
    
    chestLockDraw ( canvas, ctx, color );
}

chestLockDraw = function ( canvas, ctx, color ) {
    var width = canvas.width;
    var height = canvas.height;
    
    //draw our canvas
    var grad = ctx.createLinearGradient ( 0, 0, 0, height );
    grad.addColorStop ( 0, '#444444' );
    grad.addColorStop ( 1 / 2, color );
    grad.addColorStop ( 1, color );
    ctx.fillStyle = grad;
    ctx.fillRect ( 0, 0, width, height );
}

placeMarkerOnLock = function ( event ) {
    var canvas = event.target
                 var context = canvas.getContext ( '2d' );
    var width = canvas.width;
    var height = canvas.height;
    
    var grad = context.fillStyle
               context.fillStyle = "#FFFFFF";
    context.fillRect ( 0, 0, width, height );
    context.fillStyle = grad;
    context.fillRect ( 0, 0, width, height );
    
    // Calculate the current mouse X, Y coordinates with canvas offset
    var posArray = findPos ( canvas )
                   var x = event.pageX - posArray[0]
                           var y = event.pageY - posArray[1]
                           
                                   context.fillStyle = "#000000"
                                           context.fillRect ( x, y, 10, 10 )
                                           
                                           context.fillStyle = grad
}
