markers = new Array();

poll = function() {
    $.ajax({ 
        url: "../async", 
        dataType: "json", 
        async: true, 
        cache: false, 
        timeout: 30000,
        success: function(data) {
            handleData(data);
        } 
    });
}
	
    handleData = function(data) {
            alert(data);
    }
	                   
$(document).ready(function() {
                    
    var position = new google.maps.LatLng(37.774947, -122.419414);
    
    var map = new google.maps.Map(
        document.getElementById("map"), 
        {
            zoom: 14,
            center: position, 
            mapTypeId: google.maps.MapTypeId.ROADMAP
        });

    var infoWindow = new google.maps.InfoWindow({});
    
    poll();

});

