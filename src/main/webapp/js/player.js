var playerStats = null;

playerDisplay = function ( foursquareId ) {
    playerStats = playerStats || (
    $.ajax ( {
        type: "GET",
        url: "/player/" + foursquareId,
    } ).done ( function ( msg ) {
        alert ( "Data: " + msg );
    } );
}
}
