content = {
    
    map: null,
    tooltip: null,
    options: {
        latitude: 40.778281,
        longitude: -73.969878,
        elementId: "map",
        zoom: 14
    },
    ids: new Array(),
    markers: new Array(),
    
    setup: function() {
        
        var position = new google.maps.LatLng(content.options.latitude, content.options.longitude);
        
        content.map = new google.maps.Map(
            document.getElementById(content.options.elementId), 
            {
                zoom: content.options.zoom,
                center: position, 
                mapTypeId: google.maps.MapTypeId.ROADMAP
            });
            
        tooltip = new google.maps.InfoWindow({});
        
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
    
     showItem: function(item) {
         
        if ($.inArray(item.id, content.ids) == -1) {

            content.ids.push(item.id);
            marker = content.addMarker(item.data.latitude, item.data.longitude);
            tooltip.setContent(tmpl("info", item.data));
            tooltip.open(content.map, marker);
        
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
                            
                            content.showItem({ 
                                id: instagram.id,
                                data: {
                                    latitude: instagram.location.latitude, 
                                    longitude: instagram.location.longitude,
                                    username: instagram.user.username, 
                                    thumbnailWidth: instagram.images.thumbnail.width, 
                                    thumbnailHeight: instagram.images.thumbnail.height, 
                                    thumbnailUrl: instagram.images.thumbnail.url,
                                    url: instagram.link
                                }
                            });
                                
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

