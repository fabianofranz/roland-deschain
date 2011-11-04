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
        
        content.map = new google.maps.Map(
            document.getElementById(content.options.elementId), 
            {
                zoom: content.options.zoom,
                center: position, 
                mapTypeId: google.maps.MapTypeId.ROADMAP
            });
    },
    
    addMarker: function(latitude, longitude) {
        
        marker = new google.maps.Marker({
            position: new google.maps.LatLng(latitude, longitude),
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
    
    addTooltip: function(marker, username, thumbnailWidth, thumbnailHeight, thumbnailUrl) {
        
        tooltip = new google.maps.InfoWindow({});

        tooltip.setContent(tmpl("info", { 
            username: username, 
            width: thumbnailWidth, 
            height: thumbnailHeight, 
            thumbnail: thumbnailUrl }));

        tooltip.open(content.map, marker);
        
        content.tooltips.push(tooltip);
        
        return tooltip;
        
     },
     
     addItem: function(latitude, longitude, username, thumbnailWidth, thumbnailHeight, thumbnailUrl) {
         
        var key = username + ':' + latitude + ':' + longitude;

        if ($.inArray(key, content.keys) == -1) {

            content.keys.push(key);

            marker = content.addMarker(latitude, longitude);

            content.addTooltip(marker, username, thumbnailWidth, thumbnailHeight, thumbnailUrl);

        }
         
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
                            
                            content.addItem(instagram.location.latitude, 
                                instagram.location.longitude,
                                instagram.user.username, 
                                instagram.images.thumbnail.width, 
                                instagram.images.thumbnail.height, 
                                instagram.images.thumbnail.url);
                                
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

