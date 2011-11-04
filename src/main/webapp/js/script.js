content = {
    
    map: null,
    options: {
        latitude: 40.778281,
        longitude: -73.969878,
        elementId: "map",
        zoom: 14
    },
    keys: new Array(),
    markers: new Array(),
    tooltips: new Array(),
    
    setup: function() {
        
        var position = new google.maps.LatLng(content.options.latitude, content.options.longitude);
        
        map = new google.maps.Map(
            document.getElementById(content.options.elementId), 
            {
                zoom: content.options.zoom,
                center: position, 
                mapTypeId: google.maps.MapTypeId.ROADMAP
            });
    },
    
    addMarker: function(location) {
        
        marker = new google.maps.Marker({
            position: location,
            map: content.map
        });
        
        content.markers.push(marker);
        
        return marker;
    },
    
    clearMarkers: function() {
        if (content.markers) for (i in content.markers) content.markers[i].setMap(null);
    },
    
    deleteMarkers: function() {
        content.clearMarkers();
        content.markers.length = 0;
    },
    
    addTooltip: function(info, marker) {
        
        tooltip = new google.maps.InfoWindow({});

        tooltip.setContent(tmpl("info", { 
            username: info.user.username, 
            width: info.images.thumbnail.width, 
            height: info.images.thumbnail.height, 
            thumbnail: info.images.thumbnail.url }));

        tooltip.open(content.map, marker);
        
        content.tooltips.push(tooltip);
        
        return tooltip;
        
     }
     
}

	
$(document).ready(function() {
    
    content.setup();
                    
    $.stream.setup({enableXDR: true});

    $.stream("../async", {
        type: "http",
        dataType: "json",
        context: $("#map")[0],
        open: function(event, stream) {
            stream.send({});
        },
        message: function(event) {
            $.each(event.data, function(index1, update) { 
                if (update.object == "geography") {
                    $.getJSON("../details?object=" + update.object_id, function(instagrams) {
                        $.each(instagrams.data, function(index2, instagram) {
                            
                            var key = instagram.user.username + ':' + instagram.location.latitude + ':' + instagram.location.longitude;
                            
                            if ($.inArray(key, content.keys) == -1) {
                                
                                content.keys.push(key);
                                
                                marker = content.addMarker(new google.maps.LatLng(instagram.location.latitude, instagram.location.longitude));
                                
                                content.addTooltip(marker, instagram);
                                
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

