markers = new Array();
	
$(document).ready(function() {
                    
    var position = new google.maps.LatLng(37.774947, -122.419414);
    var map = new google.maps.Map(
        document.getElementById("map_canvas"), 
        {
            zoom: 14, 
            center: position, 
            mapTypeId: google.maps.MapTypeId.ROADMAP
        });
    var infoWindow = new google.maps.InfoWindow({});

    $.stream("./async", {
        type: "http",
        dataType: "json",
        context: $("#map-canvas")[0],
        open: function(event, stream) {
            stream.send({});
        },
        message: function(event) {
            $.each(event.data, function(index1, update) { 
                if (update.object == "geography") {
                    $.getJSON("./details?object=" + update.object_id, function(instagrams) {
                        $.each(instagrams.data, function(index2, instagram) {
                            var key = instagram.user.username + ':' + instagram.location.latitude + ':' + instagram.location.longitude;
                            if ($.inArray(key, markers) == -1) {
                                markers.push(key);
                                var p = new google.maps.LatLng(instagram.location.latitude, instagram.location.longitude);
                                var marker = new google.maps.Marker({
                                    position: p, 
                                    title: instagram.user.username
                                });
                                marker.setMap(map);
                                infoWindow.setContent("<strong>" + instagram.user.username + "</strong> just saw:<br /><img style=\"width:" + instagram.images.thumbnail.width + "px;height:" + instagram.images.thumbnail.height + "px;\" src=\"" + instagram.images.thumbnail.url + "\" />");
                                infoWindow.open(map, marker);
                            }

                        });
                    });
                }
            });
        },
        error: function() {
            alert("error");
        },
        close: function() {
        }
    });
	                   
});


