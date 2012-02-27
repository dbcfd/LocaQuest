// Adapted from TinyDoodle to perform selections, http://tinydoodle.com/
// Sets an attack type based on user selection of a canvas

displayBarInit = function ( domName, fillColor ) {
    // Collect elements from the DOM and set-up the canvas
    var canvas = $ ( domName ) [0];
    var context = canvas.getContext ( '2d' );
    
    var width = canvas.width;
    var height = canvas.height;
    {
        var grad = context.createLinearGradient ( 0, 0, width, height * 2 );
        grad.addColorStop ( 0, fillColor );
        grad.addColorStop ( 1, 'black' );
        context.fillStyle = grad;
        context.fillRect ( 0, 0, width, height );
    }
}

updateBarPosition = function ( domName, level ) {
    try {
        var canvas = $ ( domName ) [0]
                     var context = canvas.getContext ( '2d' );
        var height = canvas.height
                     var width = canvas.width
                     
                                 var grad = context.fillStyle
                                            context.fillStyle = "#FFFFFF";
        context.fillRect ( 0, 0, width, height )
        context.fillStyle = grad;
        context.fillRect ( 0, Math.floor ( height - ( height * level ) ), width, height )
    } catch ( err ) {
        alert ( "caught exception: " + err.message )
    }
}
