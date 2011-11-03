markers = new Array();

poll = {
    start: function() {
        $.get("../async", function(data) {
            poll.handle();
            handleData(data);
        });
    },
    handle: function() {
        $.ajax({ 
            url: "../async", 
            dataType: "json",
            type: "POST",
            async: true, 
            cache: false, 
            timeout: 30000,
            success: function(data) {
                poll.start();
            } 
        });
    }
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
    
    poll.start();

});

